package com.carlos.flowable.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程定义 VO
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
@Schema(description = "流程定义信息")
public class ProcessDefinitionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "流程定义ID")
    private String id;

    @Schema(description = "流程定义Key")
    private String key;

    @Schema(description = "流程定义名称")
    private String name;

    @Schema(description = "流程定义描述")
    private String description;

    @Schema(description = "流程定义版本")
    private Integer version;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "部署ID")
    private String deploymentId;

    @Schema(description = "是否挂起")
    private Boolean suspended;

    @Schema(description = "部署时间")
    private Date deploymentTime;
}
