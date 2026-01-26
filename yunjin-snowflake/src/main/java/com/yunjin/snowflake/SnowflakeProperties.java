package com.yunjin.snowflake;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.core.exception.ServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * <p>
 * 雪花算法属性配置
 * </p>
 *
 * @author yunjin
 * @date 2020/10/25 23:45
 */
@Slf4j
@Data
@ConfigurationProperties("yunjin.snowflake")
public class SnowflakeProperties implements InitializingBean {

    /**
     * 当前服务标签
     */
    private String tag;

    /**
     * 缓存空间
     */
    private String namespace = SnowflakeConstant.NAMESPACE;

    /**
     * 缓存过期时间 默认24小时
     */
    private Duration redisExpire = Duration.ofHours(24);


    /**
     * 是否使用{@link SystemClock} 获取当前时间戳
     */
    private boolean isUseSystemClock = false;

    @Override
    public void afterPropertiesSet() {
        if (tag == null) {
            tag = SpringUtil.getProperty("spring.application.name");
            if (StrUtil.isBlank(tag)) {
                log.error("snowflake tag is null, please check your application.yml, set 'spring.application.name' or 'yunjin.snowflake.tag'");
                throw new ServiceException("snowflake tag is null, please check your application.yml, set 'spring.application.name' or 'yunjin.snowflake.tag'");
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Snowflake properties:{}", this);
        }
    }
}
