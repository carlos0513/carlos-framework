package com.carlos.message.channel;

import lombok.Data;

import java.time.Duration;

/**
 * <p>
 * 短信渠道配置
 * </p>
 *
 * @author Carlos
 * @date 2025-05-06 23:58
 */
@Data
public class DingtalkChannelConfig implements com.carlos.message.channel.ChannelConfig {
    /**
     * 钉钉服务地址
     */
    private String host;
    /**
     * 应用的唯一标识key
     */
    private String appkey;
    /**
     * 应用的密钥
     */
    private String appsecret;
    /**
     * agentId
     */
    private String agentId;
    /**
     * 企业id
     */
    private String corpId;
    /**
     * AccessToken过期时间 默认两小时
     */
    private Duration tokenDuration = Duration.ofHours(2L);
}
