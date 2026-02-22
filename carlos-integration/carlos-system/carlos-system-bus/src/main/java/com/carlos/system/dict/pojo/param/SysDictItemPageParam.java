package com.carlos.system.dict.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 系统字典详情 列表查询参数封装
 * </p>
 *
 * @author carlos
 * @date 2021-11-22 14:49:00
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统字典详情列表查询参数")
public class SysDictItemPageParam extends ParamPage {

    @Schema(description = "字典id")
    private Long dictId;

    @Schema(description = "字典项值")
    private String itemName;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
