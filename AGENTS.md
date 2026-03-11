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
│   ├── carlos-spring-boot-core/                       # 核心抽象、异常、分页、响应包装
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

## 代码组织规范

### 包命名约定

- **核心模块**: `com.carlos.core.*`
- **工具模块**: `com.carlos.utils.*`
- **Spring Boot 模块**: `com.carlos.boot.*`、`com.carlos.redis.*` 等
- **测试模块**: `com.carlos.test.*`

### 应用分层架构

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

**Param 细分规范：**

- `XxxCreateParam` - 创建操作参数
- `XxxUpdateParam` - 更新操作参数
- `XxxPageParam` - 分页查询参数
- 禁止超过 2 个参数的查询使用 Map 传输

**VO 规范：**

- VO 对象需标明 Swagger 属性描述注解（`@Schema`）
- 属性名称规范命名，字段不多不少（多余字段后期无法随意删除）
- 不同接口返回内容有差异的，建议创建多个 VO 对象进行字段封装
- VO 对象尽量避免出现在 Service/Manager 层

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
│   ├── XxxConfig.java            # 业务配置
│   ├── XxxConstant.java          # 常量定义
│   ├── XxxProperties.java        # 配置属性
│   └── ...
├── controller/                    # Web 控制器层
│   └── XxxController.java
├── convert/                       # MapStruct 对象转换
│   ├── XxxConvert.java           # 转换接口（@Mapper）
│   └── CommonConvert.java        # 公共转换
├── exception/                     # 自定义异常
│   └── XxxException.java
├── manager/                       # 数据查询封装层
│   ├── XxxManager.java           # Manager 接口
│   └── impl/                     # Manager 实现
│       └── XxxManagerImpl.java
├── mapper/                        # MyBatis Mapper 接口
│   └── XxxMapper.java
├── pojo/                          # 领域对象
│   ├── dto/                      # DTO（数据传输对象）
│   │   └── XxxDTO.java
│   ├── emuns/                    # 枚举类型
│   │   └── XxxStatusEnum.java
│   ├── entity/                   # Entity（数据库实体）
│   │   └── Xxx.java
│   ├── excel/                    # Excel 导入导出对象
│   │   └── XxxExcel.java
│   ├── param/                    # 请求参数（按操作细分）
│   │   ├── XxxCreateParam.java
│   │   ├── XxxPageParam.java
│   │   └── XxxUpdateParam.java
│   └── vo/                       # VO（视图对象）
│       └── XxxVO.java
└── service/                       # Service 层（业务逻辑）
    └── XxxService.java           # 只有接口，无 impl 目录

src/main/resources/
└── mapper/                        # MyBatis XML 映射文件
    └── {service}/
        └── XxxMapper.xml
```

### 分层异常处理规约

| 层级           | 异常处理方式                                                                |
|--------------|-----------------------------------------------------------------------|
| DAO/Mapper 层 | 产生的异常使用 `catch(Exception e)` 方式，并 `throw new DAOException(e)`，不需要打印日志 |
| Manager 层    | 与 Service 同机部署时异常处理方式与 DAO 层一致；单独部署时采用与 Service 一致的处理方式               |
| Service 层    | 出现异常时必须记录出错日志到磁盘，尽可能带上参数信息，相当于保护案发现场                                  |
| Controller 层 | 绝不应该继续往上抛异常，应转换成用户可以理解的错误提示，跳转到友好错误页面                                 |
| API 实现层      | 将异常处理成错误码和错误信息方式返回                                                    |

### 对象转换规范（MapStruct）

1. **Convert 接口定义**：
    - 使用 `@Mapper` 注解，配置 `componentModel = "spring"`
    - 单例模式：`XxxConvert.INSTANCE` 方式调用
    - 转换接口位于 `convert/` 目录

2. **转换规则**：
    - Controller 层：Param → DTO（入参）、DTO → VO（出参）
    - 相同属性名自动映射，不同属性名使用 `@Mapping` 注解指定
    - 复杂转换使用自定义 default 方法

3. **示例**：
   ```java
   @Mapper(componentModel = "spring")
   public interface OrgUserConvert {
       OrgUserConvert INSTANCE = Mappers.getMapper(OrgUserConvert.class);
       
       OrgUserDTO toDTO(OrgUserCreateParam param);
       
       OrgUserVO toVO(OrgUserDTO dto);
       
       List<OrgUserVO> toVOList(List<OrgUserDTO> dtoList);
   }
   ```

### 编码实践规范

1. **Controller 编码规范**：
    - 使用 `@RequiredArgsConstructor` 进行依赖注入
    - 使用 `BASE_NAME` 常量定义模块名称，用于接口文档
    - 增删改操作调用 Service 层，查询操作可直接调用 Manager 层
    - 分页查询返回 `Paging<VO>` 对象

2. **Service 编码规范**：
    - 使用 `@Slf4j` 记录业务日志
    - 业务方法命名：`addXxx()`, `deleteXxx()`, `updateXxx()`
    - 处理业务异常和成功后的后续操作（如发送消息）
   - **严禁直接引用 Mapper 层**：Service 层必须通过 Manager 层进行数据操作，禁止直接注入 Mapper

3. **Manager 编码规范**：
    - 继承 `BaseService<Entity>` 获取基础 CRUD 能力
    - 方法命名：`add()`, `delete()`, `modify()`, `getDtoById()`, `getPage()`
    - 返回值为 boolean 表示操作是否成功

4. **数据查询规范（强制）**：
    - **严禁在代码中直接编写 SQL 语句**（包括原生 SQL、XML Mapper 中的 SQL 等）
    - **必须使用 MyBatis-Plus + mybatis-plus-join 在 Manager 层实现数据查询**
    - 仅当 MyBatis-Plus 组件无法实现的复杂查询（如复杂报表、统计分析等），才允许编写自定义 SQL
    - 所有查询逻辑统一封装在 Manager 层，Service 层通过 Manager 获取数据

5. **Redis 缓存规范（强制）**：
    - **所有 Redis 缓存操作必须统一在 Manager 层实现**
    - Service 层禁止直接操作 Redis，必须通过 Manager 层进行缓存数据的读写
    - 缓存 key 的命名规范：`{模块名}:{业务名}:{标识}`，如 `org:user:123`
    - 缓存注解（如 `@Cacheable`、`@CacheEvict`）统一应用在 Manager 层方法上

6. **Param 细分规范**：
   ```
   XxxCreateParam   - 新增操作（@Validated 校验）
   XxxUpdateParam   - 更新操作（包含 ID 字段）
   XxxPageParam     - 分页查询（继承分页基类）
   ```

### Feign 接口规范

1. **接口定义**（API 模块）：
    - 使用 `@FeignClient` 注解，配置 `contextId` 避免 Bean 名称冲突
    - `path` 属性以 `/api` 开头，如 `/api/org/user`
    - 配置 `fallbackFactory` 实现熔断降级

2. **接口实现**（BUS 模块）：
    - 实现 Feign 接口，使用 `@RestController` 暴露 REST 端点
    - `RequestMapping` 路径与 Feign 接口 `path` 保持一致
    - 使用 `@Tag` 标注 Swagger 文档名称

3. **熔断降级**：
    - 每个 Feign 接口都有对应的 FallbackFactory
    - Fallback 实现中返回降级后的默认数据或抛出异常

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
    <artifactId>carlos-spring-boot-core</artifactId>
</dependency>

<!-- 错误：硬编码版本 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-core</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 模块命名规范

| 模块类型                | 命名格式                                                      | 示例                                       |
|---------------------|-----------------------------------------------------------|------------------------------------------|
| Commons 模块          | `carlos-{function}`                                       | `carlos-spring-boot-core`、`carlos-utils` |
| Spring Boot Starter | `carlos-spring-boot-starter-{function}`                   | `carlos-spring-boot-starter-redis`       |
| License 模块          | `carlos-license-*`、`carlos-spring-boot-starter-license-*` | `carlos-license-core`                    |

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

### Spring Boot 属性注入规范（强制）

**禁止使用 `@Value` 注解进行属性注入，统一使用 `@ConfigurationProperties` 的 Properties 类。**

```java
// ❌ 错误：使用 @Value 注解
@Value("${carlos.redis.host}")
private String redisHost;

@Value("${carlos.redis.port:6379}")
private int redisPort;

// ✅ 正确：使用 Properties 类
@ConfigurationProperties(prefix = "carlos.redis")
@Data
public class RedisProperties {
    private String host;
    private int port = 6379;
}

// 在配置类中注入
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {
    private final RedisProperties properties;
    
    public RedisAutoConfiguration(RedisProperties properties) {
        this.properties = properties;
    }
}

// 或者在业务类中注入
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisProperties properties;
}
```

**规范说明：**

1. **类型安全**: Properties 类提供编译时类型检查，`@Value` 在运行时才可能发现类型错误
2. **IDE 支持**: Properties 类可以被 IDE 识别，支持自动补全和重构
3. **默认值**: 在 Properties 类中使用字段初始化设置默认值，比 `@Value` 的 `:` 语法更清晰
4. **批量注入**: 相关配置可以集中在一个 Properties 类中，避免分散的 `@Value` 注解
5. **验证支持**: 可以在 Properties 类上使用 `@Validated` 进行配置校验
6. **可测试性**: Properties 类易于在单元测试中构造和注入

### Lombok 使用规范（强制）

**所有 POJO 类必须使用 Lombok 注解生成 getter/setter 方法，禁止手写 get/set 方法。**

```java
// ❌ 错误：手写 get/set 方法
public class UserDTO {
    private Long id;
    private String name;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}

// ✅ 正确：使用 Lombok 注解
@Data
public class UserDTO {
    private Long id;
    private String name;
}

// ✅ 正确：使用 @Getter/@Setter（需要精细控制时）
@Getter
@Setter
public class UserDTO {
    private Long id;
    
    // 只读字段，不生成 setter
    @Setter(AccessLevel.NONE)
    private String name;
}
```

**常用 Lombok 注解：**

| 注解                         | 用途                                        | 适用场景                    |
|----------------------------|-------------------------------------------|-------------------------|
| `@Data`                    | 生成 getter、setter、toString、equals、hashCode | POJO 类、DTO、VO、Param 等   |
| `@Getter`                  | 只生成 getter                                | 只读对象或需要精细控制时            |
| `@Setter`                  | 只生成 setter                                | 特殊场景                    |
| `@NoArgsConstructor`       | 生成无参构造器                                   | 需要无参构造的场景               |
| `@AllArgsConstructor`      | 生成全参构造器                                   | 配合 @Builder 使用          |
| `@RequiredArgsConstructor` | 生成包含 final 字段的构造器                         | Service、Controller 依赖注入 |
| `@Builder`                 | 生成 Builder 模式代码                           | 构建复杂对象                  |
| `@Slf4j`                   | 生成日志对象                                    | 需要记录日志的类                |
| `@EqualsAndHashCode`       | 生成 equals 和 hashCode                      | 需要自定义比较逻辑时              |

**规范说明：**

1. **代码整洁**: 避免样板代码，提高可读性
2. **易于维护**: 修改字段时无需同步修改 get/set 方法
3. **减少错误**: 避免手写 get/set 时的拼写错误或逻辑错误
4. **IDE 兼容**: 所有主流 IDE 都支持 Lombok 插件
5. **构建工具**: 项目已配置 Lombok 依赖，无需额外引入

## 编程规约

### 命名风格

| 类型           | 规范                                                | 示例                                               |
|--------------|---------------------------------------------------|--------------------------------------------------|
| 类名           | UpperCamelCase，抽象类以 Abstract 开头，异常类以 Exception 结尾 | `UserService`, `AbstractService`, `BizException` |
| 方法名/参数名/成员变量 | lowerCamelCase                                    | `getUserById()`, `userName`, `localValue`        |
| 常量           | 全大写，下划线分隔                                         | `MAX_STOCK_COUNT`, `CACHE_EXPIRED_TIME`          |
| 包名           | 全小写，单数形式，点分隔符间仅一个单词                               | `com.carlos.user.service`                        |
| 布尔变量         | 不加 is 前缀（POJO 类）                                  | `deleted`（非 `isDeleted`）                         |

**强制规定：**

- 代码命名严禁以下划线或美元符号开始/结束（如 `_name`, `name$`）
- 严禁使用拼音与英文混合方式命名
- 类型与中括号紧挨相连表示数组：`int[] arrayDemo`
- 枚举类名带 Enum 后缀，成员全大写：`ProcessStatusEnum.SUCCESS`

### 领域模型命名

| 模型类型                       | 命名规约       | 说明                               |
|----------------------------|------------|----------------------------------|
| DO (Data Object)           | `xxx`      | 与数据库表结构一一对应，位于 `entity` 包        |
| DTO (Data Transfer Object) | `xxxDTO`   | 服务层向外传输的对象，位于 `dto` 包            |
| VO (View Object)           | `xxxVO`    | 显示层对象，位于 `vo` 包，需标注 Swagger 注解   |
| Param                      | `xxxParam` | 前端参数接收对象，位于 `param` 包            |
| AO (Api Object)            | `xxxAO`    | API 接口响应对象                       |
| Enum                       | `xxxEnum`  | 枚举类型，位于 `enums` 包，需实现 `BaseEnum` |

### 常量定义

1. **不要使用魔法值**（未经预先定义的常量）直接出现在代码中
2. **long/Long 赋值**：数值后使用大写 `L`（如 `2L` 而非 `2l`）
3. **常量归类**：按功能分开维护（`CacheConsts`, `ConfigConsts`）
4. **枚举使用**：固定范围内变化的值使用 enum 类型定义

### 枚举类规范

所有业务枚举类必须遵循以下统一规范：

#### 1. 命名规范

- **类名**：使用 `XxxEnum` 格式，以 `Enum` 后缀结尾
- **包路径**：位于 `com.carlos.{module}.pojo.enums` 包下
- **枚举值**：全大写，下划线分隔（如 `NUMBER`, `STRING_TYPE`）

#### 2. 标准模板

```java
package com.carlos.{module}.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * {功能描述}枚举
 * </p>
 *
 * @author {author}
 * @date {date}
 */
@AppEnum(code = "{EnumCode}")
@Getter
@AllArgsConstructor
public enum {EnumName}Enum implements BaseEnum {

    /**
     * {描述1}
     */
    VALUE_ONE(1, "{描述1}"),
    /**
     * {描述2}
     */
    VALUE_TWO(2, "{描述2}");

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
```

#### 3. 必备注解说明

| 注解                       | 说明                           | 必填 |
|--------------------------|------------------------------|----|
| `@AppEnum(code = "xxx")` | 标记为应用枚举，用于反射扫描，code 作为字典唯一标识 | 是  |
| `@Getter`                | Lombok 自动生成 getter 方法        | 是  |
| `@AllArgsConstructor`    | Lombok 生成全参构造器               | 是  |
| `@EnumValue`             | MyBatis-Plus 注解，标记数据库存储字段    | 是  |

#### 4. 必须实现的接口

- **实现 `BaseEnum` 接口**：提供 `getCode()` 和 `getDesc()` 方法
- **code 字段**：使用 `@EnumValue` 注解，类型为 `Integer`，用于数据库存储
- **desc 字段**：类型为 `String`，表示枚举描述

#### 5. 示例参考

```java
@AppEnum(code = "DictType")
@Getter
@AllArgsConstructor
public enum DictTypeEnum implements BaseEnum {

    NUMBER(1, "数值类型"),
    STRING(2, "字符串类型");

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
```

#### 6. 注意事项

- 枚举类必须实现 `BaseEnum` 接口，确保框架能正确处理枚举序列化/反序列化
- `@AppEnum` 的 `code` 属性值在同一应用中必须唯一，用于前端字典接口识别
- `@EnumValue` 注解的字段将用于 MyBatis-Plus 的数据库存储和读取
- 禁止在枚举类中添加与业务无关的字段或方法
- 如需扩展枚举属性，请先与团队确认并更新本规范

### 代码格式

1. **大括号规范**：
    - 左大括号前不换行，后换行
    - 右大括号前换行，后还有 else 等代码则不换行
    - 终止的右大括号后必须换行

2. **空格规范**：
    - 左小括号和右边字符之间无空格，右小括号和左边字符之间无空格
    - `if/for/while/switch/do` 等保留字与括号之间必须加空格
    - 任何二目、三目运算符的左右两边都需要加一个空格

3. **缩进**：采用 4 个空格缩进，禁止使用 tab 字符

4. **注释**：
    - 双斜线与注释内容之间有且仅有一个空格：`// 这是注释`
    - 类、类属性、类方法必须使用 Javadoc 规范 `/**内容*/`
    - 方法内部单行注释使用 `//`，多行注释使用 `/* */`

5. **行长度**：单行字符数限制不超过 120 个，超出需要换行

6. **方法行数**：单个方法的总行数不超过 80 行

### OOP 规约

1. **静态访问**：避免通过对象引用访问静态变量/方法，直接用类名访问
2. **覆写注解**：所有覆写方法必须加 `@Override` 注解
3. **equals 使用**：使用常量或确定有值的对象调用 `equals`，推荐使用 `Objects.equals()`
4. **包装类型**：所有 POJO 类属性、RPC 方法返回值和参数必须使用包装数据类型
5. **BigDecimal**：
    - 禁止使用 `BigDecimal(double)` 构造方法，使用 `BigDecimal(String)` 或 `BigDecimal.valueOf()`
    - 任何货币金额，均以最小货币单位且整型类型进行存储
6. **toString**：POJO 类必须写 `toString` 方法，继承类需调用 `super.toString()`
7. **final 使用**：
    - 不允许被继承的类、不允许修改引用的域对象、不允许被覆写的方法
    - 避免上下文重复使用一个变量

### 日期时间

1. **日期格式化**：
    - 年份统一使用小写 `y`：`yyyy-MM-dd HH:mm:ss`
    - 月份大写 `M`，分钟小写 `m`，24小时制大写 `H`，12小时制小写 `h`

2. **获取当前毫秒数**：使用 `System.currentTimeMillis()` 而非 `new Date().getTime()`

3. **禁止使用**：`java.sql.Date`, `java.sql.Time`, `java.sql.Timestamp`

4. **JDK8 推荐**：使用 `Instant` 代替 `Date`，`LocalDateTime` 代替 `Calendar`，`DateTimeFormatter` 代替 `SimpleDateFormat`

### 集合处理

1. **hashCode 和 equals**：只要重写 `equals`，就必须重写 `hashCode`
2. **空判断**：使用 `isEmpty()` 方法而非 `size() == 0`
3. **toMap 转换**：使用 `Collectors.toMap()` 时必须处理 key 重复和 value 为 null 的情况
4. **subList**：不可强转成 `ArrayList`，对父集合的修改会导致子列表异常
5. **foreach**：不要在 foreach 循环里进行元素的 remove/add 操作，使用 `Iterator`
6. **初始化**：集合初始化时指定初始值大小

### 并发处理

1. **线程名称**：创建线程或线程池时指定有意义的线程名称
2. **线程池**：
    - 线程资源必须通过线程池提供，不允许显式创建线程
    - 不允许使用 `Executors` 创建，必须通过 `ThreadPoolExecutor` 方式
3. **ThreadLocal**：必须回收自定义的 ThreadLocal 变量，使用 try-finally 块
4. **锁**：
    - 能用无锁数据结构，就不要用锁
    - 能锁区块，就不要锁整个方法体
    - 高并发时，避免使用"等于"判断作为中断或退出条件
5. **SimpleDateFormat**：线程不安全，不要定义为 static，或使用 `DateTimeFormatter`

### 控制语句

1. **switch**：
    - 每个 case 必须通过 break/return 终止，或注释说明继续执行到哪个 case
    - 必须包含 default 语句放在最后
    - String 类型的 switch 变量必须先进行 null 判断

2. **大括号**：`if/else/for/while/do` 语句中必须使用大括号，即使只有一行代码

3. **三目运算符**：注意表达式 1 和 2 类型对齐时可能抛出 NPE 异常

4. **嵌套层数**：避免超过 3 层的 if-else 嵌套，可使用卫语句、策略模式等重构

## 异常处理规范

### 异常类使用规范（强制）

**严禁在业务代码中直接使用 `RuntimeException` 或 `Exception` 抛出异常！**

必须使用框架定义的异常类或模块自定义异常：

#### 1. Core 模块异常类（位于 `com.carlos.core.exception` 包）

| 异常类                  | 使用场景               | 示例                                        |
|----------------------|--------------------|-------------------------------------------|
| `GlobalException`    | 全局异常基类，一般不直接使用     | 继承使用                                      |
| `ServiceException`   | **业务层异常，最常用的业务异常** | `throw new ServiceException("用户不存在")`     |
| `DaoException`       | DAO/Mapper 层异常     | `throw new DaoException(e)`               |
| `RestException`      | REST 接口层异常         | `throw new RestException("请求参数错误")`       |
| `ComponentException` | 组件异常               | `throw new ComponentException("组件初始化失败")` |

#### 2. 模块自定义异常（推荐按业务场景定义）

各业务模块应在 `com.carlos.{module}.exception` 包下根据**具体业务场景**定义异常类，继承 `GlobalException`：

**推荐做法：按业务场景定义具体异常类**

```java
// ✅ 推荐：为不同业务场景定义具体异常类
package com.carlos.org.exception;

// 用户不存在异常
public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(String userId) {
        super("用户不存在：" + userId);
    }
}

// 账号已存在异常
public class AccountExistsException extends GlobalException {
    public AccountExistsException(String account) {
        super("账号已存在：" + account);
    }
}

// 密码异常
public class PasswordException extends GlobalException {
    public PasswordException(String message) {
        super(message);
    }
}

// 用户状态异常
public class InvalidUserStateException extends GlobalException {
    public InvalidUserStateException(String message) {
        super(message);
    }
}
```

**不推荐：一个模块只有一个通用异常类**

```java
// ❌ 不推荐：所有业务都用同一个异常类
throw new OrgModuleException("用户不存在");
throw new OrgModuleException("账号已存在");
throw new OrgModuleException("密码错误");
```

**使用示例：**

```java
// ✅ 正确：根据业务场景抛出具体异常
throw new UserNotFoundException(userId);
throw new AccountExistsException(account);
throw new PasswordException("旧密码不正确");
throw new InvalidUserStateException("用户已锁定");
```

#### 3. 使用示例

```java
// ✅ 正确：使用 ServiceException
throw new ServiceException("用户不存在");

// ✅ 正确：使用模块自定义异常
throw new OrgModuleException("部门不存在");

// ❌ 错误：严禁使用 RuntimeException
throw new RuntimeException("用户不存在");
```

#### 4. 分层异常抛出规范

| 层级         | 推荐异常类型                            |
|------------|-----------------------------------|
| Controller | 不捕获异常，统一由全局异常处理                   |
| Service    | `ServiceException` 或模块自定义异常       |
| Manager    | `ServiceException`、`DaoException` |
| Mapper/DAO | `DaoException`                    |

### Service 层与 Controller 层数据转换规范

#### 1. Service 层禁止返回 VO 类（强制）

**Service 层只能返回 DTO 或 Entity，Controller 层使用 Convert 进行 DTO 到 VO 的转换。**

```java
// ❌ 错误：Service 层返回 VO
@Service
public class OrgUserService {
    public OrgUserVO getUser(Long id) {  // 错误！
        // ...
    }
}

// ✅ 正确：Service 层返回 DTO，Controller 层转换
@Service
public class OrgUserService {
    public OrgUserDTO getUser(Long id) {  // 正确！
        // ...
    }
}

@RestController
public class OrgUserController {
    public OrgUserVO getUser(Long id) {
        OrgUserDTO dto = userService.getUser(id);
        return OrgUserConvert.INSTANCE.toVO(dto);  // Controller 层转换
    }
}
```

#### 2. 主键类型规范（强制）

**主键在 Entity 和 DTO 中使用 `Long` 类型，方法签名中使用 `Serializable` 类型，需要转换时使用 `cn.hutool.core.convert.Convert` 类。**

```java
// Entity 中使用 Long
public class OrgUser {
    private Long id;  // Long 类型
}

// Controller/Service 方法签名使用 Serializable
public OrgUserDTO getById(Serializable id) {  // 方法签名使用 Serializable
    // 如需转换
    Long userId = Convert.toLong(id);
    // ...
}
```

#### 3. 枚举类型规范（强制）

**字段值为枚举类型的，必须定义枚举类，DTO 和 Entity 中必须使用枚举类。**

```java
// 1. 定义枚举类
@AppEnum(code = "OrgUserState")
@Getter
@AllArgsConstructor
public enum OrgUserStateEnum implements BaseEnum {
    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    LOCK(2, "锁定");

    @EnumValue
    private final Integer code;
    private final String desc;
}

// 2. Entity 中使用枚举
public class OrgUser {
    private OrgUserStateEnum state;  // 使用枚举类
    private OrgUserGenderEnum gender;
}

// 3. DTO 中使用枚举
public class OrgUserDTO {
    private OrgUserStateEnum state;  // 使用枚举类
    private OrgUserGenderEnum gender;
}
```

#### 4. 逻辑删除规范（强制）

**Manager 中逻辑删除不需要显式设置 deleted 字段，框架会自动处理。**

```java
// ❌ 错误：显式设置 deleted 字段
public boolean logicDelete(Serializable id) {
    OrgUser entity = new OrgUser();
    entity.setId(Convert.toLong(id));
    entity.setDeleted(true);  // 不需要显式设置！
    return updateById(entity);
}

// ✅ 正确：直接使用 removeById，框架自动处理逻辑删除
public boolean delete(Serializable id) {
    return removeById(id);  // 框架自动处理逻辑删除
}
```

## 异常日志

### 错误码规约

1. **格式**：字符串类型，共 5 位（错误产生来源 + 四位数字编号）
    - A 开头：错误来源于用户（参数错误等）
    - B 开头：错误来源于当前系统（业务逻辑错误）
    - C 开头：错误来源于第三方服务

2. **成功码**：`00000`

3. **使用**：错误码不能直接输出给用户作为提示信息使用

### 异常处理

1. **RuntimeException**：`NullPointerException`、`IndexOutOfBoundsException` 等不应通过 catch 处理，应通过预检查规避
2. **异常用途**：异常不要用来做流程控制、条件控制
3. **catch 范围**：区分稳定代码和非稳定代码，对大段代码进行 try-catch 是不负责任的
4. **处理要求**：捕获异常是为了处理它，如果不想处理，请抛给调用者
5. **事务**：事务场景中抛出异常被 catch 后，如需回滚，注意手动回滚事务
6. **finally**：
    - 必须对资源对象、流对象进行关闭
    - 不要在 finally 块中使用 return
7. **RPC 调用**：调用 RPC、二方包或动态生成类的方法时，捕捉异常必须使用 `Throwable`

### 日志规约

1. **日志框架**：应用中不可直接使用日志系统（Log4j2、Logback）的 API，应依赖使用日志框架（SLF4J）的 API，推荐使用 Lombok 的 `@Slf4j` 注解

2. **日志输出**：
    - 字符串变量之间的拼接使用占位符方式：`logger.debug("Processing trade with id: {}", id)`
    - 对 trace/debug/info 级别的日志输出，必须进行日志级别的开关判断

3. **生产环境**：
    - 禁止直接使用 `System.out` 或 `System.err` 输出日志
    - 禁止直接使用 `e.printStackTrace()` 打印异常堆栈
    - 生产环境禁止输出 debug 日志，有选择地输出 info 日志

4. **异常日志**：异常信息应该包括案发现场信息和异常堆栈信息：`logger.error("参数:{}_{}", param, e.getMessage(), e)`

5. **日志保留**：所有日志文件至少保存 15 天

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

## 安全规约

1. **权限控制**：隶属于用户个人的页面或者功能必须进行权限控制校验
2. **数据脱敏**：用户敏感数据禁止直接展示，必须对展示数据进行脱敏（如手机号 `137****0969`）
3. **SQL 注入**：用户输入的 SQL 参数严格使用参数绑定或 `#{ }`，禁止 `${ }` 拼接 SQL
4. **参数校验**：用户请求传入的任何参数必须做有效性验证（page size 限制、order by 校验等）
5. **XSS 防护**：禁止向 HTML 页面输出未经安全过滤或未正确转义的用户数据
6. **CSRF**：表单、AJAX 提交必须执行 CSRF 安全验证
7. **防重放**：使用平台资源（短信、邮件、电话、下单、支付）必须实现防重放机制
8. **防刷策略**：发贴、评论、发送即时消息等场景必须实现防刷、文本内容违禁词过滤

## MySQL 数据库规约

### 建表规约

1. **命名**：
    - 表达是与否概念的字段，使用 `is_xxx` 的方式命名，数据类型 `tinyint unsigned`（1 表示是，0 表示否）
    - 表名、字段名必须使用**小写字母或数字**，禁止出现数字开头
    - 表名不使用复数名词
    - 禁用保留字（desc、range、match、delayed 等）

2. **索引命名**：
    - 主键索引：`pk_字段名`
    - 唯一索引：`uk_字段名`
    - 普通索引：`idx_字段名`

3. **数据类型**：
    - 小数类型为 `decimal`，禁止使用 `float` 和 `double`
    - 如果存储的字符串长度几乎相等，使用 `char` 定长字符串类型
    - `varchar` 长度不要超过 5000，超过则使用 `text`，独立出来一张表

4. **必备字段**：`id`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`
    - `id` 为主键，类型 `char(32)`，生成方式 `IdUtils.date32Id()`

5. **分库分表**：单表行数超过 500 万行或单表容量超过 2GB，才推荐进行分库分表

### 索引规约

1. **唯一索引**：业务上具有唯一特性的字段，即使是组合字段，也必须建成唯一索引
2. **join 限制**：超过三个表禁止 join，需要 join 的字段数据类型保持绝对一致
3. **索引长度**：在 `varchar` 字段上建立索引时，必须指定索引长度（一般 20 长度区分度达 90% 以上）
4. **模糊查询**：页面搜索严禁左模糊或者全模糊，需要请走搜索引擎
5. **order by**：利用索引的有序性，order by 最后的字段是组合索引的一部分，放在索引组合顺序的最后

### SQL 语句

1. **count**：不要使用 `count(列名)` 或 `count(常量)` 来替代 `count(*)`,使用 `ISNULL()` 来判断是否为 NULL 值
3. **分页**：代码中写分页查询逻辑时，若 count 为 0 应直接返回
4. **外键**：不得使用外键与级联，一切外键概念必须在应用层解决
5. **存储过程**：禁止使用存储过程，存储过程难以调试和扩展，更没有移植性
6. **数据订正**：数据订正时，要先 select，避免出现误删除
7. **别名**：多表查询时，需要在列名前加表的别名（或表名）进行限定

### ORM 映射

1. **查询字段**：在表查询中，一律不要使用 `*` 作为查询的字段列表，需要哪些字段必须明确写明
2. **布尔映射**：POJO 类的布尔属性不能加 is，而数据库字段必须加 `is_`，要求在 resultMap 中进行映射
3. **resultMap**：不要用 `resultClass` 当返回参数，即使所有类属性名与数据库字段一一对应，也需要定义 `<resultMap>`
4. **参数**：`sql.xml` 配置参数使用 `#{ }`，不要使用 `${ }` 防止 SQL 注入
5. **更新数据**：更新数据表记录时，必须同时更新记录对应的 `update_time` 字段值为当前时间

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

1. **核心模块必测**: carlos-spring-boot-core、carlos-spring-boot-starter-mybatis、carlos-auth
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
- ❌ Service 层直接引用 Mapper：Service 必须通过 Manager 层进行数据操作，严禁直接注入 Mapper

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

- 核心模块: `carlos-commons/carlos-spring-boot-core/README.md`
- 父模块说明: `carlos-parent/README.md`
- License 文档: `carlos-integration/carlos-license/README.md`
- OAuth2 文档: `carlos-spring-boot/carlos-spring-boot-starter-oauth2/README.md`
- 工具模块: `carlos-integration/carlos-tools/README.md`
- 优化报告: `OPTIMIZATION_REPORT.md`

---

**注意**: 本框架仅供内部使用，请勿开源或外传。

**版本**: 3.0.0-SNAPSHOT | **Spring Boot**: 3.5.9 | **JDK**: 17+
