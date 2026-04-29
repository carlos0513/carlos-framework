package com.carlos.boot;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.carlos.boot.request.RequestInfo;
import com.carlos.boot.request.RequestUtil;
import com.carlos.core.exception.BusinessException;
import com.carlos.core.exception.DaoException;
import com.carlos.core.exception.GlobalException;
import com.carlos.core.exception.RestException;
import com.carlos.core.response.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * <p>
 * 统一处理各类异常，转换为标准 Result 响应格式
 * <p>
 * 错误码规范：5位数字字符串 A-BB-CC
 * <ul>
 *   <li>A - 错误级别：0成功，1客户端错误，2业务错误，3第三方错误，5系统错误</li>
 *   <li>BB - 模块编码：00通用，01用户，02认证等</li>
 *   <li>CC - 具体错误序号</li>
 * </ul>
 *
 * @author carlos
 * @since 1.0.0
 * @see Result
 * @see CommonErrorCode
 */
@Slf4j
public class GlobalExceptionHandler {

    // ==================== 参数校验异常 ====================

    /**
     * 处理 @Valid @RequestBody 校验失败
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception, HttpServletRequest request) {

        List<FieldErrorDetail> details = exception.getBindingResult().getFieldErrors().stream()
            .map(this::convertToDetail)
            .collect(Collectors.toList());

        log.warn("[参数校验失败] {} - {}", request.getRequestURI(), details);
        printRequestDetail();

        Result<Void> response = Result.validationError(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理 @ModelAttribute / @RequestParam 绑定校验失败
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> handleBindException(
        BindException exception, HttpServletRequest request) {

        List<FieldErrorDetail> details = exception.getFieldErrors().stream()
            .map(this::convertToDetail)
            .collect(Collectors.toList());

        String firstError = details.isEmpty() ? "参数绑定失败" : details.getFirst().getMessage();
        log.warn("[参数绑定失败] {} - {}", request.getRequestURI(), firstError);
        printRequestDetail();

        Result<Void> response = Result.validationError(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理 @RequestParam @PathVariable 校验失败（@Validated）
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Void>> handleConstraintViolation(
        ConstraintViolationException exception, HttpServletRequest request) {

        List<FieldErrorDetail> details = exception.getConstraintViolations().stream()
            .map(this::convertToDetail)
            .collect(Collectors.toList());

        String firstError = details.isEmpty() ? "参数约束违反" : details.getFirst().getMessage();
        log.warn("[参数约束违反] {} - {}", request.getRequestURI(), firstError);
        printRequestDetail();

        Result<Void> response = Result.validationError(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理缺少必需参数
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result<Void>> handleMissingServletRequestParameter(
        MissingServletRequestParameterException exception, HttpServletRequest request) {

        String message = "缺少必要参数: %s".formatted(exception.getParameterName());
        log.warn("[缺少参数] {} - {}", request.getRequestURI(), message);
        printRequestDetail();

        Result<Void> response = Result.error(CommonErrorCode.PARAM_MISSING, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理参数类型不匹配
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException exception, HttpServletRequest request) {

        String message = "参数类型错误: %s 期望类型 %s".formatted(
            exception.getName(),
            exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "未知");
        log.warn("[参数类型错误] {} - {}", request.getRequestURI(), message);
        printRequestDetail();

        Result<Void> response = Result.error(CommonErrorCode.PARAM_TYPE_ERROR, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理HTTP消息不可读异常（JSON解析失败等）
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> handleHttpMessageNotReadable(
        HttpMessageNotReadableException exception, HttpServletRequest request) {

        log.warn("[请求体解析失败] {} - {}", request.getRequestURI(), exception.getMessage());
        printRequestDetail();

        Result<Void> response = Result.error(CommonErrorCode.PARAM_FORMAT_ERROR, "请求参数格式错误");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ==================== 业务异常 ====================
    /**
     * 处理旧版全局异常（兼容层）
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Result<Void>> handleGlobalException(
        GlobalException exception, HttpServletRequest request) {

        printRequestDetail();

        ErrorCode errorCode = switch (exception) {
            case BusinessException b -> CommonErrorCode.BUSINESS_ERROR;
            case DaoException d -> CommonErrorCode.DATABASE_ERROR;
            case RestException r -> CommonErrorCode.BAD_REQUEST;
            default -> CommonErrorCode.INTERNAL_ERROR;
        };

        int httpStatus = exception.getHttpStatus();

        log.error("[全局异常] {} - type={}, httpStatus={}, message={}",
            request.getRequestURI(), exception.getClass().getSimpleName(), httpStatus, exception.getMessage());

        Result<Void> response = Result.error(errorCode, exception.getMessage());
        return ResponseEntity.status(httpStatus).body(response);
    }

    // ==================== HTTP 相关异常 ====================

    /**
     * 处理请求方法不支持异常
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result<Void>> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {

        log.warn("[请求方法不支持] {} {}", exception.getMethod(), request.getRequestURI());
        printRequestDetail();

        String message = "不支持的请求方法: %s".formatted(exception.getMethod());
        Result<Void> response = Result.error(CommonErrorCode.METHOD_NOT_ALLOWED, message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * 处理HTTP媒体类型异常
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<Result<Void>> handleHttpMediaType(
        HttpMediaTypeException exception, HttpServletRequest request) {

        log.warn("[媒体类型错误] {} - {}", request.getRequestURI(), exception.getMessage());
        printRequestDetail();

        Result<Void> response = Result.error(CommonErrorCode.BAD_REQUEST, "Content-Type 不支持");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理404找不到处理器
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result<Void>> handleNoHandlerFound(
        NoHandlerFoundException exception, HttpServletRequest request) {

        log.warn("[资源不存在] {} {}", exception.getHttpMethod(), exception.getRequestURL());

        Result<Void> response = Result.error(CommonErrorCode.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ==================== 兜底异常 ====================

    /**
     * 处理未预期的异常
     *
     * @param exception 异常对象
     * @param request   HTTP请求
     * @return Result
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(
        Exception exception, HttpServletRequest request) {

        log.error("[未预期异常] {} - {}", request.getRequestURI(), exception.getMessage(), exception);
        printRequestDetail();

        // 生产环境不暴露详细错误信息
        String message = isProduction() ? "系统繁忙，请稍后重试" : exception.getMessage();
        Result<Void> response = Result.error(CommonErrorCode.INTERNAL_ERROR, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // ==================== 工具方法 ====================

    /**
     * 转换 FieldError 为 FieldErrorDetail
     */
    private FieldErrorDetail convertToDetail(FieldError fieldError) {
        return FieldErrorDetail.builder()
            .field(fieldError.getField())
            .message(fieldError.getDefaultMessage())
            .rejectedValue(fieldError.getRejectedValue())
            .build();
    }

    /**
     * 转换 ConstraintViolation 为 FieldErrorDetail
     */
    private FieldErrorDetail convertToDetail(ConstraintViolation<?> violation) {
        String field = violation.getPropertyPath().toString();
        // 简化字段路径
        if (field.contains(".")) {
            field = field.substring(field.lastIndexOf('.') + 1);
        }
        return FieldErrorDetail.builder()
            .field(field)
            .message(violation.getMessage())
            .rejectedValue(violation.getInvalidValue())
            .build();
    }

    /**
     * 打印请求详情
     */
    private void printRequestDetail() {
        try {
            RequestInfo requestInfo = RequestUtil.getRequestInfo();
            log.error("异常来源：{}",
                JSONUtil.toJsonStr(requestInfo, JSONConfig.create().setIgnoreNullValue(true).setIgnoreError(true)));
        } catch (Exception e) {
            log.error("打印请求详情失败", e);
        }
    }

    /**
     * 判断是否为生产环境
     */
    private boolean isProduction() {
        String profile = System.getProperty("spring.profiles.active", "");
        return "prod".equals(profile) || "production".equals(profile);
    }

    /**
     * 根据错误级别记录不同级别的日志
     *
     * @param level   错误级别
     * @param message 日志消息
     */
    private void logByLevel(ErrorLevel level, String message) {
        switch (level) {
            case CLIENT_ERROR, BUSINESS_ERROR -> log.warn(message);
            case THIRD_PARTY_ERROR, SYSTEM_ERROR -> log.error(message);
            default -> log.info(message);
        }
    }

    /**
     * 根据错误级别判断是否打印堆栈
     *
     * @param level 错误级别
     * @return true 表示需要打印堆栈
     */
    private boolean shouldPrintStackTrace(ErrorLevel level) {
        // 系统错误和第三方错误打印堆栈
        return level == ErrorLevel.SYSTEM_ERROR || level == ErrorLevel.THIRD_PARTY_ERROR;
    }

}
