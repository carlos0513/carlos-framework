# Carlos Audit 通用审计模块

基于 **Disruptor + ClickHouse** 的高性能分布式审计日志模块，支持日写入千万级审计数据。

## 核心特性

- **高性能写入**: Disruptor 无锁队列，单机支持 10万+ TPS
- **海量存储**: ClickHouse 列式存储，支持 PB 级数据
- **实时分析**: 物化视图预聚合，毫秒级统计查询
- **混合存储**: MyBatis-Plus(MySQL配置) + ClickHouse Client(日志存储)
- **声明式使用**: 基于 `@AuditLog` 注解的 AOP 拦截

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos.audit</groupId>
    <artifactId>carlos-audit-bus</artifactId>
</dependency>
```

### 2. 配置 ClickHouse

```yaml
carlos:
  audit:
    enabled: true
    clickhouse:
      host: localhost
      port: 8123
      database: default
      username: default
      password: ""
```

### 3. 执行建表脚本

```bash
# 执行 sql/audit_ck.txt 中的 ClickHouse 建表语句
```

### 4. 使用 @AuditLog 注解

```java
@RestController
public class OrderController {
    
    @AuditLog(
        type = AuditLogTypeEnum.ORDER_PAY,
        category = AuditLogCategoryEnum.BUSINESS,
        operation = "'订单支付: ' + #param.orderNo",
        targetId = "#param.orderNo",
        targetType = AuditLogTargetTypeEnum.ORDER,
        riskLevel = 70
    )
    @PostMapping("/order/pay")
    public OrderVO pay(@RequestBody OrderPayParam param) {
        return orderService.pay(param);
    }
}
```

## 架构说明

```
@AuditLog 注解 -> AOP 拦截 -> Disruptor RingBuffer -> EventHandler 
                                                              |
                                                              v
ClickHouse BatchWriter -> 双缓冲 -> 批量写入 ClickHouse
```

## 配置项

| 配置项                                      | 默认值     | 说明            |
|------------------------------------------|---------|---------------|
| carlos.audit.enabled                     | true    | 是否启用审计日志      |
| carlos.audit.disruptor.buffer-size       | 1048576 | RingBuffer 大小 |
| carlos.audit.disruptor.consumer-count    | 4       | 消费者线程数        |
| carlos.audit.batch-writer.batch-size     | 500     | 批量写入大小        |
| carlos.audit.batch-writer.flush-interval | 1000    | 刷新间隔(ms)      |

## API 接口

- `POST /api/audit/logs/page` - 分页查询审计日志
- `GET /api/audit/stats/realtime` - 实时统计
- `GET /api/audit/trail/{principalId}` - 用户行为轨迹
- `GET /api/audit/risks` - 风险事件查询

## 文档

- [需求设计文档](./REQUIREMENT_DESIGN.md)
- [需求清单](./REQUIREMENTS_CHECKLIST.md)

## License

Copyright (c) Carlos Team
