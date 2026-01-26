package com.yunjin.log.config;


import com.yunjin.log.aspect.LogAspect;
import com.yunjin.log.service.OperationLogService;
import com.yunjin.log.service.impl.DefaultOperationLogServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * MP配置
 *
 * @author yunjin
 */
@Configuration(proxyBeanMethods = false)
public class LogConfig {

    @Bean
    public LogAspect aspect(OperationLogService operationLogService) {
        return new LogAspect(operationLogService);
    }

    @Bean
    @ConditionalOnMissingBean(OperationLogService.class)
    public OperationLogService operationLogService() {
        return new DefaultOperationLogServiceImpl();
    }
}
