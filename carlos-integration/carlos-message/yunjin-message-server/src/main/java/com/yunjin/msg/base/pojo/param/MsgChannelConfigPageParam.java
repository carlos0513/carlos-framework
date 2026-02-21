package com.carlos.msg.base.pojo.param;


import com.carlos.core.param.ParamPage;
import com.carlos.msg.api.pojo.enums.ChannelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 消息渠道配置 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "消息渠道配置列表查询参数", description = "消息渠道配置列表查询参数")
public class MsgChannelConfigPageParam extends ParamPage {
    @ApiModelProperty(value = "渠道类型")
    private ChannelType channelType;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "样例配置信息")
    private String channelConfig;
    @ApiModelProperty(value = "备注信息")
    private String remark;
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
    @ApiModelProperty("开始时间")
    private LocalDateTime start;

    @ApiModelProperty("结束时间")
    private LocalDateTime end;
}
