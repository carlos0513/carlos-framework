package com.carlos.system.pojo.ao;

import com.carlos.json.jackson.annotation.UserIdField;
import com.carlos.json.jackson.annotation.UserIdField.SerializerType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "来源")
    private String source;
    @ApiModelProperty(value = "标题图片")
    private List<com.carlos.system.pojo.ao.ImageAO> images;
    @ApiModelProperty(value = "发送日期")
    private LocalDateTime sendDate;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "")
    private Boolean enabled;
    @ApiModelProperty(value = "简介")
    private String introducing;
    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;
    @UserIdField(type = SerializerType.REALNAME)
    @ApiModelProperty(value = "创建者")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "修改者")
    private String updateBy;
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "租户id")
    private String tenantId;
    @ApiModelProperty(value = "图像url")
    private List<String> imageUrlList;

}
