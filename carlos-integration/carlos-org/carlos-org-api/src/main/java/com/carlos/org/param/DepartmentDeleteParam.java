package com.carlos.org.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhangpd
 */
@Data
public class DepartmentDeleteParam {
    @NotBlank(message = "部门id不能为空")
    private String deptId;
}
