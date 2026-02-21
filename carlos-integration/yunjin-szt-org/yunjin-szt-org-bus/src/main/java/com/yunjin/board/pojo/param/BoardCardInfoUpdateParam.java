package com.yunjin.board.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 工作台卡片信息 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@Schema(value = "工作台卡片信息修改参数", description = "工作台卡片信息修改参数")
public class BoardCardInfoUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(value = "主键")
    private Long id;
    @Schema(value = "卡片名称")
    private String cardName;
    @Schema(value = "卡片编码")
    private String cardCode;
    @Schema(value = "组件名称")
    private String component;
    @Schema(value = "缩略图")
    private String thumbnail;
    @Schema(value = "描述")
    private String description;
}
