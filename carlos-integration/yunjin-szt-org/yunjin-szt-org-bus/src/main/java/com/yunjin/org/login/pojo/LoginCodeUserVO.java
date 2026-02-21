package com.yunjin.org.login.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author Carlos
 * @date 2022/11/17 14:48
 */
@Data
@Accessors(chain = true)
public class LoginCodeUserVO implements Serializable {

    private static final long serialVersionUID = 1125713819389425229L;
    @Schema("id")
    private String id;

    @Schema("账号")
    private String account;

    @Schema("姓名")
    private String name;

    @Schema("手机号")
    private String phone;

}
