package com.carlos.org.position.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * <p>
 * 职级表 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Data
@Accessors(chain = true)
@Schema(description = "职级表新增参数")
public class OrgPositionLevelCreateParam {
    @NotNull(message = "所属职系ID不能为空")
    @Schema(description = "所属职系ID")
    private Long categoryId;
    @NotBlank(message = "职级编码，如：T1、T2-1、M3不能为空")
    @Schema(description = "职级编码，如：T1、T2-1、M3")
    private String levelCode;
    @NotBlank(message = "职级名称，如：初级工程师、高级工程师、部门经理不能为空")
    @Schema(description = "职级名称，如：初级工程师、高级工程师、部门经理")
    private String levelName;
    @NotNull(message = "职级序列号，用于排序和比较，如：1、2、3不能为空")
    @Schema(description = "职级序列号，用于排序和比较，如：1、2、3")
    private Integer levelSeq;
    @Schema(description = "职级分组：初级、中级、高级、专家、资深专家")
    private String levelGroup;
    @Schema(description = "薪资范围下限")
    private BigDecimal minSalary;
    @Schema(description = "薪资范围上限")
    private BigDecimal maxSalary;
    @Schema(description = "股权激励等级：0无，1部分，2全额")
    private Integer stockLevel;
    @Schema(description = "职级描述")
    private String description;
    @Schema(description = "晋升要求（JSON格式）")
    private String requirements;
    @NotNull(message = "状态：0禁用，1启用不能为空")
    @Schema(description = "状态：0禁用，1启用")
    private Integer state;
    @Schema(description = "租户ID")
    private Long tenantId;
}
