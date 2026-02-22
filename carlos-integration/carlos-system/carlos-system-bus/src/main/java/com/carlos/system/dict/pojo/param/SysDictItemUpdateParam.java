package com.carlos.system.dict.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统字典详情 更新参数封装
 * </p>
 *
 * @author carlos
 * @date 2021-11-22 14:49:00
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统字典详情修改参数")
public class SysDictItemUpdateParam {

    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private String id;

    @Schema(description = "字典项值")
    private String itemName;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "是否启用")
    private Boolean enable;

}
