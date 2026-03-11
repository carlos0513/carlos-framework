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
 * 消息模板实体
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("message_template")
public class MessageTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long typeId;

    private String templateCode;

    private String templateName;

    private String titleTemplate;

    private String contentTemplate;

    private String paramSchema;

    private String channelConfig;

    private Integer isEnabled;

    @TableLogic
    private Integer isDeleted;

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;
}
