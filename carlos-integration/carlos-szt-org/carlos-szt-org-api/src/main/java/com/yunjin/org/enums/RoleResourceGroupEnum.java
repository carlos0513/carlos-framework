package com.yunjin.org.enums;

import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shenyong
 * @e-mail sheny60@chinaunicom.cn
 * @date 2024/10/15 10:04
 **/
@AppEnum(code = "RoleResourceGroupEnum")
@Getter
@AllArgsConstructor
public enum RoleResourceGroupEnum implements BaseEnum {
    DEPARTMENT_ADMIN(0, "department-admin", "部门管理员"),
    SYSTEM_ADMIN(0, "system-admin", "系统管理员"),
    DEPARTMENT_OPERATE(0, "departmnet-operate", "部门操作员"),
    ADMISSION_AUDITOR(0, "admission-auditor", "准入审核员"),
    ORDINARY_USERS(0, "ordinary-users", "普通用户");

    private final Integer code;
    private final String group;
    private final String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static RoleResourceGroupEnum codeOf(final String group) {
        final RoleResourceGroupEnum[] values = RoleResourceGroupEnum.values();
        for (final RoleResourceGroupEnum value : values) {
            if (value.getGroup().equals(group)) {
                return value;
            }
        }
        throw new ServiceException("角色权限组不合法");
    }
}
