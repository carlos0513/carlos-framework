-- =========================================
-- OAuth2 认证服务 - 用户和权限表
-- =========================================
-- 创建日期: 2026-02-26
-- 版本: 1.0.0
-- 作者: Carlos
-- =========================================

-- 用户表
CREATE TABLE IF NOT EXISTS `auth_user`
(
    `id`             BIGINT       NOT NULL COMMENT '用户ID',
    `username`       VARCHAR(50)  NOT NULL COMMENT '用户名',
    `email`          VARCHAR(100)          DEFAULT NULL COMMENT '邮箱',
    `phone`          VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    `password`       VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    `status`         VARCHAR(20)  NOT NULL DEFAULT 'ENABLE' COMMENT '状态: ENABLE-启用, DISABLE-禁用, LOCKED-锁定',
    `login_attempts` INT                   DEFAULT 0 COMMENT '登录失败次数',
    `lock_time`      DATETIME              DEFAULT NULL COMMENT '锁定时间',
    `mfa_enabled`    TINYINT(1)            DEFAULT 0 COMMENT 'MFA是否启用',
    `mfa_secret`     VARCHAR(100)          DEFAULT NULL COMMENT 'MFA密钥',
    `is_deleted`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除状态: 0-未删除, 1-已删除',
    `create_by`      BIGINT                DEFAULT NULL COMMENT '创建人',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      BIGINT                DEFAULT NULL COMMENT '更新人',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_username` (`username`),
    KEY `idx_email` (`email`),
    KEY `idx_phone` (`phone`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `auth_role`
(
    `id`          BIGINT       NOT NULL COMMENT '角色ID',
    `role_code`   VARCHAR(50)  NOT NULL COMMENT '角色编码: ADMIN-管理员, USER-普通用户',
    `role_name`   VARCHAR(100) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(255)          DEFAULT NULL COMMENT '描述',
    `is_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除状态: 0-未删除, 1-已删除',
    `create_by`   BIGINT                DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT                DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`),
    KEY `idx_role_code` (`role_code`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `auth_user_role`
(
    `id`          BIGINT     NOT NULL COMMENT '关联ID',
    `user_id`     BIGINT     NOT NULL COMMENT '用户ID',
    `role_id`     BIGINT     NOT NULL COMMENT '角色ID',
    `is_deleted`  TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除状态: 0-未删除, 1-已删除',
    `create_by`   BIGINT              DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT              DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

-- MFA恢复码表
CREATE TABLE IF NOT EXISTS `auth_mfa_recovery_code`
(
    `id`          BIGINT      NOT NULL COMMENT '恢复码ID',
    `user_id`     BIGINT      NOT NULL COMMENT '用户ID',
    `code`        VARCHAR(20) NOT NULL COMMENT '恢复码',
    `used`        TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否已使用: 0-未使用, 1-已使用',
    `used_time`   DATETIME             DEFAULT NULL COMMENT '使用时间',
    `is_deleted`  TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '删除状态: 0-未删除, 1-已删除',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_code` (`code`),
    KEY `idx_used` (`used`) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='MFA恢复码表';

-- 可信设备表
CREATE TABLE IF NOT EXISTS `auth_trusted_device`
(
    `id`                 BIGINT      NOT NULL COMMENT '设备ID',
    `user_id`            BIGINT      NOT NULL COMMENT '用户ID',
    `device_fingerprint` VARCHAR(50) NOT NULL COMMENT '设备指纹（MD5）',
    `device_name`        VARCHAR(100)         DEFAULT NULL COMMENT '设备名称',
    `ip_address`         VARCHAR(45)          DEFAULT NULL COMMENT 'IP地址',
    `location`           VARCHAR(100)         DEFAULT NULL COMMENT '地理位置',
    `user_agent`         VARCHAR(500)         DEFAULT NULL COMMENT 'User-Agent',
    `browser`            VARCHAR(50)          DEFAULT NULL COMMENT '浏览器名称',
    `is_mobile`          TINYINT(1)           DEFAULT 0 COMMENT '是否为移动设备: 0-否, 1-是',
    `trusted_time`       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加为可信时间',
    `last_used_time`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后使用时间',
    `is_deleted`         TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '删除状态: 0-未删除, 1-已删除',
    `create_time`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_device` (`user_id`, `device_fingerprint`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_last_used_time` (`last_used_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='可信设备表';

-- 安全告警表
CREATE TABLE IF NOT EXISTS `auth_security_alert`
(
    `id`           BIGINT       NOT NULL COMMENT '告警ID',
    `user_id`      BIGINT                DEFAULT NULL COMMENT '用户ID',
    `username`     VARCHAR(50)           DEFAULT NULL COMMENT '用户名',
    `alert_type`   VARCHAR(50)  NOT NULL COMMENT '告警类型',
    `severity`     VARCHAR(20)  NOT NULL COMMENT '告警级别: low/medium/high/critical',
    `title`        VARCHAR(100) NOT NULL COMMENT '告警标题',
    `content`      TEXT COMMENT '告警内容',
    `ip_address`   VARCHAR(45)           DEFAULT NULL COMMENT 'IP地址',
    `location`     VARCHAR(100)          DEFAULT NULL COMMENT '地理位置',
    `user_agent`   VARCHAR(500)          DEFAULT NULL COMMENT 'User-Agent',
    `handled`      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否已处理: 0-未处理, 1-已处理',
    `handled_time` DATETIME              DEFAULT NULL COMMENT '处理时间',
    `remark`       VARCHAR(500) COMMENT '备注',
    `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_alert_type` (`alert_type`),
    KEY `idx_severity` (`severity`),
    KEY `idx_handled` (`handled`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='安全告警表';

-- =========================================
-- 插入默认数据
-- =========================================

-- 默认角色
INSERT INTO `auth_role` (`id`, `role_code`, `role_name`, `description`)
VALUES (1, 'ADMIN', '管理员', '系统管理员，拥有所有权限');

INSERT INTO `auth_role` (`id`, `role_code`, `role_name`, `description`)
VALUES (2, 'USER', '普通用户', '普通用户，拥有基本权限');

-- 默认管理员用户（密码: admin123，BCrypt加密）
INSERT INTO `auth_user` (`id`, `username`, `email`, `phone`, `password`, `status`, `login_attempts`)
VALUES (1, 'admin', 'admin@carlos.com', '13800000000', '$2a$10$r0Ag1lLQGJRhCUYe7lGE5ebsj4WfKJUeJUxIrD8I6g9J9uC6zvyI2',
        'ENABLE', 0);

-- 分配管理员角色
INSERT INTO `auth_user_role` (`id`, `user_id`, `role_id`)
VALUES (1, 1, 1);

-- =========================================
-- 登录审计表（按月分区）
CREATE TABLE IF NOT EXISTS `auth_audit_login_2026_03`
(
    `id`            BIGINT      NOT NULL COMMENT '日志ID',
    `user_id`       BIGINT COMMENT '用户ID',
    `username`      VARCHAR(50) COMMENT '用户名',
    `client_id`     VARCHAR(100) COMMENT '客户端ID',
    `event_type`    VARCHAR(50) NOT NULL COMMENT '事件类型: LOGIN-登录、LOGOUT-登出、REFRESH-刷新、LOCKED-锁定',
    `ip_address`    VARCHAR(45) COMMENT 'IP地址',
    `location`      VARCHAR(100) COMMENT '地理位置',
    `user_agent`    VARCHAR(500) COMMENT 'User-Agent',
    `status`        VARCHAR(20) NOT NULL COMMENT '状态: SUCCESS-成功、FAILURE-失败',
    `error_message` TEXT COMMENT '错误消息',
    `login_time`    DATETIME COMMENT '登录时间',
    `session_id`    VARCHAR(100) COMMENT '会话ID',
    `create_time`   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_ip_address` (`ip_address`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='登录审计日志（2026年3月）';

-- 下月分区表示例（2026年4月）
-- CREATE TABLE IF NOT EXISTS `auth_audit_login_2026_04` ...

-- 操作审计表
CREATE TABLE IF NOT EXISTS `auth_audit_operation`
(
    `id`             BIGINT      NOT NULL COMMENT '审计ID',
    `user_id`        BIGINT      NOT NULL COMMENT '用户ID',
    `username`       VARCHAR(50) COMMENT '用户名',
    `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型: UPDATE_PASSWORD-修改密码、DISABLE_MFA-禁用MFA',
    `resource_type`  VARCHAR(50) COMMENT '资源类型: USER-用户、MFA_DEVICE-MFA设备',
    `resource_id`    VARCHAR(100) COMMENT '资源ID',
    `before_value`   TEXT COMMENT '操作前值（JSON格式）',
    `after_value`    TEXT COMMENT '操作后值（JSON格式）',
    `ip_address`     VARCHAR(45) COMMENT 'IP地址',
    `user_agent`     VARCHAR(500) COMMENT 'User-Agent',
    `create_time`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_resource_id` (`resource_id`),
    KEY `idx_operation_type` (`operation_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作审计表';

-- =========================================
-- 存储过程和定时任务（生产环境使用）
-- =========================================
-- 示例：定时创建下个月分区表的存储过程
-- DELIMITER //
-- CREATE PROCEDURE create_next_month_partition()
-- BEGIN
--     DECLARE next_month VARCHAR(20);
--     DECLARE table_name VARCHAR(50);
--     DECLARE create_table_sql TEXT;
--
--     SET next_month = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 MONTH), '%Y_%m');
--     SET table_name = CONCAT('audit_login_', next_month);
--
--     SET create_table_sql = CONCAT(
--         'CREATE TABLE IF NOT EXISTS ', table_name, ' (
--             `id` BIGINT NOT NULL COMMENT ''日志ID'',
--             `user_id` BIGINT COMMENT ''用户ID'',
--             `username` VARCHAR(50) COMMENT ''用户名'',
--             `client_id` VARCHAR(100) COMMENT ''客户端ID'',
--             `event_type` VARCHAR(50) NOT NULL COMMENT ''事件类型'',
--             `ip_address` VARCHAR(45) COMMENT ''IP地址'',
--             `location` VARCHAR(100) COMMENT ''地理位置'',
--             `user_agent` VARCHAR(500) COMMENT ''User-Agent'',
--             `status` VARCHAR(20) NOT NULL COMMENT ''状态'',
--             `error_message` TEXT COMMENT ''错误消息'',
--             `login_time` DATETIME COMMENT ''登录时间'',
--             `session_id` VARCHAR(100) COMMENT ''会话ID'',
--             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
--             PRIMARY KEY (`id`),
--             KEY `idx_user_id` (`user_id`),
--             KEY `idx_ip_address` (`ip_address`),
--             KEY `idx_status` (`status`),
--             KEY `idx_create_time` (`create_time`)
--         ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=''登录审计日志 (', next_month, ')'''
--     );
--
--     PREPARE stmt FROM create_table_sql;
--     EXECUTE stmt;
--     DEALLOCATE PREPARE stmt;
--
--     SELECT CONCAT('Partition table ', table_name, ' created successfully');
-- END //
-- DELIMITER ;
--
-- 每月1日凌晨3点创建下月分区表
-- CREATE EVENT create_audit_table_monthly
-- ON SCHEDULE EVERY 1 MONTH
-- STARTS CONCAT(DATE_FORMAT(CURDATE() + INTERVAL 1 MONTH, '%Y-%m-01'), ' 03:00:00')
-- DO
-- CALL create_next_month_partition();

-- 数据说明
-- =========================================
-- 1. 管理员账号: admin / admin123
-- 2. 默认角色: ADMIN（管理员）、USER（普通用户）
-- 3. 用户状态说明:
--    - ENABLE: 账号正常启用
--    - DISABLE: 账号已禁用
--    - LOCKED: 账号已被锁定（登录失败次数过多）
-- 4. MFA字段:
--    - mfa_enabled: 0-未启用, 1-已启用
--    - mfa_secret: 启用MFA时存储密钥（base32编码）
-- 5. 可信设备: 记录用户常用登录设备，用于异地/新设备检测
-- 6. 登录审计: 按月分区表，记录所有登录事件，保留90天
-- 7. 安全告警: 记录安全相关事件，未处理告警需要关注
-- 8. 恢复码: 每用户10组8位随机码，使用后失效
-- 6. 恢复码: MFA备用恢复码，每用户10组8位随机码
