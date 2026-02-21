package com.carlos.system.configration.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统配置 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:55
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "参数Id")
    private String id;
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
    @ApiModelProperty(value = "备注")
    private String remark;
}
