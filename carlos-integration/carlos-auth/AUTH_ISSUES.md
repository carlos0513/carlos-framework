# carlos-auth 模块问题分析报告

> 生成时间：2026-04-17
> 基于 questions.txt 中的问题，结合模块代码分析

---

## 一、架构设计问题

### 问题1：授权码模式流程缺失

**关联问题**：questions.txt #1、#4

**现状**：

- `ExtendAuthenticationConverter` 只处理自定义 grant type（password/sms_code 等）
- 缺少 `/oauth2/authorize` 端点对应的前端登录页和授权确认页
- 缺少授权码换 token 的完整链路文档

**授权码模式标准流程**：

```
用户浏览器
  → GET /oauth2/authorize?response_type=code&client_id=xxx&redirect_uri=xxx&state=xxx
  → 重定向到登录页 /login（当前放行但未实现）
  → 用户登录成功后重定向到授权确认页 /oauth2/consent
  → 用户确认后，授权服务器回调 redirect_uri?code=xxx&state=xxx
  → 前端/后端用 code 换 token：POST /oauth2/token (grant_type=authorization_code)
```

**需要补充**：

- 登录页 `/login`（HTML 或前端路由）
- 授权确认页 `/oauth2/consent`
- 完整链路时序图文档

---

### 问题2：Token 类型识别机制不完整

**关联问题**：questions.txt #2

**现状**：

- `CustomizeUserOAuth2TokenCustomizer` 添加了 `token_type=user_token`
- `CustomizeClientOAuth2TokenCustomizer` 添加了 `token_type=client_token`
- 客户端模式 Token value 使用 `IdUtil.randomUUID()`（Opaque Token）
- 网关需调用 `/auth/token/checkToken` 才能获取 claims

**问题**：

- `TokenController.checkToken()` 响应结构不明确，网关无法标准化解析
- 缺少网关集成文档说明如何根据 `token_type` 做路由决策
- 客户端 token 无用户上下文，网关需要区分处理逻辑

**建议**：

- 明确 `checkToken` 响应 VO，包含 `token_type`、`user_id`、`client_id` 等字段
- 网关统一通过 `token_type` 字段判断：`user_token` 注入用户上下文，`client_token` 仅做权限校验

---

### 问题3：遗留 SMS 类与新架构并存（架构混乱）

**现状**：`carlos-auth-service/src/main/java/com/carlos/auth/oauth2/grant/sms/` 下存在：

- `SmsAuthenticationProvider`
- `SmsAuthenticationConverter`
- `SmsAuthenticationToken`

这三个类基于旧 `BaseAuthenticationProvider` 架构，与新的 `ExtendAuthenticationProvider` 架构**重复冲突**。新架构已通过
`IdentityProvider` 接口统一处理 `sms_code` grant type。

**建议**：删除 `sms/` 目录下的三个遗留类。

---

## 二、安全问题

### 问题4：SM4 对称加密存储密码（高危）

**文件**：`Sm4PasswordEncoder.java`

```java
public boolean matches(CharSequence rawPassword, String encodedPassword) {
    String decrypted = EncryptUtil.decrypt(encodedPassword);  // 可解密！
    return rawPassword.toString().equals(decrypted);
}
```

**风险**：SM4 是对称加密，不是哈希。数据库泄露后所有密码明文可还原。

**建议**：

- 密码存储必须使用不可逆哈希（bcrypt/argon2）
- SM4 仅用于传输加密（前端传输密码时加密），不用于存储

---

### 问题5：DefaultUserProvider 硬编码测试用户存在生产泄露风险

**文件**：`DefaultUserProvider.java`

内存中硬编码 `admin/123456`、`user/123456`、`test/123456`，若业务方未实现 `UserProvider`，生产环境会使用默认实现。

**建议**：

- 添加 `@Profile("dev")` 限制仅开发环境生效
- 或在 Bean 初始化时打印明显的警告日志

---

### 问题6：DefaultExtendUserDetailsService 与 DefaultUserProvider 逻辑重复

**文件**：`DefaultExtendUserDetailsService.java`

硬编码测试账号 `admin/admin123`、`user/user123`、`test/test123`，与 `DefaultUserProvider` 的测试用户**不一致**
（密码不同），容易造成混淆。

**建议**：`DefaultExtendUserDetailsService` 应完全委托给 `UserProvider`，不应有独立的测试用户。

---

## 三、功能缺失问题

### 问题7：第三方登录 IdentityProvider 未实现

**关联问题**：questions.txt #3

**现状**：

- `IdentityProvider` 接口定义完整
- `IdentityProviderRegistry` 注册中心存在
- 仅有 `LocalIdentityProvider` 实现

**缺失**：

- `DingTalkIdentityProvider`（钉钉扫码/免密登录）
- `WeChatIdentityProvider`（微信登录）
- 第三方登录回调处理 Controller（`/auth/callback/{providerId}`）

---

### 问题8：单点登录（SSO）机制未实现

**关联问题**：questions.txt #3

**缺失**：

- 免密跳转三方系统的 Token 交换接口（应用管理模块跳转场景）
- SSO 登出（Single Logout）联动机制
- 多系统 Session 失效同步策略

---

### 问题9：checkToken 响应结构未标准化

**文件**：`TokenController.java`

网关调用 `GET /auth/token/checkToken` 需要明确的响应 VO，当前返回结构不明确。

**建议响应结构**：

```json
{
  "active": true,
  "token_type": "user_token",
  "user_id": 1001,
  "username": "admin",
  "tenant_id": 1,
  "dept_id": 10,
  "role_ids": [1, 2],
  "authorities": ["sys:user:list"],
  "client_id": "web-client",
  "scopes": ["read", "write"],
  "expires_in": 7200
}
```

---

## 四、代码规范问题

### 问题10：UserLoginService 绕过 OAuth2 标准流程

**文件**：`UserLoginService.java`

`/auth/login` 接口直接调用 `OAuth2TokenGenerator` 生成 token，绕过了 `/oauth2/token` 标准端点，导致：

- 两套登录入口并存，逻辑分散
- Token 审计和撤销可能不一致
- 不符合 OAuth2 规范

**建议**：统一走 `/oauth2/token` 端点，`/auth/login` 可作为前端友好的代理接口，内部转发到标准端点。

---

### 问题11：RedisOAuth2AuthorizationService 序列化配置风险

**文件**：`RedisOAuth2AuthorizationService.java`

使用 Jackson 序列化 `OAuth2Authorization`，Spring Authorization Server 内部类需要注册 `SecurityJackson2Modules`
，否则版本升级后可能出现反序列化失败。

**建议**：确认 `ObjectMapper` 已注册以下 Module：

```java
objectMapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
```

---

## 优先级汇总

| 优先级 | 问题编号  | 问题描述                       | 类型    |
|-----|-------|----------------------------|-------|
| P0  | #4    | SM4 对称加密存储密码               | 安全漏洞  |
| P0  | #3    | 遗留 SMS 类与新架构冲突             | 架构混乱  |
| P1  | #9    | checkToken 响应结构未标准化        | 功能缺失  |
| P1  | #5、#6 | DefaultUserProvider 生产泄露风险 | 安全风险  |
| P1  | #2    | 网关 token 类型识别机制不完整         | 功能缺失  |
| P2  | #7    | 第三方登录 IdentityProvider 未实现 | 功能缺失  |
| P2  | #8    | SSO 单点登录机制未实现              | 功能缺失  |
| P2  | #1    | 授权码模式登录页/确认页缺失             | 功能缺失  |
| P3  | #10   | UserLoginService 双入口问题     | 代码规范  |
| P3  | #11   | Redis 序列化 Module 配置风险      | 稳定性风险 |
