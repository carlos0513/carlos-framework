package com.carlos.audit.api.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 审计日志主体类型枚举
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@AppEnum(code = "AuditLogPrincipalType")
@Getter
@AllArgsConstructor
public enum AuditLogPrincipalTypeEnum implements BaseEnum {

    /**
     * 用户
     */
    USER(1, "用户"),

    /**
     * 服务
     */
    SERVICE(2, "服务"),

    /**
     * 系统
     */
    SYSTEM(3, "系统"),

    /**
     * 匿名用户
     */
    ANONYMOUS(4, "匿名用户");

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
