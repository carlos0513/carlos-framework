package com.yunjin.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户导入 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Data
@Accessors(chain = true)
public class UserDeptMapDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String yjUserId;
    private String hbUserId;
    private String account;
    private String phone;
    private String realname;
    private String yjDeptId;
    private String hbDeptId;
    private String dept;
    private String deptCode;
    private String hbRoleName;
    private String hbRoleId;
    private String yjRoleId;


}
