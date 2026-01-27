package com.carlos.redis.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * <p>
 * minio配置项
 * </p>
 *
 * @author yunjin
 * @date 2021/6/10 13:21
 */
@Data
@ConfigurationProperties(prefix = "carlos.cache")
public class CacheProperties implements InitializingBean {

    /**
     * 是否使用统一key前缀 默认关闭
     */
    private boolean userPrefix = false;

    /**
     * 缓存前缀
     */
    private String keyPrefix;


    @Override
    public void afterPropertiesSet() {
        if (userPrefix) {

            Assert.hasText(this.keyPrefix, " When 'yunjin.cache.use_prefix' is true', 'yunjin.cache.key_prefix' " +
                    "can't be blank.");
            if (!keyPrefix.endsWith(StrUtil.COLON)) {
                keyPrefix = keyPrefix + StrUtil.COLON;
            }
        }
    }

}
