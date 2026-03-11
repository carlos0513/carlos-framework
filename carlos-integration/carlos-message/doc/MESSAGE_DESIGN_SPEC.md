# Carlos Message 模块设计规格说明书

> 版本: 2.0  
> 日期: 2026-03-11  
> 状态: 已更新（与数据库设计保持一致）

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

#### 3.1.1 消息类型表 (message_type)

```sql
CREATE TABLE message_type (
    id          BIGINT(20)  NOT NULL COMMENT '主键ID',
    type_code   VARCHAR(32) NOT NULL COMMENT '类型编码，如：ORDER_NOTIFY',
    type_name   VARCHAR(32) NOT NULL COMMENT '类型名称，如：订单通知',
    is_enabled  TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用: 0-禁用 1-启用',
    is_deleted  TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-否 1-是',
    create_by   BIGINT(32)           DEFAULT NULL COMMENT '创建者编号',
    create_time DATETIME             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   BIGINT(32)           DEFAULT NULL COMMENT '更新者编号',
    update_time DATETIME             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY uk_type_code (type_code)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息类型'
  ROW_FORMAT = DYNAMIC;
```

**初始数据：**

```sql
INSERT INTO message_type (id, type_code, type_name, is_enabled) VALUES
(1, 'TASK_NOTIFY', '任务通知', 1),
(2, 'VERIFY_CODE', '验证码', 1),
(3, 'SYSTEM_NOTIFY', '系统通知', 1),
(4, 'APPROVAL_REMIND', '审批提醒', 1),
(5, 'MARKETING_PUSH', '营销推送', 1);
```

---

#### 3.1.2 消息模板表 (message_template)

```sql
CREATE TABLE message_template (
    id               BIGINT(20)  NOT NULL COMMENT '主键ID',
    type_id          BIGINT(20)  NOT NULL COMMENT '消息类型ID，关联message_type.id',
    template_code    VARCHAR(50) NOT NULL COMMENT '模板编码，唯一标识',
    template_name    VARCHAR(50) NOT NULL COMMENT '模板名称',
    title_template   VARCHAR(100)         DEFAULT NULL COMMENT '标题模板，可为空',
    content_template TEXT        NOT NULL COMMENT '模板内容，支持变量占位符如：${userName}',
    param_schema     JSON                 DEFAULT NULL COMMENT '参数定义JSON，如：{"userName":"string","orderNo":"string"}',
    channel_config   JSON                 DEFAULT NULL COMMENT '渠道特殊配置，如：{"SMS":{"templateCode":"SMS_123"},"DINGTALK":{"agentId":"123"}}',
    is_enabled       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否启用: 0-禁用 1-启用 2-草稿',
    is_deleted       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-否 1-是',
    create_by        BIGINT(32)           DEFAULT NULL COMMENT '创建者编号',
    create_time      DATETIME             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by        BIGINT(32)           DEFAULT NULL COMMENT '更新者编号',
    update_time      DATETIME             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY uk_template_code (template_code),
    INDEX idx_type_id (type_id),
    INDEX idx_is_enabled (is_enabled)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息模板'
  ROW_FORMAT = DYNAMIC;
```

**示例数据：**

```sql
INSERT INTO message_template (type_id, template_code, template_name, title_template, content_template, param_schema, channel_config, is_enabled) VALUES
(1, 'ORDER_SUCCESS', '订单支付成功', '订单通知', '尊敬的${userName}，您的订单${orderNo}已支付成功，金额￥${amount}。', 
 '{"userName":"string","orderNo":"string","amount":"string"}', 
 '{"SMS":{"templateCode":"SMS_123"},"DINGTALK":{"agentId":"123"}}', 1),
(2, 'VERIFY_CODE', '验证码', NULL, '您的验证码是${code}，10分钟内有效，请勿泄露。', 
 '{"code":"string"}', 
 '{"SMS":{"templateCode":"SMS_456"}}', 1);
```

#### 3.1.3 渠道配置表 (message_channel)

```sql
CREATE TABLE message_channel (
    id               BIGINT       NOT NULL COMMENT '主键ID',
    channel_code     VARCHAR(32)  NOT NULL COMMENT '渠道编码，如：ALIYUN_SMS、DINGTALK_WORK',
    channel_name     VARCHAR(32)  NOT NULL COMMENT '渠道名称，如：阿里云短信',
    channel_type     TINYINT      NOT NULL DEFAULT 1 COMMENT '渠道类型: 1-短信 2-邮件 3-钉钉 4-企业微信 5-站内信',
    provider         VARCHAR(32)           DEFAULT NULL COMMENT '服务商编码，如：aliyun、tencent',
    channel_config   JSON         NOT NULL COMMENT '渠道配置JSON，包含密钥等敏感信息（建议加密存储）',
    rate_limit_qps   INT                   DEFAULT 100 COMMENT '每秒最大请求数',
    rate_limit_burst INT                   DEFAULT 200 COMMENT '突发流量限制',
    retry_times      TINYINT               DEFAULT 3 COMMENT '最大重试次数',
    retry_interval   INT                   DEFAULT 1000 COMMENT '重试间隔，单位毫秒',
    weight           INT                   DEFAULT 100 COMMENT '负载均衡权重，范围0-1000',
    is_enabled       TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '是否启用: 0-禁用 1-启用 2-故障',
    is_deleted       TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-否 1-是',
    create_by        BIGINT                DEFAULT NULL COMMENT '创建者编号',
    create_time      DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by        BIGINT                DEFAULT NULL COMMENT '更新者编号',
    update_time      DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY uk_channel_code (channel_code),
    INDEX idx_channel_type (channel_type),
    INDEX idx_is_enabled (is_enabled)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息渠道配置'
  ROW_FORMAT = DYNAMIC;
```

**示例数据：**

```sql
INSERT INTO message_channel (channel_code, channel_name, channel_type, provider, channel_config, weight, is_enabled) VALUES
('ALIYUN_SMS', '阿里云短信', 1, 'aliyun', '{"accessKey":"xxx","secretKey":"xxx","signName":"xxx","endpoint":"dysmsapi.aliyuncs.com"}', 100, 1),
('TENCENT_SMS', '腾讯云短信', 1, 'tencent', '{"secretId":"xxx","secretKey":"xxx","smsSdkAppId":"xxx"}', 80, 1),
('DINGTALK_WORK', '钉钉工作通知', 3, 'dingtalk', '{"appKey":"xxx","appSecret":"xxx","agentId":"xxx"}', 100, 1);
```

#### 3.1.4 消息记录表 (message_record)

**设计说明：**

- 一条消息（message_id）可以发送给多个接收人
- 本表记录消息的整体信息，不包含单个接收人的发送状态
- 发送统计通过 `total_count/success_count/fail_count` 字段记录

```sql
CREATE TABLE message_record (
    id               BIGINT       NOT NULL COMMENT '主键ID',
    message_id       VARCHAR(64)  NOT NULL COMMENT '消息唯一标识，业务ID，如：message_2024031112000001',
    
    -- 模板信息
    template_code    VARCHAR(32)  NOT NULL COMMENT '模板编码，关联message_template',
    template_params  JSON                  DEFAULT NULL COMMENT '模板参数JSON，如：{"userName":"张三"}',
    
    -- 消息内容（渲染后的最终内容）
    message_type     VARCHAR(32)  NOT NULL COMMENT '消息类型编码，关联message_type.type_code',
    message_title    VARCHAR(255)          DEFAULT NULL COMMENT '消息标题（渲染后）',
    message_content  TEXT         NOT NULL COMMENT '消息内容（渲染后）',
    
    -- 发送方信息
    sender_id        VARCHAR(32)  NOT NULL COMMENT '发送人ID',
    sender_name      VARCHAR(64)           DEFAULT NULL COMMENT '发送人名称',
    sender_system    VARCHAR(50)  NOT NULL COMMENT '发送系统标识，如：ORDER_SYSTEM',
    
    -- 操作反馈（可选）
    feedback_type    VARCHAR(16)           DEFAULT NULL COMMENT '操作反馈类型: none-无 detail-详情 inner-站内跳转 url-外链',
    feedback_content VARCHAR(255)          DEFAULT NULL COMMENT '操作反馈内容，如跳转链接',
    
    -- 优先级与时效
    priority         TINYINT      NOT NULL DEFAULT 3 COMMENT '优先级: 1-紧急 2-高 3-普通 4-低',
    valid_until      DATETIME              DEFAULT NULL COMMENT '消息有效期，过期不再发送',
    
    -- 发送统计（替代原来的状态字段）
    total_count   INT DEFAULT 0 COMMENT '总接收人数',
    success_count INT DEFAULT 0 COMMENT '成功发送数',
    fail_count    INT DEFAULT 0 COMMENT '失败发送数',
    
    -- 扩展字段
    extras        JSON                  DEFAULT NULL COMMENT '扩展信息JSON',
    
    -- 审计字段
    create_by     BIGINT       NULL     DEFAULT NULL COMMENT '创建人',
    create_time   DATETIME     NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by     BIGINT       NULL     DEFAULT NULL COMMENT '更新人',
    update_time   DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY uk_message_id (message_id),
    INDEX idx_template_code (template_code),
    INDEX idx_message_type (message_type),
    INDEX idx_sender_id (sender_id),
    INDEX idx_sender_system (sender_system),
    INDEX idx_priority (priority),
    INDEX idx_create_time (create_time)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息记录表'
  ROW_FORMAT = DYNAMIC;
```

**说明：**

- `message_id` 业务唯一标识，用于幂等控制和查询
- `message_title/message_content` 存储渲染后的最终内容，便于查询和重发
- 统计字段实时更新或通过定时任务汇总更新

#### 3.1.5 消息接收人表 (message_receiver)

**设计说明：**

- 记录每个接收人在每个渠道的发送情况
- 一条消息发给N个人，会产生N条记录（同一接收人多渠道会生成多条）
- 状态流转：待发送(0) → 发送中(1) → 已发送(2) → 送达(3) → 已读(4) 或 失败(5) → 撤回(6)

```sql
CREATE TABLE message_receiver (
    id                BIGINT       NOT NULL COMMENT '主键ID',
    message_id        VARCHAR(64)  NOT NULL COMMENT '消息ID，关联message_record.message_id',
    channel_code      VARCHAR(32)  NOT NULL COMMENT '渠道编码，如：ALIYUN_SMS',
    
    -- 接收人信息
    receiver_id       VARCHAR(64)  NOT NULL COMMENT '接收者ID，如用户ID',
    receiver_type     TINYINT               DEFAULT 1 COMMENT '接收人类型: 1-用户 2-部门 3-角色',
    receiver_number   VARCHAR(128)          DEFAULT NULL COMMENT '接收地址，如手机号/邮箱/钉钉ID',
    receiver_audience VARCHAR(64)           DEFAULT NULL COMMENT '接收者设备标识，用于推送',
    channel_message_id VARCHAR(64)          DEFAULT NULL COMMENT '渠道返回的消息ID，用于查询渠道状态',
    
    -- 状态流转（核心字段）
    status      TINYINT  NOT NULL DEFAULT 0 COMMENT '状态: 0-待发送 1-发送中 2-已发送 3-送达 4-已读 5-失败 6-撤回',
    fail_count  TINYINT           DEFAULT 0 COMMENT '失败次数',
    fail_reason VARCHAR(512)      DEFAULT NULL COMMENT '最后一次失败原因',
    
    -- 时间追踪
    schedule_time DATETIME COMMENT '定时发送时间，NULL表示立即发送',
    send_time     DATETIME COMMENT '实际发送时间',
    deliver_time  DATETIME COMMENT '渠道返回的送达时间',
    read_time     DATETIME COMMENT '用户阅读时间',
    
    -- 回调
    callback_url VARCHAR(512) COMMENT '状态回调URL',
    extras       JSON         COMMENT '扩展信息',
    
    -- 审计字段
    create_by   BIGINT   NULL     DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   BIGINT   NULL     DEFAULT NULL COMMENT '更新人',
    update_time DATETIME NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_message_id (message_id),
    INDEX idx_channel_code (channel_code),
    INDEX idx_receiver_id (receiver_id),
    INDEX idx_status (status),
    -- 复合索引，优化高频查询
    INDEX idx_receiver_status (receiver_id, status),
    INDEX idx_channel_status (channel_code, status),
    INDEX idx_message_status (message_id, status),
    INDEX idx_schedule (schedule_time, status),
    INDEX idx_create_time (create_time)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息接收人表'
  ROW_FORMAT = DYNAMIC;
```

**状态流转图:**

```
                    ┌─────────────┐
         ┌─────────│   待发送    │
         │         │   (0)       │
         │         └──────┬──────┘
         │                │ 定时到/立即发送
         │                ▼
         │         ┌─────────────┐
         │    ┌────│   发送中    │
         │    │    │   (1)       │
         │    │    └──────┬──────┘
         │    │           │
         │    │     ┌─────┴─────┐
         │    │     ▼           ▼
         │    │  ┌──────┐   ┌────────┐
         │    │  │ 成功 │   │  失败  │
         │    │  │ (2)  │   │  (5)   │
         │    │  └──┬───┘   └───┬────┘
         │    │     │           │
         │    │     ▼           │ 重试<3次
         │    │  ┌──────┐       │
         │    │  │ 送达 │◄──────┘
         │    │  │ (3)  │
         │    │  └──┬───┘
         │    │     │
         │    │     ▼
         │    │  ┌──────┐
         │    └──►│ 已读 │
         │       │ (4)  │
         │       └──────┘
         │
         └──────────────────────────► 撤回 (6)
```

**常用查询场景：**

```sql
-- 1. 查询某人的待发送消息
SELECT * FROM message_receiver 
WHERE receiver_id = ? AND status = 0 
ORDER BY create_time DESC;
-- 使用索引: idx_receiver_status

-- 2. 查询某条消息的所有接收人状态
SELECT * FROM message_receiver 
WHERE message_id = ? 
ORDER BY create_time;
-- 使用索引: idx_message_id

-- 3. 查询某渠道的失败记录（用于重试）
SELECT * FROM message_receiver 
WHERE channel_code = ? AND status = 5 AND fail_count < 3 
ORDER BY create_time 
LIMIT 100;
-- 使用索引: idx_channel_status

-- 4. 查询定时发送队列
SELECT * FROM message_receiver 
WHERE status = 0 AND schedule_time <= NOW() 
ORDER BY schedule_time 
LIMIT 1000;
-- 使用索引: idx_schedule
```

#### 3.1.6 发送日志表 (message_send_log)

**设计说明：**

- 记录每次发送的详细日志，包括请求参数和响应结果
- 用于问题排查、对账、审计
- 数据量大，建议保留7天后清理

```sql
CREATE TABLE message_send_log (
    id            BIGINT       NOT NULL COMMENT '主键ID',
    message_id    VARCHAR(64)  NOT NULL COMMENT '消息ID，关联message_record',
    receiver_id   BIGINT       NOT NULL COMMENT '接收人记录ID，关联message_receiver.id',
    channel_code  VARCHAR(32)  NOT NULL COMMENT '渠道编码',
    
    -- 请求响应（用于排查问题）
    request_param MEDIUMTEXT   COMMENT '发送请求参数',
    response_data MEDIUMTEXT   COMMENT '渠道返回数据',
    
    -- 执行结果
    is_success    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否成功: 0-失败 1-成功',
    error_code    VARCHAR(32)  COMMENT '错误码',
    error_message VARCHAR(512) COMMENT '错误信息',
    cost_time     INT          COMMENT '耗时，单位毫秒',
    
    -- 审计
    create_time   DATETIME     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_message_id (message_id),
    INDEX idx_receiver_id (receiver_id),
    INDEX idx_channel_code (channel_code),
    INDEX idx_is_success (is_success),
    INDEX idx_create_time (create_time)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息发送日志'
  ROW_FORMAT = DYNAMIC;
```

**注意：** 增加了 `receiver_id` 字段，用于精确关联到具体的接收人记录。

---

### 3.2 接口设计

#### 3.2.1 发送接口

```java
/**
 * 消息发送服务（Feign接口定义）
 * 
 * 路径规范：以 /api 开头
 * 熔断降级：配置 fallbackFactory
 */
@FeignClient(
    name = "carlos-message",
    contextId = "messageSendApi",
    path = "/api/message",
    fallbackFactory = MessageSendApiFallbackFactory.class
)
public interface MessageSendApi {
    
    /**
     * 同步发送消息
     * 
     * @param request 发送请求
     * @return 发送结果
     */
    @PostMapping("/send")
    Result<SendResult> send(@RequestBody @Valid MessageSendParam request);
    
    /**
     * 异步发送消息
     * 
     * @param request 发送请求
     * @return 消息ID
     */
    @PostMapping("/sendAsync")
    Result<String> sendAsync(@RequestBody @Valid MessageSendParam request);
    
    /**
     * 批量发送消息
     * 
     * @param requests 发送请求列表
     * @return 批量发送结果
     */
    @PostMapping("/sendBatch")
    Result<BatchSendResult> sendBatch(@RequestBody @Valid List<MessageSendParam> requests);
    
    /**
     * 定时发送消息
     * 
     * @param request 发送请求
     * @param scheduleTime 定时时间
     * @return 消息ID
     */
    @PostMapping("/schedule")
    Result<String> schedule(
        @RequestBody @Valid MessageSendParam request,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduleTime
    );
    
    /**
     * 撤回消息
     * 
     * @param messageId 消息ID
     * @return 是否成功
     */
    @PostMapping("/{messageId}/revoke")
    Result<Boolean> revoke(@PathVariable String messageId);
    
    /**
     * 查询消息状态
     * 
     * @param messageId 消息ID
     * @return 消息状态
     */
    @GetMapping("/{messageId}/status")
    Result<MessageStatusAO> queryStatus(@PathVariable String messageId);
}
```

#### 3.2.2 查询接口

```java
/**
 * 消息查询服务（Feign接口定义）
 */
@FeignClient(
    name = "carlos-message",
    contextId = "messageQueryApi",
    path = "/api/message",
    fallbackFactory = MessageQueryApiFallbackFactory.class
)
public interface MessageQueryApi {
    
    /**
     * 查询消息记录详情
     * 
     * @param messageId 消息ID
     * @return 消息记录
     */
    @GetMapping("/{messageId}")
    Result<MessageRecordAO> getById(@PathVariable String messageId);
    
    /**
     * 分页查询消息记录
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    Result<Paging<MessageRecordAO>> page(@RequestBody @Valid MessagePageParam param);
    
    /**
     * 查询消息接收人列表
     * 
     * @param messageId 消息ID
     * @return 接收人列表
     */
    @GetMapping("/{messageId}/receivers")
    Result<List<MessageReceiverAO>> getReceivers(@PathVariable String messageId);
    
    /**
     * 查询用户的未读消息
     * 
     * @param userId 用户ID
     * @return 未读消息列表
     */
    @GetMapping("/unread/{userId}")
    Result<List<MessageRecordAO>> getUnread(@PathVariable String userId);
}
```

#### 3.2.3 管理接口（Controller 层）

```java
/**
 * 模板管理（非Feign接口，管理后台直接调用）
 */
@RestController
@RequestMapping("/message/template")
@Tag(name = "消息模板管理")
public class MessageTemplateController {
    
    @PostMapping
    @Operation(summary = "创建模板")
    Result<Long> create(@RequestBody @Valid TemplateCreateParam param);
    
    @PutMapping("/{id}")
    @Operation(summary = "更新模板")
    Result<Void> update(@PathVariable Long id, @RequestBody @Valid TemplateUpdateParam param);
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除模板")
    Result<Void> delete(@PathVariable Long id);
    
    @GetMapping("/{code}")
    @Operation(summary = "根据编码查询模板")
    Result<MessageTemplateVO> getByCode(@PathVariable String code);
    
    @PostMapping("/page")
    @Operation(summary = "分页查询模板")
    Result<Paging<MessageTemplateVO>> page(@RequestBody @Valid TemplatePageParam param);
}

/**
 * 渠道管理（非Feign接口，管理后台直接调用）
 */
@RestController
@RequestMapping("/message/channel")
@Tag(name = "消息渠道管理")
public class MessageChannelController {
    
    @PostMapping
    @Operation(summary = "创建渠道")
    Result<Long> create(@RequestBody @Valid ChannelCreateParam param);
    
    @PutMapping("/{id}")
    @Operation(summary = "更新渠道")
    Result<Void> update(@PathVariable Long id, @RequestBody @Valid ChannelUpdateParam param);
    
    @PostMapping("/{code}/enable")
    @Operation(summary = "启用渠道")
    Result<Void> enable(@PathVariable String code);
    
    @PostMapping("/{code}/disable")
    @Operation(summary = "禁用渠道")
    Result<Void> disable(@PathVariable String code);
    
    @GetMapping("/{code}")
    @Operation(summary = "根据编码查询渠道")
    Result<MessageChannelVO> getByCode(@PathVariable String code);
    
    @PostMapping("/page")
    @Operation(summary = "分页查询渠道")
    Result<Paging<MessageChannelVO>> page(@RequestBody @Valid ChannelPageParam param);
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

| 版本  | 日期         | 修订人          | 修订内容                                                                                                                                                                                           |
|-----|------------|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.0 | 2026-03-11 | AI Assistant | 初始版本                                                                                                                                                                                           |
| 2.0 | 2026-03-11 | AI Assistant | 更新数据模型，与 DATABASE_SCHEMA.md 保持一致：<br>1. 表名统一改为 message_* 前缀<br>2. 字段定义与数据库保持一致<br>3. 新增 message_type 表设计<br>4. 完善 message_receiver 状态流转说明<br>5. 更新接口设计，符合 Feign 规范<br>6. 新增管理接口 Controller 层定义 |
