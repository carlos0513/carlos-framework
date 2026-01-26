package com.yunjin.docking.linkage.config;

import com.yunjin.docking.config.FeignClientCustomBuilder;
import com.yunjin.docking.linkage.BigLinkAgeService;
import com.yunjin.docking.linkage.BigLinkAgeUtil;
import com.yunjin.docking.linkage.FeignBigLinkAge;
import com.yunjin.docking.linkage.FeignChqBigLinkAge;
import com.yunjin.docking.linkage.FeignPdqBigLinkAge;
import com.yunjin.docking.linkage.config.BigLinkAgeProperties.ServerTag;
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
@EnableConfigurationProperties(BigLinkAgeProperties.class)
@ConditionalOnProperty(prefix = "yunjin.docking.linkage", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class BigLinkAgeConfig {

    private final BigLinkAgeProperties properties;


    @Bean
    public FeignBigLinkAge feignBigLinkAge() {
        ServerTag tag = properties.getTag();
        Class<? extends FeignBigLinkAge> clazz;
        switch (tag) {
            case PDQ:
                clazz = FeignPdqBigLinkAge.class;
                break;
            case CHQ:
                clazz = FeignChqBigLinkAge.class;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + tag);
        }
        return FeignClientCustomBuilder.getFeignClient(clazz, properties.getApi());
    }


    @Bean
    public BigLinkAgeService bigLinkAgeService(FeignBigLinkAge feignBigLinkAge) {
        return new BigLinkAgeService(feignBigLinkAge, properties);
    }

    @Bean
    public BigLinkAgeUtil bigLinkAgeUtil(BigLinkAgeService service) {
        return new BigLinkAgeUtil(service);
    }


}
