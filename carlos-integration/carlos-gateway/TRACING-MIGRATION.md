# Gateway 链路追踪升级指南

## 升级说明

本次升级整合了原有的 `ReqHeaderFilter`（生成 x-request-id）和 `TracingFilter`（处理 Micrometer Tracing），统一由新的
`RequestTracingFilter` 处理。

## 变更清单

### 新增文件

| 文件                          | 说明                                   |
|-----------------------------|--------------------------------------|
| `RequestTracingFilter.java` | 统一链路追踪过滤器，同时处理 Request ID 和 Trace ID |
| `AccessLogFilter.java`      | 访问日志过滤器，记录所有请求信息                     |
| `AccessLogProperties.java`  | 访问日志配置属性                             |
| `logback-spring.xml`        | Gateway 专用日志配置                       |
| `README-TRACING.md`         | 链路追踪使用文档                             |

### 修改文件

| 文件                       | 变更                             |
|--------------------------|--------------------------------|
| `ReqHeaderFilter.java`   | 标记为 `@Deprecated`，在新过滤器启用时自动禁用 |
| `TracingFilter.java`     | 标记为 `@Deprecated`，在新过滤器启用时自动禁用 |
| `TracingProperties.java` | 添加 Request ID 配置               |
| `GlobalFilterOrder.java` | 添加过滤器顺序定义                      |
| `GatewayConfig.java`     | 注册新的过滤器，替换旧的 TracingFilter     |
| `pom.xml`                | 添加 Brave 依赖                    |
| `application.yml`        | 添加链路追踪配置                       |

## 向后兼容

### 场景 1：完全升级（推荐）

默认配置即可，无需任何修改：

```yaml
carlos:
  gateway:
    tracing:
      enabled: true
```

新的 `RequestTracingFilter` 会自动：

- 生成/提取 Request ID
- 生成/提取 Trace ID
- 传递到下游服务
- 在响应头中返回两个 ID

### 场景 2：保持原有行为

如果需要保持只生成 x-request-id 而不启用链路追踪：

```yaml
carlos:
  gateway:
    tracing:
      enabled: false
```

此时旧的 `ReqHeaderFilter` 会自动启用。

## 日志格式变更

### 升级前

```
2025-03-26 14:30:25.123 [reactor-http-epoll-1] INFO  c.c.g.f.AuthFilter - 请求认证通过
```

### 升级后

```
2025-03-26 14:30:25.123 [reactor-http-epoll-1] INFO  [a1b2c3d4e5f67890] [abc123def4567890-xyz789abc123def4] c.c.g.f.AuthFilter - 请求认证通过
                              ^^^^^^^^^^^^^^^^ ^^^^^^^^^^^^^^^^^^^^ ^^^^^^^^^^^^^^^^
                                   Request ID       Trace ID          Span ID
```

## 响应头变更

### 升级前

```
X-Request-Id: req-abc123
```

### 升级后

```
X-Request-Id: a1b2c3d4e5f67890
X-Trace-Id: abc123def4567890
X-Span-Id: xyz789abc123def4
```

## 下游服务集成

下游服务（业务微服务）需要引入 APM starter：

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-apm</artifactId>
</dependency>
```

无需额外配置，自动生效。

## 测试验证

### 1. 启动 Gateway

```bash
cd carlos-integration/carlos-gateway
mvn spring-boot:run
```

### 2. 发送测试请求

```bash
# 不带 Request ID（Gateway 自动生成）
curl -v http://localhost:9510/api/users

# 带自定义 Request ID
curl -v -H "X-Request-Id: my-custom-id" http://localhost:9510/api/users

# 带 Trace 上下文（模拟上游服务调用）
curl -v -H "b3: abc123def4567890-xyz789abc123def4-1" http://localhost:9510/api/users
```

### 3. 检查响应头

```
< X-Request-Id: a1b2c3d4e5f67890
< X-Trace-Id: abc123def4567890
< X-Span-Id: xyz789abc123def4
```

### 4. 检查日志

```
2025-03-26 14:30:25.123 [reactor-http-epoll-1] INFO  [a1b2c3d4e5f67890] [abc123def4567890-xyz789abc123def4] c.c.g.o.RequestTracingFilter - 请求开始
```

### 5. 检查访问日志

```bash
tail -f logs/carlos-gateway-access.log
```

输出：

```
2025-03-26 14:30:25.123|a1b2c3d4e5f67890|abc123def4567890|GET|/api/users|200|45|127.0.0.1
```

## 常见问题

### Q: 新的过滤器没有生效？

**A:**

1. 检查 `carlos.gateway.tracing.enabled=true`
2. 检查是否有其他过滤器优先级更高，阻塞了新过滤器
3. 查看日志是否有 `[Gateway Tracing]` 开头的调试信息

### Q: 日志中没有显示 Request ID？

**A:**

1. 确认使用的是 `logback-spring.xml` 而不是其他日志配置
2. 确认日志格式包含 `%X{requestId}`
3. 确认过滤器已执行（添加断点或调试日志）

### Q: 如何关闭新的过滤器回退到旧实现？

**A:**

```yaml
carlos:
  gateway:
    tracing:
      enabled: false
```

此时会自动启用 `ReqHeaderFilter`。

## 回滚方案

如果需要回滚到升级前的状态：

1. 在 `application.yml` 中关闭新过滤器：
   ```yaml
   carlos:
     gateway:
       tracing:
         enabled: false
   ```

2. 或者删除/注释掉 `RequestTracingFilter` 的 Bean 定义（在 `GatewayConfig.java` 中）

3. 恢复原有的 `ReqHeaderFilter` 和 `TracingFilter`（移除 `@Deprecated` 和条件注解）

## 联系支持

如有问题，请联系：

- 项目维护者：Carlos
- 相关模块：`carlos-gateway`, `carlos-spring-boot-starter-apm`
