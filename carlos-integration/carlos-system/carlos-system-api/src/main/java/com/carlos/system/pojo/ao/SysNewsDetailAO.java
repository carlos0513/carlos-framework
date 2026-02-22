package com.carlos.system.pojo.ao;

import com.carlos.json.jackson.annotation.UserIdField;
import com.carlos.json.jackson.annotation.UserIdField.SerializerType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统-通知公告 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-14 23:48:53
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysNewsDetailAO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "来源")
    private String source;
    @Schema(description = "标题图片")
    private List<com.carlos.system.pojo.ao.ImageAO> images;
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
    @UserIdField(type = SerializerType.REALNAME)
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
