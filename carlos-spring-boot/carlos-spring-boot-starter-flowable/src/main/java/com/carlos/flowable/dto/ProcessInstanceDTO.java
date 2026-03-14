package com.carlos.flowable.dto;

import com.carlos.flowable.enums.ProcessStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 流程实例 DTO
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
public class ProcessInstanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程实例ID
     */
    private String id;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 流程实例名称
     */
    private String name;

    /**
     * 业务Key
     */
    private String businessKey;

    /**
     * 启动时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 启动人ID
     */
    private String startUserId;

    /**
     * 启动人名称
     */
    private String startUserName;

    /**
     * 流程状态
     */
    private ProcessStatusEnum status;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;
}
