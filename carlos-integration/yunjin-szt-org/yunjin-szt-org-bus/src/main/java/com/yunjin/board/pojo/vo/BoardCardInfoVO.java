package com.yunjin.board.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.board.pojo.enums.CardPosition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 工作台卡片信息 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardCardInfoVO implements Serializable {


    private static final long serialVersionUID = 1L;

    @Schema(value = "卡片名称")
    private String cardName;
    @Schema(value = "组件名称")
    private String component;
    @Schema(value = "缩略图")
    private String thumbnail;
    @Schema(value = "描述")
    private String description;
    @Schema(value = "序号")
    private Integer sort;
    @Schema(value = "是否显示")
    private Boolean display;
    @Schema(value = "位置")
    private CardPosition position;
    @Schema(value = "选项配置")
    Set<String> options;
}
