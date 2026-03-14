package com.carlos.flowable.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 *
 * @author Carlos
 * @date 2026-01-27
 */
@AppEnum(code = "TaskStatus")
@Getter
@AllArgsConstructor
public enum TaskStatusEnum implements BaseEnum {

    /**
     * 待处理
     */
    PENDING(0, "待处理"),

    /**
     * 已处理
     */
    COMPLETED(1, "已处理"),

    /**
     * 已转办
     */
    DELEGATED(2, "已转办"),

    /**
     * 已签收
     */
    CLAIMED(3, "已签收"),

    /**
     * 已退回
     */
    RETURNED(4, "已退回");

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
