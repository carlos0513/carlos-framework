---
name: carlos-module-scaffolder
description: >
  Carlos Framework 业务模块脚手架生成器。新建业务模块（如 carlos-order、carlos-product）时调用。
  自动生成符合规范的 api/bus/boot/cloud 四层子模块完整骨架代码。
---

# Carlos Framework 模块脚手架生成

你是 Carlos Framework 的模块脚手架生成器，负责生成标准化的业务模块骨架代码。

## 生成流程

### 第一步：确认模块信息

收集以下参数后再生成：

- `{module}`: 模块名（如 `order`, `product`, `inventory`）
- `{Module}`: 模块名首字母大写（如 `Order`, `Product`）
- `{entity}`: 主要实体名（如 `order`, `product`）
- `{Entity}`: 实体名首字母大写（如 `Order`, `Product`）
- `{table}`: 数据库表名（如 `ord_order`, `prd_product`）
- `{description}`: 模块描述（如 `订单管理`, `商品管理`）

### 第二步：目录结构

```
carlos-integration/
└── carlos-{module}/
    ├── pom.xml                                 # 聚合 POM
    ├── carlos-{module}-api/
    │   ├── pom.xml
    │   └── src/main/java/com/carlos/{module}/
    │       ├── api/
    │       │   ├── Api{Entity}.java
    │       │   └── fallback/
    │       │       └── Api{Entity}FallbackFactory.java
    │       └── pojo/
    │           ├── ao/
    │           │   └── {Entity}AO.java
    │           └── param/
    │               └── Api{Entity}Param.java
    ├── carlos-{module}-bus/
    │   ├── pom.xml
    │   └── src/
    │       ├── main/
    │       │   ├── java/com/carlos/{module}/
    │       │   │   ├── config/
    │       │   │   ├── controller/
    │       │   │   │   └── {Entity}Controller.java
    │       │   │   ├── convert/
    │       │   │   │   └── {Entity}Convert.java
    │       │   │   ├── manager/
    │       │   │   │   ├── {Entity}Manager.java
    │       │   │   │   └── impl/
    │       │   │   │       └── {Entity}ManagerImpl.java
    │       │   │   ├── mapper/
    │       │   │   │   └── {Entity}Mapper.java
    │       │   │   ├── pojo/
    │       │   │   │   ├── dto/
    │       │   │   │   │   └── {Entity}DTO.java
    │       │   │   │   ├── entity/
    │       │   │   │   │   └── {Entity}.java
    │       │   │   │   ├── enums/
    │       │   │   │   ├── param/
    │       │   │   │   │   ├── {Entity}CreateParam.java
    │       │   │   │   │   ├── {Entity}UpdateParam.java
    │       │   │   │   │   └── {Entity}PageParam.java
    │       │   │   │   └── vo/
    │       │   │   │       └── {Entity}VO.java
    │       │   │   └── service/
    │       │   │       ├── {Entity}Service.java
    │       │   │       └── impl/
    │       │   │           └── {Entity}ServiceImpl.java
    │       │   └── resources/
    │       │       └── mapper/{module}/
    │       │           └── {Entity}Mapper.xml
    │       └── test/
    │           └── java/com/carlos/{module}/
    │               ├── manager/
    │               │   └── {Entity}ManagerImplTest.java
    │               └── service/
    │                   └── {Entity}ServiceImplTest.java
    ├── carlos-{module}-boot/
    │   ├── pom.xml
    │   └── src/main/
    │       ├── java/com/carlos/{module}/
    │       │   └── {Module}Application.java
    │       └── resources/
    │           └── application.yml
    └── carlos-{module}-cloud/
        ├── pom.xml
        └── src/main/
            ├── java/com/carlos/{module}/
            │   └── {Module}CloudApplication.java
            └── resources/
                └── bootstrap.yml
```

### 第三步：生成关键文件

#### 聚合 POM（`carlos-{module}/pom.xml`）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.carlos</groupId>
        <artifactId>carlos-integration</artifactId>
        <version>${revision}</version>
    </parent>

    <artifactId>carlos-{module}</artifactId>
    <packaging>pom</packaging>
    <name>carlos-{module}</name>
    <description>{description}</description>

    <modules>
        <module>carlos-{module}-api</module>
        <module>carlos-{module}-bus</module>
        <module>carlos-{module}-boot</module>
        <module>carlos-{module}-cloud</module>
    </modules>
</project>
```

#### Bus 模块 POM（`carlos-{module}-bus/pom.xml`）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.carlos</groupId>
        <artifactId>carlos-{module}</artifactId>
        <version>${revision}</version>
    </parent>

    <artifactId>carlos-{module}-bus</artifactId>
    <name>carlos-{module}-bus</name>

    <dependencies>
        <dependency>
            <groupId>com.carlos</groupId>
            <artifactId>carlos-{module}-api</artifactId>
            <version>${revision}</version>
        </dependency>
        <dependency>
            <groupId>com.carlos</groupId>
            <artifactId>carlos-spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.carlos</groupId>
            <artifactId>carlos-spring-boot-starter-mybatis</artifactId>
        </dependency>
        <dependency>
            <groupId>com.carlos</groupId>
            <artifactId>carlos-spring-boot-starter-redis</artifactId>
        </dependency>
    </dependencies>
</project>
```

#### 单体启动类（`{Module}Application.java`）

```java
@SpringBootApplication
@MapperScan("com.carlos.{module}.mapper")
public class {Module}Application {

    public static void main(String[] args) {
        SpringApplication.run({Module}Application.class, args);
    }
}
```

#### 微服务启动类（`{Module}CloudApplication.java`）

```java
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.carlos.{module}.mapper")
public class {Module}CloudApplication {

    public static void main(String[] args) {
        SpringApplication.run({Module}CloudApplication.class, args);
    }
}
```

#### bootstrap.yml

```yaml
spring:
  application:
    name: carlos-{module}
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_ADDR:127.0.0.1:8848}
        namespace: ${NACOS_NAMESPACE:}
      config:
        server-addr: ${NACOS_ADDR:127.0.0.1:8848}
        namespace: ${NACOS_NAMESPACE:}
        file-extension: yaml

carlos:
  boot:
    enums:
      scan-package: com.carlos.{module}
      enabled: true
```

### 第四步：注册模块

生成文件后，必须在以下位置注册新模块：

1. **`carlos-integration/pom.xml`** — 添加 `<module>carlos-{module}</module>`
2. **`carlos-dependencies/pom.xml`** — 添加依赖管理：

```xml
<!-- 在 dependencyManagement 中添加 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-{module}-api</artifactId>
    <version>${revision}</version>
</dependency>
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-{module}-bus</artifactId>
    <version>${revision}</version>
</dependency>
```

### 第五步：验证

```bash
# 验证新模块能正常编译
mvn clean install -pl carlos-integration/carlos-{module} -am -DskipTests

# 验证依赖树正常
mvn dependency:tree -pl carlos-integration/carlos-{module}/carlos-{module}-bus
```

## 生成后检查清单

- [ ] 聚合 POM 包含所有子模块声明
- [ ] 所有子模块 parent 使用 `${revision}`，无硬编码版本
- [ ] `carlos-integration/pom.xml` 已添加新模块
- [ ] `carlos-dependencies/pom.xml` 已添加依赖管理
- [ ] Bus 模块包含基础 CRUD 骨架（Manager/Service/Controller）
- [ ] 测试骨架文件已生成（`*Test.java`）
- [ ] MapStruct Convert 接口已生成
- [ ] Mapper XML 文件已创建（`resources/mapper/{module}/`）
- [ ] 构建验证通过
