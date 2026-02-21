package com.yunjin.board.data.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 看板数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BoardRefluxDataResult extends BoardDataResult {

    /**
     * 回流数据集数量
     */
    private int refluxDatasetCount;

    /**
     * 回流数据项数量
     */
    private int refluxDataItemCount;

    /**
     * 回流数据总量
     */
    private long refluxDataVolume;


    /**
     * 颜色数组字符串，用于排行图表
     */
    private String colorForRank;

    /**
     * 回流排行
     */
    private List<TopicRank> refluxRank;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicRank implements Serializable {
        /**
         * 主题名称
         */
        private String topicName;

        /**
         * 各层级数据量统计
         */
        private List<LevelCount> levelCounts;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LevelCount implements Serializable {
        /**
         * 层级名称
         */
        private String levelName;

        /**
         * 数据量
         */
        private long dataCount;

        /**
         * 颜色值
         */
        private String color;
    }
}
