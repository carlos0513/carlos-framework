package com.carlos.org.pojo.param;


import com.carlos.org.pojo.enums.UserStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户状态
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统用户状态参数")
public class UserStateParam {

    @NotBlank(message = "用户id不能为空")
    @Schema(description = "主键")
    private String id;
    @NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    private UserStateEnum state;
}
