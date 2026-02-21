package com.yunjin.board.data.result;

import com.yunjin.metric.TaskMetricEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BoardTaskRunningOverviewV2DataResult extends BoardDataResult {
    private List<ItemTaskName> regions;


    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class ItemTaskName {
        private String typeDescription;
        /**
         * 派发任务总量
         */
        private Item dispatchedTaskTotal;
        /**
         * 收集数据总量
         */
        private Item collectedDataTotal;
        /**
         * 县级
         */
        private Item UnderCounty;
        /**
         * 街镇
         */
        private Item UnderTown;
        /**
         * 村社
         */
        private Item UnderVillage;
        /**
         * 网格
         */
        private Item UnderGrid;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class Item {
        /**
         * key
         */
        private TaskMetricEnum key;
        /**
         * 描述(县级、街镇、村社、网格)
         */
        private String description;

        /**
         * 任务数量
         */
        private Long count;

    }
}
