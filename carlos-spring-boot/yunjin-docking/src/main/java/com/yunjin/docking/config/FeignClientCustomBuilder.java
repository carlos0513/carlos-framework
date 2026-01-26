package com.yunjin.docking.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.cloud.openfeign.FeignClientBuilder;

/**
 * <p>
 * 自定义feignclient
 * </p>
 *
 * @author Carlos
 * @date 2022/4/28 14:01
 */
public class FeignClientCustomBuilder {

    private static final FeignClientBuilder BUILDER = new FeignClientBuilder(SpringUtil.getApplicationContext());

    /**
     * 手动生成FeignClient,准备一个FeignClient基类，该类不用打{@link org.springframework.cloud.openfeign.FeignClient}注解
     *
     * @param clazz      client Class
     * @param properties 服务属性
     * @return T
     * @author Carlos
     * @date 2022/4/28 13:55
     */
    public static <T> T getFeignClient(Class<T> clazz, FeignBaseProperties properties) {
        FeignClientBuilder.Builder<T> client = BUILDER.forType(clazz, properties.getName());
        if (StrUtil.isNotBlank(properties.getHost())) {
            client.url(properties.getHost());
        }
        if (StrUtil.isNotBlank(properties.getPath())) {
            client.path(properties.getPath());
        }
        if (StrUtil.isNotBlank(properties.getContextId())) {
            client.contextId(properties.getContextId());
        }
        return client.build();
    }

}
