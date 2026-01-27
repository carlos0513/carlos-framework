package com.carlos.docking.linkage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 错误码枚举
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 11:01
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    /**
     *
     */
    A_001("-1", "操作失败"),
    A_002("0", "访问异常"),
    A_003("1", "操作成功"),
    A_004("2", "参数格式不正确"),
    A_005("3", "参数不能为空"),
    A_006("4", "数据已存在，无法新增"),
    A_007("5", "数据不存在，无法修改（删除）"),
    A_008("6", "区域码值所属关系错误"),
    A_009("7", "码值不存在"),


    ;

    private final String code;
    private final String name;

    public static ErrorCodeEnum ofCode(String code) {
        ErrorCodeEnum[] values = ErrorCodeEnum.values();
        for (ErrorCodeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
