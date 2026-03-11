package com.carlos.message.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息接收人实体
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("message_receiver")
public class MessageReceiver implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 渠道编码
     */
    private String channelCode;

    /**
     * 接收者ID
     */
    private String receiverId;

    /**
     * 接收者类型: 1-用户 2-部门 3-角色
     */
    private Integer receiverType;

    /**
     * 接收地址
     */
    private String receiverNumber;

    /**
     * 接收者设备标识
     */
    private String receiverAudience;

    /**
     * 渠道返回的消息ID
     */
    private String channelMessageId;

    /**
     * 状态: 0-待发送 1-发送中 2-已发送 3-送达 4-已读 5-失败 6-撤回
     */
    private Integer status;

    /**
     * 失败次数
     */
    private Integer failCount;

    /**
     * 最后一次失败原因
     */
    private String failReason;

    /**
     * 定时发送时间
     */
    private LocalDateTime scheduleTime;

    /**
     * 实际发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 渠道返回的送达时间
     */
    private LocalDateTime deliverTime;

    /**
     * 用户阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 状态回调URL
     */
    private String callbackUrl;

    /**
     * 扩展信息
     */
    private String extras;

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
