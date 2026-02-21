package com.yunjin.board.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 工作台卡片选项信息 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@Schema(value = "工作台卡片选项信息新增参数", description = "工作台卡片选项信息新增参数")
public class BoardOptionInfoCreateParam {
    @NotBlank(message = "按钮名称不能为空")
    @Schema(value = "按钮名称")
    private String optionId;
    @NotBlank(message = "选项类型 标签 按钮 超链接不能为空")
    @Schema(value = "选项类型 标签 按钮 超链接")
    private String optionType;
    @NotBlank(message = "描述不能为空")
    @Schema(value = "描述")
    private String description;
}
