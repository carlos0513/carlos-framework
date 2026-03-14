package com.carlos.flowable.dto;

import com.carlos.flowable.enums.TaskStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 任务 DTO
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
public class TaskDTO implements Serializable {

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
     * 执行ID
     */
    private String executionId;

    /**
     * 处理人ID
     */
    private String assignee;

    /**
     * 处理人名称
     */
    private String assigneeName;

    /**
     * 任务所有者
     */
    private String owner;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 到期时间
     */
    private Date dueDate;

    /**
     * 认领时间
     */
    private Date claimTime;

    /**
     * 任务状态
     */
    private TaskStatusEnum status;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    /**
     * 候选人列表
     */
    private String[] candidateUsers;

    /**
     * 候选组列表
     */
    private String[] candidateGroups;
}
