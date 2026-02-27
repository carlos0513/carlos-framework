package com.carlos.org.pojo.excel;


import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 部门 导入导出数据对象
 * </p>
 */
@Data
public class DepartmentExcel implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 部门名称
     */
    @ExcelProperty(value = "部门名称")
    private String deptName;

    /**
     * 部门编号
     */
    @ExcelProperty(value = "部门编号")
    private String deptCode;

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
     * 联系方式
     */
    @ExcelProperty(value = "联系方式")
    private String tel;

    /**
     * 父id
     */
    @ExcelProperty(value = "父id")
    private String parentId;

    /**
     * 部门层级
     */
    @ExcelProperty(value = "部门层级")
    private Long level;

    /**
     * 状态，0：禁用，1：启用
     */
    @ExcelProperty(value = "状态")
    private String state;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String description;


}
