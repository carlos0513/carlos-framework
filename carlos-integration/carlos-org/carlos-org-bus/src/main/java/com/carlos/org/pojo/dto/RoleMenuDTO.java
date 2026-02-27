package com.carlos.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * <p>
 * 角色菜单 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
public class RoleMenuDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 菜单id
     */
    private String menuId;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 菜单id的集合
     */
    private Set<String> menuIds;
}
