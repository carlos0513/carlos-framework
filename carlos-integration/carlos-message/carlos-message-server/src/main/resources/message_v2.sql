CREATE DATABASE carlos_message;

SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS msg_type;
CREATE TABLE msg_type
(
    id          BIGINT(20)  NOT NULL COMMENT '主键ID',
    type_code   VARCHAR(32) NOT NULL COMMENT '类型编码',
    type_name   VARCHAR(32) NOT NULL COMMENT '类型名称',
    is_enabled  TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用',
    is_deleted  TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by   BIGINT(32)           DEFAULT NULL COMMENT '创建者编号',
    create_time DATETIME             DEFAULT NULL COMMENT '创建时间',
    update_by   BIGINT(32)           DEFAULT NULL COMMENT '更新者编号',
    update_time DATETIME             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息类型'
  ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS msg_template;
CREATE TABLE msg_template
(
    id               BIGINT(20)  NOT NULL COMMENT '主键ID',
    type_id          BIGINT(20)  NOT NULL COMMENT '消息类型',
    template_code    VARCHAR(50) NOT NULL COMMENT '模板编码',
    template_name    VARCHAR(50) NOT NULL COMMENT '模板名称',
    title_template   VARCHAR(50) NOT NULL COMMENT '标题模板',
    content_template TEXT        NOT NULL COMMENT '模板内容(含变量占位符)',
    param_schema     JSON COMMENT '参数定义，如：{"userName": "string", "orderNo": "string"}',
    channel_config   TEXT                 DEFAULT NULL COMMENT '渠道特殊配置(如短信模板ID),配置对应渠道编码',
    is_enabled       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否启用',
    is_deleted       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by        BIGINT(32)           DEFAULT NULL COMMENT '创建者编号',
    create_time      DATETIME             DEFAULT NULL COMMENT '创建时间',
    update_by        BIGINT(32)           DEFAULT NULL COMMENT '更新者编号',
    update_time      DATETIME             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息模板'
  ROW_FORMAT = DYNAMIC;


-- 渠道配置表
DROP TABLE IF EXISTS msg_channel;
CREATE TABLE msg_channel
(
    id               BIGINT      NOT NULL COMMENT '主键ID',
    channel_code     VARCHAR(32) NOT NULL COMMENT '渠道编码',
    channel_name     VARCHAR(32) NOT NULL COMMENT '渠道名称',
    channel_config   JSON        NOT NULL COMMENT '样例配置信息',
    provider         VARCHAR(32) COMMENT '服务商编码',
    rate_limit_qps   INT                  DEFAULT 100 COMMENT '每秒最大请求数',
    rate_limit_burst INT                  DEFAULT 200 COMMENT '突发流量限制',
    retry_times      TINYINT              DEFAULT 3 COMMENT '最大重试次数',
    retry_interval   INT                  DEFAULT 1000 COMMENT '重试间隔(ms)',
    weight           INT                  DEFAULT 100 COMMENT '权重(0-1000)',
    is_enabled       TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用',
    is_deleted       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by        BIGINT               DEFAULT NULL COMMENT '创建者编号',
    create_time      DATETIME             DEFAULT NULL COMMENT '创建时间',
    update_by        BIGINT               DEFAULT NULL COMMENT '更新者编号',
    update_time      DATETIME             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY uk_channel_code (channel_code)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息渠道配置'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS msg_record;
CREATE TABLE msg_record
(
    id               BIGINT       NOT NULL COMMENT '主键',
    msg_id           VARCHAR(64)  NOT NULL COMMENT '消息唯一标识（业务ID，如：MSG_2024031112000001）',
    template_code    VARCHAR(32)  NOT NULL COMMENT '模板code',
    template_params  JSON COMMENT '模板参数',
    msg_type         VARCHAR(255) NOT NULL COMMENT '消息类型',
    msg_title        VARCHAR(255) NOT NULL COMMENT '标题',
    msg_content      TEXT         NOT NULL COMMENT '消息内容',
    sender_id        VARCHAR(32)  NOT NULL COMMENT '发送人id',
    sender_name      VARCHAR(16)  NOT NULL COMMENT '发送人名称',
    sender_system    VARCHAR(50)  NOT NULL COMMENT '系统来源标识',
    feedback_type    VARCHAR(16)  NOT NULL COMMENT '操作反馈类型(无, 详情, 站内跳转, 外链)',
    feedback_content VARCHAR(255) NOT NULL COMMENT '操作反馈内容',
    priority         TINYINT(2)   NOT NULL COMMENT '优先级',
    -- 多渠道多条记录
    channel_code     VARCHAR(16)  NOT NULL COMMENT '推送渠道(短信、站内信、钉钉等)',
    extras           JSON COMMENT '扩展信息',
    is_delete        TINYINT(1)   NOT NULL COMMENT '是否发送成功',
    create_by        BIGINT       NULL DEFAULT NULL COMMENT '创建人',
    create_time      DATETIME     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY uk_msg_id (msg_id)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息记录表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for message_receiver
-- ----------------------------
DROP TABLE IF EXISTS msg_receiver;
CREATE TABLE msg_receiver
(
    id                BIGINT       NOT NULL COMMENT '主键',
    msg_id            VARCHAR(64)  NOT NULL COMMENT '消息id',
    receiver_id       VARCHAR(255) NOT NULL COMMENT '接收者id',
    receiver_type     TINYINT(2)        DEFAULT 1 COMMENT '接收人类型: 1-用户 2-部门 3-角色',
    receiver_number   VARCHAR(255) NOT NULL COMMENT '接收者号码 钉钉号 手机号码',
    receiver_audience VARCHAR(255) NOT NULL COMMENT '接收者设备',

    -- 状态流转（核心字段）
    status            TINYINT(2)        DEFAULT 0 COMMENT '状态: 0-待发送 1-发送中 2-已发送 3-送达 4-已读 5-失败 6-撤回',
    -- 时间追踪
    schedule_time     DATETIME COMMENT '定时发送时间',
    send_time         DATETIME COMMENT '实际发送时间',
    deliver_time      DATETIME COMMENT '送达时间',
    read_time         DATETIME COMMENT '阅读时间',
    -- 失败信息
    fail_count        TINYINT           DEFAULT 0 COMMENT '失败次数',
    fail_reason       VARCHAR(512) COMMENT '失败原因',
    -- 回调
    callback_url      VARCHAR(512) COMMENT '回调地址',
    -- 扩展字段
    extras            JSON COMMENT '扩展信息',
    create_by         BIGINT       NULL DEFAULT NULL COMMENT '创建人',
    create_time       DATETIME     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息接受者'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS msg_send_log;
CREATE TABLE msg_send_log
(
    id            BIGINT      NOT NULL COMMENT '主键',
    msg_id        VARCHAR(64) NOT NULL COMMENT '消息id',
    channel_code  VARCHAR(32) NOT NULL COMMENT '推送渠道(短信、站内信、钉钉等)',
    retry_count   TINYINT              DEFAULT 0 COMMENT '重试次数',
    send_time     DATETIME    NOT NULL COMMENT '发送时间',
    request_param MEDIUMTEXT COMMENT '原始请求参数',
    response_data MEDIUMTEXT COMMENT '渠道返回数据',
    is_success    TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否发送成功 0 失败 1 成功',
    error_msg     VARCHAR(512) COMMENT '错误信息',
    cost_time     INT COMMENT '耗时(ms)',
    create_time   DATETIME    NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_msg_id (msg_id),
    INDEX idx_create_time (create_time)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息发送日志'
  ROW_FORMAT = DYNAMIC;











