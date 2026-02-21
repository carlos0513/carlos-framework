
package com.yunjin.org.pojo.emuns;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AppEnum(code = "ComplainTypeEnum")
@Getter
@AllArgsConstructor
public enum ComplainActionEnum implements BaseEnum {

    CREATE(0, "原始上报"),
    ACCEPT(1, "受理"),
    UP_REPORT(2, "上报市级"),
    URGE_REPORT(3, "催办"),
    HANDLED(4, "处理"),
    DENY_ACCEPTED(5, "不予受理"),
    WITHDRAW(6, "撤回"),

    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    public static String getDescByCode(final Integer code) {

        for (final ComplainActionEnum value : ComplainActionEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ComplainActionEnum codeOf(final Integer code) {
        final ComplainActionEnum[] values = ComplainActionEnum.values();
        for (final ComplainActionEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("数据类型不合法");
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
