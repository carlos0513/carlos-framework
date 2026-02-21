package com.yunjin.board.data.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Title: BoardTaskRunningOverviewDataResult
 * @Author fxd
 * @Date 2025/11/20 11:12
 * @description: BoardTaskRunningOverviewDataResult
 */
@Data
@Accessors(chain = true)
public class BoardTaskRunningOverviewDataResult extends BoardDataResult {

    /**
     * 派发任务总量
     */
    private MetricItem dispatchedTaskTotal;

    /**
     * 收集数据总量
     */
    private MetricItem collectedDataTotal;

    /**
     * 近30日任务运行情况
     */
    private Last30DaysTaskMetrics last30DaysTaskMetrics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class MetricItem {
        /**
         * 指标键名
         */
        private String key;

        /**
         * 任务数量/数据总量
         */
        private Long count;

        /**
         * 描述信息（可选）
         */
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Last30DaysTaskMetrics {
        /**
         * 总任务数
         */
        private MetricItem totalTasks;

        /**
         * 进行中任务数
         */
        private MetricItem ongoingTasks;

        /**
         * 超期任务数
         */
        private MetricItem overdueTasks;
    }
}