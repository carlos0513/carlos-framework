package com.carlos.org.pojo.dto;

import com.carlos.json.jackson.annotation.UserIdField;
import com.carlos.system.pojo.ao.ImageAO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Accessors(chain = true)
public class NoticeDetailDTO extends AbstractMessageDetailDTO {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "来源")
    private String source;
    @Schema(description = "标题图片")
    private List<ImageAO> images;
    @Schema(description = "发送日期")
    private LocalDateTime sendDate;
    @Schema(description = "内容")
    private String content;
    @Schema(description = "")
    private Boolean enabled;
    @Schema(description = "简介")
    private String introducing;
    @Schema(description = "是否已读")
    private Boolean isRead;
    @UserIdField(type = UserIdField.SerializerType.REALNAME)
    @Schema(description = "创建者")
    private String createBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改者")
    private String updateBy;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
    @Schema(description = "租户id")
    private String tenantId;
    @Schema(description = "图像url")
    private List<String> imageUrlList;
}
