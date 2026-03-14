package com.carlos.flowable.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 历史任务 DTO
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
public class HistoricTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务Key
     */
    private String taskDefinitionKey;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    /**
     * 处理人ID
     */
    private String assignee;

    /**
     * 处理人名称
     */
    private String assigneeName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 持续时间（毫秒）
     */
    private Long durationInMillis;

    /**
     * 删除原因
     */
    private String deleteReason;

    /**
     * 处理意见
     */
    private String comment;

    /**
     * 处理结果
     */
    private String outcome;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;
}
