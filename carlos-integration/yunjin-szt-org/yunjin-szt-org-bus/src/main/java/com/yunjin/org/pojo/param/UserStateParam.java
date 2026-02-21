package com.yunjin.org.pojo.param;


import com.yunjin.org.pojo.enums.UserStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@Schema(value = "系统用户状态参数", description = "系统用户状态参数")
public class UserStateParam {

    @NotBlank(message = "用户id不能为空")
    @Schema(value = "主键")
    private String id;
    @NotNull(message = "状态不能为空")
    @Schema(value = "状态")
    private UserStateEnum state;
}
