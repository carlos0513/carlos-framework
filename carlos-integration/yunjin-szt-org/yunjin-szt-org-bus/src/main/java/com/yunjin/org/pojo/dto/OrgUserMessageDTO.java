package com.yunjin.org.pojo.dto;


import com.yunjin.org.enums.SmsMessageEnum;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import com.yunjin.system.pojo.ao.ImageAO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户消息表 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@Data
@Accessors(chain = true)
public class OrgUserMessageDTO {
    /** 主键 */
    private String id;
    /** 用户id */
    private String userId;
    /** 消息id */
    private String messageId;
    /** 消息类型 */
    private UserMessageType type;
    /** 标题 */
    private String title;
    /** 读取状态 */
    private UserMessageStatus status;
    /** 创建人 */
    private String creator;
    /** 发布日期 */
    private LocalDateTime sendDate;
    /** 内容 */
    private String content;
    /** 部门Code */
    private String deptCode;

    private SmsMessageEnum smsMessageEnum;

    private List<ImageAO> images;
}
