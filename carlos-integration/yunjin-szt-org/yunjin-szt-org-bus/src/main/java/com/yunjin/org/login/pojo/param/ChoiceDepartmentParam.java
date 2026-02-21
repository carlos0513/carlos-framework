package com.yunjin.org.login.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 部门选择参数
 * </p>
 *
 * @author Mutsuki
 * @date 2023/7/17 14:46
 */
@Data
@Accessors(chain = true)
public class ChoiceDepartmentParam {

    @Schema(value = "部门id", required = true)
    @NotNull
    private Serializable departmentId;
}
