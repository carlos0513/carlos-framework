package com.carlos.system.dict.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 系统字典 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统字典修改参数", description = "系统字典修改参数")
public class SysDictUpdateParam {

    @NotNull(message = "主键不能为空")
    @Schema(value = "主键")
    private String id;
    @Schema(value = "字典名称")
    private String dictName;
    @Schema(value = "描述")
    private String description;
    // @Schema(value = "字典类型 数字类型 字符类型")
    // private DictTypeEnum type;


    @Schema(value = "字典选项")
    private List<Item> items;

    @Data
    public static class Item {

        @Schema(value = "id")
        private String id;
        @NotBlank(message = "字典项值不能为空")
        @Schema(value = "字典项值")
        private String itemName;
        @NotBlank(message = "字典项code不能为空")
        @Schema(value = "字典项key")
        private String itemCode;
        @Schema(value = "描述")
        private String description;
        @Schema(value = "排序")
        private Integer sort;
        @Schema(value = "是否开启")
        private Boolean enable;

    }

}
