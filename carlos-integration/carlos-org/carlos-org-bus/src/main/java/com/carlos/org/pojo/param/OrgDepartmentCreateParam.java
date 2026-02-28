package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 部门 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "部门新增参数")
public class OrgDepartmentCreateParam {
    @Schema(description = "父id")
    private Long parentId;
    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "部门编号")
    private String deptCode;
    @NotBlank(message = "部门路径不能为空")
    @Schema(description = "部门路径")
    private String path;
    @NotNull(message = "负责人id不能为空")
    @Schema(description = "负责人id")
    private Long leaderId;
    @NotNull(message = "状态，0：禁用，1：启用不能为空")
    @Schema(description = "状态，0：禁用，1：启用")
    private Integer state;
    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;
    @NotNull(message = "层级不能为空")
    @Schema(description = "层级")
    private Integer level;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "租户id")
    private Long tenantId;
}
