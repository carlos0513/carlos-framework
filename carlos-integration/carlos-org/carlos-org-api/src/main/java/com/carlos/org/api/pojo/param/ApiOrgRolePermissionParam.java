package com.carlos.org.api.pojo.param;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色权限 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
public class ApiOrgRolePermissionParam implements Serializable {
    /** 主键 */
    private Long id;
    /** 角色id */
    private Long roleId;
    /** 权限id */
    private Long permissionId;
    /** 创建者 */
    private Long createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 租户id */
    private Long tenantId;
}
