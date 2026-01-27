package com.carlos.mongodb;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 填充字段标识
 *
 * @author Carlos
 * @date 2021/12/24 14:06
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldFill {

    /**
     * 字段自动填充策略
     */
    FieldFillStrategy strategy() default FieldFillStrategy.DEFAULT;
}
