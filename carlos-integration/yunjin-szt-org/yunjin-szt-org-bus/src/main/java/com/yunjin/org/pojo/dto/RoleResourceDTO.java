package com.yunjin.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色资源 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
public class RoleResourceDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 资源id
     */
    private String resourceId;
    /**
     * 租户id
     */
    private String tenantId;
}
