package com.carlos.system.configration.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 系统配置 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:54
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统配置新增参数", description = "系统配置新增参数")
public class SysConfigCreateParam {

    @Schema(value = "参数名称")
    private String configName;
    @Schema(value = "参数编码")
    private String configCode;
    @Schema(value = "参数键值")
    private String configValue;
    @Schema(value = "值类型")
    private String valueType;
    @Schema(value = "状态")
    private Boolean state;

}
