package com.carlos.boot.validator.constraints;

import com.carlos.boot.validator.EnumValidator;
import com.carlos.core.enums.BaseEnum;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举类型注解
 *
 * @author yunjin
 * @date 2018-11-08
 */
@Documented
@Constraint(validatedBy = {EnumValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface EnumType {

    String message() default "{请输入正确的类型值}";

    Class<? extends BaseEnum> type();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
