package com.carlos.cloud.feign;


import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.StatusCode;

/**
 * <p>
 * openfeign异常父类
 * </p>
 *
 * @author yunjin
 * @date 2022/6/16 13:41
 */
public class OpenFeignException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public OpenFeignException() {
        super("openfeign调用异常！");
    }

    public OpenFeignException(final String message) {
        super(message);
    }

    public OpenFeignException(final Integer errorCode, final String message) {
        super(errorCode, message);
    }

    public OpenFeignException(final StatusCode statusCode) {
        super(statusCode);
    }

    public OpenFeignException(final Throwable cause) {
        super(cause);
    }

    public OpenFeignException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
