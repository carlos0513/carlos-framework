-- ============================================
-- Carlos Message 数据库初始化脚本
-- 版本: 2.0
-- 日期: 2026-03-11
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS carlos_message
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE carlos_message;

-- ============================================
-- 0. 消息类型表
-- ============================================
DROP TABLE IF EXISTS msg_type;
CREATE TABLE msg_type
(
    id          BIGINT(20)  NOT NULL COMMENT '主键ID',
    type_code   VARCHAR(32) NOT NULL COMMENT '类型编码',
    type_name   VARCHAR(32) NOT NULL COMMENT '类型名称',
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

-- 插入初始数据
INSERT INTO msg_type (id, type_code, type_name, is_enabled)
VALUES (1, 'TASK_NOTIFY', '任务通知', 1),
       (2, 'VERIFY_CODE', '验证码', 1),
       (3, 'SYSTEM_NOTIFY', '系统通知', 1),
       (4, 'APPROVAL_REMIND', '审批提醒', 1),
       (5, 'MARKETING_PUSH', '营销推送', 1);

-- ============================================
-- 1. 消息模板表
-- ============================================
DROP TABLE IF EXISTS msg_template;
CREATE TABLE msg_template
(
    id               BIGINT(20)  NOT NULL COMMENT '主键ID',
    type_id          BIGINT(20)  NOT NULL COMMENT '消息类型ID',
    template_code    VARCHAR(50) NOT NULL COMMENT '模板编码',
    template_name    VARCHAR(50) NOT NULL COMMENT '模板名称',
    title_template   VARCHAR(100)         DEFAULT NULL COMMENT '标题模板',
    content_template TEXT        NOT NULL COMMENT '模板内容',
    param_schema     JSON                 DEFAULT NULL COMMENT '参数定义JSON',
    channel_config   JSON                 DEFAULT NULL COMMENT '渠道特殊配置',
    is_enabled       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否启用: 0-禁用 1-启用 2-草稿',
    is_deleted       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
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

-- ============================================
-- 2. 渠道配置表
-- ============================================
DROP TABLE IF EXISTS msg_channel;
CREATE TABLE msg_channel
(
    id               BIGINT      NOT NULL COMMENT '主键ID',
    channel_code     VARCHAR(32) NOT NULL COMMENT '渠道编码',
    channel_name     VARCHAR(32) NOT NULL COMMENT '渠道名称',
    channel_type     TINYINT     NOT NULL DEFAULT 1 COMMENT '渠道类型: 1-短信 2-邮件 3-钉钉 4-企业微信 5-站内信',
    provider         VARCHAR(32)          DEFAULT NULL COMMENT '服务商编码',
    channel_config   JSON        NOT NULL COMMENT '渠道配置JSON',
    rate_limit_qps   INT                  DEFAULT 100 COMMENT '每秒最大请求数',
    rate_limit_burst INT                  DEFAULT 200 COMMENT '突发流量限制',
    retry_times      TINYINT              DEFAULT 3 COMMENT '最大重试次数',
    retry_interval   INT                  DEFAULT 1000 COMMENT '重试间隔(ms)',
    weight           INT                  DEFAULT 100 COMMENT '权重',
    is_enabled       TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用: 0-禁用 1-启用 2-故障',
    is_deleted       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by        BIGINT               DEFAULT NULL COMMENT '创建者编号',
    create_time      DATETIME             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by        BIGINT               DEFAULT NULL COMMENT '更新者编号',
    update_time      DATETIME             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY uk_channel_code (channel_code),
    INDEX idx_channel_type (channel_type),
    INDEX idx_is_enabled (is_enabled)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息渠道配置'
  ROW_FORMAT = DYNAMIC;

-- ============================================
-- 3. 消息记录表（核心表）
-- ============================================
DROP TABLE IF EXISTS msg_record;
CREATE TABLE msg_record
(
    id               BIGINT      NOT NULL COMMENT '主键ID',
    msg_id           VARCHAR(64) NOT NULL COMMENT '消息唯一标识',
    template_code    VARCHAR(32) NOT NULL COMMENT '模板编码',
    template_params  JSON                 DEFAULT NULL COMMENT '模板参数JSON',
    msg_type         VARCHAR(32) NOT NULL COMMENT '消息类型编码',
    msg_title        VARCHAR(255)         DEFAULT NULL COMMENT '消息标题',
    msg_content      TEXT        NOT NULL COMMENT '消息内容',
    sender_id        VARCHAR(32) NOT NULL COMMENT '发送人ID',
    sender_name      VARCHAR(64)          DEFAULT NULL COMMENT '发送人名称',
    sender_system    VARCHAR(50) NOT NULL COMMENT '发送系统标识',
    feedback_type    VARCHAR(16)          DEFAULT NULL COMMENT '操作反馈类型',
    feedback_content VARCHAR(255)         DEFAULT NULL COMMENT '操作反馈内容',
    priority         TINYINT     NOT NULL DEFAULT 3 COMMENT '优先级: 1-紧急 2-高 3-普通 4-低',
    valid_until      DATETIME             DEFAULT NULL COMMENT '消息有效期',
    total_count      INT                  DEFAULT 0 COMMENT '总接收人数',
    success_count    INT                  DEFAULT 0 COMMENT '成功发送数',
    fail_count       INT                  DEFAULT 0 COMMENT '失败发送数',
    extras           JSON                 DEFAULT NULL COMMENT '扩展信息',
    create_by        BIGINT      NULL     DEFAULT NULL COMMENT '创建人',
    create_time      DATETIME    NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by        BIGINT      NULL     DEFAULT NULL COMMENT '更新人',
    update_time      DATETIME    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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

-- ============================================
-- 4. 消息接收人表
-- ============================================
DROP TABLE IF EXISTS msg_receiver;
CREATE TABLE msg_receiver
(
    id                BIGINT      NOT NULL COMMENT '主键ID',
    msg_id            VARCHAR(64) NOT NULL COMMENT '消息ID',
    channel_code      VARCHAR(32) NOT NULL COMMENT '渠道编码',
    receiver_id       VARCHAR(64) NOT NULL COMMENT '接收者ID',
    receiver_type     TINYINT              DEFAULT 1 COMMENT '接收人类型: 1-用户 2-部门 3-角色',
    receiver_number   VARCHAR(128)         DEFAULT NULL COMMENT '接收地址',
    receiver_audience VARCHAR(64)          DEFAULT NULL COMMENT '接收者设备标识',
    channel_msg_id    VARCHAR(64)          DEFAULT NULL COMMENT '渠道返回的消息ID',
    status            TINYINT     NOT NULL DEFAULT 0 COMMENT '状态: 0-待发送 1-发送中 2-已发送 3-送达 4-已读 5-失败 6-撤回',
    fail_count        TINYINT              DEFAULT 0 COMMENT '失败次数',
    fail_reason       VARCHAR(512)         DEFAULT NULL COMMENT '失败原因',
    schedule_time     DATETIME             DEFAULT NULL COMMENT '定时发送时间',
    send_time         DATETIME             DEFAULT NULL COMMENT '实际发送时间',
    deliver_time      DATETIME             DEFAULT NULL COMMENT '送达时间',
    read_time         DATETIME             DEFAULT NULL COMMENT '阅读时间',
    callback_url      VARCHAR(512)         DEFAULT NULL COMMENT '回调URL',
    extras            JSON                 DEFAULT NULL COMMENT '扩展信息',
    create_by         BIGINT      NULL     DEFAULT NULL COMMENT '创建人',
    create_time       DATETIME    NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by         BIGINT      NULL     DEFAULT NULL COMMENT '更新人',
    update_time       DATETIME    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_msg_id (msg_id),
    INDEX idx_channel_code (channel_code),
    INDEX idx_receiver_id (receiver_id),
    INDEX idx_status (status),
    INDEX idx_receiver_status (receiver_id, status),
    INDEX idx_channel_status (channel_code, status),
    INDEX idx_msg_status (msg_id, status),
    INDEX idx_schedule (schedule_time, status),
    INDEX idx_create_time (create_time)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息接收人表'
  ROW_FORMAT = DYNAMIC;

-- ============================================
-- 5. 发送日志表
-- ============================================
DROP TABLE IF EXISTS msg_send_log;
CREATE TABLE msg_send_log
(
    id            BIGINT      NOT NULL COMMENT '主键ID',
    msg_id        VARCHAR(64) NOT NULL COMMENT '消息ID',
    receiver_id   BIGINT      NOT NULL COMMENT '接收人记录ID',
    channel_code  VARCHAR(32) NOT NULL COMMENT '渠道编码',
    request_param MEDIUMTEXT COMMENT '请求参数',
    response_data MEDIUMTEXT COMMENT '响应数据',
    is_success    TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否成功: 0-失败 1-成功',
    error_code    VARCHAR(32) COMMENT '错误码',
    error_msg     VARCHAR(512) COMMENT '错误信息',
    cost_time     INT COMMENT '耗时(ms)',
    create_time   DATETIME    NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
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

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 初始化完成
-- ============================================
SELECT 'Carlos Message 数据库初始化完成' AS result;
SHOW TABLES;
