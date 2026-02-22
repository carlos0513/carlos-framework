package com.carlos.org.login.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

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

    @Schema(description = "部门id", required = true)
    @NotNull
    private Serializable departmentId;
}
