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
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysNewsDetailAO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "标题")
    private String title;
    @Schema(value = "来源")
    private String source;
    @Schema(value = "标题图片")
    private List<com.carlos.system.pojo.ao.ImageAO> images;
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
    @UserIdField(type = SerializerType.REALNAME)
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
