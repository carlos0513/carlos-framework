package com.carlos.system.news.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 浏览记录 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysViewRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "")
    private String id;
    @ApiModelProperty(value = "记录类型(0 通知公告,1 消息提醒)")
    private Integer type;
    @ApiModelProperty(value = "关联id")
    private String referenceId;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
