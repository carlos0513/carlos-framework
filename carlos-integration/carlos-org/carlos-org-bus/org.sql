SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for org_user
-- ----------------------------
DROP TABLE IF EXISTS `org_user`;
CREATE TABLE `org_user`
(
    `id`              BIGINT       NOT NULL COMMENT '主键',
    `account`         VARCHAR(20)  NOT NULL COMMENT '用户名',
    `realname`        VARCHAR(20)  NOT NULL COMMENT '真实姓名',
    `pwd`             VARCHAR(64)  NOT NULL COMMENT '密码',
    `identify`        VARCHAR(18)  NULL     DEFAULT NULL COMMENT '证件号码',
    `phone`           VARCHAR(11)  NULL     DEFAULT NULL COMMENT '手机号码',
    `address`         VARCHAR(100) NULL     DEFAULT NULL COMMENT '详细地址',
    `gender`          TINYINT(2)   NULL     DEFAULT NULL COMMENT '性别，0：保密, 1：男，2：女，默认0',
    `email`           VARCHAR(30)  NULL     DEFAULT NULL COMMENT '邮箱',
    `head`            VARCHAR(200) NULL     DEFAULT NULL COMMENT '头像',
    `description`     VARCHAR(200) NULL     DEFAULT NULL COMMENT '备注',
    `state`           TINYINT(2)   NULL     DEFAULT NULL COMMENT '状态，0：禁用，1：启用，2：锁定',
    `main_dept_id`    BIGINT       NULL     DEFAULT NULL COMMENT '主部门id',
    `last_login`      DATETIME     NULL     DEFAULT NULL COMMENT '最后登录时间',
    `login_count`     INT          NOT NULL DEFAULT 0 COMMENT '登录次数',
    `version`         INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
    `pwd_last_modify` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '密码最后修改时间',
    `is_deleted`      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除，0：未删除，1：已删除',
    `create_by`       BIGINT       NULL     DEFAULT NULL COMMENT '创建者',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`       BIGINT       NULL     DEFAULT NULL COMMENT '修改者',
    `update_time`     DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `tenant_id`       BIGINT       NULL     DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_main_dept` (`main_dept_id`, `state`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统用户'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for org_department
-- ----------------------------
DROP TABLE IF EXISTS `org_department`;
CREATE TABLE `org_department`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `parent_id`   BIGINT       NULL     DEFAULT '0' COMMENT '父id',
    `dept_name`   VARCHAR(64)  NOT NULL COMMENT '部门名称',
    `dept_code`   VARCHAR(64)  NULL     DEFAULT NULL COMMENT '部门编号',
    `path`        VARCHAR(500) NULL NOT NULL COMMENT '部门路径',
    `leader_id`   BIGINT       NOT NULL COMMENT '负责人id',
    `state`       TINYINT(2)   NOT NULL DEFAULT '1' COMMENT '状态，0：禁用，1：启用',
    `sort`        TINYINT      NOT NULL DEFAULT 0 COMMENT '排序',
    `level`       INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '层级',
    `description` VARCHAR(200) NULL     DEFAULT NULL COMMENT '备注',
    `version`     INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
    `is_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除，0：未删除，1：已删除',
    `create_by`   BIGINT       NOT NULL COMMENT '创建者',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT       NULL     DEFAULT NULL COMMENT '修改者',
    `update_time` DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `tenant_id`   BIGINT       NULL     DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_dept_code` (`dept_code` ASC) USING BTREE,
    INDEX `idx_path` (`path`(100)) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '部门'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for org_role
-- ----------------------------
DROP TABLE IF EXISTS `org_role`;
CREATE TABLE `org_role`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `role_name`   VARCHAR(40)  NOT NULL COMMENT '角色名称',
    `role_code`   VARCHAR(100) NULL     DEFAULT NULL COMMENT '角色唯一编码',
    `role_type`   TINYINT(2)   NOT NULL DEFAULT '1' COMMENT '角色类型， 1：系统角色, 2: 自定义角色',
    `data_scope`  TINYINT(2)   NOT NULL DEFAULT '1' COMMENT '数据范围， 1：全部, 2: 本部及子部门, 3:仅本部门, 4:仅本人, 5:自定义规则',
    `state`       TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '角色状态， 0：禁用, 1: 启用',
    `description` VARCHAR(200) NULL     DEFAULT '' COMMENT '备注',
    `version`     INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本',
    `is_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除，0：未删除，1：已删除',
    `create_by`   BIGINT       NULL     DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME     NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT       NULL     DEFAULT NULL COMMENT '修改者',
    `update_time` DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `tenant_id`   BIGINT       NULL     DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统角色'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for org_user_department
-- ----------------------------
DROP TABLE IF EXISTS `org_user_department`;
CREATE TABLE `org_user_department`
(
    `id`            BIGINT      NOT NULL COMMENT '主键',
    `user_id`       BIGINT      NOT NULL COMMENT '用户id',
    `department_id` BIGINT      NOT NULL COMMENT '部门id',
    `is_main`       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否为主部门',
    `position`      VARCHAR(50) NULL     DEFAULT NULL COMMENT '该部门职位',
    `create_by`     BIGINT      NULL     DEFAULT NULL COMMENT '创建者',
    `create_time`   DATETIME    NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE `uk_user_id_department_id` (`user_id` ASC, `department_id` ASC) USING BTREE,
    INDEX `idx_user_id` (`user_id` ASC) USING BTREE,
    INDEX `idx_department_id` (`department_id` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户部门'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `org_role_permission`
(
    `id`            BIGINT   NOT NULL COMMENT '主键',
    `role_id`       BIGINT   NOT NULL COMMENT '角色id',
    `permission_id` BIGINT   NOT NULL COMMENT '权限id',
    `create_by`     BIGINT   NULL DEFAULT NULL COMMENT '创建者',
    `create_time`   DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `tenant_id`     BIGINT        DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_permission_id` (`permission_id`) USING BTREE,
    KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色权限';

-- ----------------------------
-- Table structure for org_user_import
-- ----------------------------
DROP TABLE IF EXISTS `org_user_import`;
CREATE TABLE `org_user_import`
(
    `id`              BIGINT       NOT NULL COMMENT '主键',
    `account`         VARCHAR(20)  NOT NULL COMMENT '用户名',
    `realname`        VARCHAR(20)  NOT NULL COMMENT '真实姓名',
    `identify`        VARCHAR(18)  NULL DEFAULT NULL COMMENT '证件号码',
    `phone`           VARCHAR(11)  NOT NULL COMMENT '手机号码',
    `role`            VARCHAR(16)  NULL DEFAULT NULL COMMENT '角色名称',
    `department`      VARCHAR(200) NULL DEFAULT NULL COMMENT '部门完整信息，以”-“分割部门级别',
    `department_type` VARCHAR(255) NULL DEFAULT NULL COMMENT '部门类型',
    `region_code`     VARCHAR(50)  NULL DEFAULT NULL COMMENT '行政区域编码',
    `gender`          VARCHAR(10)  NULL DEFAULT NULL COMMENT '性别，0：保密, 1：男，2：女，默认0',
    `email`           VARCHAR(30)  NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户导入'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for org_user_role
-- ----------------------------
DROP TABLE IF EXISTS `org_user_role`;
CREATE TABLE `org_user_role`
(
    `id`          BIGINT   NOT NULL COMMENT '主键',
    `user_id`     BIGINT   NOT NULL COMMENT '用户id',
    `role_id`     BIGINT   NOT NULL COMMENT '角色id',
    `dept_id`     BIGINT        DEFAULT NULL COMMENT '角色生效的部门id',
    `expire_time` DATETIME NULL DEFAULT NULL COMMENT '失效时间',
    `create_by`   BIGINT   NULL DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `tenant_id`   BIGINT   NULL DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id` ASC) USING BTREE,
    INDEX `idx_role_id` (`role_id` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户角色'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for org_department_role
-- ----------------------------
DROP TABLE IF EXISTS `org_department_role`;
CREATE TABLE `org_department_role`
(
    `id`            BIGINT     NOT NULL COMMENT '主键',
    `department_id` BIGINT     NOT NULL COMMENT '部门id',
    `role_id`       BIGINT     NOT NULL COMMENT '角色id',
    `version`       INT        NOT NULL DEFAULT 0 COMMENT '版本',
    `is_default`    TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为默认角色',
    `create_by`     BIGINT     NULL     DEFAULT NULL COMMENT '创建者',
    `create_time`   TIMESTAMP  NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   TIMESTAMP  NULL     DEFAULT NULL COMMENT '修改时间',
    `tenant_id`     BIGINT     NULL     DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_dept_role` (`department_id`, `role_id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '部门角色'
  ROW_FORMAT = DYNAMIC;


-- ==================== 4. 权限表（新增） ====================
DROP TABLE IF EXISTS `org_permission`;
CREATE TABLE `org_permission`
(
    `id`           BIGINT       NOT NULL COMMENT '主键，UUID',
    `parent_id`    BIGINT       NOT NULL DEFAULT '0' COMMENT '父权限ID，0为根节点',
    `perm_name`    VARCHAR(50)  NOT NULL COMMENT '权限名称，如"用户新增"',
    `perm_code`    VARCHAR(100) NOT NULL COMMENT '权限编码，如"user:create"，程序控制使用',
    `perm_type`    TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '类型：1菜单，2按钮，3API接口，4数据字段',
    `resource_url` VARCHAR(200) NULL     DEFAULT NULL COMMENT '资源路径：菜单为路由，API为接口路径',
    `method`       VARCHAR(10)  NULL     DEFAULT NULL COMMENT 'HTTP方法：GET/POST/PUT/DELETE，仅API类型有效',
    `icon`         VARCHAR(50)  NULL     DEFAULT NULL COMMENT '菜单图标',
    `sort`         INT          NOT NULL DEFAULT 0 COMMENT '同级排序',
    `state`        TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `version`      INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted`   TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_by`    BIGINT       NULL     DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME     NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`    BIGINT       NULL     DEFAULT NULL COMMENT '修改者ID',
    `update_time`  DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `tenant_id`    BIGINT       NULL     DEFAULT NULL COMMENT '租户ID，NULL表示系统通用',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_perm_code` (`tenant_id`, `perm_code`) USING BTREE,
    KEY `idx_parent_type` (`parent_id`, `perm_type`, `state`) USING BTREE,
    KEY `idx_resource` (`resource_url`, `method`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '权限表'
  ROW_FORMAT = DYNAMIC;

-- ==================== 操作日志表 ====================
DROP TABLE IF EXISTS `org_operation_log`;
CREATE TABLE `org_operation_log`
(
    `id`               BIGINT       NOT NULL COMMENT '主键，UUID',
    `log_type`         TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '日志类型：1操作日志，2登录日志，3异常日志，4数据变更日志',
    `module`           VARCHAR(50)  NOT NULL COMMENT '功能模块，如：用户管理、角色管理',
    `operation`        VARCHAR(100) NOT NULL COMMENT '操作描述，如：创建用户、分配角色',
    `operation_code`   VARCHAR(50)  NOT NULL COMMENT '操作编码，如：user:create、role:assign',
    `request_method`   VARCHAR(10)  NULL     DEFAULT NULL COMMENT '请求方法：GET/POST/PUT/DELETE',
    `request_url`      VARCHAR(500) NULL     DEFAULT NULL COMMENT '请求URL',
    `request_params`   TEXT         NULL     DEFAULT NULL COMMENT '请求参数（JSON格式，敏感字段脱敏）',
    `response_data`    TEXT         NULL     DEFAULT NULL COMMENT '响应数据（JSON格式，失败时记录错误信息）',
    `execute_time`     INT          NULL     DEFAULT 0 COMMENT '执行时长（毫秒）',
    `operation_status` TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '操作状态：0失败，1成功',
    `error_msg`        VARCHAR(500) NULL     DEFAULT NULL COMMENT '错误信息',
    `ip_address`       VARCHAR(50)  NULL     DEFAULT NULL COMMENT '操作人IP地址',
    `ip_location`      VARCHAR(100) NULL     DEFAULT NULL COMMENT 'IP归属地（省市区）',
    `user_agent`       VARCHAR(500) NULL     DEFAULT NULL COMMENT '浏览器User-Agent',
    `device_type`      VARCHAR(20)  NULL     DEFAULT NULL COMMENT '设备类型：PC、MOBILE、TABLET',
    `browser`          VARCHAR(50)  NULL     DEFAULT NULL COMMENT '浏览器类型',
    `os`               VARCHAR(50)  NULL     DEFAULT NULL COMMENT '操作系统',
    `business_type`    VARCHAR(50)  NULL     DEFAULT NULL COMMENT '业务类型，用于分类查询',
    `business_id`      BIGINT       NULL     DEFAULT NULL COMMENT '业务主键，关联具体业务数据',
    `old_value`        TEXT         NULL     DEFAULT NULL COMMENT '变更前数据（JSON，数据变更日志用）',
    `new_value`        TEXT         NULL     DEFAULT NULL COMMENT '变更后数据（JSON，数据变更日志用）',
    `diff_value`       TEXT         NULL     DEFAULT NULL COMMENT '差异数据（JSON，自动计算变更字段）',
    `risk_level`       TINYINT(1)   NULL     DEFAULT 0 COMMENT '风险等级：0正常，1低风险，2中风险，3高风险（敏感操作标记）',
    `tenant_id`        BIGINT       NULL     DEFAULT NULL COMMENT '租户ID',
    `operator_id`      BIGINT       NOT NULL COMMENT '操作人ID',
    `operator_name`    VARCHAR(20)  NOT NULL COMMENT '操作人姓名（冗余，方便查询）',
    `dept_id`          BIGINT       NULL     DEFAULT NULL COMMENT '操作人部门ID（冗余）',
    `dept_name`        VARCHAR(50)  NULL     DEFAULT NULL COMMENT '操作人部门名称（冗余）',
    `create_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_operator` (`operator_id`, `create_time`) USING BTREE,
    KEY `idx_module_op` (`module`, `operation_code`, `create_time`) USING BTREE,
    KEY `idx_business` (`business_type`, `business_id`) USING BTREE,
    KEY `idx_tenant_time` (`tenant_id`, `create_time`) USING BTREE,
    KEY `idx_log_type` (`log_type`, `risk_level`, `create_time`) USING BTREE,
    KEY `idx_status` (`operation_status`, `create_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '操作日志表'
  ROW_FORMAT = DYNAMIC;

-- ==================== 登录日志表（从操作日志分离，字段更精简） ====================
DROP TABLE IF EXISTS `org_login_log`;
CREATE TABLE `org_login_log`
(
    `id`           BIGINT       NOT NULL COMMENT '主键，UUID',
    `log_type`     TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '类型：1登录，2登出，3密码修改，4密码错误',
    `account`      VARCHAR(20)  NOT NULL COMMENT '登录账号',
    `user_id`      BIGINT       NULL     DEFAULT NULL COMMENT '用户ID（登录成功时记录）',
    `login_status` TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '状态：0失败，1成功',
    `fail_reason`  VARCHAR(100) NULL     DEFAULT NULL COMMENT '失败原因：密码错误、账号锁定、账号禁用',
    `ip_address`   VARCHAR(50)  NULL     DEFAULT NULL COMMENT 'IP地址',
    `ip_location`  VARCHAR(100) NULL     DEFAULT NULL COMMENT 'IP归属地',
    `user_agent`   VARCHAR(500) NULL     DEFAULT NULL COMMENT 'User-Agent',
    `device_type`  VARCHAR(20)  NULL     DEFAULT NULL COMMENT '设备类型',
    `browser`      VARCHAR(50)  NULL     DEFAULT NULL COMMENT '浏览器',
    `os`           VARCHAR(50)  NULL     DEFAULT NULL COMMENT '操作系统',
    `login_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `logout_time`  DATETIME     NULL     DEFAULT NULL COMMENT '登出时间',
    `online_time`  INT          NULL     DEFAULT 0 COMMENT '在线时长（秒）',
    `token_id`     VARCHAR(64)  NULL     DEFAULT NULL COMMENT 'Token标识，用于单点登录分析',
    `tenant_id`    BIGINT       NULL     DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_account` (`account`, `login_time`) USING BTREE,
    KEY `idx_user` (`user_id`, `login_time`) USING BTREE,
    KEY `idx_ip` (`ip_address`, `login_time`) USING BTREE,
    KEY `idx_status` (`login_status`, `login_time`) USING BTREE,
    KEY `idx_tenant` (`tenant_id`, `login_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '登录日志表'
  ROW_FORMAT = DYNAMIC;

-- ==================== 敏感操作审计表（高风险操作二次确认） ====================
DROP TABLE IF EXISTS `org_audit_record`;
CREATE TABLE `org_audit_record`
(
    `id`               BIGINT       NOT NULL COMMENT '主键，UUID',
    `audit_type`       TINYINT(1)   NOT NULL COMMENT '审计类型：1权限变更，2数据导出，3批量操作，4敏感数据访问',
    `operation_log_id` BIGINT       NOT NULL COMMENT '关联的操作日志ID',
    `business_type`    VARCHAR(50)  NOT NULL COMMENT '业务类型',
    `business_id`      BIGINT       NOT NULL COMMENT '业务主键',
    `operation_desc`   VARCHAR(200) NOT NULL COMMENT '操作描述',
    `operator_id`      BIGINT       NOT NULL COMMENT '操作人ID',
    `operator_name`    VARCHAR(20)  NOT NULL COMMENT '操作人姓名',
    `audit_status`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '审计状态：0待审计，1已通过，2已驳回，3已撤销',
    `auditor_id`       BIGINT       NULL     DEFAULT NULL COMMENT '审计人ID',
    `auditor_name`     VARCHAR(20)  NULL     DEFAULT NULL COMMENT '审计人姓名',
    `audit_time`       DATETIME     NULL     DEFAULT NULL COMMENT '审计时间',
    `audit_remark`     VARCHAR(500) NULL     DEFAULT NULL COMMENT '审计意见',
    `apply_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `deadline`         DATETIME     NOT NULL COMMENT '审计截止时间',
    `tenant_id`        BIGINT       NULL     DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_log_id` (`operation_log_id`) USING BTREE,
    KEY `idx_operator` (`operator_id`, `audit_status`) USING BTREE,
    KEY `idx_auditor` (`auditor_id`, `audit_status`) USING BTREE,
    KEY `idx_status_time` (`audit_status`, `deadline`) USING BTREE,
    KEY `idx_business` (`business_type`, `business_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '敏感操作审计表（高风险操作需审批）'
  ROW_FORMAT = DYNAMIC;
