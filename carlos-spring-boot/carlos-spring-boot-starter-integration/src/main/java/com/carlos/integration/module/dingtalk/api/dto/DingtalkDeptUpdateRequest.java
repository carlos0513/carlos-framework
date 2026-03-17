package com.carlos.integration.module.dingtalk.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 更新部门请求
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DingtalkDeptUpdateRequest extends DingtalkBaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID（必填）
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
     * 是否同步创建一个关联此部门的企业群
     */
    @JsonProperty("create_dept_group")
    private Boolean createDeptGroup;

    /**
     * 是否隐藏本部�?
     */
    private Boolean hideDept;

    /**
     * 部门主管列表
     */
    @JsonProperty("dept_manager_userid_list")
    private List<String> deptManagerUseridList;
}

