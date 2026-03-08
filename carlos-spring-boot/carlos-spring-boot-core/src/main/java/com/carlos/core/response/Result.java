package com.carlos.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * REST API 返回结果
 * </p>
 *
 * @author carlos
 * @date 2020/4/11 22:57
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class Result<T> implements Serializable {

    /**
     * 响应的状态码
     */
    private Integer code;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 响应信息，用来说明响应情况
     */
    private String message;

    /**
     * 异常栈信息
     */
    private String stack;

    /**
     * 响应数据
     */
    private T data;


    public Result() {

    }

    public static <T> Result<T> result(boolean flag) {
        if (flag) {
            return ok();
        }
        return fail();
    }

    public static <T> Result<T> result(StatusCode statusCode) {
        return result(statusCode, null);
    }

    public static <T> Result<T> result(StatusCode statusCode, T data) {
        return result(statusCode, null, data);
    }

    /**
     * 响应数据封装
     *
     * @author carlos
     * @date 2020/5/16 23:32
     */
    public static <T> Result<T> result(StatusCode statusCode, String message, T data) {
        boolean success = false;
        if (statusCode.getCode() == StatusCode.SUCCESS.getCode()) {
            success = true;
        }
        String apiMessage = statusCode.getMessage();
        if (StringUtils.isBlank(message)) {
            message = apiMessage;
        }
        return Result.<T>builder()
                .code(statusCode.getCode())
                .message(message)
                .data(data)
                .success(success)
                .build();
    }
    // region----------------------  成功响应相关结果 start  ------------------------

    /**
     * 快速返回状态码200
     *
     * @author carlos
     * @date 2020/5/16 23:38
     */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /**
     * 成功返回Data数据
     *
     * @author carlos
     * @date 2020/5/16 23:38
     */
    public static <T> Result<T> ok(T data) {
        return result(StatusCode.SUCCESS, data);
    }

    /**
     * 成功返回数据，并且自定义返回消息
     *
     * @author carlos
     * @date 2020/5/16 23:39
     */
    public static <T> Result<T> ok(T data, String message) {
        return result(StatusCode.SUCCESS, message, data);
    }

    /**
     * 快速返回一个键值对数据
     *
     * @author carlos
     * @date 2020/5/16 23:41
     */
    public static Result<Map<String, Object>> ok(String key, Object value) {
        Map<String, Object> map = new HashMap<>(1, 1);
        map.put(key, value);
        return ok(map);
    }

    // endregion----------------------   成功响应相关结果 end   ------------------------

    // region----------------------  失败相关结果 start  ------------------------


    /**
     * 响应数据封装
     *
     * @author carlos
     * @date 2020/5/16 23:32
     */
    public static <T> Result<T> fail(Integer code, String message, String stack) {
        if (code == null) {
            code = StatusCode.FAIL.getCode();
        }
        return Result.<T>builder()
                .code(code)
                .message(message)
                .stack(stack)
                .success(false)
                .build();
    }

    /**
     * 响应数据封装
     *
     * @author carlos
     * @date 2020/5/16 23:32
     */
    public static <T> Result<T> fail(String message, String stack) {
        return fail(StatusCode.FAIL, message, stack);
    }


    /**
     * 响应数据封装
     *
     * @author carlos
     * @date 2020/5/16 23:32
     */
    public static <T> Result<T> fail(StatusCode statusCode, String message, String stack) {
        if (statusCode == null) {
            statusCode = StatusCode.FAIL;
        }

        if (StringUtils.isBlank(message)) {
            message = statusCode.getMessage();
        }
        return Result.<T>builder()
                .code(statusCode.getCode())
                .message(message)
                .stack(stack)
                .success(false)
                .build();
    }

    /**
     * 请求失败结果，返回500，并使用默认的失败信息
     *
     * @author carlos
     * @date 2020/5/16 23:46
     */
    public static <T> Result<T> fail() {
        return fail(StatusCode.FAIL);
    }

    /**
     * 失败快速返回，指定状态码， 使用默认的失败信息
     *
     * @author carlos
     * @date 2020/5/16 23:41
     */
    public static <T> Result<T> fail(StatusCode statusCode) {
        return fail(statusCode, null, null);
    }

    /**
     * fail
     *
     * @param statusCode 状态码
     * @param message    错误信息
     * @return com.carlos.core.response.Result<T>
     * @author Carlos
     * @date 2022/11/16 9:55
     */
    public static <T> Result<T> fail(StatusCode statusCode, String message) {
        return fail(statusCode, message, null);
    }

    /**
     * 返回状态码500，并自定义失败信息
     *
     * @author carlos
     * @date 2020/5/16 23:42
     */
    public static <T> Result<T> fail(String message) {
        return fail(StatusCode.FAIL, message, null);

    }

    /**
     * 返回失败数据和指定状态码
     *
     * @author carlos
     * @date 2020/5/16 23:43
     */
    public static <T> Result<T> fail(StatusCode statusCode, T data) {
        if (StatusCode.SUCCESS == statusCode) {
            throw new RuntimeException("失败结果状态码不能为" + StatusCode.SUCCESS.getCode());
        }
        return result(statusCode, data);

    }

    /**
     * 失败返回自定义状态码和自定义失败信息
     *
     * @author carlos
     * @date 2020/5/16 23:45
     */
    public static Result<String> fail(Integer errorCode, String message) {
        return new Result<String>()
                .setSuccess(false)
                .setCode(errorCode)
                .setMessage(message);
    }


    /**
     * 获取请求结果中的数据
     *
     * @return java.util.Optional<?>
     * @author Carlos
     * @date 2023/7/3 23:37
     */
    public Optional<T> data() {

        if (Boolean.FALSE.equals(this.success)) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.data);
    }


    // endregion----------------------   失败相关结果 end   ------------------------
}
