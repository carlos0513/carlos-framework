package com.carlos.integration.module.dingtalk.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 获取子部门列表响�?
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingtalkDeptListResponse extends DingtalkBaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 部门列表
     */
    private List<DeptBaseResponse> result;

    /**
     * <p>
     * 部门基础信息
     * </p>
     */
    @Data
    public static class DeptBaseResponse {

        /**
         * 部门ID
         */
        @JsonProperty("dept_id")
        private Long deptId;

        /**
         * 部门名称
         */
        private String name;

        /**
         * 父部门ID
         */
        @JsonProperty("parent_id")
        private Long parentId;

        /**
         * 部门路径
         */
        @JsonProperty("parent_dept_path_list")
        private List<Long> parentDeptPathList;
    }
}

