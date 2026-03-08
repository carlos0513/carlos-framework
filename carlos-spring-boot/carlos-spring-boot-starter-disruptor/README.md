# carlos-disruptor

Disruptor高性能队列组件，提供基于LMAX Disruptor的高性能内存队列实现，适用于高并发、低延迟的事件处理场景。

## 功能特性

- **高性能**: 基于LMAX Disruptor，提供百万级TPS的事件处理能力
- **低延迟**: 无锁设计，减少线程上下文切换
- **事件驱动**: 支持事件发布-订阅模式
- **多消费者**: 支持多个消费者并行或串行处理
- **Spring Boot集成**: 自动配置和依赖注入

## 快速开始

### Maven依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-disruptor</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 配置示例

```yaml
carlos:
  disruptor:
    enabled: true
    # RingBuffer大小，必须是2的幂
    buffer-size: 1024
    # 等待策略: blocking, sleeping, yielding, busy-spin
    wait-strategy: blocking
    # 线程池配置
    thread-pool:
      core-size: 4
      max-size: 8
      queue-capacity: 100
```

## 使用示例

### 定义事件

```java
@Data
public class OrderEvent {
    private Long orderId;
    private String orderNo;
    private BigDecimal amount;
    private Long timestamp;
}
```

### 定义事件处理器

```java
@Component
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        // 处理订单事件
        System.out.println("处理订单: " + event.getOrderNo());
    }
}
```

### 发布事件

```java
@Autowired
private DisruptorTemplate disruptorTemplate;

public void publishOrder(Order order) {
    OrderEvent event = new OrderEvent();
    event.setOrderId(order.getId());
    event.setOrderNo(order.getOrderNo());
    event.setAmount(order.getAmount());
    event.setTimestamp(System.currentTimeMillis());

    disruptorTemplate.publishEvent(event);
}
```

## 等待策略说明

| 策略                   | 说明                 | 适用场景           |
|----------------------|--------------------|----------------|
| BlockingWaitStrategy | 使用锁和条件变量，CPU占用低    | 低延迟要求不高的场景     |
| SleepingWaitStrategy | 先自旋，后yield，最后sleep | 平衡性能和CPU占用     |
| YieldingWaitStrategy | 先自旋，后yield         | 低延迟场景，可接受较高CPU |
| BusySpinWaitStrategy | 持续自旋               | 极低延迟，专用CPU核心   |

## 依赖模块

- **carlos-spring-boot-core**: 核心基础功能
- **LMAX Disruptor**: 高性能队列框架

## 性能优化建议

1. **RingBuffer大小**: 设置为2的幂，建议1024-8192
2. **等待策略**: 根据延迟要求选择合适策略
3. **消费者数量**: 避免过多消费者导致竞争
4. **批处理**: 利用endOfBatch标志进行批量处理

## 注意事项

- RingBuffer大小必须是2的幂次方
- 事件对象建议使用对象池复用，减少GC压力
- 生产环境建议使用独立线程池
- 注意监控队列积压情况

## 版本要求

- JDK 17+
- Spring Boot 3.x
- Disruptor 3.4.x
