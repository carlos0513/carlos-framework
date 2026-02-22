package com.carlos.gateway.config;

import com.carlos.gateway.auth.MenuApiMapping;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p>
 * 网关相关配置
 * </p>
 *
 * @author carlos
 * @date 2022/4/13 16:00
 */

@Data
@ConfigurationProperties(prefix = "carlos.gateway")
public class GatewayProperties implements InitializingBean {

    private String prefix = "/bbt-api";

    private boolean roleCheck = false;

    private List<MenuApiMapping> mappings;


    @Override
    public void afterPropertiesSet() {

    }


}
