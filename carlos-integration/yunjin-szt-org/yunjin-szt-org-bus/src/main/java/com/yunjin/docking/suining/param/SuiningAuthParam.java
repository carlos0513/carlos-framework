package com.yunjin.docking.suining.param;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunjin.core.param.Param;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 翻译返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class SuiningAuthParam implements Param {


    /** 授权码，用户对应用授权后得到。本参数在 grant_type 为 authorization_code 时必填；为 refresh_token 时不填 */
    @JsonProperty("code")
    private String code;

    /** 授权方式。支持：
     authorization_code，表示换取使用用户授权码code换取授权令牌access_token。
     refresh_token，表示使用refresh_token刷新获取新授权令牌。 */
    @JsonProperty("grant_type")
    private String grantType;

    /** 刷新令牌，上次换取访问令牌时得到。本参数在 grant_type 为 authorization_code 时不填；为 refresh_token 时必填，且该值来源于此接口的返回值 refresh_token（即至少需要通过 grant_type=authorization_code 调用此接口一次才能获取）。 */
    @JsonProperty("refresh_token")
    private String refreshToken;
}
