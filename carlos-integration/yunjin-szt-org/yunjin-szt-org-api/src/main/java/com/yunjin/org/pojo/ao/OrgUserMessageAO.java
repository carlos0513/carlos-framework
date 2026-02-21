package com.yunjin.org.pojo.ao;


import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户消息表 API提供的对象(API Object)
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@Data
@Accessors(chain = true)
public class OrgUserMessageAO implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 消息id
     */
    private String messageId;
    /**
     * 消息类型
     */
    private UserMessageType type;
    /**
     * 标题
     */
    private String title;
    /**
     * 读取状态
     */
    private UserMessageStatus status;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建人姓名
     */
    private String creatorName;
    /**
     * 发布日期
     */
    private LocalDateTime sendDate;
    /**
     * 内容
     */
    private String content;
    /**
     * 部门Code
     */
    private String deptCode;
}
