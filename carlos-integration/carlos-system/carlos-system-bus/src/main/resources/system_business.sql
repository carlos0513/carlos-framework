-- ----------------------------
-- Table structure for sys_service_register
-- ----------------------------
DROP TABLE IF EXISTS sys_service_register;
CREATE TABLE sys_service_register
(
    id             VARCHAR(32) NOT NULL COMMENT '主键ID',
    service_code   VARCHAR(32) NOT NULL COMMENT '服务编码',
    service_name   VARCHAR(32) NOT NULL COMMENT '服务名称',
    service_key    VARCHAR(32) NOT NULL COMMENT '服务秘钥',
    service_remark VARCHAR(255)         DEFAULT NULL COMMENT '服务描述',
    state          VARCHAR(12) NOT NULL COMMENT '服务状态',
    online_num     INT(11)              DEFAULT 0 COMMENT '在线服务数',
    is_deleted     TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除，0：未删除，1：已删除',
    create_by      CHAR(32)             DEFAULT NULL COMMENT '创建者编号',
    create_time    DATETIME             DEFAULT NULL COMMENT '创建时间',
    update_by      CHAR(32)             DEFAULT NULL COMMENT '更新者编号',
    update_time    DATETIME             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '服务注册信息'
  ROW_FORMAT = DYNAMIC;


ALTER TABLE `carlos_bbt`.`sys_region`
    ADD COLUMN `sort` INT NULL DEFAULT 0 COMMENT '排序' AFTER `update_time`


