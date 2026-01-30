package com.carlos.sms.config;

import cn.hutool.json.JSONUtil;
import com.carlos.sms.template.SmsTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * <p>
 * minio配置项
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 13:21
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "carlos.sms")
public class SmsProperties implements InitializingBean {


    /**
     * 模板列表
     */
    private Map<String, SmsTemplate> templates;

    @Override
    public void afterPropertiesSet() {
        if (log.isDebugEnabled()) {
            log.debug("sms properties:{}", JSONUtil.toJsonPrettyStr(this));
        }
    }
}
