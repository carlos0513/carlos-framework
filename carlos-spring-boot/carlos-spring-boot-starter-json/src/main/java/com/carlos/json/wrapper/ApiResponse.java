package com.carlos.json.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 统一的 API 响应包装类
 * </p>
 *
 * @author carlos
 * @date 2025/01/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private String timestamp;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 追踪 ID
     */
    private String traceId;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 创建成功响应
     *
     * @param data 数据
     * @param <T>  泛型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .code(200)
            .message("success")
            .data(data)
            .success(true)
            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .build();
    }

    /**
     * 创建成功响应
     *
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 创建成功响应
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     泛型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
            .code(200)
            .message(message)
            .data(data)
            .success(true)
            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .build();
    }

    /**
     * 创建失败响应
     *
     * @param code    状态码
     * @param message 消息
     * @param <T>     泛型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return ApiResponse.<T>builder()
            .code(code)
            .message(message)
            .success(false)
            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .build();
    }

    /**
     * 创建失败响应
     *
     * @param message 消息
     * @param <T>     泛型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }

    /**
     * 创建参数错误响应
     *
     * @param message 消息
     * @param <T>     泛型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(400, message);
    }

    /**
     * 创建未授权响应
     *
     * @param message 消息
     * @param <T>     泛型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * 创建禁止访问响应
     *
     * @param message 消息
     * @param <T>     泛型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return error(403, message);
    }

    /**
     * 创建资源不存在响应
     *
     * @param message 消息
     * @param <T>     泛型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }

    /**
     * 包装原始数据
     *
     * @param data 原始数据
     * @param <T>  泛型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> wrap(T data) {
        if (data instanceof ApiResponse) {
            return (ApiResponse<T>) data;
        }
        return success(data);
    }

    /**
     * 判断是否成功
     *
     * @return true 如果成功
     */
    public boolean isSuccess() {
        return Boolean.TRUE.equals(success) || (code != null && code == 200);
    }
}
