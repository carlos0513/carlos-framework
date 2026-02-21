package com.carlos.system.news.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 浏览记录 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "浏览记录新增参数", description = "浏览记录新增参数")
public class SysViewRecordCreateParam {

    @NotNull(message = "记录类型(0 通知公告,1 消息提醒)不能为空")
    @ApiModelProperty(value = "记录类型(0 通知公告,1 消息提醒)")
    private Integer type;
    @NotBlank(message = "关联id不能为空")
    @ApiModelProperty(value = "关联id")
    private String referenceId;
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String userId;
}
