
package com.carlos.org.pojo.emuns;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户菜单收藏数据类型
 *
 * @author yunjin
 * @date 2021/11/17 23:54
 */
@AppEnum(code = "ComplainStatusEnum")
@Getter
@AllArgsConstructor
public enum ComplainStatusEnum implements BaseEnum {

    UN_ACCEPTED(1, "未受理"),

    ACCEPTED(2, "已受理"),

    HANDLED(3, "已处理"),

    UP_REPORT(4, "已上报"),

    DENY_ACCEPTED(5, "不予受理"),

    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    public static String getDescByCode(final Integer code) {

        for (final ComplainStatusEnum value : ComplainStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ComplainStatusEnum codeOf(final Integer code) {
        final ComplainStatusEnum[] values = ComplainStatusEnum.values();
        for (final ComplainStatusEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("数据类型不合法");
    }
}
