package com.carlos.oauth.support.thirdparty;

import com.carlos.core.auth.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;

/**
 * 默认的第三方登录服务实现
 *
 * <p><strong>警告：此类仅用于演示和测试！</strong></p>
 *
 * <p>该实现仅打印日志并返回模拟数据，不包含真实的第三方平台调用。</p>
 * <p>生产环境必须为每种登录方式提供真实的实现：</p>
 * <ul>
 *   <li>微信登录 - 接入微信开放平台 API</li>
 *   <li>钉钉登录 - 接入钉钉开放平台 API</li>
 *   <li>短信登录 - 接入阿里云/腾讯云短信服务</li>
 * </ul>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Slf4j
public class DefaultThirdPartyLoginService implements ThirdPartyLoginService {

    private final ThirdPartyType type;

    public DefaultThirdPartyLoginService(ThirdPartyType type) {
        this.type = type;
    }

    @Override
    public LoginUserInfo login(String authCode) {
        log.warn("=================================================================");
        log.warn(" Using DefaultThirdPartyLoginService - FOR DEVELOPMENT ONLY!     ");
        log.warn(" Type: {}", type.getDescription());
        log.warn("=================================================================");
        log.warn("Auth code received: {}", authCode);
        log.warn("Please implement real ThirdPartyLoginService for production!");
        log.warn("=================================================================");

        // 返回模拟数据
        LoginUserInfo info = new LoginUserInfo();
        info.setId(999L);
        info.setAccount(type.getCode() + "_user_" + System.currentTimeMillis());
        info.setPassword("N/A");
        info.setEnable(true);
        info.setRoleIds(new HashSet<>(Collections.singletonList(2L)));

        return info;
    }

    @Override
    public boolean validateAuthCode(String authCode) {
        return authCode != null && !authCode.isEmpty();
    }

    @Override
    public ThirdPartyType getType() {
        return type;
    }
}
