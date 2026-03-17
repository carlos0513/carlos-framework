package com.carlos.integration.module.dingtalk.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 更新用户请求
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DingtalkUserUpdateRequest extends DingtalkBaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（必填）
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
}

