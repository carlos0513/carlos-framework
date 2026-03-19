package com.carlos.log.config;

import com.carlos.log.aspect.LogAspect;
import com.carlos.log.disruptor.LogEventProducer;
import com.carlos.log.storage.CompositeLogStorage;
import com.carlos.log.storage.LogStorage;
import com.carlos.log.storage.LoggingLogStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志模块自动配置
 * <p>
 * 核心配置：LogStorage、LogAspect
 * 异步配置：通过 {@link LogAsyncConfig} 导入
 *
 * @author carlos
 * @since 3.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties(com.carlos.log.properties.LogProperties.class)
@ConditionalOnProperty(prefix = "carlos.log", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(LogAsyncConfig.class)
public class LogConfig {

    /**
     * 日志切面
     * 使用 ObjectProvider 使 LogEventProducer 变为可选依赖
     * 同步模式下 LogEventProducer 不存在，直接通过 LogStorage 存储
     */
    @Bean
    @ConditionalOnMissingBean(LogAspect.class)
    public LogAspect logAspect(ObjectProvider<LogEventProducer> eventProducer, LogStorage logStorage) {
        return new LogAspect(eventProducer, logStorage);
    }

    /**
     * 日志存储器
     * 如果应用没有自定义存储器，使用默认的 LoggingLogStorage
     */
    @Bean
    @ConditionalOnMissingBean(LogStorage.class)
    public LogStorage logStorage(ObjectProvider<LogStorage> logStorages) {
        List<LogStorage> storages = new ArrayList<>();

        // 获取所有自定义存储器
        for (LogStorage storage : logStorages) {
            storages.add(storage);
        }

        if (storages.isEmpty()) {
            // 没有自定义存储器，使用默认的日志存储
            log.info("未检测到自定义 LogStorage，使用默认的 LoggingLogStorage");
            return new LoggingLogStorage();
        }

        if (storages.size() == 1) {
            log.info("检测到单个 LogStorage: {}", storages.get(0).getName());
            return storages.get(0);
        }

        // 多个存储器，使用组合存储
        log.info("检测到多个 LogStorage ({}个)，使用组合存储", storages.size());
        return new CompositeLogStorage(storages);
    }
}
