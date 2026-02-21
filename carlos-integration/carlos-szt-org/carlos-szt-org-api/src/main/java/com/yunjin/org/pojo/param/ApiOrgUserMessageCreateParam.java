package com.yunjin.org.pojo.param;


import com.yunjin.org.enums.SmsMessageEnum;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


/**
 * <p>
 * 用户消息表 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@Data
@Accessors(chain = true)
@Schema(value = "用户消息表新增参数", description = "用户消息表新增参数")
public class ApiOrgUserMessageCreateParam {
    @NotBlank(message = "用户id不能为空")
    @Schema(value = "用户id")
    private String userId;
    @Schema(value = "消息id")
    @NotBlank(message = "消息id不能为空")
    private String messageId;
    @Schema(value = "消息类型")
    private UserMessageType type;
    @NotBlank(message = "标题不能为空")
    @Schema(value = "标题")
    private String title;
    @Schema(value = "读取状态")
    private UserMessageStatus status;
    @NotBlank(message = "创建人不能为空")
    @Schema(value = "创建人")
    private String creator;
    @Schema(value = "发布日期")
    private LocalDateTime sendDate;
    @NotBlank(message = "内容不能为空")
    @Schema(value = "内容")
    private String content;
    @Schema(value = "部门CODE")
    private String deptCode;
    @Schema(value = "短信类型")
    private SmsMessageEnum smsMessageEnum;
}
