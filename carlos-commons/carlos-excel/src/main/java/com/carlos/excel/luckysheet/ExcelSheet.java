package com.carlos.excel.luckysheet;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * Excel中一个sheet页
 * </p>
 *
 * @author Carlos
 * @date 2023/9/4 13:47
 */
@Data
@NoArgsConstructor
public class ExcelSheet {

    /**
     * 工作表名称
     */
    private String name;
    /**
     * 工作表颜色
     */
    private String color;
    /**
     * 工作表索引
     */
    private Integer index;
    /**
     * 激活状态
     */
    private Integer status;
    /**
     * 工作表小标
     */
    private Integer order;
    /**
     * 是否隐藏
     */
    private Integer hide;
    /**
     * 行数
     */
    private Integer row;
    /**
     * 列数
     */
    private Integer column;
    /**
     * 自定义行高
     */
    private Integer defaultRowHeight;
    /**
     * 自定义列宽
     */
    private Integer defaultColWidth;
    /**
     * 初始化使用的单元格数据
     */
    private List<CellData> celldata;
    /**
     * config
     */
    private Config config;
    /**
     * 左右滚动条位置
     */
    private Integer scrollLeft;
    /**
     * 上下滚动条位置
     */
    private Integer scrollTop;
    /**
     * 选中区域
     */
    private List<?> luckysheetSelectSave;
    /**
     * 公式链
     */
    private List<?> calcChain;
    /**
     * 是否数据透视表
     */
    private Boolean isPivotTable;
    /**
     * 数据透视表设置
     */
    private PivotTable pivotTable;
    /**
     * 筛选范围
     */
    private FilterSelect filterSelect;
    /**
     * 筛选设置
     */
    private Object filter;
    /**
     * 交替颜色
     */
    private List<?> luckysheetAlternateformatSave;
    /**
     * 自定义交替颜色
     */
    private List<?> luckysheetAlternateformatSaveModelcustom;
    /**
     * 条件格式
     */
    private LuckysheetConditionformatSave luckysheetConditionformatSave;
    /**
     * 冻结行列配置
     */
    private Frozen frozen;
    /**
     * 图表配置
     */
    private List<?> chart;
    /**
     * 缩放比例
     */
    private Integer zoomRatio;
    /**
     * 图片
     */
    private List<?> image;
    /**
     * 是否显示网格线
     */
    private Integer showGridLines;
    /**
     * 数据验证配置
     */
    private DataVerification dataVerification;

    /**
     * <p>
     * 数据验证配置
     * </p>
     *
     * @author Carlos
     * @date 2023/9/4 13:32
     */
    public static class DataVerification {

    }

    /**
     * <p>
     * 冻结行列配置
     * </p>
     *
     * @author Carlos
     * @date 2023/9/4 13:33
     */
    public static class Frozen {

    }

    public static class LuckysheetConditionformatSave {

    }

    public static class FilterSelect {

    }

    public static class PivotTable {

    }

    @NoArgsConstructor
    @Data
    public static class Config {

        /**
         * 合并单元格
         */
        private Merge merge;
        /**
         * 表格行高
         */
        private Rowlen rowlen;
        /**
         * 表格列宽
         */
        private Columnlen columnlen;
        /**
         * 隐藏行
         */
        private Rowhidden rowhidden;
        /**
         * 隐藏列
         */
        private Colhidden colhidden;
        /**
         * 边框
         */
        private BorderInfo borderInfo;
        /**
         * 工作表保护
         */
        private Authority authority;

        /**
         * Merge
         */
        @NoArgsConstructor
        @Data
        public static class Merge {
            private List<MergeDetail> details;
        }

        /**
         * MergeDetail
         */
        @NoArgsConstructor
        @Data
        public static class MergeDetail {
            private Integer r;
            private Integer c;
            private Integer rs;
            private Integer cs;
        }

        /**
         * Rowlen
         */
        @NoArgsConstructor
        @Data
        public static class Rowlen {

        }

        /**
         * Columnlen
         */
        @NoArgsConstructor
        @Data
        public static class Columnlen {

        }

        /**
         * Rowhidden
         */
        @NoArgsConstructor
        @Data
        public static class Rowhidden {

        }

        /**
         * Colhidden
         */
        @NoArgsConstructor
        @Data
        public static class Colhidden {

        }

        /**
         * BorderInfo
         */
        @NoArgsConstructor
        @Data
        public static class BorderInfo {

        }

        /**
         * Authority
         */
        @NoArgsConstructor
        @Data
        public static class Authority {

        }
    }
}
