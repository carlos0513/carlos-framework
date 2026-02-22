DROP TABLE IF EXISTS org_docking_mapping;
CREATE TABLE org_docking_mapping
(
    id           varchar(32) NOT NULL COMMENT '主键ID',
    system_id    varchar(32) NOT NULL COMMENT '系统数据id',
    target_id    varchar(64) NOT NULL COMMENT '目标系统id',
    target_code  varchar(32) NOT NULL COMMENT '目标系统标识',
    docking_type varchar(16) NOT NULL COMMENT '对接类型',
    is_deleted   tinyint(1)  NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除，1：已删除',
    create_by    char(32)             DEFAULT NULL COMMENT '创建者编号',
    create_time  datetime             DEFAULT NULL COMMENT '创建时间',
    update_by    char(32)             DEFAULT NULL COMMENT '更新者编号',
    update_time  datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY uk_target (target_id, target_code, docking_type) USING BTREE COMMENT '联合唯一索引',
    KEY idx_system_id (system_id, docking_type) USING BTREE COMMENT '系统id'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户信息对接关联表';





