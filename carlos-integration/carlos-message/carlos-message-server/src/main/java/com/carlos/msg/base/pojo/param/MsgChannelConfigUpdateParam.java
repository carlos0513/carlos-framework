package com.carlos.msg.base.pojo.param;


import com.carlos.message.channel.ChannelConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Schema(description = "消息渠道配置修改参数")
public class MsgChannelConfigUpdateParam {
    @NotNull(message = "主键ID不能为空")
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "渠道名称")
    private String channelName;
    @Schema(description = "样例配置信息")
    private ChannelConfig channelConfig;
    @Schema(description = "备注信息")
    private String remark;
    @Schema(description = "是否启用")
    private Boolean enabled;
}
