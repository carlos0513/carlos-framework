# Carlos Log Starter

基于 Disruptor 的高性能异步操作日志框架，支持 SpEL 表达式、多存储器扩展。

## 特性

- **高性能异步处理** - 基于 Disruptor 无锁队列，单机支持 100万+ TPS
- **SpEL 表达式支持** - 注解中支持动态解析方法参数和返回值
- **存储接口化** - 应用可自定义存储实现（DB、ClickHouse、ES、Kafka 等）
- **多存储器支持** - 支持同时向多个存储器写入日志
- **风险评估** - 支持设置风险等级，高 Risk 操作特殊处理
- **条件记录** - 支持 SpEL 条件表达式，按需记录日志
- **批量处理** - 支持批量存储，减少网络开销

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-log</artifactId>
</dependency>
```

### 2. 基本使用

```java
@RestController
public class UserController {

    
    @PostMapping("/users")
    public User createUser(@RequestBody UserCreateParam param) {
        // 业务逻辑
        return userService.create(param);
    }

    @Log(
        title = "订单管理",
        businessType = BusinessType.UPDATE,
        operation = "'支付订单: ' + #param.orderNo",
        targetId = "#param.orderNo",
        targetType = "ORDER",
        riskLevel = 50
    )
    @PostMapping("/orders/pay")
    public Order payOrder(@RequestBody OrderPayParam param) {
        // 业务逻辑
        return orderService.pay(param);
    }
}
```

### 3. 自定义存储器

实现 `LogStorage` 接口：

```java
@Component
public class ElasticsearchLogStorage implements LogStorage {

    @Override
    public boolean store(OperationLog log) {
        // 存储到 ES
        return true;
    }

    @Override
    public boolean storeBatch(List<OperationLog> logs) {
        // 批量存储到 ES
        return true;
    }

    @Override
    public int getPriority() {
        return 10; // 优先级
    }
}
```

## 配置说明

```yaml
carlos:
  log:
    enabled: true          # 是否启用日志记录
    async: true            # 是否异步记录
    disruptor:
      ringBufferSize: 1024 # RingBuffer 大小（必须是2的幂）
      consumerCount: 2     # 消费者线程数
      useWorkPool: true    # 是否使用 WorkPool 模式
      waitStrategy: BLOCKING # 等待策略
    storage:
      defaultStorage: logging # 默认存储器
      batchSize: 100       # 批量大小
      batchTimeout: 100    # 批量超时（毫秒）
```

## @Log 注解详解

| 属性                  | 说明           | 示例                             |
|---------------------|--------------|--------------------------------|
| `title`             | 模块名称         | `"用户管理"`                       |
| `businessType`      | 业务类型         | `BusinessType.INSERT`          |
| `logType`           | 日志类型标识       | `"USER_CREATE"`                |
| `operation`         | 操作描述（SpEL）   | `"'创建用户: ' + #param.username"` |
| `operatorType`      | 操作人类型        | `OperatorType.MANAGE`          |
| `saveRequestParams` | 是否保存请求参数     | `true`                         |
| `saveResponseData`  | 是否保存响应数据     | `false`                        |
| `async`             | 是否异步记录       | `true`                         |
| `riskLevel`         | 风险等级（0-100）  | `50`                           |
| `targetId`          | 目标对象ID（SpEL） | `"#param.orderNo"`             |
| `targetType`        | 目标对象类型       | `"ORDER"`                      |
| `targetName`        | 目标对象名称（SpEL） | `"#result.name"`               |
| `bizChannel`        | 业务渠道（SpEL）   | `"'WEB'"`                      |
| `bizScene`          | 业务场景（SpEL）   | `"#param.scene"`               |
| `condition`         | 记录条件（SpEL）   | `"#param.needLog == true"`     |
| `excludeParams`     | 排除字段         | `"password,token"`             |

## SpEL 表达式

### 可用变量

- `#paramName` - 方法参数名
- `#result` - 方法返回值
- `#request` - HttpServletRequest
- `#method` - 当前方法
- `#target` - 目标对象
- `#class` - 目标类

### 示例

```java
@Log(
    title = "订单管理",
    operation = "'支付订单: ' + #param.orderNo + ', 金额: ' + #param.amount",
    targetId = "#param.orderNo",
    targetName = "#result != null ? #result.orderName : '未知'",
    bizChannel = "#request.getHeader('X-Channel')",
    condition = "#param.amount > 100"  // 只记录大额订单
)
```

## 与 Audit 模块集成

Audit 模块已提供 `AuditLogStorage` 实现：

```xml
<dependency>
    <groupId>com.carlos.audit</groupId>
    <artifactId>carlos-audit-bus</artifactId>
</dependency>
```

应用只需同时依赖两个模块，日志会自动存储到 ClickHouse。

## 版本变更

### 3.0.0

- 完全重构，基于 Disruptor 的高性能异步处理
- 支持 SpEL 表达式
- 存储接口化，支持多存储器
- 兼容旧 API（已标记为弃用）
