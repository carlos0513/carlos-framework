package com.carlos.audit.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 审计日志类型枚举
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@AppEnum(code = "AuditLogType")
@Getter
@AllArgsConstructor
public enum AuditLogTypeEnum implements BaseEnum {

    /**
     * 用户登录
     */
    USER_LOGIN(1, "用户登录"),

    /**
     * 用户登出
     */
    USER_LOGOUT(2, "用户登出"),

    /**
     * 订单支付
     */
    ORDER_PAY(3, "订单支付"),

    /**
     * 数据查询
     */
    DATA_QUERY(4, "数据查询"),

    /**
     * 数据新增
     */
    DATA_CREATE(5, "数据新增"),

    /**
     * 数据更新
     */
    DATA_UPDATE(6, "数据更新"),

    /**
     * 数据删除
     */
    DATA_DELETE(7, "数据删除"),

    /**
     * 权限变更
     */
    PERMISSION_CHANGE(8, "权限变更"),

    /**
     * 配置变更
     */
    CONFIG_CHANGE(9, "配置变更"),

    /**
     * 导出操作
     */
    DATA_EXPORT(10, "导出操作"),

    /**
     * 导入操作
     */
    DATA_IMPORT(11, "导入操作"),

    /**
     * 审批操作
     */
    APPROVAL(12, "审批操作"),

    /**
     * 文件上传
     */
    FILE_UPLOAD(13, "文件上传"),

    /**
     * 文件下载
     */
    FILE_DOWNLOAD(14, "文件下载"),

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
