package com.carlos.core.exception;


import com.carlos.core.response.ErrorCode;

/**
 * <p>
 * 数据库操作异常父类
 * </p>
 *
 * @author carlos
 * @date 2020/4/14 13:37
 */
public class DaoException extends GlobalException {

    private static final long serialVersionUID = -6912618737345878854L;

    public DaoException() {
        super("数据操作异常");
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DaoException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public DaoException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
