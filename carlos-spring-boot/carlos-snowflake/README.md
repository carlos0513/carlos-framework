# carlos-snowflake

雪花算法分布式ID生成组件，提供高性能、全局唯一的ID生成服务。

## 功能特性

- **全局唯一**: 保证分布式环境下ID全局唯一
- **高性能**: 单机每秒可生成400万+ID
- **趋势递增**: ID按时间趋势递增，有利于数据库索引
- **信息可解析**: ID包含时间戳、机器ID、序列号等信息
- **灵活配置**: 支持自定义工作机器ID和数据中心ID

## 快速开始

### Maven依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-snowflake</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 配置示例

```yaml
carlos:
  snowflake:
    # 是否启用
    enabled: true
    # 工作机器ID (0-31)
    worker-id: 1
    # 数据中心ID (0-31)
    datacenter-id: 1
    # 是否使用系统时钟
    use-system-clock: false
```

## 使用示例

### 生成ID

```java
@Autowired
private SnowflakeIdGenerator idGenerator;

public void generateId() {
    // 生成Long类型ID
    Long id = idGenerator.nextId();
    System.out.println("生成的ID: " + id);

    // 生成String类型ID
    String idStr = idGenerator.nextIdStr();
    System.out.println("生成的ID字符串: " + idStr);
}
```

### 批量生成ID

```java
public void generateBatch() {
    // 批量生成100个ID
    List<Long> ids = idGenerator.nextIds(100);

    for (Long id : ids) {
        System.out.println(id);
    }
}
```

### 解析ID信息

```java
public void parseId(Long id) {
    SnowflakeInfo info = idGenerator.parseId(id);

    System.out.println("时间戳: " + info.getTimestamp());
    System.out.println("数据中心ID: " + info.getDatacenterId());
    System.out.println("工作机器ID: " + info.getWorkerId());
    System.out.println("序列号: " + info.getSequence());
    System.out.println("生成时间: " + info.getGenerateTime());
}
```

### 在实体类中使用

```java
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;
    private String email;
}
```

## ID结构说明

雪花算法生成的64位ID结构：

```
0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
|   |-------------------------------------------|   |-----|   |-----|   |----------|
符号位(1bit)    时间戳(41bit)                    数据中心(5bit) 机器(5bit) 序列号(12bit)
```

- **符号位**: 1位，始终为0
- **时间戳**: 41位，精确到毫秒，可使用69年
- **数据中心ID**: 5位，支持32个数据中心
- **工作机器ID**: 5位，每个数据中心支持32台机器
- **序列号**: 12位，每毫秒可生成4096个ID

## 配置说明

### Worker ID 和 Datacenter ID

- **取值范围**: 0-31
- **配置方式**:
    - 手动配置（推荐）
    - 自动获取（基于IP或MAC地址）

```yaml
carlos:
  snowflake:
    # 手动指定
    worker-id: 1
    datacenter-id: 1

    # 或使用自动获取
    auto-worker-id: true
```

### 时钟回拨处理

组件内置时钟回拨检测和处理机制：

- 检测到时钟回拨会抛出异常
- 可配置容忍的回拨时间（毫秒）
- 建议使用NTP同步服务器时间

```yaml
carlos:
  snowflake:
    # 容忍的时钟回拨时间（毫秒）
    max-backward-ms: 5
```

## 性能测试

单机性能测试结果：

| 线程数 | QPS   | 平均耗时   |
|-----|-------|--------|
| 1   | 400万+ | 0.25μs |
| 10  | 350万+ | 2.8μs  |
| 100 | 300万+ | 33μs   |

## 依赖模块

- **carlos-core**: 核心基础功能

## 注意事项

- **生产环境必须配置唯一的worker-id和datacenter-id**
- 不同机器的worker-id必须不同，否则可能产生重复ID
- 注意服务器时间同步，避免时钟回拨
- ID生成依赖系统时间，确保系统时间准确
- 单机部署可以不配置datacenter-id

## 与其他ID生成方案对比

| 方案    | 优点       | 缺点           |
|-------|----------|--------------|
| 数据库自增 | 简单可靠     | 性能差，单点故障     |
| UUID  | 全局唯一     | 无序，占用空间大     |
| 雪花算法  | 高性能，趋势递增 | 依赖时钟，需配置机器ID |
| Redis | 高性能      | 依赖Redis，网络开销 |

## 版本要求

- JDK 17+
- Spring Boot 3.x
