package com.carlos.org.config;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * <p>
 * 蓉政通属性配置
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:34
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "carlos.org")
public class OrgProperties implements InitializingBean {


    private String defaultPassword;

    /**
     * 密码过期时间
     */
    private Duration passwordExpire = Duration.ofDays(90);

    /**
     * 登录属性
     */
    private OrgLoginProperties login = new OrgLoginProperties();


    /**
     * 登录属性
     */
    @Data
    public static class OrgLoginProperties {

        /**
         * 计数最大间隔时间  单位：分钟
         */
        private Long countIntervalTime = 24 * 60L;

        /**
         * 登录失败限制次数  单位：次
         */
        private Long failLimit = 5L;

        /**
         * 锁定时长 单位：分钟
         */
        private Long lockTime = 5L;

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("org config:{}", JSONUtil.toJsonPrettyStr(this));
        }
    }
}
