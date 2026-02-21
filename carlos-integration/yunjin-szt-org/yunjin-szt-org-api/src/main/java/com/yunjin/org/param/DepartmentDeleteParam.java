package com.yunjin.org.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangpd
 */
@Data
public class DepartmentDeleteParam {
    @NotBlank(message = "部门id不能为空")
    private String deptId;
}
