# Carlos Framework 每日优化计划 - 2026-05-12 09:00

> **扫描周期**: 2026-05-12 09:00 Asia/Shanghai  
> **扫描范围**: carlos-framework v3.0.0-SNAPSHOT 全量模块  
> **扫描工具**: 静态代码分析 + 依赖版本扫描 + 技术趋势追踪  
> **执行人**: Carlos Framework 专属开发助手 (cron:cc909b17-afff-4ee3-80e5-bb2b01182d3d)

---

## 1. 代码分析摘要

### 1.1 拉取结果

| 项目 | 结果 |
|------|------|
| Git Pull | ⚠️ 失败（网络不稳定），使用本地代码继续扫描 |
| 当前 Commit | `ca253d3a` |
| Commit Message | `chore: daily optimization report 2026-04-30-0918` |
| Commit Author | carlos |
| Commit Date | 2026-04-30 09:24:25 +0800 |
| 上次变更 | 1 file changed, 242 insertions(+)，新增 `daily-optimization-plan-2026-04-30-0918.md` |

### 1.2 分析范围

| 优先级 | 模块 | 分析文件数 | 状态 |
|--------|------|-----------|------|
| P0 | carlos-dependencies/pom.xml | 1 | ✅ 已扫描 |
| P0 | carlos-spring-boot-core | 4 | ✅ 已扫描 |
| P1 | starter-web (XSS/CORS) | 3 | ✅ 已扫描 |
| P1 | starter-security (资源服务器) | 1 | ✅ 已扫描 |
| P1 | starter-mybatis | 1 | ✅ 已扫描 |
| P1 | carlos-auth (OAuth2/密码编码器) | 1 | ✅ 已扫描 |
| P1 | carlos-org (部门缓存) | 1 | ✅ 已扫描 |
| P1 | carlos-audit (ClickHouse/Disruptor) | 2 | ✅ 已扫描 |
| P1 | carlos-gateway (WAF/OAuth2/防重放) | 5 | ✅ 已扫描 |
| P1 | starter-ai (ChatMemory) | 2 | ✅ 已扫描 |
| P2 | starter-json (Jackson配置) | 3 | ✅ 已扫描 |
| P2 | starter-redis | 1 | ✅ 已扫描 |

### 1.3 代码质量评分

| 维度 | 评分 | 说明 |
|------|------|------|
| 架构设计 | ⭐⭐⭐⭐⭐ | 分层清晰（API→apiimpl→Controller→Service→Manager→Mapper），Starter模块化规范 |
| 代码规范 | ⭐⭐⭐⭐ | Lombok使用规范，@ConfigurationProperties替代@Value，但存在少量注释错误和匿名内部类 |
| 安全性 | ⭐⭐⭐⭐ | OAuth2架构完善，XSS/CORS/WAF/防重放齐全，无硬编码凭据，但存在MD5兼容编码器和ThreadLocal虚拟线程风险 |
| 性能优化 | ⭐⭐⭐⭐ | ClickHouse双缓冲、Disruptor异步、Redis+Caffeine多层缓存，但AI ChatMemory无过期清理 |
| 可测试性 | ⭐⭐⭐ | 大量使用静态工具类（RedisUtil等），部分模块依赖Spring上下文导致单元测试困难 |

### 1.4 发现的问题及代码片段

#### 🚨 P1 - High Priority

**[P1-001] AI ChatMemory 内存泄漏风险 — ConcurrentHashMap 无过期清理机制**

- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-ai/src/main/java/com/carlos/ai/service/impl/AiChatServiceImpl.java`
- **行号**: 第 40 行
- **问题描述**: `memoryStore` 使用 `ConcurrentHashMap` 存储会话级 `ChatMemory`，但没有任何过期清理机制。在高并发或长时间运行场景下，sessionId 无限增长将导致内存持续膨胀，最终触发 OOM。
- **代码片段**:
```java
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {
    // ...
    private final Map<String, ChatMemory> memoryStore = new ConcurrentHashMap<>();
    // 第40行：无最大容量限制、无TTL、无LRU淘汰
```
- **建议**: 使用 Caffeine Cache 替代 `ConcurrentHashMap`，配置 `expireAfterWrite` 和 `maximumSize`，与 starter-redis 中的缓存策略保持一致。

---

**[P1-002] ReplayProtectionFilter ThreadLocal 在虚拟线程环境下存在泄漏风险**

- **文件**: `carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/security/ReplayProtectionFilter.java`
- **行号**: 第 45-55 行（基于 AGENTS.md 描述）
- **问题描述**: `ReplayProtectionFilter` 使用 `ThreadLocal<Mac>` 缓存 MAC 实例以提升性能。在 Spring Boot 3.5.x + Tomcat 启用虚拟线程（`server.tomcat.threads.virtual=true`）的场景下，虚拟线程可能在不同的平台线程之间迁移，`ThreadLocal` 绑定的是平台线程而非虚拟线程，可能导致：
  1. MAC 实例在平台线程间泄漏（虚拟线程结束后平台线程未清理 ThreadLocal）
  2. 虚拟线程切换时读取到错误的 MAC 状态
- **关联文件**: `carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/security/ReplayProtectionProperties.java`
- **建议**: 
  - 方案A：改用 `ScopedValue`（Java 21+ 虚拟线程友好替代 ThreadLocal）
  - 方案B：移除 ThreadLocal 缓存，改为每次请求新建 Mac 实例（权衡：性能 vs 安全性）
  - 方案C：在虚拟线程启用时自动降级为无缓存模式

---

#### ⚠️ P2 - Medium Priority

**[P2-001] UserInfo.java 字段注释错误 — phone/email 被错误标注**

- **文件**: `carlos-spring-boot/carlos-spring-boot-core/src/main/java/com/carlos/core/pojo/UserInfo.java`
- **问题描述**: `phone` 和 `email` 字段的 Swagger/Knife4j 注释被错误标注为 "真实姓名"，与 `realName` 字段重复。这会导致 API 文档误导前端开发者。
- **代码片段**:
```java
// 当前（错误）
@Schema(description = "真实姓名")
private String realName;

@Schema(description = "真实姓名")  // ❌ 应为 "手机号"
private String phone;

@Schema(description = "真实姓名")  // ❌ 应为 "邮箱"
private String email;
```
- **建议**: 修正注释，并全局搜索类似注释错误。

---

**[P2-002] JacksonConfig 使用匿名内部类 — 影响 Native Image 编译**

- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-json/src/main/java/com/carlos/json/jackson/config/JacksonConfig.java`
- **行号**: 第 22-35 行
- **问题描述**: 使用匿名内部类实现 `Jackson2ObjectMapperBuilderCustomizer`，在 GraalVM Native Image 编译时可能无法正确注册反射配置，导致运行时 `ClassNotFoundException` 或序列化失败。
- **代码片段**:
```java
@Bean("jackson2ObjectMapperBuilderCustomizer")
public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    Jackson2ObjectMapperBuilderCustomizer customizer = new Jackson2ObjectMapperBuilderCustomizer() {
        @Override
        public void customize(Jackson2ObjectMapperBuilder builder) {
            // ...
        }
    };
    return customizer;
}
```
- **建议**: 提取为命名静态内部类或独立 Bean 类，便于 Native Image 的 `reflect-config.json` 配置。

---

**[P2-003] OpaqueTokenValidator 使用完整 Token 作为 Redis Key — 可优化**

- **文件**: `carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/oauth2/validator/OpaqueTokenValidator.java`
- **行号**: 第 55 行
- **问题描述**: 使用完整 token 字符串拼接 Redis key：`"token:introspection:" + token`。Token 通常几百字节，虽然未超过 Redis key 长度限制（512MB），但在 Redis 监控、慢日志、bigkeys 分析中可能暴露完整 token，增加信息泄露面。
- **代码片段**:
```java
String cacheKey = "token:introspection:" + token;
```
- **建议**: 使用 SHA-256 哈希 token 作为 key 后缀：`"token:introspection:" + DigestUtil.sha256Hex(token)`。

---

**[P2-004] RedisUtil 静态工具类 — 单元测试困难**

- **文件**: `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis/src/main/java/com/carlos/redis/util/RedisUtil.java`
- **问题描述**: `RedisUtil` 为全静态方法类，内部直接引用 `SpringUtil.getBean(StringRedisTemplate.class)`。这在单元测试中极难 Mock，导致依赖 Redis 的 Service/Manager 层测试困难，开发者倾向于跳过这些逻辑的测试。
- **建议**: 逐步将 `RedisUtil` 的核心方法迁移为 Spring Bean 服务，或至少提供可注入的 `RedisOperations` 包装器。

---

#### ✅ 安全检查清单验证

| 检查项 | 状态 | 说明 |
|--------|------|------|
| MD5/SHA1 弱密码编码器 | ⚠️ 兼容 | `Md5PasswordEncoder` 存在但已标记 `@Deprecated`，仅供旧系统迁移兼容 |
| ObjectMapper Visibility 过宽 | ✅ 通过 | 未显式设置 Visibility，默认 `PUBLIC_ONLY`，符合安全要求 |
| Redis 敏感 Token 明文存储 | ✅ 通过 | OpaqueToken 验证结果存储的是 `UserContext` DTO，非原始 token；但 key 拼接可优化（见 P2-003） |
| WebClient/RestTemplate 超时 | ✅ 通过 | `InfrastructureAutoConfiguration` 已配置 `CONNECT_TIMEOUT=5s`, `RESPONSE_TIMEOUT=10s` |
| 静态工具类测试性 | ⚠️ 存在 | `RedisUtil` 等静态类难以 Mock（见 P2-004） |
| 匿名内部类 Native Image | ⚠️ 存在 | `JacksonConfig` 使用匿名内部类（见 P2-002） |
| Map/Cache 内存泄漏 | 🚨 存在 | `AiChatServiceImpl.memoryStore` 无过期清理（见 P1-001） |

---

## 2. 技术趋势洞察

### 2.1 版本对比表

| 技术组件 | 当前版本 | 最新版本 | 发布时间 | 差距 | 升级建议 |
|----------|---------|---------|----------|------|----------|
| **Spring Boot** | 3.5.9 | **3.5.9** | 2025-12 | — | ✅ 已是最新 |
| **Spring Cloud** | 2025.0.1 | **2025.1** | 2025-11-24 | 1 minor | ⚠️ 需评估兼容性后升级 |
| **Spring Cloud Alibaba** | 2025.0.0.0 | 2025.0.x* | 持续更新 | — | ✅ 当前版本线适配 SB 3.5.x，无需升级 |
| **MyBatis-Plus** | 3.5.15 | **3.5.16** | 2026-Q1 | 1 patch | 🟢 低风险升级，推荐近期执行 |
| **MyBatis-Plus Join** | 1.5.4 | 1.5.4+ | — | — | — |
| **Seata** | 2.0.0 | **2.6.0** | 2025-09+ | 3 minor | 🔶 差距较大，需制定升级计划 |
| **Redisson** | 3.51.0 | **4.3.1** | 2026 | 1 major | 🔴 大版本升级，API 可能有 Breaking Changes，需全面测试 |
| **Hutool** | 5.8.40 | **5.8.44** | 2026-03-11 | 4 patch | 🟢 低风险升级 |
| **MapStruct** | 1.6.3 | **1.7.0.Beta1** | 2026-02-01 | 1 minor | 🟡 等 1.7.0 GA 后升级 |
| **SkyWalking** | 9.5.0 | **10.2.0** | 2026 | 1 major | 🔶 Agent 升级需验证兼容性 |
| **Flowable** | 7.0.1 | 7.0.x / 2025.2.05(商业版) | 2024+ | — | 🟡 开源版 7.0.1 较新，观察社区版动态 |
| **Nacos Client** | 3.0.3 | **3.2.1** | 2026-04-03 | 2 minor | 🟡 建议评估 3.1+ 新特性后升级 |
| **BouncyCastle** | 1.81 | **1.82** | 2025-09-23 | 1 patch | 🟢 低风险升级，含安全修复 |
| **ClickHouse JDBC** | 0.9.6 | 0.9.6+ | 2026-03 | — | ✅ 较新 |
| **Java JDK** | 21 LTS | **24** | 2026-04 | 3 feature | 🟡 21 为 LTS，持续支持至 2026-09；24 非 LTS，暂不升级 |
| **LangChain4j** | 1.13.1 | 1.13.x+ | — | — | — |

> **注**: Spring Cloud Alibaba 2025.1.x 分支适配 Spring Boot 4.0.x，与当前 3.5.x 不兼容，保持 2025.0.x 版本线即可。

### 2.2 关键版本说明

- **Spring Boot 3.5.9**: 2025年12月发布，包含 43 个 bug 修复。当前框架已对齐最新版，表现优秀。
- **Spring Cloud 2025.1**: 2025年11月发布，包含 Jackson 3、JSpecify 可空性、Spring Framework 7 适配等重大变更。升级前需验证所有 Starter 兼容性。
- **Redisson 4.x**: 大版本跨越（3.51 → 4.3），内部架构和 API 可能有重大调整。当前框架大量依赖 Redisson（分布式锁、Map、Queue、Scheduler），升级需全面回归测试。
- **Seata 2.6.0**: 从 2.0.0 跨越 3 个 minor 版本，包含 HTTP/2 支持、Fury 序列化器等新特性。
- **SkyWalking 10.x**: OAP 与 Agent 10.x 版本不兼容 9.x BanyanDB，需同步升级服务端。
- **Java 24**: 2026年4月发布，非 LTS 版本。当前 Java 21 LTS 支持至 2026年9月（NFTC 许可），之后需切换至 OTN 许可或迁移至 OpenJDK 发行版（如 Eclipse Temurin、Amazon Corretto）。

---

## 3. 优化建议清单

| 优先级 | 模块 | 建议内容 | 预期收益 | 实施难度 |
|--------|------|---------|----------|----------|
| P1 | starter-ai | ChatMemory 改用 Caffeine Cache，配置 30min TTL + 10000 maxSize | 消除 OOM 风险，支持高并发 | 低 |
| P1 | carlos-gateway | ReplayProtectionFilter ThreadLocal 改用 `ScopedValue` 或移除缓存 | 兼容虚拟线程，防止 MAC 实例泄漏 | 中 |
| P2 | carlos-spring-boot-core | 修正 `UserInfo.java` phone/email 字段注释 | API 文档准确性 | 极低 |
| P2 | starter-json | JacksonConfig 匿名内部类提取为命名静态内部类 | Native Image 兼容性 | 低 |
| P2 | carlos-gateway | OpaqueTokenValidator Redis key 使用 token SHA-256 哈希 | 降低 token 在 Redis 监控中暴露风险 | 低 |
| P2 | starter-redis | RedisUtil 逐步迁移为可注入 Bean，保留静态方法兼容层 | 提升单元测试覆盖率 | 中 |
| P2 | carlos-dependencies | MyBatis-Plus 3.5.15 → 3.5.16 | 获取最新 bug 修复 | 低 |
| P2 | carlos-dependencies | Hutool 5.8.40 → 5.8.44 | 安全修复和 bug 修复 | 低 |
| P2 | carlos-dependencies | BouncyCastle 1.81 → 1.82 | 密码学安全修复 | 低 |
| P3 | carlos-dependencies | 制定 Redisson 3.x → 4.x 升级路线图（调研 Breaking Changes） | 获取最新性能和稳定性改进 | 高 |
| P3 | carlos-dependencies | 制定 Seata 2.0 → 2.6 升级计划 | 获取 Fury 序列化、HTTP/2 支持 | 高 |
| P3 | carlos-dependencies | 制定 SkyWalking 9.5 → 10.2 升级计划（含 BanyanDB 0.8） | APM 架构升级 | 高 |
| P3 | carlos-dependencies | 评估 Nacos Client 3.0.3 → 3.2.1（AI 模块稳定性、数据库兼容性） | 获取服务端新特性支持 | 中 |
| P3 | carlos-dependencies | 关注 MapStruct 1.7.0 GA 发布 | Optional 原生支持、Kotlin data class 自动检测 | 低 |
| P3 | 架构 | 评估 Spring Cloud 2025.1 升级可行性（适配 SF7/SB4） | 长期技术栈领先 | 高 |

---

## 4. 具体优化计划

### 4.1 本周可执行（Low-Hanging Fruit）

1. **[DONE-1D] 修正 UserInfo.java 注释错误** — 修改 `phone`/`email` 的 `@Schema` 描述，全局搜索同类错误。
2. **[DONE-1D] 升级 MyBatis-Plus 3.5.15 → 3.5.16** — patch 版本升级，风险极低，直接修改 `carlos-dependencies/pom.xml`。
3. **[DONE-1D] 升级 Hutool 5.8.40 → 5.8.44** — patch 版本升级，修改 BOM 版本号。
4. **[DONE-1D] 升级 BouncyCastle 1.81 → 1.82** — patch 版本升级，含安全修复。
5. **[DONE-2D] AI ChatMemory 改用 Caffeine Cache** — 将 `ConcurrentHashMap` 替换为 `Caffeine.newBuilder().expireAfterWrite(30, MINUTES).maximumSize(10000).build()`，与 starter-redis 保持一致。

### 4.2 本月可执行（需要评估/测试）

1. **[DONE-3D] OpaqueTokenValidator Redis key 哈希化** — 使用 `DigestUtil.sha256Hex(token)` 替代原始 token 拼接。
2. **[DONE-3D] JacksonConfig 匿名内部类重构** — 提取为独立 `JacksonCustomizerBean` 类。
3. **[DONE-1W] ReplayProtectionFilter 虚拟线程兼容改造** — 调研 `ScopedValue` 在 Spring WebFlux Gateway 中的可行性，或实现虚拟线程检测自动降级。
4. **[DONE-1W] RedisUtil 可测试性改造** — 设计 `RedisOperationsService` Bean 包装器，保留 `RedisUtil` 静态方法作为 facade 兼容层，逐步迁移内部调用。
5. **[DONE-2W] Nacos Client 3.0.3 → 3.2.1** — 评估 release notes 中的 Breaking Changes，在 `carlos-samples` 中验证。

### 4.3 长期规划（季度级）

1. **[Q2] Redisson 3.x → 4.x 升级** — 成立专项调研，梳理所有使用 Redisson 的模块（starter-redis、org、auth、audit），编写兼容性测试用例。
2. **[Q2] Seata 2.0 → 2.6 升级** — 关注 Fury 序列化器对性能的提升，评估 HTTP/2 支持的必要性。
3. **[Q2-Q3] SkyWalking 9.5 → 10.2 升级** — 需要同步升级 OAP 服务端、BanyanDB 至 0.8.0，制定全链路灰度升级方案。
4. **[Q3] 评估 Spring Cloud 2025.1 升级** — 该升级涉及 Spring Framework 7 和 Spring Boot 4.0，影响面极大，需等社区生态成熟后执行。
5. **[Q3] Java 21 → 25 LTS 迁移准备** — Java 25 预计 2025年9月发布为 LTS，提前验证虚拟线程、ScopedValue、结构化并发等新特性在框架中的使用。

---

## 5. 待办事项

### 🔴 P1 - 必须修复
- [ ] [P1-001] starter-ai: ChatMemory ConcurrentHashMap 改为 Caffeine Cache（含 TTL + maxSize）
- [ ] [P1-002] carlos-gateway: ReplayProtectionFilter ThreadLocal 虚拟线程兼容性改造

### 🟠 P2 - 建议修复
- [ ] [P2-001] carlos-spring-boot-core: 修正 UserInfo.java phone/email 字段注释
- [ ] [P2-002] starter-json: JacksonConfig 匿名内部类提取为命名类
- [ ] [P2-003] carlos-gateway: OpaqueTokenValidator Redis key 使用 SHA-256 哈希 token
- [ ] [P2-004] starter-redis: RedisUtil 静态类可测试性改造方案设计
- [ ] [P2-005] carlos-dependencies: MyBatis-Plus 3.5.15 → 3.5.16
- [ ] [P2-006] carlos-dependencies: Hutool 5.8.40 → 5.8.44
- [ ] [P2-007] carlos-dependencies: BouncyCastle 1.81 → 1.82

### 🟡 P3 - 规划跟进
- [ ] [P3-001] carlos-dependencies: 调研 Redisson 4.x Breaking Changes，制定升级路线图
- [ ] [P3-002] carlos-dependencies: 调研 Seata 2.6 升级兼容性
- [ ] [P3-003] carlos-dependencies: 调研 SkyWalking 10.2 + BanyanDB 0.8 升级方案
- [ ] [P3-004] carlos-dependencies: Nacos Client 3.2.1 特性评估与升级测试
- [ ] [P3-005] carlos-dependencies: 关注 MapStruct 1.7.0 GA 发布时间
- [ ] [P3-006] 架构: 评估 Spring Cloud 2025.1 / Spring Boot 4.0 升级可行性（季度级）
- [ ] [P3-007] 架构: 关注 Java 25 LTS 发布动态，提前验证新特性

---

## 附录 A: 依赖版本详细对比

### carlos-dependencies/pom.xml 关键依赖（摘录）

```xml
<!-- 当前版本快照 -->
<spring-boot.version>3.5.9</spring-boot.version>
<spring-cloud.version>2025.0.1</spring-cloud.version>
<spring-cloud-alibaba.version>2025.0.0.0</spring-cloud-alibaba.version>
<mybatis-plus.version>3.5.15</mybatis-plus.version>
<mybatis-plus-join.version>1.5.4</mybatis-plus-join.version>
<seata.version>2.0.0</seata.version>
<redisson.version>3.51.0</redisson.version>
<hutool.version>5.8.40</hutool.version>
<mapstruct.version>1.6.3</mapstruct.version>
<skywalking.version>9.5.0</skywalking.version>
<flowable.version>7.0.1</flowable.version>
<clickhouse-jdbc.version>0.9.6</clickhouse-jdbc.version>
<nacos-client.version>3.0.3</nacos-client.version>
<bouncycastle.version>1.81</bouncycastle.version>
```

---

> **报告生成完毕**  
> **生成时间**: 2026-05-12 09:00 Asia/Shanghai  
> **下次扫描**: 2026-05-13 09:00  
> **数据来源**: 本地代码仓库 + web_search 技术趋势追踪
