# Carlos Framework 每日代码质量与优化报告

**生成时间**: 2026-05-17 09:00
**扫描 Commit**: `5584f5a8dd746c9f70505eb1b8aad0126b64897f`
**扫描范围**: 全量模块（2312 个 Java 文件，142 个 XML 文件，95 个 Vue 文件，139 个 TypeScript 文件）
**发现问题总数**: 18 个（P0: 4 个, P1: 8 个, P2: 5 个, P3: 1 个）
**组件特性利用建议**: 10 条

---

## 一、执行摘要

### 1.1 今日变更概览

- **Git 拉取状态**: 成功（有更新）
- **Commit**: `5584f5a8` (chore(deps): 更新依赖版本并清理注释配置)
- **基准代码 Commit**: `93be3acb` (docs: 每日代码质量与优化报告 2026-05-15-0900)
- **变更文件数**: 11 个
- **新增代码**: +37 行 / 删除代码: -2,250 行
- **主要变更**:
  - `carlos-dependencies/pom.xml` — MyBatis-Plus 3.5.15 → 3.5.16，fastjson 2.0.60 → 2.0.61，清理注释掉的旧版本配置
  - 删除 9 份旧 `daily-optimization-plan` 报告文件（2026-04-04 至 2026-04-30）
  - 新增 `daily-scan-report-2026-05-14-1649.md`

### 1.2 昨日问题修复状态

| 昨日问题 | 级别 | 状态 | 说明 |
|---------|------|------|------|
| JacksonSerializer Visibility.ANY | P0 | ❌ 未修复 | 仍使用 `Visibility.ANY` |
| RedisOAuth2AuthorizationService Visibility.ANY | P0 | ❌ 未修复 | 仍使用 `Visibility.ANY` |
| MongoDB Starter AutoConfiguration 路径错误 | P0 | ❌ 未修复 | 仍在 `spring/` 目录下 |
| spring.factories 残留 | P0 | ❌ 未修复 | 3 个文件仍在 |
| GlobalExceptionHandler 误注册 | P1 | ❌ 未修复 | 仍在 `.imports` 中 |
| FeignGlobalExceptionHandler 误注册 | P1 | ❌ 未修复 | 仍在 `.imports` 中 |
| Audit bus AutoConfiguration.imports 为空 | P1 | ❌ 未修复 | 文件仍为空，AuditAutoConfiguration 未注册 |
| RabbitMqProperties 默认凭据 | P1 | ❌ 未修复 | 仍为 `guest` |
| AuditProperties 空密码默认 | P1 | ❌ 未修复 | 仍为 `""` |
| 测试覆盖率极低 | P1 | ❌ 未修复 | 仍为 6 个测试类 |
| EncryptConfig 日志泄露 SM4 密钥 | P2 | ❌ 未修复 | `log.info()` 仍在输出密钥 |
| BatchServiceImpl @Autowired 字段注入 | P2 | ❌ 未修复 | 仍为字段注入 |
| FlowableProcessService 文件类型校验缺失 | P2 | ❌ 未修复 | 未扫描到新变更 |
| SimpleErrorCode 可变状态 | P3 | ❌ 未修复 | 仍为 `@Data` |

> ⚠️ **警示**: 昨日报告的全部 16 个问题今日无一修复，已持续累积 3 天（自 2026-05-14 首次报告）。

### 1.3 问题分布总览

| 问题类型 | P0-Critical | P1-High | P2-Medium | P3-Low | 合计 |
|---------|-------------|---------|-----------|--------|------|
| 设计缺陷 | 0 | 2 | 2 | 0 | 4 |
| 功能缺失 | 1 | 2 | 0 | 0 | 3 |
| 性能优化 | 0 | 0 | 1 | 0 | 1 |
| 安全漏洞 | 2 | 2 | 1 | 0 | 5 |
| 技术债务 | 1 | 2 | 1 | 1 | 5 |
| **合计** | **4** | **8** | **5** | **1** | **18** |

### 1.4 组件特性利用概览

| 组件 | 当前使用深度 | 待利用关键特性数 | 优先级推荐 |
|------|-------------|-----------------|-----------|
| Spring Boot | 中等 | 3 | 高 |
| Jackson | 基础 | 2 | 高 |
| Redisson | 中等 | 3 | 高 |
| MyBatis-Plus | 中等 | 2 | 中（版本已升级） |
| MapStruct | 基础 | 2 | 中 |
| Caffeine | 中等 | 2 | 中 |
| Disruptor | 中等 | 1 | 低 |
| SkyWalking | 基础 | 1 | 低 |
| Fastjson | 基础 | 1 | 低 |

### 1.5 今日重点关注
- **[TOP 1]** 昨日 4 个 P0 安全问题连续 3 天未修复（反序列化 Visibility.ANY ×2、MongoDB 路径错误、spring.factories 残留）— **安全风险持续累积**
- **[TOP 2]** `carlos-audit-bus` AutoConfiguration.imports 为空 — **审计自动配置完全失效，影响框架核心功能**
- **[TOP 3]** `carlos-spring-boot-starter-web` 和 `carlos-spring-cloud-starter` 的 `@RestControllerAdvice` 误注册为 AutoConfiguration — **设计缺陷，降低框架灵活性**
- **[TOP 4]** MyBatis-Plus 已升级至 3.5.16，建议评估并启用新特性 `insert-ignore-auto-increment-column`
- **[TOP 特性]** 建议 Redisson 启用 `RLocalCachedMap` 多级缓存替代手动 Caffeine+Redis 组合

---

## 二、设计缺陷详情

### 问题 1: FeignGlobalExceptionHandler 被误注册为 AutoConfiguration（遗留，第 3 天）

**模块**: `carlos-spring-cloud-starter`
**文件**: `carlos-spring-boot/carlos-spring-cloud-starter/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
**行号**: L7
**严重程度**: P1-High
**影响范围**: 模块级
**首次报告**: 2026-05-16
**修复状态**: ❌ 未修复（已持续 2 天）

#### 相关代码

```java
// carlos-spring-boot/carlos-spring-cloud-starter/src/main/java/com/carlos/cloud/feign/FeignGlobalExceptionHandler.java
package com.carlos.cloud.feign;

import lombok.extern.slf4j.Slf4j;
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
com.carlos.cloud.feign.FeignConfig
com.carlos.cloud.nacos.NacosConfig
com.carlos.cloud.seata.SeataConfig
com.carlos.cloud.sentinel.SentinelConfig
com.carlos.cloud.sentinel.SentinelFeignConfig
com.carlos.cloud.loadbalance.LoadBalancerConfig
com.carlos.cloud.feign.FeignGlobalExceptionHandler
```

#### 详细分析
- **问题描述**: `FeignGlobalExceptionHandler` 是一个 `@RestControllerAdvice` 异常处理器，被列入了 `AutoConfiguration.imports` 文件中。这与 `GlobalExceptionHandler` 问题属于同一类设计缺陷。
- **违反规范**: AGENTS.md 中 "自动配置缺陷" 检查项；`@RestControllerAdvice` 类不应作为 AutoConfiguration 注册。
- **触发条件**: 所有引入 `carlos-spring-cloud-starter` 的应用都会无条件加载此异常处理器。
- **潜在风险**: 异常处理器无法通过配置关闭；与 Spring Boot 自动配置语义不一致；框架灵活性受损。

#### 解决方案
**推荐修复**:
从 `AutoConfiguration.imports` 中移除 `FeignGlobalExceptionHandler`，改为在 `FeignConfig` 中通过 `@Bean` + `@ConditionalOnProperty` 注册：

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

---

### 问题 2: GlobalExceptionHandler 仍被误注册为 AutoConfiguration（遗留，第 4 天）

**模块**: `carlos-spring-boot-starter-web`
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
**行号**: L14
**严重程度**: P1-High
**影响范围**: 模块级
**首次报告**: 2026-05-14
**修复状态**: ❌ 未修复（已持续 4 天）

#### 相关代码

```
com.carlos.boot.GlobalExceptionHandler
```

#### 详细分析
- **问题描述**: `GlobalExceptionHandler` 作为 `@RestControllerAdvice` 被注册为 AutoConfiguration，连续 4 天未修复。
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

### 问题 3: BatchServiceImpl 使用 @Autowired 字段注入（遗留，第 2 天）

**模块**: `carlos-spring-boot-starter-mybatis`
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-mybatis/src/main/java/com/carlos/datasource/base/BatchServiceImpl.java`
**行号**: L35
**严重程度**: P2-Medium
**影响范围**: 类级
**首次报告**: 2026-05-16
**修复状态**: ❌ 未修复

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
- **问题描述**: `BatchServiceImpl` 使用 `@Autowired` 字段注入。Spring Boot 官方推荐构造器注入。
- **违反规范**: Spring Boot 最佳实践 — 构造器注入优先
- **潜在风险**: 可测试性降低；依赖关系不透明；循环依赖难以发现

#### 解决方案
**推荐修复**:

```java
@Slf4j
@RequiredArgsConstructor
public class BatchServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BatchService<T> {

    // 使用 Lombok @RequiredArgsConstructor 生成构造器注入
    // 或显式声明构造器
    
    public BatchServiceImpl(M baseMapper) {
        super(baseMapper);
    }
}
```

---

### 问题 4: Audit bus AutoConfiguration.imports 为空 — 审计模块完全失效（遗留，第 2 天）

**模块**: `carlos-audit-bus`
**文件**: `carlos-integration/carlos-audit/carlos-audit-bus/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
**行号**: 全文
**严重程度**: P1-High
**影响范围**: 模块级
**首次报告**: 2026-05-16
**修复状态**: ❌ 未修复

#### 相关代码

```
# carlos-integration/carlos-audit/carlos-audit-bus/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
（文件为空）
```

而实际存在的自动配置类：

```java
// carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/config/AuditAutoConfiguration.java
// carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/config/AuditDisruptorConfig.java
// carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/clickhouse/ClickHouseConfig.java
```

#### 详细分析
- **问题描述**: `carlos-audit-bus` 模块的 `AutoConfiguration.imports` 文件为空，但模块内存在 `AuditAutoConfiguration`、`AuditDisruptorConfig`、`ClickHouseConfig` 等配置类。这意味着引入 `carlos-audit-boot` 或 `carlos-audit-bus` 依赖后，审计相关的自动配置完全不会生效。
- **违反规范**: AGENTS.md "自动配置缺陷" 检查项；`AutoConfiguration.imports` 必须完整注册所有配置类
- **触发条件**: 任何引入审计 Starter 的应用
- **潜在风险**: **审计日志功能完全失效** — ClickHouse 批量写入、Disruptor 事件处理、审计配置管理等核心功能不会被 Spring Boot 自动加载；业务操作无审计追踪；合规性风险

#### 解决方案
**推荐修复**:
补充 `AutoConfiguration.imports`：

```
com.carlos.audit.config.AuditAutoConfiguration
com.carlos.audit.config.AuditDisruptorConfig
com.carlos.audit.clickhouse.ClickHouseConfig
```

**短期规避**: 在应用主类上手动 `@Import` 上述配置类。

---

## 三、功能未完善详情

### 问题 5: MongoDB Starter AutoConfiguration 路径错误（遗留，第 4 天）

**模块**: `carlos-spring-boot-starter-mongodb`
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-mongodb/src/main/resources/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
**严重程度**: P0-Critical
**影响范围**: 模块级
**首次报告**: 2026-05-14
**修复状态**: ❌ 未修复（已持续 4 天）

#### 相关代码

```
# 错误路径
spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
# 正确路径应为
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

#### 详细分析
- **问题描述**: 文件放在了 `resources/spring/` 目录下，而非标准的 `resources/META-INF/spring/` 目录。Spring Boot 3.x 的自动配置加载器只会扫描 `META-INF/spring/` 路径下的 `.imports` 文件。
- **违反规范**: Spring Boot 3.x 自动配置规范
- **触发条件**: 引入 `carlos-spring-boot-starter-mongodb` 的应用
- **潜在风险**: MongoDB 自动配置完全不会生效，需要手动 `@Import(MongoAutoConfiguration.class)` 才能启用

#### 解决方案
**推荐修复**:
```bash
mkdir -p carlos-spring-boot/carlos-spring-boot-starter-mongodb/src/main/resources/META-INF/spring/
mv carlos-spring-boot/carlos-spring-boot-starter-mongodb/src/main/resources/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports \
   carlos-spring-boot/carlos-spring-boot-starter-mongodb/src/main/resources/META-INF/spring/
```

---

### 问题 6: 测试覆盖率极低（遗留，长期）

**模块**: 全框架
**严重程度**: P1-High
**影响范围**: 框架级
**首次报告**: 2026-05-14
**修复状态**: ❌ 未修复

#### 相关数据
- Java 文件总数: 2312
- 测试类数量: 6 个
- 测试覆盖率估算: ~0.26%

#### 测试类清单
1. `RestClientBuilderUtilsTest.java` — integration starter
2. `SM4UtilTest.java` — encrypt starter
3. `MigrationServiceTest.java` — migration starter
4. `GitlabServiceTest.java` — tools（非标准单元测试，位于 main 目录）
5. `PasswordUtilTest.java` — test 模块
6. `EncryptUtilTest.java` — test 模块

#### 详细分析
- **问题描述**: 2312 个 Java 源文件仅有 6 个测试类，测试覆盖率极低。`GitlabServiceTest` 甚至位于 `src/main` 目录而非 `src/test`。
- **违反规范**: 测试驱动开发原则；核心逻辑必须覆盖单元测试
- **潜在风险**: 回归风险极高；重构时无安全网；新贡献者难以验证变更

#### 解决方案
**推荐修复**:
1. 为核心模块（core、web、mybatis、security）编写单元测试，目标覆盖率 10%（基线）
2. 将 `GitlabServiceTest` 移至 `src/test` 目录
3. 使用 `@SpringBootTest` + `@AutoConfigureMockMvc` 覆盖 Controller 层
4. 为 `BaseServiceImpl`、`BatchServiceImpl`、`Result`、`CommonErrorCode` 等核心类编写单元测试

---

## 四、代码可优化点详情

### 问题 7: carlos-dependencies/pom.xml 仍可进一步优化注释清理

**模块**: `carlos-dependencies`
**文件**: `carlos-dependencies/pom.xml`
**严重程度**: P3-Low
**影响范围**: 模块级

#### 相关代码

本次 commit 清理了大量注释掉的旧版本配置（ commendable 👍 ），但仍发现少量残留：

```xml
<!-- carlos-dependencies/pom.xml 中仍残留的注释 -->
<!--<alibaba.seata.version>1.5.1</alibaba.seata.version>-->
<!--<transmittable-thread-local.version>2.14.5</transmittable-thread-local.version>-->
```

#### 详细分析
- **问题描述**: 本次 commit 已清理了大部分注释掉的旧版本配置（MyBatis-Plus 3.5.2、mapstruct 1.5.2.Final、okhttp 4.9.3、poi 4.1.2、kaptcha 2.3.2 等），但仍有少量残留。
- **潜在风险**: 注释冗余，影响 pom 可读性

#### 解决方案
继续清理残留的注释行，保持 pom 文件整洁。

---

## 五、安全漏洞详情

### 问题 8: JacksonSerializer 使用 Visibility.ANY（遗留，第 4 天）

**模块**: `carlos-spring-boot-starter-redis-core`
**文件**: `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis-core/src/main/java/com/carlos/redis/serialize/JacksonSerializer.java`
**行号**: L64
**严重程度**: P0-Critical
**影响范围**: 框架级（所有使用 Redis 序列化的模块）
**CWE**: CWE-502: Deserialization of Untrusted Data
**首次报告**: 2026-05-14
**修复状态**: ❌ 未修复（已持续 4 天）

#### 相关代码

```java
// JacksonSerializer.java L64
// 设置所有属性可见
mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
```

完整上下文：

```java
private ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    // 序列化时忽略 null 值
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // 反序列化时忽略未知属性
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // 禁用日期转为时间戳
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    // 设置所有属性可见 ⚠️ 安全风险
    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // ...
}
```

#### 详细分析
- **问题描述**: `Visibility.ANY` 允许 Jackson 反序列化所有字段（包括 private 字段），即使类没有提供相应的 getter/setter。虽然配置了 `BasicPolymorphicTypeValidator` 白名单，但 `Visibility.ANY` 仍然扩大了反序列化攻击面。
- **违反规范**: AGENTS.md 安全检查清单 "反序列化安全风险"
- **触发条件**: 攻击者若能控制 Redis 中存储的序列化数据（如通过 Redis 注入、反序列化漏洞链），可利用此配置反序列化恶意对象
- **潜在风险**: 即使存在白名单，攻击者仍可能利用 `Visibility.ANY` 绕过部分安全检查，构造反序列化 gadget chain

#### 解决方案
**推荐修复**:

```java
// 将 Visibility.ANY 降级为 PUBLIC_ONLY
mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
mapper.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.PUBLIC_ONLY);
```

---

### 问题 9: RedisOAuth2AuthorizationService 使用 Visibility.ANY（遗留，第 4 天）

**模块**: `carlos-auth-service`
**文件**: `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/repository/RedisOAuth2AuthorizationService.java`
**行号**: L98
**严重程度**: P0-Critical
**影响范围**: 框架级（OAuth2 授权服务器）
**CWE**: CWE-502: Deserialization of Untrusted Data
**首次报告**: 2026-05-14
**修复状态**: ❌ 未修复（已持续 4 天）

#### 相关代码

```java
@PostConstruct
private void configureObjectMapper() {
    // 配置可见性 ⚠️ 安全风险
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // 注册 JavaTimeModule 支持 JDK8 日期时间
    objectMapper.registerModule(new JavaTimeModule());
    // 注册 Spring Security Jackson2 Modules
    ClassLoader classLoader = getClass().getClassLoader();
    objectMapper.registerModules(SecurityJackson2Modules.getModules(classLoader));
    // 注册 OAuth2 Authorization Server Jackson2 Module
    objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    // ...
}
```

#### 详细分析
- **问题描述**: OAuth2 授权数据（授权码、访问令牌、刷新令牌）的 Redis 序列化同样使用 `Visibility.ANY`。OAuth2 授权数据属于**高敏感数据**，反序列化漏洞可能导致令牌伪造、授权绕过。
- **违反规范**: AGENTS.md 安全红线 "反序列化安全风险"
- **触发条件**: Redis 数据被篡改或存在反序列化 gadget chain
- **潜在风险**: OAuth2 令牌伪造、未授权访问、会话劫持

#### 解决方案
**推荐修复**:

```java
@PostConstruct
private void configureObjectMapper() {
    // 安全配置：仅反序列化 public 属性和字段
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    objectMapper.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    objectMapper.registerModule(new JavaTimeModule());
    // ... 其余配置保持不变
}
```

**注意**: 降级 Visibility 后需验证 Spring Security OAuth2 相关类的序列化/反序列化是否正常工作。这些类通常有 public getter/setter，使用 `PUBLIC_ONLY` 应该兼容。

---

### 问题 10: EncryptConfig INFO 级别日志输出 SM4 密钥和 IV（遗留，第 2 天）

**模块**: `carlos-spring-boot-starter-encrypt`
**文件**: `carlos-spring-boot/carlos-spring-boot-starter-encrypt/src/main/java/com/carlos/encrypt/config/EncryptConfig.java`
**行号**: L57-L58
**严重程度**: P2-Medium
**影响范围**: 模块级
**CWE**: CWE-532: Insertion of Sensitive Information into Log File
**首次报告**: 2026-05-16
**修复状态**: ❌ 未修复

#### 相关代码

```java
// EncryptConfig.java L57-L58
log.info("SM4 key:{}", key);
log.info("SM4 iv:{}", iv);
```

#### 详细分析
- **问题描述**: 在国密 SM4 加密配置初始化时，使用 `INFO` 级别日志输出密钥（key）和初始化向量（iv）。生产环境通常配置 INFO 级别日志，这意味着密钥会以明文形式出现在日志文件中。
- **违反规范**: AGENTS.md "无硬编码凭据" 安全红线（扩展到日志输出）
- **触发条件**: 应用启动时，生产环境日志采集系统会记录这些密钥
- **潜在风险**: 密钥泄露；日志系统被攻破后可直接获取加密密钥；不符合国密合规要求

#### 解决方案
**推荐修复**:

```java
// 方案 1：完全移除密钥日志输出
// 方案 2：降级为 DEBUG + 脱敏
if (log.isDebugEnabled()) {
    log.debug("SM4 config loaded, key fingerprint: {}", sha256Fingerprint(key));
    log.debug("SM4 config loaded, iv fingerprint: {}", sha256Fingerprint(iv));
}

// 辅助方法
private String sha256Fingerprint(String data) {
    return DigestUtils.sha256Hex(data).substring(0, 16);
}
```

---

### 问题 11: RabbitMQ 和 ClickHouse 密码硬编码默认值（遗留，第 4 天）

**模块**: `carlos-spring-boot-starter-mq` / `carlos-audit-bus`
**文件**:
- `carlos-spring-boot/carlos-spring-boot-starter-mq/src/main/java/com/carlos/mq/config/RabbitMqProperties.java` L36-L41
- `carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/config/AuditProperties.java` L102
**严重程度**: P1-High
**影响范围**: 模块级
**CWE**: CWE-798: Use of Hard-coded Credentials
**首次报告**: 2026-05-14
**修复状态**: ❌ 未修复（已持续 4 天）

#### 相关代码

```java
// RabbitMqProperties.java
private String username = "guest";
private String password = "guest";

// AuditProperties.java
private String password = "";
```

#### 详细分析
- **问题描述**: `@ConfigurationProperties` 默认值中硬编码了敏感凭据。虽然 Spring Boot 配置可以覆盖，但默认值在生产环境中可能被遗忘修改。
- **违反规范**: AGENTS.md "无硬编码凭据" 安全红线
- **触发条件**: 用户未在 `application.yml` 中显式配置 RabbitMQ/ClickHouse 密码时
- **潜在风险**: 使用默认凭据连接生产环境，安全风险极高

#### 解决方案
**推荐修复**:

```java
// RabbitMqProperties.java
private String username;
private String password;

// AuditProperties.java
private String password;

// 在配置类中校验非空
@PostConstruct
public void validate() {
    Assert.hasText(username, "RabbitMQ username must be configured");
    Assert.hasText(password, "RabbitMQ password must be configured");
}
```

---

## 六、依赖与技术债务

### 6.1 今日依赖升级评估

| 依赖 | 原版本 | 新版本 | 变更类型 | 评估 |
|------|--------|--------|---------|------|
| MyBatis-Plus | 3.5.15 | 3.5.16 | 小版本升级 | ✅ 正向升级，包含新特性和 bug 修复 |
| fastjson | 2.0.60 | 2.0.61 | 小版本升级 | ✅ 正向升级，修复 record types 中 @JsonProperty 问题 |

#### MyBatis-Plus 3.5.16 变更要点
- **feat**: 新增 `insert-ignore-auto-increment-column` 全局配置（默认 `false`），开启后 INSERT 语句忽略自增主键字段生成
- **feat**: 新增参数填充器跳过方式（基于 `MappedStatement#id`）
- 其他 bug 修复和性能优化

**框架影响评估**: MyBatis-Plus 3.5.16 为向后兼容升级，现有代码无需改动。建议评估 `insert-ignore-auto-increment-column` 特性是否需要启用。

#### fastjson 2.0.61 变更要点
- **fix**: 修复 Jackson `@JsonProperty` 在 record types 中被忽略的问题 (#3893)
- 常规维护更新

**框架影响评估**: fastjson 2.0.61 为常规维护升级，对框架现有使用无影响。

### 6.2 CVE 安全通告

| CVE 编号 | 影响组件 | 框架版本 | 风险等级 | 说明 |
|---------|---------|---------|---------|------|
| CVE-2026-22733 | Spring Boot | 3.5.9 | 高危 | CloudFoundry Actuator 路径冲突导致认证绕过。框架未使用 CloudFoundry，风险较低，建议关注 Spring Boot 3.5.10 补丁。 |

### 6.3 spring.factories 残留（Spring Boot 3.x 废弃机制）（遗留，第 4 天）

| 文件 | 状态 | 说明 |
|----|------|------|
| `carlos-spring-boot-starter-apm/src/main/resources/META-INF/spring.factories` | ❌ 残留 | 应迁移至 `.imports` |
| `carlos-spring-boot-starter-migration/src/main/resources/META-INF/spring.factories` | ❌ 残留 | 应迁移至 `.imports` |
| `carlos-message-core/src/main/resources/META-INF/spring.factories` | ❌ 残留 | 应迁移至 `.imports` |

Spring Boot 3.x 已废弃 `spring.factories` 用于自动配置注册，应统一使用 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`。

**注意**: `apm` 和 `migration` 模块同时存在 `.imports` 和 `spring.factories`，属于重复注册。`message-core` 仅存在 `spring.factories`。

### 6.4 已弃用 API 与迁移残留

| 文件 | 行号 | 已弃用 API / 问题 | 替代方案 |
|----|----|---------|------|
| `JacksonSerializer.java` | L64 | `Visibility.ANY` | `Visibility.PUBLIC_ONLY` |
| `RedisOAuth2AuthorizationService.java` | L98 | `Visibility.ANY` | `Visibility.PUBLIC_ONLY` |
| `GitlabServiceTest.java` | L26,36 | `throw new RuntimeException(e)` | `throw new BusinessException(e)` |
| `RabbitMqProperties.java` | L36-L41 | 硬编码默认凭据 `guest` | 无默认值 + `@PostConstruct` 校验 |
| `EncryptConfig.java` | L57-L58 | `log.info()` 输出 SM4 密钥 | 移除或改为 DEBUG + 脱敏 |

### 6.5 javax.* 包分析

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

**建议 1: 将 `Visibility.ANY` 降级为 `PUBLIC_ONLY`（安全优先，已持续 4 天未实施）**

- **场景**: `JacksonSerializer`（Redis 序列化）和 `RedisOAuth2AuthorizationService`（OAuth2 授权数据序列化）
- **代码示例**:
```java
mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
```
- **收益**: 缩小反序列化攻击面，降低 CWE-502 风险
- **紧急程度**: 🔴 极高（已标记 P0 连续 4 天）

---

#### 组件 2: MyBatis-Plus (当前版本: 3.5.16 ⬆️ 刚升级)

**当前项目使用概况**
- 使用位置: 数据层 CRUD、分页、Join 查询
- 当前用法摘要: BaseServiceImpl、mybatis-plus-join 1.5.4、分页插件、TenantLineInnerInterceptor

**3.5.16 新增特性分析**

| 特性名称 | 版本 | 当前状态 | 推荐应用场景 | 预期收益 | 实施难度 |
|------|------|------|--------|------|------|
| `insert-ignore-auto-increment-column` | 3.5.16 | 未配置 | 所有使用自增主键的 Entity | 避免主键冲突 | 低 |
| 参数填充器跳过方式 | 3.5.16 | 未配置 | 特定 MappedStatement 跳过填充 | 精细化控制 | 低 |
| Lambda 链式查询 | 3.5.x | 部分使用 | 所有 Manager 层 | 类型安全 | 低 |
| Active Record 模式 | 3.5.x | 未使用 | 简单 CRUD 场景 | 代码简化 | 低 |

**具体应用建议**

**建议 1: 评估启用 `insert-ignore-auto-increment-column`（新特性）**

- **场景**: 框架中大量 Entity 使用 `@TableId(type = IdType.AUTO)` 自增主键，在特定场景（如数据迁移、批量导入）下可能需要 INSERT 时不生成主键字段
- **配置方式**:
```yaml
mybatis-plus:
  global-config:
    db-config:
      insert-ignore-auto-increment-column: true  # 默认 false
```
- **收益**: 避免数据迁移时自增主键冲突，提升批量导入兼容性
- **注意**: 默认 `false` 保持现有行为，按需开启

**建议 2: 推广 Lambda 链式查询到所有 Manager 层**

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

#### 组件 3: Redisson (当前版本: 3.51.0)

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

---

#### 组件 4: Spring Boot 3.5.9

**当前项目使用概况**
- 使用位置: 全框架基础设施
- 当前用法摘要: 已启用虚拟线程（`VirtualThreadConfig`、`TomcatVirtualThreadConfig`），已使用 `RestClient` 和 `@HttpExchange`

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
- **收益**: 符合 RFC 7807 标准，与 Spring Security OAuth2 错误响应格式统一

---

### 7.2 中优先级组件

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

#### 组件 7: Fastjson (当前版本: 2.0.61 ⬆️ 刚升级)

**当前项目使用概况**
- 使用位置: JSON 序列化配置（`JsonAutoConfig`）、可能的部分工具类
- 当前用法摘要: 基础 JSON 解析/生成

**2.0.61 新特性**
- **fix**: 修复 Jackson `@JsonProperty` 在 record types 中被忽略的问题

**未被充分利用的特性**

| 特性名称 | 当前状态 | 推荐应用场景 | 预期收益 | 实施难度 |
|------|------|--------|------|------|
| JSONB 格式 | 未使用 | 高性能二进制序列化场景 | 性能提升 | 低 |
| autoType 安全白名单 | 部分使用 | 反序列化安全 | 安全增强 | 低 |
| JSONPath | 未使用 | 复杂 JSON 查询 | 功能增强 | 低 |

**具体应用建议**

本次升级 2.0.61 为常规维护更新，无需额外操作。建议确认 `JsonAutoConfig` 中已正确配置 autoType 白名单。

---

### 7.3 已充分利用组件（表扬）

| 组件 | 当前使用深度 | 说明 |
|------|-------------|------|
| Spring HTTP Interface | 中等 | `DingtalkApiClient` 使用 `@HttpExchange`，`RestClientBuilderUtils` 提供工厂方法 |
| RestClient | 中等 | `carlos-spring-boot-starter-integration` 测试和主代码均使用 |
| MyBatis-Plus TenantLineInnerInterceptor | 已启用 | `MyBatisPlusConfig` 已配置租户拦截器 |
| Disruptor | 中等 | `carlos-audit-bus` 使用 Disruptor 批量处理审计日志 |
| Virtual Threads | 已启用 | `VirtualThreadConfig`、`TomcatVirtualThreadConfig` 已启用 JDK 21 虚拟线程 |

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
| carlos-audit-bus | ⭐⭐⭐☆☆ | ⭐☆☆☆☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | 2.6/5.0 |
| carlos-auth-service | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐☆☆☆ | ⭐⭐⭐☆☆ | 3.2/5.0 |
| carlos-gateway | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐⭐☆ | 4.0/5.0 |
| carlos-integration (整体) | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐⭐☆ | 3.2/5.0 |
| carlos-samples | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐☆☆ | ⭐⭐⭐⭐☆ | ⭐⭐⭐☆☆ | 3.2/5.0 |
| carlos-test | ⭐⭐⭐☆☆ | ⭐⭐☆☆☆ | ⭐⭐☆☆☆ | ⭐⭐⭐⭐☆ | ⭐⭐☆☆☆ | 2.4/5.0 |

> 评分标准：5星=优秀，4星=良好，3星=一般，2星=较差，1星=需立即整改
> `carlos-spring-boot-starter-mongodb` 因 AutoConfiguration 路径错误拉低功能完整评分
> `carlos-audit-bus` 因 AutoConfiguration.imports 为空拉低功能完整评分（从 2.8 降至 2.6）
> `carlos-spring-boot-starter-encrypt` 因日志泄露密钥拉低安全性评分
> `carlos-spring-boot-starter-redis` 因 Jackson Visibility.ANY 拉低安全性评分

---

## 九、待办事项与修复计划

### 9.1 今日新增待办

- [ ] **[特性]** `carlos-dependencies`: 评估 MyBatis-Plus 3.5.16 新特性 `insert-ignore-auto-increment-column` 是否适用于框架场景
- [ ] **[P3]** `carlos-dependencies`: 继续清理 `pom.xml` 中残留的旧版本注释（seata 1.5.1、ttl 2.14.5）

### 9.2 历史遗留跟踪（重点关注连续多日未修复项）

#### 🔴 P0-Critical（连续 4 天未修复，安全风险累积）

- [ ] **[P0]** `carlos-spring-boot-starter-redis-core`: `JacksonSerializer` `Visibility.ANY` → `PUBLIC_ONLY` ⏳ 遗留未修复（第 4 天）
- [ ] **[P0]** `carlos-auth-service`: `RedisOAuth2AuthorizationService` `Visibility.ANY` → `PUBLIC_ONLY` ⏳ 遗留未修复（第 4 天）
- [ ] **[P0]** `carlos-spring-boot-starter-mongodb`: 修复 `AutoConfiguration.imports` 路径到 `META-INF/spring/` ⏳ 遗留未修复（第 4 天）
- [ ] **[P0]** `carlos-spring-boot-starter-apm` / `carlos-spring-boot-starter-migration` / `carlos-message-core`: 迁移 `spring.factories` 到 `.imports` ⏳ 遗留未修复（第 4 天）

#### 🟠 P1-High（连续 2~4 天未修复）

- [ ] **[P1]** `carlos-spring-boot-starter-web`: 将 `GlobalExceptionHandler` 从 `AutoConfiguration.imports` 移除 ⏳ 遗留未修复（第 4 天）
- [ ] **[P1]** `carlos-spring-cloud-starter`: 将 `FeignGlobalExceptionHandler` 从 `AutoConfiguration.imports` 移除 ⏳ 遗留未修复（第 2 天）
- [ ] **[P1]** `carlos-audit-bus`: 在 `AutoConfiguration.imports` 中补充 `AuditAutoConfiguration`、`AuditDisruptorConfig`、`ClickHouseConfig` ⏳ 遗留未修复（第 2 天）
- [ ] **[P1]** `carlos-spring-boot-starter-mq`: `RabbitMqProperties` 移除默认凭据 `guest` ⏳ 遗留未修复（第 4 天）
- [ ] **[P1]** `carlos-audit-bus`: `AuditProperties` 移除空密码默认值 ⏳ 遗留未修复（第 4 天）
- [ ] **[P1]** 全框架: 测试覆盖率提升至 10%（基线目标） ⏳ 遗留未修复（长期）

#### 🟡 P2-Medium

- [ ] **[P2]** `carlos-spring-boot-starter-encrypt`: 移除 `EncryptConfig` 中 `log.info()` 输出 SM4 密钥/IV 的代码 ⏳ 遗留未修复（第 2 天）
- [ ] **[P2]** `carlos-spring-boot-starter-mybatis`: 将 `BatchServiceImpl` 的 `@Autowired` 字段注入改为构造器注入 ⏳ 遗留未修复（第 2 天）
- [ ] **[P2]** `carlos-spring-boot-starter-flowable`: 为 `FlowableProcessService.deploy()` 增加文件类型白名单和大小限制校验 ⏳ 遗留未修复

#### 🟢 P3-Low

- [ ] **[P3]** `carlos-spring-boot-core`: `SimpleErrorCode` 将 `@Data` 改为 `@Getter` + `final` 字段 ⏳ 遗留未修复（长期）

### 9.3 本周已修复问题（表扬）

- ✅ **[P1]** `carlos-system-bus`: `SysDictCacheManager` Guava Cache → Caffeine 替换（176283c9, 2026-05-15 前）
- ✅ **[P1]** `carlos-system-bus`: `SysConfigServiceImpl` Guava Cache → Caffeine 替换（176283c9, 2026-05-15 前）
- ✅ **[P1]** 11 个文件: `synchronized` → `ReentrantLock` 修复 JDK 21 虚拟线程 Pinning 风险（945c1a58, 2026-05-15 前）
- ✅ `carlos-auth-service`: `Oauth2ExceptionHandler` 优化（2d0aab98, d4308143, 2026-05-15 前）
- ✅ `carlos-spring-boot-starter-web`: `GlobalExceptionHandler` 优化（2d0aab98, d4308143, 2026-05-15 前）
- ✅ `carlos-dependencies`: MyBatis-Plus 3.5.15 → 3.5.16，fastjson 2.0.60 → 2.0.61，清理旧版本注释（5584f5a8, 2026-05-17）

---

## 十、技术趋势洞察

### 10.1 关键依赖版本动态

| 组件 | 框架版本 | 最新版本 | 发布日期 | 升级建议 |
|------|---------|---------|---------|---------|
| Spring Boot | 3.5.9 | 3.5.9 | 2026-05 | 当前最新，保持 |
| Spring Cloud | 2025.0.1 | 2025.0.1 | 2026-04 | 当前最新，保持 |
| MyBatis-Plus | 3.5.16 | 3.5.16 | 2026-01 | 刚升级至最新，保持 |
| Redisson | 3.51.0 | 3.51.0 | 2025-11 | 当前最新，保持 |
| Hutool | 5.8.40 | 5.8.40 | 2025-11 | 当前最新，保持 |
| Fastjson | 2.0.61 | 2.0.61 | 2026-02 | 刚升级至最新，保持 |

### 10.2 安全通告

- **CVE-2026-22733**: Spring Boot 认证绕过漏洞（CloudFoundry Actuator 路径冲突）。框架未直接依赖 CloudFoundry，但建议关注 Spring Boot 3.5.10 补丁版本。

---

## 十一、扫描总结与建议

### 11.1 今日扫描结论

今日代码变更量较小（仅依赖版本升级和报告清理），未发现新的代码质量问题。**核心风险在于历史遗留问题的持续累积**：

1. **4 个 P0 安全问题已连续 4 天未修复**，其中 2 个涉及 Jackson `Visibility.ANY` 反序列化风险，1 个涉及 MongoDB 自动配置失效，1 个涉及 spring.factories 废弃机制残留。
2. **审计模块（carlos-audit-bus）AutoConfiguration.imports 为空**是一个严重的功能缺陷，意味着审计 Starter 引入后完全无法自动生效。
3. **MyBatis-Plus 升级至 3.5.16 是正向变更**，建议进一步评估新特性的适用场景。

### 11.2 下一步行动建议（按优先级排序）

| 优先级 | 行动项 | 预计工时 | 影响 |
|------|--------|---------|------|
| 🔴 紧急 | 修复 4 个 P0 安全问题（Jackson Visibility ×2、MongoDB 路径、spring.factories） | 2h | 消除安全风险 |
| 🟠 高 | 补充 carlos-audit-bus AutoConfiguration.imports | 15min | 恢复审计功能 |
| 🟠 高 | 移除 2 个 @RestControllerAdvice 的 AutoConfiguration 误注册 | 30min | 规范自动配置 |
| 🟠 高 | 移除 RabbitMQ/ClickHouse 默认凭据 | 20min | 消除安全隐患 |
| 🟡 中 | 修复 EncryptConfig 日志泄露密钥 | 10min | 消除信息泄露 |
| 🟡 中 | 编写核心模块单元测试（目标 10% 覆盖率） | 8h | 建立质量基线 |
| 🟢 低 | 评估 MyBatis-Plus 3.5.16 新特性 | 1h | 功能增强 |

---

*本报告由 OpenClaw 定时任务自动生成，问题定位基于静态代码分析，具体修复前请人工复核。*
*报告生成时间: 2026-05-17 09:00*
*扫描 Commit: 5584f5a8dd746c9f70505eb1b8aad0126b64897f*
