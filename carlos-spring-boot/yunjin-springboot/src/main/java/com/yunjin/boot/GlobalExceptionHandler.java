package com.yunjin.boot;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.yunjin.boot.request.RequestInfo;
import com.yunjin.boot.request.RequestUtil;
import com.yunjin.core.exception.DaoException;
import com.yunjin.core.exception.GlobalException;
import com.yunjin.core.exception.RestException;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.response.Result;
import com.yunjin.core.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 全局异常监听
 * </p>
 *
 * @author yunjin
 * @date 2020/4/10 15:32
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 接口参数校验异常
     *
     * @param exception 异常对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<List<String>> handleMethodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException ", exception);
        printRequestDetail();
        final BindingResult bindingResult = exception.getBindingResult();
        final List<String> list = new ArrayList<>();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (final FieldError fieldError : fieldErrors) {
            list.add(fieldError.getDefaultMessage());
        }
        Collections.sort(list);
        log.error(getApiCodeString(StatusCode.PARAMETER_EXCEPTION) + ":" + list);
        return Result.fail(StatusCode.PARAMETER_EXCEPTION, list.get(0));
    }

    /**
     * 参数校验异常
     *
     * @return com.yunjin.core.response.ResultVO<java.lang.String>
     * @author yunjin
     * @date 2020/10/24 0:16
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<String> bindExceptionHandler(final BindException exception) {
        final List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        final String errorMessage = fieldErrors.get(0).getDefaultMessage();
        log.error(errorMessage, exception);
        return Result.fail(StatusCode.PARAMETER_EXCEPTION, errorMessage);

    }


    /**
     * HTTP解析请求参数异常
     *
     * @param exception 异常对象
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> httpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        printRequestDetail();
        printApiCodeException(StatusCode.PARAMETER_EXCEPTION, exception);
        return Result.fail(StatusCode.PARAMETER_EXCEPTION);
    }

    /**
     * HTTP
     *
     * @param exception 异常对象
     */
    @ExceptionHandler(value = HttpMediaTypeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> httpMediaTypeException(final HttpMediaTypeException exception) {
        printRequestDetail();
        printApiCodeException(StatusCode.HTTP_MEDIA_TYPE_EXCEPTION, exception);
        return Result.fail(StatusCode.HTTP_MEDIA_TYPE_EXCEPTION);
    }

    /**
     * 自定义业务/数据异常处理
     *
     * @param exception 异常对象
     */
    @ExceptionHandler(value = {GlobalException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> globalException(final GlobalException exception) {
        printRequestDetail();
        log.error("请求异常:", exception);
        final int errorCode;
        if (exception instanceof ServiceException) {
            errorCode = StatusCode.BUSINESS_EXCEPTION.getCode();
        } else if (exception instanceof DaoException) {
            errorCode = StatusCode.DAO_EXCEPTION.getCode();
        } else if (exception instanceof RestException) {
            errorCode = StatusCode.FAIL.getCode();
        } else {
            errorCode = StatusCode.FAIL.getCode();
        }
        return Result.fail(errorCode, exception.getMessage(), null);
    }

    /**
     * 请求方式异常
     *
     * @param exception 异常对象
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<String> httpRequestMethodNotSupportedExceptionHandler(final Exception exception) {
        printRequestDetail();
        printApiCodeException(StatusCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION, exception);
        return Result.fail(StatusCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION, exception.getMessage());
    }

    /**
     * 默认的异常处理
     *
     * @param exception 异常对象
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> exceptionHandler(final Exception exception) {
        printRequestDetail();
        printApiCodeException(StatusCode.FAIL, exception);
        return Result.fail(StatusCode.FAIL);
    }


    /**
     * 获取ApiCode格式化字符串
     *
     * @param statusCode 状态码
     */
    private String getApiCodeString(final StatusCode statusCode) {
        if (statusCode != null) {
            return String.format("errorCode: %s, errorMessage: %s", statusCode.getCode(), statusCode.getMessage());
        }
        return null;
    }

    /**
     * 打印错误码及异常
     *
     * @param statusCode 状态码
     * @param exception  异常对象
     */
    private void printApiCodeException(final StatusCode statusCode, final Exception exception) {
        log.error(getApiCodeString(statusCode), exception);
    }

    /**
     * 打印请求详情
     */
    private void printRequestDetail() {
        final RequestInfo requestInfo = RequestUtil.getRequestInfo();
        // 忽略空值打印json
        log.error("异常来源：{}", JSONUtil.toJsonStr(requestInfo, JSONConfig.create().setIgnoreNullValue(true).setIgnoreError(true)));
    }
}
