package com.carlos.boot.resource;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * <p>
 * 资源相关配置
 * </p>
 *
 * @author carlos
 * @date 2021/10/9 11:45
 */
@Data
@ConfigurationProperties(prefix = "carlos.boot.resource")
public class ApplicationResourceProperties {

    /**
     * 系统名称
     */
    private String appName;

    /**
     * 前缀
     */
    private String prefix;
    /**
     * 扫描的包
     */
    private Set<String> scanPackage;


}
