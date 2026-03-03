package com.carlos.org.position.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 岗位类别表（职系） 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Data
@Accessors(chain = true)
@Schema(description = "岗位类别表（职系）修改参数")
public class OrgPositionCategoryUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "类别编码，如：M、T、P、S")
    private String categoryCode;
    @Schema(description = "类别名称，如：管理系、技术系、专业系、销售系")
    private String categoryName;
    @Schema(description = "类别类型：1管理通道，2专业通道，3双通道")
    private Integer categoryType;
    @Schema(description = "类别描述")
    private String description;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "状态：0禁用，1启用")
    private Integer state;
    @Schema(description = "租户ID")
    private Long tenantId;
}
