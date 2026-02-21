package com.yunjin.board.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 工作台卡片信息 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@Schema(value = "工作台卡片信息新增参数", description = "工作台卡片信息新增参数")
public class BoardCardInfoCreateParam {
    @NotBlank(message = "卡片名称不能为空")
    @Schema(value = "卡片名称")
    private String cardName;
    @NotBlank(message = "卡片编码不能为空")
    @Schema(value = "卡片编码")
    private String cardCode;
    @NotBlank(message = "组件名称不能为空")
    @Schema(value = "组件名称")
    private String component;
    @NotBlank(message = "缩略图不能为空")
    @Schema(value = "缩略图")
    private String thumbnail;
    @Schema(value = "描述")
    private String description;
}
