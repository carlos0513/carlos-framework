# Carlos Message 数据库设计文档

> 版本: 2.0  
> 日期: 2026-03-11  
> 状态: 已评审并优化

---

## 版本历史

| 版本  | 日期         | 修改内容               | 作者           |
|-----|------------|--------------------|--------------|
| 1.0 | 2026-03-11 | 初始版本               | -            |
| 2.0 | 2026-03-11 | 优化字段命名、完善索引、统一状态设计 | AI Assistant |

---

## 表清单

| 表名           | 说明     | 预估数据量   | 保留策略 |
|--------------|--------|---------|------|
| msg_type     | 消息类型表  | < 200条  | 永久   |
| msg_template | 消息模板表  | < 1000条 | 永久   |
| msg_channel  | 渠道配置表  | < 50条   | 永久   |
| msg_record   | 消息记录表  | 20万条/天  | 30天  |
| msg_receiver | 消息接收人表 | 40万条/天  | 30天  |
| msg_send_log | 发送日志表  | 20万条/天  | 7天   |

---

## 设计原则

1. **状态分离原则**：消息整体状态用统计字段，单接收人状态用状态字段
2. **命名一致性**：所有表都包含 `create_by/update_by/create_time/update_time`
3. **索引优化**：针对高频查询场景设计复合索引
4. **字段合理性**：必填字段只保留真正必须的，其他允许NULL

---

## 详细表结构

### 0. msg_type - 消息类型表

```sql
CREATE TABLE msg_type
(
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
INSERT INTO msg_type (id, type_code, type_name, is_enabled) VALUES
(1, 'TASK_NOTIFY', '任务通知', 1),
(2, 'VERIFY_CODE', '验证码', 1),
(3, 'SYSTEM_NOTIFY', '系统通知', 1),
(4, 'APPROVAL_REMIND', '审批提醒', 1),
(5, 'MARKETING_PUSH', '营销推送', 1);
```

---

### 1. msg_template - 消息模板表

```sql
CREATE TABLE msg_template
(
    id               BIGINT(20)  NOT NULL COMMENT '主键ID',
    type_id          BIGINT(20)  NOT NULL COMMENT '消息类型ID，关联msg_type.id',
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
INSERT INTO msg_template (type_id, template_code, template_name, title_template, content_template, param_schema, channel_config, is_enabled) VALUES
(1, 'ORDER_SUCCESS', '订单支付成功', '订单通知', '尊敬的${userName}，您的订单${orderNo}已支付成功，金额￥${amount}。', 
 '{"userName":"string","orderNo":"string","amount":"string"}', 
 '{"SMS":{"templateCode":"SMS_123"},"DINGTALK":{"agentId":"123"}}', 1),
(2, 'VERIFY_CODE', '验证码', NULL, '您的验证码是${code}，10分钟内有效，请勿泄露。', 
 '{"code":"string"}', 
 '{"SMS":{"templateCode":"SMS_456"}}', 1);
```

---

### 2. msg_channel - 渠道配置表

```sql
CREATE TABLE msg_channel
(
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
INSERT INTO msg_channel (channel_code, channel_name, channel_type, provider, channel_config, weight, is_enabled) VALUES
('ALIYUN_SMS', '阿里云短信', 1, 'aliyun', '{"accessKey":"xxx","secretKey":"xxx","signName":"xxx","endpoint":"dysmsapi.aliyuncs.com"}', 100, 1),
('TENCENT_SMS', '腾讯云短信', 1, 'tencent', '{"secretId":"xxx","secretKey":"xxx","smsSdkAppId":"xxx"}', 80, 1),
('DINGTALK_WORK', '钉钉工作通知', 3, 'dingtalk', '{"appKey":"xxx","appSecret":"xxx","agentId":"xxx"}', 100, 1);
```

---

### 3. msg_record - 消息记录表（核心表）

**设计说明：**

- 一条消息（msg_id）可以发送给多个接收人
- 本表记录消息的整体信息，不包含单个接收人的发送状态
- 发送统计通过 `total_count/success_count/fail_count` 字段记录

```sql
CREATE TABLE msg_record
(
    id               BIGINT       NOT NULL COMMENT '主键ID',
    msg_id           VARCHAR(64)  NOT NULL COMMENT '消息唯一标识，业务ID，如：MSG_2024031112000001',
    
    -- 模板信息
    template_code    VARCHAR(32)  NOT NULL COMMENT '模板编码，关联msg_template',
    template_params  JSON                  DEFAULT NULL COMMENT '模板参数JSON，如：{"userName":"张三"}',
    
    -- 消息内容（渲染后的最终内容）
    msg_type         VARCHAR(32)  NOT NULL COMMENT '消息类型编码，关联msg_type.type_code',
    msg_title        VARCHAR(255)          DEFAULT NULL COMMENT '消息标题（渲染后）',
    msg_content      TEXT         NOT NULL COMMENT '消息内容（渲染后）',
    
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
    UNIQUE KEY uk_msg_id (msg_id),
    INDEX idx_template_code (template_code),
    INDEX idx_msg_type (msg_type),
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

- `msg_id` 业务唯一标识，用于幂等控制和查询
- `msg_title/msg_content` 存储渲染后的最终内容，便于查询和重发
- 统计字段实时更新或通过定时任务汇总更新

---

### 4. msg_receiver - 消息接收人表

**设计说明：**

- 记录每个接收人在每个渠道的发送情况
- 一条消息发给N个人，会产生N条记录（同一接收人多渠道会生成多条）
- 状态流转：待发送(0) → 发送中(1) → 已发送(2) → 送达(3) → 已读(4) 或 失败(5) → 撤回(6)

```sql
CREATE TABLE msg_receiver
(
    id                BIGINT       NOT NULL COMMENT '主键ID',
    msg_id            VARCHAR(64)  NOT NULL COMMENT '消息ID，关联msg_record.msg_id',
    channel_code      VARCHAR(32)  NOT NULL COMMENT '渠道编码，如：ALIYUN_SMS',
    
    -- 接收人信息
    receiver_id       VARCHAR(64)  NOT NULL COMMENT '接收者ID，如用户ID',
    receiver_type     TINYINT               DEFAULT 1 COMMENT '接收人类型: 1-用户 2-部门 3-角色',
    receiver_number   VARCHAR(128)          DEFAULT NULL COMMENT '接收地址，如手机号/邮箱/钉钉ID',
    receiver_audience VARCHAR(64)           DEFAULT NULL COMMENT '接收者设备标识，用于推送',
    channel_msg_id    VARCHAR(64)           DEFAULT NULL COMMENT '渠道返回的消息ID，用于查询渠道状态',
    
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
    INDEX idx_msg_id (msg_id),
    INDEX idx_channel_code (channel_code),
    INDEX idx_receiver_id (receiver_id),
    INDEX idx_status (status),
    -- 复合索引，优化高频查询
    INDEX idx_receiver_status (receiver_id, status),
    INDEX idx_channel_status (channel_code, status),
    INDEX idx_msg_status (msg_id, status),
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
SELECT * FROM msg_receiver 
WHERE receiver_id = ? AND status = 0 
ORDER BY create_time DESC;
-- 使用索引: idx_receiver_status

-- 2. 查询某条消息的所有接收人状态
SELECT * FROM msg_receiver 
WHERE msg_id = ? 
ORDER BY create_time;
-- 使用索引: idx_msg_id

-- 3. 查询某渠道的失败记录（用于重试）
SELECT * FROM msg_receiver 
WHERE channel_code = ? AND status = 5 AND fail_count < 3 
ORDER BY create_time 
LIMIT 100;
-- 使用索引: idx_channel_status

-- 4. 查询定时发送队列
SELECT * FROM msg_receiver 
WHERE status = 0 AND schedule_time <= NOW() 
ORDER BY schedule_time 
LIMIT 1000;
-- 使用索引: idx_schedule
```

---

### 5. msg_send_log - 发送日志表

**设计说明：**

- 记录每次发送的详细日志，包括请求参数和响应结果
- 用于问题排查、对账、审计
- 数据量大，建议保留7天后清理

```sql
CREATE TABLE msg_send_log
(
    id            BIGINT       NOT NULL COMMENT '主键ID',
    msg_id        VARCHAR(64)  NOT NULL COMMENT '消息ID，关联msg_record',
    receiver_id   BIGINT       NOT NULL COMMENT '接收人记录ID，关联msg_receiver.id',
    channel_code  VARCHAR(32)  NOT NULL COMMENT '渠道编码',
    
    -- 请求响应（用于排查问题）
    request_param MEDIUMTEXT   COMMENT '发送请求参数',
    response_data MEDIUMTEXT   COMMENT '渠道返回数据',
    
    -- 执行结果
    is_success    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否成功: 0-失败 1-成功',
    error_code    VARCHAR(32)  COMMENT '错误码',
    error_msg     VARCHAR(512) COMMENT '错误信息',
    cost_time     INT          COMMENT '耗时，单位毫秒',
    
    -- 审计
    create_time   DATETIME     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_msg_id (msg_id),
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

## 数据清理策略

```sql
-- ============================================
-- 定时清理任务（建议每日凌晨2点执行）
-- ============================================

-- 1. 清理30天前的消息记录（已完成的消息）
DELETE FROM msg_record 
WHERE create_time < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- 2. 清理30天前的接收人记录
DELETE FROM msg_receiver 
WHERE create_time < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- 3. 清理7天前的发送日志
DELETE FROM msg_send_log 
WHERE create_time < DATE_SUB(NOW(), INTERVAL 7 DAY);

-- 4. 归档重要数据（可选）
-- 将30天前的数据插入到历史表
INSERT INTO msg_record_history 
SELECT * FROM msg_record 
WHERE create_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
```

---

## 分库分表建议（未来扩展）

当单表数据量超过500万条时，建议按以下方案分表：

### 方案1：按时间分表（推荐）

```sql
-- 每月一张表
msg_record_202401
msg_record_202402
msg_record_202403
...

-- 应用层根据时间路由
String tableName = "msg_record_" + formatDate(now, "yyyyMM");
```

### 方案2：按用户ID取模

```sql
-- 16张分表
msg_record_00  -- userId % 16 = 0
msg_record_01  -- userId % 16 = 1
...
msg_record_15
```

### 方案3：使用ShardingSphere（推荐生产环境）

```yaml
# 配置示例
spring:
  shardingsphere:
    rules:
      sharding:
        tables:
          msg_record:
            actual-data-nodes: ds0.msg_record_$->{2024..2025}0$->{1..9},ds0.msg_record_$->{2024..2025}1$->{0..2}
            table-strategy:
              standard:
                sharding-column: create_time
                sharding-algorithm-name: month-algorithm
```

---

## 执行脚本

### 完整建表脚本

见文件：[database_init.sql](./database_init.sql)

### 增量更新脚本（从V1.0到V2.0）

见文件：[database_upgrade_v2.sql](./database_upgrade_v2.sql)

---

**文档版本: 2.0**  
**最后更新: 2026-03-11**  
**更新说明: 根据评审意见优化字段命名、完善索引、统一状态设计**
