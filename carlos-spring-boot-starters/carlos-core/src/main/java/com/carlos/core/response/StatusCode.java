package com.carlos.core.response;

/**
 * <p>
 * REST API 响应码枚举
 * </p>
 *
 * @author carlos
 * @date 2020/4/11 22:57
 */
public enum StatusCode {

    /**
     * 操作成功
     */
    SUCCESS(2000, "操作成功"),
    /**
     * 登录失败
     */
    LOGIN_EXCEPTION(4000, "登录失败"),
    /**
     * 非法访问
     */
    UNAUTHORIZED(4001, "非法访问"),
    /**
     * 没有权限
     */
    NOT_PERMISSION(4003, "没有权限"),
    /**
     * 你请求的资源不存在
     */
    NOT_FOUND(4004, "你请求的资源不存在"),
    /**
     * 操作失败
     */
    FAIL(5000, "操作失败"),
    /**
     * 请求参数校验异常
     */
    PARAMETER_EXCEPTION(5001, "请求参数校验异常"),
    /**
     * 请求参数解析异常
     */
    PARAMETER_PARSE_EXCEPTION(5002, "请求参数解析异常"),
    /**
     * HTTP内容类型异常
     */
    HTTP_MEDIA_TYPE_EXCEPTION(5003, "HTTP内容类型异常"),
    /**
     * 业务处理异常
     */
    BUSINESS_EXCEPTION(5101, "业务处理异常"),
    /**
     * 数据库处理异常
     */
    DAO_EXCEPTION(5102, "数据库处理异常"),
    /**
     * 验证码校验异常
     */
    VERIFICATION_CODE_EXCEPTION(5103, "验证码校验异常"),
    /**
     * 登录授权异常
     */
    AUTHENTICATION_EXCEPTION(5104, "登录授权异常"),
    /**
     * 没有访问权限
     */
    UNAUTHENTICATED_EXCEPTION(5105, "没有访问权限"),
    /**
     * 没有访问权限
     */
    UNAUTHORIZED_EXCEPTION(5106, "没有访问权限"),
    /**
     * JWT Token解析异常
     */
    JWT_DECODE_EXCEPTION(5107, "Token解析异常"),

    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(5108, "请求方式不合法");

    private final int code;

    private final String message;

    StatusCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据状态码获取状态码枚举对象
     *
     * @param code 状态码
     * @author carlos
     * @date 2020/6/30 13:50
     */
    public static StatusCode getApiCode(int code) {
        StatusCode[] ecs = StatusCode.values();
        for (StatusCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
