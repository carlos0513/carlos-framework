package com.carlos.flowable.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史活动 VO
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
@Schema(description = "历史活动记录")
public class HistoricActivityVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "活动ID")
    private String id;

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "活动类型")
    private String activityType;

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "执行ID")
    private String executionId;

    @Schema(description = "任务ID")
    private String taskId;

    @Schema(description = "处理人")
    private String assignee;

    @Schema(description = "处理人名称")
    private String assigneeName;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "持续时间（毫秒）")
    private Long durationInMillis;

    @Schema(description = "处理意见")
    private String comment;
}
