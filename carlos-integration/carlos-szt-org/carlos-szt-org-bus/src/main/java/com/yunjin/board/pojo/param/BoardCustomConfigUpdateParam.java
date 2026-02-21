package com.yunjin.board.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 看板自定义配置 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@Schema(value = "看板自定义配置修改参数", description = "看板自定义配置修改参数")
public class BoardCustomConfigUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(value = "主键")
    private Long id;
    @Schema(value = "用户id")
    private String configKey;
    @Schema(value = "配置信息")
    private String configJson;
}
