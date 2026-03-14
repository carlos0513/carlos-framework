package com.carlos.flowable.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 任务查询参数
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
public class TaskQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 业务Key
     */
    private String businessKey;

    /**
     * 处理人
     */
    private String assignee;

    /**
     * 候选人
     */
    private String candidateUser;

    /**
     * 候选组列表
     */
    private List<String> candidateGroups;

    /**
     * 分页-页码
     */
    private Integer pageNum = 1;

    /**
     * 分页-每页大小
     */
    private Integer pageSize = 10;
}
