package com.yunjin.org.pojo.emuns;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *   用户卡片位置
 * </p>
 *
 * @author Carlos
 * @date 2025-10-14 15:44
 */
@AppEnum(code = "UserCardLocation")
@Getter
@AllArgsConstructor
public enum UserCardLocationEnum implements BaseEnum {

    /**
     * 用户状态
     */

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

}
