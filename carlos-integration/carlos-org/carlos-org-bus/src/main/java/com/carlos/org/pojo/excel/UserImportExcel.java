package com.carlos.org.pojo.excel;


import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统用户 导入导出数据对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
public class UserImportExcel implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ExcelProperty(value = {"主键"})
    private Long id;
    /**
     * 用户名
     */
    @ExcelProperty(value = {"登录账号"})
    private String account;
    /**
     * 真实姓名
     */
    @ExcelProperty(value = {"用户名称"})
    private String realname;
    ///**
    // * 密码
    // */
    //@ExcelProperty(value = {"密码", "pwd"})
    //@ExcelIgnore
    //private String pwd;
    /**
     * 证件号码
     */
    @ExcelProperty(value = {"证件号码"})
    private String identify;
    /**
     * 手机号码
     */
    @ExcelProperty(value = {"手机号码"})
    private String phone;
    /**
     * 详细地址
     */
    @ExcelProperty(value = {"详细地址"})
    private String address;
    /**
     * 性别，0：保密, 1：男，2：女，默认0
     */
    @ExcelProperty(value = {"性别"})
    private String gender;
    /**
     * 邮箱
     */
    @ExcelProperty(value = {"邮箱"})
    private String email;
    /**
     * 备注
     */
    //@ExcelProperty(value = {"备注"})
    //private String description;
    /**
     * 角色
     */
    @ExcelProperty(value = {"角色"})
    private String role;
    /**
     * 组织机构
     */
    @ExcelProperty(value = {"组织机构"})
    private String department;
    /**
     * 部门编码,自编，非org_department表dept_code字段值
     */
    //@ExcelProperty(value = {"部门编码"})
    //private String departmentCode;
    /**
     * 排序
     */
    @ExcelProperty(value = {"部门排序"})
    private Integer deptSort;
    /**
     * 排序
     */
    @ExcelProperty(value = {"用户排序"})
    private Integer userSort;

    /**
     * 错误信息
     */
    @ExcelProperty(value = {"错误信息"})
    private String errorMsg;

}
