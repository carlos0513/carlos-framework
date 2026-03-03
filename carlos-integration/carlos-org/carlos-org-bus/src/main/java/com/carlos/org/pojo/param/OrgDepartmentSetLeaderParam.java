package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * <p>
 * 设置部门负责人参数
 * </p>
 * <p>DM-009 设置部门负责人</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
public class OrgDepartmentSetLeaderParam {

    /**
     * 部门id
     */
    @NotBlank(message = "部门id不能为空")
    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptId;

    /**
     * 负责人id
     */
    @NotBlank(message = "负责人id不能为空")
    @Schema(description = "负责人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String leaderId;

}
