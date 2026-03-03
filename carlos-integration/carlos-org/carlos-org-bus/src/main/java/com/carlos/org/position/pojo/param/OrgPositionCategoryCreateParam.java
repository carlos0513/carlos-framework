package com.carlos.org.position.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 岗位类别表（职系） 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Data
@Accessors(chain = true)
@Schema(description = "岗位类别表（职系）新增参数")
public class OrgPositionCategoryCreateParam {
    @NotBlank(message = "类别编码，如：M、T、P、S不能为空")
    @Schema(description = "类别编码，如：M、T、P、S")
    private String categoryCode;
    @NotBlank(message = "类别名称，如：管理系、技术系、专业系、销售系不能为空")
    @Schema(description = "类别名称，如：管理系、技术系、专业系、销售系")
    private String categoryName;
    @NotNull(message = "类别类型：1管理通道，2专业通道，3双通道不能为空")
    @Schema(description = "类别类型：1管理通道，2专业通道，3双通道")
    private Integer categoryType;
    @Schema(description = "类别描述")
    private String description;
    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;
    @NotNull(message = "状态：0禁用，1启用不能为空")
    @Schema(description = "状态：0禁用，1启用")
    private Integer state;
    @Schema(description = "租户ID")
    private Long tenantId;
}
