package com.carlos.org.pojo.dto;


import com.carlos.org.pojo.enums.UserStateEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户部门 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
public class UserDepartmentDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 部门id
     */
    private String departmentId;
    /**
     * 部门id
     */
    private String departmentCode;

    private String deptCode;
    /**
     * 部门id
     */
    private String departmentName;
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
     * 排序
     */
    private int sort;

    /**
     * 用户创建时间
     */
    private LocalDateTime createTime;
    /**
     * 机构层级Code
     */
    private String departmentLevelCode;
    /**
     * 用户角色
     */
    private String roleNames;
    /**
     * 用户角色
     */
    private String roleId;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 区域code
     */
    private String regionCode;

    /**
     * 用户创建人
     */
    private Long createBy;
}
