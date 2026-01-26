package com.yunjin.oauth2.constant;

/**
 * OAuth2常量
 *
 * @author yunjin
 * @date 2026-01-25
 */
public interface OAuth2Constant {

    /**
     * Token类型
     */
    String TOKEN_TYPE_BEARER = "Bearer";

    /**
     * 授权类型
     */
    interface GrantType {
        String AUTHORIZATION_CODE = "authorization_code";
        String IMPLICIT = "implicit";
        String PASSWORD = "password";
        String CLIENT_CREDENTIALS = "client_credentials";
        String REFRESH_TOKEN = "refresh_token";
    }

    /**
     * 作用域
     */
    interface Scope {
        String READ = "read";
        String WRITE = "write";
        String ALL = "all";
    }

    /**
     * JWT Claims
     */
    interface Claims {
        String USER_ID = "user_id";
        String USER_NAME = "user_name";
        String TENANT_ID = "tenant_id";
        String DEPT_ID = "dept_id";
        String ROLE_IDS = "role_ids";
        String AUTHORITIES = "authorities";
    }

    /**
     * 缓存Key前缀
     */
    interface CachePrefix {
        String OAUTH2_TOKEN = "oauth2:token:";
        String OAUTH2_REFRESH_TOKEN = "oauth2:refresh_token:";
        String OAUTH2_AUTHORIZATION = "oauth2:authorization:";
        String OAUTH2_CONSENT = "oauth2:consent:";
    }
}
