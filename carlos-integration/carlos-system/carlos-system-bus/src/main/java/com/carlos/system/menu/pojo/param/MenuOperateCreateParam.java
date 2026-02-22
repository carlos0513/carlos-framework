package com.carlos.system.menu.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 菜单操作 新增参数封装
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@Schema(description = "菜单操作新增参数")
public class MenuOperateCreateParam {
    @Schema(description = "资源名称")
    private String operateName;
    @Schema(description = "资源编码")
    private String operateCode;
    @Schema(description = "接口路径")
    private String path;
    @NotBlank(message = "请求方式不能为空")
    @Schema(description = "请求方式")
    private String operateMethod;
    @Schema(description = "图标")
    private String icon;
    @NotBlank(message = "资源类型，按钮不能为空")
    @Schema(description = "资源类型，按钮")
    private String operateType;
    @NotBlank(message = "状态，0：禁用，1：启用不能为空")
    @Schema(description = "状态，0：禁用，1：启用")
    private String state;
    @NotNull(message = "显示和隐藏，0：显示，1：隐藏不能为空")
    @Schema(description = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @Schema(description = "资源描述")
    private String description;
}
