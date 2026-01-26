package com.yunjin.datacenter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;


/**
 * <p>
 * 数据平台对接主配置
 * </p>
 *
 * @author Carlos
 * @date 2024-10-09 22:25
 */

@Configuration
@EnableConfigurationProperties(DatacenterProperties.class)
public class DatacenterMainConfig {
    // TODO: Carlos 2024-12-13 此处写数据平台组件公共属性配置，如线程池等

    @EventListener
    void init(ContextRefreshedEvent event) {

    }

}
