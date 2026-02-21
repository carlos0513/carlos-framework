package com.yunjin.docking.suining.result;


import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AccessTokenResult {


    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("user_type")
    private String userType;
    @JsonProperty("re_expires_in")
    private Integer reExpiresIn;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("jwtToken")
    private String jwtToken;
    @JsonProperty("expire_in")
    private Integer expireIn;
    @JsonProperty("token_type")
    private String tokenType;
    /** 用戶json加密串 */
    @JsonProperty("userinfo")
    private String userinfo;
    @JsonProperty("status")
    private String status;
}
