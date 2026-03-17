package com.carlos.integration.module.dingtalk.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 获取部门用户列表响应
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingtalkUserListResponse extends DingtalkBaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 分页结果
     */
    private PageResult result;

    /**
     * <p>
     * 分页结果
     * </p>
     */
    @Data
    public static class PageResult {

        /**
         * 是否还有更多数据
         */
        @JsonProperty("has_more")
        private Boolean hasMore;

        /**
         * 下一次游�?
         */
        @JsonProperty("next_cursor")
        private Long nextCursor;

        /**
         * 用户列表
         */
        private List<DingtalkUserResponse.DingtalkUser> list;
    }
}

