package com.carlos.org.exception;


import com.carlos.core.exception.ComponentException;

/**
 * <p>
 * 加密异常父类
 * </p>
 *
 * @author Carlos
 * @date 2023/3/22 14:15
 */
public class OrgException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public OrgException() {
        super("用户功能异常!");
    }

    public OrgException(String message) {
        super(message);
    }

    public OrgException(Throwable cause) {
        super(cause);
    }

    public OrgException(String message, Throwable cause) {
        super(message, cause);
    }
}
