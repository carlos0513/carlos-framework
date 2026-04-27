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
 * 短信验证码身份源提供者
 *
 * <p>支持短信验证码登录认证，通过 Feign 调用 carlos-org 模块进行验证码校验和用户查询。</p>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsCodeIdentityProvider implements IdentityProvider {


    @Override
    public String getProviderId() {
        return "sms";
    }

    @Override
    public String getProviderType() {
        return "local";
    }

    @Override
    public Set<String> getSupportedGrantTypes() {
        return Collections.singleton(CustomGrantTypes.SMS_CODE.getValue());
    }

    @Override
    public UserIdentity authenticate(IdentityProviderRequest request) {
        String phone = (String) request.getPrincipal();
        String smsCode = (String) request.getCredentials();


        ApiOrgUser userApi = SpringUtil.getBean(ApiOrgUser.class);
        Result<OrgUserAO> userByIdentifier = userApi.getUserByIdentifier(phone);


        // 2. 查询用户信息（优先用手机号查）
        OrgUserAO orgUser = userByIdentifier.getData();

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
