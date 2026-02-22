package com.carlos.org.pojo.ao;


import com.carlos.org.pojo.enums.UserStateEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户部门 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
public class UserDepartmentAO {

    /**
     * 主键
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 部门id
     */
    private String departmentId;
    /**
     * 是否是管理员
     */
    private Boolean isAdmin;

    /**
     * 用户名
     */
    private String account;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 用户状态
     */
    private UserStateEnum state;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户部门Code
     */
    private String departmentCode;

    /**
     * 用户部门名称
     */
    private String departmentName;

    /**
     * 用户部门层级
     */
    private String departmentLevelCode;
}
