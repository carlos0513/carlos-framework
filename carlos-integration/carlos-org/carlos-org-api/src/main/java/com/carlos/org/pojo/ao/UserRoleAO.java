package com.carlos.org.pojo.ao;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户角色 数据传输对象，service和manager向外传输对象
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
public class UserRoleAO {

    /**
     * 主键
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 部门id
     */
    private String departmentId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色名称
     */
    private String roleCode;
}
