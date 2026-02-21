package com.carlos.system.news.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 浏览记录 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "浏览记录修改参数", description = "浏览记录修改参数")
public class SysViewRecordUpdateParam {

    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "")
    private String id;
    @ApiModelProperty(value = "记录类型(0 通知公告,1 消息提醒)")
    private Integer type;
    @ApiModelProperty(value = "关联id")
    private String referenceId;
    @ApiModelProperty(value = "用户id")
    private String userId;
}
