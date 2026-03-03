package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * <p>
 * 部门移动参数
 * </p>
 * <p>DM-006 移动部门</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
public class OrgDepartmentMoveParam {

    /**
     * 部门id
     */
    @NotBlank(message = "部门id不能为空")
    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 目标父部门id（0表示根部门）
     */
    @NotBlank(message = "目标父部门id不能为空")
    @Schema(description = "目标父部门id，0表示根部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String parentId;

}
