package com.carlos.org.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 权限类型枚举
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@AppEnum(code = "OrgPermissionType")
@Getter
@AllArgsConstructor
public enum OrgPermissionTypeEnum implements BaseEnum {

    /**
     * 菜单
     */
    MENU(1, "菜单"),

    /**
     * 按钮
     */
    BUTTON(2, "按钮"),

    /**
     * API接口
     */
    API(3, "API接口"),

    /**
     * 数据字段
     */
    DATA_FIELD(4, "数据字段");

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
