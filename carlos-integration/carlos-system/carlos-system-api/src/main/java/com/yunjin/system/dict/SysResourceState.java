package com.carlos.system.dict;

import com.carlos.core.dict.Dict;
import com.carlos.core.dict.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统资源状态
 *
 * @author yunjin
 * @date 2020/4/11 23:26
 */
@Getter
@AllArgsConstructor
@Dict(code = "sys_resource_state", name = "系统资源状态")
public enum SysResourceState implements DictEnum {

    /**
     * 启用
     */
    ENABLE("resource_state_enable", "启用"),

    /**
     * 禁用
     */
    DISABLE("resource_state_disable", "禁用"),
    ;

    private final String code;

    private final String name;
}
