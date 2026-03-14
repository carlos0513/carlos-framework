package com.carlos.flowable.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 启动流程参数
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
public class StartProcessParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 业务Key
     */
    private String businessKey;

    /**
     * 流程实例名称
     */
    private String processInstanceName;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    /**
     * 启动人ID
     */
    private String startUserId;
}
