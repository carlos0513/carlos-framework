-- ============================================
-- 审计日志主表 (覆盖 90% 查询场景)
-- ============================================
CREATE TABLE `audit_log_main`
(
    -- 主键与分类 (4个字段)
    `id`                   BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `log_schema_version`   TINYINT UNSIGNED NOT NULL DEFAULT 3 COMMENT '日志Schema版本',
    `category`             VARCHAR(20)      NOT NULL COMMENT '大类: SECURITY/BUSINESS/SYSTEM/AUDIT',
    `log_type`             VARCHAR(50)      NOT NULL COMMENT '细类: USER_LOGIN/ORDER_PAY等',

    -- 时间体系 (5个字段)
    `server_time`          DATETIME(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '服务器时间(毫秒精度)',
    `client_time`          DATETIME(3)      NULL COMMENT '客户端时间',
    `event_time`           DATETIME(3)      NULL COMMENT '事件实际发生时间',
    `duration_ms`          INT UNSIGNED     NULL COMMENT '操作耗时(毫秒)',
    `retention_deadline`   DATE             NOT NULL COMMENT '数据保留截止日期(TTL)',

    -- 操作者体系 (6个字段)
    `principal_id`         VARCHAR(64)      NOT NULL COMMENT '操作主体ID',
    `principal_type`       VARCHAR(20)      NOT NULL DEFAULT 'USER' COMMENT '主体类型: USER/SERVICE/SYSTEM/ANONYMOUS',
    `principal_name`       VARCHAR(100)     NULL COMMENT '主体名称(冗余)',
    `tenant_id`            VARCHAR(64)      NOT NULL DEFAULT '0' COMMENT '租户ID(SaaS隔离)',
    `dept_id`              VARCHAR(64)      NULL COMMENT '部门ID',
    `dept_name`    VARCHAR(64) NULL COMMENT '部门名称',
    `dept_path`            VARCHAR(500)     NULL COMMENT '部门层级路径, 如: 1/12/156/',

    -- 目标对象体系 (4个字段)
    `target_type`          VARCHAR(50)      NULL COMMENT '对象类型: ORDER/USER/CONFIG',
    `target_id`            VARCHAR(64)      NULL COMMENT '对象唯一标识',
    `target_name`          VARCHAR(200)     NULL COMMENT '对象显示名称',
    `target_snapshot`      JSON             NULL COMMENT '对象关键信息摘要',

    -- 操作结果 (3个字段)
    `state`                VARCHAR(20)      NOT NULL DEFAULT 'SUCCESS' COMMENT '状态: SUCCESS/FAIL/PENDING/TIMEOUT/PARTIAL_SUCCESS',
    `result_code`          VARCHAR(50)      NULL COMMENT '业务结果码',
    `result_message`       VARCHAR(500)     NULL COMMENT '结果描述',

    -- 内容摘要 (2个字段)
    `operation`            VARCHAR(200)     NOT NULL COMMENT '操作描述(人工可读)',
    `risk_level`           TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '风险等级 0-100',

    -- 网络与设备 (扁平化常用字段 6个)
    `client_ip`            VARCHAR(40)      NULL COMMENT '客户端IP(支持IPv6)',
    `client_port`          INT UNSIGNED     NULL COMMENT '客户端端口',
    `server_ip`            VARCHAR(40)      NULL COMMENT '处理服务器IP',
    `user_agent`           VARCHAR(500)     NULL COMMENT '浏览器UA',
    `device_fingerprint`   VARCHAR(64)      NULL COMMENT '设备指纹',
    `location_country`     VARCHAR(50)      NULL COMMENT '国家',
    `location_province`    VARCHAR(50)      NULL COMMENT '省份',
    `location_city`        VARCHAR(50)      NULL COMMENT '城市',
    `location_lat`         DECIMAL(10, 8)   NULL COMMENT '纬度',
    `location_lon`         DECIMAL(11, 8)   NULL COMMENT '经度',

    -- 认证与权限 (扁平化 4个字段)
    `auth_type`            VARCHAR(20)      NULL COMMENT '认证方式: PASSWORD/SMS/OAUTH2/LDAP/CERT',
    `auth_provider`        VARCHAR(30)      NULL COMMENT '认证源: LOCAL/WECHAT/DINGTALK',
    `roles`                JSON             NULL COMMENT '当前角色列表',
    `permissions`          JSON             NULL COMMENT '当前权限列表',

    -- 业务上下文 (扁平化 5个字段)
    `biz_channel`          VARCHAR(20)      NULL COMMENT '业务渠道: WEB/APP/MINI_PROGRAM/OPEN_API',
    `biz_scene`            VARCHAR(50)      NULL COMMENT '业务场景',
    `biz_order_no`         VARCHAR(64)      NULL COMMENT '业务订单号',
    `related_biz_ids`      JSON             NULL COMMENT '关联业务ID列表',
    `monetary_amount`      DECIMAL(18, 4)   NULL COMMENT '涉及金额',

    -- 批量操作 (3个字段)
    `batch_id`             VARCHAR(64)      NULL COMMENT '批量操作批次号',
    `batch_index`          INT UNSIGNED     NULL COMMENT '批次内序号',
    `batch_total`          INT UNSIGNED     NULL COMMENT '批次总数',

    -- 审批工作流 (4个字段)
    `process_id`           VARCHAR(64)      NULL COMMENT '流程实例ID',
    `task_id`              VARCHAR(64)      NULL COMMENT '任务ID',
    `approver_id`          VARCHAR(64)      NULL COMMENT '审批人ID',
    `approval_comment`     VARCHAR(500)     NULL COMMENT '审批意见',

    -- 数据变更摘要 (关联扩展表)
    `has_data_change`      TINYINT(1)       NOT NULL DEFAULT 0 COMMENT '是否有数据变更详情(0=否,1=是)',
    `entity_class`         VARCHAR(200)     NULL COMMENT '实体类名',
    `table_name`           VARCHAR(100)     NULL COMMENT '数据库表名',
    `change_summary`       VARCHAR(500)     NULL COMMENT '变更摘要',

    -- 技术上下文 (可选分离)
    `technical_context_id` BIGINT UNSIGNED  NULL COMMENT '关联技术上下文表ID',

    -- 动态扩展 (JSON存储)
    `dynamic_tags` JSON        NULL COMMENT '业务标签',
    `dynamic_extras`       JSON             NULL COMMENT '业务扩展字段',

    -- 系统字段
    `created_time`         DATETIME(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_time`         DATETIME(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (`id`),

    -- 核心查询索引
    INDEX `idx_tenant_category_time` (`tenant_id`, `category`, `server_time`),
    INDEX `idx_principal_time` (`principal_id`, `server_time`),
    INDEX `idx_target_audit` (`target_type`, `target_id`, `server_time`),
    INDEX `idx_log_type_time` (`log_type`, `server_time`),

    -- 业务查询索引
    INDEX `idx_biz_order` (`biz_order_no`, `server_time`),
    INDEX `idx_batch` (`batch_id`, `batch_index`),
    INDEX `idx_process` (`process_id`),

    -- 安全风控索引
    INDEX `idx_ip_time` (`client_ip`, `server_time`),
    INDEX `idx_device` (`device_fingerprint`, `server_time`),
    INDEX `idx_risk` (`risk_level`, `server_time`),

    -- 时间范围查询 (覆盖TTL删除)
    INDEX `idx_retention` (`retention_deadline`),

    -- 复合覆盖索引 (减少回表)
    INDEX `idx_cover_principal` (`principal_id`, `principal_type`, `tenant_id`, `server_time`, `operation`, `state`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='审计日志主表';


-- ============================================
-- 数据变更详情表 (仅业务操作日志使用)
-- ============================================
CREATE TABLE `audit_log_data_change`
(
    `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `audit_log_id`        BIGINT UNSIGNED NOT NULL COMMENT '关联主表ID',

    -- 变更前数据 (压缩存储大JSON)
    `old_data`            LONGTEXT        NULL COMMENT '变更前数据(JSON)',
    `old_data_compressed` TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '是否压缩',

    -- 变更后数据
    `new_data`            LONGTEXT        NULL COMMENT '变更后数据(JSON)',
    `new_data_compressed` TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '是否压缩',

    -- 统计信息
    `changed_field_count` INT UNSIGNED    NULL COMMENT '变更字段数量',

    `created_time`        DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_audit_log` (`audit_log_id`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='审计日志-数据变更详情';


-- ============================================
-- 技术上下文表 (大字段分离，降低主表IO)
-- ============================================
CREATE TABLE `audit_log_technical`
(
    `id`                    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `audit_log_id`          BIGINT UNSIGNED NOT NULL COMMENT '关联主表ID',

    -- 分布式追踪
    `trace_id`              VARCHAR(64)     NULL COMMENT '全链路追踪ID',
    `span_id`               VARCHAR(32)     NULL COMMENT '当前Span',
    `parent_span_id`        VARCHAR(32)     NULL COMMENT '父Span',
    `trace_path`            JSON            NULL COMMENT '调用链路节点',

    -- 性能指标
    `db_query_count`        INT UNSIGNED    NULL COMMENT '数据库查询次数',
    `db_query_time_ms`      INT UNSIGNED    NULL COMMENT '数据库查询耗时',
    `external_call_count`   INT UNSIGNED    NULL COMMENT '外部接口调用次数',
    `external_call_time_ms` INT UNSIGNED    NULL COMMENT '外部接口耗时',
    `custom_metrics`        JSON            NULL COMMENT '自定义指标',

    -- 原始数据 (大对象，可存对象存储，此处存引用)
    `request_payload_ref`   VARCHAR(500)    NULL COMMENT '请求数据存储引用(OSS/MinIO)',
    `response_payload_ref`  VARCHAR(500)    NULL COMMENT '响应数据存储引用',
    `payload_storage_type` TINYINT(2) NULL COMMENT '存储类型: OSS/S3/MINIO/DB',

    -- 环境信息
    `app_name`              VARCHAR(50)     NULL COMMENT '应用名',
    `app_version`           VARCHAR(30)     NULL COMMENT '应用版本',
    `cluster`               VARCHAR(50)     NULL COMMENT '集群标识',
    `host_name`             VARCHAR(100)    NULL COMMENT '主机名',

    `created_time`          DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_audit_log` (`audit_log_id`),
    INDEX `idx_trace` (`trace_id`, `span_id`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='审计日志-技术上下文';
-- ============================================
-- 动态标签表 (倒排索引，支持灵活查询)
-- ============================================
CREATE TABLE `audit_log_tags`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `audit_log_id` BIGINT UNSIGNED NOT NULL,

    `tag_key`      VARCHAR(50)     NOT NULL COMMENT '标签键',
    `tag_value`    VARCHAR(200)    NOT NULL COMMENT '标签值',

    `created_time` DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_log_tag` (`audit_log_id`, `tag_key`),
    INDEX `idx_tag_query` (`tag_key`, `tag_value`, `audit_log_id`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='审计日志-动态标签';

-- ============================================
-- 附件引用表
-- ============================================
CREATE TABLE `audit_log_attachments`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `audit_log_id`    BIGINT UNSIGNED NOT NULL,

    `attachment_type` VARCHAR(20)     NOT NULL COMMENT '类型: IMAGE/VIDEO/FILE/SNAPSHOT',
    `storage_type`    VARCHAR(20)     NOT NULL COMMENT '存储: OSS/S3/MINIO/GRIDFS',
    `bucket_name`     VARCHAR(100)    NULL COMMENT '存储桶',
    `object_key`      VARCHAR(500)    NOT NULL COMMENT '对象键',
    `file_name`       VARCHAR(200)    NULL COMMENT '原始文件名',
    `file_size`       BIGINT UNSIGNED NULL COMMENT '文件大小(字节)',
    `content_type`    VARCHAR(100)    NULL COMMENT 'MIME类型',
    `created_time`    DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    PRIMARY KEY (`id`),
    INDEX `idx_log` (`audit_log_id`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='审计日志-附件引用';



-- ============================================
-- 日志归档记录表 (冷数据管理)
-- ============================================
CREATE TABLE `audit_log_archive_record`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    `archive_id`      VARCHAR(64)     NOT NULL COMMENT '归档批次ID',
    `archive_date`    DATE            NOT NULL COMMENT '归档日期',
    `start_time`      DATETIME        NOT NULL COMMENT '归档数据起始时间',
    `end_time`        DATETIME        NOT NULL COMMENT '归档数据结束时间',

    `record_count`    BIGINT UNSIGNED NOT NULL COMMENT '归档记录数',
    `file_size_bytes` BIGINT UNSIGNED NOT NULL COMMENT '归档文件大小',
    `storage_path`    VARCHAR(500)    NOT NULL COMMENT '存储路径',
    `storage_type`    VARCHAR(20)     NOT NULL COMMENT '存储类型: OSS/S3/LOCAL',

    `verify_checksum` VARCHAR(64)     NULL COMMENT '校验和',
    `state` VARCHAR(32) NOT NULL DEFAULT 'SUCCESS' COMMENT '状态',

    `created_time`    DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_archive_date` (`archive_date`),
    INDEX `idx_time_range` (`start_time`, `end_time`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='审计日志归档记录';

-- ============================================
-- 日志配置表 (动态TTL与采样)
-- ============================================
CREATE TABLE `audit_log_config`
(
    `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `log_type`          VARCHAR(50)     NOT NULL COMMENT '日志类型',
    `retention_days`    INT UNSIGNED    NOT NULL DEFAULT 90 COMMENT '保留天数',
    `sampling_rate`     DECIMAL(3, 2)   NOT NULL DEFAULT 1.00 COMMENT '采样率 0.00-1.00',
    `async_write`       TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '是否异步写入',
    `store_data_change` TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '是否存储数据变更',
    `store_technical`   TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '是否存储技术上下文',
    is_deleted          TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除，0：未删除，1：已删除',
    create_by   BIGINT               DEFAULT NULL COMMENT '创建者编号',
    create_time DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    update_by   BIGINT               DEFAULT NULL COMMENT '更新者编号',
    update_time DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `tenant_id` VARCHAR(64) NOT NULL DEFAULT '0' COMMENT '租户ID，0表示系统级',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_type` (`tenant_id`, `log_type`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='审计日志配置';
