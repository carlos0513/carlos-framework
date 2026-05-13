# Carlos Framework 每日优化计划 - 2026-05-13 09:00

> 扫描范围：carlos-dependencies / carlos-spring-boot / carlos-auth / carlos-gateway / carlos-audit / carlos-org / carlos-spring-boot-starter-ai
> 扫描策略：安全红线优先 + 依赖版本跟踪 + 代码质量审计

---

## 1. 代码分析摘要

### 1.1 代码同步
- **状态**: 成功（本地已是最新，无需更新）
- **当前 Commit**: `fd32e218cd1e54e9de0623b7cebce515545cd993`
- **变更统计**: 0 files changed（与前次扫描 commit `ca253d3a` 无新增 commit）
- **说明**: 上次代码提交为 `chore: daily optimization report 2026-04-30-0918`，本次扫描基于当前 HEAD 进行。

### 1.2 分析范围

| 优先级 | 模块 | 分析重点 | 状态 |
|--------|------|---------|------|
| P0 | carlos-dependencies | 依赖版本矩阵、CVE 跟踪 | ✅ 已扫描 |
| P0 | starter-security / auth | 资源服务器、权限评估、Token 管理 | ✅ 已扫描 |
| P0 | starter-web | XSS 过滤器、请求包装、CORS | ✅ 已扫描 |
| P0 | carlos-gateway | OAuth2 过滤器、WAF、防重放、限流 | ✅ 已扫描 |
| P1 | starter-mybatis | MyBatisPlus 配置、分页、ID 生成器 | ✅ 已扫描 |
| P1 | carlos-audit | ClickHouse 批量写入、Disruptor | ✅ 已扫描 |
| P1 | carlos-org | 部门缓存、Redis 策略 | ✅ 已扫描 |
| P1 | starter-ai | ChatMemory 内存管理 | ✅ 已扫描 |
| P2 | starter-json | ObjectMapper 安全配置 | ✅ 已扫描 |
| P2 | starter-redis | RedisUtil 静态工具类设计 | ✅ 已扫描 |

### 1.3 代码质量评分

| 维度 | 评分 | 说明 |
|------|------|------|
| 架构设计 | ★★★★☆ | 分层清晰（API→apiimpl→Controller→Service→Manager→Mapper），Starter 拆分合理 |
| 代码规范 | ★★★★☆ | Lombok 使用规范，强制 `@ConfigurationProperties`，禁止 `@Value` |
| 安全性 | ★★★☆☆ | **存在 3 处 ObjectMapper Visibility 过宽（P0）**；WAF/防重放/限流完善 |
| 性能优化 | ★★★★☆ | 双缓冲 ClickHouse 写入、Redis 多层缓存、Disruptor 事件处理 |
| 可维护性 | ★★★★☆ | BaseService 体系完善，但 `RedisUtil` 静态耦合影响测试 |

### 1.4 发现的问题及代码片段

#### 🔴 P0 — 安全问题（必须立即修复）

**P0-1: JacksonSerializer 设置 Visibility.ANY，扩大序列化攻击面**
- **文件**: `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis-core/src/main/java/com/carlos/redis/serialize/JacksonSerializer.java`
- **代码片段**:
  ```java
  mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
  ```
- **风险**: `Visibility.ANY` 允许 Jackson 序列化/反序列化所有字段（含 `private`），可能导致敏感字段泄露或被恶意 JSON 利用进行攻击（如利用非预期字段进行反序列化漏洞利用）。
- **建议**: 改为 `Visibility.PUBLIC_ONLY` 或显式使用 `@JsonProperty` 控制字段暴露。

**P0-2: RedisOAuth2AuthorizationService 同样设置 Visibility.ANY**
- **文件**: `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/repository/RedisOAuth2AuthorizationService.java`
- **代码片段**:
  ```java
  objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
  ```
- **风险**: OAuth2 授权信息（含 Token、用户认证对象）存入 Redis 时，若实体含敏感内部字段，可能被序列化到 Redis 中；反序列化时同样面宽大，增加被构造恶意 Token 的风险。

**P0-3: AuthorizationGrantTypeMixin 使用 `@JsonAutoDetect(fieldVisibility = ANY)`**
- **文件**: `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/repository/AuthorizationGrantTypeMixin.java`
- **代码片段**:
  ```java
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public abstract class AuthorizationGrantTypeMixin {
  }
  ```
- **风险**: Mixin 类显式放宽字段可见性，配合前两个配置形成**全链路可见性放大**。

**P0-4: AiChatServiceImpl ChatMemory 内存泄漏风险**
- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-ai/src/main/java/com/carlos/ai/service/impl/AiChatServiceImpl.java`
- **代码片段**:
  ```java
  private final Map<String, ChatMemory> memoryStore = new ConcurrentHashMap<>();
  // getOrCreateMemory:
  return memoryStore.computeIfAbsent(sessionId, k ->
      MessageWindowChatMemory.builder()
          .maxMessages(properties.getMemoryMaxMessages())
          .build());
  ```
- **风险**: `memoryStore` 只有 `getOrCreate`，**没有任何过期清理或容量上限**。在高并发长会话场景下（如客服机器人），无限制增长会导致 OOM。即使 `MessageWindowChatMemory` 内部限制了消息条数，每个会话的 `ChatMemory` 对象本身及其 metadata 仍会常驻内存。
- **建议**: 增加 `expireAfterAccess` 或 `expireAfterWrite`（Guava/Caffeine 替代），或定期清理不活跃会话。

#### 🟡 P1 — 重要改进

**P1-1: System.out.println 存在于生产代码中**
- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-ai/src/main/java/com/carlos/ai/config/CarlosAiAutoConfiguration.java`
- **代码片段**:
  ```java
  System.out.println("=== Carlos AI AutoConfiguration Debug ===");
  ```
- **建议**: 替换为 `log.debug()`，或在生产构建中自动阻断 `System.out.println`。

**P1-2: RedisUtil 静态工具类导致测试困难**
- **文件**: `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis/src/main/java/com/carlos/redis/util/RedisUtil.java`
- **说明**: `RedisUtil` 是静态工具类，内部直接持有 `static RedisTemplate`。单元测试时难以 Mock，导致 Service 层测试必须依赖真实 Redis 或复杂反射替换。
- **建议**: 逐步将 `RedisUtil` 改造为 Spring Bean（`@Component`），通过注入使用，或提供可替换的接口层。

**P1-3: GlobalExceptionHandler 日志配置未脱敏**
- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/java/com/carlos/boot/handler/GlobalExceptionHandler.java`
- **说明**: 异常处理器中直接记录 `e.getMessage()`，若底层抛出的异常消息包含 SQL 片段、Token、密码等敏感信息，可能通过日志泄露。
- **建议**: 增加敏感信息正则过滤（如 `password=xxx`、`token=xxx`），或配置结构化日志脱敏处理器。

**P1-4: CarlosPermissionEvaluator 权限拒绝未记录安全日志**
- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-security/src/main/java/com/carlos/security/permission/CarlosPermissionEvaluator.java`
- **说明**: 权限校验失败时直接返回 `false`，无审计日志记录，不利于安全回溯。
- **建议**: 增加 `WARN` 级别日志，记录主体、目标、权限及失败原因。

#### 🟢 P2 — 优化建议

**P2-1: JacksonConfig 未显式限制 Visibility**
- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-json/src/main/java/com/carlos/json/jackson/config/JacksonConfig.java`
- **说明**: 当前配置未显式设置 `Visibility`，默认继承行为。建议显式配置 `PropertyAccessor.ALL, Visibility.PUBLIC_ONLY` 作为防御性默认值。

**P2-2: UserInfo 字段注释重复/错误**
- **文件**: `carlos-spring-boot/carlos-spring-boot-core/src/main/java/com/carlos/core/auth/UserInfo.java`
- **说明**: `realName`、`phone`、`email` 字段注释均标注为 "真实姓名"（历史已知问题），建议修正注释。

**P2-3: JacksonSerializer 对象 mapper 在静态块创建**
- **说明**: 每个 `JacksonSerializer` 实例共享同一个静态 `ObjectMapper`（无并发问题），但失去了按 RedisTemplate 定制序列化的灵活性。当前设计可接受，但建议评估是否需要支持不同数据类型的差异化序列化配置。

---

## 2. 技术趋势洞察

### 2.1 版本对比表

| 组件 | 当前版本 | 最新稳定版 | 差距 | 升级建议 | 来源/日期 |
|------|---------|-----------|------|---------|----------|
| Spring Boot | 3.5.9 | **3.5.10** | 1 patch | ⬆️ 建议升级（29 bug fixes） | spring.io / 2026-01-22 |
| Spring Cloud | 2025.0.1 | **2025.0.2** (Northfields) | 1 patch | ⬆️ 建议升级 | spring.io / 2026-04-02 |
| Spring Cloud Alibaba | 2025.0.0.0 | **2025.1.0.0** | 1 minor | ⚠️ 需 Spring Boot 4.0.x，暂不建议 | sca.aliyun.com / 2025-10 |
| MyBatis-Plus | 3.5.15 | **3.5.16** | 1 patch | ⬆️ 建议升级 | mvnrepository / 2026-01-11 |
| MyBatis-Plus Join | 1.5.4 | — | — | 跟踪社区版本 | — |
| Redisson | 3.51.0 | **3.52.0** | 1 minor | ⬆️ 建议升级 | mvnrepository / 2025-09-25 |
| Hutool | 5.8.40 | **5.8.44** | 4 patches | ⬆️ 建议升级 | GitHub / 2026-03-12 |
| MapStruct | 1.6.3 | 1.7.0.Beta1 | — | ⏸️ 等待 1.7.0 GA | mapstruct.org / 2026-02-01 |
| ClickHouse JDBC | 0.9.6 | **0.9.7** | 1 patch | ⬆️ 建议升级 | mvnrepository / 2026-03-18 |
| Java SE | 21 (LTS) | 25 (LTS) / 26 (latest) | — | ✅ JDK 21 支持至 2031，暂无需升级 | oracle.com / 2026-04 |
| Seata | 2.0.0 | 2.1.0 (SCA 2025.1.0.0 中) | 1 minor | ⏸️ 随 SCA 升级计划 | — |
| SkyWalking | 9.5.0 | 追踪中 | — | 跟踪 APM 社区 | — |

### 2.2 重点趋势分析

1. **Spring Boot 3.5.x 进入维护末期**：根据 endoflife.date 与 HeroDevs 信息，Spring Boot 3.5.x 的 OSS 支持将于 **2026-06-30** 结束。当前项目需评估是否在此之前迁移至 Spring Boot 4.0.x，或购买第三方扩展支持。建议本季度启动兼容性评估。

2. **Redisson 4.x 已发布**：Redisson 已推出 4.x 系列（4.1.0 于 2025-12-16，4.3.1 于 2026-04-06），底层可能有 Breaking Changes。当前使用 3.51.0，建议先升级至 3.52.0，再规划 4.x 迁移。

3. **MapStruct 1.7 即将 GA**：1.7.0.Beta1 发布于 2026-02-01，新增 Native Optional support 和 Kotlin 改进。稳定版预计 2026 年 Q2 发布，可关注。

4. **ClickHouse Java Client 0.9.7**：发布于 2026-03-18，建议升级以获取最新 JDBC 兼容性修复。

5. **Hutool 5.8.44**：2026-03-12 发布，包含工具类改进，建议升级。

---

## 3. 优化建议清单

| 优先级 | 模块 | 建议内容 | 预期收益 | 实施难度 |
|--------|------|---------|---------|----------|
| P0 | starter-redis / auth | **将 3 处 ObjectMapper Visibility.ANY 改为 PUBLIC_ONLY** | 消除序列化攻击面，防止敏感字段泄露 | 低（配置变更） |
| P0 | starter-ai | **为 AiChatServiceImpl.memoryStore 增加过期清理机制** | 防止 AI 服务 OOM，提升长时稳定性 | 中（需设计 eviction） |
| P1 | starter-ai | **移除 CarlosAiAutoConfiguration 中的 System.out.println** | 消除生产环境非结构化输出 | 极低 |
| P1 | starter-redis | **RedisUtil 改造为 Spring Bean，支持注入与 Mock** | 提升单元测试覆盖率与可维护性 | 中（涉及面广） |
| P1 | starter-web | **GlobalExceptionHandler 日志增加敏感信息脱敏过滤** | 防止日志泄露 Token/密码/SQL | 低 |
| P1 | starter-security | **CarlosPermissionEvaluator 权限拒绝增加 WARN 审计日志** | 支持安全回溯与异常检测 | 低 |
| P2 | starter-json | **JacksonConfig 显式配置 Visibility.PUBLIC_ONLY** | 防御性编程，统一序列化策略 | 低 |
| P2 | core | **修正 UserInfo 字段注释重复问题** | 提升代码可读性 | 极低 |
| P2 | dependencies | **升级 Spring Boot 3.5.10 + Spring Cloud 2025.0.2 + MyBatis-Plus 3.5.16 + Redisson 3.52.0 + Hutool 5.8.44 + ClickHouse JDBC 0.9.7** | 获取 bug fix、安全补丁、兼容性改进 | 低（版本升级） |

---

## 4. 具体优化计划

### 本周可执行（1-3 天）
- [ ] **P0-1/2/3**: 统一修复 3 处 `ObjectMapper.setVisibility(ALL, ANY)` → 改为 `PUBLIC_ONLY` 或移除该行（确认是否依赖私有字段反序列化，若需要则改用 `@JsonProperty`/`@JsonAutoDetect` 在实体级别精确控制）。
- [ ] **P1-1**: 删除 `CarlosAiAutoConfiguration.java` 中的 `System.out.println`。
- [ ] **P2-2**: 修正 `UserInfo.java` 中 `phone`、`email` 字段的 Javadoc 注释。
- [ ] **P2-3**: 升级依赖 patch 版本（Spring Boot 3.5.10、Spring Cloud 2025.0.2、MyBatis-Plus 3.5.16、Hutool 5.8.44、ClickHouse JDBC 0.9.7）。

### 本月可执行（2-4 周）
- [ ] **P0-4**: 为 `AiChatServiceImpl` 的 `memoryStore` 设计并实现过期策略：
  - 方案 A：使用 Guava `CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).maximumSize(10000)` 替代 `ConcurrentHashMap`
  - 方案 B：接入 Caffeine（项目已有依赖），提供 `memory.maxSize` / `memory.expireMinutes` 配置项
- [ ] **P1-2**: 启动 `RedisUtil` 改造设计：将核心方法迁移到 `RedisTemplate` 的 Bean 包装类中，保持 API 兼容的同时支持注入。
- [ ] **P1-3**: 在 `GlobalExceptionHandler` 中引入 `SensitiveDataMasker` 工具类，对 `Throwable.getMessage()` 和 `StackTrace` 中的敏感模式进行正则脱敏。
- [ ] **P1-4**: 为 `CarlosPermissionEvaluator` 增加 `log.warn("Permission denied: subject={}, target={}, permission={}", ...)`。

### 长期规划（1-3 个月）
- [ ] **架构**: 评估 Spring Boot 4.0.x + Spring Cloud 2025.1.x + Spring Cloud Alibaba 2025.1.0.0 的兼容性迁移计划。注意 JDK 要求可能变化。
- [ ] **性能**: Redisson 3.x → 4.x 迁移评估（关注 Breaking Changes）。
- [ ] **安全**: 引入自动化安全扫描（如 OWASP Dependency-Check Maven 插件），将 CVE 检测纳入 CI。
- [ ] **可观测性**: 评估将 `CarlosPermissionEvaluator` 拒绝事件和 `LoginAttemptManager` 锁定事件推送到 Audit 模块（ClickHouse），形成统一安全审计视图。

---

## 5. 待办事项

- [ ] P0-1: 修复 `JacksonSerializer.java` visibility 配置
- [ ] P0-2: 修复 `RedisOAuth2AuthorizationService.java` visibility 配置
- [ ] P0-3: 修复 `AuthorizationGrantTypeMixin.java` visibility 配置
- [ ] P0-4: 为 `AiChatServiceImpl` ChatMemory 增加过期清理
- [ ] P1-1: 移除 `CarlosAiAutoConfiguration` System.out.println
- [ ] P1-2: 设计 RedisUtil 改造方案（静态工具 → Bean）
- [ ] P1-3: GlobalExceptionHandler 日志脱敏
- [ ] P1-4: CarlosPermissionEvaluator 拒绝日志
- [ ] P2-1: JacksonConfig 显式 visibility 配置
- [ ] P2-2: 修正 UserInfo 注释
- [ ] P2-3: 依赖 patch 升级（Spring Boot 3.5.10, Spring Cloud 2025.0.2, MyBatis-Plus 3.5.16, Hutool 5.8.44, ClickHouse JDBC 0.9.7, Redisson 3.52.0）
- [ ] 长期: Spring Boot 4.0 迁移评估
- [ ] 长期: Redisson 4.x 迁移评估
- [ ] 长期: 引入 OWASP Dependency-Check 插件

---

> 报告生成时间: 2026-05-13 09:00 (Asia/Shanghai)
> 扫描 Commit: fd32e218cd1e54e9de0623b7cebce515545cd993
> 生成工具: Carlos Framework 每日扫描 Agent
