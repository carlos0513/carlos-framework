package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限 更新参数封装
 * </p>
 * <p>PM-003 编辑权限</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "权限修改参数")
public class OrgPermissionUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    private String permName;

    /**
     * 资源路径
     */
    @Schema(description = "资源路径")
    private String resourceUrl;

    /**
     * HTTP方法
     */
    @Schema(description = "HTTP方法")
    private String method;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String description;

}
