package com.yunjin.org.pojo.param;


import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.enums.UserMessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.yunjin.core.param.ParamPage;

import java.time.LocalDateTime;

import java.lang.String;


/**
 * <p>
 * 用户消息表 列表查询参数封装
 * </p>
 *
 * @author  yunjin
 * @date    2024-2-28 17:39:16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "用户消息表列表查询参数", description = "用户消息表列表查询参数")
public class OrgUserMessagePageParam extends ParamPage {
    @Schema(value = "用户id")
    private String userId;
    @Schema(value = "消息类型")
    private UserMessageType type;
    @Schema(value = "标题")
    private String title;
    @Schema(value = "读取状态")
    private UserMessageStatus status;
    @Schema("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime start;
    @Schema("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime end;
    @Schema(value = "用户deptCode")
    private String deptCode;
}
