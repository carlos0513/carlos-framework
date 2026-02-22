package com.carlos.msg.base.pojo.param;


import com.carlos.message.channel.ChannelConfig;
import com.carlos.msg.api.pojo.enums.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 消息渠道配置 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息渠道配置新增参数")
public class MsgChannelConfigCreateParam {
    @NotBlank(message = "渠道类型不能为空")
    @Schema(description = "渠道类型")
    private ChannelType channelType;
    @NotBlank(message = "渠道名称不能为空")
    @Schema(description = "渠道名称")
    private String channelName;
    @NotBlank(message = "样例配置信息不能为空")
    @Schema(description = "样例配置信息")
    private ChannelConfig channelConfig;
    @NotBlank(message = "备注信息不能为空")
    @Schema(description = "备注信息")
    private String remark;
    @NotNull(message = "是否启用不能为空")
    @Schema(description = "是否启用")
    private Boolean enabled;
}
