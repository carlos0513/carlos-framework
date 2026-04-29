package com.carlos.boot.error;

import com.carlos.core.response.CommonErrorCode;
import com.carlos.core.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 无法被@ExceptionHandler捕获的异常可以由ErrorController处理 系统发生异常时，返回错误信息 该视图访问实现 浏览器访问返回页面，其他客户端访问返回json数据
 * 异常处理映射为“/error”。BasicErrorController已经默认实现了“text/html”的处理，如果想返回自定义JSON格式信息， 则实现“ErrorController ”接口，增加一个produces 为“application/json”的方法即可
 * </p>
 *
 * @author carlos
 * @date 2020/4/3 15:30
 */
@Slf4j
@RestController
@Tag(name = "系统错误")
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController implements ErrorController {

    @Operation(summary = "错误信息详情")
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<?> handleError(final HttpServletRequest request, final HttpServletResponse response) {
        log.error("系统异常！");
        final int status = response.getStatus();
        return switch (status) {
            case HttpServletResponse.SC_UNAUTHORIZED -> Result.error(CommonErrorCode.UNAUTHORIZED);
            case HttpServletResponse.SC_FORBIDDEN -> Result.error(CommonErrorCode.UNAUTHORIZED);
            case HttpServletResponse.SC_NOT_FOUND -> Result.error(CommonErrorCode.NOT_FOUND);
            default -> Result.error(CommonErrorCode.INTERNAL_ERROR);
        };
    }

    /**
     * 获取请求的状态码
     *
     * @param request 请求
     * @return org.springframework.http.HttpStatus
     * @author carlos
     * @date 2021/3/3 23:20
     */
    protected HttpStatus getStatus(final HttpServletRequest request) {
        final Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (final Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
