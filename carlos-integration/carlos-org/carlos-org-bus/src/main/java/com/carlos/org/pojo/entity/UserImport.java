package com.carlos.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@TableName("org_user_import")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserImport implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 用户名
     */
    @TableField(value = "account")
    private String account;
    /**
     * 真实姓名
     */
    @TableField(value = "realname")
    private String realname;
    /**
     * 证件号码
     */
    @TableField(value = "identify")
    private String identify;
    /**
     * 手机号码
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 角色名称
     */
    @TableField(value = "role")
    private String role;
    /**
     * 部门完整信息，以”-“分割部门级别
     */
    @TableField(value = "department")
    private String department;
    /**
     * 行政区域编码
     */
    @TableField(value = "region_code")
    private String regionCode;
    /**
     * 性别，0：保密, 1：男，2：女，默认0
     */
    @TableField(value = "gender")
    private String gender;
    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;
}
