package com.carlos.msg.base.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;

/**
 * <p>
 * 消息类型 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "消息类型修改参数", description = "消息类型修改参数")
public class MsgMessageTypeUpdateParam {
    @NotNull(message = "主键ID不能为空")
    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    @ApiModelProperty(value = "类型名称")
    private String typeName;
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
}
