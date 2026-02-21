
package com.yunjin.org.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AppEnum(code = "ComplainTypeEnum")
@Getter
@AllArgsConstructor
public enum ComplainTypeEnum implements BaseEnum {


    SAME_TASK(31, "相同相似任务"),

    NOT_MATCH_TASK(32, "不符合管理办法任务");

    @EnumValue
    private final Integer code;

    private final String desc;

    public static String getDescByCode(final Integer code) {

        for (final ComplainTypeEnum value : ComplainTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ComplainTypeEnum codeOf(final Integer code) {
        final ComplainTypeEnum[] values = ComplainTypeEnum.values();
        for (final ComplainTypeEnum value : values) {
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
