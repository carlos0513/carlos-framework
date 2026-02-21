package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

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
    @Schema(value = "用户id")
    private String userId;
    @Schema(value = "部门id")
    private String deptId;
    @Schema(value = "部门编码")
    private String deptCode;
}
