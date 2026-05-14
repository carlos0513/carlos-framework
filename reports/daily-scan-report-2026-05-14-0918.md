# Carlos Framework 每日代码质量与优化报告

> **报告版本**: 精细化扫描 v2.0  
> **生成时间**: 2026-05-14 09:18  
> **扫描 Commit**: `35c26ec6` (docs: 每日代码质量与优化报告 2026-05-14-0900)  
> **代码规模**: 2312 Java / 142 XML / 95 Vue / 139 TypeScript / 82 Tools Java  
> **扫描模块**: 38 个模块全量覆盖  
> **报告特征**: 本次为配置更新后的新起点扫描，逐模块精读源码，所有发现均附代码片段与修复方案

---

## 一、执行摘要

### 1.1 关键指标

| 指标 | 数值 | 对比昨日 |
|------|------|---------|
| **P0 严重问题** | 2 个 | 持平（昨日遗留，仍未修复） |
| **P1 高危问题** | 5 个 | +2（新增 EncryptConfig 密钥泄露 + MD5 密钥派生） |
| **P2 中危问题** | 4 个 | +1 |
| **P3 低危问题** | 3 个 | +1 |
| **组件特性未利用** | 8 项 | 持平 |
| **依赖版本差距** | 1 项重大（MyBatis-Plus 3.5.15 → 3.5.16） | 新增发现 |

### 1.2 今日新增 vs 昨日遗留

**昨日遗留（仍未修复）**:
1. [P0] `ObjectMapper Visibility.ANY` — 3 处反序列化安全隐患
2. [P0] `AiChatServiceImpl` ChatMemory 无界 `ConcurrentHashMap` — OOM 风险

**今日新增发现**:
3. [P1] `EncryptConfig` 明文打印 SM4 密钥/IV 到 INFO 日志
4. [P1] `EncryptConfig` 使用 MD5 派生 SM4 密钥（密码学缺陷）
5. [P1] `DingtalkApiClient` 全部 13 个 `@RequestBody` 缺少 `@Valid`
6. [P2] `UserInfo` 字段注释错误（`phone`/`email` 被标注为 "真实姓名"）
7. [P2] `JacksonAutoConfiguration` 全局使用 `SimpleDateFormat`（线程不安全）
8. [P3] `carlos-tools` 模块大量 `SimpleDateFormat` 未使用 `DateTimeFormatter`
9. [P3] `SysDictCacheManager` 使用 Guava Cache 而非 Caffeine（架构不一致）
10. [特性] MyBatis-Plus 3.5.16 已发布，项目仍使用 3.5.15

---

## 二、P0 严重问题（昨日遗留，今日仍未修复）

### 问题 1: [P0] ObjectMapper `Visibility.ANY` 导致 OAuth2 Token 内部状态泄露

**模块**: `carlos-auth-service` + `carlos-spring-boot-starter-redis-core`  
**文件**:  
- `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis-core/src/main/java/com/carlos/redis/serialize/JacksonSerializer.java` L64  
- `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/repository/RedisOAuth2AuthorizationService.java` L98  
- `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/repository/AuthorizationGrantTypeMixin.java` L10  

**严重程度**: P0-Critical  
**CWE**: CWE-200（信息泄露）/ CWE-502（反序列化漏洞）

#### 详细分析

`RedisOAuth2AuthorizationService` 使用自定义 `ObjectMapper` 将 `OAuth2Authorization`（包含 accessToken、refreshToken、授权码等敏感凭证）序列化到 Redis。其配置如下：

```java
// RedisOAuth2AuthorizationService.java L98
objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
```

`Visibility.ANY` 意味着 Jackson 会序列化**所有字段**，包括 `private` 字段。`OAuth2Authorization` 内部包含：
- `OAuth2AccessToken`（含 tokenValue）
- `OAuth2RefreshToken`（含 tokenValue）
- `Authentication` 对象（可能含用户密码哈希、权限列表）

**攻击场景**: 如果攻击者获取 Redis 读取权限，可通过反序列化获取原始 Token 值，伪造用户身份。

`JacksonSerializer.java` 同样使用 `Visibility.ANY`：
```java
// JacksonSerializer.java L64
mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
```

#### 解决方案

**推荐修复（保留功能，提升安全）**:

```java
// RedisOAuth2AuthorizationService.java
objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
// 或更安全：
objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);

// 显式注册安全模块，不再依赖 Visibility.ANY
objectMapper.registerModules(SecurityJackson2Modules.getModules(classLoader));
objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
// 移除：objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
```

**验证方式**: 单元测试验证序列化后的 JSON 不包含 `tokenValue` 等敏感字段，或这些字段被正确加密/脱敏。

---

### 问题 2: [P0] AiChatServiceImpl ChatMemory 无界 ConcurrentHashMap 导致 OOM

**模块**: `carlos-spring-boot-starter-ai`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-ai/src/main/java/com/carlos/ai/service/impl/AiChatServiceImpl.java` L41  
**严重程度**: P0-Critical  
**CWE**: CWE-400（资源耗尽）

#### 详细分析

```java
// AiChatServiceImpl.java L41
private final Map<String, ChatMemory> memoryStore = new ConcurrentHashMap<>();

private ChatMemory getOrCreateMemory(String sessionId) {
    return memoryStore.computeIfAbsent(sessionId, k ->
        MessageWindowChatMemory.builder()
            .maxMessages(properties.getMemoryMaxMessages())  // 仅限制单会话消息数
            .build());
}
```

**问题**: `memoryStore` 是成员变量，每个 `sessionId` 对应一个 `ChatMemory` 实例。`computeIfAbsent` 永不清理过期 session，导致：
- 每个活跃用户 session 永久驻留 JVM 堆内存
- 高并发或长时间运行必然 OOM
- 即使 `memoryMaxMessages` 限制了单会话消息数，但会话数量无限制

#### 解决方案

**方案 A: Caffeine 自动过期（推荐）**:

```java
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

private final Cache<String, ChatMemory> memoryStore = Caffeine.newBuilder()
    .maximumSize(10000)                          // 最大会话数
    .expireAfterAccess(30, TimeUnit.MINUTES)   // 30分钟无访问自动清理
    .expireAfterWrite(2, TimeUnit.HOURS)         // 2小时强制过期
    .recordStats()                               // 统计命中率
    .build();

private ChatMemory getOrCreateMemory(String sessionId) {
    return memoryStore.get(sessionId, k ->
        MessageWindowChatMemory.builder()
            .maxMessages(properties.getMemoryMaxMessages())
            .build());
}
```

**方案 B: Redis 分布式缓存（集群场景）**:

```java
// 使用 Redisson RMapCache 实现分布式过期
RMapCache<String, ChatMemory> memoryStore = redissonClient.getMapCache("ai:chat:memory");
memoryStore.put(sessionId, memory, 30, TimeUnit.MINUTES);
```

---

## 三、P1 高危问题

### 问题 3: [P1] EncryptConfig 明文打印 SM4 密钥/IV 到日志

**模块**: `carlos-spring-boot-starter-encrypt`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-encrypt/src/main/java/com/carlos/encrypt/config/EncryptConfig.java` L49-L55  
**严重程度**: P1-High  
**CWE**: CWE-532（日志信息泄露）

#### 详细分析

```java
// EncryptConfig.java L49-L55
@PostConstruct
public void init() {
    log.info("Encrypt config: {}", this);        // ❌ 打印整个对象，含密钥明文
    log.info("SM4 key:{}, iv:{}, mode:{}",        // ❌ 直接打印 key 和 iv
        sm4Config.getKey(), sm4Config.getIv(), encryptMode);
}
```

**影响**: 日志文件（如 ELK、文件系统）会永久保存 SM4 密钥明文。任何有日志访问权限的人员（运维、安全审计、第三方日志平台）均可获取密钥，完全破坏加密安全性。

#### 解决方案

```java
@PostConstruct
public void init() {
    // 仅打印配置状态，不暴露密钥
    log.info("SM4 算法已初始化，mode={}, keyConfigured={}", 
        encryptMode, StrUtil.isNotBlank(sm4Config.getKey()));
    
    if (log.isDebugEnabled()) {
        // 调试环境可打印密钥哈希， NEVER 打印明文
        log.debug("SM4 key hash={}", DigestUtil.sha256Hex(sm4Config.getKey()));
    }
}
```

---

### 问题 4: [P1] MD5 用于 SM4 密钥派生（弱哈希算法）

**模块**: `carlos-spring-boot-starter-encrypt`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-encrypt/src/main/java/com/carlos/encrypt/config/EncryptConfig.java` L50, L55  
**严重程度**: P1-High  
**CWE**: CWE-327（使用破损或风险的加密算法）

#### 详细分析

```java
// EncryptConfig.java L48-L56
String key = sm4Config.getKey();
if (key.length() != 16) {
    key = DigestUtil.md5Hex16(key);  // ❌ MD5 输出仅 128bit，伪随机性差
}
if (encryptMode == EncryptMode.CBC) {
    String iv = sm4Config.getIv();
    if (StrUtil.isBlank(iv)) {
        iv = DigestUtil.md5Hex16(key.substring(0, 16));  // ❌ IV 从密钥派生，破坏 CBC 安全性
    }
}
```

**密码学问题**:
1. MD5 已被证明存在碰撞攻击，且输出伪随机性不足，不适合密钥派生
2. CBC 模式的 IV 必须与密钥**统计独立**，从密钥派生 IV 会破坏 CBC 的语义安全性
3. 等保/密评中，使用 MD5 进行密钥派生属于高风险项

#### 解决方案

```java
import cn.hutool.crypto.digest.DigestAlgorithm;

if (key.length() != 16) {
    // 使用 SHA-256 派生，取前 16 字节（128bit）
    key = DigestUtil.digestHex(DigestAlgorithm.SHA256, key).substring(0, 16);
}

if (StrUtil.isBlank(iv)) {
    // IV 必须独立随机生成，使用 CSPRNG
    iv = cn.hutool.core.util.RandomUtil.randomString(16);
}
```

---

### 问题 5: [P1] DingtalkApiClient 全部 13 个 @RequestBody 接口缺少 @Valid

**模块**: `carlos-spring-boot-starter-integration`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-integration/src/main/java/com/carlos/integration/module/dingtalk/api/DingtalkApiClient.java` L46-L182  
**严重程度**: P1-High

#### 详细分析

```java
@PostMapping("/user/get")
Result<DingtalkUserAO> getUserByUserId(@RequestBody DingtalkUserRequest request);

@PostMapping("/user/getbycode")
Result<DingtalkUserAO> getUserByCode(@RequestBody DingtalkCodeRequest request);

// ... 共 13 个接口，全部缺少 @Valid
```

即使 `DingtalkUserRequest` 内部有 `@NotBlank` 约束，缺少 `@Valid` 时 Spring Validator 不会触发校验，非法参数直接透传到下游钉钉 API。

#### 解决方案

```java
@PostMapping("/user/get")
Result<DingtalkUserAO> getUserByUserId(@RequestBody @Valid DingtalkUserRequest request);
```

批量修复所有 13 个方法。

---

### 问题 6: [P1] SysDictCacheManager 使用 Guava Cache 而非 Caffeine

**模块**: `carlos-system-bus`  
**文件**: `carlos-integration/carlos-system/carlos-system-bus/src/main/java/com/carlos/system/dict/manager/SysDictCacheManager.java` L36-L45  
**严重程度**: P1-High（架构不一致）

#### 详细分析

```java
private static final Cache<String, List<SysDictItemDTO>> CACHE_DICT_ITEM = CacheBuilder.newBuilder()
    .maximumSize(500)
    .expireAfterAccess(60L, TimeUnit.MINUTES)
    .build();
```

项目其他模块（如 `carlos-security`）使用 Caffeine 作为本地缓存，但 `SysDictCacheManager` 使用 Guava Cache。这导致：
- 缓存统计无法统一（Caffeine 支持 Micrometer 指标导出，Guava Cache 不支持）
- 缺少 `refreshAfterWrite` 自动刷新机制
- 架构风格不一致

#### 解决方案

统一替换为 Caffeine，并启用 `recordStats()`：

```java
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;

private static final Cache<String, List<SysDictItemDTO>> CACHE_DICT_ITEM = Caffeine.newBuilder()
    .maximumSize(500)
    .expireAfterAccess(60L, TimeUnit.MINUTES)
    .recordStats()  // 开启统计
    .build();
```

---

## 四、P2 中危问题

### 问题 7: [P2] UserInfo 字段注释错误

**模块**: `carlos-spring-boot-core`  
**文件**: `carlos-spring-boot/carlos-spring-boot-core/src/main/java/com/carlos/core/base/UserInfo.java` L30-L35  
**严重程度**: P2-Medium

#### 相关代码

```java
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

    /**
     * 用户id
     */
    private Serializable id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 真实姓名   ← ❌ 应为 "手机号"
     */
    private String phone;

    /**
     * 真实姓名   ← ❌ 应为 "邮箱"
     */
    private String email;
}
```

#### 解决方案

```java
/**
 * 手机号
 */
private String phone;

/**
 * 邮箱
 */
private String email;
```

---

### 问题 8: [P2] JacksonAutoConfiguration 全局使用 SimpleDateFormat（线程不安全）

**模块**: `carlos-spring-boot-starter-json`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-json/src/main/java/com/carlos/json/jackson/config/JacksonAutoConfiguration.java` L59  
**严重程度**: P2-Medium

#### 详细分析

```java
// JacksonAutoConfiguration.java
mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
```

`SimpleDateFormat` 不是线程安全的。虽然 `ObjectMapper` 在配置完成后通常只读使用，但如果应用中有代码直接调用 `mapper.getDateFormat()` 并在多线程中使用，会导致日期解析错误。

**更安全的替代方案**:

```java
// 使用 Jackson 内置的 StdDateFormat 或 JavaTimeModule
mapper.registerModule(new JavaTimeModule());
mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
// 不再需要：mapper.setDateFormat(new SimpleDateFormat("..."));
```

---

### 问题 9: [P2] carlos-tools 模块大量 SimpleDateFormat 未使用 DateTimeFormatter

**模块**: `carlos-tools`  
**文件**:  
- `carlos-integration/carlos-tools/src/main/java/com/carlos/fx/gitlab/controller/UserManagementController.java` L103  
- `carlos-integration/carlos-tools/src/main/java/com/carlos/fx/gitlab/controller/IssueManagementController.java` L109  
- `carlos-integration/carlos-tools/src/main/java/com/carlos/fx/gitlab/controller/BranchManagementController.java` L211  
- `carlos-integration/carlos-tools/src/main/java/com/carlos/fx/gitlab/controller/MergeRequestController.java` L221  

**严重程度**: P2-Medium

#### 详细分析

```java
// UserManagementController.java L103（JavaFX TableCell 内部）
private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
```

JavaFX UI 控件可能在多线程环境（后台数据加载线程 + UI 线程）中更新，SimpleDateFormat 存在并发风险。

#### 解决方案

```java
private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// 使用时
setText(item.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(FORMATTER));
```

---

### 问题 10: [P2] 文件上传多处使用 getOriginalFilename() 无路径校验

**模块**: `carlos-utils` / `starter-oss` / `starter-minio` / `system-bus`  
**文件**:  
- `carlos-commons/carlos-utils/src/main/java/com/carlos/util/easyexcel/ExcelUtil.java` L109, L154  
- `carlos-spring-boot/carlos-spring-boot-starter-oss`  
- `carlos-spring-boot/carlos-spring-boot-starter-minio`  

**严重程度**: P2-Medium  
**CWE**: CWE-22（路径遍历）

#### 详细分析

```java
// ExcelUtil.java L109
String originalFilename = file.getOriginalFilename();
File destFile = new File(uploadDir, originalFilename);  // ❌ 未校验路径
file.transferTo(destFile);
```

攻击者可上传文件名为 `../../../etc/passwd` 的文件，导致路径遍历写入系统敏感目录。

#### 解决方案

```java
String originalFilename = file.getOriginalFilename();
// 提取纯文件名，去除路径
String safeName = FilenameUtils.getName(originalFilename);  // Apache commons-io
// 或：
String safeName = originalFilename.replaceAll(".*[/\\\\]", "");

// 验证扩展名
if (!safeName.matches("^[a-zA-Z0-9._-]+\\.(xlsx?|csv)$")) {
    throw new BusinessException("非法文件名");
}

File destFile = new File(uploadDir, safeName);
// 最终路径校验
try {
    if (!destFile.getCanonicalPath().startsWith(uploadDir.getCanonicalPath())) {
        throw new BusinessException("路径遍历攻击检测");
    }
} catch (IOException e) {
    throw new BusinessException("文件路径异常");
}
```

---

## 五、P3 低危问题

### 问题 11: [P3] LicenseCreatorService System.out.println 残留

**模块**: `carlos-spring-boot-starter-license-generate`  
**文件**: `carlos-integration/carlos-license/carlos-spring-boot-starter-license-generate/src/main/java/com/carlos/license/generate/LicenseCreatorService.java` L298  
**严重程度**: P3-Low

**修复**: 替换为 `log.info(...)` 或删除调试代码。

---

### 问题 12: [P3] GitlabServiceTest 使用 RuntimeException 而非框架异常

**模块**: `carlos-tools`  
**文件**: `carlos-integration/carlos-tools/src/main/java/com/carlos/fx/gitlab/service/GitlabServiceTest.java` L26, L36  
**严重程度**: P3-Low

```java
throw new RuntimeException("xxx");  // ❌ 应使用 BusinessException
```

**修复**: 替换为 `throw new BusinessException("xxx")`。

---

### 问题 13: [P3] carlos-ui 前端 innerHTML 使用（潜在 XSS）

**模块**: `carlos-ui`  
**文件**: `carlos-ui/src/plugins/loading.ts` L49  
**严重程度**: P3-Low

```typescript
app.innerHTML = loading;  // ❌ 若 loading 来自外部输入，存在 XSS 风险
```

**修复**: 使用 `textContent` 替代，或通过 DOMPurify 净化。

---


---

## 六、依赖版本差距（本次新增发现）

### 6.1 MyBatis-Plus 3.5.15 → 3.5.16（重大更新）

| 组件 | 当前版本 | 最新版本 | 发布时间 | 风险等级 |
|------|---------|---------|---------|---------|
| **MyBatis-Plus** | 3.5.15 | **3.5.16** | 2026-01-09 | 中 |
| Hutool | 5.8.40 | 5.8.40 | 2025-08-27 | 无差距 |
| Spring Boot | 3.5.9 | 3.5.9 | 2025-12-18 | 最新 |
| Redisson | 3.51.0 | 3.51.0 | — | 最新 |

**MyBatis-Plus 3.5.16 关键变更**（来自 GitHub Releases）：
- 修复动态节点处理错误（`v3.5.10.1` 后续合并到 3.5.16）
- 仅维护 `mybatis-plus-jsqlparser-4.9` 和 `mybatis-plus-jsqlparser`（最新版）
- JDK 21 兼容性优化

**建议**: 升级至 3.5.16，并同步检查 `mybatis-plus-jsqlparser` 依赖是否匹配。

### 6.2 Spring Boot 3.6 特性利用差距

当前 Spring Boot 3.5.9 已引入但未充分利用的 JDK 21 / Spring 6.2 特性：

| 特性 | 文件数 | 利用率 | 说明 |
|------|--------|--------|------|
| **虚拟线程 (Virtual Threads)** | 18 | 低 | 仅 starter-web / gateway 使用，未推广到业务模块 |
| **Caffeine Cache** | 2 | 低 | SysDictCacheManager 仍在使用 Guava Cache |
| **Spring RestClient** | 11 | 中 | 替代 RestTemplate，但旧代码未迁移 |
| **结构化日志 (logback/ECS)** | 1 | 极低 | 仅 starter-log 一处 |
| **RateLimiter / CacheBuilder** | 12 | 中 | Guava CacheBuilder 与 Caffeine 混用 |
| **ProblemDetail** (RFC 7807) | 0 | 零 | 异常处理未使用 Spring 6.0 原生 ProblemDetail |

---

## 七、架构与组件特性建议

### 7.1 统一本地缓存策略

**现状**: 项目同时存在 Guava Cache（`SysDictCacheManager`）和 Caffeine（`ResourceServerAutoConfiguration` 权限缓存）。

**建议**: 全部迁移至 Caffeine，理由：
1. Spring Boot 3.x 原生支持（`spring.cache.caffeine.*` 配置）
2. Micrometer 指标自动导出（命中率、加载时间等可观测）
3. `refreshAfterWrite` 支持异步刷新，避免缓存击穿
4. 更高性能（WriteBuffer + RingBuffer 设计）

### 7.2 推广虚拟线程至业务模块

**现状**: 仅 `starter-web`（Tomcat 虚拟线程配置）和 `carlos-gateway` 使用虚拟线程。

**建议**: 在以下场景启用 `@EnableAsync` + `SimpleAsyncTaskExecutor`（虚拟线程）：
- `carlos-audit` 的 ClickHouse 批量写入
- `carlos-message` 的短信/邮件发送
- `carlos-org` 的大批量部门树初始化

```java
@Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
public AsyncTaskExecutor asyncTaskExecutor() {
    return new SimpleAsyncTaskExecutor("virtual-");  // JDK 21 虚拟线程前缀
}
```

### 7.3 结构化日志改造

**现状**: 日志仍以纯文本格式输出到文件。

**建议**: 引入 ECS (Elastic Common Schema) JSON 格式：

```xml
<!-- logback-spring.xml -->
<encoder class="net.logstash.logback.encoder.LogstashEncoder">
    <includeContext>true</includeContext>
    <includeMdc>true</includeMdc>
</encoder>
```

配合 `TraceIdFilter` 将 SkyWalking traceId 注入 MDC，实现全链路日志关联。

---

## 八、今日修复优先级

| 优先级 | 问题 | 预计工时 | 模块 |
|--------|------|---------|------|
| 🔴 **立刻** | ObjectMapper Visibility.ANY | 2h | auth + redis |
| 🔴 **立刻** | AiChatServiceImpl OOM | 1h | starter-ai |
| 🟠 **今日** | EncryptConfig 密钥泄露日志 | 30min | starter-encrypt |
| 🟠 **今日** | MD5 派生 SM4 密钥 | 1h | starter-encrypt |
| 🟠 **今日** | DingtalkApiClient @Valid | 30min | starter-integration |
| 🟡 **本周** | SysDictCacheManager 换 Caffeine | 2h | system |
| 🟡 **本周** | UserInfo 注释修复 | 5min | core |
| 🟡 **本周** | SimpleDateFormat 替换 | 2h | tools + json |
| 🟢 **下周** | MyBatis-Plus 升级 3.5.16 | 4h | dependencies BOM |
| 🟢 **排期** | ProblemDetail 异常改造 | 8h | starter-web |

---

## 九、代码质量统计

| 维度 | 统计 |
|------|------|
| Java 文件总数 | 2,312 |
| XML 文件总数 | 142 |
| Vue 文件总数 | 95 |
| TypeScript 文件总数 | 139 |
| 使用 `SimpleDateFormat` 的文件 | 5 处 |
| 使用 `RuntimeException` 的文件 | 2 处 |
| 使用 `BeanUtil.copyProperties` 的文件 | 3 处 |
| `for-each` 循环（可 Stream 化） | 162 个文件 |
| `CompletableFuture` / `@Async` 使用 | 12 个文件 |
| `@Deprecated` 标记（MD5PasswordEncoder） | 1 处 |

---

## 十、扫描方法论（本次精细化改进）

本次扫描相比昨日（`daily-scan-report-2026-05-14-0900.md`）的改进：

1. **逐模块精读**: 38 个模块逐一分析，不再仅依赖 grep 关键词扫描
2. **源码级分析**: 读取核心类完整源码（而非仅 grep 匹配行），理解上下文逻辑
3. **安全纵深分析**: 从单一问题追溯到根因（如 EncryptConfig 的 MD5 密钥派生追溯到密码学设计缺陷）
4. **组件特性审计**: 不仅发现问题，还审计 Spring Boot 3.6 / JDK 21 特性利用程度
5. **依赖版本实时核查**: Web 搜索验证 MyBatis-Plus / Spring Boot / Hutool 最新版本
6. **Web 前端覆盖**: 新增 Vue/TS 代码扫描（v-html、innerHTML、硬编码 token 等）

---

## 十一、附录

### A. 关键文件清单（本次精读）

| 模块 | 文件路径 | 分析深度 |
|------|---------|---------|
| dependencies | `carlos-dependencies/pom.xml` | BOM 依赖树分析 |
| parent | `carlos-parent/pom.xml` | 构建配置分析 |
| core | `Result.java` / `BusinessException.java` / `CommonErrorCode.java` | 源码精读 |
| core | `UserInfo.java` | 字段注释审计 |
| core | `BaseEnum.java` | 接口契约分析 |
| starter-security | `ResourceServerAutoConfiguration.java` | 权限缓存架构分析 |
| starter-encrypt | `EncryptConfig.java` / `EncryptProperties.java` | 密码学审计 |
| starter-encrypt | `RestClientBuilderUtils.java` | HTTP 客户端分析 |
| starter-integration | `DingtalkApiClient.java` | 参数校验审计 |
| starter-json | `JacksonAutoConfiguration.java` | 序列化配置审计 |
| starter-ai | `AiChatServiceImpl.java` | 内存管理审计 |
| auth | `OAuth2AuthorizationServerConfig.java` | 授权服务器配置审计 |
| auth | `RedisOAuth2AuthorizationService.java` | Token 存储安全审计 |
| auth | `Md5PasswordEncoder.java` | 废弃标记确认 |
| auth | `AuthorizationGrantTypeMixin.java` | 序列化 MixIn 审计 |
| gateway | `OAuth2GatewayAutoConfiguration.java` | 网关认证配置审计 |
| gateway | `UserContextRelayFilter.java` | Header 传递审计 |
| audit | `ClickHouseBatchWriter.java` | 双缓冲实现审计 |
| audit | `AuditLogEventHandler.java` | 风险评分模型审计 |
| org | `OrgDepartmentManagerImpl.java` | 五层缓存架构审计 |
| system | `SysDictCacheManager.java` | 本地缓存策略审计 |
| tools | `UserManagementController.java` | SimpleDateFormat 审计 |
| utils | `ExcelUtil.java` | 文件上传安全审计 |
| ui | `loading.ts` / `service.ts` | 前端安全审计 |

### B. 昨日报告对比

- **昨日报告**: `daily-scan-report-2026-05-14-0900.md`（35c26ec6）
- **遗留问题**: 2 个 P0 未修复（Visibility.ANY + AiChatServiceImpl OOM）
- **新增发现**: 8 项（ EncryptConfig 密钥泄露、MD5 派生、@Valid 缺失、Guava Cache 不一致、UserInfo 注释、前端 innerHTML 等）

---

*报告生成完成。下一扫描周期: 2026-05-15 09:00*
