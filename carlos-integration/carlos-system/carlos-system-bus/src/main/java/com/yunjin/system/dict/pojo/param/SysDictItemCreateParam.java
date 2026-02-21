package com.carlos.system.dict.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * <p>
 * 系统字典详情 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统字典详情新增参数", description = "系统字典详情新增参数")
public class SysDictItemCreateParam {

    @NotNull(message = "字典id不能为空")
    @Schema(value = "字典id")
    private String dictId;
    @NotBlank(message = "字典项值不能为空")
    @Schema(value = "字典项值")
    private String itemName;
    @NotBlank(message = "字典项code不能为空")
    @Schema(value = "字典项key")
    private String itemCode;
    @Schema(value = "描述")
    private String description;

}
