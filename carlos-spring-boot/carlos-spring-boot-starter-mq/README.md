# carlos-spring-boot-starter-mq

Carlos Framework 消息队列封装模块，提供统一的消息队列操作接口，支持 RabbitMQ、RocketMQ、Kafka 等多种消息中间件。

## 功能特性

- **统一接口**：屏蔽底层 MQ 实现差异，一套 API 操作多种 MQ
- **自动适配**：根据项目依赖自动检测并切换 MQ 实现
- **丰富功能**：支持普通消息、延迟消息、顺序消息、事务消息
- **配置驱动**：通过 YAML 配置即可切换 MQ 类型
- **注解支持**：使用 `@MqListener` 注解轻松实现消息监听
- **工具类支持**：提供 `MqUtil` 静态工具类便捷操作

## 快速开始

### 1. 添加依赖

```xml
<!-- MQ Starter -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-mq</artifactId>
</dependency>

<!-- 选择以下一种 MQ 实现 -->

<!-- RabbitMQ -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

<!-- 或 RocketMQ -->
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
</dependency>

<!-- 或 Kafka -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### 2. 配置文件

```yaml
carlos:
  mq:
    # MQ 类型：auto(自动检测) / rabbitmq / rocketmq / kafka
    type: auto
    
    # 全局配置
    global:
      send-timeout: 3s
      max-retry-times: 3
      enable-trace: true
    
    # RabbitMQ 配置
    rabbitmq:
      host: localhost
      port: 5672
      username: guest
      password: guest
    
    # RocketMQ 配置
    rocketmq:
      name-server: localhost:9876
      producer:
        group: default-producer-group
    
    # Kafka 配置
    kafka:
      bootstrap-servers:
        - localhost:9092
```

### 3. 发送消息

```java
@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final MqTemplate mqTemplate;
    
    public void createOrder(Order order) {
        // 保存订单...
        
        // 发送普通消息
        mqTemplate.send("order-topic", order);
        
        // 发送带标签的消息（RocketMQ）
        mqTemplate.send("order-topic", "PAID", order);
        
        // 发送延迟消息（5分钟后检查）
        mqTemplate.sendDelayed("order-check-topic", order, DelayLevel.MINUTE_5);
        
        // 发送顺序消息
        mqTemplate.sendOrdered("order-topic", order, order.getUserId());
    }
}
```

### 4. 接收消息

```java
@Component
@Slf4j
public class OrderMessageListener {
    
    @MqListener(topic = "order-topic", group = "order-consumer-group")
    public void onOrderMessage(Order order) {
        log.info("收到订单消息: {}", order.getOrderId());
        // 处理订单...
    }
    
    @MqListener(topic = "order-topic", tag = "PAID", group = "order-paid-consumer")
    public void onOrderPaidMessage(Order order) {
        log.info("收到订单支付消息: {}", order.getOrderId());
        // 处理支付...
    }
}
```

### 5. 使用工具类

```java
// 发送消息
MqUtil.send("order-topic", order);

// 发送延迟消息
MqUtil.sendDelayed("order-topic", order, DelayLevel.MINUTE_5);

// 发送顺序消息
MqUtil.sendOrdered("order-topic", order, userId);

// 链式构建消息并发送
MqUtil.builder("order-topic", order)
    .tag("PAID")
    .key(order.getOrderId())
    .header("source", "web")
    .send();
```

## 配置详解

### 自动检测机制

当 `carlos.mq.type=auto` 时，按以下优先级自动检测：

1. RocketMQ (检查 `rocketmq-spring-boot-starter`)
2. RabbitMQ (检查 `spring-boot-starter-amqp`)
3. Kafka (检查 `spring-kafka`)

### 功能支持矩阵

| 功能   | RabbitMQ | RocketMQ | Kafka   |
|------|----------|----------|---------|
| 普通消息 | ✅        | ✅        | ✅       |
| 异步发送 | ✅        | ✅        | ✅       |
| 单向发送 | ✅        | ✅        | ✅       |
| 延迟消息 | ✅ (死信)   | ✅ (原生)   | ⚠️ (模拟) |
| 顺序消息 | ✅        | ✅        | ✅       |
| 事务消息 | ✅        | ✅        | ✅       |
| 消息过滤 | ✅        | ✅        | ❌       |
| 广播消费 | ✅        | ✅        | ✅       |

## API 文档

### MqTemplate 接口

| 方法                                        | 说明        |
|-------------------------------------------|-----------|
| `send(topic, message)`                    | 同步发送消息    |
| `send(topic, tag, message)`               | 同步发送带标签消息 |
| `sendAsync(topic, message, callback)`     | 异步发送消息    |
| `sendOneWay(topic, message)`              | 单向发送消息    |
| `sendDelayed(topic, message, delayLevel)` | 发送延迟消息    |
| `sendOrdered(topic, message, hashKey)`    | 发送顺序消息    |
| `sendTransaction(topic, message, arg)`    | 发送事务消息    |
| `ping()`                                  | 检查连接状态    |

### @MqListener 注解

| 属性              | 说明       | 默认值        |
|-----------------|----------|------------|
| `topic`         | 监听主题（必填） | -          |
| `tag`           | 消息标签过滤   | "*"        |
| `group`         | 消费组      | ""         |
| `consumeMode`   | 消费模式     | CLUSTER    |
| `maxRetryTimes` | 最大重试次数   | -1（使用全局配置） |
| `ordered`       | 是否顺序消费   | false      |
| `concurrency`   | 并发线程数    | 1          |

## 注意事项

1. **延迟消息实现**：
    - RabbitMQ 使用死信队列实现
    - RocketMQ 使用原生延迟级别
    - Kafka 使用延时任务模拟

2. **事务消息**：
    - 需要配合 `MqTransactionListener` 实现本地事务
    - RocketMQ 支持最完善

3. **消息大小**：
    - RabbitMQ: 默认 512MB
    - RocketMQ: 默认 4MB
    - Kafka: 默认 1MB

## 版本要求

- JDK 17+
- Spring Boot 3.x
- RabbitMQ 3.8+ / RocketMQ 5.x / Kafka 3.x
