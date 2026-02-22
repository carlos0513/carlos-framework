package com.carlos.oauth.support.thirdparty;

import org.springframework.security.core.AuthenticationException;

/**
 * 第三方登录异常
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
public class ThirdPartyLoginException extends AuthenticationException {

    private final ThirdPartyType type;

    public ThirdPartyLoginException(String msg, ThirdPartyType type) {
        super(msg);
        this.type = type;
    }

    public ThirdPartyLoginException(String msg, Throwable cause, ThirdPartyType type) {
        super(msg, cause);
        this.type = type;
    }

    public ThirdPartyType getType() {
        return type;
    }
}
