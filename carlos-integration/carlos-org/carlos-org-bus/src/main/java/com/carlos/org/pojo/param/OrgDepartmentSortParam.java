package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * <p>
 * 部门排序参数
 * </p>
 * <p>DM-007 部门排序</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
public class OrgDepartmentSortParam {

    /**
     * 部门id
     */
    @NotBlank(message = "部门id不能为空")
    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 排序值
     */
    @NotNull(message = "排序值不能为空")
    @Schema(description = "排序值", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sort;

}
