# 网关认证过滤器安全漏洞修复报告

## 修复时间
2026-04-25

## 漏洞描述

**严重级别**: 🔴 P0 (高危)

**问题位置**: `carlos-gateway/src/main/java/com/carlos/gateway/oauth2/OAuth2AuthenticationFilter.java`

**漏洞详情**:
`OAuth2AuthenticationFilter.filter()` 在以下两种情况下直接放行未认证请求到下游服务：

1. **Token 验证失败时** — `tokenValidator.validate(token)` 抛异常后，`onErrorResume` 捕获异常并直接执行 `chain.filter(exchange)`，未认证请求穿透到下游微服务
2. **请求未携带 Token 时** — `extractToken(request)` 返回空后直接执行 `chain.filter(exchange)`

**安全风险**: 攻击者可构造无 Token 或非法 Token 的请求，绕过网关认证直接访问受保护的下游服务，造成未授权访问。

## 修复内容

### 修改前
```java
// 无 Token 时放行
if (StrUtil.isBlank(token)) {
    log.warn("No token found for path: {}", path);
    return chain.filter(exchange);  // ❌ 直接放行
}

// Token 验证异常时放行
.onErrorResume(e -> {
    log.error("Authentication failed for path: {}", path, e);
    return chain.filter(exchange);  // ❌ 直接放行
});
```

### 修改后
```java
// 无 Token 时返回 401
if (StrUtil.isBlank(token)) {
    log.warn("No token found for path: {}", path);
    return writeUnauthorizedResponse(exchange, CommonErrorCode.UNAUTHORIZED, "未提供认证凭证");
}

// Token 验证异常时返回 401
.onErrorResume(e -> {
    log.error("Authentication failed for path: {}", path, e);
    return writeUnauthorizedResponse(exchange, CommonErrorCode.AUTH_TOKEN_INVALID, "登录凭证无效或已过期");
});
```

### 新增方法
```java
private Mono<Void> writeUnauthorizedResponse(ServerWebExchange exchange, CommonErrorCode errorCode, String message) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.UNAUTHORIZED.value())
        .code(errorCode.getCode())
        .msg(message)
        .path(exchange.getRequest().getURI().getPath())
        .method(exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "UNKNOWN")
        .build();

    byte[] bytes = JSONUtil.toJsonStr(errorResponse).getBytes(StandardCharsets.UTF_8);
    return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
}
```

## 修复后行为

| 场景 | 修改前 | 修改后 |
|------|--------|--------|
| 白名单路径 | 放行 ✅ | 放行 ✅ |
| OPTIONS 预检请求 | 放行 ✅ | 放行 ✅ |
| OAuth2 未启用 | 放行 ✅ | 放行 ✅ |
| 携带有效 Token | 注入用户信息后放行 ✅ | 注入用户信息后放行 ✅ |
| **未携带 Token** | **穿透下游 ❌** | **返回 401 ✅** |
| **Token 验证失败** | **穿透下游 ❌** | **返回 401 ✅** |

## 响应格式

HTTP Status: `401 Unauthorized`
Content-Type: `application/json`

```json
{
  "success": false,
  "status": 401,
  "code": "10002",
  "msg": "未提供认证凭证",
  "path": "/api/protected/resource",
  "method": "GET",
  "timestamp": 1714001234567
}
```

## 安全收益

- ✅ 彻底封堵未认证请求穿透漏洞
- ✅ 统一 401 响应格式，与框架 `CommonErrorCode` 错误码体系一致
- ✅ 与 `ReplayProtectionFilter`、`WafFilter` 等现有过滤器的错误响应风格保持一致
- ✅ 前端可基于 `code` 字段精准处理认证失败场景

## 建议后续措施

1. 在测试环境验证白名单路径和 OPTIONS 请求仍能正常放行
2. 为网关认证过滤器补充单元测试（模拟 Token 验证失败场景）
3. 在 `TokenValidator` 实现中区分 Token 过期 vs Token 无效，返回更精准的错误码 (`AUTH_TOKEN_EXPIRED` vs `AUTH_TOKEN_INVALID`)
