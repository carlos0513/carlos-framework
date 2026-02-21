package com.carlos.system.dict.pojo.param;


import com.carlos.core.param.ParamPage;
import com.carlos.system.dict.pojo.enums.DictTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 系统字典 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统字典列表查询参数", description = "系统字典列表查询参数")
public class SysDictPageParam extends ParamPage {

    @ApiModelProperty(value = "字典名称")
    private String dictName;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "字典类型 数字类型 字符类型")
    private DictTypeEnum type;
    @ApiModelProperty("开始时间")
    private LocalDateTime start;
    @ApiModelProperty("结束时间")
    private LocalDateTime end;
}
