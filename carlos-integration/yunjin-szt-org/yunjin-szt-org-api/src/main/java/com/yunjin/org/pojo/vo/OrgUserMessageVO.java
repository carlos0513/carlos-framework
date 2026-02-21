package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户消息表 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgUserMessageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "用户id")
    private String userId;
    @Schema(value = "消息id")
    private String messageId;
    @Schema(value = "消息类型")
    private UserMessageType type;
    @Schema(value = "标题")
    private String title;
    @Schema(value = "读取状态")
    private UserMessageStatus status;
    @Schema(value = "创建人")
    private String creator;
    @Schema(value = "发布日期")
    private LocalDateTime sendDate;
    @Schema(value = "内容")
    private String content;
    @Schema(value = "部门code")
    private String deptCode;
    /**
     * 创建人姓名
     */
    @Schema(value = "创建人姓名")
    private String creatorName;
}
