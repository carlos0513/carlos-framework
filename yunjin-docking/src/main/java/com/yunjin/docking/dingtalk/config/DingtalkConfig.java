package com.yunjin.docking.dingtalk.config;

import com.dingtalk.api.DingTalkClient;
import com.yunjin.docking.dingtalk.DingtalkService;
import com.yunjin.docking.dingtalk.DingtalkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 钉钉配置
 * </p>
 *
 * @author Carlos
 * @date 2022/4/22 10:24
 */
@Configuration
@EnableConfigurationProperties(DingtalkProperties.class)
@ConditionalOnClass(DingTalkClient.class)
@ConditionalOnProperty(prefix = "yunjin.docking.dingtalk", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class DingtalkConfig {

    private final DingtalkProperties properties;


    /**
     * token管理器
     */
    @Bean
    public DingtalkAccessTokenManager dingtalkAccessTokenManager(DingtalkProperties properties) {
        return new DingtalkAccessTokenManager(properties);
    }

    @Bean
    public DingtalkService dingtalkService(DingtalkAccessTokenManager accessTokenManager) {
        return new DingtalkService(accessTokenManager, properties);
    }

    @Bean
    public DingtalkUtil dingtalkUtil(DingtalkService rztService) {
        return new DingtalkUtil(rztService);
    }


    /**
     * token初始化进程
     */
    @Bean
    public ApplicationRunner dingtalkAccessTokenRunner(DingtalkAccessTokenManager tokenManager) {
        return new DingtalkInitWorker(tokenManager, properties.getAccessTokenRefreshCorn());
    }

}


