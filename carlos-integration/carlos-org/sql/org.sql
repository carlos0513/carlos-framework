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
    `nickname`   VARCHAR(20)  NOT NULL COMMENT '昵称',
    `pwd`             VARCHAR(64)  NOT NULL COMMENT '密码',
    `identify`        VARCHAR(18)  NULL     DEFAULT NULL COMMENT '证件号码',
    `phone`           VARCHAR(11)  NULL     DEFAULT NULL COMMENT '手机号码',
    `address`         VARCHAR(100) NULL     DEFAULT NULL COMMENT '详细地址',
    `gender`          TINYINT(2)   NULL     DEFAULT NULL COMMENT '性别，0：保密, 1：男，2：女，默认0',
    `email`           VARCHAR(30)  NULL     DEFAULT NULL COMMENT '邮箱',
    `avatar`     VARCHAR(200) NULL     DEFAULT NULL COMMENT '头像',
    `description`     VARCHAR(200) NULL     DEFAULT NULL COMMENT '备注',
    `state`           TINYINT(2)   NULL     DEFAULT NULL COMMENT '状态，0：禁用，1：启用，2：锁定',
    `main_dept_id`    BIGINT       NULL     DEFAULT NULL COMMENT '主部门id',
    `login_time` DATETIME     NULL     DEFAULT NULL COMMENT '最后登录时间',
    `login_ip`   VARCHAR(50)  NOT NULL DEFAULT 0 COMMENT '最后登录ip',
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
    `id`           BIGINT     NOT NULL COMMENT '主键',
    `user_id`      BIGINT     NOT NULL COMMENT '用户id',
    `dept_id`      BIGINT     NOT NULL COMMENT '部门id',
    `is_main_dept` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为主部门',
    `create_by`    BIGINT     NULL     DEFAULT NULL COMMENT '创建者',
    `create_time`  DATETIME   NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE `uk_user_id_dept_id` (`user_id` ASC, `dept_id` ASC) USING BTREE,
    INDEX `idx_user_id` (`user_id` ASC) USING BTREE,
    INDEX `idx_dept_id` (`dept_id` ASC) USING BTREE
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
    `id`              BIGINT     NOT NULL COMMENT '主键',
    `dept_id`         BIGINT     NOT NULL COMMENT '部门id',
    `role_id`         BIGINT     NOT NULL COMMENT '角色id',
    `version`         INT        NOT NULL DEFAULT 0 COMMENT '版本',
    `is_default_role` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为默认角色',
    `create_by`       BIGINT     NULL     DEFAULT NULL COMMENT '创建者',
    `create_time`     TIMESTAMP  NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     TIMESTAMP  NULL     DEFAULT NULL COMMENT '修改时间',
    `tenant_id`       BIGINT     NULL     DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_dept_role` (`dept_id`, `role_id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '部门角色'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for org_user_import
-- ----------------------------
DROP TABLE IF EXISTS `org_user_import`;
CREATE TABLE `org_user_import`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `account`     VARCHAR(20)  NOT NULL COMMENT '用户名',
    `realname`    VARCHAR(20)  NOT NULL COMMENT '真实姓名',
    `identify`    VARCHAR(18)  NULL DEFAULT NULL COMMENT '证件号码',
    `phone`       VARCHAR(11)  NOT NULL COMMENT '手机号码',
    `role`        VARCHAR(16)  NULL DEFAULT NULL COMMENT '角色名称',
    `dept`        VARCHAR(200) NULL DEFAULT NULL COMMENT '部门完整信息，以”-“分割部门级别',
    `dept_type`   VARCHAR(255) NULL DEFAULT NULL COMMENT '部门类型',
    `region_code` VARCHAR(50)  NULL DEFAULT NULL COMMENT '行政区域编码',
    `gender`      VARCHAR(10)  NULL DEFAULT NULL COMMENT '性别，0：保密, 1：男，2：女，默认0',
    `email`       VARCHAR(30)  NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户导入'
  ROW_FORMAT = DYNAMIC;

