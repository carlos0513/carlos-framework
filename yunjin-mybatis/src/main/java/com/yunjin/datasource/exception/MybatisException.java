package com.yunjin.datasource.exception;


import com.yunjin.core.exception.ComponentException;

/**
 * <p>
 * mybatis异常父类
 * </p>
 *
 * @author yunjin
 * @date 2022/10/24 13:20
 */
public class MybatisException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public MybatisException() {
        super("mybatis exception!");
    }

    public MybatisException(String message) {
        super(message);
    }

    public MybatisException(Throwable cause) {
        super(cause);
    }

    public MybatisException(String message, Throwable cause) {
        super(message, cause);
    }
}
