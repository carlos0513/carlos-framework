package com.yunjin.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 角色资源组关联表 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
public class RoleResourceGroupRefDTO {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 资源组id
     */
    private String resourceGroupId;
    /**
     * 创建者编号
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
