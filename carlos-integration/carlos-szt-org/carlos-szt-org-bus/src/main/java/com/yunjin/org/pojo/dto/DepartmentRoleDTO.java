package com.yunjin.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 部门角色 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
public class DepartmentRoleDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 部门id
     */
    private String departmentType;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 版本
     */
    private Long version;
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
}
