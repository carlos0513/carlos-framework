package com.yunjin.docking.suining.config;

import cn.hutool.json.JSONUtil;
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
@ConfigurationProperties(prefix = "yunjin.docking.suining")
public class SuiningAuthProperties implements InitializingBean {

    private boolean enabled = false;


    private String baseUrl;

    /**
     *
     */
    private String appId;

    /**
     *
     */
    private String privateKey;

    /**
     *
     */
    private String publicKey;

    /** http连接超时 */
    private int connectTimeout = 25000;
    /** http读超时 */
    private int readTimeout = 25000;

    /**
     *
     */
    private final Sm4Properties sm4 = new Sm4Properties();


    /**
     *
     */
    @Data
    public static class Sm4Properties {

        /**
         * 秘钥
         */
        private String key;

        /**
         * 填充方式
         */
        private String padding = "PKCS7Padding";

        /**
         * 模式
         */
        private String mode = "ECB";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SuiningAuth config:{}", JSONUtil.toJsonPrettyStr(this));
        }
    }
}
