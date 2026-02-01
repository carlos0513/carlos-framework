# carlos-oauth2

OAuth2认证授权模块，基于Spring Security OAuth2 Authorization Server实现。

## 功能特性

- ✅ OAuth2授权服务器（Authorization Server）
- ✅ OAuth2资源服务器（Resource Server）
- ✅ JWT Token支持
- ✅ 多种授权模式（授权码、密码、客户端凭证、刷新令牌）
- ✅ 自定义Token增强（用户信息、租户、部门、角色等）
- ✅ 灵活的客户端配置
- ✅ 基于注解的方法级权限控制
- ✅ 与carlos-core的用户上下文集成

## 快速开始

### 1. 添加依赖

在你的项目`pom.xml`中添加：

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-oauth2</artifactId>
</dependency>
```

### 2. 配置授权服务器

在`application.yml`中配置：

```yaml
carlos:
  oauth2:
    enabled: true

    # 授权服务器配置
    authorization-server:
      enabled: true
      access-token-time-to-live: 2h
      refresh-token-time-to-live: 7d

    # JWT配置
    jwt:
      issuer: http://localhost:8080
      include-user-info: true

    # 客户端配置
    clients:
      - client-id: my-client
        client-secret: my-secret
        client-name: My Application
        authorization-grant-types:
          - authorization_code
          - refresh_token
          - password
        redirect-uris:
          - http://localhost:8080/authorized
        scopes:
          - read
          - write
```

### 3. 实现用户服务

创建自定义的`OAuth2UserDetailsService`实现：

```java
@Service
public class MyUserDetailsService implements OAuth2UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserInfo userInfo = loadLoginUserInfo(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return convertToUserDetails(userInfo);
    }

    @Override
    public LoginUserInfo loadLoginUserInfo(String username) {
        // 从数据库加载用户信息
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }

        LoginUserInfo userInfo = new LoginUserInfo();
        userInfo.setId(user.getId());
        userInfo.setAccount(user.getUsername());
        userInfo.setPassword(user.getPassword());
        userInfo.setClientId(user.getTenantId());
        userInfo.setRoleIds(user.getRoleIds());
        userInfo.setDepartmentId(user.getDeptId());
        userInfo.setEnable(user.getEnabled());
        return userInfo;
    }
}
```

### 4. 配置资源服务器

如果你的应用需要作为资源服务器验证JWT Token：

```yaml
carlos:
  oauth2:
    enabled: true

    # 资源服务器配置
    resource-server:
      enabled: true
      jwk-set-uri: http://localhost:8080/oauth2/jwks
      # 或使用 issuer-uri
      # issuer-uri: http://localhost:8080
      permit-all-paths:
        - /public/**
        - /health
```

## 使用示例

### 获取Token

#### 1. 授权码模式（Authorization Code）

```bash
# 1. 获取授权码
GET http://localhost:8080/oauth2/authorize?
    response_type=code&
    client_id=my-client&
    redirect_uri=http://localhost:8080/authorized&
    scope=read write

# 2. 使用授权码获取Token
POST http://localhost:8080/oauth2/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic bXktY2xpZW50Om15LXNlY3JldA==

grant_type=authorization_code&
code=<授权码>&
redirect_uri=http://localhost:8080/authorized
```

#### 2. 密码模式（Password）

```bash
POST http://localhost:8080/oauth2/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic bXktY2xpZW50Om15LXNlY3JldA==

grant_type=password&
username=admin&
password=admin123&
scope=read write
```

#### 3. 客户端凭证模式（Client Credentials）

```bash
POST http://localhost:8080/oauth2/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic bXktY2xpZW50Om15LXNlY3JldA==

grant_type=client_credentials&
scope=read write
```

#### 4. 刷新令牌（Refresh Token）

```bash
POST http://localhost:8080/oauth2/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic bXktY2xpZW50Om15LXNlY3JldA==

grant_type=refresh_token&
refresh_token=<刷新令牌>
```

### 使用Token访问资源

```bash
GET http://localhost:8080/api/users
Authorization: Bearer <access_token>
```

### 在代码中获取用户信息

```java
import com.carlos.oauth2.util.OAuth2Util;
import com.carlos.core.auth.UserContext;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/current-user")
    public Result<UserContext> getCurrentUser() {
        // 获取当前用户上下文
        UserContext userContext = OAuth2Util.extractUserContext();
        return Result.ok(userContext);
    }

    @GetMapping("/user-id")
    public Result<Long> getCurrentUserId() {
        // 获取当前用户ID
        Long userId = OAuth2Util.getCurrentUserId();
        return Result.ok(userId);
    }

    @GetMapping("/user-name")
    public Result<String> getCurrentUserName() {
        // 获取当前用户名
        String userName = OAuth2Util.getCurrentUserName();
        return Result.ok(userName);
    }
}
```

### 方法级权限控制

```java
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // 需要ADMIN角色
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public Result<List<User>> listUsers() {
        // ...
    }

    // 需要特定权限
    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        // ...
    }

    // 需要任意一个角色
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/users")
    public Result<User> createUser(@RequestBody User user) {
        // ...
    }
}
```

## 配置说明

### OAuth2Properties

| 配置项                     | 类型      | 默认值  | 说明           |
|-------------------------|---------|------|--------------|
| `carlos.oauth2.enabled` | Boolean | true | 是否启用OAuth2模块 |

### AuthorizationServer配置

| 配置项                                                    | 类型       | 默认值                | 说明        |
|--------------------------------------------------------|----------|--------------------|-----------|
| `authorization-server.enabled`                         | Boolean  | false              | 是否启用授权服务器 |
| `authorization-server.authorization-endpoint`          | String   | /oauth2/authorize  | 授权端点路径    |
| `authorization-server.token-endpoint`                  | String   | /oauth2/token      | Token端点路径 |
| `authorization-server.token-revocation-endpoint`       | String   | /oauth2/revoke     | Token撤销端点 |
| `authorization-server.token-introspection-endpoint`    | String   | /oauth2/introspect | Token自省端点 |
| `authorization-server.jwk-set-endpoint`                | String   | /oauth2/jwks       | JWK Set端点 |
| `authorization-server.authorization-code-time-to-live` | Duration | 5m                 | 授权码有效期    |
| `authorization-server.access-token-time-to-live`       | Duration | 2h                 | 访问令牌有效期   |
| `authorization-server.refresh-token-time-to-live`      | Duration | 7d                 | 刷新令牌有效期   |
| `authorization-server.reuse-refresh-tokens`            | Boolean  | false              | 是否重用刷新令牌  |

### ResourceServer配置

| 配置项                                | 类型           | 默认值   | 说明          |
|------------------------------------|--------------|-------|-------------|
| `resource-server.enabled`          | Boolean      | false | 是否启用资源服务器   |
| `resource-server.jwk-set-uri`      | String       | -     | JWK Set URI |
| `resource-server.issuer-uri`       | String       | -     | Issuer URI  |
| `resource-server.permit-all-paths` | List<String> | []    | 不需要认证的路径    |

### JWT配置

| 配置项                     | 类型      | 默认值                   | 说明            |
|-------------------------|---------|-----------------------|---------------|
| `jwt.algorithm`         | String  | RS256                 | JWT签名算法       |
| `jwt.key-id`            | String  | carlos-key-id         | 密钥ID          |
| `jwt.issuer`            | String  | http://localhost:8080 | Issuer        |
| `jwt.include-user-info` | Boolean | true                  | 是否在JWT中包含用户信息 |

### Client配置

| 配置项                                       | 类型           | 说明       |
|-------------------------------------------|--------------|----------|
| `clients[].client-id`                     | String       | 客户端ID    |
| `clients[].client-secret`                 | String       | 客户端密钥    |
| `clients[].client-name`                   | String       | 客户端名称    |
| `clients[].authorization-grant-types`     | List<String> | 授权类型     |
| `clients[].redirect-uris`                 | List<String> | 重定向URI   |
| `clients[].scopes`                        | List<String> | 作用域      |
| `clients[].require-authorization-consent` | Boolean      | 是否需要授权同意 |
| `clients[].require-proof-key`             | Boolean      | 是否需要PKCE |
| `clients[].access-token-time-to-live`     | Duration     | 访问令牌有效期  |
| `clients[].refresh-token-time-to-live`    | Duration     | 刷新令牌有效期  |

## JWT Token结构

生成的JWT Token包含以下Claims：

```json
{
  "sub": "admin",
  "aud": ["my-client"],
  "nbf": 1706169600,
  "scope": ["read", "write"],
  "iss": "http://localhost:8080",
  "exp": 1706176800,
  "iat": 1706169600,
  "jti": "xxx-xxx-xxx",
  "user_id": 1,
  "user_name": "admin",
  "tenant_id": "default-client",
  "dept_id": 1,
  "role_ids": [1, 2],
  "authorities": "ROLE_1,ROLE_2,user:read,user:write"
}
```

## 工具类

### OAuth2Util

提供便捷的方法从JWT Token中提取用户信息：

- `getAuthentication()` - 获取当前认证信息
- `getCurrentJwt()` - 获取当前JWT Token
- `extractUserContext()` - 提取用户上下文
- `getCurrentUserId()` - 获取当前用户ID
- `getCurrentUserName()` - 获取当前用户名
- `getCurrentTenantId()` - 获取当前租户ID
- `getCurrentDeptId()` - 获取当前部门ID
- `getCurrentRoleIds()` - 获取当前用户角色ID列表
- `getCurrentAuthorities()` - 获取当前用户权限列表
- `hasAuthority(String)` - 判断是否有指定权限
- `hasRole(String)` - 判断是否有指定角色
- `hasAnyAuthority(String...)` - 判断是否有任意一个指定权限
- `hasAnyRole(String...)` - 判断是否有任意一个指定角色

## 注意事项

1. **生产环境配置**：默认的`DefaultOAuth2UserDetailsService`仅用于测试，生产环境必须实现自己的`OAuth2UserDetailsService`
2. **密钥管理**：生产环境建议使用外部密钥存储，而不是自动生成的密钥
3. **HTTPS**：生产环境必须使用HTTPS
4. **客户端密钥**：客户端密钥会自动使用BCrypt加密
5. **Token存储**：当前使用内存存储，生产环境建议使用Redis等持久化存储

## 与其他模块集成

### 与carlos-core集成

OAuth2模块与carlos-core的认证体系完全集成：

- 使用`LoginUserInfo`作为用户信息载体
- 使用`UserContext`作为用户上下文
- 支持租户、部门、角色等多租户架构

### 与carlos-redis集成

可以使用Redis存储Token和授权信息（需要自定义实现）。

## 扩展开发

### 自定义Token增强

```java
@Bean
public OAuth2TokenCustomizer<JwtEncodingContext> customTokenEnhancer() {
    return context -> {
        if (OAuth2ParameterNames.ACCESS_TOKEN.equals(context.getTokenType().getValue())) {
            // 添加自定义Claims
            context.getClaims().claim("custom_field", "custom_value");
        }
    };
}
```

### 自定义授权逻辑

```java
@Component
public class CustomAuthorizationService {

    @Autowired
    private RegisteredClientRepository clientRepository;

    public boolean validateClient(String clientId, String clientSecret) {
        // 自定义客户端验证逻辑
        return true;
    }
}
```

## 常见问题

### Q: 如何在微服务架构中使用？

A: 在微服务架构中，通常有一个独立的认证服务作为授权服务器，其他服务作为资源服务器：

- 认证服务：启用`authorization-server.enabled=true`
- 其他服务：启用`resource-server.enabled=true`，配置`jwk-set-uri`指向认证服务

### Q: 如何自定义登录页面？

A: 在授权服务器配置中，Spring Security会使用默认的登录页面。可以通过自定义`SecurityFilterChain`来配置自定义登录页面。

### Q: 如何撤销Token？

A: 使用Token撤销端点：

```bash
POST http://localhost:8080/oauth2/revoke
Authorization: Basic <client_credentials>
Content-Type: application/x-www-form-urlencoded

token=<access_token>
```

## 版本历史

- v3.0.0-SNAPSHOT - 初始版本，基于Spring Security OAuth2 Authorization Server

## 技术栈

- Spring Security 6.x
- Spring Security OAuth2 Authorization Server 1.x
- Spring Security OAuth2 Resource Server
- Nimbus JOSE + JWT
- Spring Boot 3.x

## 参考资料

- [Spring Authorization Server官方文档](https://docs.spring.io/spring-authorization-server/docs/current/reference/html/)
- [OAuth 2.0 RFC 6749](https://tools.ietf.org/html/rfc6749)
- [JWT RFC 7519](https://tools.ietf.org/html/rfc7519)
