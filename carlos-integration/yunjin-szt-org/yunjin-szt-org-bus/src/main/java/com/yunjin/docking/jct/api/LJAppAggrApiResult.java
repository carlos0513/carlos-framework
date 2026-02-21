package com.yunjin.docking.jct.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunjin.docking.jct.LJAppAggrStatusCode;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 统一返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@Data
@Accessors(chain = true)
@Builder
public class LJAppAggrApiResult<T> implements Serializable {

    /**
     * 错误码
     */
    @JsonProperty("code")
    private String code;
    /**
     * 错误信息
     */
    @JsonProperty("msg")
    private String msg;
    /**
     * 成功收到通知响应 0；异常1
     */
    @JsonProperty("status")
    private Integer status;
    /**
     * 响应内容
     */
    @JsonProperty("data")
    private T data;


    public static <T> LJAppAggrApiResult<T> result(boolean flag) {
        if (flag) {
            return ok();
        }
        return fail();
    }

    public static <T> LJAppAggrApiResult<T> result(LJAppAggrStatusCode statusCode) {
        return result(statusCode, null);
    }

    public static <T> LJAppAggrApiResult<T> result(LJAppAggrStatusCode statusCode, T data) {
        return result(statusCode, null, data);
    }

    /**
     * 响应数据封装
     *
     * @author yunjin
     * @date 2020/5/16 23:32
     */
    public static <T> LJAppAggrApiResult<T> result(LJAppAggrStatusCode statusCode, String message, T data) {
        boolean success = false;
        if (statusCode.getCode() == LJAppAggrStatusCode.SUCCESS.getCode()) {
            success = true;
        }
        String apiMessage = statusCode.getMessage();
        if (StringUtils.isBlank(message)) {
            message = apiMessage;
        }

        return LJAppAggrApiResult.<T>builder()
                .status(statusCode.getCode())
                .msg(message)
                .data(data)
                .code(String.valueOf(statusCode.getCode()))
                .build();
    }
    // region----------------------  成功响应相关结果 start  ------------------------

    /**
     * 快速返回状态码200
     *
     * @author yunjin
     * @date 2020/5/16 23:38
     */
    public static <T> LJAppAggrApiResult<T> ok() {
        return ok(null);
    }

    /**
     * 成功返回Data数据
     *
     * @author yunjin
     * @date 2020/5/16 23:38
     */
    public static <T> LJAppAggrApiResult<T> ok(T data) {
        return result(LJAppAggrStatusCode.SUCCESS, data);
    }

    /**
     * 成功返回数据，并且自定义返回消息
     *
     * @author yunjin
     * @date 2020/5/16 23:39
     */
    public static <T> LJAppAggrApiResult<T> ok(T data, String message) {
        return result(LJAppAggrStatusCode.SUCCESS, message, data);
    }

    /**
     * 快速返回一个键值对数据
     *
     * @author yunjin
     * @date 2020/5/16 23:41
     */
    public static LJAppAggrApiResult<Map<String, Object>> ok(String key, Object value) {
        Map<String, Object> map = new HashMap<>(1, 1);
        map.put(key, value);
        return ok(map);
    }

    // endregion----------------------   成功响应相关结果 end   ------------------------

    // region----------------------  失败相关结果 start  ------------------------


    /**
     * 响应数据封装
     *
     * @author yunjin
     * @date 2020/5/16 23:32
     */
    public static <T> LJAppAggrApiResult<T> fail(Integer code, String message) {
        if (code == null) {
            code = LJAppAggrStatusCode.FAIL.getCode();
        }
        return LJAppAggrApiResult.<T>builder()
                .status(code)
                .msg(message)
                .code(code.toString())
                .build();
    }


    /**
     * 响应数据封装
     *
     * @author yunjin
     * @date 2020/5/16 23:32
     */
    public static <T> LJAppAggrApiResult<T> fail(LJAppAggrStatusCode statusCode, String message) {
        if (statusCode == null) {
            statusCode = LJAppAggrStatusCode.FAIL;
        }

        if (StringUtils.isBlank(message)) {
            message = statusCode.getMessage();
        }
        return LJAppAggrApiResult.<T>builder()
                .status(statusCode.getCode())
                .msg(message)
                .code(String.valueOf(statusCode.getCode()))
                .build();
    }

    /**
     * 请求失败结果，返回500，并使用默认的失败信息
     *
     * @author yunjin
     * @date 2020/5/16 23:46
     */
    public static <T> LJAppAggrApiResult<T> fail() {
        return fail(LJAppAggrStatusCode.FAIL);
    }

    /**
     * 失败快速返回，指定状态码， 使用默认的失败信息
     *
     * @author yunjin
     * @date 2020/5/16 23:41
     */
    public static <T> LJAppAggrApiResult<T> fail(LJAppAggrStatusCode statusCode) {
        return fail(statusCode, null);
    }

    /**
     * 返回状态码500，并自定义失败信息
     *
     * @author yunjin
     * @date 2020/5/16 23:42
     */
    public static <T> LJAppAggrApiResult<T> fail(String message) {
        return fail(LJAppAggrStatusCode.FAIL, message);

    }

    /**
     * 返回失败数据和指定状态码
     *
     * @author yunjin
     * @date 2020/5/16 23:43
     */
    public static <T> LJAppAggrApiResult<T> fail(LJAppAggrStatusCode statusCode, T data) {
        if (LJAppAggrStatusCode.SUCCESS == statusCode) {
            throw new RuntimeException("失败结果状态码不能为" + LJAppAggrStatusCode.SUCCESS.getCode());
        }
        return result(statusCode, data);

    }

    // endregion----------------------   失败相关结果 end   ------------------------

}
