package com.carlos.gateway.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * <p>
 * 网关错误响应对象
 * 统一封装网关层面的错误响应信息
 * </p>
 *
 * @author carlos
 * @date 2026/3/24
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * 是否成功（始终为 false）
     */
    private boolean success;

    /**
     * HTTP 状态码
     */
    private int status;

    /**
     * 业务错误码
     */
    private String code;

    /**
     * 错误消息
     */
    private String msg;

    /**
     * 错误详情（开发环境可展示）
     */
    private String detail;

    /**
     * 异常堆栈（仅开发环境展示）
     */
    private String stack;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求 ID（链路追踪）
     */
    private String traceId;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 扩展字段（特定异常类型的额外信息）
     */
    private Map<String, Object> extra;

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder()
            .success(false)
            .timestamp(System.currentTimeMillis());
    }
}
