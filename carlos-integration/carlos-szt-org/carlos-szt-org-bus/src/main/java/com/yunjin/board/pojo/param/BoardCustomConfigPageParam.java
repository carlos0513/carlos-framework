package com.yunjin.board.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 看板自定义配置 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "看板自定义配置列表查询参数", description = "看板自定义配置列表查询参数")
public class BoardCustomConfigPageParam extends ParamPage {
    private static final long serialVersionUID = 4580123072930033484L;
    @Schema(value = "用户id")
    private String configKey;
    @Schema(value = "配置信息")
    private String configJson;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
