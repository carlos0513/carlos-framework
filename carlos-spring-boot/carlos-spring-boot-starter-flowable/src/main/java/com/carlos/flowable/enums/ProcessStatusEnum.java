package com.carlos.flowable.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程状态枚举
 *
 * @author Carlos
 * @date 2026-01-27
 */
@AppEnum(code = "ProcessStatus")
@Getter
@AllArgsConstructor
public enum ProcessStatusEnum implements BaseEnum {

    /**
     * 草稿
     */
    DRAFT(0, "草稿"),

    /**
     * 运行中
     */
    RUNNING(1, "运行中"),

    /**
     * 已完成
     */
    COMPLETED(2, "已完成"),

    /**
     * 已终止
     */
    TERMINATED(3, "已终止"),

    /**
     * 已挂起
     */
    SUSPENDED(4, "已挂起");

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
