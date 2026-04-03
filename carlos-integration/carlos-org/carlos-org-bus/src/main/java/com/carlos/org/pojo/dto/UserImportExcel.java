package com.carlos.org.pojo.dto;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 用户导入 Excel DTO
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
public class UserImportExcel {

    @ExcelProperty("用户名")
    @ColumnWidth(20)
    private String username;

    @ExcelProperty("姓名")
    @ColumnWidth(15)
    private String realName;

    @ExcelProperty("手机号")
    @ColumnWidth(15)
    private String phone;

    @ExcelProperty("邮箱")
    @ColumnWidth(25)
    private String email;

    @ExcelProperty("部门编码")
    @ColumnWidth(20)
    private String deptCode;

    @ExcelProperty("角色编码")
    @ColumnWidth(20)
    private String roleCode;

    @ExcelProperty("岗位编码")
    @ColumnWidth(20)
    private String positionCode;

    /**
     * 错误信息（导入时回填）
     */
    @ExcelProperty(value = "错误信息", index = 7)
    @ColumnWidth(30)
    private String errorMsg;
}
