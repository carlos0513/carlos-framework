package com.carlos.excel.exception;


import com.carlos.core.exception.ComponentException;

/**
 * <p>
 * excel异常父类
 * </p>
 *
 * @author Carlos
 * @date 2022/11/23 11:33
 */
public class ExcelException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public ExcelException() {
        super("excel exception!");
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
