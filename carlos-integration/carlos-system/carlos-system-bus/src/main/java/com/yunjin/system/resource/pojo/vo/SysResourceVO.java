package com.carlos.system.resource.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.carlos.json.jackson.annotation.DictField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统资源 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "分类id")
    private Long categoryId;
    @ApiModelProperty(value = "菜单名称")
    private String categoryName;
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
    @DictField
    @ApiModelProperty(value = "资源描述")
    private String description;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

}
