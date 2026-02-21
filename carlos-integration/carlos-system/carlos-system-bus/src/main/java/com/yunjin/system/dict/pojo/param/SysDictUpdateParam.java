package com.carlos.system.dict.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@ApiModel(value = "系统字典修改参数", description = "系统字典修改参数")
public class SysDictUpdateParam {

    @NotNull(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "字典名称")
    private String dictName;
    @ApiModelProperty(value = "描述")
    private String description;
    // @ApiModelProperty(value = "字典类型 数字类型 字符类型")
    // private DictTypeEnum type;


    @ApiModelProperty(value = "字典选项")
    private List<Item> items;

    @Data
    public static class Item {

        @ApiModelProperty(value = "id")
        private String id;
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
        @ApiModelProperty(value = "是否开启")
        private Boolean enable;

    }

}
