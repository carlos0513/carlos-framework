package com.carlos.auth.idp;

import cn.hutool.extra.spring.SpringUtil;
import com.carlos.auth.oauth2.grant.CustomGrantTypes;
import com.carlos.core.response.Result;
import com.carlos.org.api.ApiOrgUser;
import com.carlos.org.api.pojo.ao.OrgUserAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * 本地身份源提供者
 *
 * <p>支持用户名密码登录认证，通过 Feign 调用 carlos-org 模块进行用户校验。</p>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalIdentityProvider implements IdentityProvider {


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
        Result<OrgUserAO> userByIdentifier = userApi.getUserByIdentifier(username);


        // 2. 查询用户信息（优先用手机号查）
        OrgUserAO orgUser = userByIdentifier.getData();

        // 1. 校验密码


        // 3. 构建统一用户身份
        return convertToUserIdentity(orgUser);
    }


    private UserIdentity convertToUserIdentity(OrgUserAO user) {
        return UserIdentity.builder()
            .providerId(getProviderId())
            .providerUserId(String.valueOf(user.getId()))
            .username(user.getAccount())
            .email(user.getEmail())
            .phone(user.getPhone())
            .roleCodes(Collections.emptySet()) // TODO: 从 org 查询角色
            .isNewUser(false)
            .build();
    }
}
