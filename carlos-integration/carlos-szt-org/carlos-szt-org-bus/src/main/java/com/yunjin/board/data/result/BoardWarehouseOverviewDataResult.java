package com.yunjin.board.data.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 看板数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@Data
@Accessors(chain = true)
public class BoardWarehouseOverviewDataResult extends BoardDataResult {

    /**
     * 数据集
     */
    private DataItem dataSet;
    /**
     * 数据项
     */
    private DataItem dataItem;
    /**
     * 数据量
     */
    private DataItem dataVolume;

    /**
     * 市级 县级数仓基础共用下-数仓数据统计MAP
     */
    private Map<String, BasicData> warehouseDataMap;


    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataItem implements Serializable {

        /**
         * 镇街道
         */
        private int total;

        /**
         * 卡片列表值
         */
        private List<Item> items;

    }


    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item implements Serializable {

        /**
         * 名称
         */
        private String name;
        /**
         * 镇街道
         */
        private int count;

    }



    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicData implements Serializable {
        /**
         * 数据项列表
         */
        private List<SourceLevelData> sourceLevelData;

        /**
         * 数据集数量
         */
        private int dataSet;

        /**
         * 数据项数量
         */
        private int dataItem;

        /**
         * 数据量
         */
        private int dataVolume;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceLevelData implements Serializable {
        /**
         * 层级名称
         */
        private String sourceLevelName;

        /**
         * 数据集数量
         */
        private int dataSet;

        /**
         * 数据项数量
         */
        private int dataItem;

        /**
         * 数据量
         */
        private int dataVolume;
    }


}
