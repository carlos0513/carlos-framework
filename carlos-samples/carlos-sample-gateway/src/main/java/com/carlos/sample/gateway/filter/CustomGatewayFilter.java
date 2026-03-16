package com.carlos.sample.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义网关过滤器示例
 *
 * <p>
 * 演示如何创建自定义的 GatewayFilter 工厂类。
 * 该过滤器可以在请求头中添加自定义参数，并记录请求日志。
 * </p>
 *
 * <p>
 * 使用方式（在 application.yml 中配置）：
 * <pre>
 * filters:
 *   - name: CustomGatewayFilter
 *     args:
 *       paramName: X-Custom-Header
 *       paramValue: CustomValue
 * </pre>
 * </p>
 *
 * @author carlos
 */
@Slf4j
@Component
public class CustomGatewayFilter extends AbstractGatewayFilterFactory<CustomGatewayFilter.Config> {

    /**
     * 配置参数名常量
     */
    private static final String PARAM_NAME_KEY = "paramName";
    private static final String PARAM_VALUE_KEY = "paramValue";

    public CustomGatewayFilter() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(PARAM_NAME_KEY, PARAM_VALUE_KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 记录请求日志
            log.info("[CustomGatewayFilter] 处理请求: {} {}",
                request.getMethod(),
                request.getURI().getPath());
            log.info("[CustomGatewayFilter] 添加自定义请求头: {} = {}",
                config.getParamName(),
                config.getParamValue());

            // 添加自定义请求头
            ServerHttpRequest modifiedRequest = request.mutate()
                .header(config.getParamName(), config.getParamValue())
                .header("X-Filter-Applied", "CustomGatewayFilter")
                .build();

            // 继续处理过滤器链
            return chain.filter(exchange.mutate()
                .request(modifiedRequest)
                .build());
        };
    }

    /**
     * 过滤器配置类
     */
    @Data
    public static class Config {
        /**
         * 参数名称
         */
        private String paramName;

        /**
         * 参数值
         */
        private String paramValue;
    }

}
