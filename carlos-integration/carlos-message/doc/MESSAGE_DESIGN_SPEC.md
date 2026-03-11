# Carlos Message 模块设计规格说明书

> 版本: 1.0  
> 日期: 2026-03-11  
> 状态: 草案

---

## 1. 引言

### 1.1 编写目的

本文档为 Carlos Message 统一消息中心模块的详细设计规格说明书，用于指导开发人员进行系统实现。

### 1.2 适用范围

- 消息中心服务端开发
- 消息客户端 SDK 开发
- 渠道适配器开发
- 管理后台开发

### 1.3 术语定义

| 术语   | 说明                 |
|------|--------------------|
| 消息模板 | 预定义的消息内容格式，支持变量占位符 |
| 渠道   | 消息发送通道，如短信、邮件、钉钉等  |
| 优先级  | 消息发送的优先级，影响队列处理顺序  |
| 消息状态 | 消息在生命周期中的状态        |

---

## 2. 总体设计

### 2.1 架构设计

```
┌─────────────────────────────────────────────────────────────────┐
│                        接入层 (Access Layer)                     │
├─────────────────────────────────────────────────────────────────┤
│  REST API    │   gRPC   │   WebSocket   │   MQ Consumer          │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                       网关层 (Gateway Layer)                     │
├─────────────────────────────────────────────────────────────────┤
│  鉴权 │ 限流 │ 熔断 │ 路由 │ 协议转换                              │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                       业务层 (Service Layer)                     │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐               │
│  │  模板引擎    │ │  消息路由    │ │  调度中心    │               │
│  └─────────────┘ └─────────────┘ └─────────────┘               │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐               │
│  │  限流控制    │ │  重试机制    │ │  状态管理    │               │
│  └─────────────┘ └─────────────┘ └─────────────┘               │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      渠道层 (Channel Layer)                      │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐   │
│  │  SMS    │ │  Email  │ │ DingTalk│ │ WeChat  │ │ WebSocket│   │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      存储层 (Storage Layer)                      │
├─────────────────────────────────────────────────────────────────┤
│  MySQL │ Redis │ MongoDB │ Elasticsearch                        │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 核心流程

#### 2.2.1 消息发送流程

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│  接收请求 │ → │ 参数校验 │ → │ 模板渲染 │ → │ 渠道路由 │ → │  入队    │
└─────────┘    └─────────┘    └─────────┘    └─────────┘    └────┬────┘
                                                                  │
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐         │
│  记录结果 │ ← │  发送    │ ← │ 渠道适配 │ ← │  出队    │ ← ─ ─ ─ ┘
└─────────┘    └─────────┘    └─────────┘    └─────────┘
```

#### 2.2.2 消息消费流程

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│  拉取消息 │ → │ 限流检查 │ → │ 渠道选择 │ → │ 发送执行 │ → │ 状态更新 │
└─────────┘    └─────────┘    └─────────┘    └────┬────┘    └─────────┘
                                                  │
                                                  ↓ 失败
                                            ┌─────────┐
                                            │  重试   │ ─ → 超过重试次数
                                            └────┬────┘        ↓
                                                 └ ─ ─ ─ → 死信队列
```

---

## 3. 详细设计

### 3.1 数据模型设计

#### 3.1.1 消息模板表 (msg_template)

```sql
CREATE TABLE msg_template (
    id              BIGINT          PRIMARY KEY COMMENT '主键ID',
    template_code   VARCHAR(64)     NOT NULL COMMENT '模板编码，唯一',
    template_name   VARCHAR(128)    NOT NULL COMMENT '模板名称',
    category        TINYINT         NOT NULL COMMENT '消息大类: 1-通知 2-验证码 3-营销 4-系统 5-交易',
    content_type    TINYINT         NOT NULL DEFAULT 1 COMMENT '内容类型: 1-文本 2-HTML 3-Markdown',
    content         TEXT            NOT NULL COMMENT '模板内容',
    params          JSON            COMMENT '参数定义',
    channel_configs JSON            COMMENT '各渠道配置',
    priority        TINYINT         DEFAULT 3 COMMENT '默认优先级: 1-紧急 2-高 3-普通 4-低',
    status          TINYINT         DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    create_by       VARCHAR(32)     COMMENT '创建人',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(32)     COMMENT '更新人',
    update_time     DATETIME        ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_template_code (template_code),
    INDEX idx_category (category),
    INDEX idx_status (status)
) COMMENT '消息模板表';
```

#### 3.1.2 渠道配置表 (msg_channel)

```sql
CREATE TABLE msg_channel (
    id              BIGINT          PRIMARY KEY COMMENT '主键ID',
    channel_code    VARCHAR(32)     NOT NULL COMMENT '渠道编码',
    channel_name    VARCHAR(64)     NOT NULL COMMENT '渠道名称',
    channel_type    TINYINT         NOT NULL COMMENT '渠道类型: 1-短信 2-邮件 3-钉钉 4-企业微信 5-站内信',
    provider        VARCHAR(32)     COMMENT '服务商',
    config          JSON            NOT NULL COMMENT '配置内容',
    rate_limit      JSON            COMMENT '限流配置',
    retry_policy    JSON            COMMENT '重试策略',
    weight          INT             DEFAULT 100 COMMENT '权重',
    status          TINYINT         DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_channel_code (channel_code),
    INDEX idx_channel_type (channel_type)
) COMMENT '渠道配置表';
```

#### 3.1.3 消息记录表 (msg_record)

```sql
CREATE TABLE msg_record (
    id              BIGINT          PRIMARY KEY COMMENT '主键ID',
    msg_id          VARCHAR(64)     NOT NULL COMMENT '消息唯一标识',
    biz_id          VARCHAR(64)     COMMENT '业务方ID',
    biz_type        VARCHAR(32)     COMMENT '业务类型',
    template_code   VARCHAR(64)     COMMENT '模板编码',
    category        TINYINT         NOT NULL COMMENT '消息大类',
    priority        TINYINT         NOT NULL COMMENT '优先级',
    title           VARCHAR(256)    COMMENT '消息标题',
    content         TEXT            COMMENT '消息内容',
    params          JSON            COMMENT '模板参数',
    channels        JSON            COMMENT '目标渠道',
    receivers       JSON            COMMENT '接收人列表',
    status          TINYINT         DEFAULT 0 COMMENT '状态: 0-待发送 1-发送中 2-已发送 3-成功 4-失败 5-撤回',
    schedule_time   DATETIME        COMMENT '定时发送时间',
    send_time       DATETIME        COMMENT '实际发送时间',
    callback_url    VARCHAR(512)    COMMENT '回调地址',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_msg_id (msg_id),
    INDEX idx_biz (biz_id, biz_type),
    INDEX idx_status (status),
    INDEX idx_schedule_time (schedule_time),
    INDEX idx_create_time (create_time)
) COMMENT '消息记录表';
```

#### 3.1.4 发送详情表 (msg_send_detail)

```sql
CREATE TABLE msg_send_detail (
    id              BIGINT          PRIMARY KEY COMMENT '主键ID',
    msg_id          VARCHAR(64)     NOT NULL COMMENT '消息ID',
    channel_code    VARCHAR(32)     NOT NULL COMMENT '渠道编码',
    receiver        VARCHAR(256)    NOT NULL COMMENT '接收人',
    status          TINYINT         DEFAULT 0 COMMENT '状态: 0-待发送 1-发送中 2-成功 3-失败 4-重试中',
    retry_count     TINYINT         DEFAULT 0 COMMENT '重试次数',
    max_retry       TINYINT         DEFAULT 3 COMMENT '最大重试次数',
    request_param   TEXT            COMMENT '请求参数',
    response_data   TEXT            COMMENT '响应数据',
    error_msg       VARCHAR(512)    COMMENT '错误信息',
    send_time       DATETIME        COMMENT '发送时间',
    finish_time     DATETIME        COMMENT '完成时间',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_msg_id (msg_id),
    INDEX idx_status (status),
    INDEX idx_channel (channel_code, status)
) COMMENT '发送详情表';
```

### 3.2 接口设计

#### 3.2.1 发送接口

```java
/**
 * 消息发送服务
 */
public interface MessageSendApi {
    
    /**
     * 同步发送消息
     * 
     * @param request 发送请求
     * @return 发送结果
     */
    @PostMapping("/message/send")
    Result<SendResult> send(@RequestBody @Valid SendRequest request);
    
    /**
     * 异步发送消息
     * 
     * @param request 发送请求
     * @return 消息ID
     */
    @PostMapping("/message/sendAsync")
    Result<String> sendAsync(@RequestBody @Valid SendRequest request);
    
    /**
     * 批量发送消息
     * 
     * @param requests 发送请求列表
     * @return 批量发送结果
     */
    @PostMapping("/message/sendBatch")
    Result<BatchSendResult> sendBatch(@RequestBody @Valid List<SendRequest> requests);
    
    /**
     * 定时发送消息
     * 
     * @param request 发送请求
     * @param scheduleTime 定时时间
     * @return 消息ID
     */
    @PostMapping("/message/schedule")
    Result<String> schedule(
        @RequestBody @Valid SendRequest request,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduleTime
    );
    
    /**
     * 撤回消息
     * 
     * @param msgId 消息ID
     * @return 是否成功
     */
    @PostMapping("/message/{msgId}/revoke")
    Result<Boolean> revoke(@PathVariable String msgId);
    
    /**
     * 查询消息状态
     * 
     * @param msgId 消息ID
     * @return 消息状态
     */
    @GetMapping("/message/{msgId}/status")
    Result<MessageStatus> queryStatus(@PathVariable String msgId);
}
```

#### 3.2.2 管理接口

```java
/**
 * 模板管理
 */
public interface TemplateManageApi {
    
    @PostMapping("/template")
    Result<Long> create(@RequestBody @Valid TemplateCreateRequest request);
    
    @PutMapping("/template/{id}")
    Result<Void> update(@PathVariable Long id, @RequestBody @Valid TemplateUpdateRequest request);
    
    @DeleteMapping("/template/{id}")
    Result<Void> delete(@PathVariable Long id);
    
    @GetMapping("/template/{code}")
    Result<TemplateVO> getByCode(@PathVariable String code);
    
    @PostMapping("/template/page")
    Result<Paging<TemplateVO>> page(@RequestBody @Valid TemplatePageRequest request);
}

/**
 * 渠道管理
 */
public interface ChannelManageApi {
    
    @PostMapping("/channel")
    Result<Long> create(@RequestBody @Valid ChannelCreateRequest request);
    
    @PutMapping("/channel/{id}")
    Result<Void> update(@PathVariable Long id, @RequestBody @Valid ChannelUpdateRequest request);
    
    @PostMapping("/channel/{code}/enable")
    Result<Void> enable(@PathVariable String code);
    
    @PostMapping("/channel/{code}/disable")
    Result<Void> disable(@PathVariable String code);
    
    @GetMapping("/channel/{code}")
    Result<ChannelVO> getByCode(@PathVariable String code);
    
    @PostMapping("/channel/page")
    Result<Paging<ChannelVO>> page(@RequestBody @Valid ChannelPageRequest request);
}
```

### 3.3 核心类设计

#### 3.3.1 渠道适配器

```java
/**
 * 渠道适配器接口
 */
public interface ChannelAdapter {
    
    /**
     * 获取渠道类型
     */
    ChannelType getChannelType();
    
    /**
     * 检查渠道是否可用
     */
    boolean isAvailable();
    
    /**
     * 发送消息
     * 
     * @param context 消息上下文
     * @return 发送结果
     */
    SendResult send(MessageContext context);
    
    /**
     * 批量发送
     * 
     * @param contexts 消息上下文列表
     * @return 批量发送结果
     */
    BatchSendResult sendBatch(List<MessageContext> contexts);
    
    /**
     * 检查是否支持该消息类型
     * 
     * @param messageType 消息类型
     * @return 是否支持
     */
    boolean supports(MessageType messageType);
}

/**
 * 抽象渠道适配器
 */
@Slf4j
public abstract class AbstractChannelAdapter implements ChannelAdapter, InitializingBean {
    
    @Autowired
    protected ChannelConfigService configService;
    
    @Autowired
    protected RateLimiter rateLimiter;
    
    @Autowired
    protected MeterRegistry meterRegistry;
    
    private String channelCode;
    
    @Override
    public void afterPropertiesSet() {
        this.channelCode = getChannelType().getCode();
        // 注册到渠道工厂
        ChannelFactory.register(this);
    }
    
    @Override
    public SendResult send(MessageContext context) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            // 1. 检查渠道状态
            if (!isAvailable()) {
                return SendResult.failed("渠道不可用: " + channelCode);
            }
            
            // 2. 限流检查
            if (!rateLimiter.tryAcquire(channelCode, 1)) {
                return SendResult.failed("触发限流: " + channelCode);
            }
            
            // 3. 执行发送
            SendResult result = doSend(context);
            
            // 4. 记录指标
            sample.stop(meterRegistry.timer("message.send", 
                "channel", channelCode,
                "status", result.isSuccess() ? "success" : "failed"));
            
            return result;
            
        } catch (Exception e) {
            log.error("消息发送异常, channel={}, msgId={}", channelCode, context.getMsgId(), e);
            sample.stop(meterRegistry.timer("message.send", 
                "channel", channelCode,
                "status", "error"));
            return SendResult.failed("发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 子类实现具体发送逻辑
     */
    protected abstract SendResult doSend(MessageContext context);
}
```

#### 3.3.2 消息队列

```java
/**
 * 消息队列服务
 */
public interface MessageQueueService {
    
    /**
     * 发送消息到队列
     * 
     * @param message 消息
     * @param priority 优先级
     */
    void enqueue(MessageTask message, MessagePriority priority);
    
    /**
     * 延时发送
     * 
     * @param message 消息
     * @param delayMillis 延迟毫秒
     */
    void delay(MessageTask message, long delayMillis);
    
    /**
     * 定时发送
     * 
     * @param message 消息
     * @param scheduleTime 定时时间
     */
    void schedule(MessageTask message, LocalDateTime scheduleTime);
    
    /**
     * 消费消息
     * 
     * @param priority 优先级
     * @param handler 处理器
     */
    void consume(MessagePriority priority, MessageHandler handler);
}

/**
 * Redis Stream 实现
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "message.queue.type", havingValue = "redis")
public class RedisStreamMessageQueue implements MessageQueueService {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private MessageProperties properties;
    
    private final Map<MessagePriority, StreamMessageListenerContainer<String, ObjectRecord<String, MessageTask>>> 
        containers = new ConcurrentHashMap<>();
    
    @Override
    public void enqueue(MessageTask message, MessagePriority priority) {
        String streamKey = getStreamKey(priority);
        redisTemplate.opsForStream().add(streamKey, message.toMap());
        log.debug("消息入队, msgId={}, priority={}", message.getMsgId(), priority);
    }
    
    @Override
    public void delay(MessageTask message, long delayMillis) {
        String zsetKey = "message:delay";
        double score = System.currentTimeMillis() + delayMillis;
        redisTemplate.opsForZSet().add(zsetKey, message.getMsgId(), score);
        // 缓存消息内容
        redisTemplate.opsForHash().putAll("message:delay:data:" + message.getMsgId(), message.toMap());
        log.debug("延时消息, msgId={}, delay={}ms", message.getMsgId(), delayMillis);
    }
    
    @Override
    public void schedule(MessageTask message, LocalDateTime scheduleTime) {
        long delayMillis = Duration.between(LocalDateTime.now(), scheduleTime).toMillis();
        if (delayMillis <= 0) {
            enqueue(message, message.getPriority());
        } else {
            delay(message, delayMillis);
        }
    }
    
    @Override
    public void consume(MessagePriority priority, MessageHandler handler) {
        String streamKey = getStreamKey(priority);
        String group = "message-consumer-group";
        String consumer = properties.getConsumerId();
        
        // 创建消费组（如果不存在）
        try {
            redisTemplate.opsForStream().createGroup(streamKey, group);
        } catch (Exception e) {
            // 组已存在
        }
        
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, MessageTask>> 
            options = StreamMessageListenerContainerOptions.builder()
                .batchSize(10)
                .pollTimeout(Duration.ofSeconds(1))
                .targetType(MessageTask.class)
                .build();
        
        StreamMessageListenerContainer<String, ObjectRecord<String, MessageTask>> container = 
            StreamMessageListenerContainer.create(redisTemplate.getConnectionFactory(), options);
        
        container.receiveAutoAck(Consumer.from(group, consumer), 
            StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
            message -> handler.handle(message.getValue().getValue()));
        
        container.start();
        containers.put(priority, container);
        
        log.info("启动消息消费, priority={}, stream={}", priority, streamKey);
    }
    
    private String getStreamKey(MessagePriority priority) {
        return "stream:message:" + priority.name().toLowerCase();
    }
}
```

#### 3.3.3 模板引擎

```java
/**
 * 模板引擎
 */
public interface TemplateEngine {
    
    /**
     * 渲染模板
     * 
     * @param templateCode 模板编码
     * @param params 参数
     * @return 渲染后的内容
     */
    String render(String templateCode, Map<String, Object> params);
    
    /**
     * 渲染模板
     * 
     * @param templateContent 模板内容
     * @param contentType 内容类型
     * @param params 参数
     * @return 渲染后的内容
     */
    String render(String templateContent, ContentType contentType, Map<String, Object> params);
}

/**
 * FreeMarker 模板引擎实现
 */
@Component
public class FreeMarkerTemplateEngine implements TemplateEngine {
    
    private final Configuration configuration;
    
    @Autowired
    private TemplateCacheService cacheService;
    
    public FreeMarkerTemplateEngine() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setNumberFormat("0.##");
    }
    
    @Override
    public String render(String templateCode, Map<String, Object> params) {
        MessageTemplate template = cacheService.getTemplate(templateCode);
        if (template == null) {
            throw new ServiceException("模板不存在: " + templateCode);
        }
        return render(template.getContent(), template.getContentType(), params);
    }
    
    @Override
    public String render(String templateContent, ContentType contentType, Map<String, Object> params) {
        try {
            Template template = new Template("dynamic", templateContent, configuration);
            StringWriter writer = new StringWriter();
            template.process(params, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new ServiceException("模板渲染失败: " + e.getMessage(), e);
        }
    }
}
```

---

## 4. 安全设计

### 4.1 接口安全

- **鉴权**: 所有接口需携带有效 Token
- **签名**: 关键接口需进行参数签名
- **限流**: 按应用+接口维度限流
- **IP白名单**: 渠道回调接口限制IP

### 4.2 内容安全

- **敏感词过滤**: 使用 DFA 算法进行敏感词检测
- **内容审计**: 营销类消息人工审核
- **频率控制**: 单用户单渠道发送频率限制

### 4.3 数据安全

- **加密存储**: 渠道配置敏感字段加密
- **脱敏展示**: 手机号、邮箱等脱敏展示
- **审计日志**: 所有操作记录审计日志

---

## 5. 监控告警

### 5.1 监控指标

| 指标                        | 说明    | 告警阈值     |
|---------------------------|-------|----------|
| message.send.total        | 发送总数  | -        |
| message.send.success      | 成功数   | -        |
| message.send.failed       | 失败数   | > 100/分钟 |
| message.send.duration     | 发送耗时  | p99 > 3s |
| message.queue.size        | 队列积压  | > 10000  |
| message.channel.fail_rate | 渠道失败率 | > 5%     |

### 5.2 告警规则

- **渠道故障**: 单个渠道失败率 > 5%，持续 5 分钟
- **队列积压**: 队列积压 > 10000，持续 3 分钟
- **发送延迟**: 消息发送 p99 延迟 > 3s
- **系统异常**: 异常数 > 10/分钟

---

## 6. 部署架构

### 6.1 单机部署

```
┌─────────────────────────────────────┐
│           Load Balancer             │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│      Message Server (单实例)         │
│  ┌─────────┐ ┌─────────┐           │
│  │ REST API│ │  Admin  │           │
│  └────┬────┘ └────┬────┘           │
│       └───────────┘                 │
│  ┌─────────┐ ┌─────────┐           │
│  │ Consumer│ │Scheduler│           │
│  └─────────┘ └─────────┘           │
└─────────────┬───────────────────────┘
              │
    ┌─────────┼─────────┐
    ↓         ↓         ↓
┌───────┐ ┌───────┐ ┌───────┐
│ MySQL │ │ Redis │ │MongoDB│
└───────┘ └───────┘ └───────┘
```

### 6.2 集群部署

```
┌───────────────────────────────────────────────────────────────┐
│                     Load Balancer (Nginx)                     │
└───────┬───────────────────────┬───────────────────────┬───────┘
        │                       │                       │
┌───────▼───────┐       ┌───────▼───────┐       ┌───────▼───────┐
│ Message API-1 │       │ Message API-2 │       │ Message API-N │
│  (无状态)      │       │  (无状态)      │       │  (无状态)      │
└───────┬───────┘       └───────┬───────┘       └───────┬───────┘
        │                       │                       │
        └───────────────────────┼───────────────────────┘
                                │
┌───────────────────────────────▼───────────────────────────────┐
│                   Message Queue Cluster                        │
│              (Redis Cluster / RabbitMQ Cluster)               │
└───────────────────────────────┬───────────────────────────────┘
                                │
        ┌───────────────────────┼───────────────────────┐
        ↓                       ↓                       ↓
┌───────────────┐       ┌───────────────┐       ┌───────────────┐
│ Consumer-1    │       │ Consumer-2    │       │ Consumer-N    │
│ (消息消费实例)  │       │ (消息消费实例)  │       │ (消息消费实例)  │
└───────────────┘       └───────────────┘       └───────────────┘
```

---

## 7. 附录

### 7.1 错误码定义

| 错误码     | 说明         |
|---------|------------|
| MSG_001 | 模板不存在      |
| MSG_002 | 模板参数错误     |
| MSG_003 | 渠道不可用      |
| MSG_004 | 触发限流       |
| MSG_005 | 发送失败       |
| MSG_006 | 消息不存在      |
| MSG_007 | 消息已发送，无法撤回 |
| MSG_008 | 渠道配置错误     |

### 7.2 配置文件示例

```yaml
message:
  # 队列配置
  queue:
    type: redis  # redis / rabbitmq / rocketmq
    redis:
      stream-prefix: "stream:message"
      consumer-group: "message-consumer"
      consumer-count: 3
  
  # 限流配置
  rate-limiter:
    enabled: true
    default-limit: 100  # 默认每秒100条
    channels:
      sms: 50
      email: 30
      dingtalk: 100
  
  # 重试配置
  retry:
    max-attempts: 3
    initial-interval: 1000
    multiplier: 2
    max-interval: 30000
  
  # 渠道配置
  channels:
    sms:
      enabled: true
      provider: aliyun
      aliyun:
        access-key: ${SMS_ALIYUN_ACCESS_KEY}
        secret-key: ${SMS_ALIYUN_SECRET_KEY}
        sign-name: ${SMS_ALIYUN_SIGN_NAME}
    dingtalk:
      enabled: true
      app-key: ${DINGTALK_APP_KEY}
      app-secret: ${DINGTALK_APP_SECRET}
  
  # 监控配置
  metrics:
    enabled: true
    export:
      prometheus:
        enabled: true
```

---

## 8. 修订记录

| 版本  | 日期         | 修订人          | 修订内容 |
|-----|------------|--------------|------|
| 1.0 | 2026-03-11 | AI Assistant | 初始版本 |
