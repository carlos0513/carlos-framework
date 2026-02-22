package com.carlos.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 部门菜单表 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
public class DepartmentMenuDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 部门id
     */
    private String departmentId;
    /**
     * 菜单id
     */
    private String menuId;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 租户id
     */
    private String tenantId;
}
