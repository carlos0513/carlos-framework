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
public class UserRegionInitExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "主键")
    private Long id;

    @ExcelProperty(value = "用户名")
    private String account;

    @ExcelProperty(value = "真实姓名")
    private String realname;

    @ExcelProperty(value = "手机号码")
    private String phone;

    @ExcelProperty(value = "行政区域编码")
    private String regionCode;

    @ExcelProperty(value = "行政区域名称")
    private String regionName;

    @ExcelProperty(value = "部门code")
    private String deptCode;

    @ExcelProperty(value = "部门名称")
    private String deptName;

    @ExcelProperty(value = "成功")
    private boolean success;


}
