package com.carlos.message.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * @date 2026年3月12日 上午11:17:05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息渠道配置列表查询参数")
public class MessageChannelPageParam extends ParamPage {
    @Schema(description = "渠道编码")
    private String channelCode;
    @Schema(description = "渠道名称")
    private String channelName;
    @Schema(description = "渠道类型: 1-短信 2-邮件 3-钉钉 4-企业微信 5-站内信")
    private Integer channelType;
    @Schema(description = "服务商编码")
    private String provider;
    @Schema(description = "渠道配置JSON")
    private String channelConfig;
    @Schema(description = "每秒最大请求数")
    private Integer rateLimitQps;
    @Schema(description = "突发流量限制")
    private Integer rateLimitBurst;
    @Schema(description = "最大重试次数")
    private Integer retryTimes;
    @Schema(description = "重试间隔(ms)")
    private Integer retryInterval;
    @Schema(description = "权重")
    private Integer weight;
    @Schema(description = "是否启用: 0-禁用 1-启用 2-故障")
    private Boolean enabled;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
