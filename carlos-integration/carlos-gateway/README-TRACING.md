# Carlos Gateway 链路追踪指南

## 概述

Carlos Gateway 模块集成了完整的链路追踪能力，包括：

| 功能             | 说明                                    |
|----------------|---------------------------------------|
| **Request ID** | 业务层面的请求标识，用于单请求追踪                     |
| **Trace ID**   | 分布式链路追踪标识（Micrometer Tracing + Brave） |
| **Span ID**    | 单个服务内的操作单元标识                          |
| **访问日志**       | 记录所有请求的详细信息和耗时                        |

## 架构

```
┌─────────────────────────────────────────────────────────────────────┐
│                          客户端请求                                   │
│              Header: X-Request-Id (可选), b3 (可选)                  │
└─────────────────────────────────────────────────────────────────────┘
                                    ↓
┌─────────────────────────────────────────────────────────────────────┐
│                      RequestTracingFilter                            │
│  ┌───────────────────────────────────────────────────────────────┐ │
│  │ 1. 从 Header 提取 Request ID，如果没有则生成                  │ │
│  │ 2. 从 Header 提取 B3 Trace 上下文，如果没有则创建新的 Span     │ │
│  │ 3. 将 Request ID 和 Trace ID 注入到下游请求                    │ │
│  │ 4. 在响应头中返回 X-Request-Id 和 X-Trace-Id                   │ │
│  └───────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
                                    ↓
┌─────────────────────────────────────────────────────────────────────┐
│                      其他 Gateway 过滤器                             │
│  (WafFilter → AuthFilter → RateLimiter → GrayRelease → ...)        │
└─────────────────────────────────────────────────────────────────────┘
                                    ↓
┌─────────────────────────────────────────────────────────────────────┐
│                        AccessLogFilter                               │
│  记录访问日志：method|path|status|duration|requestId|traceId|clientIp │
└─────────────────────────────────────────────────────────────────────┘
                                    ↓
┌─────────────────────────────────────────────────────────────────────┐
│                         下游微服务                                    │
│  请求头包含：X-Request-Id, b3, X-B3-TraceId, X-B3-SpanId           │
└─────────────────────────────────────────────────────────────────────┘
```

## 请求头说明

### 入站请求头（客户端 → Gateway）

| Header         | 说明                                | 必需         |
|----------------|-----------------------------------|------------|
| `X-Request-Id` | 业务自定义请求 ID                        | 否（不传则自动生成） |
| `b3`           | B3 单头格式: `traceId-spanId-sampled` | 否          |
| `X-B3-TraceId` | B3 Trace ID（多头部格式）                | 否          |
| `X-B3-SpanId`  | B3 Span ID（多头部格式）                 | 否          |
| `X-B3-Sampled` | 是否采样: `1` 或 `0`                   | 否          |
| `X-User-Id`    | 用户 ID（用于追踪标签）                     | 否          |

### 出站请求头（Gateway → 下游服务）

| Header         | 说明                   |
|----------------|----------------------|
| `X-Request-Id` | 业务 Request ID        |
| `b3`           | B3 单头格式              |
| `X-B3-TraceId` | Trace ID             |
| `X-B3-SpanId`  | Span ID（Gateway 生成的） |
| `X-B3-Sampled` | 采样标志                 |

### 响应头（Gateway → 客户端）

| Header         | 说明                |
|----------------|-------------------|
| `X-Request-Id` | 业务 Request ID     |
| `X-Trace-Id`   | 链路追踪 Trace ID     |
| `X-Span-Id`    | Gateway 的 Span ID |

## 日志输出

### 控制台日志

```
2025-03-26 14:30:25.123 [reactor-http-epoll-1] INFO  [a1b2c3d4e5f67890] [abc123def4567890-xyz789abc123def4] c.c.g.f.AuthFilter - 请求认证通过
                              ^^^^^^^^^^^^^^^^ ^^^^^^^^^^^^^^^^^^^^ ^^^^^^^^^^^^^^^^
                                   Request ID       Trace ID          Span ID
```

### 访问日志（gateway-access.log）

```
2025-03-26 14:30:25.123|a1b2c3d4e5f67890|abc123def4567890|GET|/api/users|200|45
```

格式：`timestamp|requestId|traceId|method|path|status|duration(ms)|clientIp`

## 配置

### application.yml

```yaml
carlos:
  gateway:
    tracing:
      enabled: true                    # 启用链路追踪
      sampling-rate: 1.0               # 采样率（0.0-1.0）
      request-id:
        header-name: X-Request-Id      # Request ID 请求头名称
        auto-generate: true            # 未提供时是否自动生成
        add-to-response: true          # 是否添加到响应头
        prefix: ""                     # Request ID 前缀

management:
  tracing:
    enabled: true
    sampling:
      probability: 1.0                 # Micrometer 采样率
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans
```

### 日志配置（logback-spring.xml）

日志配置已内置在模块中，如需自定义可覆盖 `logback-spring.xml`。

关键配置：

```xml
<!-- 同时显示 Request ID 和 Trace ID -->
<property name="CONSOLE_PATTERN" 
          value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{requestId:-}] [%X{traceId:-}-%X{spanId:-}] %logger{36} - %msg%n"/>
```

## 过滤器顺序

Gateway 过滤器执行顺序（数值越小优先级越高）：

| 顺序                      | 过滤器                        | 说明             |
|-------------------------|----------------------------|----------------|
| HIGHEST_PRECEDENCE + 50 | RequestTracingFilter       | 最先执行，生成/提取追踪信息 |
| 1000+                   | WafFilter                  | Web 应用防火墙      |
| 2000+                   | ReplayProtectionFilter     | 重放攻击防护         |
| 3000+                   | OAuth2AuthenticationFilter | 认证             |
| 4000+                   | OAuth2AuthorizationFilter  | 授权             |
| 5000+                   | RateLimiter                | 限流             |
| 6000+                   | CircuitBreaker             | 熔断             |
| 7000+                   | GrayReleaseFilter          | 灰度发布           |
| 8000                    | AccessLogFilter            | 最后执行，记录访问日志    |

## 与下游服务集成

下游服务（如 `carlos-system-bus`）需要：

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-apm</artifactId>
</dependency>
```

### 2. 复制日志配置

```bash
cp carlos-spring-boot/carlos-spring-boot-starter-apm/src/main/resources/logback-spring-example.xml \
   your-service/src/main/resources/logback-spring.xml
```

### 3. 无需额外配置

Gateway 会自动将追踪信息传递到下游服务，下游服务的 `MdcFilter` 会自动处理。

## Zipkin 集成

启动 Zipkin 服务：

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

访问 `http://localhost:9411` 查看链路追踪。

## 常见问题

### Q: 日志中没有显示 requestId？

**A:**

1. 检查 `RequestTracingFilter` 是否已启用（`carlos.gateway.tracing.enabled=true`）
2. 检查 logback.xml 是否包含 `%X{requestId}`
3. 检查过滤器顺序，`RequestTracingFilter` 应该在其他过滤器之前执行

### Q: 下游服务获取不到 traceId？

**A:**

1. 检查下游服务是否引入了 `carlos-spring-boot-starter-apm`
2. 检查 Gateway 是否将 B3 头传递到下游（查看 `b3` 头）
3. 检查下游服务的 `MdcFilter` 是否正常工作

### Q: Request ID 和 Trace ID 有什么区别？

**A:**

- **Request ID**: 业务层面的标识，可以由前端传入，用于单请求追踪和问题排查
- **Trace ID**: 分布式链路追踪的标识，由 Micrometer Tracing 生成，用于跨服务调用追踪

### Q: 如何传递自定义 Request ID？

**A:** 在请求头中添加 `X-Request-Id`：

```bash
curl -H "X-Request-Id: my-custom-id" http://gateway:9510/api/users
```

如果不传，Gateway 会自动生成一个 16 位的随机 ID。

### Q: 如何关闭链路追踪？

**A:**

```yaml
carlos:
  gateway:
    tracing:
      enabled: false
```

关闭后，`ReqHeaderFilter` 会自动启用（保持向后兼容）。

## 参考

- [Micrometer Tracing Documentation](https://micrometer.io/docs/tracing)
- [Brave B3 Propagation](https://github.com/openzipkin/b3-propagation)
- [Spring Cloud Gateway Documentation](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
