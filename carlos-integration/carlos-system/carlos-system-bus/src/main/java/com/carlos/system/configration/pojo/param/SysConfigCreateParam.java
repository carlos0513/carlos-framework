package com.carlos.system.configration.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 系统配置 新增参数封装
 * </p>
 *
 * @author carlos
 * @date 2022-11-3 13:47:54
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统配置新增参数")
public class SysConfigCreateParam {

    @Schema(description = "参数名称")
    private String configName;
    @Schema(description = "参数编码")
    private String configCode;
    @Schema(description = "参数键值")
    private String configValue;
    @Schema(description = "值类型")
    private String valueType;
    @Schema(description = "状态")
    private Boolean state;

}
