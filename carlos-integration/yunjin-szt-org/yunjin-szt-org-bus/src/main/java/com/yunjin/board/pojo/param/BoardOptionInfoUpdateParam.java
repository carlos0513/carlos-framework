package com.yunjin.board.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 工作台卡片选项信息 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@Schema(value = "工作台卡片选项信息修改参数", description = "工作台卡片选项信息修改参数")
public class BoardOptionInfoUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(value = "主键")
    private Long id;
    @Schema(value = "按钮名称")
    private String optionId;
    @Schema(value = "选项类型 标签 按钮 超链接")
    private String optionType;
    @Schema(value = "描述")
    private String description;
}
