# Carlos OAuth2 模块重构记录

## 重构概述

**重构时间**: 2026-02-22  
**重构版本**: 3.0.0-SNAPSHOT  
**重构目标**: 将 `carlos-spring-boot-starter-oauth2` 合并至 `carlos-auth` 模块，升级至 Spring Boot 3.x 兼容版本

## 主要变更

### 1. 模块合并

#### 1.1 移除的模块

- **模块名**: `carlos-spring-boot-starter-oauth2`
- **原路径**: `carlos-spring-boot/carlos-spring-boot-starter-oauth2/`
- **合并目标**: `carlos-integration/carlos-auth/carlos-auth-service/`

#### 1.2 依赖管理更新

**carlos-spring-boot/pom.xml**:

```xml
<!-- 移除 -->
<module>carlos-spring-boot-starter-oauth2</module>
```

**carlos-dependencies/pom.xml**:

```xml
<!-- 移除 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-oauth2</artifactId>
</dependency>

<!-- 新增 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-auth-service</artifactId>
</dependency>
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-auth-api</artifactId>
</dependency>
```

### 2. 版本升级

#### 2.1 Spring Authorization Server 版本升级

| 组件                          | 旧版本   | 新版本   | 说明     |
|-----------------------------|-------|-------|--------|
| Spring Authorization Server | 0.4.5 | 1.x   | 重大版本升级 |
| Spring Security             | 5.x   | 6.x   | 配合升级   |
| Spring Boot                 | 2.7.x | 3.5.9 | 框架升级   |

#### 2.2 API 变更适配

**旧版本配置方式 (已废弃)**:

```java
// SAS 0.4.x - 使用 http.apply()
OAuth2AuthorizationServerConfigurer authServerConfig = new OAuth2AuthorizationServerConfigurer();
http.apply(authServerConfig);
```

**新版本配置方式**:

```java
// SAS 1.x - 使用 http.with() 和 applyDefaultSecurity
OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
    .oidc(Customizer.withDefaults());
```

### 3. 新增文件

#### 3.1 配置类

| 文件                                     | 路径        | 说明                   |
|----------------------------------------|-----------|----------------------|
| `OAuth2Properties.java`                | `config/` | 统一的配置属性类（600+ 行详细注释） |
| `OAuth2AuthorizationServerConfig.java` | `config/` | 授权服务器自动配置            |
| `OAuth2ResourceServerConfig.java`      | `config/` | 资源服务器自动配置            |
| `OAuth2ServiceConfig.java`             | `config/` | 服务层自动配置              |

#### 3.2 服务接口与实现

| 文件                                     | 路径         | 说明            |
|----------------------------------------|------------|---------------|
| `ExtendUserDetailsService.java`        | `service/` | 扩展的用户服务接口     |
| `DefaultExtendUserDetailsService.java` | `service/` | 默认实现（仅开发测试）   |
| `CarlosJwtTokenCustomizer.java`        | `token/`   | JWT Token 增强器 |

#### 3.3 自动配置注册

| 文件                                                                 | 路径                 | 说明                   |
|--------------------------------------------------------------------|--------------------|----------------------|
| `org.springframework.boot.autoconfigure.AutoConfiguration.imports` | `META-INF/spring/` | Spring Boot 3.x 自动配置 |
| `additional-spring-configuration-metadata.json`                    | `META-INF/`        | IDE 配置提示             |

#### 3.4 文档

| 文件           | 路径    | 说明     |
|--------------|-------|--------|
| `README.md`  | 模块根目录 | 详细使用文档 |
| `REACTOR.md` | 模块根目录 | 本重构记录  |

### 4. 配置属性变更

#### 4.1 配置前缀变更

```yaml
# 旧配置前缀（已废弃）
yj:
  oauth:
    # ...

# 新配置前缀
carlos:
  oauth2:
    # ...
```

#### 4.2 新增配置项

```yaml
carlos:
  oauth2:
    enabled: true  # 总开关
    
    authorization-server:
      enabled: false
      oidc-enabled: true  # OIDC 支持
      device-code-time-to-live: 5m  # 设备码有效期
      
    resource-server:
      enabled: false
      permit-all-paths: []  # 白名单路径
      
    jwt:
      custom-claims: {}  # 自定义声明
      
    security:
      password-encoder: bcrypt  # 密码编码器类型
      token-storage: memory  # Token 存储方式
      login-limit:  # 登录限制
        enabled: true
        max-attempts: 5
        lock-duration: 30m
```

### 5. 扩展点设计

#### 5.1 用户服务扩展

```java
@Service
@Primary
public class MyUserDetailsService implements ExtendUserDetailsService {
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        // 自定义用户加载逻辑
    }
    
    @Override
    public LoginUserInfo loadLoginUserInfo(String username) {
        // 自定义用户信息加载
    }
}
```

#### 5.2 Token 增强扩展

```java
@Bean
public OAuth2TokenCustomizer<JwtEncodingContext> customTokenCustomizer() {
    return context -> {
        context.getClaims().claim("custom_key", "custom_value");
    };
}
```

#### 5.3 客户端存储扩展

```java
@Service
@Primary
public class JdbcRegisteredClientRepository implements RegisteredClientRepository {
    // 数据库存储实现
}
```

### 6. 依赖变更

#### 6.1 carlos-auth-service/pom.xml 更新

**新增依赖**:

```xml
<!-- Spring Authorization Server 1.x -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-authorization-server</artifactId>
</dependency>

<!-- Spring Resource Server -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>

<!-- 自动配置处理器 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
</dependency>
```

**移除依赖**:

```xml
<!-- 旧版本依赖（硬编码版本）-->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-authorization-server</artifactId>
    <version>0.4.5</version>  <!-- 移除硬编码版本 -->
</dependency>
```

### 7. 删除的文件

```
carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/oauth/oauth2/config/OAuth2Properties.java
carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/oauth/oauth2/config/Oauth2ServerConfig.java
```

## 迁移指南

### 从旧版本迁移步骤

#### 步骤 1: 更新依赖

```xml
<!-- 移除旧依赖 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-oauth2</artifactId>
</dependency>

<!-- 添加新依赖 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-auth-service</artifactId>
</dependency>
```

#### 步骤 2: 更新配置

```yaml
# 旧配置
yj:
  oauth:
    enabled: true

# 新配置
carlos:
  oauth2:
    enabled: true
    authorization-server:
      enabled: true
    resource-server:
      enabled: false
```

#### 步骤 3: 实现用户服务（生产环境必需）

```java
@Service
@Primary
public class MyUserDetailsService implements ExtendUserDetailsService {
    // 实现接口方法
}
```

#### 步骤 4: 检查安全配置

Spring Boot 3.x 中 `WebSecurityConfigurerAdapter` 已移除，确保使用新的 `SecurityFilterChain` 配置方式。

## 注意事项

### 生产环境检查清单

- [ ] 实现自定义 `ExtendUserDetailsService`，替换默认实现
- [ ] 配置 `security.token-storage=redis` 或 `jdbc`
- [ ] 配置固定的 JWT 密钥对（`jwt.private-key-path`）
- [ ] 使用 HTTPS 协议
- [ ] 配置强密码策略
- [ ] 配置登录限制参数

### 开发环境警告

默认的 `DefaultExtendUserDetailsService` 仅用于开发测试，包含硬编码的测试用户：

- 用户名: `admin` / 密码: `admin123`
- 用户名: `user` / 密码: `user123`

## 兼容性

### 支持的版本

| 组件                          | 版本     |
|-----------------------------|--------|
| JDK                         | 17+    |
| Spring Boot                 | 3.5.9+ |
| Spring Security             | 6.x    |
| Spring Authorization Server | 1.x    |

### 向后兼容性

本次重构**不保证**向后兼容，主要因为：

1. Spring Authorization Server 0.4.x 到 1.x 存在重大 API 变更
2. 配置属性前缀从 `yj.oauth` 变更为 `carlos.oauth2`

建议按迁移指南进行升级。

## 重构参与人员

- **重构执行**: Kimi Code CLI
- **架构设计**: Carlos Framework Team
- **代码审查**: AI Assistant

## 附录

### 参考文档

- [Spring Authorization Server 文档](https://docs.spring.io/spring-authorization-server/docs/current/reference/html/)
- [Spring Security 6.x 迁移指南](https://docs.spring.io/spring-security/reference/5.8/migration/index.html)
- [OAuth 2.1 规范](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-09)

### 相关文件

- `README.md` - 模块使用文档
- `AGENTS.md` - 项目整体架构说明
- `CLAUDE.md` - AI 编程助手指南

---

**最后更新**: 2026-02-22  
**文档版本**: 1.0.0
