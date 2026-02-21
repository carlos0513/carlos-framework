package com.yunjin.org.pojo.emuns;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AppEnum(code = "FileEnum")
@Getter
@AllArgsConstructor
public enum HelpFileEnum implements BaseEnum {

    WORDFILE(0, "文本"),
    VIDEOFILE(1, "视频"),
    PDFFILE(2, "PDF")
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

    public static HelpFileEnum codeOf(final Integer code) {
        final HelpFileEnum[] values = HelpFileEnum.values();
        for (final HelpFileEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("搜索类型不合法");
    }
}