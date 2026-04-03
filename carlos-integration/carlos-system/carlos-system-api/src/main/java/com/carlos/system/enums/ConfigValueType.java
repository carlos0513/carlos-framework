package com.carlos.system.enums;

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
@AppEnum(code = "ConfigValueType")
public enum ConfigValueType implements BaseEnum {

    /**
     * 系统配置值类型
     */
    TEXT(1, "文本"),
    FILE(2, "文件"),
    ;

    private final Integer code;

    private final String desc;

    public static ConfigValueType ofCode(final Integer code) {
        final ConfigValueType[] values = ConfigValueType.values();
        for (final ConfigValueType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new BusinessException("Can't find enum of code " + code);
    }
}
