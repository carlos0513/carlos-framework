package com.carlos.excel.luckysheet;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 单元格数据类型
 * </p>
 *
 * @author Carlos
 * @date 2023/9/4 13:55
 */
@Getter
@AllArgsConstructor
public enum CellDataType {
    /**
     *
     */
    AUTO("g/n", "自动"),
    TEXT("s", "纯文本"),
    NUMBER("n", "数字"),
    DATE("d", "时间"),

    ;

    /**
     * 数据类型
     */
    private final String dataType;
    /**
     * 描述
     */
    private final String desc;
}
