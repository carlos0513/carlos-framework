package com.carlos.flowable.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 委托任务参数
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
public class DelegateTaskParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 当前处理人
     */
    private String assignee;

    /**
     * 被委托人ID
     */
    private String delegateToUserId;

    /**
     * 委托原因
     */
    private String reason;
}
