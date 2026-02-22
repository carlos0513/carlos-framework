package com.carlos.org.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author 李海俊
 * @description 设置部门管理员入参
 * @date 2023/5/15 11:33
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
@Schema(description = "部门管理员设置参数")
public class DepartmentAdminParam {

    @Schema(description = "部门id", required = true)
    @NotBlank(message = "部门id不能为空")
    private String deptId;

    @NotBlank(message = "用户id不能为空")
    @Schema(description = "用户id", required = true)
    private String userId;

    @NotNull(message = "管理员标识不能为空")
    @Schema(description = "是否为管理标识 true:设为管理员;false:取消", required = true)
    private Boolean adminFlag;
}
