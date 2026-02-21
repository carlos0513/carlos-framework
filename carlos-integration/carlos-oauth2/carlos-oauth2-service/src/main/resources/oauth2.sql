CREATE DATABASE oauth2_db;

SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

CREATE TABLE app_client
(
    id                        BIGINT       NOT NULL COMMENT '主键',
    app_key                   VARCHAR(64)  NOT NULL COMMENT 'appKey',
    app_name                  VARCHAR(64)  NOT NULL COMMENT '应用名称',
    app_logo                  TEXT COMMENT '应用logo',
    app_secret                VARCHAR(100) NOT NULL COMMENT '应用密钥',
    client_secret_expires_at  DATETIME     NOT NULL COMMENT '客户端密钥到期时间',
    client_issued_at          DATETIME     NOT NULL COMMENT '客户端发行时间',
    authentication_methods    VARCHAR(100) NOT NULL COMMENT '认证方式',
    authorization_grant_types VARCHAR(100) NOT NULL COMMENT 'grant_type',
    scopes                    VARCHAR(100)          DEFAULT NULL COMMENT 'scopes',
    redirect_uris             VARCHAR(255)          DEFAULT NULL COMMENT '重定向地址',
    client_settings           MEDIUMTEXT COMMENT 'client设置 key:value;',
    token_settings            MEDIUMTEXT COMMENT 'token设置 key:value;',
    state                     CHAR(5)      NOT NULL DEFAULT '0' COMMENT '客户端状态',
    is_deleted                TINYINT(1)   NOT NULL DEFAULT '0' COMMENT '删除状态',
    create_by                 BIGINT                DEFAULT NULL COMMENT '创建人',
    create_time               DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by                 BIGINT                DEFAULT NULL COMMENT '更新人',
    update_time               DATETIME              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY idx_client_code (app_key) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='应用客户端信息';


-- api管理相关表
DROP TABLE IF EXISTS open_api_category;
CREATE TABLE open_api_category
(
    id            BIGINT(20)  NOT NULL COMMENT '主键ID',
    parent_id     BIGINT      NOT NULL COMMENT '父级id',
    category_code VARCHAR(32) NOT NULL COMMENT '目录编码',
    category_name VARCHAR(64) NOT NULL COMMENT '目录名称',
    is_enabled    TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用',
    is_deleted    TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by     CHAR(32)             DEFAULT NULL COMMENT '创建者编号',
    create_time   DATETIME             DEFAULT NULL COMMENT '创建时间',
    update_by     CHAR(32)             DEFAULT NULL COMMENT '更新者编号',
    update_time   DATETIME             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_parent_id (parent_id) USING BTREE,
    UNIQUE INDEX idx_category_code (parent_id, category_code) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '授权目录'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS resource_open_api;
CREATE TABLE resource_open_api
(
    id              BIGINT       NOT NULL COMMENT '主键ID',
    category_id     BIGINT       NOT NULL COMMENT '目录id',
    api_name        VARCHAR(64)  NOT NULL COMMENT '接口名称',
    api_path        VARCHAR(255) NOT NULL COMMENT '接口路径(如/user/info)',
    method          VARCHAR(8)   NOT NULL COMMENT 'HTTP方法GET,POST,PUT,DELETE',
    permission_key  VARCHAR(64)  NOT NULL COMMENT '权限标识(如user:read)',
    rate_limit      INT                   DEFAULT 100 COMMENT '每分钟调用上限',
    status          TINYINT(2)            DEFAULT 1 COMMENT '状态(0停用 1启用)',
    require_sign    TINYINT(1)            DEFAULT 1 COMMENT '是否需要签名验证(0否 1是)',
    description     TEXT COMMENT '接口功能描述',
    version         VARCHAR(16)           DEFAULT 'v1' COMMENT '接口版本',
    request_sample  MEDIUMINT COMMENT '请求示例（JSON结构）',
    response_sample MEDIUMINT COMMENT '响应示例（JSON结构）',
    is_enabled      TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '是否启用',
    is_deleted      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by       CHAR(32)              DEFAULT NULL COMMENT '创建者编号',
    create_time     DATETIME              DEFAULT NULL COMMENT '创建时间',
    update_by       CHAR(32)              DEFAULT NULL COMMENT '更新者编号',
    update_time     DATETIME              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_category_id (category_id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '开放接口信息'
  ROW_FORMAT = DYNAMIC;

-- 应用接口授权管理
DROP TABLE IF EXISTS resource_scope_category;
CREATE TABLE resource_scope_category
(
    id            BIGINT(20)  NOT NULL COMMENT '主键ID',
    category_code VARCHAR(32) NOT NULL COMMENT '分类编码（如ORG_USER）',
    category_name VARCHAR(64) NOT NULL COMMENT '分类名称（如组织用户）',
    is_enabled    TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用',
    is_deleted    TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by     CHAR(32)             DEFAULT NULL COMMENT '创建者编号',
    create_time   DATETIME             DEFAULT NULL COMMENT '创建时间',
    update_by     CHAR(32)             DEFAULT NULL COMMENT '更新者编号',
    update_time   DATETIME             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '授权目录'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS resource_scope_info;
CREATE TABLE resource_scope_info
(
    id            BIGINT(20)   NOT NULL COMMENT '主键ID',
    category_id   BIGINT       NOT NULL COMMENT '目录id',
    scope_code    VARCHAR(64)  NOT NULL COMMENT '权限标识符(如user:read)',
    scope_name    VARCHAR(128) NOT NULL COMMENT '权限名称(如用户信息读取)',
    description   TEXT COMMENT '权限详细描述',
    parent_scope  VARCHAR(64)           DEFAULT NULL COMMENT '父级Scope(支持权限继承)',
    is_sensitive  TINYINT               DEFAULT 0 COMMENT '是否敏感权限(0否 1是)',
    require_audit TINYINT               DEFAULT 1 COMMENT '是否需要人工审核(0否 1是)',
    status        TINYINT               DEFAULT 1 COMMENT '状态(0停用 1启用)',
    is_deleted    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_by     CHAR(32)              DEFAULT NULL COMMENT '创建者编号',
    create_time   DATETIME              DEFAULT NULL COMMENT '创建时间',
    update_by     CHAR(32)              DEFAULT NULL COMMENT '更新者编号',
    update_time   DATETIME              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_category_id (category_id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '权限范围'
  ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS resource_scope_api;
CREATE TABLE resource_scope_api
(
    id          BIGINT     NOT NULL COMMENT '主键',
    scope_id    BIGINT     NOT NULL COMMENT 'scopeid',
    api_id      BIGINT     NOT NULL COMMENT '接口id',
    create_by   BIGINT(20) NULL DEFAULT NULL COMMENT '创建人',
    create_time DATETIME   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_scope_id (scope_id) USING BTREE,
    INDEX idx_api_id (api_id) USING BTREE,
    UNIQUE INDEX idx_unique (scope_id, api_id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '授权范围关联api'
  ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS app_client_scope;
CREATE TABLE app_client_scope
(
    id          BIGINT     NOT NULL COMMENT '主键',
    app_id      BIGINT     NOT NULL COMMENT '应用id',
    scope_id    BIGINT     NOT NULL COMMENT '范围id',
    status      TINYINT(2)      DEFAULT 1 COMMENT '状态(开通 审批中)',
    create_by   BIGINT(20) NULL DEFAULT NULL COMMENT '创建人',
    create_time DATETIME   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_scope_id (scope_id) USING BTREE,
    INDEX idx_client_id (app_id) USING BTREE,
    UNIQUE INDEX idx_unique (scope_id, app_id) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '应用关联授权'
  ROW_FORMAT = DYNAMIC;

