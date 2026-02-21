package com.yunjin.docking.jct.config;

import com.yunjin.docking.config.FeignClientCustomBuilder;
import com.yunjin.docking.jct.FeignLJAppAggr;
import com.yunjin.docking.jct.LJAppAggrService;
import com.yunjin.docking.jct.LJAppAggrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 蓉政通配置
 * </p>
 *
 * @author Carlos
 * @date 2022/4/22 10:24
 */
@Configuration
@EnableConfigurationProperties(LJAppAggrProperties.class)
@ConditionalOnProperty(prefix = "yunjin.docking.jct", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class LJAppAggrConfig {

    private final LJAppAggrProperties properties;


    @Bean
    public FeignLJAppAggr feignLJAppAggr() {
        return FeignClientCustomBuilder.getFeignClient(FeignLJAppAggr.class, properties.getApi());
    }

    @Bean
    public LJAppAggrService LJAppAggrAuthService(FeignLJAppAggr feign) {
        return new LJAppAggrService(feign, properties);
    }


    @Bean
    public LJAppAggrUtil LJAppAggrAuthUtil(LJAppAggrService LJAppAggrService) {
        return new LJAppAggrUtil(LJAppAggrService);
    }
}
