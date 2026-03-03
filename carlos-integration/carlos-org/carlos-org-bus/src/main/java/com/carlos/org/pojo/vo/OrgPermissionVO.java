package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 权限 VO
 * </p>
 * <p>PM-001 权限树, PM-002-006 权限管理</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgPermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Schema(description = "权限id")
    private Long id;

    /**
     * 父权限ID
     */
    @Schema(description = "父权限ID")
    private Long parentId;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    private String permName;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码")
    private String permCode;

    /**
     * 权限类型，1：菜单, 2：按钮, 3：API, 4：数据字段
     */
    @Schema(description = "权限类型")
    private Integer permType;

    /**
     * 权限类型名称
     */
    @Schema(description = "权限类型名称")
    private String permTypeName;

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
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 状态，0：禁用, 1：启用
     */
    @Schema(description = "状态")
    private Integer state;

    /**
     * 状态名称
     */
    @Schema(description = "状态名称")
    private String stateName;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String description;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 子权限列表
     */
    @Schema(description = "子权限列表")
    private List<OrgPermissionVO> children;

}
