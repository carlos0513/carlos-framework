package com.yunjin.docking.tftd.config;

import com.yunjin.docking.config.FeignClientCustomBuilder;
import com.yunjin.docking.tftd.FeignTfAuth;
import com.yunjin.docking.tftd.TfAuthService;
import com.yunjin.docking.tftd.TfAuthUtil;
import feign.Client;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 蓉政通配置
 * </p>
 *
 * @author Carlos
 * @date 2022/4/22 10:24
 */
@Configuration
@EnableConfigurationProperties(TfAuthProperties.class)
@ConditionalOnProperty(prefix = "yunjin.docking.tf.auth", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class TfAuthConfig {

    private final TfAuthProperties properties;


    /**
     * 接口连接工具配置
     */
    @Bean
    public FeignTfAuth feignTfAuth() {
        return FeignClientCustomBuilder.getFeignClient(FeignTfAuth.class, properties.getApi());
    }

    @Bean
    public TfAuthService tfAuthService(FeignTfAuth feignTfAuth) {
        return new TfAuthService(feignTfAuth, properties);
    }


    @Bean
    public TfAuthUtil tfAuthUtil(TfAuthService tfAuthService) {
        return new TfAuthUtil(tfAuthService);
    }

    @Bean
    @ConditionalOnMissingBean
    public Client feignClient() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);

        return new Client.Default(ctx.getSocketFactory(),
                (hostname, session) -> true);
    }


}
