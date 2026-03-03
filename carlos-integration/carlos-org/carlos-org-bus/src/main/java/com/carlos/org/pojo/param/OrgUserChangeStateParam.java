package com.carlos.org.pojo.param;


import com.carlos.org.pojo.enums.OrgUserStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户状态变更参数
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Data
@Accessors(chain = true)
@Schema(description = "用户状态变更参数-UM006")
public class OrgUserChangeStateParam {

    @NotBlank(message = "用户id不能为空")
    @Schema(description = "用户ID")
    private String id;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0禁用，1启用")
    private OrgUserStateEnum state;

}
