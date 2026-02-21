package com.yunjin.org.pojo.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统用户 导入导出数据对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
public class UserExcel implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;
    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    private String account;
    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名")
    private String realname;
    /**
     * 密码
     */
    @ExcelProperty(value = "密码")
    private String pwd;
    /**
     * 证件号码
     */
    @ExcelProperty(value = "证件号码")
    private String identify;
    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码")
    private String phone;
    /**
     * 行政区域编码
     */
    @ExcelProperty(value = "行政区域编码")
    private String regionCode;
    /**
     * 详细地址
     */
    @ExcelProperty(value = "详细地址")
    private String address;
    /**
     * 性别，0：保密, 1：男，2：女，默认0
     */
    @ExcelProperty(value = "性别")
    private String gender;
    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    private String email;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String description;
    /**
     * 状态，0：禁用，1：启用，2：锁定
     */
    @ExcelProperty(value = "状态")
    private String state;
    /**
     * 是否是机构管理员
     */
    @ExcelProperty(value = "是否是机构管理员")
    private Boolean admin;
    ///**
    // * 角色id列表
    // */
    //@ExcelProperty(value = "角色id列表(逗号,分隔)", converter = RoleIdConverter.class)
    //private Set<String> roleIds;
    ///**
    // * 用户部门id
    // */
    //@ExcelProperty(value = "用户部门Id")
    //private String departmentId;

}
