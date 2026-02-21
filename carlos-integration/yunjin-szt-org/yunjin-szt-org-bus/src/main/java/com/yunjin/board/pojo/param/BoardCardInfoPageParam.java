package com.yunjin.board.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 工作台卡片信息 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "工作台卡片信息列表查询参数", description = "工作台卡片信息列表查询参数")
public class BoardCardInfoPageParam extends ParamPage {
    private static final long serialVersionUID = -5901609436377526265L;
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
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
