package com.carlos.boot.validator;

import com.carlos.boot.validator.constraints.IdCard;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * <p>
 * 自定义身份证号码验证注解实现类
 * </p>
 *
 * @author yunjin
 * @date 2020/4/15 16:42
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    private static final String REG_EX = "(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{2}[0-9Xx]$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[0-9Xx]$)";
    private static final Pattern PATTERN = Pattern.compile(REG_EX);

    @Override
    public void initialize(final IdCard parameters) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return PATTERN.matcher(value).matches();
    }
}
