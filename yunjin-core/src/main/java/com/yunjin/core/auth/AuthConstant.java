package com.yunjin.core.auth;

/**
 * <p>
 * 统一认证相关常量
 * </p>
 *
 * @author yunjin
 * @date 2021/12/8 10:38
 */
public interface AuthConstant {

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String AUTHORITY_CLAIM_NAME = "authorities";

    /**
     * 认证信息Http请求头
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * token前缀 后面有个空格
     */
    String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户id
     */
    String USER_ID = "user_id";
    /**
     * 用户账号
     */
    String USER_ACCOUNT = "user_account";
    /**
     * 用户账号
     */
    String USER_TOKEN = "user_token";
    /**
     * 用户电话
     */
    String USER_PHONE = "user_phone";

    /**
     * 部门id
     */
    String DEPT_ID = "dept_id";

    /**
     * 部门id
     */
    String DEPT_IDS = "dept_ids";

    /**
     * 角色id
     */
    String ROLE_ID = "role_id";

    /**
     * 角色id
     */
    String ROLE_IDS = "role_ids";

    /**
     * tenant_id
     */
    String TENANT_ID = "tenant_id";

    /**
     * 客户端id
     */
    String APP_ID = "X-App-id";
    /**
     * APP-KEY
     */
    String APP_KEY = "X-App-Key";
    /**
     * APP-NAME
     */
    String APP_NAME = "X-App-name";

}
