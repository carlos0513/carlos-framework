package com.carlos.integration.module.dingtalk.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 钉钉用户查询响应
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingtalkUserResponse extends DingtalkBaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 用户信息
     */
    private DingtalkUser result;

    /**
     * <p>
     * 钉钉用户信息
     * </p>
     */
    @Data
    public static class DingtalkUser {

        /**
         * 用户ID
         */
        private String userid;

        /**
         * 姓名
         */
        private String name;

        /**
         * 手机�?
         */
        private String mobile;

        /**
         * 部门ID列表
         */
        @JsonProperty("dept_id_list")
        private List<Long> deptIdList;

        /**
         * 职位
         */
        private String title;

        /**
         * 邮箱
         */
        private String email;

        /**
         * 头像URL
         */
        private String avatar;

        /**
         * 是否管理�?
         */
        @JsonProperty("admin")
        private Boolean isAdmin;

        /**
         * 是否老板
         */
        @JsonProperty("boss")
        private Boolean isBoss;
    }
}

