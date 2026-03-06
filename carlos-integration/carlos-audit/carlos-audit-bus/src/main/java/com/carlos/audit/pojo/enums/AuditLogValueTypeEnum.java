package com.carlos.audit.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 审计日志值类型枚举
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@AppEnum(code = "AuditLogValueType")
@Getter
@AllArgsConstructor
public enum AuditLogValueTypeEnum implements BaseEnum {

    /**
     * 字符串
     */
    STRING(1, "字符串"),

    /**
     * 数值
     */
    NUMBER(2, "数值"),

    /**
     * JSON
     */
    JSON(3, "JSON"),

    /**
     * 布尔值
     */
    BOOLEAN(4, "布尔值"),

    /**
     * 日期时间
     */
    DATETIME(5, "日期时间"),

    /**
     * 大文本
     */
    TEXT(6, "大文本");

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
