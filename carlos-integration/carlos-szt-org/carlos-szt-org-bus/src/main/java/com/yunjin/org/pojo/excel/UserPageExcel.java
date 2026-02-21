package com.yunjin.org.pojo.excel;

import com.yunjin.json.jackson.annotation.EnumField;
import com.yunjin.org.pojo.enums.UserStateEnum;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author： lvbw
 * @date： 2025/10/23 15:16
 * @Description：
 */
@Data
public class UserPageExcel implements Serializable {

    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = "账号")
    private String account;
    @ExcelProperty(value = "姓名")
    private String realname;
    @ExcelProperty(value = "联系电话")
    private String phone;
    @ExcelProperty(value = "归属机构")
    private String deptName;
    @ExcelProperty(value = "角色")
    private String roleNames;
    @ExcelProperty(value = "区域")
    private String regionName;
    @ExcelProperty(value = "状态")
    private String state;
    @ExcelProperty(value = "创建时间")
    private String createTime;
    @ExcelProperty(value = "排序")
    private int sort;

}
