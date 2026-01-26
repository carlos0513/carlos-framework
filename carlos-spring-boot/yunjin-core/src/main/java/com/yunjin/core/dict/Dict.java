package com.yunjin.core.dict;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 标记枚举属于字典枚举
 * </p>
 *
 * @author yunjin
 * @date 2021/12/9 14:40
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Dict {

    /**
     * 字典code
     */
    String code();

    /**
     * 字典名称
     */
    String name();

}
