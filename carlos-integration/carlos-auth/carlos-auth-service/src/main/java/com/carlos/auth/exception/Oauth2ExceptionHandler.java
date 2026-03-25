package com.carlos.auth.exception;

import com.carlos.core.response.Result;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 全局处理OAUTH2得异常
 * </p>
 *
 * @author carlos
 * @date 2021/11/4 11:49
 */
@ControllerAdvice
public class Oauth2ExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = OAuth2AuthenticationException.class)
    public Result<String> handleOauth2(OAuth2AuthenticationException e) {
        return Result.error(e.getMessage());
    }
}
