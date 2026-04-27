# Carlos Auth 统一认证中心技术设计深度分析

> 基于 carlos-auth 模块现有代码，对统一认证中心和系统登录的 5 个核心技术设计问题进行深度分析。
> 分析时间：2026-04-16

---

## 问题2：网关如何识别 Token 类型（客户端凭证 vs 用户授权）

### 核心问题本质

`/oauth2/token` 是统一端点，不同 `grant_type` 都走这里。网关拿到 JWT 后必须能区分：

- **用户 Token**：有 `user_id`，代表具体用户，按用户权限控制
- **客户端 Token**：无 `user_id`，代表服务/应用，只能访问开放 API

---

### 当前框架的区分机制（代码级）

**客户端 Token**（`CustomizeClientOAuth2TokenCustomizer.java:66-84`）：

```json
{
  "sub": "third-party-service",
  "client_id": "third-party-service",
  "grant_type": "client_credentials",
  "token_type": "client_token"
}
```

**用户 Token**（`Oauth2JwtTokenCustomizer.java:107-175`）：

```json
{
  "sub": "zhangsan",
  "user_id": 10001,
  "username": "zhangsan",
  "role_ids": [1, 2],
  "authorities": "read,write,ROLE_1,ROLE_2"
}
```

识别规则：`token_type == "client_token"` → 客户端凭证；`user_id != null` → 用户授权。

---

### 网关实现方案

**全局过滤器（推荐）**

```java
@Component
@Order(-1)
public class TokenTypeGatewayFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = extractBearerToken(exchange);
        if (token == null) return chain.filter(exchange);

        return jwtDecoder.decode(token).flatMap(jwt -> {
            String tokenType = jwt.getClaimAsString("token_type");
            String path = exchange.getRequest().getPath().value();

            if ("client_token".equals(tokenType)) {
                if (!path.startsWith("/api/open/")) {
                    return unauthorized(exchange, "客户端凭证不能访问用户资源");
                }
                ServerHttpRequest req = exchange.getRequest().mutate()
                    .header("X-Client-Id", jwt.getClaimAsString("client_id"))
                    .header("X-Token-Type", "client").build();
                return chain.filter(exchange.mutate().request(req).build());
            }

            Long userId = jwt.getClaim("user_id");
            if (userId != null) {
                ServerHttpRequest req = exchange.getRequest().mutate()
                    .header("X-User-Id", userId.toString())
                    .header("X-Token-Type", "user").build();
                return chain.filter(exchange.mutate().request(req).build());
            }
            return unauthorized(exchange, "无效的 Token 类型");
        });
    }
}
```

---

### 深层设计建议：使用 JWT `aud` 约束

仅靠 `token_type` claim 是软约束，更安全的方式是使用 JWT 标准的 `aud` 字段：

```json
{ "aud": ["open-api"] }           // 客户端 Token：只能访问 open-api
{ "aud": ["user-api", "open-api"] } // 用户 Token：可访问两者
```

网关验证 `aud` 是否包含当前服务标识，不匹配直接拒绝（RFC 7519 标准行为）。

---

### 技术陷阱

**陷阱1：`token_type` 与 JWT 标准字段冲突**
JWT 标准中 `token_type` 通常是 `Bearer`，当前框架在 Claims 里自定义了 `token_type: "client_token"` 是非标准用法。建议改用
`carlos_token_type` 或 `principal_type` 避免混淆。

**陷阱2：客户端 Token 不支持 refresh_token**
`BaseAuthenticationProvider.java:177` 中 refresh_token 只颁发给用户 Token，客户端凭证模式需要重新申请。

**陷阱3：Token 撤销范围**
`/oauth2/revoke` 只撤销该 Token 本身，不影响同一用户的其他 Token。

---

## 问题3：多种登录场景的统一设计

### 场景矩阵

| 场景        | 推荐入口                    | 底层 GrantType         | 是否需要浏览器 |
|-----------|-------------------------|----------------------|---------|
| 账号密码登录    | `POST /auth/user/login` | 直接登录                 | 否       |
| 手机验证码登录   | `POST /oauth2/token`    | `sms_code`           | 否       |
| 微信/钉钉扫码   | `GET /oauth2/authorize` | `authorization_code` | 是       |
| 单点登录(SSO) | `GET /oauth2/authorize` | `authorization_code` | 是       |
| 三方服务端调用   | `POST /oauth2/token`    | `client_credentials` | 否       |
| 应用免密跳转    | 自定义 `/sso/ticket`       | `ticket`             | 是       |

---

### 各场景具体实现

**1. 账号密码登录**（内部系统，不走 OAuth2 标准流程）

```
POST /auth/user/login
{ "username": "admin", "password": "123456", "clientId": "web-app" }
```

**2. 手机验证码登录**（已支持：`SmsAuthenticationProvider.java`）

```
POST /oauth2/token
Authorization: Basic base64(client_id:secret)
grant_type=sms_code&phone=13800138000&sms_code=123456
```

**3. 微信/钉钉登录**（推荐：扩展 `grant_type=social`）

```
// 前端拿到第三方 code 后
POST /oauth2/token
grant_type=social&social_type=wechat&social_code=WECHAT_CODE
```

**4. 单点登录（SSO）**

```
// 业务系统 A 未登录 → 跳转
302 → https://auth.carlos.com/oauth2/authorize?
       response_type=code&client_id=app-a&redirect_uri=...&state=xxx

// 用户已在 auth 登录（Session 有效）→ 访问业务系统 B 时无需再次输入密码
```

**5. 三方服务端调用**

```
POST /oauth2/token
grant_type=client_credentials&scope=read
Authorization: Basic base64(client_id:secret)
```

**6. 免密跳转（应用管理→三方系统）**

```java
// 应用管理模块生成一次性 Ticket
redis.set("sso:ticket:" + ticket, userId, 5, MINUTES);
// 返回跳转链接：https://third-party.com/login?ticket=xxx

// 三方系统验证 ticket
POST /sso/ticket/verify { "ticket": "xxx" }
// carlos-auth 验证通过后生成用户 Token 返回
```

---

### 关键设计决策

**决策1：密码登录是否走 `/oauth2/token`？**
当前框架选择不走，原因：内部系统不需要 client_id/client_secret 约束，接口更简洁。但如果需要第三方客户端接入，则必须通过
`/oauth2/token`。

**决策2：微信/钉钉用 `authorization_code` 还是自定义 `grant_type`？**

- 浏览器（SPA/PCWeb）：推荐 `authorization_code`，浏览器自动跳转体验好
- App/小程序：推荐 `grant_type=social`，前端直接拿 code 传给后端

---

### 技术陷阱

**陷阱1：SSO Session 的域名限制**
`JSESSIONID` Cookie 只在当前域名有效。`auth.carlos.com` 和 `app-a.carlos.com` 可共享（同根域），但跨域系统无法共享
Session，需要改用 OIDC 或 token 透传方案。

**陷阱2：扫码登录的竞态条件**
微信扫码时 PC 端需要轮询状态，当前框架没有内置扫码状态机，需要在 `qr_code`
授权类型外单独设计状态流转（pending/scanned/confirmed/expired）。

**陷阱3：不同登录入口的 Token Claims 一致性**
`/auth/user/login` 和 `/oauth2/token` 都使用 `OAuth2TokenGenerator`，但需要确认 `Oauth2JwtTokenCustomizer` 对两种路径都生效，否则
Claims 会不一致。

---

## 问题1：授权码模式下，授权码是如何获得的？

### 核心认知纠正

授权码来自 `/oauth2/authorize`，而非 `/oauth2/token`。两个端点职责完全不同：

| 端点                  | 调用方       | 返回               | 协议层       |
|---------------------|-----------|------------------|-----------|
| `/oauth2/authorize` | 浏览器（用户代理） | 302重定向（code在URL） | HTTP重定向   |
| `/oauth2/token`     | 业务系统服务端   | JSON（token在Body） | HTTP POST |

---

### 完整授权码获取流程

**第一阶段：获取授权码（浏览器参与）**

```
浏览器                    carlos-auth                  业务系统后端
  │                           │                              │
  │  GET /oauth2/authorize    │                              │
  │  ?response_type=code      │                              │
  │  &client_id=app-a         │                              │
  │  &redirect_uri=...        │                              │
  │  &state=random_csrf_token │                              │
  │──────────────────────────>│                              │
  │                           │ 检查 SecurityContext/Session │
  │  [未登录] 302 → /login    │                              │
  │<──────────────────────────│                              │
  │  POST /auth/user/login    │                              │
  │──────────────────────────>│ 认证成功，写入SecurityContext │
  │  302 → /oauth2/authorize  │                              │
  │<──────────────────────────│                              │
  │  GET /oauth2/authorize    │                              │
  │──────────────────────────>│ 生成一次性授权码(5分钟有效)    │
  │                           │ redis: oauth2:code:{code}    │
  │  302 → redirect_uri       │                              │
  │  ?code=AbCdEf123          │                              │
  │  &state=random_csrf_token │                              │
  │<──────────────────────────│                              │
  │  GET /callback?code=...   │                              │
  │──────────────────────────────────────────────────────────>│
```

**第二阶段：用授权码换Token（服务端对服务端）**

```
业务系统后端
  │  POST /oauth2/token
  │  Authorization: Basic base64(client_id:secret)
  │  grant_type=authorization_code
  │  &code=AbCdEf123
  │  &redirect_uri=...
  │──────────────────> carlos-auth
  │                    验证code有效性 + 验证redirect_uri匹配
  │                    删除Redis中的code（一次性）
  │                    生成JWT accessToken
  │<──────────────────
  │  {access_token, refresh_token, ...}
```

---

### 关键设计细节（结合代码）

**1. 授权码的存储机制**

`RedisOAuth2AuthorizationService` 中，授权码存储在：

```
oauth2:code:{code_value}  →  OAuth2Authorization 序列化对象
TTL = 5分钟（AuthorizationServerProperties.authorizationCodeTimeToLive）
```

授权码一旦被使用就立即删除，这是防重放的关键。

**2. state 参数的作用（CSRF防护）**

浏览器发起请求时携带 `state=random_csrf_token`，授权码回调时业务系统必须验证 state 与发起时一致，防止 CSRF 攻击。

**3. 为什么授权码要经过浏览器？**

- Token 不经过浏览器（防止被浏览器历史/日志泄露）
- 授权码经过浏览器（但一次性、短期有效，即使泄露危害有限）
- 业务系统服务端持有 client_secret（浏览器不知道 secret）

**4. 当前框架的登录态维持**

`carlos-auth` 使用 Spring Security Session 维持登录态，浏览器携带 `JSESSIONID` Cookie。这是 SSO 的基础：用户在 auth
登录一次，访问其他系统时 Session 仍有效，无需再次输入密码。

---

### 技术陷阱

**陷阱1：redirect_uri 必须精确匹配**
注册客户端时配置的 `redirect_uri` 必须与请求中的完全一致（包括尾部斜杠）。

**陷阱2：前后端分离场景的特殊处理**
在授权码模式下，登录完成后需要重定向回 `/oauth2/authorize`，而不是直接返回 token，需要前端配合处理。

**陷阱3：SPA 应用必须使用 PKCE**
纯前端应用无法安全存储 `client_secret`，必须启用 PKCE：

```yaml
clients:
  - client-id: spa-app
    require-proof-key: true
```

---
