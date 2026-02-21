package com.carlos.system.news.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 系统-通知公告 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统-通知公告列表查询参数", description = "系统-通知公告列表查询参数")
public class SysNewsPageParam extends ParamPage {

    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "来源")
    private String source;
    @ApiModelProperty(value = "发送日期")
    private LocalDateTime sendDate;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "简介")
    private String introducing;
    @ApiModelProperty("开始时间")
    private LocalDateTime start;
    @ApiModelProperty("结束时间")
    private LocalDateTime end;
}
