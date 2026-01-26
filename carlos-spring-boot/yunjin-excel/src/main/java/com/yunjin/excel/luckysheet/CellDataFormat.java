package com.yunjin.excel.luckysheet;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 单元格格式
 * </p>
 *
 * @author Carlos
 * @date 2023/9/4 13:55
 */
@Getter
@AllArgsConstructor
public enum CellDataFormat {
    /**
     *
     */
    GENERAL("General", "g/n", "自动"),
    TEXT("@", "s", "纯文本"),

    ;

    private final String format;
    private final String dataType;
    private final String desc;
}
