package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色 更新参数封装
 * </p>
 * <p>RM-004 编辑角色</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "角色修改参数")
public class OrgRoleUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 数据范围，1：全部, 2: 本部及子部门, 3:仅本部门, 4:仅本人, 5:自定义规则
     */
    @Schema(description = "数据范围，1：全部, 2: 本部及子部门, 3:仅本部门, 4:仅本人, 5:自定义规则")
    private Integer dataScope;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String description;

}
