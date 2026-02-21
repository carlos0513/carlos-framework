package com.yunjin.org.dict;

import com.yunjin.core.dict.Dict;
import com.yunjin.core.dict.DictEnum;
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
@Dict(code = "ums_resource_type", name = "系统资源类型")
public enum UmsResourceType implements DictEnum {

    /**
     * 按钮
     */
    BUTTON("button", "按钮"),

    /**
     * 列表
     */
    LIST("list", "列表"),

    /**
     * 超链接
     */
    HYPER_LINK("hyper_link", "超链接"),
    ;

    private final String code;

    private final String name;
}
