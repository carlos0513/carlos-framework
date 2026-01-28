# carlos-springcloud

## 模块简介

`carlos-springcloud` 是 Carlos 框架的 Spring Cloud Alibaba 集成模块，提供了微服务架构所需的核心组件集成，包括服务发现、配置管理、服务调用、负载均衡和容错保护等功能。基于 Spring Cloud Alibaba 2025.0.0.0 构建，支持 Spring Boot 3.5.8+。

## 主要功能

### 1. 一站式启动注解

**@SpringCloudApplication** 注解简化微服务启动配置：

```java
@SpringCloudApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
```

**注解包含**：

- `@EnableDiscoveryClient`: 启用服务发现
- `@EnableFeignClients("com.carlos")`: 启用 Feign 客户端
- `@SpringBootApplication`: Spring Boot 应用

### 2. OpenFeign 服务调用

#### Feign 配置

```java
// 配置 Feign 客户端
@FeignClient(name = "user-service", path = "/api/user")
public interface UserServiceClient {

    @GetMapping("/{id}")
    Result<User> getUser(@PathVariable("id") Long id);

    @PostMapping
    Result<User> create(@RequestBody UserDTO user);

    @GetMapping("/list")
    Result<List<User>> list(@SpringQueryMap UserQuery query);
}
```

#### Feign 配置类

```yaml
# Feign 日志配置
yunjin:
  feign:
    log:
      enable: true          # 启用 Feign 日志
      level: FULL           # 日志级别: NONE, BASIC, HEADERS, FULL

# Feign 客户端配置
feign:
  client:
    config:
      default:
        connect-timeout: 5000   # 连接超时(ms)
        read-timeout: 10000     # 读取超时(ms)
        logger-level: full      # 日志级别
```

#### 请求拦截器

自动传递请求头信息：

```java
// 原始请求中的以下头部会自动传递到 Feign 调用
// - Authorization: 认证令牌
// - X-User-Id: 用户 ID
// - X-Request-Id: 请求 ID
// - X-Tenant-Id: 租户 ID

// 自动添加 RPC 标记头部
// RPC-Header: true (用于标识 Feign 调用)
```

### 3. 负载均衡

集成 Spring Cloud LoadBalancer：

```yaml
# 负载均衡配置
spring:
  cloud:
    loadbalancer:
      enabled: true
      health-check:
        interval: 30s      # 健康检查间隔
        initial-delay: 0s  # 初始延迟

      # 缓存配置
      cache:
        enabled: true
        caffeine:
          spec: maximumSize=1000,expireAfterWrite=30s
```

### 4. Nacos 服务发现与配置

#### 服务注册与发现

```yaml
spring:
  application:
    name: user-service    # 服务名称

  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER:localhost:8848}  # Nacos 服务器地址
        namespace: ${NACOS_NAMESPACE:public}         # 命名空间
        group: ${NACOS_GROUP:DEFAULT_GROUP}          # 分组
        metadata:
          version: 1.0.0                             # 服务版本
          region: beijing                            # 区域

      config:
        server-addr: ${NACOS_SERVER:localhost:8848}  # 配置中心地址
        file-extension: yaml                          # 配置文件格式
        refresh-enabled: true                         # 启用配置刷新
        shared-configs:                               # 共享配置
          - data-id: common-config.yaml
            group: DEFAULT_GROUP
            refresh: true
```

#### 配置管理

```java
@RefreshScope  // 支持动态刷新
@Component
public class DynamicConfig {

    @Value("${custom.property:default}")
    private String property;

    @PostConstruct
    public void init() {
        log.info("Property value: {}", property);
    }
}
```

### 5. Sentinel 熔断降级

#### 流量控制与熔断

```yaml
spring:
  cloud:
    sentinel:
      enabled: true
      transport:
        dashboard: ${SENTINEL_DASHBOARD:localhost:8080}  # Sentinel 控制台
        port: 8719                                       # 客户端端口
      filter:
        enabled: true                                    # 启用过滤器
      metric:
        charset: UTF-8                                   # 字符集

      # 数据源配置
      datasource:
        flow:
          nacos:
            server-addr: ${NACOS_SERVER:localhost:8848}
            data-id: ${spring.application.name}-flow-rules
            group-id: SENTINEL_GROUP
            rule-type: flow
```

#### 熔断规则配置

```java
// 1. 在 Controller 中使用 @SentinelResource
@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/{id}")
    @SentinelResource(
        value = "getUserById",
        blockHandler = "handleBlock",
        fallback = "handleFallback"
    )
    public Result<User> getUser(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }

    // 限流处理
    public Result<User> handleBlock(Long id, BlockException ex) {
        return Result.fail("请求过于频繁，请稍后重试");
    }

    // 降级处理
    public Result<User> handleFallback(Long id, Throwable ex) {
        return Result.fail("服务暂时不可用");
    }
}

// 2. 在 Feign 客户端中使用 fallback
@FeignClient(
    name = "order-service",
    fallback = OrderServiceFallback.class
)
public interface OrderServiceClient {
    @GetMapping("/api/orders/user/{userId}")
    Result<List<Order>> getUserOrders(@PathVariable Long userId);
}

@Component
public class OrderServiceFallback implements OrderServiceClient {
    @Override
    public Result<List<Order>> getUserOrders(Long userId) {
        log.warn("Order service fallback triggered for user: {}", userId);
        return Result.ok(Collections.emptyList());
    }
}
```

### 6. 统一异常处理

#### Feign 异常解码器

```java
// Feign 调用异常时，自动解码为统一的异常格式
@RestControllerAdvice
public class FeignGlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public Result<Void> handleFeignException(FeignException e) {
        // 解析 Feign 异常，返回标准格式
        return Result.fail(StatusCode.SERVICE_UNAVAILABLE, "服务调用失败");
    }
}
```

### 7. 实际应用场景

#### 场景 1：微服务间调用

```java
// 用户服务
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private OrderServiceClient orderService;

    @GetMapping("/{id}/orders")
    public Result<UserWithOrders> getUserWithOrders(@PathVariable Long id) {
        User user = userService.getById(id);
        Result<List<Order>> ordersResult = orderService.getUserOrders(id);

        UserWithOrders result = new UserWithOrders();
        result.setUser(user);
        result.setOrders(ordersResult.getData());

        return Result.ok(result);
    }
}
```

#### 场景 2：配置中心动态配置

```java
@RestController
@RefreshScope
public class ConfigController {

    @Value("${dynamic.feature.enabled:false}")
    private Boolean featureEnabled;

    @Value("${dynamic.rate.limit:100}")
    private Integer rateLimit;

    @GetMapping("/config")
    public Result<ConfigInfo> getConfig() {
        ConfigInfo info = new ConfigInfo();
        info.setFeatureEnabled(featureEnabled);
        info.setRateLimit(rateLimit);
        return Result.ok(info);
    }
}
```

#### 场景 3：服务熔断与降级

```java
@Service
public class PaymentService {

    @SentinelResource(
        value = "processPayment",
        blockHandler = "processPaymentBlock",
        fallback = "processPaymentFallback"
    )
    public Result<Payment> processPayment(PaymentDTO payment) {
        // 调用支付网关
        return paymentGateway.process(payment);
    }

    public Result<Payment> processPaymentBlock(PaymentDTO payment, BlockException ex) {
        // 限流处理
        return Result.fail(StatusCode.TOO_MANY_REQUESTS, "系统繁忙，请稍后重试");
    }

    public Result<Payment> processPaymentFallback(PaymentDTO payment, Throwable ex) {
        // 降级处理
        log.error("Payment service fallback", ex);
        return Result.fail(StatusCode.SERVICE_UNAVAILABLE, "支付服务暂不可用");
    }
}
```

#### 场景 4：服务注册与发现

```java
@SpringBootApplication
@SpringCloudApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner checkRegistration(DiscoveryClient discoveryClient) {
        return args -> {
            List<String> services = discoveryClient.getServices();
            log.info("Registered services: {}", services);

            List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
            log.info("User service instances: {}", instances);
        };
    }
}
```

### 8. 配置说明

#### 完整配置示例

```yaml
# 应用配置
spring:
  application:
    name: user-service
  profiles:
    active: dev

  # Nacos 配置
  cloud:
    nacos:
      # 服务发现
      discovery:
        server-addr: ${NACOS_SERVER:localhost:8848}
        namespace: ${NACOS_NAMESPACE:dev}
        group: ${NACOS_GROUP:SERVICES}
        metadata:
          version: 1.0.0
          region: beijing
          weight: 1
        # 注册配置
        ip: ${SERVER_IP:}
        port: ${SERVER_PORT:8080}
        # 心跳配置
        heart-beat-interval: 5000
        heart-beat-timeout: 15000
        ip-delete-timeout: 30000

      # 配置中心
      config:
        server-addr: ${NACOS_SERVER:localhost:8848}
        namespace: ${NACOS_NAMESPACE:dev}
        group: ${NACOS_GROUP:CONFIG}
        file-extension: yaml
        refresh-enabled: true
        # 扩展配置
        shared-configs:
          - data-id: common-config.yaml
            group: DEFAULT_GROUP
            refresh: true
          - data-id: redis-config.yaml
            group: DEFAULT_GROUP
            refresh: true
        # 配置项
        name: ${spring.application.name}
        prefix: ${spring.application.name}

    # Sentinel 配置
    sentinel:
      enabled: true
      eager: true
      transport:
        dashboard: ${SENTINEL_DASHBOARD:localhost:8080}
        port: 8719
      filter:
        enabled: true
        url-patterns: /**
      # 数据源
      datasource:
        # 流控规则
        flow:
          nacos:
            server-addr: ${NACOS_SERVER:localhost:8848}
            data-id: ${spring.application.name}-flow-rules
            group-id: SENTINEL_GROUP
            rule-type: flow
        # 降级规则
        degrade:
          nacos:
            server-addr: ${NACOS_SERVER:localhost:8848}
            data-id: ${spring.application.name}-degrade-rules
            group-id: SENTINEL_GROUP
            rule-type: degrade
        # 系统规则
        system:
          nacos:
            server-addr: ${NACOS_SERVER:localhost:8848}
            data-id: ${spring.application.name}-system-rules
            group-id: SENTINEL_GROUP
            rule-type: system
        # 授权规则
        authority:
          nacos:
            server-addr: ${NACOS_SERVER:localhost:8848}
            data-id: ${spring.application.name}-authority-rules
            group-id: SENTINEL_GROUP
            rule-type: authority
        # 热点规则
        param-flow:
          nacos:
            server-addr: ${NACOS_SERVER:localhost:8848}
            data-id: ${spring.application.name}-param-flow-rules
            group-id: SENTINEL_GROUP
            rule-type: param-flow

    # 负载均衡
    loadbalancer:
      enabled: true
      health-check:
        interval: 30s
        initial-delay: 0s
      cache:
        enabled: true
        caffeine:
          spec: maximumSize=1000,expireAfterWrite=30s

# Feign 配置
feign:
  client:
    config:
      default:
        connect-timeout: 5000
        read-timeout: 10000
        logger-level: full
  compression:
    request:
      enabled: true
      mime-types: text/xml,application/xml,application/json
      min-request-size: 2048
    response:
      enabled: true

  # 使用 OkHttp 客户端
  okhttp:
    enabled: true
  httpclient:
    enabled: false

# Carlos Feign 配置
yunjin:
  feign:
    log:
      enable: true
      level: FULL

# 服务端口
server:
  port: 8080
  servlet:
    context-path: /
```

## 依赖引入

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-springcloud</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

## 依赖项

- **Spring Cloud OpenFeign**: 服务调用
- **Spring Cloud LoadBalancer**: 负载均衡
- **Spring Cloud Alibaba Nacos Discovery**: 服务发现
- **Spring Cloud Alibaba Nacos Config**: 配置管理
- **Spring Cloud Alibaba Sentinel**: 熔断降级
- **Feign OkHttp**: HTTP 客户端
- **Caffeine**: 缓存库
- **carlos-core**: 核心基础模块
- **carlos-springboot**: Spring Boot 集成

## 使用指南

### 1. 快速开始

```java
// 1. 创建微服务应用
@SpringCloudApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

// 2. 配置 application.yml
// 3. 定义 Feign 客户端
// 4. 实现业务逻辑
```

### 2. 自定义配置

```java
@Configuration
public class CustomFeignConfig {

    @Bean
    public RequestInterceptor customInterceptor() {
        return template -> {
            // 添加自定义头部
            template.header("X-Custom-Header", "custom-value");
        };
    }

    @Bean
    public Retryer feignRetryer() {
        // 自定义重试策略
        return new Retryer.Default(100, 1000, 3);
    }
}
```

### 3. 健康检查

```java
@Component
public class ServiceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // 自定义健康检查逻辑
        boolean isHealthy = checkServiceHealth();

        if (isHealthy) {
            return Health.up()
                .withDetail("service", "running")
                .withDetail("timestamp", System.currentTimeMillis())
                .build();
        } else {
            return Health.down()
                .withDetail("service", "unavailable")
                .withDetail("error", "service check failed")
                .build();
        }
    }
}
```

## 注意事项

1. **Nacos 版本兼容性**: 确保 Nacos 版本与 Spring Cloud Alibaba 版本兼容
2. **Sentinel 控制台**: 生产环境建议部署 Sentinel 控制台进行监控
3. **配置刷新**: 使用 `@RefreshScope` 注解的 Bean 支持配置动态刷新
4. **服务发现延迟**: 服务注册和发现可能存在短暂延迟
5. **Feign 超时**: 根据网络情况合理配置 Feign 超时时间
6. **负载均衡缓存**: 负载均衡结果会缓存，更新服务实例可能延迟
7. **Sentinel 规则持久化**: 建议将 Sentinel 规则持久化到 Nacos 配置中心
8. **多环境支持**: 通过 namespace 和 group 支持多环境隔离
9. **服务治理**: 结合 APM 工具进行服务链路追踪
10. **监控告警**: 配置服务健康监控和告警机制

## 性能优化

1. **连接池配置**: 配置 OkHttp 连接池提升性能
2. **负载均衡缓存**: 调整负载均衡缓存策略
3. **Feign 压缩**: 启用 Feign 请求响应压缩
4. **服务实例缓存**: 合理配置服务实例缓存时间
5. **线程池优化**: 根据业务量调整线程池参数

## 故障排除

### 常见问题

1. **服务注册失败**
    - 检查 Nacos 服务器是否可达
    - 验证 namespace 和 group 配置
    - 检查服务端口是否被占用

2. **Feign 调用超时**
    - 调整 connect-timeout 和 read-timeout
    - 检查网络连接
    - 确认目标服务是否正常

3. **配置刷新不生效**
    - 检查 `@RefreshScope` 注解是否正确使用
    - 确认配置已推送到 Nacos
    - 查看应用日志确认接收到配置变更

4. **Sentinel 规则不生效**
    - 检查 Sentinel 控制台连接
    - 验证规则格式是否正确
    - 确认应用已正确集成 Sentinel

### 日志分析

```bash
# 查看服务注册日志
grep "nacos registry" logs/application.log

# 查看 Feign 调用日志
grep "feign" logs/application.log

# 查看 Sentinel 日志
grep "sentinel" logs/application.log

# 查看配置刷新日志
grep "refresh" logs/application.log
```

## 版本要求

- **JDK**: 17+
- **Spring Boot**: 3.5.8+
- **Spring Cloud**: 2025.0.1+
- **Spring Cloud Alibaba**: 2025.0.0.0+
- **Nacos**: 2.2.3+
- **Sentinel**: 1.8.7+
- **Maven**: 3.8+

## 相关模块

- `carlos-core`: 核心基础模块
- `carlos-springboot`: Spring Boot 集成
- `carlos-gateway`: API 网关
- `carlos-apm`: APM 监控
- `carlos-redis`: Redis 缓存
