package com.yunjin.docking.linkage.config;

import cn.hutool.json.JSONUtil;
import com.yunjin.docking.config.FeignBaseProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 大联动配置
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:34
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "yunjin.docking.linkage")
public class BigLinkAgeProperties implements InitializingBean {

    /**
     * 是否开启
     */
    private boolean enabled = false;

    private final FeignBaseProperties api = new FeignBaseProperties();

    /**
     * 服务标识
     */
    private ServerTag tag;

    /**
     * appcode
     */
    private String appCode;

    /**
     * 私钥
     */
    private String privateKey;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("linkage config:{}", JSONUtil.toJsonPrettyStr(this));
        }
    }

    public enum ServerTag {
        PDQ,
        CHQ
    }
}
