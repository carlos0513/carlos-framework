package com.carlos.gateway.exception;

import com.carlos.core.exception.GlobalException;

/**
 * 验证码错误异常类
 *
 * @author yunjin
 */
public class CaptchaException extends GlobalException {

    private static final long serialVersionUID = 1L;

    public CaptchaException(String msg) {
        super(msg);
    }
}
