package com.yunjin.board.data;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Map;


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
@Schema(value = "看板数据查询参数", description = "看板数据查询参数")
public class BoardDataQueryParam {

    @NotEmpty(message = "指标key不能为空")
    @Schema(value = "指标key")
    private String key;


    @Schema(value = "参数信息")
    private Map<String, Object> param;

}
