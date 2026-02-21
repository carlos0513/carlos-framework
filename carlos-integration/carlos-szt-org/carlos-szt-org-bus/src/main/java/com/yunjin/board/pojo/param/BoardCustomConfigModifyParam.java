package com.yunjin.board.pojo.param;


import com.yunjin.board.pojo.enums.CardPosition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 看板自定义配置 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@Schema(value = "看板自定义配置新增参数", description = "看板自定义配置新增参数")
public class BoardCustomConfigModifyParam {

    @NotEmpty(message = "组件配置不能为空")
    @Schema(value = "组件配置")
    private List<Item> items;

    @Data
    @Accessors(chain = true)
    public static class Item {
        @NotBlank(message = "组件不能为空")
        @Schema(value = "组件")
        private String component;
        @NotBlank(message = "序号不能为空")
        @Schema(value = "序号")
        private Integer sort;
        @Schema(value = "位置")
        private CardPosition position;
        @NotBlank(message = "显示配置不能为空")
        @Schema(value = "是否显示")
        private Boolean display;
        @Schema(value = "选项配置")
        Set<String> options;
    }
}
