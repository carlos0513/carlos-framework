package com.carlos.audit.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 审计日志对象类型枚举
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@AppEnum(code = "AuditLogTargetType")
@Getter
@AllArgsConstructor
public enum AuditLogTargetTypeEnum implements BaseEnum {

    /**
     * 订单
     */
    ORDER(1, "订单"),

    /**
     * 用户
     */
    USER(2, "用户"),

    /**
     * 配置
     */
    CONFIG(3, "配置"),

    /**
     * 角色
     */
    ROLE(4, "角色"),

    /**
     * 权限
     */
    PERMISSION(5, "权限"),

    /**
     * 部门
     */
    DEPARTMENT(6, "部门"),

    /**
     * 文件
     */
    FILE(7, "文件"),

    /**
     * 字典
     */
    DICT(8, "字典"),

    /**
     * 菜单
     */
    MENU(9, "菜单"),

    /**
     * 其他
     */
    OTHER(99, "其他");

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
