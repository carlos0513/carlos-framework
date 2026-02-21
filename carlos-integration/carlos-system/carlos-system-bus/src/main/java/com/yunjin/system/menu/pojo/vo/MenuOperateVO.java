package com.carlos.system.menu.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单操作 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuOperateVO implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty(value = "主键")
        private String id;
        @ApiModelProperty(value = "资源名称")
        private String operateName;
        @ApiModelProperty(value = "资源编码")
        private String operateCode;
        @ApiModelProperty(value = "接口路径")
        private String path;
        @ApiModelProperty(value = "菜单id")
        private String menuId;
        @ApiModelProperty(value = "请求方式")
        private String operateMethod;
        @ApiModelProperty(value = "图标")
        private String icon;
        @ApiModelProperty(value = "资源类型，按钮")
        private String operateType;
        @ApiModelProperty(value = "状态，0：禁用，1：启用")
        private String state;
        @ApiModelProperty(value = "显示和隐藏，0：显示，1：隐藏")
        private Boolean hidden;
        @ApiModelProperty(value = "资源描述")
        private String description;
        @ApiModelProperty(value = "创建时间")
        private LocalDateTime createTime;
        @ApiModelProperty(value = "修改时间")
        private LocalDateTime updateTime;

}
