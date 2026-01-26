package com.yunjin.excel.luckysheet;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 单元格对象
 * </p>
 *
 * @author Carlos
 * @date 2023/9/4 13:40
 */
@NoArgsConstructor
@Data
public class CellData {

    /**
     * 行
     */
    private Integer r;
    /**
     * 列
     */
    private Integer c;
    /**
     * 单元格对象
     */
    private V v;


    @NoArgsConstructor
    @Data
    public static class V {

        /**
         * 单元格值格式
         */
        private Ct ct;
        /**
         * 内容的原始值为
         */
        private String v;
        /**
         * 内容显示值
         */
        private String m;
        /**
         * 背景色
         */
        private String bg;
        /**
         * 字体格式
         */
        private Integer ff;
        /**
         * 字体颜色
         */
        private String fc;
        /**
         * 字体加粗
         */
        private Integer bl;
        /**
         * 字体斜体
         */
        private Integer it;
        /**
         * 字体大小
         */
        private Integer fs;
        /**
         * 启动删除线
         */
        private Integer cl;
        /**
         * 水平居中
         */
        private Integer ht;
        /**
         * 垂直居中
         */
        private Integer vt;
        /**
         * 文字旋转
         */
        private Integer tr;
        /**
         * 文本自动换行
         */
        private Integer tb;
        /**
         * 批注
         */
        private Ps ps;
        /**
         * 单元格公式
         */
        private String f;

    }

    /**
     * 单元格值格式
     */
    @NoArgsConstructor
    @Data
    public static class Ct {

        /**
         * 格式名称为自动格式
         */
        private String fa;
        /**
         * 格式类型为数字类型
         */
        private String t;
    }

    /**
     * 批注
     */
    @NoArgsConstructor
    @Data
    public static class Ps {

        /**
         * 批注框左边距
         */
        private Integer left;
        /**
         * 批注框上边距
         */
        private Integer top;
        /**
         * 批注框宽度
         */
        private Integer width;
        /**
         * 批注框高度
         */
        private Integer height;
        /**
         * 批注内容
         */
        private String value;
        /**
         * 批注框为显示状态
         */
        private Boolean isshow;
    }
}
