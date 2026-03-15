package com.carlos.redis.config;

import cn.hutool.core.util.StrUtil;
import com.carlos.redis.serialize.SerializerType;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * <p>
 * 缓存配置项
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 13:21
 */
@Data
@ConfigurationProperties(prefix = "carlos.cache")
public class CacheProperties implements InitializingBean {

    /**
     * 是否使用统一key前缀 默认关闭
     */
    private boolean usePrefix = false;

    /**
     * 缓存前缀
     */
    private String keyPrefix;

    /**
     * 序列化类型：jackson、fastjson、kryo、jdk
     * 默认 jackson
     */
    private SerializerType serializer = SerializerType.JACKSON;

    /**
     * Key 生成器最大长度限制（防止超长 key）
     * 默认 256 字符
     */
    private int keyMaxLength = 256;

    /**
     * Key 过长时的摘要算法：md5、sha1、truncate
     * 默认 truncate（截断）
     */
    private String keyOverflowStrategy = "truncate";

    @Override
    public void afterPropertiesSet() {
        if (usePrefix) {
            Assert.hasText(this.keyPrefix,
                " When 'carlos.cache.use-prefix' is true', 'carlos.cache.key-prefix' can't be blank.");
            if (!keyPrefix.endsWith(StrUtil.COLON)) {
                keyPrefix = keyPrefix + StrUtil.COLON;
            }
        }
    }
}
