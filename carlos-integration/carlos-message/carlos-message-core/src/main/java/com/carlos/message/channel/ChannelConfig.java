package com.carlos.message.channel;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * <p>
 * 渠道配置
 * </p>
 *
 * @author Carlos
 * @date 2025-05-06 23:57
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "contentType", visible = true)
@JsonSubTypes({
        // @JsonSubTypes.Type(value = RoleUser.class),
        // @JsonSubTypes.Type(value = TokenUser.class),
})
public interface ChannelConfig {


}
