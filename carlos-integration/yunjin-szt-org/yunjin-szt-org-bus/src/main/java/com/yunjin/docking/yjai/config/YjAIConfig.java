package com.yunjin.docking.yjai.config;

import com.yunjin.docking.config.FeignClientCustomBuilder;
import com.yunjin.docking.yjai.FeignYjAI;
import com.yunjin.docking.yjai.YjAIService;
import com.yunjin.docking.yjai.YjAIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 元景大模型配置
 * </p>
 *
 * @author Carlos
 * @date 2022/4/22 10:24
 */
@Configuration
@EnableConfigurationProperties(YjAIProperties.class)
@ConditionalOnProperty(prefix = "yunjin.docking.yjai", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class YjAIConfig {

    private final YjAIProperties properties;


    /**
     * 接口连接工具配置
     */
    @Bean
    public FeignYjAI feignYjAI() {
        return FeignClientCustomBuilder.getFeignClient(FeignYjAI.class, properties.getApi());
    }

    @Bean
    public YjAIService yjAIService(FeignYjAI feignYjAI) {
        return new YjAIService(feignYjAI, properties);
    }


    @Bean
    public YjAIUtil yjAIUtil(YjAIService yjAIService) {
        return new YjAIUtil(yjAIService);
    }


}
