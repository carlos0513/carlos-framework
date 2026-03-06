package com.carlos.audit.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 审计日志归档状态枚举
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@AppEnum(code = "AuditLogArchiveState")
@Getter
@AllArgsConstructor
public enum AuditLogArchiveStateEnum implements BaseEnum {

    /**
     * 归档中
     */
    ARCHIVING(1, "归档中"),

    /**
     * 归档完成
     */
    COMPLETED(2, "归档完成"),

    /**
     * 归档失败
     */
    FAILED(3, "归档失败"),

    /**
     * 已删除
     */
    DELETED(4, "已删除"),

    /**
     * 已恢复
     */
    RESTORED(5, "已恢复");

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
