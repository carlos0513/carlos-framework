package com.carlos.core.constant.basic;

/**
 * 安全相关通用常量
 *
 * @author yunjin
 */
public class SecurityConstants {

    /**
     * 空用户Id
     */
    public static final Long EMPTY_USER_ID = BaseConstants.NONE_ID;

    /**
     * 空租户Id
     */
    public static final Long EMPTY_TENANT_ID = BaseConstants.NONE_ID;

    /**
     * 公共数据租户Id
     */
    public static final Long COMMON_TENANT_ID = BaseConstants.COMMON_ID;

    /**
     * 请求来源
     */
    public static final String FROM_SOURCE = "from-source";


    /**
     * 授权信息
     */
    public static final String AUTHORIZATION_HEADER = "authorization";

    /**
     * 企业Id
     */
    public static final String ENTERPRISE_ID = "enterprise_id";

    /**
     * 企业账号
     */
    public static final String ENTERPRISE_NAME = "enterprise_name";

    /**
     * 用户Id
     */
    public static final String USER_ID = "user_id";

    /**
     * 用户账号
     */
    public static final String USER_NAME = "user_name";

    /**
     * 企业类型
     */
    public static final String IS_LESSOR = "is_lessor";

    /**
     * 用户类型
     */
    public static final String USER_TYPE = "user_type";

    /**
     * 用户标识
     */
    public static final String USER_KEY = "user_key";

    /**
     * 租户策略源
     */
    public static final String SOURCE_NAME = "source_name";

    /**
     * 登录用户
     */
    public static final String LOGIN_USER = "login_user";

    /**
     * 内部请求
     */
    public static final String INNER = "inner";

    /**
     * 数据权限 - 创建者
     */
    public static final String CREATE_BY = "create_by";

    /**
     * 数据权限 - 更新者
     */
    public static final String UPDATE_BY = "update_by";

    public static final String DEPT_SCOPE = "dept_scope";

    public static final String USER_SCOPE = "user_scope";

    public static final String POST_SCOPE = "post_scope";

    public static final String DATA_SCOPE = "data_scope";

    /**
     * 令牌类型
     */
    public static final String BEARER_TOKEN_TYPE = "Bearer";

    /**
     * 授权token url
     */
    public static final String AUTH_TOKEN = "/oauth/token";

    /**
     * 注销token url
     */
    public static final String TOKEN_LOGOUT = "/token/logout";

    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";

    /**
     * sys_oauth_client_details 表的字段，不包括client_id、client_secret
     */
    public static final String CLIENT_FIELDS = "client_id, client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    /**
     * JdbcClientDetailsService 查询语句
     */
    public static final String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS + " from sys_oauth_client_details";

    /**
     * 按条件client_id 查询
     */
    public static final String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

    /**
     * 默认的查询语句
     */
    public static final String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "0";

    /**
     * 退出成功
     */
    public static final String LOGOUT_SUCCESS = "2";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "1";

    /**
     * {bcrypt} 加密的特征码
     */
    public static final String BCRYPT = "{bcrypt}";

    /**
     * {noop} 加密的特征码
     */
    public static final String NOOP = "{noop}";

    /**
     * 默认登录URL
     */
    public static final String OAUTH_TOKEN_URL = "/oauth2/token";

    /**
     * grant_type
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * 授权码模式confirm
     */
    public static final String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

    /**
     * 客户端模式
     */
    public static final String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 项目的license
     */
    public static final String PROJECT_LICENSE = "https://www.twelvet.cn/docs/";

    /**
     * 内部请求标志
     */
    public static final String REQUEST_SOURCE = "request-source";

    /**
     * 协议字段
     */
    public static final String DETAILS_LICENSE = "license";

    /**
     * 客户端ID
     */
    public static final String CLIENT_ID = "clientId";

    /**
     * 用户信息
     */
    public static final String DETAILS_USER = "user_info";

    /**
     * 手机号登录
     */
    public static final String APP = "app";

    /**
     * 短信登录 参数名称
     */
    public static final String SMS_PARAMETER_NAME = "mobile";

}
