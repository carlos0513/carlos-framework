package com.carlos.auth.idp;

import cn.hutool.extra.spring.SpringUtil;
import com.carlos.auth.oauth2.grant.CustomGrantTypes;
import com.carlos.core.response.Result;
import com.carlos.org.api.ApiOrgUser;
import com.carlos.org.api.pojo.ao.OrgUserAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * 本地身份源提供者
 *
 * <p>支持用户名密码登录认证。从 carlos-org 获取用户信息，密码校验统一在 auth 模块完成。</p>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalIdentityProvider implements IdentityProvider {

    private final PasswordEncoder passwordEncoder;

    private final RoleProvider roleProvider;

    @Override
    public String getProviderId() {
        return "local";
    }

    @Override
    public String getProviderType() {
        return "local";
    }

    @Override
    public Set<String> getSupportedGrantTypes() {
        return Collections.singleton(CustomGrantTypes.PASSWORD.getValue());
    }

    @Override
    public UserIdentity authenticate(IdentityProviderRequest request) {
        String username = (String) request.getPrincipal();
        String password = (String) request.getCredentials();

        ApiOrgUser userApi = SpringUtil.getBean(ApiOrgUser.class);
        Result<OrgUserAO> userResult = userApi.getUserByIdentifier(username);

        if (!userResult.isSuccess() || userResult.getData() == null) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        OrgUserAO orgUser = userResult.getData();
        String encodedPwd = orgUser.getPwd();

        // 密码校验统一由 auth 模块处理
        boolean matched;
        if (encodedPwd == null) {
            matched = false;
        } else if (isPlainTextPassword(encodedPwd)) {
            // 兼容过渡期明文密码（建议后续统一由 auth 加密后存储）
            log.warn("User [{}] password is stored in plaintext, please migrate to encoded password", username);
            matched = password.equals(encodedPwd);
        } else {
            matched = passwordEncoder.matches(password, encodedPwd);
        }

        if (!matched) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        return convertToUserIdentity(orgUser);
    }

    /**
     * 简单判断密码是否为明文（未使用标准加密前缀）
     */
    private boolean isPlainTextPassword(String pwd) {
        // BCrypt 以 $2 开头，SM4/其他加密通常也会有特定前缀
        return !pwd.startsWith("$") && pwd.length() < 64;
    }

    private UserIdentity convertToUserIdentity(OrgUserAO user) {
        // 从角色提供者查询用户角色
        Set<String> roleCodes = roleProvider != null
            ? roleProvider.getRoleCodesByUserId(user.getId())
            : Collections.emptySet();

        return UserIdentity.builder()
            .providerId(getProviderId())
            .providerUserId(String.valueOf(user.getId()))
            .username(user.getAccount())
            .email(user.getEmail())
            .phone(user.getPhone())
            .roleCodes(roleCodes)
            .isNewUser(false)
            .build();
    }
}
