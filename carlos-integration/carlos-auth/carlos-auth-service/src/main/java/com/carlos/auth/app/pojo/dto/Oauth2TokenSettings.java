package com.carlos.auth.app.pojo.dto;


import lombok.Data;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.io.Serializable;

@Data
public class Oauth2TokenSettings implements Serializable {

    /** 单位 分钟 */
    private Long authorizationCodeTimeToLive = 5L;

    /** 单位 分钟 */
    private Long accessTokenTimeToLive = 5L;

    /** 默认 @see OAuth2TokenFormat.SELF_CONTAINED */
    private String accessTokenFormat = OAuth2TokenFormat.SELF_CONTAINED.getValue();

    /** 默认true */
    private Boolean reuseRefreshTokens = true;

    /** 单位分钟 */
    private Long refreshTokenTimeToLive = 60L;

}
