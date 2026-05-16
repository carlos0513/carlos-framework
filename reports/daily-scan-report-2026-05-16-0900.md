# Carlos Framework 每日代码质量与优化报告

**生成时间**: 2026-05-16 09:00  
**扫描 Commit**: 73996e0bfe9b1a460987136832eef0f9d4bb92db  
**扫描范围**: 全量模块（2312 个 Java 文件，142 个 XML 文件，95 个 Vue 文件）  
**发现问题总数**: 16 个（P0: 4 个, P1: 6 个, P2: 5 个, P3: 1 个）  
**组件特性利用建议**: 9 条  

---

## 一、执行摘要

### 1.1 今日变更概览
- Git 拉取状态: ✅ 成功（已是最新的）
- Commit: `73996e0b` (docs: 每日代码质量与优化报告 2026-05-15-0900)
- 基准代码 Commit: `d4308143` (fix:优化ExceptionHandler)
- 变更文件数: 26 个（自昨日报告基线以来）
- 新增代码: +698 行 / 删除代码: -296 行
- 主要变更:
  - `fix:优化ExceptionHandler` (2d0aab98, d4308143) — OAuth2/Global 异常处理器优化
  - `fix:文件使用 synchronized` (945c1a58) — 修复 JDK 21 虚拟线程 Pinning 风险（11 个文件）
  - `refactor(cache): P1-005` (176283c9) — SysDictCacheManager/SysConfigServiceImpl Guava Cache → Caffeine 替换

### 1.2 昨日问题修复状态

| 昨日问题 | 级别 | 状态 | 说明 |
|---------|------|------|------|
| JacksonSerializer Visibility.ANY | P0 | ❌ 未修复 | 仍使用 `Visibility.ANY` |
| RedisOAuth2AuthorizationService Visibility.ANY | P0 | ❌ 未修复 | 仍使用 `Visibility.ANY` |
| MongoDB Starter AutoConfiguration 路径错误 | P0 | ❌ 未修复 | 仍在 `spring/` 目录下 |
| spring.factories 残留 | P0 | ❌ 未修复 | 3 个文件仍在 |
| GlobalExceptionHandler 误注册 | P1 | ❌ 未修复 | 仍在 `.imports` 中 |
| 测试覆盖率极低 | P1 | ❌ 未修复 | 6 个测试类，覆盖率约 0.26% |
| RabbitMqProperties 默认凭据 | P1 | ❌ 未修复 | 仍为 `guest` |
| AuditProperties 空密码默认 | P1 | ❌ 未修复 | 仍为 `""` |
| SimpleErrorCode 可变状态 | P3 | ❌ 未修复 | 被删除后重新添加，仍为 `@Data` |

### 1.3 问题分布总览

| 问题类型 | P0-Critical | P1-High | P2-Medium | P3-Low | 合计 |
|---------|-------------|---------|-----------|--------|------|
| 设计缺陷 | 0 | 2 | 1 | 0 | 3 |
| 功能缺失 | 1 | 1 | 0 | 0 | 2 |
| 性能优化 | 0 | 0 | 1 | 0 | 1 |
| 安全漏洞 | 2 | 1 | 1 | 0 | 4 |
| 技术债务 | 1 | 1 | 2 | 1 | 5 |
| **合计** | **4** | **6** | **5** | **1** | **16** |

### 1.4 组件特性利用概览

| 组件 | 当前使用深度 | 待利用关键特性数 | 优先级推荐 |
|------|-------------|-----------------|-----------|
| Spring Boot | 中等 | 3 | 高 |
| Jackson | 基础 | 2 | 高 |
| Redisson | 中等 | 3 | 高 |
| MyBatis-Plus | 中等 | 1 | 中 |
| MapStruct | 基础 | 2 | 中 |
| Caffeine | 中等 | 2 | 中 |
| Disruptor | 中等 | 1 | 低 |
| SkyWalking | 基础 | 1 | 低 |

### 1.5 今日重点关注
- **[TOP 1]** `FeignGlobalExceptionHandler` 被新增注册为 AutoConfiguration（P1，新增）— 与昨日 `GlobalExceptionHandler` 同类设计缺陷
- **[TOP 2]** `carlos-audit-bus` AutoConfiguration.imports 为空（P1，新增）— 审计自动配置完全失效
- **[TOP 3]** `EncryptConfig` INFO 级别日志输出 SM4 密钥和 IV（P2，新增）— 敏感密钥泄露到日志
- **[TOP 4]** 昨日 4 个 P0 安全问题全部未修复（反序列化 Visibility.ANY ×2、MongoDB 路径错误、spring.factories 残留）
- **[TOP 特性]** 建议 Redisson 启用 `RLocalCachedMap` 多级缓存替代手动 Caffeine+Redis 组合

---

## 二、设计缺陷详情

### 问题 1: FeignGlobalExceptionHandler 被误注册为 AutoConfiguration（新增）

**模块**: `carlos-spring-cloud-starter`  
**文件**: `carlos-spring-boot/carlos-spring-cloud-starter/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`  
**行号**: L7  
**严重程度**: P1-High  
**影响范围**: 模块级  

#### 相关代码

```java
// carlos-spring-boot/carlos-spring-cloud-starter/src/main/java/com/carlos/cloud/feign/FeignGlobalExceptionHandler.java
package com.carlos.cloud.feign;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class FeignGlobalExceptionHandler {
    // ...
}
```

```
# carlos-spring-boot/carlos-spring-cloud-starter/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
com.carlos.cloud.feign.FeignGlobalExceptionHandler
```

#### 详细分析

- **问题描述**: `FeignGlobalExceptionHandler` 是一个 `@RestControllerAdvice` 异常处理器，被列入了 `AutoConfiguration.imports` 文件中。这与昨日报告的 `GlobalExceptionHandler` 问题属于同一类设计缺陷。
- **违反规范**: AGENTS.md 中 "自动配置缺陷" 检查项；`@RestControllerAdvice` 类不应作为 AutoConfiguration 注册，应通过 `@Configuration` + `@Bean` + `@ConditionalOnProperty` 条件化注册。
- **触发条件**: 所有引入 `carlos-spring-cloud-starter` 的应用都会无条件加载此异常处理器。
- **潜在风险**: 异常处理器无法通过配置关闭；与 Spring Boot 自动配置语义不一致；框架灵活性受损。

#### 解决方案

**推荐修复**:

从 `AutoConfiguration.imports` 中移除 `FeignGlobalExceptionHandler`，改为在 `FeignConfig` 或其他 `@Configuration` 类中通过 `@Bean` + `@ConditionalOnProperty` 注册：

```java
@Configuration
public class FeignConfig {
    @Bean
    @ConditionalOnProperty(prefix = "carlos.cloud.feign.exception-handler", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FeignGlobalExceptionHandler feignGlobalExceptionHandler() {
        return new FeignGlobalExceptionHandler();
    }
}
```

**短期规避**: 无，当前功能正常但设计不优雅。

**长期建议**: 统一梳理所有 `@RestControllerAdvice` 类（`GlobalExceptionHandler`、`FeignGlobalExceptionHandler`、`Oauth2ExceptionHandler`），统一通过 `@Configuration` + `@Bean` + `@ConditionalOnProperty` 管理。

---

### 问题 2: GlobalExceptionHandler 仍被误注册为 AutoConfiguration（遗留未修复）

**模块**: `carlos-spring-boot-starter-web`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`  
**行号**: L14  
**严重程度**: P1-High  
**影响范围**: 模块级  

#### 相关代码

```
com.carlos.boot.GlobalExceptionHandler
```

#### 详细分析

- **问题描述**: 同问题 1，`GlobalExceptionHandler` 作为 `@RestControllerAdvice` 被注册为 AutoConfiguration，昨日已报告，今日仍未修复。
- **违反规范**: AGENTS.md 自动配置注册规范
- **触发条件**: 所有使用 starter-web 的应用都会加载此异常处理器
- **潜在风险**: 无法条件化关闭，影响框架灵活性

#### 解决方案

**推荐修复**:

从 `AutoConfiguration.imports` 中移除，改为在 `ApplicationWebMvcConfig` 中 `@Bean` 注册：

```java
@Configuration
public class ApplicationWebMvcConfig {
    @Bean
    @ConditionalOnProperty(prefix = "carlos.boot.web.exception-handler", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
```

---

### 问题 3: BatchServiceImpl 使用 @Autowired 字段注入

**模块**: `carlos-spring-boot-starter-mybatis`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-mybatis/src/main/java/com/carlos/datasource/base/BatchServiceImpl.java`  
**行号**: L35  
**严重程度**: P2-Medium  
**影响范围**: 类级  

#### 相关代码

```java
@Slf4j
public class BatchServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BatchService<T> {

    @Autowired
    private M baseMapper;

    // ...
}
```

#### 详细分析

- **问题描述**: `BatchServiceImpl` 使用 `@Autowired` 字段注入。Spring Boot 官方推荐构造器注入（或配合 Lombok 的 `@RequiredArgsConstructor`），字段注入会导致：测试时需反射注入依赖、隐藏类真实依赖、难以发现循环依赖。
- **违反规范**: Spring Boot 最佳实践 — 构造器注入优先
- **触发条件**: 任何测试或组件扫描场景
- **潜在风险**: 可测试性降低；依赖关系不透明；循环依赖难以发现

#### 解决方案

**推荐修复**:

```java
@Slf4j
@RequiredArgsConstructor
public class BatchServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BatchService<T> {

    // 使用构造器注入（ServiceImpl 已有 protected M baseMapper 字段，
    // 但 @Autowired 字段注入应移除，依赖父类提供的 getter）
}
```

或直接使用父类 `ServiceImpl` 提供的 `getBaseMapper()` 方法，无需额外字段。

---

## 三、功能未完善详情

### 问题 4: carlos-audit-bus AutoConfiguration.imports 为空（新增）

**模块**: `carlos-audit-bus`  
**文件**: `carlos-integration/carlos-audit/carlos-audit-bus/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`  
**严重程度**: P1-High  
**影响范围**: 模块级  

#### 相关代码

```
# 文件内容为空
```

```java
// carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/config/AuditAutoConfiguration.java
@Slf4j
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "carlos.audit", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuditAutoConfiguration {
    // ...
}
```

#### 详细分析

- **问题描述**: `carlos-audit-bus` 模块中存在 `AuditAutoConfiguration.java` 自动配置类，但对应的 `.imports` 文件内容为空。Spring Boot 3.x 只会加载 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 中声明的配置类，因此 `AuditAutoConfiguration` 永远不会被自动加载，审计相关的 HealthIndicator、Disruptor 配置等全部无法生效。
- **违反规范**: Spring Boot 3.x 自动配置加载规范
- **触发条件**: 任何引入 `carlos-audit-bus` 依赖的应用都无法自动启用审计自动配置
- **潜在风险**: 审计模块 ClickHouse 健康检查、审计监听器等 Starter 功能完全失效

#### 解决方案

**推荐修复**:

```
# carlos-integration/carlos-audit/carlos-audit-bus/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
com.carlos.audit.config.AuditAutoConfiguration
com.carlos.audit.config.AuditDisruptorConfig
```

**短期规避**: 用户需手动 `@Import(AuditAutoConfiguration.class)` 启用审计配置。

---

### 问题 5: MongoDB Starter AutoConfiguration.imports 路径错误（遗留未修复）

**模块**: `carlos-spring-boot-starter-mongodb`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-mongodb/src/main/resources/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`  
**严重程度**: P0-Critical  
**影响范围**: 模块级  

#### 详细分析

- **问题描述**: 文件路径为 `src/main/resources/spring/...` 而非 `src/main/resources/META-INF/spring/...`。Spring Boot 3.x 只在 `META-INF/spring/` 目录下扫描 `.imports` 文件，因此 `MongoAutoConfiguration` 永远不会被自动加载。
- **违反规范**: Spring Boot 3.x 自动配置加载规范
- **触发条件**: 任何引入 `carlos-spring-boot-starter-mongodb` 依赖的应用都无法自动配置 MongoDB
- **潜在风险**: MongoDB Starter 完全失效

#### 解决方案

```bash
mkdir -p carlos-spring-boot/carlos-spring-boot-starter-mongodb/src/main/resources/META-INF/spring
mv carlos-spring-boot/carlos-spring-boot-starter-mongodb/src/main/resources/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports \
   carlos-spring-boot/carlos-spring-boot-starter-mongodb/src/main/resources/META-INF/spring/
```

---

### 问题 6: 测试覆盖率极低（遗留未修复）

**模块**: 全框架  
**严重程度**: P1-High  
**影响范围**: 框架级  

#### 详细分析

- **问题描述**: 全框架 2312 个 Java 文件，仅有 6 个测试类：
  - `RestClientBuilderUtilsTest` (starter-integration)
  - `SM4UtilTest` (starter-encrypt)
  - `MigrationServiceTest` (starter-migration)
  - `PasswordUtilTest` (carlos-test)
  - `EncryptUtilTest` (carlos-test)
  - `GitlabServiceTest` (carlos-tools，位于 main 源码目录)
  
  覆盖率不足 0.26%。
- **违反规范**: AGENTS.md "测试驱动 — RED → GREEN → REFACTOR"
- **触发条件**: 任何代码变更都缺乏自动化回归验证
- **潜在风险**: 代码质量无法保障；重构时缺乏安全网；昨日虚拟线程 Pinning 修复和 Guava→Caffeine 迁移均无任何自动化测试覆盖

#### 解决方案

**推荐修复**:

1. 为核心模块建立测试基线：
   - `carlos-spring-boot-core`: Result、ErrorCode、BaseEnum、分页工具单元测试
   - `carlos-spring-boot-starter-web`: GlobalExceptionHandler 异常映射测试
   - `carlos-spring-boot-starter-security`: 权限评估器、缓存同步测试
   - `carlos-spring-boot-starter-mybatis`: BaseService CRUD + Join 测试

2. 在 `carlos-parent/pom.xml` 中配置测试策略：
```xml
<maven-surefire-plugin>
    <configuration>
        <skipTests>false</skipTests>
        <includes>
            <include>**/*Test.java</include>
        </includes>
    </configuration>
</maven-surefire-plugin>
```

---

## 四、代码可优化点详情

### 问题 7: EncryptConfig INFO 级别日志输出 SM4 密钥和 IV（新增）

**模块**: `carlos-spring-boot-starter-encrypt`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-encrypt/src/main/java/com/carlos/encrypt/config/EncryptConfig.java`  
**行号**: L57-L58  
**严重程度**: P2-Medium  
**影响范围**: 模块级  

#### 相关代码

```java
@Bean
@ConditionalOnProperty(prefix = "carlos.encrypt.sm4", name = "enabled", havingValue = "true", matchIfMissing = true)
public SM4 sm4(EncryptProperties properties) {
    EncryptProperties.SM4Properties sm4Config = properties.getSm4();
    String key = sm4Config.getKey();
    EncryptMode encryptMode = sm4Config.getEncryptMode();
    SM4 sm4 = null;
    if (key.length() != 16) {
        key = DigestUtil.md5Hex16(key);
    }
    if (encryptMode == EncryptMode.CBC) {
        String iv = sm4Config.getIv();
        if (StrUtil.isBlank(iv)) {
            iv = DigestUtil.md5Hex16(key.substring(0, 16));
        }
        log.info("SM4 key:{}", key);   // L57
        log.info("SM4 iv:{}", iv);     // L58
        sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
    }
    // ...
}
```

#### 详细分析

- **问题描述**: `EncryptConfig` 在初始化 SM4 Bean 时，使用 `log.info()` 打印了完整的 SM4 密钥和 IV。虽然这仅在应用启动时输出一次，但密钥属于高度敏感信息，一旦日志被收集到 ELK/Splunk 等日志系统，或在生产环境日志文件中留存，将导致密钥泄露。
- **违反规范**: AGENTS.md "无硬编码凭据" + "错误消息不泄露敏感数据"；安全红线：敏感配置不得输出到日志
- **触发条件**: 应用启动时，当 `carlos.encrypt.sm4.enabled=true` 时触发
- **潜在风险**: SM4 对称密钥泄露，攻击者获取日志即可解密所有使用该密钥加密的数据；合规审计不通过（等保/密评要求密钥不得明文输出）

#### 解决方案

**推荐修复**:

1. 完全移除密钥日志输出，或仅在 DEBUG 级别输出脱敏后的密钥信息：

```java
if (encryptMode == EncryptMode.CBC) {
    String iv = sm4Config.getIv();
    if (StrUtil.isBlank(iv)) {
        iv = DigestUtil.md5Hex16(key.substring(0, 16));
    }
    if (log.isDebugEnabled()) {
        log.debug("SM4 initialized with mode={}, keyLength={}, ivLength={}", 
                  encryptMode, key.length(), iv.length());
    }
    sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
}
```

2. 若必须记录密钥指纹用于排障，仅记录哈希值：

```java
log.info("SM4 initialized, key fingerprint={}", DigestUtil.sha256Hex(key).substring(0, 16));
```

**短期规避**: 在 `logback-spring.xml` 中为 `com.carlos.encrypt.config` 包配置 `ERROR` 级别过滤，阻断所有日志输出。

**长期建议**: 建立框架级日志脱敏规范，所有涉及密钥、Token、密码的配置类不得输出实际值。

---

### 问题 8: FlowableProcessService.deploy() 无文件类型校验

**模块**: `carlos-spring-boot-starter-flowable`  
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-flowable/src/main/java/com/carlos/flowable/service/FlowableProcessService.java`  
**行号**: L39-L51  
**严重程度**: P2-Medium  
**影响范围**: 类级  

#### 相关代码

```java
public String deploy(MultipartFile file, String name, String category, String description) {
    try (InputStream inputStream = file.getInputStream()) {
        Deployment deployment = repositoryService.createDeployment()
            .name(name)
            .category(category)
            .addInputStream(file.getOriginalFilename(), inputStream)
            .deploy();
        log.info("流程部署成功，部署ID：{}，名称：{}", deployment.getId(), name);
        return deployment.getId();
    } catch (IOException e) {
        throw new FlowableException("流程部署失败", e);
    }
}
```

#### 详细分析

- **问题描述**: 流程部署方法直接接受任意 `MultipartFile`，未校验文件扩展名（应为 `.bpmn20.xml` 或 `.bpmn`）、文件大小、内容类型。攻击者可能上传恶意构造的文件触发解析漏洞（如 XML 外部实体注入 XXE）或耗尽服务器资源。
- **违反规范**: AGENTS.md "文件上传是否限制类型和大小"
- **触发条件**: 调用 `/deploy` 接口上传任意文件
- **潜在风险**: XXE 攻击（若 Flowable XML 解析器配置不当）；大文件导致内存溢出；路径遍历（通过 `file.getOriginalFilename()`）

#### 解决方案

**推荐修复**:

```java
private static final Set<String> ALLOWED_EXTENSIONS = Set.of("bpmn", "bpmn20.xml", "zip");
private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

public String deploy(MultipartFile file, String name, String category, String description) {
    // 文件大小校验
    if (file.getSize() > MAX_FILE_SIZE) {
        throw BusinessException.of(CommonErrorCode.FILE_TOO_LARGE, "流程文件大小不能超过 10MB");
    }
    
    // 文件类型白名单校验
    String originalFilename = file.getOriginalFilename();
    if (StrUtil.isBlank(originalFilename) || 
        ALLOWED_EXTENSIONS.stream().noneMatch(originalFilename::endsWith)) {
        throw BusinessException.of(CommonErrorCode.FILE_TYPE_NOT_ALLOWED, 
            "仅支持 .bpmn, .bpmn20.xml, .zip 格式的流程文件");
    }
    
    // 防止路径遍历
    String safeFilename = Paths.get(originalFilename).getFileName().toString();
    
    try (InputStream inputStream = file.getInputStream()) {
        Deployment deployment = repositoryService.createDeployment()
            .name(name)
            .category(category)
            .addInputStream(safeFilename, inputStream)
            .deploy();
        log.info("流程部署成功，部署ID：{}，名称：{}", deployment.getId(), name);
        return deployment.getId();
    } catch (IOException e) {
        throw new FlowableException("流程部署失败", e);
    }
}
```

---

## 五、安全漏洞详情

### 问题 9: JacksonSerializer ObjectMapper Visibility.ANY 反序列化风险（遗留未修复）

**模块**: `carlos-spring-boot-starter-redis-core`  
**文件**: `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis-core/src/main/java/com/carlos/redis/serialize/JacksonSerializer.java`  
**行号**: L64  
**严重程度**: P0-Critical  
**影响范围**: 模块级（Redis 序列化层）  
**CWE**: CWE-502: Deserialization of Untrusted Data  

#### 相关代码

```java
private ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    // ...
    // 设置所有属性可见
    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // ...
    // 配置了 PolymorphicTypeValidator 白名单
    PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
        .allowIfBaseType(Object.class)
        .allowIfSubType("com.carlos")
        .allowIfSubType("java.util")
        .allowIfSubType("java.lang")
        .allowIfSubType("java.time")
        .allowIfSubType("java.math")
        .build();
    mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
    return mapper;
}
```

#### 详细分析

- **问题描述**: `Visibility.ANY` 允许 Jackson 序列化/反序列化所有字段（包括 private），即使它们没有 getter/setter。虽然配置了 `PolymorphicTypeValidator` 白名单，但 `Visibility.ANY` 扩大了攻击面，可能绕过某些安全控制。结合 `activateDefaultTyping`，攻击者如果能找到白名单内的恶意类（如 `java.util.HashSet` 等常用类可能存在的 gadget chain），仍存在反序列化 RCE 风险。
- **违反规范**: AGENTS.md "ObjectMapper Visibility 配置是否过宽（ANY → 应 PUBLIC_ONLY）"
- **触发条件**: 攻击者向 Redis 注入恶意序列化数据，应用读取时触发反序列化
- **潜在风险**: 反序列化远程代码执行（RCE）、数据篡改

#### 解决方案

**推荐修复**:

```java
private ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // 仅公开属性可见，配合 @JsonProperty 显式标记
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
    mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    // ... 其余配置不变
}
```

**短期规避**: 确认 `allowIfBaseType(Object.class)` 的范围是否过大，考虑缩小为更具体的基类。

**参考文档**:
- [Jackson Deserialization Security](https://cowtowncoder.medium.com/on-jackson-cves-dont-panic-here-is-what-you-need-to-know-54b0d4d8e5a)
- CWE-502

---

### 问题 10: RedisOAuth2AuthorizationService ObjectMapper Visibility.ANY（遗留未修复）

**模块**: `carlos-auth-service`  
**文件**: `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/repository/RedisOAuth2AuthorizationService.java`  
**行号**: L98  
**严重程度**: P0-Critical  
**影响范围**: 模块级（OAuth2 授权数据）  
**CWE**: CWE-502  

#### 相关代码

```java
@PostConstruct
private void configureObjectMapper() {
    // 配置可见性
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // 注册 JavaTimeModule 支持 JDK8 日期时间
    objectMapper.registerModule(new JavaTimeModule());
    // 注册 Spring Security Jackson2 Modules
    ClassLoader classLoader = getClass().getClassLoader();
    objectMapper.registerModules(SecurityJackson2Modules.getModules(classLoader));
    // 注册 OAuth2 Authorization Server Jackson2 Module
    objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    // 添加 AuthorizationGrantType 混合注解处理
    objectMapper.addMixIn(AuthorizationGrantType.class, AuthorizationGrantTypeMixin.class);
    log.info("RedisOAuth2AuthorizationService initialized with SecurityJackson2Modules");
}
```

#### 详细分析

- **问题描述**: OAuth2 授权数据（包含 Access Token、Refresh Token、用户信息等敏感数据）使用 `Visibility.ANY` 进行序列化存储到 Redis。这不仅存在反序列化安全风险，还可能导致敏感字段被意外序列化到 Redis。
- **违反规范**: AGENTS.md "Redis 中是否存储敏感 Token 明文"、"ObjectMapper Visibility 配置是否过宽"
- **触发条件**: OAuth2 授权流程中，授权信息被序列化存储到 Redis
- **潜在风险**: 敏感数据泄露到 Redis；反序列化安全漏洞

#### 解决方案

**推荐修复**:

```java
@PostConstruct
private void configureObjectMapper() {
    // 仅公开属性可见
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    // 或更严格：字段默认不可见，仅通过 @JsonProperty 标记
    // objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
    // ... 其余配置不变
}
```

---

### 问题 11: RabbitMQ 和 ClickHouse 密码硬编码默认值（遗留未修复）

**模块**: `carlos-spring-boot-starter-mq` / `carlos-audit-bus`  
**文件**:
- `carlos-spring-boot/carlos-spring-boot-starter-mq/src/main/java/com/carlos/mq/config/RabbitMqProperties.java` L36-L41
- `carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/config/AuditProperties.java` L102  
**严重程度**: P1-High  
**影响范围**: 模块级  
**CWE**: CWE-798: Use of Hard-coded Credentials  

#### 相关代码

```java
// RabbitMqProperties.java
private String username = "guest";
private String password = "guest";

// AuditProperties (推断)
private String password = "";
```

#### 详细分析

- **问题描述**: `@ConfigurationProperties` 默认值中硬编码了敏感凭据。虽然 Spring Boot 配置可以覆盖，但默认值在生产环境中可能被遗忘修改。
- **违反规范**: AGENTS.md "无硬编码凭据" 安全红线
- **触发条件**: 用户未在 `application.yml` 中显式配置 RabbitMQ/ClickHouse 密码时，使用默认凭据连接
- **潜在风险**: 使用默认凭据连接生产环境，安全风险极高

#### 解决方案

**推荐修复**:

```java
// RabbitMqProperties.java
private String username;
private String password;

// 移除默认值，在配置类中校验非空
@PostConstruct
public void validate() {
    Assert.hasText(username, "RabbitMQ username must be configured");
    Assert.hasText(password, "RabbitMQ password must be configured");
}
```

---

## 六、依赖与技术债务

### 6.1 CVE 安全通告

| CVE 编号 | 影响组件 | 框架版本 | 风险等级 | 说明 |
|---------|---------|---------|---------|------|
| CVE-2026-22733 | Spring Boot | 3.5.9 | 高危 | 当应用将需要身份认证的业务端点错误地映射到 CloudFoundry Actuator 路径下时，Actuator 与 Spring Security 的路径处理机制存在冲突，可能导致访问控制绕过。框架未使用 CloudFoundry，风险较低，但需关注后续补丁。 |

### 6.2 spring.factories 残留（Spring Boot 3.x 废弃机制）（遗留未修复）

| 文件 | 状态 | 说明 |
|----|------|------|
| `carlos-spring-boot-starter-apm/src/main/resources/META-INF/spring.factories` | ❌ 残留 | 应迁移至 `.imports` |
| `carlos-spring-boot-starter-migration/src/main/resources/META-INF/spring.factories` | ❌ 残留 | 应迁移至 `.imports` |
| `carlos-message-core/src/main/resources/META-INF/spring.factories` | ❌ 残留 | 应迁移至 `.imports` |

Spring Boot 3.x 已废弃 `spring.factories` 用于自动配置注册，应统一使用 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`。

### 6.3 已弃用 API 与迁移残留

| 文件 | 行号 | 已弃用 API / 问题 | 替代方案 |
|----|----|---------|------|
| `JacksonSerializer.java` | L64 | `Visibility.ANY` | `Visibility.PUBLIC_ONLY` |
| `RedisOAuth2AuthorizationService.java` | L98 | `Visibility.ANY` | `Visibility.PUBLIC_ONLY` |
| `GitlabServiceTest.java` | L26,36 | `throw new RuntimeException(e)` | `throw new BusinessException(e)` |
| `RabbitMqProperties.java` | L36-L41 | 硬编码默认凭据 `guest` | 无默认值 + `@PostConstruct` 校验 |
| `EncryptConfig.java` | L57-L58 | `log.info()` 输出 SM4 密钥 | 移除或改为 DEBUG + 脱敏 |

### 6.4 javax.* 包分析

经扫描，项目中的 `javax.*` 导入均为 JDK 标准 API（`javax.net.ssl.*`、`javax.sql.DataSource`、`javax.crypto.*`、`javax.xml.parsers.*`、`javax.security.auth.x500.*`），**不属于 Jakarta EE 迁移范围**，无需处理。

---

## 七、组件特性利用分析（⭐ 核心章节）

> 本章节分析框架已依赖的技术组件中，哪些高级/新特性尚未被充分利用，并提出具体的应用建议。

### 7.1 高优先级组件

#### 组件 1: Jackson (当前版本: 2.x via Spring Boot 3.5.9)

**当前项目使用概况**
- 使用位置: Redis 序列化、OAuth2 授权数据序列化、JSON 工具、Spring MVC 消息转换
- 当前用法摘要: 手动创建 ObjectMapper，配置 Visibility.ANY、PolymorphicTypeValidator 白名单、JavaTimeModule；Spring Boot 默认 Jackson 配置

**未被充分利用的特性**

| 特性名称 | 当前状态 | 推荐应用场景 | 预期收益 | 实施难度 |
|------|------|--------|------|------|
| `Visibility.PUBLIC_ONLY` | 使用 ANY | Redis/OAuth2 序列化 | 安全增强 | 低 |
| `PropertyNamingStrategies.SNAKE_CASE` | 未使用 | 全局下划线命名序列化 | API 一致性 | 低 |
| `@JsonFilter` 动态过滤 | 未使用 | 审计日志脱敏 | 安全增强 | 中 |

**具体应用建议**

**建议 1: 将 `Visibility.ANY` 降级为 `PUBLIC_ONLY`（安全优先）**

- **场景**: `JacksonSerializer`（Redis 序列化）和 `RedisOAuth2AuthorizationService`（OAuth2 授权数据序列化）
- **代码示例**:
```java
mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
```
- **收益**: 缩小反序列化攻击面，降低 CWE-502 风险

---

#### 组件 2: Redisson (当前版本: 3.51.0)

**当前项目使用概况**
- 使用位置: Redis 缓存、分布式锁、限流、多级缓存（手动 Caffeine+Redis 组合）
- 当前用法摘要: RedissonClient 基础操作，RLock 分布式锁，RBucket 字符串缓存

**未被充分利用的特性**

| 特性名称 | 当前状态 | 推荐应用场景 | 预期收益 | 实施难度 |
|------|------|--------|------|------|
| `RLocalCachedMap` 内置多级缓存 | 手动实现 | `carlos-org` 部门缓存、`carlos-system` 字典缓存 | 简化代码 + 一致性自动维护 | 中 |
| `RDelayedQueue` 延时队列 | 未使用 | `carlos-mq` 延时消息替代方案 | 减少中间件依赖 | 中 |
| `RTopic` 发布订阅 | 未使用 | 权限缓存同步替代 Redis Pub/Sub | 功能增强 | 低 |
| `RLock` 看门狗自动续期 | 部分使用 | 分布式锁超时优化 | 可靠性提升 | 低 |

**具体应用建议**

**建议 1: `RLocalCachedMap` 替代手动多级缓存**

- **场景**: `carlos-org` 的 `OrgDepartmentManagerImpl` 五层 Redis 缓存结构可大幅简化
- **代码示例**:
```java
LocalCachedMapOptions<String, Department> options = LocalCachedMapOptions.<String, Department>defaults()
    .evictionPolicy(EvictionPolicy.LRU)
    .cacheSize(1000)
    .timeToLive(10, TimeUnit.MINUTES)
    .maxIdle(5, TimeUnit.MINUTES)
    .storeMode(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS);
RLocalCachedMap<String, Department> deptCache = redisson.getLocalCachedMap("dept:cache", options);
```
- **收益**: 自动处理本地-远程缓存一致性，减少约 200 行缓存管理代码；避免缓存穿透和雪崩

**建议 2: `RTopic` 替代 Redis Pub/Sub 实现权限缓存同步**

- **场景**: `PermissionCacheSyncManager` 当前使用 Redis String 的 Pub/Sub 实现缓存同步
- **代码示例**:
```java
RTopic<String> topic = redisson.getTopic("cache:permission:sync");
topic.addListener(String.class, (channel, message) -> permissionCache.invalidate(message));
```
- **收益**: Redisson 提供可靠的消息投递保证，支持消息持久化和重试

---

#### 组件 3: Spring Boot 3.5.9

**当前项目使用概况**
- 使用位置: 全框架基础设施
- 当前用法摘要: 已启用虚拟线程（`VirtualThreadConfig`、`TomcatVirtualThreadConfig`），已使用 `RestClient` 和 `@HttpExchange`（`carlos-spring-boot-starter-integration`）

**未被充分利用的特性**

| 特性名称 | 当前状态 | 推荐应用场景 | 预期收益 | 实施难度 |
|------|------|--------|------|------|
| `ProblemDetail` (RFC 7807) | 未使用 | 全局异常响应标准化 | API 规范统一 | 中 |
| Structured Logging (JSON) | 未使用 | 日志聚合系统对接 | 可观测性增强 | 低 |
| `@HttpExchange` HTTP Interface | ✅ 已使用 | `DingtalkApiClient` 等对接模块 | 代码简化 | 已使用 |
| `RestClient` | ✅ 已使用 | `RestClientBuilderUtils` | 同步 HTTP 调用 | 已使用 |

**具体应用建议**

**建议 1: 引入 `ProblemDetail` 标准化错误响应**

- **场景**: `GlobalExceptionHandler`、`GatewayExceptionHandler`、`Oauth2ExceptionHandler` 均使用自定义的错误响应格式，可以逐步迁移到 Spring Boot 3 原生的 `ProblemDetail`（RFC 7807）规范
- **代码示例**:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getHttpStatus(), ex.getMessage());
        problemDetail.setTitle("Business Error");
        problemDetail.setProperty("errorCode", ex.getErrorCode());
        problemDetail.setProperty("timestamp", System.currentTimeMillis());
        return problemDetail;
    }
}
```
- **收益**: 符合 RFC 7807 标准，与 Spring Security OAuth2 错误响应格式统一，便于前端统一处理

**建议 2: 启用结构化日志（JSON 格式）**

- **场景**: `carlos-spring-boot-starter-apm` 的 logback 配置
- **代码示例**:
在 `logback-skywalking.xml` 中添加 JSON 输出 Appender：
```xml
<appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
    <encoder class="net.logstash.logback.encoder.LogstashEncoder" />
</appender>
```
- **收益**: 直接输出 JSON 格式日志，无需 ELK 端解析，提升日志检索效率

---

### 7.2 中优先级组件

#### 组件 4: MyBatis-Plus (当前版本: 3.5.15)

**当前项目使用概况**
- 使用位置: 数据层 CRUD、分页、Join 查询
- 当前用法摘要: BaseServiceImpl、mybatis-plus-join、分页插件、TenantLineInnerInterceptor

**未被充分利用的特性**

| 特性名称 | 当前状态 | 推荐应用场景 | 预期收益 | 实施难度 |
|------|------|--------|------|------|
| `TenantLineInnerInterceptor` | ✅ 已使用 | `carlos-datacenter` 多租户 | 减少手动租户过滤 | 已使用 |
| Lambda 链式查询 `Wrappers.lambdaQuery()` | 部分使用 | 所有 Manager 层 | 类型安全 | 低 |
| Active Record 模式 | 未使用 | 简单 CRUD 场景 | 代码简化 | 低 |

**具体应用建议**

**建议 1: 推广 Lambda 链式查询到所有 Manager 层**

- **场景**: 当前部分代码仍使用字符串字段名的 `QueryWrapper`，存在拼写错误风险且无法类型检查
- **代码示例**:
```java
// 替换前
QueryWrapper<User> wrapper = new QueryWrapper<>();
wrapper.eq("user_name", name).like("email", email);

// 替换后
LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
wrapper.eq(User::getUserName, name).like(User::getEmail, email);
```
- **收益**: 编译期字段名检查，IDE 重构支持，零运行时风险

---

#### 组件 5: MapStruct (当前版本: 1.6.3)

**当前项目使用概况**
- 使用位置: DTO/Entity/VO 转换
- 当前用法摘要: 基础 `@Mapper` 注解使用，部分使用 `componentModel = "spring"`

**未被充分利用的特性**

| 特性名称 | 当前状态 | 推荐应用场景 | 预期收益 | 实施难度 |
|------|------|--------|------|------|
| 构造函数映射 | 未使用 | 不可变 DTO | 配合不可变性原则 | 低 |
| `@BeforeMapping` / `@AfterMapping` | 未使用 | 转换前后日志/校验 | 功能增强 | 低 |
| `@BeanMapping(ignoreByDefault = true)` | 未使用 | 精确控制字段映射 | 减少遗漏字段 | 低 |

**具体应用建议**

**建议 1: 启用构造函数映射实现不可变 DTO**

- **场景**: `SimpleErrorCode` 等 Value Object 应不可变
- **代码示例**:
```java
@Mapper(componentModel = "spring")
public interface ErrorCodeMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "code", source = "code")
    @Mapping(target = "message", source = "message")
    SimpleErrorCode toSimpleErrorCode(String code, String message);
}
```
- **收益**: 生成的转换代码自动使用全参构造器，无需 setter，实现不可变性

---

#### 组件 6: Caffeine (当前版本: 3.x)

**当前项目使用概况**
- 使用位置: `CachedPermissionProvider`、`SysDictCacheManager`（已迁移）、`MultiLevelCacheUtil`
- 当前用法摘要: 基础 `CacheBuilder`，TTL 过期策略

**未被充分利用的特性**

| 特性名称 | 当前状态 | 推荐应用场景 | 预期收益 | 实施难度 |
|------|------|--------|------|------|
| W-TinyLFU 淘汰策略 | 未显式配置 | 所有缓存 | 命中率提升 | 低 |
| `recordStats()` 统计信息 | 未使用 | 缓存调优 | 可观测性 | 低 |
| `removalListener` | 未使用 | 缓存变更追踪 | 调试/审计 | 低 |
| 异步加载 `AsyncLoadingCache` | 未使用 | 字典/配置缓存 | 性能提升 | 中 |

**具体应用建议**

**建议 1: 为所有 Caffeine Cache 启用统计信息**

- **场景**: `CachedPermissionProvider`、`SysDictCacheManager`
- **代码示例**:
```java
Caffeine.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .recordStats()  // 启用统计
    .removalListener((key, value, cause) -> log.debug("Cache removal: key={}, cause={}", key, cause))
    .build();
```
- **收益**: 通过 `cache.stats()` 获取命中率、加载时间等指标，指导缓存参数调优

---

### 7.3 已充分利用组件（表扬）

| 组件 | 当前使用深度 | 说明 |
|------|-------------|------|
| Spring HTTP Interface | 中等 | `DingtalkApiClient` 使用 `@HttpExchange`，`RestClientBuilderUtils` 提供工厂方法 |
| RestClient | 中等 | `carlos-spring-boot-starter-integration` 测试和主代码均使用 |
| MyBatis-Plus TenantLineInnerInterceptor | 已启用 | `MyBatisPlusConfig` 已配置租户拦截器 |
| Disruptor | 中等 | `carlos-audit-bus` 使用 Disruptor 批量处理审计日志 |

---

## 八、模块健康度评分

| 模块 | 设计规范 | 功能完整 | 代码质量 | 安全性 | 组件利用 | 综合评分 |
|------|---------|---------|---------|--------|---------|---------|
| carlos-spring-boot-core | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐☆ | 4.8/5.0 |
| carlos-spring-boot-starter-web | ⭐⭐⭐☆☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | 3.6/5.0 |
| carlos-spring-boot-starter-redis | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐☆☆☆ | ⭐⭐⭐☆☆ | 3.4/5.0 |
| carlos-spring-boot-starter-security | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | 4.4/5.0 |
| carlos-spring-boot-starter-encrypt | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | ⭐⭐☆☆☆ | ⭐⭐⭐☆☆ | 3.0/5.0 |
| carlos-spring-cloud-starter | ⭐⭐⭐☆☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | 3.6/5.0 |
| carlos-spring-boot-starter-mongodb | ⭐⭐☆☆☆ | ⭐⭐☆☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | 2.4/5.0 |
| carlos-spring-boot-starter-flowable | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | 3.0/5.0 |
| carlos-audit-bus | ⭐⭐⭐☆☆ | ⭐⭐☆☆☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | 2.8/5.0 |
| carlos-auth-service | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐☆☆☆ | ⭐⭐⭐☆☆ | 3.2/5.0 |
| carlos-gateway | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | 4.0/5.0 |
| carlos-integration (整体) | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐⭐☆ | 3.2/5.0 |
| carlos-samples | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | 3.2/5.0 |
| carlos-test | ⭐⭐⭐☆☆ | ⭐⭐☆☆☆ | ⭐⭐☆☆☆ | ⭐⭐⭐⭐☆ | ⭐⭐☆☆☆ | 2.4/5.0 |

> 评分标准：5星=优秀，4星=良好，3星=一般，2星=较差，1星=需立即整改  
> `carlos-spring-boot-starter-mongodb` 和 `carlos-audit-bus` 因 AutoConfiguration 注册问题拉低功能完整评分  
> `carlos-spring-boot-starter-encrypt` 因日志泄露密钥拉低安全性评分  
> `carlos-spring-boot-starter-redis` 因 Jackson Visibility.ANY 拉低安全性评分

---

## 九、待办事项与修复计划

### 9.1 今日新增待办

- [ ] **[P1]** `carlos-spring-cloud-starter`: 将 `FeignGlobalExceptionHandler` 从 `AutoConfiguration.imports` 移除，改为 `@Configuration` + `@Bean` + `@ConditionalOnProperty` 注册
- [ ] **[P1]** `carlos-audit-bus`: 在 `AutoConfiguration.imports` 中补充 `com.carlos.audit.config.AuditAutoConfiguration` 和 `AuditDisruptorConfig`
- [ ] **[P2]** `carlos-spring-boot-starter-encrypt`: 移除 `EncryptConfig` 中 `log.info()` 输出 SM4 密钥/IV 的代码，改为 DEBUG 级别或输出指纹哈希
- [ ] **[P2]** `carlos-spring-boot-starter-mybatis`: 将 `BatchServiceImpl` 的 `@Autowired` 字段注入改为构造器注入
- [ ] **[P2]** `carlos-spring-boot-starter-flowable`: 为 `FlowableProcessService.deploy()` 增加文件类型白名单和大小限制校验
- [ ] **[P3]** `carlos-integration/carlos-tools`: 将 `GitlabServiceTest` 的 `RuntimeException` 改为 `BusinessException`

### 9.2 历史遗留跟踪

- [ ] **[P0]** `carlos-spring-boot-starter-redis-core`: `JacksonSerializer` `Visibility.ANY` → `PUBLIC_ONLY` ⏳ 遗留未修复
- [ ] **[P0]** `carlos-auth-service`: `RedisOAuth2AuthorizationService` `Visibility.ANY` → `PUBLIC_ONLY` ⏳ 遗留未修复
- [ ] **[P0]** `carlos-spring-boot-starter-mongodb`: 修复 `AutoConfiguration.imports` 路径到 `META-INF/spring/` ⏳ 遗留未修复
- [ ] **[P0]** `carlos-spring-boot-starter-apm` / `carlos-spring-boot-starter-migration` / `carlos-message-core`: 迁移 `spring.factories` 到 `.imports` ⏳ 遗留未修复
- [ ] **[P1]** `carlos-spring-boot-starter-web`: 将 `GlobalExceptionHandler` 从 `AutoConfiguration.imports` 移除 ⏳ 遗留未修复
- [ ] **[P1]** `carlos-spring-boot-starter-mq`: `RabbitMqProperties` 移除默认凭据 `guest` ⏳ 遗留未修复
- [ ] **[P1]** `carlos-audit-bus`: `AuditProperties` 移除空密码默认值 ⏳ 遗留未修复
- [ ] **[P1]** 全框架: 测试覆盖率提升至 10%（基线目标） ⏳ 遗留未修复
- [ ] **[P3]** `carlos-spring-boot-core`: `SimpleErrorCode` 将 `@Data` 改为 `@Getter` + `final` 字段 ⏳ 遗留未修复（被删除后重新添加，仍为可变状态）

### 9.3 本周已修复问题（表扬）

- ✅ **[P1]** `carlos-system-bus`: `SysDictCacheManager` Guava Cache → Caffeine 替换（176283c9）
- ✅ **[P1]** `carlos-system-bus`: `SysConfigServiceImpl` Guava Cache → Caffeine 替换（176283c9）
- ✅ **[P1]** 11 个文件: `synchronized` → `ReentrantLock` 修复 JDK 21 虚拟线程 Pinning 风险（945c1a58）
  - 涉及: `EncryptUtil`, `RedisUtil`, `ClickHouseBatchWriter`, `PathMatchUtil`, `MultiLevelCacheUtil`, `RateLimitUtil`, `JsonFactory`, `Postal`, `WoCloud`, `CustomLicenseManager`, `SelectRoutePredicateFactory`
- ✅ `carlos-auth-service`: `Oauth2ExceptionHandler` 优化（2d0aab98, d4308143）
- ✅ `carlos-spring-boot-starter-web`: `GlobalExceptionHandler` 优化（2d0aab98, d4308143）

---

## 十、技术趋势洞察

### 10.1 关键依赖版本动态

| 组件 | 框架版本 | 最新版本 | 发布日期 | 升级建议 |
|------|---------|---------|---------|---------|
| Spring Boot | 3.5.9 | 3.5.9 | 2026-05 | 当前最新，保持 |
| Spring Cloud | 2025.0.1 | 2025.0.1 | 2026-04 | 当前最新，保持 |
| MyBatis-Plus | 3.5.15 | 3.5.15 | 2025-11 | 当前最新，保持 |
| Redisson | 3.51.0 | 3.51.0 | 2025-11 | 当前最新，保持 |
| Hutool | 5.8.40 | 5.8.40 | 2025-11 | 当前最新，保持 |

### 10.2 安全通告

- **CVE-2026-22733**: Spring Boot 认证绕过漏洞（CloudFoundry Actuator 路径冲突）。框架未直接依赖 CloudFoundry，但建议关注 Spring Boot 3.5.10 补丁版本。若应用部署在 CloudFoundry 环境，应立即评估影响。

---

*本报告由 OpenClaw 定时任务自动生成，问题定位基于静态代码分析，具体修复前请人工复核。*
