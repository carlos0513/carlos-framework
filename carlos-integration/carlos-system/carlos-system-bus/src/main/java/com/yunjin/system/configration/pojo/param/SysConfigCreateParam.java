package com.carlos.system.configration.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "系统配置新增参数", description = "系统配置新增参数")
public class SysConfigCreateParam {

    @ApiModelProperty(value = "参数名称")
    private String configName;
    @ApiModelProperty(value = "参数编码")
    private String configCode;
    @ApiModelProperty(value = "参数键值")
    private String configValue;
    @ApiModelProperty(value = "值类型")
    private String valueType;
    @ApiModelProperty(value = "状态")
    private Boolean state;

}
