package com.carlos.gateway.auth;

import com.carlos.core.auth.AuthConstant;
import com.carlos.core.exception.ComponentException;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Set;

/**
 * <p>
 * 网关认证相关属性配置
 * </p>
 *
 * @author yunjin
 * @date 2021/12/22 10:13
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "yunjin.gateway.auth")
public class GatewayAuthProperties implements InitializingBean {

    /**
     * 默认认证白名单
     */
    private final static Set<String> DEFAULT_WHITE = Sets
            .newHashSet(
                    "/csrf",
                    "/auth/oauth/token",
                    "/auth/rsa/public"
            );

    /**
     * 是否开启认证   authenticate
     */
    boolean authenticate = true;
    /**
     * 是否开启授权   authorize
     */
    boolean authorize = false;

    /**
     * 网关白名单列表  不需要验证token
     */
    private Set<String> whitelist;

    /**
     * token过期时间
     */
    private Duration tokenExpire = Duration.ofHours(6);

    /**
     * token名称
     */
    private String tokenName = AuthConstant.TOKEN_HEADER;


    @Override
    public void afterPropertiesSet() {
        if (whitelist == null) {
            whitelist = DEFAULT_WHITE;
        } else {
            whitelist.addAll(DEFAULT_WHITE);
        }
        if (!authenticate) {
            whitelist.add("/**");
        }
        // 如果开启授权 则必须开启认证
        if (authorize) {
            if (!authenticate) {
                throw new ComponentException("When authorize is true, authenticate must be true, not false");
            }

        }
        if (log.isDebugEnabled()) {
            log.debug("Gateway white-list:{}", whitelist);
        }
    }


}
