package com.carlos.json.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <p>
 *   json主配置
 * </p>
 *
 * @author Carlos
 * @date 2026-03-24 13:47
 */
@AutoConfiguration
@EnableConfigurationProperties(JsonProperties.class)
public class JsonAutoConfig {
}
