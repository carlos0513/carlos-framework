package com.carlos.boot.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 应用配置
 * </p>
 *
 * @author yunjin
 * @date 2020/9/23 23:39
 */
@Slf4j
@Configuration
public class ApplicationRunnerConfig {

    @Bean
    public ApplicationRunner springbootRunner() {
        return new ApplicationRunnerWorker();
    }

}
