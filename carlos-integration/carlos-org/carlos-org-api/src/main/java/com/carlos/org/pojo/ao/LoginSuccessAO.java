package com.carlos.org.pojo.ao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 登录成功返回信息
 * </p>
 *
 * @author Carlos
 * @date 2022/11/17 14:48
 */
@Data
@Accessors(chain = true)
public class LoginSuccessAO implements Serializable {

    @Schema(description = "请求头名称")
    private String name;

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "是否为首次登录")
    private Boolean firstLogin = false;
}
