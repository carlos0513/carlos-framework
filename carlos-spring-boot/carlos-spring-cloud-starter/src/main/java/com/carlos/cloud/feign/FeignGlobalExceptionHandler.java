package com.carlos.cloud.feign;

import com.carlos.core.response.Result;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * <p>
 * feign全局异常处理
 * </p>
 *
 * @author carlos
 * @date 2022/4/11 11:35
 */
@Slf4j
@RestControllerAdvice
public class FeignGlobalExceptionHandler {


    @ExceptionHandler(FeignException.class)
    public Result<String> feignExceptionHandler(FeignException exception) {

        log.error(exception.getMessage(), exception);
        return Result.fail(exception.getMessage());

    }
}
