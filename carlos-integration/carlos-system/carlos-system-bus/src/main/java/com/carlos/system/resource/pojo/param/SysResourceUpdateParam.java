package com.carlos.system.resource.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统资源 更新参数封装
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统资源修改参数")
public class SysResourceUpdateParam {

    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private String id;
    @Schema(description = "分类id")
    private Long categoryId;
    @Schema(description = "资源名称")
    private String name;
    @Schema(description = "接口路径")
    private String path;
    @Schema(description = "接口路径前缀")
    private String pathPrefix;
    @Schema(description = "请求方式", allowableValues = "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE")
    private String method;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "资源类型，按钮")
    private String type;
    @Schema(description = "状态，0：禁用，1：启用")
    private String state;
    @Schema(description = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @Schema(description = "资源描述")
    private String description;
}
