# carlos-mq

消息队列抽象组件，提供统一的消息队列操作接口，支持多种MQ实现。

## 功能特性

- **统一接口**: 提供统一的消息发送和接收接口
- **多MQ支持**: 支持RabbitMQ、RocketMQ、Kafka等
- **消息确认**: 支持消息确认和重试机制
- **死信队列**: 支持死信队列处理失败消息
- **延迟消息**: 支持延迟消息发送
- **事务消息**: 支持事务消息保证一致性

## 快速开始

### Maven依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-mq</artifactId>
    <version>${carlos.version}</version>
</dependency>

<!-- 根据使用的MQ选择对应的starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### RabbitMQ配置

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /

carlos:
  mq:
    type: rabbitmq
    # 是否启用消息确认
    enable-confirm: true
    # 是否启用死信队列
    enable-dlx: true
```

## 使用示例

### 发送消息

```java
@Autowired
private MessageTemplate messageTemplate;

public void sendMessage() {
    // 发送普通消息
    messageTemplate.send("my-queue", "Hello World");

    // 发送对象消息
    User user = new User();
    messageTemplate.send("user-queue", user);

    // 发送延迟消息（5秒后消费）
    messageTemplate.sendDelayed("delay-queue", "Delayed Message", 5000);
}
```

### 接收消息

```java
@Component
public class MessageConsumer {

    @RabbitListener(queues = "my-queue")
    public void handleMessage(String message) {
        System.out.println("收到消息: " + message);
    }

    @RabbitListener(queues = "user-queue")
    public void handleUserMessage(User user) {
        System.out.println("收到用户消息: " + user.getName());
    }
}
```

### 消息确认

```java
@RabbitListener(queues = "confirm-queue")
public void handleWithConfirm(Message message, Channel channel) {
    try {
        // 处理消息
        processMessage(message);

        // 手动确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    } catch (Exception e) {
        // 拒绝消息，重新入队
        channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                         false, true);
    }
}
```

### 死信队列配置

```java
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue businessQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", "dlx-exchange");
        args.put("x-dead-letter-routing-key", "dlx-routing-key");
        // 设置消息TTL
        args.put("x-message-ttl", 60000);

        return new Queue("business-queue", true, false, false, args);
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue("dlx-queue", true);
    }
}
```

## 支持的MQ类型

| MQ类型     | 说明      | 状态     |
|----------|---------|--------|
| RabbitMQ | 轻量级消息队列 | ✅ 已支持  |
| RocketMQ | 阿里云消息队列 | 🚧 规划中 |
| Kafka    | 高吞吐消息队列 | 🚧 规划中 |

## 依赖模块

- **carlos-spring-boot-core**: 核心基础功能
- **Spring AMQP**: RabbitMQ支持

## 注意事项

- 生产环境建议启用消息持久化
- 合理设置消费者并发数，避免资源耗尽
- 重要消息建议启用消息确认机制
- 注意监控死信队列，及时处理失败消息
- 避免消息体过大，影响性能

## 版本要求

- JDK 17+
- Spring Boot 3.x
- RabbitMQ 3.x+
