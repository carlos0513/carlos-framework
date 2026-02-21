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
 * 看板自定义配置 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardCustomConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 卡片名称
     */
    @Schema(value = "卡片名称")
    private String cardName;
    /**
     * 组件名称
     */
    @Schema(value = "组件名称")
    private String component;
    /**
     * 缩略图
     */
    @Schema(value = "缩略图")
    private String thumbnail;
    /**
     * 描述
     */
    @Schema(value = "描述")
    private String description;
    /**
     * 序号
     */
    @Schema(value = "序号")
    private Integer sort;

    /**
     * 移动端序号
     */
    @Schema(value = "移动端序号")
    private Integer sortForMobile;
    /**
     * 是否显示
     */
    @Schema(value = "是否显示")
    private Boolean display;

    /**
     * 移动端是否显示
     */
    @Schema(value = "移动端是否显示")
    private Boolean displayForMobile;
    /**
     * 位置
     */
    @Schema(value = "位置")
    private CardPosition position;
    /**
     * 选项配置
     */
    @Schema(value = "选项配置")
    Set<String> options;

}
