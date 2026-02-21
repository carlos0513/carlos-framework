package com.carlos.system.news.pojo.param;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.carlos.system.news.pojo.dto.ImageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统-通知公告 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "系统-通知公告修改参数", description = "系统-通知公告修改参数")
public class SysNewsUpdateParam {

    @NotBlank(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "来源")
    private String source;
    @ApiModelProperty(value = "标题图片")
    private List<ImageDTO> images;
    @ApiModelProperty(value = "发送日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendDate;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "")
    private Boolean enabled;
    @ApiModelProperty(value = "简介")
    private String introducing;
    @ApiModelProperty(value = "租户id")
    private String tenantId;

}
