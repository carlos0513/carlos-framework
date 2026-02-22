package com.carlos.org.config;

import java.time.Duration;

/**
 * <p>
 * 组织架构相关常量
 * </p>
 *
 * @author Carlos
 * @date 2022/11/15 9:05
 */
public interface OrgConstant {


    /**
     * 参数为token
     */
    String LOGIN_USER_TOKEN_CACHE = "user:token:%s";

    /**
     * 参数token
     */
    String LOGIN_USER_CONTEXT_CACHE = "user:context:%s";

    /**
     * 参数token
     */
    String LOGIN_USER_MENU_CACHE = "user:menu:%s";

    /**
     * token过期时间
     */
    Duration EXPIRE = Duration.ofHours(6);


}
