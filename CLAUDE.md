# CLAUDE.md

本文档为 Claude Code (claude.ai/code) 提供本代码库的开发指导和规范。

## 项目概述

**Carlos Framework** 是基于 Spring Boot 3.5.9 (JDK 17) 和 Spring Cloud Alibaba 的 Java 微服务框架。项目采用多模块 Maven 构建结构，具有清晰的分层架构：

- **carlos-dependencies**: 集中式依赖版本管理 (BOM)
- **carlos-parent**: 统一父 POM，包含构建配置和插件管理
- **carlos-commons**: 与框架无关的公共工具类 (core, utils, excel)
- **carlos-spring-boot**: Spring Boot 集成层，包含 22 个 starter 模块
- **carlos-integration**: 第三方集成 (license, tools)
- **carlos-samples**: 示例应用程序和测试模块

这是一个内部框架/脚手架项目，旨在通过提供预构建的集成和工具来加速应用开发。

## Maven 命令

### 构建项目

```bash
# 构建所有模块 (pom.xml 中默认跳过测试)
mvn clean install

# 使用指定 profile 构建 (内部 Nexus 仓库)
mvn clean install -P carlos-public    # 公共服务器: zcarlos.com:8081
mvn clean install -P carlos-private   # 私有服务器: 192.168.3.30:8081

# 构建指定模块
cd carlos-commons/carlos-spring-boot-starter-core
mvn clean install

# 构建 Spring Boot starters
cd carlos-spring-boot
mvn clean install

# 部署到 Nexus
mvn clean deploy -P carlos-public
```

### 运行测试

```bash
# 运行单元测试 (pom.xml 配置中当前已跳过)
mvn test

# 运行指定测试
mvn test -Dtest=ClassName#methodName

# 运行集成测试
mvn verify
```

### 依赖管理

```bash
# 检查依赖更新
mvn versions:display-dependency-updates

# 检查插件更新
mvn versions:display-plugin-updates

# 升级到最新版本
mvn versions:use-latest-versions

# 查看有效 POM
mvn help:effective-pom
```

## 架构

### 模块结构

框架遵循分层架构，职责清晰分离：

```
carlos-framework/                          # 根聚合 POM
├── carlos-dependencies/                   # 依赖版本管理 (BOM)
├── carlos-parent/                         # 父 POM (构建配置、插件)
├── carlos-commons/                        # 与框架无关的工具类
│   ├── carlos-spring-boot-starter-core/                      # 核心抽象、异常、分页
│   ├── carlos-utils/                     # 公共工具函数
│   └── carlos-excel/                     # Excel 处理工具
├── carlos-spring-boot/                    # Spring Boot 集成层
│   ├── carlos-spring-boot-starter-apm/
│   ├── carlos-spring-boot-starter-redis/
│   ├── carlos-spring-boot-starter-mybatis/
│   └── ... (共 22 个 starters)
├── carlos-integration/                    # 第三方集成
│   ├── carlos-license/                   # 软件许可证 (TrueLicense)
│   │   ├── carlos-license-core/
│   │   ├── carlos-spring-boot-starter-license-generate/
│   │   └── carlos-spring-boot-starter-license-verify/
│   └── carlos-tools/                     # GUI 工具 (代码生成器)
└── carlos-samples/                        # 示例和测试
    └── carlos-test/                      # 测试应用
```

**构建顺序：**

1. carlos-dependencies (BOM)
2. carlos-parent (父 POM)
3. carlos-commons (3 个模块)
4. carlos-spring-boot (22 个模块)
5. carlos-integration (5 个模块)
6. carlos-samples (1 个模块)

### 核心组件模块

**Commons 层 (与框架无关)：**

- `carlos-spring-boot-starter-core`: 基础抽象、注解、AOP、异常、分页、响应包装器
- `carlos-utils`: 公共工具类和辅助函数 (树形工具、HTTP 客户端)
- `carlos-excel`: 使用 Apache POI 5.2.5 和 EasyExcel 进行 Excel 导入/导出

**Spring Boot 集成层：**

*核心基础设施：*

- `carlos-spring-boot-starter-web`: Spring Boot 自动配置和 starter 支持
- `carlos-spring-cloud-starter`: Spring Cloud Alibaba 集成 (Nacos, Sentinel)
- `carlos-spring-boot-starter-gateway`: Spring Cloud Gateway 的 API 网关工具
- `carlos-spring-boot-starter-json`: JSON 序列化 (Fastjson 2.0.60)

*数据访问：*

- `carlos-spring-boot-starter-mybatis`: MyBatis-Plus 集成，支持多数据源
- `carlos-spring-boot-starter-mongodb`: MongoDB 集成
- `carlos-spring-boot-starter-redis`: Redis + Redisson + Caffeine (统一缓存方案)
- `carlos-spring-boot-starter-datascope`: 数据权限/范围控制

*存储与消息：*

- `carlos-spring-boot-starter-minio`: MinIO 对象存储集成
- `carlos-spring-boot-starter-oss`: OSS (对象存储服务) 抽象
- `carlos-spring-boot-starter-mq`: 消息队列抽象

*安全与认证：*

- `carlos-spring-boot-starter-encrypt`: 加密工具 (国密 SM2, SM4)
- `carlos-spring-boot-starter-oauth2`: OAuth2 认证和授权

*集成：*

- `carlos-spring-boot-starter-docking`: 第三方集成框架 (钉钉、荣之通)
- `carlos-spring-boot-starter-datacenter`: 数据中心集成/同步
- `carlos-spring-boot-starter-sms`: 短信发送抽象，支持多提供商

*可观测性：*

- `carlos-spring-boot-starter-log`: 日志增强
- `carlos-spring-boot-starter-apm`: APM 集成 (SkyWalking 9.5.0)

*工具类：*

- `carlos-spring-boot-starter-openapi`: OpenAPI/Swagger 文档支持 (Knife4j)
- `carlos-spring-boot-starter-snowflake`: 分布式 ID 生成
- `carlos-spring-boot-starter-flowable`: Flowable 工作流引擎集成
- `carlos-spring-boot-starter-disruptor`: Disruptor 高性能队列组件

**Integration 层：**

- `carlos-license`: 基于 TrueLicense 的软件许可证系统 (3 个子模块)
    - `carlos-license-core`: 核心许可证功能
    - `carlos-spring-boot-starter-license-generate`: 许可证生成 (仅开发使用)
    - `carlos-spring-boot-starter-license-verify`: 许可证验证 (生产使用)
- `carlos-tools`: GUI 桌面工具 (代码生成器、项目脚手架、基于 Swing)

**Samples：**

- `carlos-test`: 演示框架用法的测试应用

### 技术栈 (来自 carlos-dependencies/pom.xml)

| 组件                   | 版本         | 说明                     |
|----------------------|------------|------------------------|
| Spring Boot          | 3.5.9      |                        |
| Spring Cloud         | 2025.0.1   |                        |
| Spring Cloud Alibaba | 2025.0.0.0 | Nacos, Sentinel, Seata |
| JDK                  | 17         | 最低版本要求                 |
| Maven                | 3.8+       | 最低版本要求                 |
| MyBatis-Plus         | 3.5.15     | 带 Join 扩展 1.5.4        |
| Seata                | 2.0.0      | 分布式事务                  |
| Hutool               | 5.8.40     | 工具库                    |
| MapStruct            | 1.6.3      | Bean 映射                |
| Guava                | 33.4.8-jre |                        |
| Druid                | 1.2.27     | 数据库连接池                 |
| SkyWalking           | 9.5.0      | APM 追踪                 |

### 依赖版本管理

框架采用集中式依赖管理方案：

1. **carlos-dependencies**: 使用 `<dependencyManagement>` 定义所有依赖版本
2. **carlos-parent**: 导入 carlos-dependencies BOM 并提供构建配置
3. **所有模块**: 继承自 carlos-parent，引用依赖时无需指定版本

使用 `${revision}` 占位符模式 (当前为 `3.0.0-SNAPSHOT`) 和 `flatten-maven-plugin` 集中管理版本。所有子模块继承此版本。

### 配置模式

框架使用 Spring Boot 的自动配置机制。模块通常提供：

- 带 `@Configuration` 和 `@ConditionalOnProperty` 的 AutoConfiguration 类
- `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 文件
- 使用 `carlos.*` 前缀命名空间的 `@ConfigurationProperties` 类

配置结构示例 (来自 carlos-test)：
```yaml
carlos:
  boot:
    cors:
      allowed-origins: [...]
    enums:
      scan-package: com.carlos
      enabled: true
  encrypt:
    sm4:
      enabled: true
      encrypt-mode: cbc
  docking:
    dingtalk:
      enabled: ${DINGTALK_ENABLE:false}
    rzt:
      enabled: ${RZT_ENABLE:true}
```

### 许可证模块架构

`carlos-license` 模块 (位于 `carlos-integration/`) 采用安全设计：

- `carlos-license-core`: 共享模型和抽象
- `carlos-spring-boot-starter-license-generate`: 证书生成 (不应包含在生产部署中)
- `carlos-spring-boot-starter-license-verify`: 证书验证 (应用中应包含此模块)

使用 TrueLicense 验证硬件指纹 (IP, MAC, CPU 序列号、主板序列号) 和时间限制。

## Spring Boot 3.x 迁移说明

本框架已从 Spring Boot 2.7 迁移到 Spring Boot 3.5。主要迁移变更包括：

### Jakarta EE 命名空间

- 所有 `javax.*` 导入已迁移到 `jakarta.*`
- 主要包: `jakarta.servlet`, `jakarta.validation`, `jakarta.annotation`

### Spring Security 6.x

- 使用 `SecurityFilterChain` 替代弃用的 `WebSecurityConfigurerAdapter`
- 使用 `requestMatchers()` 替代 `antMatchers()`
- 新的 Lambda DSL 配置风格
- 使用 `@EnableMethodSecurity` 替代 `@EnableGlobalMethodSecurity`

### MyBatis-Plus 3.5.x

- 使用 `mybatis-plus-spring-boot3-starter` 以兼容 Spring Boot 3
- `PaginationInnerInterceptor` 需要显式指定数据库类型
- `IdentifierGenerator.nextId()` 返回 `Long` 而不是 `Number`

### MySQL Connector

- `mysql-connector-java` (groupId: `mysql`) 已替换为 `mysql-connector-j` (groupId: `com.mysql`)

### Spring Authorization Server (OAuth2)

- 基于 Spring Authorization Server 1.x
- **重要**: Password 授予类型不再支持
- 使用 `authorization_code` + PKCE 进行安全认证

### AutoConfiguration 注册

- 使用新的 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 格式
- 不再使用弃用的 `spring.factories` 文件

## 开发规范

### 代码规范

#### 不可变性 (Immutability) - **至关重要**

始终创建新对象，**严禁**修改原对象 (Mutation)：

```java
// 错误：修改原对象 (Mutation)
public User updateUserName(User user, String name) {
    user.setName(name);  // 直接修改了原对象！
    return user;
}

// 正确：不可变性 (Immutability)
public User updateUserName(User user, String name) {
    return User.builder()
        .id(user.getId())
        .name(name)
        .email(user.getEmail())
        .build();
}
```

#### 文件组织

提倡"多而小"的文件，而非"少而大"的文件：

- 高内聚，低耦合
- 建议每文件 200-400 行，最大不超过 800 行
- 从大型组件中提取工具函数 (Utilities)
- 按功能/领域 (Feature/Domain) 组织，而非按类型 (Type) 组织

#### 命名规范

- **类名**: 使用 UpperCamelCase，名词形式 (如: `UserService`, `OrderController`)
- **方法名**: 使用 lowerCamelCase，动词开头 (如: `getUser()`, `createOrder()`)
- **常量**: 使用 UPPER_SNAKE_CASE (如: `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`)
- **包名**: 全小写，多层嵌套 (如: `com.carlos.auth.service.impl`)

#### 错误处理

始终进行全面的错误处理：

```java
try {
    Result result = riskyOperation();
    return result;
} catch (SpecificException e) {
    log.error("操作失败，具体原因: {}", e.getMessage(), e);
    throw new BusinessException("友好的用户提示信息");
} catch (Exception e) {
    log.error("未知错误: {}", e.getMessage(), e);
    throw new SystemException("系统繁忙，请稍后重试");
}
```

#### 输入验证

始终验证用户输入：

```java
import javax.validation.constraints.*;

public class UserCreateParam {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在 3-20 之间")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Min(value = 0, message = "年龄不能为负数")
    @Max(value = 150, message = "年龄不能超过 150")
    private Integer age;
}
```

#### 函数大小

- 函数应小而专注，**不超过 50 行**
- 单个函数只做一件事
- 嵌套层级不超过 4 层

#### 代码质量检查清单

在标记工作完成之前，确认以下内容：

- [ ] 代码易读且命名良好
- [ ] 函数体量小（<50 行）
- [ ] 文件内容聚焦（<800 行）
- [ ] 无深度嵌套（>4 层）
- [ ] 具备完善的错误处理
- [ ] 不存在 console.log 语句（使用日志框架）
- [ ] 不存在硬编码 (Hardcoded) 数值（使用常量）
- [ ] 不存在修改原对象 (Mutation) 操作（已采用不可变模式）
- [ ] 所有用户输入均已验证
- [ ] 日志级别使用正确

### 添加新模块

**Spring Boot Starters：**

1. 在 `carlos-spring-boot/` 下创建模块
2. 在 `carlos-spring-boot/pom.xml` 中添加 `<module>` 条目
3. 设置父项目为 `carlos-spring-boot`，版本为 `${revision}`
4. 遵循命名约定: `carlos-spring-boot-starter-{function}`
5. 在 `carlos-dependencies/pom.xml` 中添加依赖管理条目

**公共工具类：**

1. 在 `carlos-commons/` 下创建模块
2. 在 `carlos-commons/pom.xml` 中添加 `<module>` 条目
3. 设置父项目为 `carlos-commons`，版本为 `${revision}`
4. 遵循命名约定: `carlos-{function}`
5. 确保无 Spring Boot 依赖（保持与框架无关）

**第三方集成：**

1. 在 `carlos-integration/` 下创建模块
2. 在 `carlos-integration/pom.xml` 中添加 `<module>` 条目
3. 设置父项目为 `carlos-parent`，版本为 `${revision}`

### 模块依赖规则

**依赖规则：**

- Commons 模块 (`carlos-spring-boot-starter-core`, `carlos-utils`, `carlos-excel`) 是基础，与框架无关
- Spring Boot starters 可以依赖 commons 模块
- Spring Boot starters 应依赖 `carlos-spring-boot-starter-web` 获取基础配置
- Spring Cloud starters 应依赖 `carlos-spring-cloud-starter`
- Integration 模块可以依赖 commons 和 Spring Boot starters
- 避免模块间的循环依赖

**分层结构：**

```
carlos-samples (测试应用)
    ↓
carlos-integration (license, tools)
    ↓
carlos-spring-boot (22 starters)
    ↓
carlos-commons (core, utils, excel)
    ↓
carlos-parent (构建配置)
    ↓
carlos-dependencies (BOM)
```

### Git 工作流

#### 提交信息格式

```
<type>: <description>

<optional body>
```

类型 (Types): feat, fix, refactor, docs, test, chore, perf, ci

**示例：**

```
feat: 添加用户登录功能

- 实现用户名密码登录
- 集成 JWT token
- 添加登录日志记录
```

#### 拉取请求 (Pull Request) 工作流

创建 PR 时：

1. 分析完整的提交历史（不仅是最近一次提交）
2. 使用 `git diff [base-branch]...HEAD` 查看所有变更
3. 起草详尽的 PR 摘要
4. 包含带有 TODO 的测试计划
5. 如果是新分支，使用 `-u` 参数推送

#### 功能实现工作流

1. **规划先行**
    - 使用 **planner** 智能体创建实现计划
    - 识别依赖关系与风险
    - 拆分为多个阶段

2. **测试驱动开发 (TDD)**
    - 先编写测试 (RED)
    - 运行测试 - 应当失败 (FAIL)
    - 编写最简实现代码 (GREEN)
    - 运行测试 - 应当通过 (PASS)
    - 重构 (IMPROVE)
    - 验证 80% 以上的覆盖率

3. **代码评审**
    - 在编写代码后立即使用 **code-reviewer** 智能体
    - 解决严重 (CRITICAL) 和高 (HIGH) 等级的问题
    - 尽可能修复中 (MEDIUM) 等级的问题

### 测试要求

#### 最低测试覆盖率: 80%

测试类型（全部必选）：

1. **单元测试** - 独立函数、工具类、组件 (JUnit 5)
2. **集成测试** - API 端点、数据库操作 (MockMvc)
3. **端到端测试** - 关键用户流程 (Playwright)

#### 测试命名约定

- 单元测试类: `*Test.java` (位于 `src/test/java`)
- 集成测试类: `*IT.java` (使用 maven-failsafe-plugin)
- 测试方法: 使用 `should_xxx_when_xxx` 或 `test_xxx` 格式

```java
@Test
@DisplayName("应该成功创建用户当参数合法时")
void should_create_user_successfully_when_params_valid() {
    // given
    UserCreateParam param = new UserCreateParam();
    param.setUsername("testuser");
    param.setEmail("test@example.com");

    // when
    User user = userService.createUser(param);

    // then
    assertNotNull(user);
    assertEquals("testuser", user.getUsername());
    verify(userMapper, times(1)).insert(any(User.class));
}
```

#### Mock 使用规范

- 使用 `@Mock` 和 `@InjectMocks` 创建 Mock 对象
- 使用 `@ExtendWith(MockitoExtension.class)` 启用 Mockito
- 明确验证 Mock 对象的交互行为
- 避免过度 Mock，只 Mock 外部依赖

### 安全规范

#### 强制安全检查 (提交前必须检查)

- [ ] 无硬编码凭据（API 密钥、密码、令牌/Tokens）
- [ ] 所有用户输入均已验证
- [ ] 预防 SQL 注入（使用参数化查询/MyBatis-Plus）
- [ ] 预防 XSS（对 HTML 进行净化处理/Sanitized）
- [ ] 已启用 CSRF 保护
- [ ] 身份验证/授权已验证
- [ ] 所有端点均已设置速率限制（Rate limiting）
- [ ] 错误消息不泄露敏感数据

#### 凭据管理

```java
// 严禁：硬编码凭据
private String apiKey = "sk-proj-xxxxx";

// 推荐：环境变量
@Value("${api.key}")
private String apiKey;

// 或在启动时验证
@PostConstruct
public void init() {
    if (StringUtils.isEmpty(apiKey)) {
        throw new IllegalStateException("API_KEY not configured");
    }
}
```

### 日志规范

- 使用 SLF4J + Logback 日志框架
- 使用占位符而不是字符串拼接
- 合理使用日志级别：
    - **ERROR**: 系统错误，需要立即处理
    - **WARN**: 潜在问题，不影响系统运行
    - **INFO**: 重要业务流程
    - **DEBUG**: 调试信息，生产环境可关闭
    - **TRACE**: 详细追踪信息

```java
// 正确
log.info("用户 {} 登录成功，IP: {}", username, ip);
log.debug("订单创建参数: {}", JSON.toJSONString(orderParam));

// 错误
log.info("用户 " + username + " 登录成功");  // 性能差
System.out.println("调试信息");  // 生产环境无法关闭
```

### 代码生成工具

`carlos-tools` 模块 (位于 `carlos-integration/`) 提供基于 Swing 的 GUI，用于：

- 数据库代码生成 (从 MySQL 模式生成 MyBatis/MongoDB/Elasticsearch 代码)
- 项目脚手架
- 加密工具
- GitLab 集成

运行方式: 执行 `com.carlos.fx.ToolsApplication.main()` 方法

### 加密标准

本框架在某些模块中使用国密标准 (SM2/SM4) 而不是 RSA/AES。查阅加密代码时请注意这一点。

### 配置文件

资源过滤已启用，支持 `application*.yml/yaml/properties` 和 `bootstrap*.yml` 文件使用 `@...@` 分隔符进行 Maven 属性替换。

## 重要注意事项

- **Git 仓库**: 根目录是 Git 仓库
- **多 Profile 支持**: 存在两个 Nexus 仓库 profile，用于不同的部署环境
    - `carlos-public`: 公共服务器 zcarlos.com:8081
    - `carlos-private`: 私有服务器 192.168.3.30:8081
- **需要 JDK 17**: 这是 Spring Boot 3 项目，需要 JDK 17+
- **需要 Maven 3.8+**: 最低 Maven 版本为 3.8
- **仅限内部使用**: 此框架仅限内部使用
- **父版本**: 在子 POM 中使用 `${revision}`，不要使用硬编码版本
- **安全注意**: 切勿在生产制品中包含 `carlos-spring-boot-starter-license-generate` 模块
- **分层架构**: 框架遵循清晰的分层架构 (commons → spring-boot → integration → samples)
- **Spring Boot 3.x**: 框架已从 Spring Boot 2.7 升级到 3.5.9

## 近期更新

### carlos-auth 模块 (2026-01-25 新增)

基于 Spring Security OAuth2 Authorization Server 的综合 OAuth2 认证和授权模块。

**主要特性：**

- OAuth2 Authorization Server，支持多种授权类型 (authorization_code, client_credentials, refresh_token)
- OAuth2 Resource Server，支持 JWT 验证
- 自定义 JWT token 增强，包含用户上下文 (user_id, tenant_id, dept_id, role_ids, authorities)
- 与 carlos-spring-boot-starter-core 认证系统集成 (LoginUserInfo, UserContext)
- 基于 @PreAuthorize 注解的方法级安全控制
- OAuth2Util 工具类，便于访问当前用户信息

**模块结构：**
```
carlos-auth/
├── config/                    # OAuth2 配置类
│   ├── OAuth2Properties.java
│   ├── OAuth2AuthorizationServerConfig.java
│   └── OAuth2ResourceServerConfig.java
├── constant/                  # OAuth2 常量
├── enhancer/                  # JWT token 增强器
├── exception/                 # OAuth2 异常
├── service/                   # 用户详情服务
├── util/                      # OAuth2 工具类
└── example/                   # 使用示例
```

**配置示例：**
```yaml
carlos:
  oauth2:
    enabled: true
    authorization-server:
      enabled: true
      access-token-time-to-live: 2h
      refresh-token-time-to-live: 7d
    jwt:
      issuer: http://localhost:8080
      include-user-info: true
    clients:
      - client-id: my-client
        client-secret: my-secret
        authorization-grant-types: [authorization_code, refresh_token]
        redirect-uris: [http://localhost:8080/authorized]
        scopes: [read, write]
```

**使用方式：**
```java
// 获取当前用户信息
UserContext userContext = OAuth2Util.extractUserContext();
Long userId = OAuth2Util.getCurrentUserId();
String userName = OAuth2Util.getCurrentUserName();

// 方法级安全控制
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() { }
```

**OAuth2 端点：**

- `POST /oauth2/token` - 获取访问令牌
- `POST /oauth2/revoke` - 撤销令牌
- `POST /oauth2/introspect` - 令牌自省
- `GET /oauth2/jwks` - JWK 集
- `GET /oauth2/authorize` - 授权端点

**文档：**

- 完整文档: `carlos-spring-boot/carlos-spring-boot-starter-oauth2/README.md`
- 集成摘要: `carlos-spring-boot/carlos-spring-boot-starter-oauth2/INTEGRATION_SUMMARY.md`
- 示例代码: `com.carlos.auth.example.*`

**依赖：**

- carlos-spring-boot-starter-core (用户信息、异常)
- carlos-spring-boot-starter-redis (可选，用于令牌存储)
- spring-boot-starter-security
- spring-security-oauth2-authorization-server
- spring-boot-starter-oauth2-resource-server
- spring-security-oauth2-jose

**重要说明：**

- 默认的 `DefaultOAuth2UserDetailsService` 仅用于测试
- 生产环境必须实现自定义的 `OAuth2UserDetailsService`
- **Spring Authorization Server 1.x 不支持 Password 授权类型**
- 当前实现使用内存令牌存储 (生产环境考虑使用 Redis)
- JWT 令牌包含自定义声明: user_id, user_name, tenant_id, dept_id, role_ids, authorities

### Spring Boot 3.x 升级 (2026-02-20 完成)

框架已从 Spring Boot 2.7 升级到 Spring Boot 3.5.9。主要变更：

**Jakarta EE 迁移：**

- 所有 `javax.*` 包已迁移到 `jakarta.*`
- 主要 Servlet API: `javax.servlet` → `jakarta.servlet`
- 验证 API: `javax.validation` → `jakarta.validation`
- 注解 API: `javax.annotation` → `jakarta.annotation`

**Spring Security 6.x：**

- 从 `WebSecurityConfigurerAdapter` 迁移到 `SecurityFilterChain`
- 更新请求匹配器以使用新 API
- 使用 `@EnableMethodSecurity` 进行方法级安全控制

**MyBatis-Plus 3.5.x：**

- 使用 Spring Boot 3 兼容的 starter: `mybatis-plus-spring-boot3-starter`
- 更新 `PaginationInnerInterceptor` 并显式指定数据库类型
- 修复 `IdentifierGenerator.nextId()` 返回类型为 `Long`

**JSqlParser 更新：**

- 修复已弃用的 `ExpressionList` 构造函数用法

**依赖更新：**

- MySQL Connector: `mysql-connector-java` → `mysql-connector-j`

**OAuth2 配置：**

- 更新示例配置以说明不支持 password 授权类型
- 添加关于支持授权类型的说明注释

### 架构重构 (2026-02-01)

框架进行了重大架构重构以提高清晰度和可维护性：

**目录结构变更：**

- `carlos-spring-boot-framework` → `carlos-framework` (根聚合器)
- `carlos-spring-boot-dependencies` → `carlos-dependencies` (BOM)
- `carlos-spring-boot-parent` → `carlos-parent` (父 POM)
- `carlos-spring-boot-commons` → `carlos-commons` (与框架无关的工具类)
- `carlos-spring-boot-starters` → `carlos-spring-boot` (Spring Boot 集成)
- 创建 `carlos-integration/` 用于第三方集成 (license, tools)
- 创建 `carlos-samples/` 用于示例和测试

**优势：**

- 框架无关工具类与 Spring Boot 集成之间的清晰分离
- 更好的命名，不暗示所有内容都是 Spring Boot 特定的
- 为未来框架集成提供更好的可扩展性 (例如 Spring Cloud, Quarkus)
- 遵循行业最佳实践 (类似于 Spring Framework, Apache Commons)

### 模块合并 (2026-02-01)

**Redis 模块增强：**

- 将 `carlos-spring-boot-starter-redisson` 合并到 `carlos-spring-boot-starter-redis`
- 添加 Caffeine 本地缓存集成
- 添加多级缓存支持 (Caffeine L1 + Redis L2)
- 统一缓存方案：一个模块中包含 Redis + Redisson + Caffeine

**移除的模块：**

- `carlos-magicapi`: 已移除 (不再需要)

### 版本更新

当前版本：

- Spring Boot: **3.5.9**
- Spring Cloud: **2025.0.1**
- Spring Cloud Alibaba: **2025.0.0.0**
- MyBatis-Plus: **3.5.15**
- SkyWalking: **9.5.0**
- Hutool: **5.8.40**
- MapStruct: **1.6.3**
- Guava: **33.4.8-jre**
- Redisson: **3.51.0**

### 模块数量

框架目前包含 **38 个模块**，组织成分层架构：

- 1 个根聚合器 (carlos-framework)
- 1 个 BOM (carlos-dependencies)
- 1 个父 POM (carlos-parent)
- 4 个 commons 模块 (carlos-commons + 3 个子模块)
- 23 个 Spring Boot 模块 (carlos-spring-boot + 22 个 starters)
- 6 个集成模块 (carlos-integration + 5 个子模块)
- 2 个示例模块 (carlos-samples + 1 个测试应用)

### Git 状态

- 根目录：Git 仓库
- 当前分支：`refactor-carlos-auth-module-reorganization`
- 近期变更：Spring Boot 3.x 升级、架构重构、模块合并、目录重组

## 参考文档

- `OPTIMIZATION_REPORT.md`: 详细的优化历史和建议
- `README-REF.md`: 框架快速参考
- 模块 README：每个模块都有自己的 README.md，包含详细的使用说明
