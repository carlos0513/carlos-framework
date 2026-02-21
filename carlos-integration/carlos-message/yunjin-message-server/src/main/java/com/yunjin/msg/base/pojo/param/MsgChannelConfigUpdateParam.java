package com.carlos.msg.base.pojo.param;


import com.carlos.message.channel.ChannelConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 消息渠道配置 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "消息渠道配置修改参数", description = "消息渠道配置修改参数")
public class MsgChannelConfigUpdateParam {
    @NotNull(message = "主键ID不能为空")
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "样例配置信息")
    private ChannelConfig channelConfig;
    @ApiModelProperty(value = "备注信息")
    private String remark;
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
}
