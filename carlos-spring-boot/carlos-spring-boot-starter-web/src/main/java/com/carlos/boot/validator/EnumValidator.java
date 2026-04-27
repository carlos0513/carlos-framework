package com.carlos.boot.validator;

import com.carlos.boot.validator.constraints.EnumType;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.util.EnumUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * <p>
 * 枚举字段验证
 * </p>
 *
 * @author carlos
 * @date 2020/4/15 16:37
 */
public class EnumValidator implements ConstraintValidator<EnumType, Object> {

    private Class<? extends BaseEnum<?>> enumEntity;

    @Override
    public void initialize(final EnumType parameters) {
        enumEntity = parameters.type();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return EnumUtil.getByCode(enumEntity, value) != null;
    }
}
