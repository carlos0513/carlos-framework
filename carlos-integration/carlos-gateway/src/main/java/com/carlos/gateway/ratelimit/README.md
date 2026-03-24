# Carlos 扩展限流器

基于 Spring Cloud Gateway `RedisRateLimiter` 扩展的分布式限流组件，提供更丰富的功能和更灵活的配置。

## 特性

- **复用 Spring Cloud Gateway 核心算法**：基于 Stripe 令牌桶算法，经过生产验证
- **多维度限流策略**：IP、用户、API 路径、组合策略等 7 种 KeyResolver
- **黑白名单支持**：支持 IP/用户级别的白名单和黑名单
- **路由特定配置**：支持 per-route 不同限流策略
- **统一错误响应**：使用 Carlos `ErrorResponse` 格式返回限流错误
- **限流事件发布**：支持监听限流事件，用于监控和告警
- **Micrometer 指标集成**：自动导出限流指标到 Prometheus

## 快速开始

### 1. 启用限流

限流默认启用，可通过配置关闭：

```yaml
carlos:
  gateway:
    rate-limiter:
      enabled: true
```

### 2. 基础配置

```yaml
carlos:
  gateway:
    rate-limiter:
      # 默认限流配置
      default-config:
        replenish-rate: 10        # 每秒产生 10 个令牌
        burst-capacity: 20        # 令牌桶容量 20
        requested-tokens: 1       # 每次请求消耗 1 个令牌
        key-resolver: IP          # 默认基于 IP 限流
      
      # 黑白名单
      whitelist:
        - "127.0.0.1"
        - "10.0.0.0/8"  # 支持网段（需自定义解析）
      blacklist:
        - "192.168.1.100"
      
      # 响应配置
      response:
        include-headers: true           # 包含限流响应头
        uniform-error-response: true    # 使用统一错误格式
        retry-after: 60                 # 建议重试时间（秒）
      
      # 监控配置
      metrics:
        enabled: true
        publish-events: true
        sample-rate: 1.0
```

### 3. 路由特定配置

```yaml
carlos:
  gateway:
    rate-limiter:
      routes:
        - route-id: user-service
          replenish-rate: 100
          burst-capacity: 200
          key-resolver: USER          # 基于用户限流
        - route-id: order-service
          replenish-rate: 50
          burst-capacity: 100
          key-resolver: IP_API        # 基于 IP+API 限流
```

## KeyResolver 策略

| 策略           | 说明                          | 适用场景                |
|--------------|-----------------------------|---------------------|
| `IP`         | 基于客户端 IP 限流                 | 防止单个 IP 恶意请求        |
| `USER`       | 基于用户 ID 限流（从 X-User-Id 头获取） | 防止单个用户过度使用          |
| `API`        | 基于请求路径限流                    | API 级别限流            |
| `METHOD_API` | 基于 HTTP 方法 + 路径限流           | 细粒度 API 限流          |
| `IP_API`     | 基于 IP + 路径组合限流              | 防止单个 IP 对特定 API 的滥用 |
| `IP_USER`    | 基于 IP + 用户组合限流              | 多维度限流               |
| `SERVICE`    | 基于服务 ID 限流                  | 服务级别限流              |

## 在 Gateway 路由中使用

### 方式一：配置文件

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - name: CarlosRateLimiter
              args:
                replenish-rate: 10
                burst-capacity: 20
                key-resolver: USER
```

### 方式二：Java DSL

```java
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("user-service", r -> r
            .path("/api/users/**")
            .filters(f -> f
                .filter(new CarlosRateLimiterGatewayFilterFactory(
                    rateLimiter, properties, objectMapper)
                    .apply(config -> config
                        .setReplenishRate(10)
                        .setBurstCapacity(20)
                        .setKeyResolver(CarlosKeyResolver.USER.getResolver())
                    )
                )
            )
            .uri("lb://user-service")
        )
        .build();
}
```

## 自定义 KeyResolver

```java
@Bean
public KeyResolver customKeyResolver() {
    return exchange -> {
        // 自定义限流键生成逻辑
        String customKey = exchange.getRequest().getHeaders().getFirst("X-Custom-Key");
        return Mono.just(customKey != null ? customKey : "default");
    };
}
```

## 监听限流事件

```java
@Component
public class CustomRateLimitListener {

    @EventListener
    public void onRateLimitExceeded(RateLimitExceededEvent event) {
        log.warn("Rate limit exceeded: route={}, key={}", 
            event.getRouteId(), event.getKey());
        
        // 发送告警、记录日志等
    }
}
```

## 限流响应

### HTTP 响应头

```
X-RateLimit-Limit: 20
X-RateLimit-Remaining: 19
X-RateLimit-Reset: 1711267200
Retry-After: 60
```

### 限流错误响应（JSON）

```json
{
  "success": false,
  "status": 429,
  "code": 5429,
  "message": "请求过于频繁，请稍后重试",
  "extra": {
    "limitKey": "192.168.1.100",
    "retryAfter": 60
  }
}
```

## Micrometer 指标

限流器自动导出以下指标：

| 指标名称                                           | 类型      | 描述       |
|------------------------------------------------|---------|----------|
| `carlos.gateway.rate-limiter.exceeded`         | Counter | 限流触发次数   |
| `carlos.gateway.rate-limiter.requests`         | Counter | 限流检查请求数  |
| `carlos.gateway.rate-limiter.response-time`    | Timer   | 限流检查响应时间 |
| `carlos.gateway.rate-limiter.tokens-remaining` | Gauge   | 剩余令牌数    |

## 与 Spring Cloud Gateway 原生限流的对比

| 特性            | Spring Cloud Gateway | Carlos 扩展 |
|---------------|----------------------|-----------|
| 核心算法          | 令牌桶（Stripe）          | 复用官方实现    |
| KeyResolver   | 单一接口                 | 内置 7 种策略  |
| 黑白名单          | 不支持                  | 支持        |
| 统一错误响应        | 不支持                  | 支持        |
| 限流事件          | 不支持                  | 支持        |
| Micrometer 指标 | 需自定义                 | 自动集成      |
| per-route 配置  | 支持                   | 增强支持      |

## 注意事项

1. **Redis 依赖**：限流器需要 Redis 支持，确保 Redis 可用
2. **故障降级**：Redis 故障时，默认放行请求，避免影响业务
3. **Lua 脚本**：使用 Spring Cloud Gateway 官方 Lua 脚本，保证原子性
4. **性能**：基于 Reactive 实现，高性能低延迟

## 参考

- [Spring Cloud Gateway RateLimiter](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory)
- [Stripe Rate Limiting](https://stripe.com/blog/rate-limiters)
