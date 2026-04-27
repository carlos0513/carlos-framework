# JWT 令牌颁发实现总结

## 实现概述

已完成 JWT 令牌颁发功能的实现，替代了之前的 TODO 空实现。

---

## 修改文件清单

### 1. 新增文件

| 文件 | 说明 |
|------|------|
| `JwtTokenProvider.java` | JWT 令牌提供者，封装令牌生成逻辑 |

**路径：**
```
carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/security/JwtTokenProvider.java
```

**主要功能：**
- `generateAccessToken()` - 生成访问令牌（有效期2小时）
- `generateRefreshToken()` - 生成刷新令牌（有效期7天）
- `generateTokens()` - 生成完整的令牌响应

**令牌包含的声明（Claims）：**
- `iss` - 发行者（carlos-auth-server）
- `sub` - 用户名
- `iat` - 签发时间
- `exp` - 过期时间
- `jti` - 令牌唯一标识
- `user_id` - 用户ID
- `username` - 用户名
- `tenant_id` - 租户ID（如存在）
- `dept_id` - 部门ID（如存在）
- `role_ids` - 角色ID列表
- `authorities` - 权限列表
- `scope` - 作用域

---

### 2. 修改文件

#### LoginService.java

**修改点 1：** 添加依赖注入
```java
@Autowired
private JwtTokenProvider jwtTokenProvider;
```

**修改点 2：** 修改 `buildLoginResponse()` 方法
```java
// 修复前（TODO）
return LoginResponse.builder()
    .accessToken("")  // 空令牌
    .tokenType("Bearer")
    .expiresIn(7200L)
    .userInfo(userInfo)
    .build();

// 修复后
JwtTokenProvider.TokenResponse tokenResponse = jwtTokenProvider.generateTokens(authentication, user);

return LoginResponse.builder()
    .accessToken(tokenResponse.getAccessToken())
    .refreshToken(tokenResponse.getRefreshToken())
    .tokenType(tokenResponse.getTokenType())
    .expiresIn(tokenResponse.getExpiresIn())
    .userInfo(userInfo)
    .build();
```

#### OAuth2AuthorizationServerConfig.java

**新增 Bean：** `JwtEncoder`
```java
@Bean
@ConditionalOnMissingBean
public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
    return new NimbusJwtEncoder(jwkSource);
}
```

**新增 Import：**
```java
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
```

---

## 架构说明

```
┌─────────────────┐
│   LoginService  │
│   .login()      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ JwtTokenProvider│
│ .generateTokens()│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   JwtEncoder    │
│  (NimbusJwtEncoder)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    JWKSource    │
│   (RSA KeyPair) │
└─────────────────┘
```

---

## 配置说明

JWT 配置通过 `OAuth2Properties` 自动注入：

```yaml
carlos:
  oauth2:
    jwt:
      key-store: ~/.carlos/auth/auth.jks  # JKS 文件路径
      key-store-password: ${JKS_PASSWORD:}
      key-alias: auth-key
      key-password: ${KEY_PASSWORD:}
      key-id: carlos-key-1
      issuer: http://localhost:8080
      include-user-info: true
```

---

## 测试验证

登录成功后，响应示例：

```json
{
  "accessToken": "eyJraWQiOiJjYXJsb3Mta2V5LTEiLCJhbGciOiJSUzI1NiJ9...",
  "refreshToken": "eyJraWQiOiJjYXJsb3Mta2V5LTEiLCJhbGciOiJSUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 7200,
  "userInfo": {
    "id": 1,
    "username": "admin",
    "email": "admin@carlos.com",
    "phone": "13800000000"
  },
  "mfaRequired": false,
  "mfaRecommended": false
}
```

---

## 后续优化建议

1. **令牌黑名单** - 实现登出时的令牌失效机制
2. **令牌刷新** - 完善刷新令牌逻辑
3. **多设备登录** - 支持同一用户多设备登录管理
4. **令牌存储** - 可选的 Redis 令牌存储

---

*实现时间：2026-04-03*
*实现者：泡泡*
