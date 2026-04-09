package com.carlos.boot.application;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * application属性 主配置信息
 *
 * @author carlos
 * @date 2019-08-04
 */
@Data
@ConfigurationProperties(prefix = "carlos.boot")
public class ApplicationProperties {

    /**
     * 项目静态资源访问配置
     */
    private Map<String, String> resourceHandlers;

}
