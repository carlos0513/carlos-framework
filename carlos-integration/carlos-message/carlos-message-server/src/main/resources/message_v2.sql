CREATE DATABASE carlos_message;

SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS msg_message_type;
CREATE TABLE msg_message_type
(
    id          BIGINT(20)  NOT NULL COMMENT '主键ID',
    type_code   VARCHAR(32) NOT NULL COMMENT '类型编码',
    type_name   VARCHAR(32) NOT NULL COMMENT '类型名称',
    is_enabled  TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用',
    is_deleted  TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by   CHAR(32)             DEFAULT NULL COMMENT '创建者编号',
    create_time DATETIME             DEFAULT NULL COMMENT '创建时间',
    update_by   CHAR(32)             DEFAULT NULL COMMENT '更新者编号',
    update_time DATETIME             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息类型'
  ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS msg_message_template;
CREATE TABLE msg_message_template
(
    id               BIGINT(20)  NOT NULL COMMENT '主键ID',
    type_id          BIGINT(20)  NOT NULL COMMENT '消息类型',
    template_code    VARCHAR(50) NOT NULL COMMENT '模板编码',
    template_content TEXT        NOT NULL COMMENT '模板内容(含变量占位符)',
    channel_config   TEXT                 DEFAULT NULL COMMENT '渠道特殊配置(如短信模板ID),配置对应渠道编码',
    is_active        TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否启用',
    is_deleted       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by        CHAR(32)             DEFAULT NULL COMMENT '创建者编号',
    create_time      DATETIME             DEFAULT NULL COMMENT '创建时间',
    update_by        CHAR(32)             DEFAULT NULL COMMENT '更新者编号',
    update_time      DATETIME             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息模板'
  ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS msg_message;
CREATE TABLE msg_message
(
    id               BIGINT(20)   NOT NULL COMMENT '主键',
    template_id      BIGINT(20)   NOT NULL COMMENT '关联message_template.id',
    sender           VARCHAR(50)  NOT NULL COMMENT '系统来源标识',
    message_type     VARCHAR(255) NOT NULL COMMENT '消息类型',
    message_title    VARCHAR(255) NOT NULL COMMENT '标题',
    message_content  TEXT         NOT NULL COMMENT '消息内容',
    message_remark   VARCHAR(255) NOT NULL COMMENT '消息备注',
    source_business  VARCHAR(32)  NOT NULL COMMENT '系统来源标识',
    send_user_id     VARCHAR(32)  NOT NULL COMMENT '发送人id',
    send_user_name   VARCHAR(16)  NOT NULL COMMENT '发送人名称',
    feedback_type    VARCHAR(16)  NOT NULL COMMENT '操作反馈类型(无, 详情, 站内跳转, 外链)',
    feedback_content VARCHAR(255) NOT NULL COMMENT '操作反馈内容',
    priority         TINYINT(2)   NOT NULL COMMENT '优先级',
    push_channel     VARCHAR(16)  NOT NULL COMMENT '推送渠道(短信、站内信、钉钉等)',
    is_delete        TINYINT(1)   NOT NULL COMMENT '是否发送成功',
    create_by        VARCHAR(32)  NULL DEFAULT NULL COMMENT '创建人',
    create_time      DATETIME     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息'
  ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS msg_message_send_record;
CREATE TABLE msg_message_send_record
(
    id            BIGINT      NOT NULL COMMENT '主键',
    message_id    BIGINT      NOT NULL COMMENT '消息id',
    retry_count   TINYINT              DEFAULT 0 COMMENT '重试次数',
    send_time     DATETIME    NOT NULL COMMENT '发送时间',
    request_param MEDIUMTEXT COMMENT '原始请求参数',
    response_data MEDIUMTEXT COMMENT '渠道返回数据',
    push_channel  VARCHAR(16) NOT NULL COMMENT '推送渠道(短信、站内信、钉钉等)',
    is_success    TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否发送成功 0 失败 1 成功',
    create_by     VARCHAR(32) NULL     DEFAULT NULL COMMENT '创建人',
    create_time   DATETIME    NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息发送记录'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for message_receiver
-- ----------------------------
DROP TABLE IF EXISTS msg_message_receiver;
CREATE TABLE msg_message_receiver
(
    id                BIGINT       NOT NULL COMMENT '主键',
    message_id        BIGINT       NOT NULL COMMENT '消息id',
    receiver_id       VARCHAR(255) NOT NULL COMMENT '接收者id',
    receiver_number   VARCHAR(255) NOT NULL COMMENT '接收者号码 钉钉号 手机号码',
    receiver_audience VARCHAR(255) NOT NULL COMMENT '接收者设备',
    is_read           TINYINT(1)   NOT NULL COMMENT '是否已读',
    is_success        TINYINT(1)   NOT NULL COMMENT '是否发送成功',
    create_by         BIGINT(20)   NULL DEFAULT NULL COMMENT '创建人',
    create_time       DATETIME     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息接受者'
  ROW_FORMAT = DYNAMIC;


-- 渠道配置表
DROP TABLE IF EXISTS msg_channel_config;
CREATE TABLE msg_channel_config
(
    id             BIGINT       NOT NULL COMMENT '主键ID',
    channel_code   VARCHAR(32)  NOT NULL COMMENT '渠道编码',
    channel_name   VARCHAR(32)  NOT NULL COMMENT '渠道名称',
    channel_config TEXT         NOT NULL COMMENT '样例配置信息',
    remark         VARCHAR(300) NOT NULL COMMENT '备注信息',
    is_enabled     TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '是否启用',
    is_deleted     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by      CHAR(32)              DEFAULT NULL COMMENT '创建者编号',
    create_time    DATETIME              DEFAULT NULL COMMENT '创建时间',
    update_by      CHAR(32)              DEFAULT NULL COMMENT '更新者编号',
    update_time    DATETIME              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息渠道配置'
  ROW_FORMAT = DYNAMIC;








