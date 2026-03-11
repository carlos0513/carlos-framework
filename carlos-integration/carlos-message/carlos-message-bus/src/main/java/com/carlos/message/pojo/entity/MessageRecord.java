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
 * 消息记录实体
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("message_record")
public class MessageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消息唯一标识
     */
    private String messageId;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板参数JSON
     */
    private String templateParams;

    /**
     * 消息类型编码
     */
    private String messageType;

    /**
     * 消息标题（渲染后）
     */
    private String messageTitle;

    /**
     * 消息内容（渲染后）
     */
    private String messageContent;

    /**
     * 发送人ID
     */
    private String senderId;

    /**
     * 发送人名称
     */
    private String senderName;

    /**
     * 发送系统标识
     */
    private String senderSystem;

    /**
     * 操作反馈类型
     */
    private String feedbackType;

    /**
     * 操作反馈内容
     */
    private String feedbackContent;

    /**
     * 优先级: 1-紧急 2-高 3-普通 4-低
     */
    private Integer priority;

    /**
     * 消息有效期
     */
    private LocalDateTime validUntil;

    /**
     * 总接收人数
     */
    private Integer totalCount;

    /**
     * 成功发送数
     */
    private Integer successCount;

    /**
     * 失败发送数
     */
    private Integer failCount;

    /**
     * 扩展信息JSON
     */
    private String extras;

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
