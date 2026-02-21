# carlos-spring-boot-starter-json

JSON序列化组件，提供统一的JSON处理能力，支持Fastjson和Jackson。

## 功能特性

- **多引擎支持**: 支持Fastjson 2.x和Jackson
- **统一接口**: 提供统一的JSON工具类，屏蔽底层实现差异
- **自动配置**: Spring Boot自动配置Jackson序列化规则
- **类型安全**: 支持泛型反序列化
- **日期处理**: 统一的日期格式化配置
- **空值处理**: 灵活的null值处理策略

## 快速开始

### Maven依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-json</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 配置示例

```yaml
spring:
  jackson:
    # 日期格式
    date-format: yyyy-MM-dd HH:mm:ss
    # 时区
    time-zone: GMT+8
    # 序列化配置
    serialization:
      # 格式化输出
      indent-output: false
      # 忽略空Bean
      fail-on-empty-beans: false
    # 反序列化配置
    deserialization:
      # 忽略未知属性
      fail-on-unknown-properties: false
```

## 使用示例

### 对象转JSON

```java
@Autowired
private JacksonUtil jacksonUtil;

public void toJson() {
    User user = new User();
    user.setId(1L);
    user.setName("张三");

    // 转为JSON字符串
    String json = jacksonUtil.toJson(user);

    // 转为格式化的JSON字符串
    String prettyJson = jacksonUtil.toJsonPretty(user);
}
```

### JSON转对象

```java
public void fromJson() {
    String json = "{\"id\":1,\"name\":\"张三\"}";

    // 转为对象
    User user = jacksonUtil.fromJson(json, User.class);

    // 转为泛型对象
    List<User> users = jacksonUtil.fromJson(
        jsonArray,
        new TypeReference<List<User>>() {}
    );
}
```

### 使用Fastjson

```java
import com.alibaba.fastjson2.JSON;

public void useFastjson() {
    // 对象转JSON
    String json = JSON.toJSONString(user);

    // JSON转对象
    User user = JSON.parseObject(json, User.class);

    // JSON转List
    List<User> users = JSON.parseArray(jsonArray, User.class);
}
```

## Jackson配置说明

### 常用注解

```java
@Data
public class User {
    @JsonProperty("user_id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonIgnore
    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String remark;
}
```

### 自定义序列化器

```java
public class CustomSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen,
                         SerializerProvider serializers) throws IOException {
        gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
```

## Fastjson vs Jackson

| 特性  | Fastjson | Jackson |
|-----|----------|---------|
| 性能  | 更快       | 较快      |
| 功能  | 简单易用     | 功能丰富    |
| 社区  | 国内流行     | 国际标准    |
| 安全性 | 需注意版本    | 更安全     |

## 依赖模块

- **carlos-spring-boot-starter-core**: 核心基础功能
- **Fastjson 2.x**: 阿里巴巴JSON库
- **Jackson**: Spring默认JSON库

## 注意事项

- 生产环境建议使用Jackson，更安全稳定
- Fastjson适合对性能要求极高的场景
- 注意日期时区配置，避免时间错乱
- 敏感字段使用@JsonIgnore注解排除

## 版本要求

- JDK 17+
- Spring Boot 3.x
- Fastjson 2.0.60+
- Jackson 2.15+
