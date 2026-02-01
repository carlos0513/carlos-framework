package com.carlos.boot.enums;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * <p>
 * 枚举相关配置
 * </p>
 *
 * @author carlos
 * @date 2021/10/9 11:45
 */
@Data
@ConfigurationProperties(prefix = "carlos.boot.enums")
public class ApplicationEnumProperties {

    /**
     * 枚举功能是否开启 默认关闭
     */
    private boolean enabled = false;

    /**
     * 扫描的包
     */
    private Set<String> scanPackage;


}
