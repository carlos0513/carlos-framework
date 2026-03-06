package com.carlos.audit.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 审计日志状态枚举
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@AppEnum(code = "AuditLogState")
@Getter
@AllArgsConstructor
public enum AuditLogStateEnum implements BaseEnum {

    /**
     * 成功
     */
    SUCCESS(1, "成功"),

    /**
     * 失败
     */
    FAIL(2, "失败"),

    /**
     * 处理中
     */
    PENDING(3, "处理中"),

    /**
     * 超时
     */
    TIMEOUT(4, "超时"),

    /**
     * 部分成功
     */
    PARTIAL_SUCCESS(5, "部分成功");

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
