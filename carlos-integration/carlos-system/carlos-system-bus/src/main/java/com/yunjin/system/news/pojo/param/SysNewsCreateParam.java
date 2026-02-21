package com.carlos.system.news.pojo.param;


import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.carlos.system.enums.UserMessageType;
import com.carlos.system.news.pojo.dto.ImageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>
 * 系统-通知公告 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "系统-通知公告新增参数", description = "系统-通知公告新增参数")
public class SysNewsCreateParam {

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "来源")
    private String source;
    @ApiModelProperty(value = "消息类型")
    private UserMessageType type = UserMessageType.MESSAGE;
    @ApiModelProperty(value = "标题图片")
    private List<ImageDTO> images;
    @ApiModelProperty(value = "发送日期")
    @JsonFormat(shape = Shape.STRING, pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime sendDate;
    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "")
    private Boolean enabled;
    @ApiModelProperty(value = "简介")
    private String introducing;
}
