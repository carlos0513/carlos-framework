-- ============================================
-- MySQL: 审计日志宽主表（合并原5张业务表）
-- 用于：代码生成器生成Java实体、近期数据查询
-- ============================================

CREATE TABLE `audit_log_main`
(
    -- ========================================
    -- 1. 主键与时间体系（5个字段）
    -- ========================================
    `id`                    BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键，雪花ID',
    `server_time`           DATETIME(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '服务器时间，毫秒精度，CK分区键',
    `event_date`            DATE             NOT NULL DEFAULT (CURDATE()) COMMENT '事件日期，冗余字段，方便分区',
    `client_time`           DATETIME(3)      NULL COMMENT '客户端时间',
    `event_time`            DATETIME(3)      NULL COMMENT '事件实际发生时间',
    `duration_ms`           INT UNSIGNED     NULL COMMENT '操作耗时，毫秒',
    `retention_deadline`    DATE             NOT NULL COMMENT '数据保留截止日期，TTL删除用',

    -- ========================================
    -- 2. 分类与版本（4个字段）
    -- ========================================
    `log_schema_version`    TINYINT UNSIGNED NOT NULL DEFAULT 3 COMMENT '日志Schema版本，用于兼容性处理',
    `category`              VARCHAR(20)      NOT NULL COMMENT '大类：SECURITY-安全/BUSINESS-业务/SYSTEM-系统/AUDIT-审计',
    `log_type`              VARCHAR(50)      NOT NULL COMMENT '细类：USER_LOGIN-登录/ORDER_PAY-支付/DATA_EXPORT-数据导出等',
    `risk_level`            TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '风险等级 0-100，0为无风险，100为极高风险',

    -- ========================================
    -- 3. 操作者体系（7个字段）
    -- ========================================
    `principal_id`          VARCHAR(64)      NOT NULL COMMENT '操作主体ID，用户ID或服务账号',
    `principal_type`        VARCHAR(20)      NOT NULL DEFAULT 'USER' COMMENT '主体类型：USER-用户/SERVICE-服务/SYSTEM-系统/ANONYMOUS-匿名',
    `principal_name`        VARCHAR(100)     NULL COMMENT '主体名称，冗余存储避免关联查询',
    `tenant_id`             VARCHAR(64)      NOT NULL DEFAULT '0' COMMENT '租户ID，SaaS多租户隔离字段',
    `dept_id`               VARCHAR(64)      NULL COMMENT '部门ID',
    `dept_name`             VARCHAR(64)      NULL COMMENT '部门名称，冗余存储',
    `dept_path`             VARCHAR(500)     NULL COMMENT '部门层级路径，如：1/12/156/，方便层级查询',

    -- ========================================
    -- 4. 目标对象体系（4个字段）
    -- ========================================
    `target_type`           VARCHAR(50)      NULL COMMENT '对象类型：ORDER-订单/USER-用户/CONFIG-配置/DATA-数据',
    `target_id`             VARCHAR(64)      NULL COMMENT '对象唯一标识',
    `target_name`           VARCHAR(200)     NULL COMMENT '对象显示名称，冗余存储',
    `target_snapshot`       JSON             NULL COMMENT '对象关键信息摘要，JSON格式存储关键字段',

    -- ========================================
    -- 5. 操作结果（5个字段）
    -- ========================================
    `state`                 VARCHAR(20)      NOT NULL DEFAULT 'SUCCESS' COMMENT '状态：SUCCESS-成功/FAIL-失败/PENDING-处理中/TIMEOUT-超时/PARTIAL_SUCCESS-部分成功',
    `result_code`           VARCHAR(50)      NULL COMMENT '业务结果码，用于细分错误类型',
    `result_message`        VARCHAR(500)     NULL COMMENT '结果描述，前500字符',
    `operation`             VARCHAR(200)     NOT NULL COMMENT '操作描述，人工可读的操作说明',
    `approval_comment`      VARCHAR(500)     NULL COMMENT '审批意见，工作流用',

    -- ========================================
    -- 6. 网络与设备（10个字段）
    -- ========================================
    `client_ip`             VARCHAR(40)      NULL COMMENT '客户端IP，支持IPv6',
    `client_port`           INT UNSIGNED     NULL COMMENT '客户端端口',
    `server_ip`             VARCHAR(40)      NULL COMMENT '处理服务器IP',
    `user_agent`            VARCHAR(500)     NULL COMMENT '浏览器UA，用于设备识别',
    `device_fingerprint`    VARCHAR(64)      NULL COMMENT '设备指纹，唯一标识设备',
    `location_country`      VARCHAR(50)      NULL COMMENT '国家',
    `location_province`     VARCHAR(50)      NULL COMMENT '省份',
    `location_city`         VARCHAR(50)      NULL COMMENT '城市',
    `location_lat`          DECIMAL(10, 8)   NULL COMMENT '纬度',
    `location_lon`          DECIMAL(11, 8)   NULL COMMENT '经度',

    -- ========================================
    -- 7. 认证与权限（4个字段）
    -- ========================================
    `auth_type`             VARCHAR(20)      NULL COMMENT '认证方式：PASSWORD-密码/SMS-短信/OAUTH2-OAuth2/LDAP-LDAP/CERT-证书',
    `auth_provider`         VARCHAR(30)      NULL COMMENT '认证源：LOCAL-本地/WECHAT-微信/DINGTALK-钉钉',
    `roles`                 JSON             NULL COMMENT '当前角色列表，JSON数组',
    `permissions`           JSON             NULL COMMENT '当前权限列表，JSON数组',

    -- ========================================
    -- 8. 业务上下文（6个字段）
    -- ========================================
    `biz_channel`           VARCHAR(20)      NULL COMMENT '业务渠道：WEB-网页/APP-移动应用/MINI_PROGRAM-小程序/OPEN_API-开放接口',
    `biz_scene`             VARCHAR(50)      NULL COMMENT '业务场景，如：订单创建-ORDER_CREATE',
    `biz_order_no`          VARCHAR(64)      NULL COMMENT '业务订单号，用于关联订单',
    `related_biz_ids`       JSON             NULL COMMENT '关联业务ID列表，JSON数组',
    `monetary_amount`       DECIMAL(18, 4)   NULL COMMENT '涉及金额，精确到分',
    `process_id`            VARCHAR(64)      NULL COMMENT '流程实例ID，工作流引擎生成',

    -- ========================================
    -- 9. 批量操作（3个字段）
    -- ========================================
    `batch_id`              VARCHAR(64)      NULL COMMENT '批量操作批次号，UUID',
    `batch_index`           INT UNSIGNED     NULL COMMENT '批次内序号，从0开始',
    `batch_total`           INT UNSIGNED     NULL COMMENT '批次总数',

    -- ========================================
    -- 10. 审批工作流（2个字段，其余合并到操作结果）
    -- ========================================
    `task_id`               VARCHAR(64)      NULL COMMENT '任务ID',
    `approver_id`           VARCHAR(64)      NULL COMMENT '审批人ID',

    -- ========================================
    -- 11. 数据变更详情（原audit_log_data_change表扁平化，13个字段）
    -- ========================================
    `has_data_change`       TINYINT(1)       NOT NULL DEFAULT 0 COMMENT '是否有数据变更详情：0-否/1-是',
    `entity_class`          VARCHAR(200)     NULL COMMENT '实体类名，如：com.example.Order',
    `table_name`            VARCHAR(100)     NULL COMMENT '数据库表名，如：t_order',
    `change_summary`        VARCHAR(500)     NULL COMMENT '变更摘要，人工可读的变更说明',
    `changed_field_count`   INT UNSIGNED     NULL COMMENT '变更字段数量',

    -- 完整变更数据（JSON存储，替代原LONGTEXT）
    `old_data`              JSON             NULL COMMENT '变更前完整数据，JSON格式',
    `new_data`              JSON             NULL COMMENT '变更后完整数据，JSON格式',
    `old_data_compressed`   TINYINT(1)       NOT NULL DEFAULT 0 COMMENT '旧数据是否压缩：0-否/1-是',
    `new_data_compressed`   TINYINT(1)       NOT NULL DEFAULT 0 COMMENT '新数据是否压缩：0-否/1-是',

    -- 关键字段变更扁平化（加速查询，避免解析JSON）
    `change_field_1_name`   VARCHAR(100)     NULL COMMENT '变更字段1名称',
    `change_field_1_old`    VARCHAR(500)     NULL COMMENT '变更字段1旧值，前500字符',
    `change_field_1_new`    VARCHAR(500)     NULL COMMENT '变更字段1新值，前500字符',

    -- ========================================
    -- 12. 技术上下文（原audit_log_technical表扁平化，18个字段）
    -- ========================================
    `trace_id`              VARCHAR(64)      NULL COMMENT '全链路追踪ID，如SkyWalking TraceId',
    `span_id`               VARCHAR(32)      NULL COMMENT '当前Span ID',
    `parent_span_id`        VARCHAR(32)      NULL COMMENT '父Span ID',
    `trace_path`            JSON             NULL COMMENT '调用链路节点，JSON数组',

    -- 性能指标
    `db_query_count`        INT UNSIGNED     NULL COMMENT '数据库查询次数',
    `db_query_time_ms`      INT UNSIGNED     NULL COMMENT '数据库查询耗时，毫秒',
    `external_call_count`   INT UNSIGNED     NULL COMMENT '外部接口调用次数',
    `external_call_time_ms` INT UNSIGNED     NULL COMMENT '外部接口耗时，毫秒',
    `custom_metrics`        JSON             NULL COMMENT '自定义指标，JSON格式',

    -- Payload存储引用（避免存大字段）
    `request_payload_ref`   VARCHAR(500)     NULL COMMENT '请求数据OSS引用',
    `response_payload_ref`  VARCHAR(500)     NULL COMMENT '响应数据OSS引用',
    `payload_storage_type`  VARCHAR(20)      NULL COMMENT '存储类型：OSS/S3/MINIO/DB',

    -- 环境信息
    `app_name`              VARCHAR(50)      NULL COMMENT '应用名，如：order-service',
    `app_version`           VARCHAR(30)      NULL COMMENT '应用版本，如：1.2.3',
    `cluster`               VARCHAR(50)      NULL COMMENT '集群标识，如：prod-cluster-1',
    `host_name`             VARCHAR(100)     NULL COMMENT '主机名',

    -- ========================================
    -- 13. 动态标签（原audit_log_tags表Array化，2个字段）
    -- ========================================
    `tag_keys`              JSON             NULL COMMENT '标签键数组，如：[\'env\',\'module\']',
    `tag_values`            JSON             NULL COMMENT '标签值数组，如：[\'prod\',\'order\']',

    -- ========================================
    -- 14. 附件元数据（原audit_log_attachments表统计化，5个字段）
    -- ========================================
    `attachment_count`      TINYINT UNSIGNED          DEFAULT 0 COMMENT '附件数量',
    `attachment_types`      JSON             NULL COMMENT '附件类型数组，如：[\'IMAGE\',\'PDF\']',
    `attachment_total_size` BIGINT UNSIGNED  NULL COMMENT '附件总大小，字节',
    `first_attachment_ref`  VARCHAR(500)     NULL COMMENT '第一个附件的OSS引用，快速预览',
    `attachment_refs`       JSON             NULL COMMENT '所有附件引用数组，JSON格式',

    -- ========================================
    -- 15. 动态扩展（2个字段）
    -- ========================================
    `dynamic_tags`          JSON             NULL COMMENT '业务标签，JSON格式',
    `dynamic_extras`        JSON             NULL COMMENT '业务扩展字段，JSON格式',

    -- ========================================
    -- 16. 系统字段（3个字段）
    -- ========================================
    `created_time`          DATETIME(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '记录创建时间',
    `updated_time`          DATETIME(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '记录更新时间',
    `deleted`               TINYINT(1)       NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除/1-已删除',

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
    INDEX `idx_trace` (`trace_id`),

    -- 安全风控索引
    INDEX `idx_ip_time` (`client_ip`, `server_time`),
    INDEX `idx_device` (`device_fingerprint`, `server_time`),
    INDEX `idx_risk` (`risk_level`, `server_time`),

    -- 时间范围查询（覆盖TTL删除）
    INDEX `idx_retention` (`retention_deadline`),
    INDEX `idx_server_time` (`server_time`),

    -- 复合覆盖索引（减少回表）
    INDEX `idx_cover_principal` (`principal_id`, `principal_type`, `tenant_id`, `server_time`, `operation`, `state`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='审计日志宽主表（合并数据变更、技术上下文、标签、附件，保留7天热数据）';


-- ============================================
-- MySQL: 日志归档记录表（保持独立）
-- ============================================

CREATE TABLE `audit_log_archive_record`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',

    `archive_id`      VARCHAR(64)     NOT NULL COMMENT '归档批次ID，UUID',
    `archive_date`    DATE            NOT NULL COMMENT '归档日期',
    `start_time`      DATETIME        NOT NULL COMMENT '归档数据起始时间',
    `end_time`        DATETIME        NOT NULL COMMENT '归档数据结束时间',

    `record_count`    BIGINT UNSIGNED NOT NULL COMMENT '归档记录数',
    `file_size_bytes` BIGINT UNSIGNED NOT NULL COMMENT '归档文件大小，字节',
    `storage_path`    VARCHAR(500)    NOT NULL COMMENT '存储路径',
    `storage_type`    VARCHAR(20)     NOT NULL COMMENT '存储类型：OSS/S3/LOCAL',

    `verify_checksum` VARCHAR(64)     NULL COMMENT '校验和，MD5或SHA256',
    `state`           VARCHAR(32)     NOT NULL DEFAULT 'SUCCESS' COMMENT '状态：SUCCESS-成功/FAILED-失败/PROCESSING-处理中',

    `created_time`    DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_archive_date` (`archive_date`),
    INDEX `idx_time_range` (`start_time`, `end_time`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='审计日志归档记录（管理冷数据归档）';


-- ============================================
-- MySQL: 日志配置表（保持独立）
-- ============================================

CREATE TABLE `audit_log_config`
(
    `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `log_type`          VARCHAR(50)     NOT NULL COMMENT '日志类型，如：USER_LOGIN',
    `retention_days`    INT UNSIGNED    NOT NULL DEFAULT 90 COMMENT '保留天数',
    `sampling_rate`     DECIMAL(3, 2)   NOT NULL DEFAULT 1.00 COMMENT '采样率 0.00-1.00，1.00为全量',
    `async_write`       TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '是否异步写入：0-同步/1-异步',
    `store_data_change` TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '是否存储数据变更：0-否/1-是',
    `store_technical`   TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '是否存储技术上下文：0-否/1-是',
    `deleted`           TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除/1-已删除',

    `tenant_id`         VARCHAR(64)     NOT NULL DEFAULT '0' COMMENT '租户ID，0表示系统级配置',
    `create_by`         BIGINT          NULL COMMENT '创建者编号',
    `create_time`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_by`         BIGINT          NULL COMMENT '更新者编号',
    `update_time`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_type` (`tenant_id`, `log_type`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='审计日志配置（动态TTL与采样策略）';
