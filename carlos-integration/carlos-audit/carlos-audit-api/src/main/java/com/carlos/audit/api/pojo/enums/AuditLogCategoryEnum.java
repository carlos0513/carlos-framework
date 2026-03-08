package com.carlos.audit.api.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 审计日志大类枚举
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@AppEnum(code = "AuditLogCategory")
@Getter
@AllArgsConstructor
public enum AuditLogCategoryEnum implements BaseEnum {

    /**
     * 安全类
     */
    SECURITY(1, "安全类"),

    /**
     * 业务类
     */
    BUSINESS(2, "业务类"),

    /**
     * 系统类
     */
    SYSTEM(3, "系统类"),

    /**
     * 审计类
     */
    AUDIT(4, "审计类");

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
