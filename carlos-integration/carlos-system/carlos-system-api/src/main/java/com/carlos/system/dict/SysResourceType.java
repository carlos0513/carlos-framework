package com.carlos.system.dict;

import com.carlos.core.dict.Dict;
import com.carlos.core.dict.DictEnum;
import com.carlos.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统资源类型
 *
 * @author yunjin
 * @date 2020/4/11 23:26
 */
@Getter
@AllArgsConstructor
@Dict(code = "sys_resource_type", name = "系统资源类型")
public enum SysResourceType implements DictEnum {

    /**
     * 按钮
     */
    BUTTON("resource_type_button", "按钮"),

    /**
     * 链接
     */
    LINK("resource_type_link", "链接"),

    /**
     * 列表
     */
    LIST("resource_type_list", "列表"),

    /**
     * 其他
     */
    OTHER("resource_state_other", "其他"),
    ;

    private final String code;

    private final String name;

    public static DictEnum ofCode(final String code) {
        final SysResourceType[] values = SysResourceType.values();
        for (final SysResourceType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("Can't find enum of code " + code);
    }
}
