package com.carlos.system.dict.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 系统字典 更新参数封装
 * </p>
 *
 * @author carlos
 * @date 2021-11-22 14:49:00
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统字典修改参数")
public class SysDictUpdateParam {

    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private String id;
    @Schema(description = "字典名称")
    private String dictName;
    @Schema(description = "描述")
    private String description;
    // @Schema(description = "字典类型 数字类型 字符类型")
    // private DictTypeEnum type;


    @Schema(description = "字典选项")
    private List<Item> items;

    @Data
    public static class Item {

        @Schema(description = "id")
        private String id;
        @NotBlank(message = "字典项值不能为空")
        @Schema(description = "字典项值")
        private String itemName;
        @NotBlank(message = "字典项code不能为空")
        @Schema(description = "字典项key")
        private String itemCode;
        @Schema(description = "描述")
        private String description;
        @Schema(description = "排序")
        private Integer sort;
        @Schema(description = "是否开启")
        private Boolean enable;

    }

}
