package com.yunjin.boot.resource;


import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 资源相关配置
 * </p>
 *
 * @author yunjin
 * @date 2021/10/9 11:45
 */
@Data
@ConfigurationProperties(prefix = "yunjin.boot.resource")
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
