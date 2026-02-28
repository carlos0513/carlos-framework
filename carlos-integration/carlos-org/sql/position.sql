-- ==================== 1. 岗位类别表（职系） ====================
DROP TABLE IF EXISTS `org_position_category`;
CREATE TABLE `org_position_category`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_code` VARCHAR(50)  NOT NULL COMMENT '类别编码，如：M、T、P、S',
    `category_name` VARCHAR(50)  NOT NULL COMMENT '类别名称，如：管理系、技术系、专业系、销售系',
    `category_type` TINYINT(2)   NOT NULL DEFAULT 1 COMMENT '类别类型：1管理通道，2专业通道，3双通道',
    `description`   VARCHAR(200) NULL     DEFAULT NULL COMMENT '类别描述',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `state`         TINYINT(2)   NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `version`       INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常，1删除',
    `tenant_id`     BIGINT       NULL     DEFAULT NULL COMMENT '租户ID',
    `create_by`     BIGINT       NULL     DEFAULT NULL COMMENT '创建者',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     BIGINT       NULL     DEFAULT NULL COMMENT '修改者',
    `update_time`   DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_category_code` (`tenant_id`, `category_code`) USING BTREE,
    KEY `idx_type_state` (`category_type`, `state`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '岗位类别表（职系）'
  ROW_FORMAT = DYNAMIC;

-- ==================== 2. 职级表 ====================
DROP TABLE IF EXISTS `org_position_level`;
CREATE TABLE `org_position_level`
(
    `id`           BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_id`  BIGINT         NOT NULL COMMENT '所属职系ID',
    `level_code`   VARCHAR(20)    NOT NULL COMMENT '职级编码，如：T1、T2-1、M3',
    `level_name`   VARCHAR(50)    NOT NULL COMMENT '职级名称，如：初级工程师、高级工程师、部门经理',
    `level_seq`    INT            NOT NULL COMMENT '职级序列号，用于排序和比较，如：1、2、3',
    `level_group`  VARCHAR(20)    NULL     DEFAULT NULL COMMENT '职级分组：初级、中级、高级、专家、资深专家',
    `min_salary`   DECIMAL(12, 2) NULL     DEFAULT NULL COMMENT '薪资范围下限',
    `max_salary`   DECIMAL(12, 2) NULL     DEFAULT NULL COMMENT '薪资范围上限',
    `stock_level`  TINYINT(2)     NULL     DEFAULT 0 COMMENT '股权激励等级：0无，1部分，2全额',
    `description`  VARCHAR(200)   NULL     DEFAULT NULL COMMENT '职级描述',
    `requirements` TEXT           NULL     DEFAULT NULL COMMENT '晋升要求（JSON格式）',
    `state`        TINYINT(2)     NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `version`      INT UNSIGNED   NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted`   TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常，1删除',
    `tenant_id`    BIGINT         NULL     DEFAULT NULL COMMENT '租户ID',
    `create_by`    BIGINT         NULL     DEFAULT NULL COMMENT '创建者',
    `create_time`  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`    BIGINT         NULL     DEFAULT NULL COMMENT '修改者',
    `update_time`  DATETIME       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_level_code` (`tenant_id`, `level_code`) USING BTREE,
    UNIQUE KEY `uk_category_seq` (`category_id`, `level_seq`) USING BTREE,
    KEY `idx_group` (`level_group`, `state`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '职级表'
  ROW_FORMAT = DYNAMIC;

-- ==================== 3. 岗位表（核心） ====================
DROP TABLE IF EXISTS `org_position`;
CREATE TABLE `org_position`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `position_code`    VARCHAR(50)  NOT NULL COMMENT '岗位编码，租户内唯一，如：JAVA_DEV_001',
    `position_name`    VARCHAR(50)  NOT NULL COMMENT '岗位名称，如：Java开发工程师',
    `position_short`   VARCHAR(20)  NULL     DEFAULT NULL COMMENT '岗位简称，如：Java开发',
    `category_id`      BIGINT       NOT NULL COMMENT '所属职系ID',
    `default_level_id` BIGINT       NULL     DEFAULT NULL COMMENT '默认职级ID（新人入职默认职级）',
    `level_range`      VARCHAR(100) NULL     DEFAULT NULL COMMENT '职级范围，如：T1-T5，支持的范围',
    `position_type`    TINYINT(2)   NOT NULL DEFAULT 1 COMMENT '岗位类型：1标准岗（有编制），2虚拟岗（项目制），3兼职岗',
    `position_nature`  TINYINT(2)   NOT NULL DEFAULT 1 COMMENT '岗位性质：1全职，2兼职，3实习，4外包',
    `department_id`    BIGINT       NULL     DEFAULT NULL COMMENT '所属部门ID（标准岗必填，虚拟岗可为空）',
    `parent_id`        BIGINT       NULL     DEFAULT 0 COMMENT '上级岗位ID，用于汇报线，0表示无上级',
    `headcount`        INT          NULL     DEFAULT 1 COMMENT '编制人数（标准岗），0表示不限',
    `current_count`    INT          NOT NULL DEFAULT 0 COMMENT '当前在岗人数（冗余，实时计算）',
    `vacancy_count`    INT          NOT NULL DEFAULT 0 COMMENT '空缺人数（冗余，实时计算）',
    `duty`             TEXT         NULL     DEFAULT NULL COMMENT '岗位职责（富文本）',
    `requirement`      TEXT         NULL     DEFAULT NULL COMMENT '任职要求（富文本）',
    `qualification`    TEXT         NULL     DEFAULT NULL COMMENT '任职资格（JSON格式，支持扩展）',
    `kpi_indicator`    TEXT         NULL     DEFAULT NULL COMMENT '绩效考核指标（JSON格式）',
    `cost_center`      VARCHAR(50)  NULL     DEFAULT NULL COMMENT '成本中心编码，对接财务系统',
    `location`         VARCHAR(100) NULL     DEFAULT NULL COMMENT '工作地点，如：北京、上海、远程',
    `salary_grade`     VARCHAR(20)  NULL     DEFAULT NULL COMMENT '薪等，如：P5、P6',
    `state`            TINYINT(2)   NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用，2冻结（暂停招聘）',
    `version`          INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted`       TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常，1删除',
    `tenant_id`        BIGINT       NULL     DEFAULT NULL COMMENT '租户ID',
    `create_by`        BIGINT       NULL     DEFAULT NULL COMMENT '创建者',
    `create_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        BIGINT       NULL     DEFAULT NULL COMMENT '修改者',
    `update_time`      DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_position_code` (`tenant_id`, `position_code`) USING BTREE,
    KEY `idx_category` (`category_id`, `state`) USING BTREE,
    KEY `idx_department` (`department_id`, `position_type`, `state`) USING BTREE,
    KEY `idx_parent` (`parent_id`) USING BTREE,
    KEY `idx_level` (`default_level_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '岗位表'
  ROW_FORMAT = DYNAMIC;

-- ==================== 4. 岗位-角色关联表（岗位默认权限） ====================
DROP TABLE IF EXISTS `org_position_role`;
CREATE TABLE `org_position_role`
(
    `id`          BIGINT     NOT NULL AUTO_INCREMENT COMMENT '主键',
    `position_id` BIGINT     NOT NULL COMMENT '岗位ID',
    `role_id`     BIGINT     NOT NULL COMMENT '角色ID',
    `is_default`  TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否默认角色：1是（入职自动获得），0否（可选）',
    `state`       TINYINT(2) NOT NULL DEFAULT 1 COMMENT '状态：0停用，1启用',
    `create_by`   BIGINT     NULL     DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT     NULL     DEFAULT NULL COMMENT '修改者',
    `update_time` DATETIME   NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `tenant_id`   BIGINT     NULL     DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_position_role` (`position_id`, `role_id`) USING BTREE,
    KEY `idx_position_default` (`position_id`, `is_default`, `state`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '岗位角色关联表（岗位默认权限配置）'
  ROW_FORMAT = DYNAMIC;

-- ==================== 5. 用户-岗位-职级关联表（核心关联） ====================
DROP TABLE IF EXISTS `org_user_position`;
CREATE TABLE `org_user_position`
(
    `id`                 BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`            BIGINT        NOT NULL COMMENT '用户ID',
    `position_id`        BIGINT        NOT NULL COMMENT '岗位ID',
    `level_id`           BIGINT        NOT NULL COMMENT '职级ID（具体的职级）',
    `department_id`      BIGINT        NOT NULL COMMENT '任职部门ID（岗位可能跨部门，此处记录实际任职部门）',
    `is_main`            TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '是否主岗位：1是，0否（兼职/兼岗）',
    `position_status`    TINYINT(2)    NOT NULL DEFAULT 1 COMMENT '任职状态：1在职，2试用期，3待入职，4待离职，5已卸任',
    `appoint_type`       TINYINT(2)    NOT NULL DEFAULT 1 COMMENT '任职方式：1任命，2竞聘，3委派，4借调',
    `appoint_date`       DATE          NOT NULL COMMENT '任职日期',
    `probation_end`      DATE          NULL     DEFAULT NULL COMMENT '试用期结束日期',
    `dimission_date`     DATE          NULL     DEFAULT NULL COMMENT '卸任日期',
    `cost_ratio`         DECIMAL(3, 2) NULL     DEFAULT 1.00 COMMENT '成本分摊比例（兼职岗使用，如0.5表示50%）',
    `report_to`          BIGINT        NULL     DEFAULT NULL COMMENT '汇报对象（直接上级）user_id',
    `dotted_report`      BIGINT        NULL     DEFAULT NULL COMMENT '虚线汇报对象（矩阵管理）user_id',
    `work_location`      VARCHAR(100)  NULL     DEFAULT NULL COMMENT '实际工作地点（可覆盖岗位默认地点）',
    `contract_type`      TINYINT(2)    NULL     DEFAULT 1 COMMENT '合同类型：1劳动合同，2劳务合同，3实习协议，4外包合同',
    `performance_rating` VARCHAR(10)   NULL     DEFAULT NULL COMMENT '最近绩效等级：A/B/C/D',
    `salary_level`       VARCHAR(20)   NULL     DEFAULT NULL COMMENT '实际薪级（可能高于岗位默认）',
    `stock_grant`        TINYINT(1)    NULL     DEFAULT 0 COMMENT '是否有股权激励：0无，1有',
    `version`            INT UNSIGNED  NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted`         TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常，1删除',
    `tenant_id`          BIGINT        NULL     DEFAULT NULL COMMENT '租户ID',
    `create_by`          BIGINT        NULL     DEFAULT NULL COMMENT '创建者',
    `create_time`        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          BIGINT        NULL     DEFAULT NULL COMMENT '修改者',
    `update_time`        DATETIME      NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_position_dept` (`user_id`, `position_id`, `department_id`) USING BTREE,
    KEY `idx_user_main` (`user_id`, `is_main`, `position_status`) USING BTREE,
    KEY `idx_position` (`position_id`, `position_status`) USING BTREE,
    KEY `idx_department` (`department_id`, `position_status`) USING BTREE,
    KEY `idx_report` (`report_to`) USING BTREE,
    KEY `idx_level` (`level_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户岗位职级关联表（核心任职信息）'
  ROW_FORMAT = DYNAMIC;

-- ==================== 6. 岗位变更历史表 ====================
DROP TABLE IF EXISTS `org_position_history`;
CREATE TABLE `org_position_history`
(
    `id`              BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`         BIGINT         NOT NULL COMMENT '用户ID',
    `change_type`     TINYINT(2)     NOT NULL COMMENT '变更类型：1入职，2晋升，3降职，4转岗，5兼职，6卸任，7离职',
    `old_position_id` BIGINT         NULL     DEFAULT NULL COMMENT '原岗位ID',
    `new_position_id` BIGINT         NULL     DEFAULT NULL COMMENT '新岗位ID',
    `old_level_id`    BIGINT         NULL     DEFAULT NULL COMMENT '原职级ID',
    `new_level_id`    BIGINT         NULL     DEFAULT NULL COMMENT '新职级ID',
    `old_dept_id`     BIGINT         NULL     DEFAULT NULL COMMENT '原部门ID',
    `new_dept_id`     BIGINT         NULL     DEFAULT NULL COMMENT '新部门ID',
    `old_salary`      DECIMAL(12, 2) NULL     DEFAULT NULL COMMENT '原薪资',
    `new_salary`      DECIMAL(12, 2) NULL     DEFAULT NULL COMMENT '新薪资',
    `change_reason`   VARCHAR(200)   NOT NULL COMMENT '变更原因',
    `change_date`     DATE           NOT NULL COMMENT '变更生效日期',
    `approval_no`     VARCHAR(50)    NULL     DEFAULT NULL COMMENT '审批单号',
    `attachments`     TEXT           NULL     DEFAULT NULL COMMENT '附件（JSON数组，存储文件URL）',
    `remark`          VARCHAR(500)   NULL     DEFAULT NULL COMMENT '备注',
    `operator_id`     BIGINT         NOT NULL COMMENT '操作人ID',
    `operator_name`   VARCHAR(20)    NOT NULL COMMENT '操作人姓名（冗余）',
    `tenant_id`       BIGINT         NULL     DEFAULT NULL COMMENT '租户ID',
    `create_time`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_user` (`user_id`, `change_date`) USING BTREE,
    KEY `idx_type` (`change_type`, `create_time`) USING BTREE,
    KEY `idx_approval` (`approval_no`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '岗位变更历史表'
  ROW_FORMAT = DYNAMIC;
