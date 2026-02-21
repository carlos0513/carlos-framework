package com.yunjin.board.data.result;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.metric.TaskMetric;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

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
public class BoardCustomTaskDataResult extends BoardDataResult {

    /**
     * 待办任务
     */
    private Task undeal;
    /**
     * 下派任务
     */
    private Task sendTask;

    /**
     * 审核任务
     */
    private Task approveTask;

    /**
     * 审核任务
     */
    private Task applyTask;


    @Data
    @Accessors(chain = true)
    public static class Task {

        /**
         * 总数
         */
        private int count;
        /**
         * 列表
         */
        private List<Item> items;
    }

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {

        /**
         * id
         */
        private String id;

        /**
         * 父任务名称
         */
        private String parentTaskName;

        /**
         * 任务名称
         */
        private String name;
        /**
         * 任务状态
         */
        private String state;
        /**
         * 任务类型
         */
        private String type;
        /**
         * 创建人及其部门
         */
        private String creator;

        /**
         * 创建人id
         */
        private String creatorId;

        /**
         * 创建时间
         */
        private LocalDateTime createTime;
        /**
         * 是否超期
         */
        private boolean isDelay;

        /**
         * 是否催办
         */
        private boolean isUrged;
        /**
         * 预计结束时间
         */
        private LocalDateTime preEndTime;

        /**
         * 任务紧急程度，URGENT：紧急，SERIOUS：严重，NORMAL：一般
         */
        private String taskLevel;
        /**
         * 任务标签
         */
        private String taskLabel;

        /** 任务接收人 */
        private List<ReceiveUser> receiveUsers;

        /**
         * 完成实况
         */
        private TaskExecuteLive executeLive;

        /**
         * 执行人ID
         */
        private String executorId;

        /**
         * 更新频率
         */
        private int updateFrequency;

    }

    @Data
    @Accessors
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskExecuteLive {

        /**
         * 时间状态
         */
        private String status;
        /**
         * 时间差
         */
        private String timeDiff;
        /**
         * 要求完成时间
         */
        private String completeTime;
    }



    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class ReceiveUser {

        /**
         * 用户名称
         */
        private String userId;

        /**
         * 用户姓名
         */
        private String username;

        /**
         * 部门名称
         */
        private String deptName;

        /**
         * 区域名称
         */
        private String regionName;
    }

}
