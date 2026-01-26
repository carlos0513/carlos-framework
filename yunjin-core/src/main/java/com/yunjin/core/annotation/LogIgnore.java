package com.yunjin.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 忽略操作日志记录注解, 可用户方法和类上 在controller上标注该方法后，将不会记录操作日志 可以标注在类和方法上，如果标记在类上，则会忽略controller中的所有方法
 * </p>
 *
 * @author yunjin
 * @date 2020/4/13 22:33
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogIgnore {

}
