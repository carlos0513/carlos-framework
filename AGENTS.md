# AGENTS.md

本文档为 AI 编程助手提供 Carlos Framework 项目的背景、架构、开发规范和相关指南。

> **⚠️ 编码规范已迁移至 Skill**
>
> 详细的编码规范（分层架构、命名规范、数据查询、Redis 缓存、异常处理等）已整理为 Skill：
> `.claude/skills/carlos-framework-standard/SKILL.md`
>
> **编写代码前请务必阅读该 Skill！**

---

## 项目概述

**Carlos Framework** 是一个基于 Spring Boot 3.5.9 和 Spring Cloud Alibaba 构建的 Java 微服务脚手架框架，旨在加速企业级应用开发。该项目采用多模块 Maven 结构，提供了一套完整的分布式系统解决方案。

### 主要特点

- **模块化设计**: 38 个模块按职责分层，清晰分离关注点
- **Spring Boot 3.x**: 基于 JDK 17 和 Spring Boot 3.5.9
- **微服务生态**: 集成 Spring Cloud Alibaba (Nacos、Sentinel、Seata)
- **国密支持**: 采用 SM2/SM4 国密算法替代 RSA/AES
- **自动配置**: 所有 Starter 模块支持 Spring Boot 自动配置机制
- **多租户支持**: 内置多租户数据隔离能力

### 版本信息

- **当前版本**: 3.0.0-SNAPSHOT
- **JDK 要求**: 17+
- **Maven 要求**: 3.8+
- **Spring Boot**: 3.5.9
- **Spring Cloud**: 2025.0.1
- **Spring Cloud Alibaba**: 2025.0.0.0

### Spring Boot 3.x 升级说明

本框架已从 Spring Boot 2.7 升级至 3.5.9，主要变更包括：

- **Jakarta EE**: 所有 `javax.*` 包已迁移至 `jakarta.*`
- **Spring Security 6.x**: 使用 `SecurityFilterChain` 替代 `WebSecurityConfigurerAdapter`
- **MyBatis-Plus**: 使用 `mybatis-plus-spring-boot3-starter` 适配 Spring Boot 3
- **MySQL 驱动**: 使用 `mysql-connector-j` 替代 `mysql-connector-java`

---

## 项目结构

```
carlos-framework/                          # 根聚合器 (carlos-framework)
├── carlos-dependencies/                   # BOM - 依赖版本管理
├── carlos-parent/                         # 父 POM - 构建配置和插件管理
├── carlos-commons/                        # 通用基础库（框架无关）
│   ├── carlos-spring-boot-core/           # 核心抽象、异常、分页、响应包装
│   ├── carlos-utils/                      # 通用工具类
│   └── carlos-excel/                      # Excel 处理工具
├── carlos-spring-boot/                    # Spring Boot 集成层（22 个 Starters）
│   ├── carlos-spring-boot-starter-web/            # Web 基础自动配置
│   ├── carlos-spring-cloud-starter/               # Spring Cloud 集成
│   ├── carlos-spring-boot-starter-mybatis/        # MyBatis-Plus + 多数据源
│   ├── carlos-spring-boot-starter-redis/          # Redis + Redisson + Caffeine
│   ├── carlos-spring-boot-starter-oauth2/         # OAuth2 认证授权
│   ├── carlos-spring-boot-starter-encrypt/        # 国密加密 (SM2/SM4)
│   ├── carlos-spring-boot-starter-minio/          # MinIO 对象存储
│   ├── carlos-spring-boot-starter-flowable/       # 工作流引擎
│   └── ... (其他 13 个 Starter 模块)
├── carlos-integration/                    # 第三方集成
│   ├── carlos-license/                    # 软件授权管理（TrueLicense）
│   │   ├── carlos-license-core/           # 核心功能
│   │   ├── carlos-spring-boot-starter-license-generate/   # 证书生成（仅开发）
│   │   └── carlos-spring-boot-starter-license-verify/     # 证书验证（生产）
│   └── carlos-tools/                      # GUI 桌面工具（代码生成器）
└── carlos-samples/                        # 示例和测试
    └── carlos-test/                       # 测试应用
```

**模块数量统计**:

- 1 个根聚合器
- 1 个 BOM (carlos-dependencies)
- 1 个父 POM (carlos-parent)
- 3 个 Commons 模块
- 22 个 Spring Boot Starter 模块
- 5 个 License 相关模块（包含 3 个子模块）
- 1 个 Tools 模块
- 1 个测试模块

---

## 技术栈

| 组件                   | 版本         | 说明                      |
|----------------------|------------|-------------------------|
| JDK                  | 17         | 最低要求                    |
| Spring Boot          | 3.5.9      | 核心框架                    |
| Spring Cloud         | 2025.0.1   | 微服务框架                   |
| Spring Cloud Alibaba | 2025.0.0.0 | 阿里微服务生态                 |
| MyBatis-Plus         | 3.5.15     | ORM 框架（含 Join 扩展 1.5.4） |
| Seata                | 2.0.0      | 分布式事务                   |
| Hutool               | 5.8.40     | 工具库                     |
| MapStruct            | 1.6.3      | 对象映射                    |
| Guava                | 33.4.8-jre | Google 工具库              |
| Druid                | 1.2.27     | 数据库连接池                  |
| SkyWalking           | 9.5.0      | APM 链路追踪                |
| Fastjson             | 2.0.60     | JSON 序列化                |
| Apache POI           | 5.2.5      | Excel 处理                |
| Knife4j              | 4.5.0      | OpenAPI 文档              |

---

## 构建和测试命令

### 基础构建

```bash
# 构建所有模块（默认跳过测试）
mvn clean install

# 构建并运行测试
mvn clean install -DskipTests=false

# 仅构建特定模块
cd carlos-commons/carlos-spring-boot-core
mvn clean install
```

### 多环境部署

项目支持两种 Nexus 仓库配置：

```bash
# 公网环境 (zcarlos.com:8081)
mvn clean install -P carlos-public
mvn clean deploy -P carlos-public

# 内网环境 (192.168.3.30:8081)
mvn clean install -P carlos-private
mvn clean deploy -P carlos-private
```

### 测试命令

```bash
# 运行单元测试（默认跳过）
mvn test -DskipTests=false

# 运行特定测试类
mvn test -Dtest=ClassName

# 运行集成测试（*IT.java）
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

---

## 应用分层架构

```
                        外部服务调用
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│  API 接口层 (api)   │  Feign 接口定义，对外暴露服务              │
│  - ApiXxx.java      │  提供熔断降级工厂 (fallback)              │
│  - AO/Param         │  用于微服务间调用                         │
└─────────────────────────────────────────────────────────────────┘
                              ↓
                        HTTP 请求
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│  API 实现层 (apiimpl)│  Feign 接口的 REST 实现                  │
│  - ApiXxxImpl.java  │  同时暴露给三方服务的 HTTP 端点            │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│  Controller 层    │  接收 Web 请求，完成基本参数校验           │
│  (controller/)    │  Param → DTO 转换，调用 Service           │
│                   │  避免在 Controller 中加入任何复杂逻辑      │
│                   │  使用 MapStruct Convert 进行对象转换       │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│  Service 层       │  业务逻辑服务层，纯粹的进行业务串联        │
│  (service/)       │  处理业务流程，避免直接数据操作            │
│                   │  通过 Manager 层进行数据获取               │
│                   │  作为 Controller 和 Manager 的桥梁         │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│  Manager 层       │  数据查询封装层，继承 BaseService          │
│  (manager/)       │  实现增删改查等原子操作                    │
│                   │  与 Mapper 层交互，处理数据持久化          │
│                   │  接口在 manager/，实现在 manager/impl/     │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│  Mapper/Repository│  数据访问层，使用 MyBatis 实现             │
│  (mapper/)        │  与 MySQL、ES、MongoDB、Oracle 等交互      │
│  (resources/mapper/)│ XML 映射文件                            │
└─────────────────────────────────────────────────────────────────┘
```

### 分层领域模型

| 模型         | 名称       | 所在包           | 说明                                                                |
|------------|----------|---------------|-------------------------------------------------------------------|
| **Param**  | 参数对象     | `pojo.param`  | 前端参数接收对象，按操作细分：`XxxCreateParam`, `XxxUpdateParam`, `XxxPageParam` |
| **DTO**    | 数据传输对象   | `pojo.dto`    | 服务层与数据层之间传输的对象                                                    |
| **VO**     | 视图对象     | `pojo.vo`     | 显示层对象，响应给前端，需标注 Swagger 注解                                        |
| **AO**     | API 对象   | `pojo.ao`     | API 接口响应对象，用于 Feign 调用方                                           |
| **Entity** | 实体对象     | `pojo.entity` | 与数据库表结构一一对应，DO（Data Object）                                       |
| **Excel**  | Excel 对象 | `pojo.excel`  | 导入导出专用对象                                                          |
| **Enum**   | 枚举       | `pojo.emuns`  | 业务枚举类型                                                            |

### 模块结构说明

#### API 模块 (`{service}-api`)

```
src/main/java/com/carlos/{service}/
├── api/                           # Feign 接口定义
│   ├── ApiXxx.java               # 对外暴露的 Feign 接口
│   └── fallback/                 # 熔断降级工厂
│       └── ApiXxxFallbackFactory.java
└── pojo/
    ├── ao/                       # API 响应对象（供调用方使用）
│   └── XxxAO.java
    └── param/                    # API 请求参数（供调用方使用）
        └── ApiXxxParam.java
```

#### Business 模块 (`{service}-bus`)

```
src/main/java/com/carlos/{service}/
├── apiimpl/                       # Feign 接口实现（REST 端点）
│   └── ApiXxxImpl.java
├── config/                        # 配置类
├── controller/                    # Web 控制器层
├── convert/                       # MapStruct 对象转换
├── exception/                     # 自定义异常
├── manager/                       # 数据查询封装层
│   ├── XxxManager.java
│   └── impl/XxxManagerImpl.java
├── mapper/                        # MyBatis Mapper 接口
├── pojo/                          # 领域对象
│   ├── dto/
│   ├── enums/
│   ├── entity/
│   ├── excel/
│   ├── param/
│   └── vo/
└── service/                       # Service 层（只有接口）

src/main/resources/
└── mapper/                        # MyBatis XML 映射文件
```

---

## 编码规范速查（关键要点）

### ⚠️ 强制规范（违反会导致代码被拒绝）

| 规范              | 要求                                              | 禁止                    |
|-----------------|-------------------------------------------------|-----------------------|
| **数据查询**        | 使用 MyBatis-Plus + mybatis-plus-join 在 Manager 层 | 严禁直接编写 SQL            |
| **Redis 缓存**    | 在 Manager 层统一实现                                 | Service 层禁止直接操作 Redis |
| **异常处理**        | 使用框架定义异常类（BusinessException/SystemException）    | 严禁使用 RuntimeException |
| **错误码**         | 使用 CommonErrorCode 或自定义 ErrorCode               | 严禁硬编码错误码              |
| **统一响应**        | 使用 ApiResponse/Result 返回                        | 严禁直接返回裸数据             |
| **Lombok**      | 使用注解生成 getter/setter                            | 严禁手写 get/set 方法       |
| **属性注入**        | 使用 @ConfigurationProperties                     | 严禁使用 @Value           |
| **Service 返回值** | 返回 DTO 或 Entity                                 | 严禁返回 VO               |
| **枚举类**         | 实现 BaseEnum，使用 @AppEnum                         | 普通枚举定义                |

### 包命名约定

- **核心模块**: `com.carlos.core.*`
- **工具模块**: `com.carlos.utils.*`
- **Spring Boot 模块**: `com.carlos.boot.*`、`com.carlos.redis.*` 等
- **测试模块**: `com.carlos.test.*`

---

### 统一响应格式

**所有 API 必须返回统一响应格式：**

```java
// ✅ 正确：使用 ApiResponse
@GetMapping("/user/{id}")
public ApiResponse<UserVO> getUser(@PathVariable Long id) {
    UserDTO user = userService.getById(id);
    return Result.success(userConvert.dtoToVo(user));
}

// ❌ 错误：直接返回裸数据
@GetMapping("/user/{id}")
public UserVO getUser(@PathVariable Long id) {  // 严禁！
    // ...
}
```

**响应结构：**

```json
{
  "success": true,
  "code": "00000",
  "msg": "操作成功",
  "data": { },
  "timestamp": 1710638258000,
  "details": null
}
```

---

### 错误码规范

**错误码格式：5位数字字符串 `A-BB-CC`**

- `A` - 错误级别：0成功，1客户端错误，2业务错误，3第三方错误，5系统错误
- `BB` - 模块编码：00通用，01用户，02认证等
- `CC` - 具体序号

**抛出异常：**

```java
// ✅ 正确：使用 CommonErrorCode
throw CommonErrorCode.USER_NOT_FOUND.exception();
throw CommonErrorCode.USER_ACCOUNT_LOCKED.exception("账号已被锁定，%d分钟后重试", 30);

// ✅ 正确：使用 BusinessException
throw new BusinessException(CommonErrorCode.USER_NOT_FOUND);
throw new BusinessException(CommonErrorCode.USER_NOT_FOUND, "自定义消息", 404);

// ❌ 错误：硬编码错误码或使用原生异常
throw new BusinessException("20101", "用户不存在");  // 严禁！
throw new RuntimeException("用户不存在");  // 严禁！
```

**常用错误码：**
| 错误码 | 消息 | HTTP | 说明 |
|-------|------|------|------|
| `00000` | 操作成功 | 200 | 通用成功 |
| `10001` | 请求参数错误 | 400 | 通用参数错误 |
| `10002` | 未授权，请先登录 | 401 | 未登录或 Token 过期 |
| `10004` | 请求的资源不存在 | 404 | 资源不存在 |
| `10101` | 参数校验失败 | 400 | 字段校验失败 |
| `20101` | 用户不存在 | 404 | 用户不存在 |
| `20201` | 登录凭证已过期 | 401 | Token 过期 |
| `50001` | 系统内部错误 | 500 | 未预期异常 |

---

## 开发规范

### 版本管理

- 使用 `${revision}` 占位符统一版本号
- 通过 `flatten-maven-plugin` 处理版本变量
- 不要在子模块中硬编码版本号

### 依赖声明规范

```xml
<!-- 正确：不指定版本，由 parent 管理 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-core</artifactId>
</dependency>

<!-- 错误：硬编码版本 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-core</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 资源过滤

配置文件支持 Maven 属性替换（使用 `@...@` 分隔符）：

```yaml
app:
  name: @project.name@
  version: @project.version@
```

---

## 单元测试

### 基本原则（AIR）

- **A**：Automatic（自动化）
- **I**：Independent（独立性）
- **R**：Repeatable（可重复）

### 强制要求

1. 单元测试应该是全自动执行的，并且非交互式的，必须使用 `assert` 来验证
2. 单元测试用例之间决不能互相调用，也不能依赖执行的先后次序
3. 单元测试是可以重复执行的，不能受到外界环境的影响
4. 测试粒度至多是类级别，一般是方法级别
5. 核心业务、核心应用、核心模块的增量代码确保单元测试通过
6. 单元测试代码必须写在 `src/test/java`，不允许写在业务代码目录下

### 推荐做法

1. 语句覆盖率达到 70%，核心模块的语句覆盖率和分支覆盖率达到 100%
2. 编写单元测试遵守 BCDE 原则：
    - **B**：Border，边界值测试
    - **C**：Correct，正确的输入得到预期结果
    - **D**：Design，与设计文档相结合
    - **E**：Error，强制错误信息输入
3. 数据库相关测试使用自动回滚机制，不给数据库造成脏数据

---

## 安全规约

1. **权限控制**：隶属于用户个人的页面或者功能必须进行权限控制校验
2. **数据脱敏**：用户敏感数据禁止直接展示，必须对展示数据进行脱敏
3. **SQL 注入**：用户输入的 SQL 参数严格使用参数绑定，禁止 `${ }` 拼接 SQL
4. **参数校验**：用户请求传入的任何参数必须做有效性验证
5. **XSS 防护**：禁止向 HTML 页面输出未经安全过滤或未正确转义的用户数据
6. **CSRF**：表单、AJAX 提交必须执行 CSRF 安全验证
7. **防重放**：使用平台资源（短信、邮件、电话、下单、支付）必须实现防重放机制
8. **防刷策略**：发贴、评论、发送即时消息等场景必须实现防刷、文本内容违禁词过滤

---

## MySQL 数据库规约（摘要）

### 建表规约

1. **命名**：
    - 表达是与否概念的字段，使用 `is_xxx` 的方式命名，数据类型 `tinyint unsigned`
    - 表名、字段名必须使用**小写字母或数字**
    - 禁用保留字

2. **索引命名**：
    - 主键索引：`pk_字段名`
    - 唯一索引：`uk_字段名`
    - 普通索引：`idx_字段名`

3. **数据类型**：
    - 小数类型为 `decimal`，禁止使用 `float` 和 `double`
   - `varchar` 长度不要超过 5000，超过则使用 `text`

4. **必备字段**：`id`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`
    - `id` 为主键，类型 `char(32)`，生成方式 `IdUtils.date32Id()`

### SQL 语句

- 不要使用 `count(列名)` 或 `count(常量)` 来替代 `count(*)`
- 分页查询若 count 为 0 应直接返回
- 不得使用外键与级联
- **禁止使用存储过程**
- `sql.xml` 配置参数使用 `#{ }`，不要使用 `${ }`

---

## Spring Boot 3.x 迁移指南

### 重要变更

#### 1. Jakarta EE 命名空间

| 旧包名                   | 新包名                     |
|-----------------------|-------------------------|
| `javax.servlet.*`     | `jakarta.servlet.*`     |
| `javax.validation.*`  | `jakarta.validation.*`  |
| `javax.annotation.*`  | `jakarta.annotation.*`  |
| `javax.persistence.*` | `jakarta.persistence.*` |

#### 2. Spring Security 6.x

- **废弃类**: `WebSecurityConfigurerAdapter` 已废弃
- **新方式**: 使用 `SecurityFilterChain` bean
- **请求匹配**: `antMatchers()` → `requestMatchers()`
- **方法安全**: `@EnableGlobalMethodSecurity` → `@EnableMethodSecurity`

#### 3. MyBatis-Plus 3.5.x

- 使用 `mybatis-plus-spring-boot3-starter` 适配 Spring Boot 3
- `PaginationInnerInterceptor` 需要显式指定数据库类型
- `IdentifierGenerator.nextId()` 返回类型为 `Long`

#### 4. MySQL 驱动变更

```xml
<!-- 旧方式（Spring Boot 2.x） -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

<!-- 新方式（Spring Boot 3.x） -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

### 自动配置变更

Spring Boot 3.x 使用新的自动配置注册方式：

```
# 旧方式（已废弃）
META-INF/spring.factories

# 新方式
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

---

## 安全注意事项

### License 模块安全

**⚠️ 重要警告**:

- **生产环境**: 只包含 `carlos-spring-boot-starter-license-verify`
- **严禁**: 将 `carlos-spring-boot-starter-license-generate` 打包到生产环境
- **原因**: 证书生成模块包含私钥操作，泄露会导致授权失效

### OAuth2 安全

- 默认 `DefaultOAuth2UserDetailsService` 仅供测试
- 生产环境必须实现自定义 `OAuth2UserDetailsService`
- **Password 授权模式已被移除**，建议使用 `authorization_code` + PKCE
- 建议使用 Redis 存储 Token

### 加密模块

- 使用 SM2/SM4 国密算法替代 RSA/AES
- 密钥配置应使用环境变量或配置中心
- 避免在代码中硬编码密钥

---

## 架构依赖规则

### 依赖方向（严禁反向依赖）

```
carlos-samples (测试应用)
    ↓
carlos-integration (license, tools)
    ↓
carlos-spring-boot (starters)
    ↓
carlos-commons (core, utils, excel)
    ↓
carlos-parent (构建配置)
    ↓
carlos-dependencies (BOM)
```

### 禁止事项

- ❌ Commons 模块依赖 Spring Boot
- ❌ 循环依赖
- ❌ 子模块硬编码版本号
- ❌ 生产环境包含 license-generate 模块
- ❌ Service 层直接引用 Mapper

---

## 重要配置参考

### 应用配置示例

```yaml
carlos:
  boot:
    cors:
      allowed-origins: ["*"]
    enums:
      scan-package: com.carlos
      enabled: true
  
  redis:
    enabled: true
    host: localhost
    port: 6379
    cache-type: caffeine_redis  # 多级缓存
  
  encrypt:
    sm4:
      enabled: true
      encrypt-mode: cbc
  
  oauth2:
    enabled: true
    authorization-server:
      enabled: true
      access-token-time-to-live: 2h
    jwt:
      issuer: http://localhost:8080
  
  license:
    verify:
      enabled: true
      subject: your-app-name
      license-path: /data/license/license.lic
```

---

## 工具模块使用

### carlos-tools (GUI 工具)

用于代码生成和项目脚手架的 Swing 桌面应用：

```bash
# 启动工具（IDE 中运行）
com.carlos.fx.ToolsApplication.start();
```

功能：

- 数据库代码生成（MyBatis/MongoDB/Elasticsearch）
- 项目脚手架
- 加解密工具
- GitLab 集成

### carlos-test (测试应用)

完整的功能演示和集成测试应用：

```bash
cd carlos-samples/carlos-test
mvn spring-boot:run
```

访问地址：

- Swagger 文档: http://localhost:9812/doc.html

---

## 参考文档

| 文档                                                               | 说明                 |
|------------------------------------------------------------------|--------------------|
| `.claude/skills/carlos-framework-standard/SKILL.md`              | **编码规范（必读）**       |
| `carlos-spring-boot/carlos-spring-boot-core/ERROR_CODE_SPEC.md`  | **错误码设计规范（必读）**    |
| `CLAUDE.md`                                                      | 项目结构、Maven 命令、开发指南 |
| `carlos-commons/carlos-spring-boot-core/README.md`               | 核心模块文档             |
| `carlos-parent/README.md`                                        | 父模块说明              |
| `carlos-integration/carlos-license/README.md`                    | License 文档         |
| `carlos-spring-boot/carlos-spring-boot-starter-oauth2/README.md` | OAuth2 文档          |
| `carlos-integration/carlos-tools/README.md`                      | 工具模块文档             |
| `OPTIMIZATION_REPORT.md`                                         | 优化报告               |

---

**注意**: 本框架仅供内部使用，请勿开源或外传。

**版本**: 3.0.0-SNAPSHOT | **Spring Boot**: 3.5.9 | **JDK**: 17+
