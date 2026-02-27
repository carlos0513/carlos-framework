package com.carlos.system.menu.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单操作 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuOperateVO implements Serializable {
        private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "资源名称")
        private String operateName;
    @Schema(description = "资源编码")
        private String operateCode;
    @Schema(description = "接口路径")
        private String path;
    @Schema(description = "菜单id")
        private String menuId;
    @Schema(description = "请求方式")
        private String operateMethod;
    @Schema(description = "图标")
        private String icon;
    @Schema(description = "资源类型，按钮")
        private String operateType;
    @Schema(description = "状态，0：禁用，1：启用")
        private String state;
    @Schema(description = "显示和隐藏，0：显示，1：隐藏")
        private Boolean hidden;
    @Schema(description = "资源描述")
        private String description;
    @Schema(description = "创建时间")
        private LocalDateTime createTime;
    @Schema(description = "修改时间")
        private LocalDateTime updateTime;

}
