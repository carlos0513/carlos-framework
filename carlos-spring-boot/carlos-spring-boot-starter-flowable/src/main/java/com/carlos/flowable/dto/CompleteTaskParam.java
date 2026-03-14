package com.carlos.flowable.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 完成任务参数
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
public class CompleteTaskParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 处理人ID
     */
    private String assignee;

    /**
     * 处理意见
     */
    private String comment;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    /**
     * 局部变量（仅当前任务有效）
     */
    private Map<String, Object> transientVariables;

    /**
     * 下一个节点处理人
     */
    private String nextAssignee;
}
