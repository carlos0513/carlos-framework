package com.carlos.apm.config;

import brave.Tracer;
import com.carlos.apm.aop.TraceTagAspect;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * APM 模块主自动配置类
 * <p>
 * 集成 Micrometer Tracing (Brave) 和 SkyWalking Toolkit，提供分布式追踪能力
 *
 * @author Carlos
 * @date 2024-12-06
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "carlos.apm", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ApmProperties.class)
@Import({
    com.carlos.apm.sleuth.config.SleuthConfig.class,
    com.carlos.apm.skywalking.config.SkywalkingConfig.class,
    com.carlos.apm.mdc.MdcConfig.class,
    com.carlos.apm.config.ApmFeignConfig.class
})
public class ApmConfig {

    @PostConstruct
    public void init() {
        log.info("[Carlos APM] 模块已初始化");
    }

    /**
     * TraceTag 注解切面
     * <p>
     * 提供方法级别的自定义追踪标签支持
     */
    @Bean
    @ConditionalOnClass(Tracer.class)
    @ConditionalOnBean(Tracer.class)
    @ConditionalOnProperty(prefix = "carlos.apm.sleuth", name = "enabled", havingValue = "true", matchIfMissing = true)
    public TraceTagAspect traceTagAspect(Tracer tracer) {
        log.debug("[Carlos APM] TraceTag 切面已注册");
        return new TraceTagAspect(tracer);
    }
}
