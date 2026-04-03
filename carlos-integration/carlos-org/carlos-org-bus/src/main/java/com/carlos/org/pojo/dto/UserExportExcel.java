package com.carlos.org.pojo.dto;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户导出 Excel DTO
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
public class UserExportExcel {

    @ExcelProperty("用户ID")
    @ColumnWidth(15)
    private Long userId;

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

    @ExcelProperty("部门")
    @ColumnWidth(30)
    private String departments;

    @ExcelProperty("角色")
    @ColumnWidth(30)
    private String roles;

    @ExcelProperty("岗位")
    @ColumnWidth(30)
    private String positions;

    @ExcelProperty("状态")
    @ColumnWidth(10)
    private String status;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private LocalDateTime createTime;
}
