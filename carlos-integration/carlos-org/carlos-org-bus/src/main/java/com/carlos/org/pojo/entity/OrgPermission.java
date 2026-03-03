package com.carlos.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.carlos.org.pojo.enums.OrgPermissionStateEnum;
import com.carlos.org.pojo.enums.OrgPermissionTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@TableName("org_permission")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgPermission extends Model<OrgPermission> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;

    /**
     * 父权限ID，0为根
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 权限名称
     */
    @TableField(value = "perm_name")
    private String permName;

    /**
     * 权限编码，如"user:create"
     */
    @TableField(value = "perm_code")
    private String permCode;

    /**
     * 权限类型，1：菜单, 2：按钮, 3：API, 4：数据字段
     */
    @TableField(value = "perm_type")
    private OrgPermissionTypeEnum permType;

    /**
     * 资源路径
     */
    @TableField(value = "resource_url")
    private String resourceUrl;

    /**
     * HTTP方法：GET/POST/PUT/DELETE
     */
    @TableField(value = "method")
    private String method;

    /**
     * 菜单图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 同级排序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 状态，0：禁用, 1：启用
     */
    @TableField(value = "state")
    private OrgPermissionStateEnum state;

    /**
     * 备注
     */
    @TableField(value = "description")
    private String description;

    /**
     * 版本
     */
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改者
     */
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private Long tenantId;

}
