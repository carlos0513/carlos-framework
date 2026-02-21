package com.carlos.system.pojo.ao;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class SysConfigAO implements Serializable {

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
}
