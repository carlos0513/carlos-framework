package com.carlos.flowable.vo;

import com.carlos.flowable.enums.ProcessStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程实例 VO
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
@Schema(description = "流程实例信息")
public class ProcessInstanceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "流程实例ID")
    private String id;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    @Schema(description = "流程定义名称")
    private String processDefinitionName;

    @Schema(description = "流程实例名称")
    private String name;

    @Schema(description = "业务Key")
    private String businessKey;

    @Schema(description = "启动时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "启动人ID")
    private String startUserId;

    @Schema(description = "启动人名称")
    private String startUserName;

    @Schema(description = "流程状态")
    private ProcessStatusEnum status;
}
