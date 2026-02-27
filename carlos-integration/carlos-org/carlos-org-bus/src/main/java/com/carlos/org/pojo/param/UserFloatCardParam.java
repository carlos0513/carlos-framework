package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *   用户卡片信息参数
 * </p>
 *
 * @author Carlos
 * @date 2025-10-14 15:57
 */
@Data
@Accessors(chain = true)
public class UserFloatCardParam {

    @NotBlank(message = "用户id不能为空")
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "部门id")
    private String deptId;
    @Schema(description = "部门编码")
    private String deptCode;
}
