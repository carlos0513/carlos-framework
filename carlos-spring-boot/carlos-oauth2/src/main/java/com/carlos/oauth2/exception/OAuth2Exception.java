package com.carlos.oauth2.exception;

import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.StatusCode;

/**
 * OAuth2异常
 *
 * @author yunjin
 * @date 2026-01-25
 */
public class OAuth2Exception extends ComponentException {

    public OAuth2Exception() {
        super();
    }

    public OAuth2Exception(String message) {
        super(message);
    }

    public OAuth2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuth2Exception(Throwable cause) {
        super(cause);
    }

    public OAuth2Exception(StatusCode statusCode) {
        super(statusCode);
    }

    public OAuth2Exception(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public OAuth2Exception(Integer code, String message) {
        super(code, message);
    }
}
