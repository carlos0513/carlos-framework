package com.carlos.boot.application;

import cn.hutool.extra.spring.EnableSpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@Configuration(proxyBeanMethods = false)
@EnableSpringUtil
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfig {


}
