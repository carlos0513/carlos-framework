package com.yunjin.board.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 工作台卡片选项信息 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "工作台卡片选项信息列表查询参数", description = "工作台卡片选项信息列表查询参数")
public class BoardOptionInfoPageParam extends ParamPage {
    private static final long serialVersionUID = -5440408574583617527L;
    @Schema(value = "按钮名称")
    private String optionId;
    @Schema(value = "选项类型 标签 按钮 超链接")
    private String optionType;
    @Schema(value = "描述")
    private String description;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
