# Carlos Gateway 全局异常处理机制

## 概述

Carlos Gateway 提供了统一的全局异常处理机制，确保所有网关层面的异常都能返回标准化的 JSON 格式响应，便于前端统一处理和问题排查。

## 特性

- **标准化响应格式**：所有异常返回统一的 `ErrorResponse` 格式
- **细粒度的异常分类**：支持多种网关特定异常类型
- **HTTP 状态码映射**：自动将业务错误码映射为合适的 HTTP 状态码
- **开发/生产环境适配**：支持开发模式显示详细错误信息
- **日志记录**：自动记录异常日志，区分不同级别的日志输出

## 异常类型

### 1. 网关基础异常

| 异常类                          | HTTP 状态码 | 业务错误码 | 说明                 |
|------------------------------|----------|-------|--------------------|
| `GatewayException`           | 500      | 5000  | 网关基础异常父类           |
| `AuthenticationException`    | 401      | 5104  | 认证失败（Token 无效、过期等） |
| `AuthorizationException`     | 403      | 5106  | 授权失败（权限不足）         |
| `RateLimitException`         | 429      | 5429  | 请求限流               |
| `CircuitBreakerException`    | 503      | 5503  | 熔断降级               |
| `WafBlockException`          | 403      | 5403  | WAF 拦截             |
| `ServiceNotFoundException`   | 404      | 4004  | 服务未找到              |
| `RequestTimeoutException`    | 504      | 5504  | 请求超时               |
| `RequestValidationException` | 400      | 5001  | 参数校验失败             |
| `ReplayAttackException`      | 403      | 54031 | 重放攻击检测             |

### 2. 支持的第三方异常

- `NotFoundException` - Spring Cloud Gateway 服务未找到
- `ResponseStatusException` - Spring WebFlux 响应状态异常
- `WebClientResponseException` - 下游服务返回错误
- `TimeoutException` - 请求超时
- `ConnectException` - 连接失败
- `GlobalException` - Carlos 核心模块全局异常

## 响应格式

### 标准错误响应

```json
{
  "success": false,
  "status": 429,
  "code": 5429,
  "message": "请求过于频繁，请稍后重试",
  "detail": null,
  "stack": null,
  "path": "/api/v1/users",
  "method": "GET",
  "requestId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "timestamp": 1711245678901,
  "extra": {
    "limitDimension": "IP",
    "limitRate": 10,
    "retryAfter": 60
  }
}
```

### 字段说明

| 字段          | 类型      | 说明                |
|-------------|---------|-------------------|
| `success`   | boolean | 是否成功（始终为 false）   |
| `status`    | int     | HTTP 状态码          |
| `code`      | int     | 业务错误码             |
| `message`   | string  | 用户友好的错误消息         |
| `detail`    | string  | 详细错误信息（开发模式显示）    |
| `stack`     | string  | 异常堆栈（仅开发模式显示）     |
| `path`      | string  | 请求路径              |
| `method`    | string  | 请求方法              |
| `requestId` | string  | 请求 ID（链路追踪）       |
| `timestamp` | long    | 时间戳（毫秒）           |
| `extra`     | object  | 扩展字段（特定异常类型的额外信息） |

## 配置

### 基本配置

```yaml
carlos:
  gateway:
    exception:
      # 开发模式：显示详细错误信息
      dev-mode: false
      # 是否显示异常堆栈信息
      show-stack-trace: false
```

### 环境建议

**开发环境**：

```yaml
carlos:
  gateway:
    exception:
      dev-mode: true
      show-stack-trace: true
```

**生产环境**：

```yaml
carlos:
  gateway:
    exception:
      dev-mode: false
      show-stack-trace: false
```

## 使用示例

### 1. 在过滤器中抛出异常

```java
@Component
public class CustomFilter implements GlobalFilter {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        // 检查某些条件，不满足时抛出异常
        if (isInvalidRequest(exchange)) {
            throw new RequestValidationException("Invalid request parameters")
                .addFieldError("param1", "Value is required");
        }
        
        return chain.filter(exchange);
    }
}
```

### 2. 在 Reactive 链中返回异常响应

```java
@Component
public class CustomFilter implements GlobalFilter {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return validateToken(exchange)
            .flatMap(valid -> {
                if (!valid) {
                    return writeErrorResponse(exchange.getResponse(), 
                        new AuthenticationException("Token expired", 
                            AuthenticationException.AuthFailureType.TOKEN_EXPIRED));
                }
                return chain.filter(exchange);
            });
    }
    
    private Mono<Void> writeErrorResponse(ServerHttpResponse response, GatewayException ex) {
        // 使用 ErrorResponse 构建响应
        ErrorResponse error = ErrorResponse.builder()
            .status(ex.getHttpStatus())
            .code(ex.getErrorCode())
            .message(ex.getMessage())
            .build();
        
        response.setStatusCode(HttpStatus.valueOf(ex.getHttpStatus()));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        return response.writeWith(Mono.fromSupplier(() -> {
            try {
                byte[] bytes = objectMapper.writeValueAsBytes(error);
                return response.bufferFactory().wrap(bytes);
            } catch (Exception e) {
                return response.bufferFactory().wrap("{}".getBytes());
            }
        }));
    }
}
```

### 3. 使用特定异常类型

```java
// 限流异常
throw new RateLimitException("请求过于频繁", "IP", 100, 60);

// 熔断异常
throw new CircuitBreakerException("服务暂时不可用", "user-service", "OPEN", 75.5f);

// WAF 拦截异常
throw new WafBlockException("检测到 SQL 注入攻击", "SQL_INJECTION", "sqli_rule_001");

// 服务未找到
throw new ServiceNotFoundException("user-service", "/api/v1/users");
```

## 日志记录

异常处理器会自动记录异常日志：

- **WARN 级别**：已知业务异常（认证失败、限流、WAF 拦截等）
- **ERROR 级别**：系统异常（连接失败、超时、未知异常等）

日志格式：

```
[request-id] GET /api/v1/users - AuthenticationException: Token expired
```

## 扩展

### 自定义异常类型

1. 继承 `GatewayException`：

```java
public class CustomBusinessException extends GatewayException {
    
    public CustomBusinessException(String message) {
        super(message, 422, 5422);  // HTTP 422, 业务码 5422
    }
}
```

2. 在 `GatewayExceptionHandler` 中添加处理逻辑：

```java
private void handleGatewayException(GatewayException ex, ErrorResponse.ErrorResponseBuilder builder) {
    // ... 现有代码 ...
    
    if (ex instanceof CustomBusinessException customEx) {
        extra.put("customField", customEx.getCustomField());
    }
}
```

## 注意事项

1. **生产环境**：务必关闭 `dev-mode` 和 `show-stack-trace`，避免泄露敏感信息
2. **异常链**：在 Reactive 编程中，建议使用 `onErrorResume` 捕获异常并返回统一响应
3. **性能**：异常处理会消耗一定性能，高频异常建议记录并告警
4. **兼容性**：与 Spring Cloud Gateway 的默认异常处理兼容，优先使用自定义处理器

## 测试

可以使用以下方式测试异常处理：

```bash
# 测试限流（快速发送多个请求）
for i in {1..30}; do curl -s http://localhost:9510/api/v1/test; done

# 测试 WAF（SQL 注入）
curl -s "http://localhost:9510/api/v1/test?id=1' OR '1'='1"

# 测试不存在的路由
curl -s http://localhost:9510/api/v1/nonexistent

# 测试超时（需要配置一个慢响应的后端）
curl -s http://localhost:9510/api/v1/slow-endpoint
```
