package com.carlos.datacenter.config;


import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 数据平台对接配置
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "yunjin.docking.datacenter")
public class DatacenterProperties implements InitializingBean {


    /** 数据平台实例 */
    private Map<String, Map<String, Object>> instances;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("datacenter config:{}", JSONUtil.toJsonPrettyStr(this));
        }
    }
}
