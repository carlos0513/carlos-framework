package com.carlos.msg.api.pojo.ao;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息渠道配置 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
public class MsgChannelConfigAO implements Serializable {
    /** 主键ID */
    private Long id;
    /** 渠道编码 */
    private String channelCode;
    /** 渠道名称 */
    private String channelName;
    /** 样例配置信息 */
    private String channelConfig;
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
