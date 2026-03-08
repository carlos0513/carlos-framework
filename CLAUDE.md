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
cd carlos-commons/carlos-spring-boot-core
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
│   ├── carlos-spring-boot-core/                      # 核心抽象、异常、分页
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

- `carlos-spring-boot-core`: 基础抽象、注解、AOP、异常、分页、响应包装器
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

### 应用分层架构

服务采用六层分层架构，职责清晰分离：

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

**分层领域模型：**

| 模型         | 名称       | 所在包           | 说明                                                                |
|------------|----------|---------------|-------------------------------------------------------------------|
| **Param**  | 参数对象     | `pojo.param`  | 前端参数接收对象，按操作细分：`XxxCreateParam`, `XxxUpdateParam`, `XxxPageParam` |
| **DTO**    | 数据传输对象   | `pojo.dto`    | 服务层与数据层之间传输的对象                                                    |
| **VO**     | 视图对象     | `pojo.vo`     | 显示层对象，响应给前端，需标注 Swagger 注解                                        |
| **AO**     | API 对象   | `pojo.ao`     | API 接口响应对象，用于 Feign 调用方                                           |
| **Entity** | 实体对象     | `pojo.entity` | 与数据库表结构一一对应，DO（Data Object）                                       |
| **Excel**  | Excel 对象 | `pojo.excel`  | 导入导出专用对象                                                          |
| **Enum**   | 枚举       | `pojo.enums`  | 业务枚举类型，需实现 `BaseEnum` 接口，使用 `@AppEnum` 注解标记                       |

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

**枚举类规范：**

所有业务枚举类必须遵循统一规范，以 `DictTypeEnum` 为标准模板：

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

**必备要素：**

- 类名以 `Enum` 结尾（如 `DictTypeEnum`），位于 `pojo.enums` 包下
- 必须实现 `BaseEnum` 接口，提供 `getCode()` 和 `getDesc()` 方法
- 必须添加 `@AppEnum(code = "xxx")` 注解，code 值在同一应用中唯一，用于前端字典接口
- 必须添加 `@Getter` 和 `@AllArgsConstructor` Lombok 注解
- `code` 字段必须标注 `@EnumValue` 注解，类型为 `Integer`，用于 MyBatis-Plus 数据库存储
- `desc` 字段类型为 `String`，表示枚举描述
- 枚举值全大写，下划线分隔（如 `NUMBER`, `STRING_TYPE`）

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
│   ├── enums/                    # 枚举类型（需实现 BaseEnum 接口）
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

### 编程规约

#### 命名风格

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

#### 代码格式

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

#### OOP 规约

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

#### 日期时间

1. **日期格式化**：
    - 年份统一使用小写 `y`：`yyyy-MM-dd HH:mm:ss`
    - 月份大写 `M`，分钟小写 `m`，24小时制大写 `H`，12小时制小写 `h`

2. **获取当前毫秒数**：使用 `System.currentTimeMillis()` 而非 `new Date().getTime()`

3. **禁止使用**：`java.sql.Date`, `java.sql.Time`, `java.sql.Timestamp`

4. **JDK8 推荐**：使用 `Instant` 代替 `Date`，`LocalDateTime` 代替 `Calendar`，`DateTimeFormatter` 代替 `SimpleDateFormat`

#### 集合处理

1. **hashCode 和 equals**：只要重写 `equals`，就必须重写 `hashCode`
2. **空判断**：使用 `isEmpty()` 方法而非 `size() == 0`
3. **toMap 转换**：使用 `Collectors.toMap()` 时必须处理 key 重复和 value 为 null 的情况
4. **subList**：不可强转成 `ArrayList`，对父集合的修改会导致子列表异常
5. **foreach**：不要在 foreach 循环里进行元素的 remove/add 操作，使用 `Iterator`
6. **初始化**：集合初始化时指定初始值大小

#### 并发处理

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

#### 控制语句

1. **switch**：
    - 每个 case 必须通过 break/return 终止，或注释说明继续执行到哪个 case
    - 必须包含 default 语句放在最后
    - String 类型的 switch 变量必须先进行 null 判断

2. **大括号**：`if/else/for/while/do` 语句中必须使用大括号，即使只有一行代码

3. **三目运算符**：注意表达式 1 和 2 类型对齐时可能抛出 NPE 异常

4. **嵌套层数**：避免超过 3 层的 if-else 嵌套，可使用卫语句、策略模式等重构

#### 文件组织

提倡"多而小"的文件，而非"少而大"的文件：

- 高内聚，低耦合
- 建议每文件 200-400 行，最大不超过 800 行
- 从大型组件中提取工具函数 (Utilities)
- 按功能/领域 (Feature/Domain) 组织，而非按类型 (Type) 组织

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

3. **Manager 编码规范**：
    - 继承 `BaseService<Entity>` 获取基础 CRUD 能力
    - 方法命名：`add()`, `delete()`, `modify()`, `getDtoById()`, `getPage()`
    - 返回值为 boolean 表示操作是否成功

4. **Param 细分规范**：
   ```
   XxxCreateParam   - 新增操作（@Validated 校验）
   XxxUpdateParam   - 更新操作（包含 ID 字段）
   XxxPageParam     - 分页查询（继承分页基类）
   ```

5. **属性注入规范（强制）**：
    - **禁止使用 `@Value` 注解进行属性注入，统一使用 `@ConfigurationProperties` 的 Properties 类**

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
   
   // 或在业务类中注入
   @Service
   @RequiredArgsConstructor
   public class RedisService {
       private final RedisProperties properties;
   }
   ```

   **优势**：类型安全、IDE 支持、默认值清晰、配置集中、易于测试

6. **Lombok 使用规范（强制）**：
    - **所有 POJO 类必须使用 Lombok 注解生成 getter/setter 方法，禁止手写 get/set 方法**

   ```java
   // ❌ 错误：手写 get/set 方法
   public class UserDTO {
       private Long id;
       private String name;
       
       public Long getId() { return id; }
       public void setId(Long id) { this.id = id; }
       public String getName() { return name; }
       public void setName(String name) { this.name = name; }
   }
   
   // ✅ 正确：使用 Lombok @Data
   @Data
   public class UserDTO {
       private Long id;
       private String name;
   }
   ```

   **推荐注解**：`@Data`、`@Getter`、`@Setter`、`@NoArgsConstructor`、`@AllArgsConstructor`、`@RequiredArgsConstructor`、
   `@Builder`、`@Slf4j`

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

### 添加新模块

**Spring Boot Starters：**

1. 在 `carlos-spring-boot/` 下创建模块
2. 在 `carlos-spring-boot/pom.xml` 中添加 `<module>` 条目
3. 设置父项目为 `carlos-spring-boot`，版本为 `${revision}`
4. 遵循命名约定: `carlos-spring-boot-starter-{function}`
5. 在 `carlos-dependencies/pom.xml` 中添加依赖管理条目

**微服务模块：**

1. 在 `carlos-integration/` 下创建目录 `{service-name}`
2. 创建三个子模块：
    - `{service-name}-api`: Feign 接口定义，供其他服务调用
    - `{service-name}-bus`: 业务逻辑实现
    - `{service-name}-boot`: 启动模块（可选，用于本地开发）
    - `{service-name}-cloud`: 云原生启动模块（可选，用于 Kubernetes 部署）
3. 参考 `carlos-org` 模块结构进行配置

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

- Commons 模块 (`carlos-spring-boot-core`, `carlos-utils`, `carlos-excel`) 是基础，与框架无关
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

#### 基本原则（AIR）

- **A**：Automatic（自动化）
- **I**：Independent（独立性）
- **R**：Repeatable（可重复）

#### 强制要求

1. 单元测试应该是全自动执行的，并且非交互式的，必须使用 `assert` 来验证
2. 单元测试用例之间决不能互相调用，也不能依赖执行的先后次序
3. 单元测试是可以重复执行的，不能受到外界环境的影响
4. 测试粒度至多是类级别，一般是方法级别
5. 核心业务、核心应用、核心模块的增量代码确保单元测试通过
6. 单元测试代码必须写在 `src/test/java`，不允许写在业务代码目录下

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

#### BCDE 原则

编写单元测试遵守 BCDE 原则：

- **B**：Border，边界值测试，包括循环边界、特殊取值、特殊时间点、数据顺序等
- **C**：Correct，正确的输入，并得到预期的结果
- **D**：Design，与设计文档相结合，来编写单元测试
- **E**：Error，强制错误信息输入（如：非法数据、异常流程、业务允许外等），并得到预期的结果

#### Mock 使用规范

- 使用 `@Mock` 和 `@InjectMocks` 创建 Mock 对象
- 使用 `@ExtendWith(MockitoExtension.class)` 启用 Mockito
- 明确验证 Mock 对象的交互行为
- 避免过度 Mock，只 Mock 外部依赖

#### 数据库测试

- 对于数据库相关的查询，更新，删除等操作，不能假设数据库里的数据是存在的
- 和数据库相关的单元测试，可以设定自动回滚机制，不给数据库造成脏数据
- 或者对单元测试产生的数据有明确的前后缀标识

### 安全规约

#### 强制安全检查 (提交前必须检查)

- [ ] 无硬编码凭据（API 密钥、密码、令牌/Tokens）
- [ ] 所有用户输入均已验证
- [ ] 预防 SQL 注入（使用参数化查询/MyBatis-Plus，使用 `#{ }` 而非 `${ }`）
- [ ] 预防 XSS（对 HTML 进行净化处理/Sanitized）
- [ ] 已启用 CSRF 保护
- [ ] 身份验证/授权已验证
- [ ] 所有端点均已设置速率限制（Rate limiting）
- [ ] 错误消息不泄露敏感数据

#### 安全规范详情

1. **权限控制**：隶属于用户个人的页面或者功能必须进行权限控制校验
2. **数据脱敏**：用户敏感数据禁止直接展示，必须对展示数据进行脱敏（如手机号 `137****0969`）
3. **SQL 注入**：用户输入的 SQL 参数严格使用参数绑定或 `#{ }`，禁止 `${ }` 拼接 SQL
4. **参数校验**：用户请求传入的任何参数必须做有效性验证（page size 限制、order by 校验等）
5. **XSS 防护**：禁止向 HTML 页面输出未经安全过滤或未正确转义的用户数据
6. **CSRF**：表单、AJAX 提交必须执行 CSRF 安全验证
7. **防重放**：使用平台资源（短信、邮件、电话、下单、支付）必须实现防重放机制
8. **防刷策略**：发贴、评论、发送即时消息等场景必须实现防刷、文本内容违禁词过滤

#### 凭据管理

```java
// 严禁：硬编码凭据
private String apiKey = "sk-proj-xxxxx";

// 严禁：使用 @Value 注解（不符合本项目规范）
@Value("${api.key}")
private String apiKey;

// ✅ 推荐：使用 Properties 类
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private String apiKey;
}

// 在启动时验证
@PostConstruct
public void init() {
    if (StringUtils.isEmpty(appProperties.getApiKey())) {
        throw new IllegalStateException("API_KEY not configured");
    }
}
```

### 异常类使用规范（强制）

**严禁在业务代码中直接使用 `RuntimeException` 或 `Exception` 抛出异常！**

必须使用框架定义的异常类或模块自定义异常：

#### 1. Core 模块异常类（位于 `com.carlos.core.exception` 包）

| 异常类                  | 使用场景               | 示例代码                                      |
|----------------------|--------------------|-------------------------------------------|
| `ServiceException`   | **业务层异常，最常用的业务异常** | `throw new ServiceException("用户不存在")`     |
| `DaoException`       | DAO/Mapper 层异常     | `throw new DaoException(e)`               |
| `RestException`      | REST 接口层异常         | `throw new RestException("请求参数错误")`       |
| `ComponentException` | 组件异常               | `throw new ComponentException("组件初始化失败")` |
| `GlobalException`    | 全局异常基类，一般不直接使用     | 用于自定义异常继承                                 |

#### 2. 模块自定义异常（按业务场景定义）

各业务模块应在 `com.carlos.{module}.exception` 包下根据**具体业务场景**定义异常类：

```java
// ✅ 推荐：按业务场景定义具体异常类
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
```

**使用示例：**

```java
// ✅ 正确：使用 ServiceException（通用业务异常）
throw new ServiceException("用户不存在");

// ✅ 正确：使用模块自定义异常
throw new OrgModuleException("部门不存在");

// ❌ 错误：严禁使用 RuntimeException
throw new RuntimeException("用户不存在");
```

#### 3. 分层异常抛出规范

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
    public OrgUserVO getUser(Long id) {  // 错误！Service 不能返回 VO
        // ...
    }
}

// ✅ 正确：Service 层返回 DTO，Controller 层转换
@Service
public class OrgUserService {
    public OrgUserDTO getUser(Long id) {  // 正确！返回 DTO
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

### 分层异常处理规约

| 层级           | 异常处理方式                                                                |
|--------------|-----------------------------------------------------------------------|
| DAO/Mapper 层 | 产生的异常使用 `catch(Exception e)` 方式，并 `throw new DAOException(e)`，不需要打印日志 |
| Manager 层    | 与 Service 同机部署时异常处理方式与 DAO 层一致；单独部署时采用与 Service 一致的处理方式               |
| Service 层    | 出现异常时必须记录出错日志到磁盘，尽可能带上参数信息，相当于保护案发现场                                  |
| Controller 层 | 绝不应该继续往上抛异常，应转换成用户可以理解的错误提示，跳转到友好错误页面                                 |
| API 实现层      | 将异常处理成错误码和错误信息方式返回                                                    |

### 错误码规约

1. **格式**：字符串类型，共 5 位（错误产生来源 + 四位数字编号）
    - A 开头：错误来源于用户（参数错误等）
    - B 开头：错误来源于当前系统（业务逻辑错误）
    - C 开头：错误来源于第三方服务

2. **成功码**：`00000`

3. **使用**：错误码不能直接输出给用户作为提示信息使用

### 日志规约

1. **日志框架**：应用中不可直接使用日志系统（Log4j2、Logback）的 API，应依赖使用日志框架（SLF4J）的 API，推荐使用 Lombok 的 `@Slf4j` 注解

2. **日志输出**：
    - 使用占位符方式：`logger.debug("Processing trade with id: {}", id)`
    - 对 trace/debug/info 级别的日志输出，必须进行日志级别的开关判断

3. **生产环境**：
    - 禁止直接使用 `System.out` 或 `System.err` 输出日志
    - 禁止直接使用 `e.printStackTrace()` 打印异常堆栈
    - 生产环境禁止输出 debug 日志，有选择地输出 info 日志

4. **异常日志**：异常信息应该包括案发现场信息和异常堆栈信息：`logger.error("参数:{}_{}", param, e.getMessage(), e)`

5. **日志保留**：所有日志文件至少保存 15 天

6. **日志级别**：
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

### MySQL 数据库规约

#### 建表规约

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

#### 索引规约

1. **唯一索引**：业务上具有唯一特性的字段，即使是组合字段，也必须建成唯一索引
2. **join 限制**：超过三个表禁止 join，需要 join 的字段数据类型保持绝对一致
3. **索引长度**：在 `varchar` 字段上建立索引时，必须指定索引长度（一般 20 长度区分度达 90% 以上）
4. **模糊查询**：页面搜索严禁左模糊或者全模糊，需要请走搜索引擎
5. **order by**：利用索引的有序性，order by 最后的字段是组合索引的一部分，放在索引组合顺序的最后

#### SQL 语句

1. **count**：不要使用 `count(列名)` 或 `count(常量)` 来替代 `count(*)`
2. **NULL 判断**：使用 `ISNULL()` 来判断是否为 NULL 值
3. **分页**：代码中写分页查询逻辑时，若 count 为 0 应直接返回
4. **外键**：不得使用外键与级联，一切外键概念必须在应用层解决
5. **存储过程**：禁止使用存储过程，存储过程难以调试和扩展，更没有移植性
6. **数据订正**：数据订正时，要先 select，避免出现误删除
7. **别名**：多表查询时，需要在列名前加表的别名（或表名）进行限定

#### ORM 映射

1. **查询字段**：在表查询中，一律不要使用 `*` 作为查询的字段列表，需要哪些字段必须明确写明
2. **布尔映射**：POJO 类的布尔属性不能加 is，而数据库字段必须加 `is_`，要求在 resultMap 中进行映射
3. **resultMap**：不要用 `resultClass` 当返回参数，即使所有类属性名与数据库字段一一对应，也需要定义 `<resultMap>`
4. **参数**：`sql.xml` 配置参数使用 `#{ }`，不要使用 `${ }` 防止 SQL 注入
5. **更新数据**：更新数据表记录时，必须同时更新记录对应的 `update_time` 字段值为当前时间

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
- 与 carlos-spring-boot-core 认证系统集成 (LoginUserInfo, UserContext)
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

- carlos-spring-boot-core (用户信息、异常)
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
