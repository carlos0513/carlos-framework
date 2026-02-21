package com.yunjin.org.pojo.ao;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 系统用户 数据传输对象，service和manager向外传输对象
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
public class UserOrgAO {

    /**
     * 主键
     */
    private String id;
    /**
     * 用户名
     */
    private String account;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 手机号码
     */
    private String roleName;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门code
     */
    private String deptCode;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门完整名称
     */
    private List<String> deptFullName;


}
