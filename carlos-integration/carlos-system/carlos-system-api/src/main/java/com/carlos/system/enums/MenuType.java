package com.carlos.system.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传文件用途
 *
 * @author Carlos
 * @date 2022/12/22 11:31
 */
@Getter
@AllArgsConstructor
@AppEnum(code = "MenuType")
public enum MenuType implements BaseEnum {

    /**
     * 系统菜单类型
     */
    PC(1, "pc端菜单"),
    MOBILE(2, "移动端菜单"),
    MANAGE(3, "管理端"),
    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    public static MenuType ofCode(final Integer code) {
        final MenuType[] values = MenuType.values();
        for (final MenuType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new BusinessException("Can't find enum of code " + code);
    }
}
