package com.carlos.flowable.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 转办任务参数
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
public class TransferTaskParam implements Serializable {

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
     * 转办人ID
     */
    private String transferToUserId;

    /**
     * 转办原因
     */
    private String reason;
}
