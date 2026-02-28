package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "部门修改参数")
public class OrgDepartmentUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "父id")
    private Long parentId;
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "部门编号")
    private String deptCode;
    @Schema(description = "部门路径")
    private String path;
    @Schema(description = "负责人id")
    private Long leaderId;
    @Schema(description = "状态，0：禁用，1：启用")
    private Integer state;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "层级")
    private Integer level;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "租户id")
    private Long tenantId;
}
