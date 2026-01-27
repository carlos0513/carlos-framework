package com.carlos.docking.event.config;

import cn.hutool.json.JSONUtil;
import com.carlos.docking.config.FeignBaseProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 事件配置
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:34
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "carlos.docking.event")
public class EventProperties implements InitializingBean {

    /**
     * 是否开启
     */
    private boolean enabled = false;

    private final FeignBaseProperties api = new FeignBaseProperties();


    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("event config:{}", JSONUtil.toJsonPrettyStr(this));
        }
    }

}
