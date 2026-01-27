package com.carlos.boot.application;


import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * application属性 主配置信息
 *
 * @author yunjin
 * @date 2019-08-04
 */
@Data
@ConfigurationProperties(prefix = "carlos.boot")
public class ApplicationProperties {

    /**
     * 项目静态资源访问配置
     */
    private Map<String, String> resourceHandlers;

    /**
     * 项目信息
     */
    @NestedConfigurationProperty
    private final ApplicationInfoProperties info = new ApplicationInfoProperties();
}
