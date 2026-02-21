package com.yunjin.docking.tftd.config;

import cn.hutool.json.JSONUtil;
import com.yunjin.docking.config.FeignBaseProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 蓉政通属性配置
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:34
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "yunjin.docking.tf.auth")
public class TfAuthProperties implements InitializingBean {

    private boolean enabled = false;

    /**
     * 接口信息
     */
    private FeignBaseProperties api = new FeignBaseProperties();


    private String clientId;

    /**
     * 未加密密码
     */
    private String clientSecret;

    /**
     * 重定向地址
     */
    private String redirectUri;

    /**
     * 租户id
     */
    private String tenantId;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("tfAuth config:{}", JSONUtil.toJsonPrettyStr(this));
        }
        api.check();
    }
}
