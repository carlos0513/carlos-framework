package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 权限 新增参数封装
 * </p>
 * <p>PM-002 新增权限</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "权限新增参数")
public class OrgPermissionCreateParam {

    /**
     * 父权限ID，不传或传0表示根节点
     */
    @Schema(description = "父权限ID，不传或传0表示根节点")
    private Long parentId;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Schema(description = "权限名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String permName;

    /**
     * 权限编码，如"user:create"
     */
    @NotBlank(message = "权限编码不能为空")
    @Schema(description = "权限编码，如user:create", requiredMode = Schema.RequiredMode.REQUIRED)
    private String permCode;

    /**
     * 权限类型，1：菜单, 2：按钮, 3：API, 4：数据字段
     */
    @NotNull(message = "权限类型不能为空")
    @Schema(description = "权限类型，1：菜单, 2：按钮, 3：API, 4：数据字段", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer permType;

    /**
     * 资源路径
     */
    @Schema(description = "资源路径")
    private String resourceUrl;

    /**
     * HTTP方法：GET/POST/PUT/DELETE
     */
    @Schema(description = "HTTP方法")
    private String method;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    /**
     * 排序，不传默认为0
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String description;

}
