package com.carlos.redis.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;

/**
 * <p>
 * 带有前缀的key序列化
 * </p>
 *
 * @author carlos
 * @date 2021/11/26 15:19
 */
@Slf4j
@AllArgsConstructor
public class PrefixKeySerializer extends StringRedisSerializer {

    private final CacheProperties cacheProperties;

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        String key = super.deserialize(bytes);
        if (!cacheProperties.isUserPrefix()) {
            return key;
        }
        if (StringUtils.isNotBlank(key)) {
            key = key.replaceFirst(cacheProperties.getKeyPrefix(), StringUtils.EMPTY);
            return key;
        }
        return key;
    }


    @Override
    public byte[] serialize(@Nullable String string) {
        if (!cacheProperties.isUserPrefix()) {
            return super.serialize(string);
        }
        String keyPrefix = cacheProperties.getKeyPrefix();
        String newKey = string;
        if (!string.startsWith(keyPrefix)) {
            newKey = keyPrefix + string;
        }
        return super.serialize(newKey);
    }
}
