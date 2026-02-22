package com.carlos.org.login.pojo;

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
public class LoginVerifyVO implements Serializable {

    @Schema(description = "图片")
    private String image;

    @Schema(description = "图片令牌")
    private String verifyToken;

}
