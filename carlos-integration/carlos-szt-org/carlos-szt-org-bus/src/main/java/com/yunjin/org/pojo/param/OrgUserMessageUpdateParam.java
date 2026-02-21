package com.yunjin.org.pojo.param;


import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.lang.String;

/**
* <p>
    * 用户消息表 更新参数封装
    * </p>
*
* @author  yunjin
* @date    2024-2-28 17:39:16
*/
@Data
@Accessors(chain = true)
@Schema(value = "用户消息表修改参数", description = "用户消息表修改参数")
public class OrgUserMessageUpdateParam {
        @NotBlank(message = "主键不能为空")
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
}
