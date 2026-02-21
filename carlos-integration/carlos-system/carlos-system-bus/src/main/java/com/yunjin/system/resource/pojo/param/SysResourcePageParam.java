package com.carlos.system.resource.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 系统资源 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统资源列表查询参数", description = "系统资源列表查询参数")
public class SysResourcePageParam extends ParamPage {

    @ApiModelProperty(value = "分类id")
    private Long categoryId;
    @ApiModelProperty(value = "资源名称")
    private String name;
    @ApiModelProperty(value = "接口路径")
    private String path;
    @ApiModelProperty(value = "请求方式")
    private String method;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "资源类型，按钮")
    private String type;
    @ApiModelProperty(value = "状态，0：禁用，1：启用")
    private String state;
    @ApiModelProperty(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @ApiModelProperty(value = "资源描述")
    private String description;
    @ApiModelProperty("开始时间")
    private LocalDateTime start;

    @ApiModelProperty("结束时间")
    private LocalDateTime end;
}
