package com.yunjin.org.pojo.emuns;

import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * <p>
 *   用户对接操作类型
 * </p>
 *
 * @author Carlos
 * @date 2025-02-28 09:34
 */
@AppEnum(code = "OrgUserDockingOperateType")
@Getter
@AllArgsConstructor
public enum OrgUserDockingOperateTypeEnum implements BaseEnum {

    /**  */
    ADD(0, "新增"),
    UPDATE(1, "修改"),
    DELETE(2, "删除"),
    ENABLE(3, "启用"),
    DISABLE(4, "禁用"),
    SIGN_OFF(5, "注销"),


    ;


    private final Integer code;

    private final String desc;

}
