package com.carlos.msg.base.pojo.vo;

import com.carlos.json.jackson.annotation.EnumField;
import com.carlos.msg.api.pojo.enums.ChannelType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息渠道配置 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgChannelConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @EnumField(type = EnumField.SerializerType.DESC)
    @ApiModelProperty(value = "渠道类型")
    private ChannelType channelType;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "备注信息")
    private String remark;
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
    @ApiModelProperty(value = "创建者编号")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新者编号")
    private String updateBy;
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
