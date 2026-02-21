package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.json.jackson.annotation.EnumField;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import com.yunjin.system.pojo.ao.ImageAO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
@JsonInclude(JsonInclude.Include.ALWAYS)
public class OrgUserMessageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "用户id")
    private String userId;
    @Schema(value = "消息id")
    private String messageId;
    @EnumField(type = EnumField.SerializerType.FULL)
    @Schema(value = "消息类型")
    private UserMessageType type;
    @Schema(value = "标题")
    private String title;
    @EnumField(type = EnumField.SerializerType.FULL)
    @Schema(value = "读取状态")
    private UserMessageStatus status;
    @Schema(value = "创建人")
    private String creator;
    @Schema(value = "发布日期")
    private LocalDateTime sendDate;
    @Schema(value = "内容")
    private String content;
    @Schema(value = "部门Code")
    private String deptCode;
    /**
     * 图片信息
     */
    private List<ImageAO> images;
    /**
     * 创建人姓名
     */
    @Schema(value = "创建人姓名")
    private String creatorName;
}
