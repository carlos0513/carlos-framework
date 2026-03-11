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

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String channelCode;

    private String channelName;

    private Integer channelType;

    private String provider;

    private String channelConfig;

    private Integer rateLimitQps;

    private Integer rateLimitBurst;

    private Integer retryTimes;

    private Integer retryInterval;

    private Integer weight;

    private Integer isEnabled;

    @TableLogic
    private Integer isDeleted;

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;
}
