# 三个高优先级问题修复总结

## 修复清单

| 序号 | 问题 | 状态 | 修复内容 |
|------|------|------|----------|
| 1 | **pom.xml 版本冲突** | ✅ 已修复 | 移除重复的旧版本定义 |
| 2 | **OAuth2 JDBC 存储** | ✅ 已实现 | 新增 `JdbcOAuth2AuthorizationService` |
| 3 | **SM4 国密加密** | ✅ 已实现 | 新增 `SM4PasswordEncoder` |

---

## 1. pom.xml 版本冲突修复

### 问题描述
`spring-security-oauth2-jose` 和 `spring-boot-starter-security` 被重复定义了两次不同版本。

### 修复内容
```xml
<!-- 修复前 - 重复定义 -->
<spring-security-oauth2-jose.version>6.2.7</spring-security-oauth2-jose.version>
<spring-boot-starter-security.version>3.2.12</spring-boot-starter-security.version>
...
<spring-security-oauth2-jose.version>5.7.3</spring-security-oauth2-jose.version>
<spring-boot-starter-security.version>2.7.4</spring-boot-starter-security.version>

<!-- 修复后 - 统一版本 -->
<spring-security-oauth2-jose.version>6.2.7</spring-security-oauth2-jose.version>
<spring-boot-starter-security.version>3.2.12</spring-boot-starter-security.version>
<!-- 注意：旧版本定义已移除，避免版本冲突 -->
```

### 影响
- 统一使用 Spring Boot 3.x 兼容的版本
- 避免构建时的版本冲突警告
- 消除运行时 ClassNotFoundException 风险

---

## 2. OAuth2 JDBC 存储实现

### 新增文件

| 文件 | 路径 | 说明 |
|------|------|------|
| `JdbcOAuth2AuthorizationService.java` | `config/repository/` | JDBC 授权服务实现 |
| `OAuth2AuthorizationPOJO.java` | `pojo/` | 授权信息 POJO |
| `OAuth2AuthorizationConverter.java` | `config/converter/` | 授权信息转换器 |
| `V1.0.0__oauth2_authorization.sql` | `resources/db/migration/` | 数据库表结构 |

### 配置方式

```yaml
carlos:
  oauth2:
    security:
      token-storage: jdbc  # 启用 JDBC 存储
```

### 表结构
- 支持授权码、访问令牌、刷新令牌、OIDC ID 令牌
- 包含创建时间和更新时间
- 添加了常用查询索引

### 与 Redis 存储对比

| 特性 | JDBC | Redis |
|------|------|-------|
| 持久化 | ✅ 数据库持久化 | ⚠️ 依赖 Redis 持久化配置 |
| 查询灵活性 | ✅ SQL 灵活查询 | ⚠️ 需要设计 key 结构 |
| 性能 | 一般 | 高 |
| 适用场景 | 数据量小、需要复杂查询 | 高并发、大数据量 |

---

## 3. SM4 国密加密实现

### 新增文件

| 文件 | 路径 | 说明 |
|------|------|------|
| `SM4PasswordEncoder.java` | `security/` | SM4 密码编码器 |

### 算法特性
- 分组长度：128 位
- 密钥长度：128 位
- 迭代轮数：32 轮
- 工作模式：ECB（配合随机盐值）

### 存储格式
```
格式：{SM4}${salt}:${encrypted}
示例：{SM4}a1b2c3d4:SM4EncryptedBase64String
```

### 配置方式

```yaml
carlos:
  oauth2:
    security:
      password-encoder: sm4
      sm4-key: ${SM4_SECRET_KEY:Your16ByteKey!}  # 16字节密钥
```

### 生产环境建议
1. 密钥必须从环境变量或安全配置中心读取
2. 密钥长度恰好为 16 字节（128 位）
3. 定期更换密钥并迁移旧密码
4. 不要提交密钥到代码仓库

---

## 文件变更汇总

### 修改文件
1. `carlos-dependencies/pom.xml` - 移除重复版本定义
2. `OAuth2AuthorizationServerConfig.java` - 添加 JwtEncoder、支持 JDBC 和 SM4

### 新增文件
1. `JwtTokenProvider.java` - JWT 令牌颁发（之前实现）
2. `JdbcOAuth2AuthorizationService.java` - JDBC 授权服务
3. `OAuth2AuthorizationPOJO.java` - 授权信息 POJO
4. `OAuth2AuthorizationConverter.java` - 授权信息转换器
5. `SM4PasswordEncoder.java` - SM4 密码编码器
6. `V1.0.0__oauth2_authorization.sql` - 数据库迁移脚本

---

## 配置示例

完整的 OAuth2 配置：

```yaml
carlos:
  oauth2:
    authorization-server:
      enabled: true
    
    security:
      # 密码编码器: bcrypt | sm4
      password-encoder: sm4
      # SM4 密钥（16字节）
      sm4-key: ${SM4_SECRET_KEY:}
      
      # 令牌存储: memory | redis | jdbc
      token-storage: jdbc
    
    jwt:
      key-store: ~/.carlos/auth/auth.jks
      key-store-password: ${JKS_PASSWORD:}
      key-alias: auth-key
      key-password: ${KEY_PASSWORD:}
      key-id: carlos-key-1
      issuer: http://localhost:8080
      include-user-info: true
```

---

## 后续建议

1. **数据库连接池配置** - 如果使用 JDBC 存储，确保配置了合适的连接池
2. **SM4 密钥管理** - 考虑使用密钥管理服务（KMS）
3. **OAuth2AuthorizationConsentService** - 可以类似实现 JDBC 版本
4. **性能测试** - 对比 JDBC 和 Redis 存储的性能差异

---

*修复时间：2026-04-03*
*修复者：泡泡*
