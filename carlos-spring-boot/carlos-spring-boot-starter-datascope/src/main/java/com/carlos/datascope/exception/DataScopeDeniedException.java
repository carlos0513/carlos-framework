package com.carlos.datascope.exception;

/**
 * 数据权限拒绝异常
 * <p>
 * 当用户没有数据权限时抛出
 *
 * @author Carlos
 * @version 2.0
 */
public class DataScopeDeniedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 拒绝原因代码
     */
    private String code;

    public DataScopeDeniedException(String message) {
        super(message);
        this.code = "DATA_SCOPE_DENIED";
    }

    public DataScopeDeniedException(String code, String message) {
        super(message);
        this.code = code;
    }

    public DataScopeDeniedException(String message, Throwable cause) {
        super(message, cause);
        this.code = "DATA_SCOPE_DENIED";
    }

    public String getCode() {
        return code;
    }
}
