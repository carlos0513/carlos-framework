package com.carlos.auth.oauth2.customize;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.IOException;

public class AuthorizationGrantTypeDeserializer
    extends JsonDeserializer<AuthorizationGrantType> {

    @Override
    public AuthorizationGrantType deserialize(
        JsonParser jsonParser,
        DeserializationContext context
    ) throws IOException {
        // 从JSON中读取授权类型字符串
        String grantTypeValue = jsonParser.getValueAsString();
        if (StrUtil.isBlank(grantTypeValue)) {
            return null;
        }

        // 匹配标准授权类型
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(grantTypeValue)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(grantTypeValue)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(grantTypeValue)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }

        // 处理自定义授权类型（如设备码流程）
        return new AuthorizationGrantType(grantTypeValue);
    }
}
