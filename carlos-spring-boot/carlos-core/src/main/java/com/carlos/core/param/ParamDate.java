package com.carlos.core.param;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 接口时间参数封装
 * </p>
 *
 * @author yunjin
 * @date 2020/4/14 17:31
 */
@Data
public class ParamDate implements Param {

    @Schema(description = "时间起点")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date startDate;

    @Schema(description = "时间终点")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date endDate;
}
