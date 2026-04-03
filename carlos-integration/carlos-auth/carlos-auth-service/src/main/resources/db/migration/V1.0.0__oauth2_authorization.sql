-- OAuth2 JDBC 存储表结构
-- 用于 JdbcOAuth2AuthorizationService

CREATE TABLE IF NOT EXISTS oauth2_authorization (
    id VARCHAR(100) NOT NULL PRIMARY KEY COMMENT '授权ID',
    registered_client_id VARCHAR(100) NOT NULL COMMENT '注册客户端ID',
    principal_name VARCHAR(200) NOT NULL COMMENT '主体名称（用户名）',
    authorization_grant_type VARCHAR(100) NOT NULL COMMENT '授权类型',
    authorized_scopes VARCHAR(1000) COMMENT '授权范围（JSON数组）',
    attributes VARCHAR(4000) COMMENT '属性（JSON对象）',
    state VARCHAR(500) COMMENT '状态',
    
    -- 授权码
    authorization_code_value VARCHAR(4000) COMMENT '授权码值',
    authorization_code_issued_at TIMESTAMP COMMENT '授权码签发时间',
    authorization_code_expires_at TIMESTAMP COMMENT '授权码过期时间',
    authorization_code_metadata VARCHAR(2000) COMMENT '授权码元数据（JSON）',
    
    -- 访问令牌
    access_token_value VARCHAR(4000) COMMENT '访问令牌值',
    access_token_issued_at TIMESTAMP COMMENT '访问令牌签发时间',
    access_token_expires_at TIMESTAMP COMMENT '访问令牌过期时间',
    access_token_metadata VARCHAR(2000) COMMENT '访问令牌元数据（JSON）',
    access_token_type VARCHAR(100) COMMENT '访问令牌类型',
    access_token_scopes VARCHAR(1000) COMMENT '访问令牌范围（JSON数组）',
    
    -- 刷新令牌
    refresh_token_value VARCHAR(4000) COMMENT '刷新令牌值',
    refresh_token_issued_at TIMESTAMP COMMENT '刷新令牌签发时间',
    refresh_token_expires_at TIMESTAMP COMMENT '刷新令牌过期时间',
    refresh_token_metadata VARCHAR(2000) COMMENT '刷新令牌元数据（JSON）',
    
    -- OIDC ID 令牌
    oidc_id_token_value VARCHAR(4000) COMMENT 'OIDC ID令牌值',
    oidc_id_token_issued_at TIMESTAMP COMMENT 'OIDC ID令牌签发时间',
    oidc_id_token_expires_at TIMESTAMP COMMENT 'OIDC ID令牌过期时间',
    oidc_id_token_metadata VARCHAR(2000) COMMENT 'OIDC ID令牌元数据（JSON）',
    oidc_id_token_claims VARCHAR(2000) COMMENT 'OIDC ID令牌声明（JSON）',
    
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_registered_client_id (registered_client_id),
    INDEX idx_principal_name (principal_name),
    INDEX idx_state (state),
    INDEX idx_authorization_code_value (authorization_code_value(255)),
    INDEX idx_access_token_value (access_token_value(255)),
    INDEX idx_refresh_token_value (refresh_token_value(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2授权信息表';
