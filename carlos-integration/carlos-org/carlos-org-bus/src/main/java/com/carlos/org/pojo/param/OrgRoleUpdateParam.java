package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统角色 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统角色修改参数")
public class OrgRoleUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "角色唯一编码")
    private String roleCode;
    @Schema(description = "角色类型， 1：系统角色, 2: 自定义角色")
    private Integer roleType;
    @Schema(description = "数据范围， 1：全部, 2: 本部及子部门, 3:仅本部门, 4:仅本人, 5:自定义规则")
    private Integer dataScope;
    @Schema(description = "角色状态， 0：禁用, 1: 启用")
    private Integer state;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "租户id")
    private Long tenantId;
}
