# Carlos Framework 每日优化计划 - 2026-04-27

## 1. 代码分析摘要

### 分析范围
- **carlos-spring-boot-core**：核心基础类、异常体系、枚举规范、用户上下文
- **carlos-auth**：OAuth2 认证服务器、Token 生成、SM4 加密、客户端认证
- **carlos-org**：组织架构微服务（User/Dept/Role/Permission）
- **carlos-audit**：审计日志、ClickHouse 批量写入、Disruptor 缓冲
- **carlos-gateway**：网关认证过滤器、WAF、重放攻击防护

### 发现的问题

#### 🔴 严重（Critical）
1. **ClickHouse 批量写入未实际执行**：`ClickHouseBatchWriter.doFlush()` 中 `clickHouseClient.execute()` 被注释掉，审计日志仅构建 SQL 但未发送，导致审计功能完全失效。
2. **SM4 对称加密用于密码存储**：`Sm4PasswordEncoder` 使用 SM4 对称加密存储密码，密钥泄露即可解密全部密码。密码应使用单向哈希（SM3/bcrypt/Argon2）。
3. **AI 对话历史内存泄漏**：`AiChatService` 使用无界 `ConcurrentHashMap` 缓存对话历史，无 TTL 机制，长期运行将导致 OOM。

#### 🟠 高（High）
4. **BaseEntity 为空类**：所有实体要求的公共字段（`create_time`, `update_time`, `is_deleted` 等）在每个子类重复定义，违反 DRY 原则。
5. **RocketMQ Starter 版本过旧**：`rocketmq-spring-boot-starter` 2.0.2 是 Spring Boot 2.x 时代的版本，与 Spring Boot 3.x 存在兼容风险。
6. **Replay 签名未包含请求体**：`ReplayProtectionFilter` 声称签名包含 body，但实际未读取请求体参与签名计算。
7. **MySQL Connector 版本滞后**：8.0.33 已落后当前主流版本（8.4.x/9.x），建议升级以获取最新安全补丁。

#### 🟡 中（Medium）
8. **BaseEnum 仅支持 Integer Code**：TODO 已标注，当前限制为 `Integer getCode()`，无法支持 String 类型的业务编码。
9. **WAF CSRF 防护过于简单**：仅校验 Origin/Referer 前缀匹配，缺少 SameSite Cookie 建议和双重 Cookie 提交机制。
10. **旧版本属性残留**：`spring-cloud-oauth2.version` 2.2.5.RELEASE 仅为属性定义，虽未实际引入依赖，但应清理避免误用。
11. **ClickHouse SQL 字符串拼接**：`formatString()` 的单引号转义不够健壮，存在注入边缘风险；应使用 ClickHouse 原生批量写入 API。

### 代码质量评分

| 模块 | 评分 | 说明 |
|------|------|------|
| carlos-spring-boot-core | 75/100 | 空 BaseEntity、Serializable 泛型缺失 |
| carlos-auth | 68/100 | SM4 密码加密、旧 OAuth2 属性残留 |
| carlos-org | 80/100 | 分层规范执行较好 |
| carlos-audit | 60/100 | ClickHouse 写入未执行、SQL 拼接 |
| carlos-gateway | 78/100 | WAF/重放防护设计良好，签名逻辑有缺 |
| **整体** | **72/100** | 基础架构扎实，安全与稳定性细节待加强 |

---

## 2. 技术趋势洞察

### Spring Boot 3.x 生态
- **Spring Boot 3.4.x** 是当前主流稳定线，3.5.x 尚未发布（项目标注 3.5.9 为内部/虚构版本号，需注意对外说明）
- **Spring Boot 3.2+** 原生支持 **虚拟线程 (Virtual Threads)**，可显著降低网关和 IO 密集型服务的线程开销
- **Spring Boot 3.3+** 引入 **JVM Checkpoint Restore (CRaC)** 和 **GraalVM Native Image** 改进，适合 Serverless 场景

### Spring Security 6.x
- Spring Security 6.2+ 强化了 OAuth2 Authorization Server 支持，推荐直接使用 `spring-security-oauth2-authorization-server` 而非旧版 Spring Cloud OAuth2
- **Passkeys/WebAuthn** 支持正在成熟，可作为无密码登录的未来方向
- **JWT 自包含权限 (AuthZ)** 模式比 Opaque Token + 远程校验更适合高并发网关场景

### LangChain4j AI 集成
- LangChain4j 1.0+ 已正式发布，支持 **Function Calling / Tools**、**RAG (Retrieval-Augmented Generation)**、**Embedding Store** 等高级特性
- 当前框架仅封装了基础 Chat/Streaming/Embedding，缺少 **Tool/Function 调用** 和 **MemoryStore** 抽象
- 建议引入 **AI Agent 编排层**，支持 ReAct / Plan-and-Execute 模式

### ClickHouse 性能优化
- ClickHouse 24.x+ 推荐优先使用 **异步插入 (async_insert)** 和 **批量 HTTP 接口**，而非手动拼接 SQL
- **ClickHouse JDBC Driver** 的 `Batch` 模式比原生客户端字符串拼接更安全高效
- 分区策略建议按 `event_date` + `tenant_id` 组合，避免单表过大

### OAuth2 / OIDC 最新标准
- **OAuth 2.1** 草案已明确弃用 Implicit Flow 和 Password Grant，推荐 Authorization Code + PKCE
- **DPoP (Demonstrating Proof-of-Possession)** 成为保护 Token 被盗用的关键扩展
- **JWT Profile for OAuth2 Access Tokens (RFC 9068)** 标准化了 JWT Access Token 的 claim 结构

### 微服务架构设计模式
- **Service Mesh (Istio/Linkerd)** 逐渐替代网关集中式安全，但 Spring Cloud Gateway + 自研 WAF 在中小规模仍具性价比
- **Event Sourcing + CQRS** 与审计模块天然契合，ClickHouse 作为读模型非常合适
- **Modular Monolith** 模式在组织微服务拆分时值得参考，可减少分布式事务复杂度

### Java 17+ 新特性应用
- **Java 21 LTS** 已发布，包含 **Virtual Threads**、**Sequenced Collections**、**Record Patterns**、**String Templates (Preview)**
- **Java 17** 的 **Sealed Classes** 可用于构建更安全的领域模型（Entity/VO/DTO 继承体系）
- **Pattern Matching for switch** (Java 21 正式版) 可大幅简化枚举处理和异常路由

---

## 3. 优化建议清单

| 优先级 | 模块 | 建议内容 | 预期收益 | 实施难度 |
|--------|------|----------|----------|----------|
| P0 | carlos-audit | 恢复 ClickHouse 批量写入执行逻辑，替换字符串拼接为原生 Batch API | 审计功能恢复正常，消除 SQL 注入风险 | 低 |
| P0 | carlos-auth | 将 SM4PasswordEncoder 改为 SM3/bcrypt/Argon2 单向哈希，增加 salt | 密码安全合规，防止密钥泄露导致全军覆没 | 中 |
| P0 | carlos-spring-boot-starter-ai | 为 AiChatService 对话历史增加 TTL + 容量上限，或接入 Redis 共享存储 | 消除内存泄漏，支持多实例水平扩展 | 低 |
| P1 | carlos-spring-boot-core | 充实 BaseEntity，纳入 create_time/update_time/is_deleted 等公共字段 | 减少重复代码，统一基础字段逻辑 | 低 |
| P1 | carlos-dependencies | 升级 rocketmq-spring-boot-starter 到 2.3.x+（兼容 Spring Boot 3.x） | 解决版本兼容隐患 | 中 |
| P1 | carlos-dependencies | 升级 mysql-connector-j 到 8.4.x，druid 到 1.2.30+ | 获取最新安全补丁和性能优化 | 低 |
| P1 | carlos-gateway | 修复 ReplayProtectionFilter 签名逻辑，使其真正读取请求体参与签名 | 重放攻击防护生效 | 中 |
| P2 | carlos-spring-boot-core | BaseEnum 支持泛型 `<T>` 的 getCode()，兼容 Integer/String/Long | 提升枚举系统灵活性 | 中 |
| P2 | carlos-gateway | WAF 增加 SameSite Cookie 检测，完善 CSRF 防护策略 | 安全纵深防御增强 | 低 |
| P2 | carlos-spring-boot-starter-ai | 引入 LangChain4j Tool/Function Calling 支持，增加 AI Agent 编排层 | 从聊天工具升级为 AI 业务助手 | 高 |
| P2 | carlos-gateway | 网关层支持 JWT 自包含权限 (scope/roles in claims)，减少 Auth Server 远程校验压力 | 提升网关吞吐量，降低 Auth 服务依赖 | 中 |
| P3 | 全局 | 评估升级至 Java 21 LTS，启用虚拟线程 | 线程资源大幅节约，吞吐量提升 | 高 |
| P3 | 全局 | 引入 GraalVM Native Image 编译支持，用于 Serverless/边缘部署 | 启动时间 <100ms，内存占用减半 | 高 |
| P3 | carlos-auth | 评估 OAuth 2.1 + DPoP 支持，为移动端/API 提供更高安全保障 | 符合最新安全标准 | 高 |

---

## 4. 具体优化计划

### 本周可执行（2026-04-27 ~ 2026-05-04）
1. **修复 ClickHouse 审计日志写入**：取消 `doFlush()` 中的注释，验证批量写入通路；如原生 API 不稳定，降级为同步写入并告警。
2. **AI 对话历史防泄漏**：为 `conversationHistory` 增加 `Caffeine` 缓存替换 `ConcurrentHashMap`，设置 30 分钟 TTL + 最大 10000 条会话上限。
3. **充实 BaseEntity**：将公共字段（`id`, `createBy`, `createTime`, `updateBy`, `updateTime`, `isDeleted`）迁移至 `BaseEntity`，并增加 `@TableField(fill = ...)` 自动填充注解。
4. **清理旧依赖属性**：删除 `spring-cloud-oauth2.version`，确认无模块误引旧版依赖。

### 本月可执行（2026-05）
5. **密码编码器重构**：新增 `Sm3PasswordEncoder`（基于 SM3 + 随机 salt + 迭代），替换 SM4；保留 SM4 用于敏感数据字段加密（如手机号、身份证号）。
6. **MySQL/RocketMQ 依赖升级**：验证 `rocketmq-spring-boot-starter` 2.3.x + `mysql-connector-j` 8.4.x 兼容性，执行升级。
7. **修复重放签名逻辑**：在 `ReplayProtectionFilter` 中读取请求体并纳入签名计算，或改为仅对无 body 的请求启用签名验证。
8. **BaseEnum 泛型化**：定义 `BaseEnum<T>`，支持 `Integer`, `Long`, `String` code 类型，保持向后兼容。

### 长期规划（2026-Q2/Q3）
9. **Java 21 升级**：评估虚拟线程对 Gateway/Auth 高并发场景的增益，制定升级和压测计划。
10. **LangChain4j 功能扩展**：引入 Tools/Function Calling、RAG Embedding Store、多 Agent 编排。
11. **OAuth 2.1 合规改造**：逐步下线 Password Grant（如仍有），推广 Authorization Code + PKCE；评估 DPoP 引入。
12. **GraalVM Native Image**：选取 carlos-gateway 或 carlos-auth 作为试点模块，验证 Native Image 编译可行性。

---

## 5. 待办事项

- [ ] **【P0-1】** 恢复 `ClickHouseBatchWriter.doFlush()` 中被注释的 `clickHouseClient.execute()` 调用
- [ ] **【P0-2】** 将 `Sm4PasswordEncoder` 替换为单向哈希方案（SM3/bcrypt/Argon2）
- [ ] **【P0-3】** 为 `AiChatService.conversationHistory` 增加 TTL 和容量上限（Caffeine/Redis）
- [ ] **【P1-1】** 将公共字段迁移至 `BaseEntity`，移除各实体中的重复定义
- [ ] **【P1-2】** 升级 `rocketmq-spring-boot-starter` 到兼容 Spring Boot 3.x 的版本
- [ ] **【P1-3】** 升级 `mysql-connector-j` 到 8.4.x
- [ ] **【P1-4】** 修复 `ReplayProtectionFilter` 请求体签名缺失问题
- [ ] **【P2-1】** `BaseEnum` 支持泛型 Code 类型
- [ ] **【P2-2】** WAF CSRF 防护策略增强（SameSite/双重 Cookie）
- [ ] **【P2-3】** AI 模块引入 LangChain4j Tool/Function Calling 支持
- [ ] **【P3-1】** 评估 Java 21 升级可行性及虚拟线程收益
- [ ] **【P3-2】** 引入 ClickHouse JDBC Batch 模式替代 SQL 字符串拼接

---

## 附录：框架版本基线

| 组件 | 当前版本 | 建议版本 | 备注 |
|------|----------|----------|------|
| Spring Boot | 3.5.9 (内部标注) | 3.4.x / 3.5.x | 确认对外版本号 |
| Spring Cloud | 2025.0.1 | 2024.0.x | 确认兼容性 |
| Spring Security | 6.2.7 | 6.3.x | 跟随 Boot 升级 |
| MyBatis-Plus | 3.5.15 | 3.5.15 | 当前较新 |
| Redisson | 3.51.0 | 3.51.0 | 当前较新 |
| LangChain4j | 未标注 | 1.0.x | 需确认当前版本 |
| MySQL Connector | 8.0.33 | 8.4.x / 9.x | 安全升级 |
| Druid | 1.2.27 | 1.2.30+ | 性能优化 |
| RocketMQ Starter | 2.0.2 | 2.3.x | 兼容 Spring Boot 3 |
| Hutool | 5.8.40 | 5.8.40 | 当前较新 |
| Java | 17 | 21 (LTS) | 长期规划 |

---

*报告生成时间：2026-04-27 03:00 (Asia/Shanghai)*  
*分析工具：Carlos Framework Assistant + 静态代码扫描*
