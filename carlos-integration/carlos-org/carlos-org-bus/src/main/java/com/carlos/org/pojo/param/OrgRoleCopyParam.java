package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 角色复制参数
 * </p>
 * <p>RM-010 复制角色</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
public class OrgRoleCopyParam {

    /**
     * 源角色id
     */
    @NotNull(message = "源角色id不能为空")
    @Schema(description = "源角色id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Serializable sourceRoleId;

    /**
     * 新角色名称
     */
    @NotBlank(message = "新角色名称不能为空")
    @Schema(description = "新角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newRoleName;

    /**
     * 新角色编码
     */
    @Schema(description = "新角色编码")
    private String newRoleCode;

}
