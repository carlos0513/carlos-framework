package com.yunjin.org.dict;

import com.yunjin.core.dict.Dict;
import com.yunjin.core.dict.DictEnum;
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
@Dict(code = "ums_role_state", name = "系统角色状态")
public enum UmsRoleState implements DictEnum {

    /**
     * 启用
     */
    ENABLE("role_state_enable", "启用"),

    /**
     * 禁用
     */
    DISABLE("role_state_disable", "禁用"),
    ;

    private final String code;

    private final String name;
}
