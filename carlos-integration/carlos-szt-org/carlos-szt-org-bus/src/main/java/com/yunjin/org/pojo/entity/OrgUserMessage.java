package com.yunjin.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.enums.SmsMessageEnum;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
    import java.time.LocalDateTime;
import java.lang.String;

/**
* <p>
    * 用户消息表 数据源对象
    * </p>
*
* @author  yunjin
* @date    2024-2-28 17:39:16
*/
@Data
@Accessors(chain = true)
@TableName("org_user_message")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgUserMessage implements Serializable{
private static final long serialVersionUID = 1L;
    /**
    * 主键
    */
        @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
    * 用户id
    */
        @TableField(value = "user_id")
    private String userId;
    /**
    * 消息id
    */
        @TableField(value = "message_id")
    private String messageId;
    /**
    * 消息类型
    */
        @TableField(value = "type")
    private UserMessageType type;
    /**
    * 标题
    */
        @TableField(value = "title")
    private String title;
    /**
    * 读取状态
    */
        @TableField(value = "status")
    private UserMessageStatus status;
    /**
    * 创建人
    */
        @TableField(value = "creator")
    private String creator;
    /**
    * 发布日期
    */
        @TableField(value = "send_date")
    private LocalDateTime sendDate;
    /**
    * 内容
    */
        @TableField(value = "content")
    private String content;

    /**
     * 业务类型
     */
    @TableField(value = "dept_code")
    private String deptCode;
    @TableField(value = "sms_type")
    private SmsMessageEnum smsMessageEnum;
}
