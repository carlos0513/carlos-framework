package com.carlos.gateway.config;

/**
 * <p>
 * 组织架构相关常量
 * </p>
 *
 * @author Carlos
 * @date 2022/11/15 9:05
 */
public interface GatewayConstant {

    /**
     * 参数token
     */
    String LOGIN_USER_CONTEXT_CACHE = "context:%s";

    /**
     * 参数为token
     */
    String LOGIN_USER_TOKEN_CACHE = "token:%s";

    /**
     * 参数token
     */
    String LOGIN_USER_MENU_CACHE = "user:menu:%s";


}
