package com.carlos.docking.event.config;

import com.carlos.docking.config.FeignClientCustomBuilder;
import com.carlos.docking.event.EventService;
import com.carlos.docking.event.EventUtil;
import com.carlos.docking.event.FeignEvent;
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
@EnableConfigurationProperties(EventProperties.class)
@ConditionalOnProperty(prefix = "yunjin.docking.event", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class EventConfig {

    private final EventProperties properties;


    @Bean
    public FeignEvent feignEvent() {
        return FeignClientCustomBuilder.getFeignClient(FeignEvent.class, properties.getApi());
    }


    @Bean
    public EventService eventService(FeignEvent feignEvent) {
        return new EventService(feignEvent, properties);
    }

    @Bean
    public EventUtil eventUtil(EventService service) {
        return new EventUtil(service);
    }


}
