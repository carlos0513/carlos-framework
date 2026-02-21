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
 * 浏览记录 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "浏览记录列表查询参数", description = "浏览记录列表查询参数")
public class SysViewRecordPageParam extends ParamPage {

    @ApiModelProperty(value = "记录类型(0 通知公告,1 消息提醒)")
    private Integer type;
    @ApiModelProperty(value = "关联id")
    private String referenceId;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty("开始时间")
    private LocalDateTime start;

    @ApiModelProperty("结束时间")
    private LocalDateTime end;
}
