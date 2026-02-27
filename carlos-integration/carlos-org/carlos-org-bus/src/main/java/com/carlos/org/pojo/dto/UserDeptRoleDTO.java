package com.carlos.org.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户角色部门信息
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
public class UserDeptRoleDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色名称
     */
    private String roleCode;
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门code名称
     */
    private String deptCode;
}
