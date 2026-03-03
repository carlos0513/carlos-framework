package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 角色 新增参数封装
 * </p>
 * <p>RM-003 新增角色</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "角色新增参数")
public class OrgRoleCreateParam {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
    private String roleCode;

    /**
     * 数据范围，1：全部, 2: 本部及子部门, 3:仅本部门, 4:仅本人, 5:自定义规则
     */
    @NotNull(message = "数据范围不能为空")
    @Schema(description = "数据范围，1：全部, 2: 本部及子部门, 3:仅本部门, 4:仅本人, 5:自定义规则", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer dataScope;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String description;

}
