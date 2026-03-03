package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 部门 新增参数封装
 * </p>
 * <p>DM-003 新增部门</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "部门新增参数")
public class OrgDepartmentCreateParam {

    /**
     * 父id，不传或传0表示根部门
     */
    @Schema(description = "父id，不传或传0表示根部门")
    private Long parentId;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
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
     * 排序，不传默认为0
     */
    @Schema(description = "排序，不传默认为0")
    private Integer sort;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String description;

}
