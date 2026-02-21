package com.yunjin.org.pojo.dto;

import com.yunjin.json.jackson.annotation.UserIdField;
import com.yunjin.system.pojo.ao.ImageAO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class MessageDetailDTO extends AbstractMessageDetailDTO{
    @Schema(value = "主键")
    private String id;
    @Schema(value = "标题")
    private String title;
    @Schema(value = "来源")
    private String source;
    @Schema(value = "标题图片")
    private List<ImageAO> images;
    @Schema(value = "发送日期")
    private LocalDateTime sendDate;
    @Schema(value = "内容")
    private String content;
    @Schema(value = "")
    private Boolean enabled;
    @Schema(value = "简介")
    private String introducing;
    @Schema(value = "是否已读")
    private Boolean isRead;
    @UserIdField(type = UserIdField.SerializerType.REALNAME)
    @Schema(value = "创建者")
    private String createBy;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "修改者")
    private String updateBy;
    @Schema(value = "修改时间")
    private LocalDateTime updateTime;
    @Schema(value = "租户id")
    private String tenantId;
    @Schema(value = "图像url")
    private List<String> imageUrlList;
}
