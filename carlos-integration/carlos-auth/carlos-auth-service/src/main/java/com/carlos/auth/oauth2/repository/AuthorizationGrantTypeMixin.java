package com.carlos.auth.oauth2.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

// 定义 Mixin 类解决 AuthorizationGrantType 反序列化
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonDeserialize(using = AuthorizationGrantTypeDeserializer.class)
public abstract class AuthorizationGrantTypeMixin {
    @JsonCreator
    public static AuthorizationGrantType create(@JsonProperty("value") String value) {
        return new AuthorizationGrantType(value);
    }
}
