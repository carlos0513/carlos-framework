# carlos-Frame3 快速参考

> 基于 Spring Boot 3.5.8 + Spring Cloud Alibaba 的微服务脚手架框架（内部使用）

## 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | 17 | 最低要求 |
| Spring Boot | 3.5.8 | |
| Spring Cloud | 2023.0.6 | |
| Spring Cloud Alibaba | 2023.0.3.3 | Nacos/Sentinel/Seata |
| MyBatis-Plus | 3.5.12 | 含 Join 扩展 1.5.4 |
| Dynamic Datasource | 3.6.0 | 多数据源 |
| Seata | 2.0.0 | 分布式事务 |
| SkyWalking | 9.7.0 | APM 链路追踪 |
| Hutool | 5.8.40 | 工具库 |
| Druid | 1.2.27 | 连接池 |
| Fastjson | 2.0.60 | JSON 序列化 |
| Apache POI | 5.2.5 | Excel 处理 |

## 项目结构

```
carlos-frame3/
├── pom.xml                    # 根聚合器 (v2.0.0)
├── carlos/                    # 参考文档和示例代码 (v3.6.0)
└── carlos-parent/             # 框架实现 (v3.0.0-SNAPSHOT)
    ├── pom.xml               # 父 POM，统一依赖管理
    └── [29 个组件模块]
```

**版本管理**: 使用 `${revision}` 占位符 + flatten-maven-plugin 统一版本

## 核心模块职责

### 基础设施层

- **carlos-core**: 基础抽象、注解、AOP、异常、分页、响应包装
- **carlos-utils**: 通用工具类
- **carlos-springboot**: Spring Boot 自动配置和 Starter 支持
- **carlos-springcloud**: Spring Cloud Alibaba 集成（Nacos/Sentinel/Gateway）

### 数据访问层

- **carlos-mybatis**: MyBatis-Plus + 多数据源（Dynamic Datasource）
- **carlos-mongodb**: MongoDB 集成
- **carlos-redis**: Redis 缓存（Lettuce）
- **carlos-redisson**: Redisson 分布式锁和缓存
- **carlos-datascope**: 数据权限控制

### 集成通信层

- **carlos-gateway**: API 网关工具（Spring Cloud Gateway）
- **carlos-openapi**: OpenAPI/Swagger 文档（Knife4j）
- **carlos-docking**: 第三方集成框架（钉钉、融正通等）
- **carlos-mq**: 消息队列抽象
- **carlos-datacenter**: 数据中心集成/同步

### 安全认证层

- **carlos-encrypt**: 加密工具（SM2/SM4 国密算法）
- **carlos-license**: TrueLicense 软件授权系统（3 个子模块）
    - `carlos-license-core`: 共享模型
    - `carlos-license-generate`: 证书生成（**禁止打包到生产**）
    - `carlos-license-verify`: 证书验证（生产使用）
- **carlos-oauth2**: OAuth2 认证授权（Spring Security OAuth2 Authorization Server）

### 可观测性层

- **carlos-log**: 日志增强
- **carlos-apm**: APM 集成（SkyWalking/Sleuth）

### 工具层

- **carlos-excel**: Excel 导入导出（Apache POI）
- **carlos-json**: JSON 序列化（Fastjson）
- **carlos-minio**: MinIO 对象存储
- **carlos-sms**: 短信发送抽象（多供应商）
- **carlos-snowflake**: 分布式 ID 生成
- **carlos-tools**: GUI 桌面工具（代码生成器、项目脚手架、Swing）
- **carlos-flowable**: Flowable 工作流引擎
- **carlos-magicapi**: Magic API 集成
- **carlos-test**: 测试工具和示例

## 关键设计

### 1. 自动配置机制
- 使用 `@Configuration` + `@ConditionalOnProperty`
- 配置前缀统一为 `carlos.*`
- 通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 注册

### 2. 用户上下文传递
- `LoginUserInfo`: 登录用户信息模型
- `UserContext`: ThreadLocal 用户上下文
- `OAuth2Util`: OAuth2 用户信息提取工具

### 3. OAuth2 集成（carlos-oauth2）
- 支持 4 种授权模式：authorization_code, password, client_credentials, refresh_token
- JWT Token 增强：包含 user_id, tenant_id, dept_id, role_ids, authorities
- 方法级安全：`@PreAuthorize` 注解
- 端点：`/oauth2/token`, `/oauth2/authorize`, `/oauth2/revoke`, `/oauth2/introspect`

### 4. 多数据源支持
- 基于 Dynamic Datasource 3.6.0
- 支持动态切换和读写分离
- 配置示例：`carlos.datasource.dynamic.*`

### 5. 国密加密
- 使用 SM2/SM4 而非 RSA/AES
- 配置：`carlos.encrypt.sm4.enabled=true`

## 已知坑点

### 1. 非 Git 仓库
- 根目录不是 git 仓库，只有子目录有 `.git`
- 提交代码时需注意路径

### 2. 测试默认跳过
- `pom.xml` 中配置了 `<skipTests>true</skipTests>`
- 需要运行测试时：`mvn test -DskipTests=false`

### 3. License 模块安全

- **严禁**将 `carlos-license-generate` 打包到生产环境
- 生产只需 `carlos-license-verify`

### 4. OAuth2 默认实现
- `DefaultOAuth2UserDetailsService` 仅供测试
- 生产环境必须实现自定义 `OAuth2UserDetailsService`
- Password 授权模式已被 Spring Security OAuth2 标记为废弃

### 5. Token 存储
- 当前使用内存存储
- 生产环境建议使用 Redis（需配置 carlos-redis）

### 6. 资源过滤
- `application*.yml/yaml/properties` 使用 `@...@` 占位符
- Maven 属性替换时注意转义

### 7. JDK 版本
- 必须 JDK 17+，低版本无法编译

## Maven 常用命令

```bash
# 构建（测试已跳过）
mvn clean install

# 指定 Nexus 仓库
mvn clean install -P carlos-public    # 公网: zcarlos.com:8081
mvn clean install -P carlos-private   # 内网: 192.168.1.50:10004
mvn clean install -P carlos-yanfa     # 研发: 192.168.20.145:8081

# 构建单个模块
cd carlos-parent/carlos-core && mvn clean install

# 部署到 Nexus
mvn clean deploy -P carlos-public

# 运行测试
mvn test -DskipTests=false

# 检查依赖更新
mvn versions:display-dependency-updates
```

## 测试策略

### 当前状态
- 单元测试：`*Test.java`（**默认跳过**）
- 集成测试：`*IT.java`（使用 maven-failsafe-plugin）
- 测试配置示例：`carlos-test` 模块

### 建议策略

1. **核心模块必测**: carlos-core, carlos-mybatis, carlos-oauth2
2. **集成测试优先**: 数据库、Redis、MQ 等外部依赖
3. **安全模块重点测**: carlos-encrypt, carlos-license, carlos-oauth2
4. **工具类覆盖**: carlos-utils, carlos-excel, carlos-json

## 快速开始

### 1. 添加新模块
```bash
cd carlos-parent
mkdir carlos-newmodule
# 编辑 carlos-parent/pom.xml 添加 <module>carlos-newmodule</module>
```

### 2. 模块依赖原则
- 基础模块（core/utils）可被所有模块依赖
- Spring Boot 模块依赖 `carlos-springboot`
- Spring Cloud 模块依赖 `carlos-springcloud`
- 避免循环依赖

### 3. 代码生成工具
```java
// 运行 GUI 工具
com.carlos.tools.ToolsApplication.start()
```

## 重要文档

- 主文档: `carlos/README.md`
- 父模块说明: `carlos-parent/README_YD.md`
- License 文档: `carlos-parent/carlos-license/README.md`
- OAuth2 文档: `carlos-parent/carlos-oauth2/README.md`
- OAuth2 集成总结: `carlos-parent/carlos-oauth2/INTEGRATION_SUMMARY.md`

---

**严肃声明**: 当前脚手架代码仅限内部使用！
