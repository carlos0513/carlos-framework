package com.carlos.message.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息渠道实体
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("message_channel")
public class MessageChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 渠道编码
     */
    private String channelCode;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 渠道类型: 1-短信 2-邮件 3-钉钉 4-企业微信 5-站内信
     */
    private Integer channelType;

    /**
     * 服务商编码
     */
    private String provider;

    /**
     * 渠道配置JSON
     */
    private String channelConfig;

    /**
     * 每秒最大请求数
     */
    private Integer rateLimitQps;

    /**
     * 突发流量限制
     */
    private Integer rateLimitBurst;

    /**
     * 最大重试次数
     */
    private Integer retryTimes;

    /**
     * 重试间隔（毫秒）
     */
    private Integer retryInterval;

    /**
     * 负载均衡权重
     */
    private Integer weight;

    /**
     * 是否启用: 0-禁用 1-启用 2-故障
     */
    private Integer isEnabled;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
