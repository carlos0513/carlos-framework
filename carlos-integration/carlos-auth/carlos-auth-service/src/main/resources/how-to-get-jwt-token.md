# 如何获取JWT访问令牌

## 重要说明

本项目使用 **Spring Authorization Server** 作为OAuth2.1授权服务器，遵循标准的OAuth2.1协议。

**注意**：登录接口 (`POST /auth/login`) 返回的 `accessToken` 字段为空字符串，这是设计意图。

**真实的JWT令牌应通过OAuth2标准的 `/oauth2/token` 端点获取。**

---

## 正确的令牌获取流程

### 方式一：授权码模式（Authorization Code Flow + PKCE）

**推荐用于前端应用（SPA、移动端）**

#### 步骤1：获取授权码

```http
GET /oauth2/authorize
  ?response_type=code
  &client_id=carlos-admin-web
  &redirect_uri=http://localhost:3000/callback
  &scope=read write profile openid
  &state=xyz123
  &code_challenge=SHA256_HASH_OF_CODE_VERIFIER
  &code_challenge_method=S256
```

**参数说明：**

- `response_type`: 固定为 `code`（授权码模式）
- `client_id`: 客户端ID（在oauth2_client表中配置）
- `redirect_uri`: 前端回调地址（必须与客户端配置匹配）
- `scope`: 作用域（openid用于OIDC）
- `state`: 随机字符串（防止CSRF攻击）
- `code_challenge`: PKCE的code_verifier的SHA256哈希值
- `code_challenge_method`: 固定为 `S256`

**响应：** 重定向到 `redirect_uri?code=AUTHORIZATION_CODE&state=xyz123`

---

#### 步骤2：用授权码换取令牌

```http
POST /oauth2/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic Y2FybG9zLWFkbWluLXdlYjpzZWNyZXQ=

grant_type=authorization_code
&code=AUTHORIZATION_CODE
&redirect_uri=http://localhost:3000/callback
&code_verifier=ORIGINAL_CODE_VERIFIER
```

**响应：**

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "refresh_token": "eyJhbGciOiJSUzI1NiJ9...",
  "token_type": "Bearer",
  "expires_in": 900,
  "scope": "read write profile openid",
  "id_token": "eyJhbGciOiJSUzI1NiJ9..."
}
```

---

### 方式二：客户端凭证模式（Client Credentials Flow）

**推荐用于后端服务间调用**

```http
POST /oauth2/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic Y2xpZW50LWlkOnNlY3JldA==

grant_type=client_credentials
&scope=read write
```

**响应：**

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "token_type": "Bearer",
  "expires_in": 3600,
  "scope": "read write"
}
```

---

### 方式三：刷新令牌（Refresh Token）

**用于获取新的访问令牌**

```http
POST /oauth2/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic Y2xpZW50LWlkOnNlY3JldA==

grant_type=refresh_token
&refresh_token=eyJhbGciOiJSUzI1NiJ9...
```

**响应：**

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "refresh_token": "eyJhbGciOiJSUzI1NiJ9...",
  "token_type": "Bearer",
  "expires_in": 900,
  "scope": "read write profile openid"
}
```

---

## 关于 `/auth/login` 端点

### 设计意图

`/auth/login` 端点**不是OAuth2标准端点**，它的作用是：

1. **统一认证入口**：支持用户名/邮箱/手机号三选一登录
2. **安全加固**：实现账号锁定、IP封禁、验证码、速率限制等安全策略
3. **返回MFA状态**：告知客户端是否需要MFA二次验证
4. **触发审计**：记录登录审计日志

### 返回值说明

```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "accessToken": "",  // 空字符串，不用于实际访问
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "userInfo": {
      "id": 1,
      "username": "admin",
      "email": "admin@carlos.com",
      "phone": "13800000000"
    },
    "mfaRequired": false,      // 是否需要MFA验证
    "mfaRecommended": false    // 是否建议启用MFA
  }
}
```

**注意**：`accessToken` 字段为空，客户端必须调用 `/oauth2/token` 获取真实令牌。

---

## 推荐的前端实现流程

### 登录流程（无MFA）

```javascript
// 步骤1：调用登录接口（验证用户名密码）
const loginResponse = await fetch('/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'admin',
    password: 'admin123'
  })
});

const loginData = await loginResponse.json();

if (loginData.data.mfaRequired) {
  // 需要MFA验证，跳转到MFA页面
  redirectToMfaPage();
  return;
}

// 步骤2：发起OAuth2授权请求（获取授权码）
const oauthUrl = `/oauth2/authorize?` +
  `response_type=code` +
  `&client_id=carlos-admin-web` +
  `&redirect_uri=http://localhost:3000/callback` +
  `&scope=read write profile openid` +
  `&state=${generateRandomState()}` +
  `&code_challenge=${codeChallenge}` +
  `&code_challenge_method=S256`;

// 重定向到授权服务器
window.location.href = oauthUrl;

// 步骤3：在回调页面获取授权码
const urlParams = new URLSearchParams(window.location.search);
const code = urlParams.get('code');

// 步骤4：用授权码换取令牌
const tokenResponse = await fetch('/oauth2/token', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
    'Authorization': 'Basic ' + btoa('carlos-admin-web:secret')
  },
  body: new URLSearchParams({
    grant_type: 'authorization_code',
    code: code,
    redirect_uri: 'http://localhost:3000/callback',
    code_verifier: codeVerifier
  })
});

const tokenData = await tokenResponse.json();

// 保存令牌
localStorage.setItem('access_token', tokenData.access_token);
localStorage.setItem('refresh_token', tokenData.refresh_token);
```

---

### 登录流程（有MFA）

```javascript
// 步骤1：调用登录接口
const loginResponse = await fetch('/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'admin',
    password: 'admin123'
  })
});

const loginData = await loginResponse.json();

if (loginData.data.mfaRequired) {
  // 步骤2：获取MFA设置信息
  const mfaSetupResponse = await fetch('/mfa/setup', {
    method: 'POST',
    headers: {
      'Authorization': 'Bearer ' + localStorage.getItem('temp_token')
    }
  });

  // 步骤3：显示QR码，用户用Google Authenticator扫描
  displayQRCode(mfaSetupResponse.data.qrCodeUrl);

  // 步骤4：用户输入MFA验证码
  const mfaCode = prompt('请输入6位验证码');

  // 步骤5：验证MFA（启用MFA）
  await fetch('/mfa/verify', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      secret: mfaSetupResponse.data.secret,
      code: mfaCode
    })
  });

  // 步骤6：重新发起OAuth2授权请求
  window.location.href = oauthUrl;
}
```

---

## 使用访问令牌访问受保护资源

### 在请求头中携带令牌

```javascript
// 获取令牌
const accessToken = localStorage.getItem('access_token');

// 调用API
const response = await fetch('/api/user/profile', {
  headers: {
    'Authorization': 'Bearer ' + accessToken,
    'Content-Type': 'application/json'
  }
});

if (response.status === 401) {
  // 令牌过期，使用refresh_token获取新令牌
  await refreshToken();
}
```

---

## 常见问题

### Q1: 为什么 `/auth/login` 不返回真实令牌？

A: **设计意图**。`/auth/login` 是统一认证入口，它的作用是验证用户身份并实施安全策略（锁定、限流、MFA）。真实的JWT令牌必须通过OAuth2标准端点
`/oauth2/token` 获取，这样可以：

- 遵循OAuth2.1标准，与Spring生态系统兼容
- 支持多种授权类型（授权码、客户端凭证、刷新令牌）
- 便于令牌轮换和吊销
- 支持OIDC（OpenID Connect）身份层

### Q2: 如何配置OAuth2客户端？

A: 在 `oauth2_client` 表中添加客户端配置：

```sql
INSERT INTO oauth2_client (
    id, client_id, client_secret, client_name,
    client_authentication_methods, authorization_grant_types,
    redirect_uris, scopes, require_authorization_consent
) VALUES (
    2,
    'my-app',
    '{bcrypt}$2a$10$...', -- BCrypt加密的密钥
    'My Application',
    'client_secret_basic,client_secret_post',
    'authorization_code,refresh_token',
    'http://localhost:3000/callback,http://localhost:3000/authorized',
    'read,write,profile',
    1  -- 需要用户授权确认
);
```

### Q3: 如何生成PKCE的code_verifier和code_challenge？

A: 使用随机字符串生成code_verifier，然后计算SHA256哈希得到code_challenge：

```javascript
// 生成随机字符串（建议长度：128位）
function generateCodeVerifier() {
  const array = new Uint8Array(32);
  crypto.getRandomValues(array);
  return base64URLEncode(array);
}

// 计算SHA256哈希
async function generateCodeChallenge(codeVerifier) {
  const encoder = new TextEncoder();
  const data = encoder.encode(codeVerifier);
  const digest = await crypto.subtle.digest('SHA-256', data);
  return base64URLEncode(new Uint8Array(digest));
}

// Base64URL编码（移除=，+替换为-，/替换为_）
function base64URLEncode(buffer) {
  return btoa(String.fromCharCode(...buffer))
    .replace(/=/g, '')
    .replace(/\+/g, '-')
    .replace(/\//g, '_');
}
```

### Q4: 为什么需要PKCE？

A: **PKCE (Proof Key for Code Exchange)** 是OAuth2.1的强制要求，用于防止授权码被拦截和重放攻击。即使攻击者获取了授权码，也无法用它来换取令牌，因为不知道code_verifier。

---

## 开发环境快速测试

### 使用Postman测试

1. **获取授权码**
   ```
   GET http://localhost:9511/oauth2/authorize
   ?response_type=code
   &client_id=carlos-admin-web
   &redirect_uri=http://localhost:3000/callback
   &scope=read write profile openid
   &state=test123
   &code_challenge=0j1HPhvLjfz3Eu4T9VvTwxrpQuO6FSXHflVUaVx5kcQ
   &code_challenge_method=S256
   ```

2. **用授权码换取令牌**
   ```http
   POST http://localhost:9511/oauth2/token
   Content-Type: application/x-www-form-urlencoded
   Authorization: Basic Y2FybG9zLWFkbWluLXdlYjpzZWNyZXQ=

   grant_type=authorization_code
   &code=AUTHORIZATION_CODE_FROM_STEP1
   &redirect_uri=http://localhost:3000/callback
   &code_verifier=0j1HPhvLjfz3Eu4T9VvTwxrpQuO6FSXHflVUaVx5kcQ
   ```

3. **使用令牌访问资源**
   ```http
   GET http://localhost:9511/user/me
   Authorization: Bearer eyJhbGciOiJSUzI1NiJ9...
   ```

---

## 生产环境注意事项

1. **使用HTTPS**：所有请求必须通过HTTPS，防止令牌泄露
2. **密钥管理**：定期轮换 `application-auth.yml` 中的JKS密钥
3. **密码强度**：生产环境密码必须满足复杂度要求（大小写+数字+特殊字符）
4. **Rate Limit**：根据业务调整速率限制阈值
5. **监控告警**：关注 `security_alert` 表中的告警事件
6. **日志审计**：定期审查 `audit_login_YYYY_MM` 表
7. **令牌有效期**：生产环境建议 access_token: 15分钟，refresh_token: 7天

---

## 参考资料

- [OAuth 2.1 规范](https://oauth.net/2.1/)
- [Spring Authorization Server 文档](https://docs.spring.io/spring-authorization-server/reference/)
- [PKCE RFC 7636](https://tools.ietf.org/html/rfc7636)
- [OpenID Connect 1.0](https://openid.net/connect/)
