# carlos-apm

## 模块简介

`carlos-apm` 是 Carlos 框架的应用性能监控（APM）集成模块，提供了对 SkyWalking 9.7.0 和 Spring Cloud Sleuth（基于
Brave）的集成支持。该模块实现了分布式追踪、链路监控、性能指标收集等功能，帮助开发者和运维人员监控微服务系统的运行状态。

## 主要功能

### 1. 分布式追踪支持

集成 Spring Cloud Sleuth（基于 Brave）提供分布式追踪能力，自动为每个请求生成唯一的 Trace ID 和 Span ID：

```java
import com.carlos.apm.TraceUtil;

// 获取当前请求的 Trace ID
String traceId = TraceUtil.getTraceId();

// 获取当前 Span 的 ID
String spanId = TraceUtil.getSpanId();

// 在日志中记录追踪信息
log.info("[TraceId: {}] 用户操作记录", traceId);
```

### 2. SkyWalking 集成

集成 Apache SkyWalking 9.7.0，提供完整的 APM 功能：

- **分布式追踪**：跨服务的调用链路追踪
- **服务拓扑图**：可视化展示服务间依赖关系
- **性能指标**：响应时间、吞吐量、错误率等
- **日志关联**：日志与追踪信息自动关联
- **告警监控**：基于规则的异常告警

### 3. 追踪工具类

提供 `TraceUtil` 工具类，方便在代码中获取追踪信息：

```java
/**
 * 获取当前 Trace ID
 */
public static String getTraceId() {
    Tracer tracer = SpringUtil.getBean(Tracer.class);
    Span currentSpan = tracer.currentSpan();
    if (currentSpan != null) {
        return currentSpan.context().traceIdString();
    }
    return "";
}

/**
 * 获取当前 Span ID
 */
public static String getSpanId() {
    Tracer tracer = SpringUtil.getBean(Tracer.class);
    Span currentSpan = tracer.currentSpan();
    if (currentSpan != null) {
        return currentSpan.context().spanIdString();
    }
    return "";
}
```

### 4. 日志与追踪关联

通过 SkyWalking Logback 插件，自动将追踪信息注入到日志中：

```xml
<!-- logback-spring.xml 配置示例 -->
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout">
                <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%tid] [%thread] %-5level %logger{36} - %msg%n</pattern>
            </layout>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
</configuration>
```

日志输出格式：

```
2023-01-01 12:00:00.123 [TID:1234567890abcdef] [main] INFO  com.carlos.controller.UserController - 用户查询成功
```

### 5. 自定义追踪标签

支持在代码中添加自定义追踪标签，便于业务监控：

```java
import brave.Tracer;
import brave.Span;
import cn.hutool.extra.spring.SpringUtil;

// 添加自定义标签到当前 Span
Tracer tracer = SpringUtil.getBean(Tracer.class);
Span currentSpan = tracer.currentSpan();
if (currentSpan != null) {
    currentSpan.tag("user.id", "123");
    currentSpan.tag("operation.type", "CREATE");
    currentSpan.tag("business.module", "USER_MANAGEMENT");
}

// 或者使用注解方式（需结合自定义AOP）
@TraceTag(key = "user.id", value = "#user.id")
public Result<User> createUser(@RequestBody User user) {
    // 业务逻辑
}
```

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-apm</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 2. SkyWalking 配置

在 `application.yml` 中配置 SkyWalking Agent：

```yaml
# SkyWalking 配置
spring:
  application:
    name: user-service  # 服务名称，用于在SkyWalking中标识

# SkyWalking Agent 配置（通过环境变量或JVM参数）
# -javaagent:/path/to/skywalking-agent.jar
# -Dskywalking.agent.service_name=${spring.application.name}
# -Dskywalking.collector.backend_service=localhost:11800
```

或者通过环境变量配置：

```bash
# Linux/Mac
export SW_AGENT_NAME=user-service
export SW_AGENT_COLLECTOR_BACKEND_SERVICES=localhost:11800

# Windows
set SW_AGENT_NAME=user-service
set SW_AGENT_COLLECTOR_BACKEND_SERVICES=localhost:11800
```

### 3. Sleuth 配置

Spring Cloud Sleuth 自动配置，无需额外配置：

```yaml
spring:
  sleuth:
    enabled: true
    sampler:
      probability: 1.0  # 采样率，1.0表示100%采样，生产环境可适当降低

  cloud:
    sleuth:
      propagation:
        # 传播类型，支持B3、W3C等
        type: B3
```

### 4. 基本使用示例

#### 在控制器中使用追踪信息：

```java
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        // 获取当前追踪信息
        String traceId = TraceUtil.getTraceId();
        String spanId = TraceUtil.getSpanId();

        log.info("[TraceId: {}, SpanId: {}] 查询用户信息，用户ID: {}", traceId, spanId, id);

        User user = userService.getUserById(id);

        // 添加业务标签到追踪
        Tracer tracer = SpringUtil.getBean(Tracer.class);
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null && user != null) {
            currentSpan.tag("user.id", user.getId().toString());
            currentSpan.tag("user.name", user.getUsername());
        }

        return Result.ok(user);
    }

    @PostMapping("/create")
    public Result<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        String traceId = TraceUtil.getTraceId();
        log.info("[TraceId: {}] 创建用户，用户名: {}", traceId, request.getUsername());

        User user = userService.createUser(request);

        // 记录业务操作
        log.info("[TraceId: {}] 用户创建成功，用户ID: {}", traceId, user.getId());

        return Result.ok(user);
    }
}
```

#### 在服务层使用追踪：

```java
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderService orderService;

    @Override
    public User getUserById(Long id) {
        String traceId = TraceUtil.getTraceId();

        log.debug("[TraceId: {}] 开始查询用户，ID: {}", traceId, id);

        // 模拟跨服务调用
        List<Order> orders = orderService.getUserOrders(id);
        log.debug("[TraceId: {}] 获取用户订单数量: {}", traceId, orders.size());

        User user = userMapper.selectById(id);

        log.debug("[TraceId: {}] 用户查询完成，用户名: {}", traceId,
                 user != null ? user.getUsername() : "null");

        return user;
    }

    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {
        String traceId = TraceUtil.getTraceId();

        // 记录详细的操作步骤
        log.info("[TraceId: {}] 开始创建用户，用户名: {}", traceId, request.getUsername());

        // 参数校验
        if (userMapper.existsByUsername(request.getUsername())) {
            log.warn("[TraceId: {}] 用户名已存在: {}", traceId, request.getUsername());
            throw new BusinessException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setCreateTime(LocalDateTime.now());

        int result = userMapper.insert(user);

        if (result > 0) {
            log.info("[TraceId: {}] 用户创建成功，用户ID: {}", traceId, user.getId());

            // 添加追踪标签
            Tracer tracer = SpringUtil.getBean(Tracer.class);
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                currentSpan.tag("created.user.id", user.getId().toString());
                currentSpan.tag("created.user.name", user.getUsername());
                currentSpan.annotate("User creation completed");
            }
        } else {
            log.error("[TraceId: {}] 用户创建失败", traceId);
        }

        return user;
    }
}
```

#### 日志配置示例（logback-spring.xml）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- SkyWalking TraceId 布局 -->
    <property name="LOG_PATTERN"
              value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%tid] [%thread] %-5level %logger{36} - %msg%n" />

    <!-- 控制台输出 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <!-- 文件输出 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/application.log</file>
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/application.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <!-- 异步输出 -->
    <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="FILE" />
        <queueSize>512</queueSize>
        <discardingThreshold>0</discardingThreshold>
    </appender>

    <!-- SkyWalking GRPC 日志上报 -->
    <appender name="GRPC_LOG" class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.log.GRPCLogClientAppender">
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout">
                <pattern>${LOG_PATTERN}</pattern>
            </layout>
        </encoder>
    </appender>

    <!-- 日志级别配置 -->
    <logger name="com.carlos" level="DEBUG" />
    <logger name="org.springframework" level="INFO" />
    <logger name="org.apache.skywalking" level="INFO" />

    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="ASYNC_FILE" />
        <appender-ref ref="GRPC_LOG" />
    </root>
</configuration>
```

## 详细功能说明

### 1. 追踪信息传播

支持多种追踪信息传播格式：

- **B3格式**：Zipkin 标准的传播格式
- **W3C TraceContext**：W3C 标准的传播格式
- **SkyWalking**：SkyWalking 专有格式
- **Jaeger**：Jaeger 传播格式

配置示例：

```yaml
spring:
  cloud:
    sleuth:
      propagation:
        type: B3,W3C  # 支持多种格式，按顺序尝试
```

### 2. 采样率控制

支持按概率采样，控制追踪数据量：

```yaml
spring:
  sleuth:
    sampler:
      probability: 0.1  # 10%的请求会被追踪，适用于高并发场景

# 或者使用速率限制采样
sampler:
  rate: 100  # 每秒最多采样100个请求
```

### 3. 自定义追踪过滤器

可以自定义追踪过滤器，控制哪些请求需要追踪：

```java
@Configuration
public class TraceFilterConfig {

    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilter() {
        FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TraceFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    public static class TraceFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // 排除健康检查等不需要追踪的端点
            String path = httpRequest.getRequestURI();
            if (path.startsWith("/actuator/health") || path.startsWith("/favicon.ico")) {
                chain.doFilter(request, response);
                return;
            }

            // 继续处理
            chain.doFilter(request, response);
        }
    }
}
```

### 4. 业务追踪标签

支持为业务操作添加自定义标签，便于后续分析和查询：

```java
@Component
public class BusinessTracer {

    @Autowired
    private Tracer tracer;

    /**
     * 为当前 Span 添加业务标签
     */
    public void addBusinessTag(String key, String value) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag("business." + key, value);
        }
    }

    /**
     * 记录业务事件
     */
    public void recordBusinessEvent(String eventName, Map<String, String> attributes) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.annotate("business.event." + eventName);
            if (attributes != null) {
                attributes.forEach((key, value) ->
                    currentSpan.tag("business.event." + eventName + "." + key, value));
            }
        }
    }

    /**
     * 开始业务操作
     */
    public Span startBusinessOperation(String operationName) {
        return tracer.nextSpan()
            .name("business." + operationName)
            .tag("business.operation.start.time", LocalDateTime.now().toString())
            .start();
    }
}
```

### 5. 性能指标收集

集成 Micrometer 和 SkyWalking，自动收集以下指标：

- **HTTP 请求指标**：响应时间、状态码、吞吐量
- **数据库指标**：SQL 执行时间、连接池状态
- **缓存指标**：Redis 操作时间、命中率
- **JVM 指标**：内存使用、GC 时间、线程状态
- **自定义业务指标**：业务操作次数、成功率

## 配置项说明

### 1. SkyWalking 配置项

| 配置项  | 环境变量                                  | 默认值                  | 说明                   |
|------|---------------------------------------|----------------------|----------------------|
| 服务名称 | `SW_AGENT_NAME`                       | -                    | 在 SkyWalking 中显示的服务名 |
| 后端地址 | `SW_AGENT_COLLECTOR_BACKEND_SERVICES` | `127.0.0.1:11800`    | SkyWalking OAP 服务器地址 |
| 采样率  | `SW_AGENT_SAMPLE`                     | `10000`              | 采样率，每 N 个请求采样 1 个    |
| 日志级别 | `SW_LOGGING_LEVEL`                    | `INFO`               | Agent 日志级别           |
| 日志文件 | `SW_LOGGING_FILE_NAME`                | `skywalking-api.log` | 日志文件路径               |

### 2. Sleuth 配置项

| 配置项                                    | 类型      | 默认值    | 说明            |
|----------------------------------------|---------|--------|---------------|
| `spring.sleuth.enabled`                | boolean | `true` | 是否启用 Sleuth   |
| `spring.sleuth.sampler.probability`    | double  | `1.0`  | 采样概率（0.0-1.0） |
| `spring.cloud.sleuth.propagation.type` | String  | `B3`   | 传播类型          |
| `spring.sleuth.web.enabled`            | boolean | `true` | 是否启用 Web 追踪   |
| `spring.sleuth.messaging.enabled`      | boolean | `true` | 是否启用消息队列追踪    |

### 3. 日志配置项

| 配置项       | 说明                                                              |
|-----------|-----------------------------------------------------------------|
| `%tid`    | SkyWalking Trace ID（需要在 layout 中使用 TraceIdPatternLogbackLayout） |
| `%TID`    | 大写的 Trace ID                                                    |
| `%sw_ctx` | 完整的 SkyWalking 上下文                                              |

## 依赖项

- `carlos-spring-boot-core`：基础工具类
- `micrometer-tracing-bridge-brave`：Spring Cloud Sleuth（Brave 实现）
- `apm-toolkit-trace`：SkyWalking 追踪工具包
- `apm-toolkit-logback-1.x`：SkyWalking Logback 集成
- `spring-boot-starter-actuator`：Actuator 端点（可选，用于指标）
- `micrometer-registry-prometheus`：Prometheus 指标（可选）

## 注意事项

### 1. 性能考虑

- 全量采样（probability=1.0）在高并发场景下可能影响性能
- 建议生产环境设置适当的采样率（如 0.1）
- 异步日志上报可以减少对业务的影响
- 定期清理旧的追踪数据，避免存储压力

### 2. 部署要求

- SkyWalking Agent 需要单独部署和配置
- 确保网络能够访问 SkyWalking OAP 服务器
- 多实例部署时，每个实例需要相同的服务名称
- 考虑使用 Sidecar 模式部署 Agent

### 3. 使用建议

- 为关键业务路径添加详细的追踪标签
- 统一日志格式，确保 Trace ID 能够正确提取
- 结合告警系统，对异常追踪进行监控
- 定期分析追踪数据，优化系统性能

### 4. 常见问题

**Q: Trace ID 获取为空怎么办？**
A: 检查以下事项：

1. 确认 Sleuth 依赖已正确引入
2. 确认 `spring.sleuth.enabled=true`
3. 确认当前线程有活跃的 Span（通常需要在 Web 请求上下文中）
4. 检查是否有自定义的过滤器或拦截器影响了追踪传播

**Q: SkyWalking 数据没有上报怎么办？**
A: 检查以下事项：

1. 确认 SkyWalking Agent 已正确配置并启动
2. 确认网络可以访问 OAP 服务器（默认 11800 端口）
3. 查看 Agent 日志，确认没有连接错误
4. 确认服务名称配置正确

**Q: 如何自定义采样策略？**
A: 实现自定义的 `Sampler`：

```java
@Bean
public Sampler customSampler() {
    return new Sampler() {
        @Override
        public boolean isSampled(long traceId) {
            // 自定义采样逻辑
            // 例如：只采样特定路径的请求
            HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String path = request.getRequestURI();
            return path.startsWith("/api/");
        }
    };
}
```

**Q: 如何集成其他 APM 系统？**
A: 模块支持通过 Sleuth 的扩展点集成其他系统：

```java
@Bean
public BravePropagation.Factory propagationFactory() {
    // 自定义传播工厂，支持其他格式
    return new BravePropagation.FactoryBuilder()
        .addType(B3Propagation.FACTORY)
        .addType(W3CPropagation.FACTORY)
        .build();
}
```

## 版本要求

- JDK 17+
- Spring Boot 3.5.8+
- Spring Cloud 2023.0.6+
- SkyWalking Agent 9.7.0+
- Micrometer Tracing 1.2+

## 相关模块

- **carlos-spring-boot-core**：基础工具类、工具类依赖
- **carlos-log**：日志记录，与追踪信息结合
- **carlos-spring-cloud-starter**：微服务基础，分布式追踪依赖
- **carlos-gateway**：API 网关，网关层追踪支持
