package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门 更新参数封装
 * </p>
 * <p>DM-004 编辑部门</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "部门修改参数")
public class OrgDepartmentUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    /**
     * 父id
     */
    @Schema(description = "父id")
    private Long parentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    /**
     * 部门编号
     */
    @Schema(description = "部门编号")
    private String deptCode;

    /**
     * 负责人id
     */
    @Schema(description = "负责人id")
    private Long leaderId;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String description;

}
