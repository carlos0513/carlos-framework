package com.carlos.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单 数据源对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@TableName("org_role_menu")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class RoleMenu extends Model<RoleMenu> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private String roleId;
    /**
     * 菜单id
     */
    @TableField(value = "menu_id")
    private String menuId;
    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private String tenantId;

}
