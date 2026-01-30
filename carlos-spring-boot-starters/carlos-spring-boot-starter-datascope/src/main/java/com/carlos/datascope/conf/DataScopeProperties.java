package com.carlos.datascope.conf;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 数据权限配置项
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 13:21
 */
@Data
@ConfigurationProperties(prefix = "carlos.datascope")
public class DataScopeProperties implements InitializingBean {

    /**
     * 是否启用 默认弃用
     */
    private boolean enabled = false;


    @Override
    public void afterPropertiesSet() {
        if (enabled) {

        }
    }

}
