package com.carlos.system.configration.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统配置 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:55
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "系统配置修改参数", description = "系统配置修改参数")
public class SysConfigUpdateParam {

    @NotNull(message = "参数Id不能为空")
    @ApiModelProperty(value = "参数Id")
    private String id;
    @ApiModelProperty(value = "参数名称")
    private String configName;
    @ApiModelProperty(value = "参数键值")
    private String configValue;
    @ApiModelProperty(value = "值类型")
    private String valueType;
    @ApiModelProperty(value = "状态")
    private Boolean state;
    @ApiModelProperty(value = "备注")
    private String remark;
}
