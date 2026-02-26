# AGENTS.md

本文档为 AI 编程助手提供 Carlos Framework 项目的背景、架构、开发规范和相关指南。

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

- **Jakarta EE**: 所有 `jakarta.*` 包已迁移至 `jakarta.*`
- **Spring Security 6.x**: 使用 `SecurityFilterChain` 替代 `WebSecurityConfigurerAdapter`
- **MyBatis-Plus**: 使用 `mybatis-plus-spring-boot3-starter` 适配 Spring Boot 3
- **MySQL 驱动**: 使用 `mysql-connector-j` 替代 `mysql-connector-java`

## 项目结构

```
carlos-framework/                          # 根聚合器 (carlos-framework)
├── carlos-dependencies/                   # BOM - 依赖版本管理
├── carlos-parent/                         # 父 POM - 构建配置和插件管理
├── carlos-commons/                        # 通用基础库（框架无关）
│   ├── carlos-spring-boot-starter-core/                       # 核心抽象、异常、分页、响应包装
│   ├── carlos-utils/                      # 通用工具类
│   └── carlos-excel/                      # Excel 处理工具
├── carlos-spring-boot/                    # Spring Boot 集成层（22 个 Starters）
│   ├── carlos-spring-boot-starter-web/            # Web 基础自动配置
│   ├── carlos-spring-cloud-starter/               # Spring Cloud 集成
│   ├── carlos-spring-boot-starter-mybatis/        # MyBatis-Plus + 多数据源
│   ├── carlos-spring-boot-starter-redis/          # Redis + Redisson + Caffeine
│   ├── carlos-spring-boot-starter-oauth2/         # OAuth2 认证授权
│   ├── carlos-spring-boot-starter-encrypt/        # 国密加密 (SM2/SM4)
│   ├── carlos-spring-boot-starter-gateway/        # API 网关
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

## 构建和测试命令

### 基础构建

```bash
# 构建所有模块（默认跳过测试）
mvn clean install

# 构建并运行测试
mvn clean install -DskipTests=false

# 仅构建特定模块
cd carlos-commons/carlos-spring-boot-starter-core
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

## 代码组织规范

### 包命名约定

- **核心模块**: `com.carlos.core.*`
- **工具模块**: `com.carlos.utils.*`
- **Spring Boot 模块**: `com.carlos.boot.*`、`com.carlos.redis.*` 等
- **测试模块**: `com.carlos.test.*`

### 代码分层结构

```
src/main/java/com/carlos/{module}/
├── config/                # 配置类
├── constant/              # 常量定义
├── controller/            # 控制器层
├── service/               # 服务层
│   ├── impl/              # 服务实现
├── mapper/                # 数据访问层（MyBatis）
├── entity/                # 实体类
├── dto/                   # 数据传输对象
├── vo/                    # 视图对象
├── ao/                    # 应用对象（参数）
├── util/                  # 工具类
├── annotation/            # 自定义注解
├── aspect/                # AOP 切面
├── exception/             # 异常类
├── handler/               # 处理器
└── properties/            # 配置属性类
```

### 自动配置规范

所有 Spring Boot Starter 模块遵循以下规范：

1. **配置类**: 使用 `@Configuration` + `@ConditionalOnProperty`
2. **配置前缀**: 统一使用 `carlos.*` 命名空间
3. **自动配置注册**:
    - 文件: `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
    - 内容: 配置类的全限定名
4. **属性类**: 使用 `@ConfigurationProperties(prefix = "carlos.xxx")`

示例：

```java
@Configuration
@ConditionalOnProperty(prefix = "carlos.redis", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {
    // 配置代码
}
```

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
    <artifactId>carlos-spring-boot-starter-core</artifactId>
</dependency>

<!-- 错误：硬编码版本 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-core</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 模块命名规范

| 模块类型                | 命名格式                                                      | 示例                                               |
|---------------------|-----------------------------------------------------------|--------------------------------------------------|
| Commons 模块          | `carlos-{function}`                                       | `carlos-spring-boot-starter-core`、`carlos-utils` |
| Spring Boot Starter | `carlos-spring-boot-starter-{function}`                   | `carlos-spring-boot-starter-redis`               |
| License 模块          | `carlos-license-*`、`carlos-spring-boot-starter-license-*` | `carlos-license-core`                            |

### 资源过滤

配置文件支持 Maven 属性替换（使用 `@...@` 分隔符）：

```yaml
# application.yml
app:
  name: @project.name@
  version: @project.version@
```

### 编码规范

- 所有文件使用 UTF-8 编码（无 BOM）
- 保持原始文件编码，禁止转换
- 中文内容使用 UTF-8 存储
## Spring Boot 3.x 迁移指南

### 重要变更

#### 1. Jakarta EE 命名空间

Spring Boot 3.x 使用 Jakarta EE 9+，所有 `jakarta.*` 包名已改为 `jakarta.*`：

| 旧包名                     | 新包名                     |
|-------------------------|-------------------------|
| `jakarta.servlet.*`     | `jakarta.servlet.*`     |
| `jakarta.validation.*`  | `jakarta.validation.*`  |
| `jakarta.annotation.*`  | `jakarta.annotation.*`  |
| `jakarta.persistence.*` | `jakarta.persistence.*` |

**注意**: 以下包名不受影响（不属于 Jakarta EE）：

- `jakarta.sql.*` - JDBC 相关
- `javax.crypto.*` - 加密相关
- `jakarta.net.*` - 网络相关
- `jakarta.swing.*` - GUI 相关
- `jakarta.security.*` - 安全相关

#### 2. Spring Security 6.x

- **废弃类**: `WebSecurityConfigurerAdapter` 已废弃
- **新方式**: 使用 `SecurityFilterChain` bean
- **请求匹配**: `antMatchers()` → `requestMatchers()`
- **方法安全**: `@EnableGlobalMethodSecurity` → `@EnableMethodSecurity`

#### 3. MyBatis-Plus 3.5.x

- 使用 `mybatis-plus-spring-boot3-starter` 替代旧版本
- `PaginationInnerInterceptor` 需要显式指定数据库类型
- `IdentifierGenerator.nextId()` 返回类型为 `Long`
- JSqlParser 4.x+ 中 `ExpressionList` 构造函数已弃用

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

#### 5. OAuth2 授权服务器

Spring Authorization Server 1.x（配合 Spring Boot 3.x）的重要变更：

- **不支持** `password` 授权类型
- **推荐**使用 `authorization_code` + PKCE 模式
- 内存存储仅用于测试，生产环境应使用 JDBC/Redis

### 自动配置变更

Spring Boot 3.x 使用新的自动配置注册方式：

```
# 旧方式（已废弃）
META-INF/spring.factories

# 新方式
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

内容格式：直接列出自动配置类的全限定名，每行一个。

## 测试策略

### 测试类型

1. **单元测试**: `*Test.java`（默认跳过）
2. **集成测试**: `*IT.java`（使用 maven-failsafe-plugin）

### 测试配置

- 单元测试默认跳过：`<skipTests>true</skipTests>`
- 如需强制运行：`-DskipTests=false`
- JUnit 5 并行执行已配置

### 建议测试策略

1. **核心模块必测**: carlos-spring-boot-starter-core、carlos-spring-boot-starter-mybatis、carlos-auth
2. **集成测试优先**: 数据库、Redis、MQ 等外部依赖
3. **安全模块重点测**: carlos-spring-boot-starter-encrypt、carlos-license、carlos-auth
4. **工具类覆盖**: carlos-utils、carlos-excel

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
- 建议使用 Redis 存储 Token（当前使用内存存储）

### 加密模块

- 使用 SM2/SM4 国密算法替代 RSA/AES
- 密钥配置应使用环境变量或配置中心
- 避免在代码中硬编码密钥

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

### 模块依赖规则

| 模块类型                  | 可依赖                                           |
|-----------------------|-----------------------------------------------|
| Commons               | 无（框架无关）                                       |
| Spring Boot Starters  | carlos-commons、carlos-spring-boot-starter-web |
| Spring Cloud Starters | carlos-spring-cloud-starter                   |
| Integration           | carlos-commons、Spring Boot Starters           |

### 禁止事项

- ❌ Commons 模块依赖 Spring Boot
- ❌ 循环依赖（已由 maven-enforcer-plugin 强制检查）
- ❌ 子模块硬编码版本号
- ❌ 生产环境包含 license-generate 模块

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

## 工具模块使用

### carlos-tools (GUI 工具)

用于代码生成和项目脚手架的 Swing 桌面应用：

```java
// 启动工具
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

## 最佳实践

### 添加新模块步骤

1. 在相应目录创建模块文件夹
2. 创建 `pom.xml`，继承 `carlos-parent`
3. 在父模块 `pom.xml` 中添加 `<module>` 声明
4. 如需加入 BOM，在 `carlos-dependencies/pom.xml` 中添加依赖管理
5. 遵循命名规范和代码结构

### 代码提交前检查

```bash
# 1. 本地构建通过
mvn clean install

# 2. 检查依赖冲突
mvn dependency:analyze

# 3. 检查代码风格（如配置了 checkstyle）
mvn checkstyle:check
```

## 参考文档

- 核心模块: `carlos-commons/carlos-spring-boot-starter-core/README.md`
- 父模块说明: `carlos-parent/README.md`
- License 文档: `carlos-integration/carlos-license/README.md`
- OAuth2 文档: `carlos-spring-boot/carlos-spring-boot-starter-oauth2/README.md`
- 工具模块: `carlos-integration/carlos-tools/README.md`
- 优化报告: `OPTIMIZATION_REPORT.md`

---

**注意**: 本框架仅供内部使用，请勿开源或外传。

**版本**: 3.0.0-SNAPSHOT | **Spring Boot**: 3.5.9 | **JDK**: 17+
