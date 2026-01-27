package com.carlos.core.enums;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义应用枚举标记注解（用于反射扫描） 该注解可以被子类继承
 *
 * @author yunjin
 * @date 2020/3/19
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AppEnum {

    /**
     * 枚举code
     */
    String code();

}
