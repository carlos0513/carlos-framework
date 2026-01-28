# carlos-gateway

## 模块简介

`carlos-gateway` 是 Carlos 框架的 API 网关模块，基于 Spring Cloud Gateway 构建，提供微服务架构中的网关服务。该模块集成了 Nacos 服务发现与配置、负载均衡、Knife4j API 文档等功能，为企业级微服务架构提供统一的网关入口。

## 模块状态

**注意**：当前模块处于 **基础框架搭建** 阶段，提供了完整的配置文件和依赖管理，但具体的网关路由、过滤器等业务逻辑需要根据实际需求进行开发实现。

## 核心功能

### 1. 网关基础架构

- **Spring Cloud Gateway**: 基于 WebFlux 的高性能网关
- **动态路由**: 支持从配置中心动态加载路由配置
- **负载均衡**: 集成 Spring Cloud LoadBalancer
- **服务发现**: 与 Nacos Discovery 集成

### 2. 配置管理

- **Nacos Config**: 配置中心集成，支持动态配置更新
- **Bootstrap 配置**: 支持 Spring Cloud Bootstrap 配置模式
- **环境隔离**: 支持多环境配置隔离

### 3. 监控与日志

- **集中式日志**: 预配置 Logback 日志系统
- **异步日志**: 高性能异步日志记录
- **日志分级**: 支持 DEBUG、INFO、WARN、ERROR 分级存储
- **请求追踪**: 支持请求 ID 追踪

### 4. API 文档

- **Knife4j 集成**: 网关级别 API 文档聚合
- **文档聚合**: 自动聚合后端服务 API 文档
- **在线调试**: 支持在线接口调试

## 依赖项

```xml
<!-- 核心依赖 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-core</artifactId>
</dependency>

<!-- Spring Cloud Gateway -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

<!-- Nacos 集成 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>

<!-- 负载均衡 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>

<!-- API 文档 -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-gateway-spring-boot-starter</artifactId>
</dependency>
```

## 配置文件

### 1. Bootstrap 配置 (bootstrap.yml)

```yaml
spring:
  application:
    name: carlos-gateway
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER:localhost:8848}
        namespace: ${NACOS_NAMESPACE:public}
        group: ${NACOS_GROUP:DEFAULT_GROUP}
      config:
        server-addr: ${NACOS_SERVER:localhost:8848}
        file-extension: yaml
        namespace: ${NACOS_NAMESPACE:public}
        group: ${NACOS_GROUP:DEFAULT_GROUP}
        refresh-enabled: true
```

### 2. 网关路由配置 (application.yml)

```yaml
spring:
  cloud:
    gateway:
      # 全局过滤器配置
      default-filters:
        - name: RequestRateLimiter
          args:
            redis-rate-limiter.replenishRate: 10
            redis-rate-limiter.burstCapacity: 20

      # 路由配置（示例）
      routes:
        - id: auth-service
          uri: lb://auth-service
          predicates:
            - Path=/api/auth/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: auth-service
                fallbackUri: forward:/fallback/auth

        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=1

        # 静态路由示例
        - id: static-route
          uri: https://example.com
          predicates:
            - Host=**.example.com

# 服务发现
  nacos:
    discovery:
      server-addr: ${spring.cloud.nacos.discovery.server-addr}

# 负载均衡配置
  loadbalancer:
    nacos:
      enabled: true

# Knife4j 配置
knife4j:
  gateway:
    enabled: true
    strategy: discover
    # 需要聚合的服务
    discover:
      enabled: true
      version: openapi3
```

### 3. 日志配置 (logback.xml)

模块预配置了完整的 Logback 配置：

- **Console Appender**: 控制台输出，彩色日志
- **File Appender**: 文件输出，按级别分隔
- **Async Appender**: 异步日志，提升性能
- **Rolling Policy**: 按日期和大小滚动
- **Pattern Layout**: 自定义日志格式，包含请求 ID

## 开发指南

### 1. 创建自定义过滤器

```java
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 认证逻辑
        String token = request.getHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(token)) {
            return unauthorized(exchange);
        }

        // 验证 Token
        if (!validateToken(token)) {
            return unauthorized(exchange);
        }

        // 添加用户信息到请求头
        ServerHttpRequest modifiedRequest = request.mutate()
            .header("X-User-Id", extractUserId(token))
            .header("X-User-Name", extractUserName(token))
            .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
```

### 2. 配置动态路由

```java
@Configuration
public class DynamicRouteConfig {

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @EventListener(ApplicationReadyEvent.class)
    public void initRoutes() {
        // 从数据库或其他配置源加载路由
        List<RouteDefinition> routes = loadRoutesFromDatabase();

        routes.forEach(route -> {
            routeDefinitionWriter.save(Mono.just(route)).subscribe();
        });
    }

    @Scheduled(fixedDelay = 30000)
    public void refreshRoutes() {
        // 定时刷新路由配置
        List<RouteDefinition> newRoutes = loadRoutesFromDatabase();
        // 比较并更新路由
    }
}
```

### 3. 实现熔断降级

```java
@Component
public class FallbackController {

    @GetMapping("/fallback/auth")
    public Mono<Result<Void>> authFallback() {
        return Mono.just(Result.fail(StatusCode.SERVICE_UNAVAILABLE, "认证服务暂不可用"));
    }

    @GetMapping("/fallback/user")
    public Mono<Result<Void>> userFallback() {
        return Mono.just(Result.fail(StatusCode.SERVICE_UNAVAILABLE, "用户服务暂不可用"));
    }
}
```

### 4. 集成限流

```java
@Configuration
public class RateLimitConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter(ReactiveRedisTemplate<String, String> redisTemplate) {
        return new RedisRateLimiter(
            redisTemplate,
            RateLimiterConfig.builder()
                .limitForPeriod(100)      // 每秒限制数
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(500))
                .build()
        );
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
            return Mono.just(Optional.ofNullable(userId).orElse("anonymous"));
        };
    }
}
```

## 部署配置

### Docker 部署

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/carlos-gateway.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes 部署

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: carlos-gateway
spec:
  replicas: 2
  selector:
    matchLabels:
      app: carlos-gateway
  template:
    metadata:
      labels:
        app: carlos-gateway
    spec:
      containers:
      - name: gateway
        image: yunjin/gateway:latest
        ports:
        - containerPort: 8080
        env:
        - name: NACOS_SERVER
          value: "nacos-server:8848"
        - name: JAVA_OPTS
          value: "-Xmx512m -Xms256m"
---
apiVersion: v1
kind: Service
metadata:
  name: carlos-gateway
spec:
  selector:
    app: carlos-gateway
  ports:
  - port: 80
    targetPort: 8080
  type: LoadBalancer
```

## 监控与健康检查

### Spring Boot Actuator 端点

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,gateway
  endpoint:
    health:
      show-details: always
    gateway:
      enabled: true
```

### 自定义健康检查

```java
@Component
public class GatewayHealthIndicator implements ReactiveHealthIndicator {

    @Override
    public Mono<Health> health() {
        return checkDownstreamServices()
            .then(Mono.just(Health.up().build()))
            .onErrorResume(ex -> Mono.just(
                Health.down().withException(ex).build()
            ));
    }

    private Mono<Void> checkDownstreamServices() {
        // 检查下游服务健康状况
        return Mono.empty();
    }
}
```

## 安全配置

### SSL/TLS 配置

```yaml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: changeit
    key-store-type: PKCS12
    key-alias: carlos-gateway
  port: 8443
```

### CORS 配置

```java
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
```

## 性能优化

### 1. 响应式编程最佳实践

```java
@Component
public class ReactiveFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.fromRunnable(() -> {
            // 预处理
            long startTime = System.currentTimeMillis();
            exchange.getAttributes().put("startTime", startTime);
        })
        .then(chain.filter(exchange))
        .doFinally(signalType -> {
            // 后处理
            Long startTime = exchange.getAttribute("startTime");
            if (startTime != null) {
                long duration = System.currentTimeMillis() - startTime;
                log.info("Request took {}ms", duration);
            }
        });
    }
}
```

### 2. 缓存配置

```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public ReactiveRedisCacheManager cacheManager(ReactiveRedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeValuesWith(SerializationPair.fromSerializer(
                new Jackson2JsonRedisSerializer<>(Object.class)
            ));

        return ReactiveRedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }
}
```

## 故障排除

### 常见问题

1. **路由不生效**
    - 检查 Nacos 配置是否正确
    - 验证服务发现是否正常工作
    - 检查 Predicate 配置

2. **请求超时**
    - 调整连接超时配置
    - 检查下游服务响应时间
    - 配置合理的熔断策略

3. **内存泄漏**
    - 监控堆内存使用情况
    - 使用响应式编程避免阻塞
    - 定期清理缓存

### 日志分析

```bash
# 查看网关日志
tail -f logs/carlos-gateway.log

# 查看错误日志
tail -f logs/carlos-gateway-error.log

# 查看调试日志
tail -f logs/carlos-gateway-debug.log
```

## 扩展开发

### 1. 自定义 Predicate

```java
@Component
public class CustomPredicateFactory extends AbstractRoutePredicateFactory<CustomPredicateFactory.Config> {

    public CustomPredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            // 自定义匹配逻辑
            return config.getValue().equals(
                exchange.getRequest().getHeaders().getFirst(config.getHeader())
            );
        };
    }

    @Data
    public static class Config {
        private String header;
        private String value;
    }
}
```

### 2. 自定义 Filter Factory

```java
@Component
public class CustomFilterFactory extends AbstractGatewayFilterFactory<CustomFilterFactory.Config> {

    public CustomFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 自定义过滤逻辑
            ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-Custom-Header", config.getValue())
                .build();

            return chain.filter(
                exchange.mutate().request(request).build()
            );
        };
    }

    @Data
    public static class Config {
        private String value;
    }
}
```

## 版本要求

- **JDK**: 17+
- **Spring Boot**: 3.5.8+
- **Spring Cloud**: 2023.0.6+
- **Spring Cloud Alibaba**: 2023.0.3.3+
- **Nacos**: 2.2.3+
- **Maven**: 3.8+

## 相关模块

- `carlos-core`: 核心基础模块
- `carlos-springcloud`: Spring Cloud 集成模块
- `carlos-redis`: Redis 缓存模块（限流依赖）
- `carlos-apm`: APM 监控模块
- `carlos-oauth2`: OAuth2 认证模块

---

**注意**: 本模块当前提供的是基础框架和配置模板，实际网关业务逻辑需要根据具体需求进行开发实现。建议参考 Spring Cloud Gateway 官方文档和 Carlos 框架的其他模块进行开发。
