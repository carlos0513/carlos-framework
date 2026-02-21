package com.carlos.system.dict.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import java.util.List;


/**
 * <p>
 * 系统字典 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "系统字典新增参数", description = "系统字典新增参数")
public class SysDictCreateParam {

    @NotBlank(message = "字典名称不能为空")
    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @NotBlank(message = "字典编码不能为空")
    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "字典选项")
    private List<Item> items;

    // @NotNull(message = "字典类型 数字类型 字典类型不能为空")
    // @ApiModelProperty(value = "字典类型 数字类型 字符类型")
    // private DictTypeEnum type;


    @Data
    public static class Item {

        @NotBlank(message = "字典项值不能为空")
        @ApiModelProperty(value = "字典项值")
        private String itemName;
        @NotBlank(message = "字典项code不能为空")
        @ApiModelProperty(value = "字典项key")
        private String itemCode;
        @ApiModelProperty(value = "描述")
        private String description;
        @ApiModelProperty(value = "排序")
        private Integer sort;

    }
}
