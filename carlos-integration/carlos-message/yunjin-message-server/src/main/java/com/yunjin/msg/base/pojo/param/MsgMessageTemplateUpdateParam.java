package com.carlos.msg.base.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;

/**
 * <p>
 * 消息模板 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "消息模板修改参数", description = "消息模板修改参数")
public class MsgMessageTemplateUpdateParam {
    @NotNull(message = "主键ID不能为空")
    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "消息类型")
    private Long typeId;
    @ApiModelProperty(value = "模板内容(含变量占位符)")
    private String templateContent;
    @ApiModelProperty(value = "渠道特殊配置(如短信模板ID),配置对应渠道编码")
    private String channelConfig;
    @ApiModelProperty(value = "是否启用")
    private Boolean active;
}
