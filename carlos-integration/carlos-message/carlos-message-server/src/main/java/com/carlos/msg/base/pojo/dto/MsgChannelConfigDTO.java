package com.carlos.msg.base.pojo.dto;


import com.carlos.message.channel.ChannelConfig;
import com.carlos.msg.api.pojo.enums.ChannelType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息渠道配置 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
public class MsgChannelConfigDTO {
    /** 主键ID */
    private Long id;
    /** 渠道编码 */
    private ChannelType channelType;
    /** 渠道名称 */
    private String channelName;
    /** 样例配置信息 */
    private ChannelConfig channelConfig;
    /** 备注信息 */
    private String remark;
    /** 是否启用 */
    private Boolean enabled;
    /** 创建者编号 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新者编号 */
    private String updateBy;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
