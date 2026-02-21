package com.yunjin.docking.yjai.config;

import cn.hutool.json.JSONUtil;
import com.yunjin.docking.config.FeignBaseProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 元景大模型属性配置
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:34
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "yunjin.docking.yjai")
public class YjAIProperties implements InitializingBean {

    private boolean enabled = false;

    /**
     * 接口信息
     */
    private FeignBaseProperties api = new FeignBaseProperties();

    /**
     * 通道id
     */
    private String channelId;

    /**
     * 秘钥
     */
    private String secretKey;

    /**
     * agentId
     */
    private String agentId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 注册ip
     */
    private String registerIp;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("tfAuth config:{}", JSONUtil.toJsonPrettyStr(this));
        }
        api.check();
    }
}
