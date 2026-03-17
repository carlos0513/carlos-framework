package com.carlos.integration.module.dingtalk.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * <p>
 * 钉钉 Token 响应
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
public class DingtalkTokenResponse {

    /**
     * 返回�?
     */
    private Integer errcode;

    /**
     * 返回信息
     */
    private String errmsg;

    /**
     * AccessToken
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 过期时间（秒�?
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;

    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return errcode != null && errcode == 0;
    }
}

