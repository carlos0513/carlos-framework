package com.carlos.integration.module.dingtalk.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 发送工作通知消息响应
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingtalkMessageResponse extends DingtalkBaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 发送结�?
     */
    private AsyncSendMessageResponse result;

    /**
     * <p>
     * 异步发送消息结�?
     * </p>
     */
    @Data
    public static class AsyncSendMessageResponse {

        /**
         * 消息任务ID
         */
        @JsonProperty("task_id")
        private Long taskId;

        /**
         * 无效的用户ID列表
         */
        @JsonProperty("invalid_userid_list")
        private String invalidUseridList;

        /**
         * 无效的部门ID列表
         */
        @JsonProperty("invalid_dept_id_list")
        private String invalidDeptIdList;

        /**
         * 因发送消息过于频繁或超量而被流控过滤后，实际未发送的部门ID列表
         */
        @JsonProperty("forbidden_list")
        private String forbiddenList;

        /**
         * 被禁止发送消息的用户ID列表
         */
        @JsonProperty("forbidden_user_id_list")
        private String forbiddenUserIdList;
    }
}

