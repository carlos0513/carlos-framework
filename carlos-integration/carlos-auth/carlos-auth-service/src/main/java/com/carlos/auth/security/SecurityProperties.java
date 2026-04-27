package com.carlos.auth.security;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

/**
 * 安全配置属性
 *
 * <p>密码加密、登录限制等安全相关配置。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 */
@Data
public class SecurityProperties {

    /**
     * 密码编码器类型
     */
    private String passwordEncoder = "bcrypt";

    /**
     * 登录失败限制
     */
    @NestedConfigurationProperty
    private LoginLimitProperties loginLimit = new LoginLimitProperties();

    /**
     * 登录限制配置
     */
    @Data
    public static class LoginLimitProperties {

        /**
         * 是否启用登录限制
         */
        private boolean enabled = true;

        /**
         * 最大失败次数
         */
        private int maxAttempts = 5;

        /**
         * 锁定时间
         */
        private Duration lockDuration = Duration.ofMinutes(30);
    }

}
