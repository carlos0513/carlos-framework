package com.carlos.flowable.vo;

import com.carlos.flowable.enums.TaskStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务 VO
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
@Schema(description = "任务信息")
public class TaskVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "任务ID")
    private String id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "任务描述")
    private String description;

    @Schema(description = "任务Key")
    private String taskDefinitionKey;

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "流程定义名称")
    private String processDefinitionName;

    @Schema(description = "处理人ID")
    private String assignee;

    @Schema(description = "处理人名称")
    private String assigneeName;

    @Schema(description = "任务所有者")
    private String owner;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "到期时间")
    private Date dueDate;

    @Schema(description = "认领时间")
    private Date claimTime;

    @Schema(description = "任务状态")
    private TaskStatusEnum status;

    @Schema(description = "业务Key")
    private String businessKey;
}
