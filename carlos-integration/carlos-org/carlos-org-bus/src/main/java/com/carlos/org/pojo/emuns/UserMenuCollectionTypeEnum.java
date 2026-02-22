
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
 * @author carlos
 * @date 2021/11/17 23:54
 */
@AppEnum(code = "UserMenuCollectionTypeEnum")
@Getter
@AllArgsConstructor
public enum UserMenuCollectionTypeEnum implements BaseEnum {
    MARKED(1, "收藏"),
    UNMARKED(0, "未收藏"),
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


    public static UserMenuCollectionTypeEnum codeOf(final Integer code) {
        final UserMenuCollectionTypeEnum[] values = UserMenuCollectionTypeEnum.values();
        for (final UserMenuCollectionTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("数据类型不合法");
    }
}
