package com.carlos.oauth.oauth2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * auth中心相关配置
 * </p>
 *
 * @author carlos
 * @date 2021/12/8 10:49
 */
@Data
@ConfigurationProperties("carlos.auth")
public class OAuth2Properties {


}
