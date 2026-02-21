package com.carlos.gateway.filter;

import com.google.common.collect.Sets;
import com.carlos.core.util.PathMatchUtil;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * <p>
 * 选择性地址截取   /xxx/doc.html ->  /doc.html; /xxx/application   -> /application ; /xxx/abc  -> /xxx/abc
 * </p>
 *
 * @author Carlos
 * @date 2024/1/24 8:58
 */
public class SelectStripPrefixGatewayFilterFactory
        extends AbstractGatewayFilterFactory<SelectStripPrefixGatewayFilterFactory.Config> {

    /**
     * Parts key.
     */
    public static final String PARTS_KEY = "parts";


    /**
     * 通用忽略过滤路径
     */
    Set<String> PATTENS = Sets.newHashSet(
            "/**/swagger-ui.html",
            "/**/doc.html",
            "/**/favicon.ico",
            "/**/swagger-resources",
            "/**/v2/api-docs/**",
            "/**/v3/api-docs",
            "/**/webjars/**",
            "/**/csrf",
            "/**/v2/api-docs-ext",
            "/**/v3/api-docs-ext"
    );

    public SelectStripPrefixGatewayFilterFactory() {
        super(SelectStripPrefixGatewayFilterFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(PARTS_KEY);
    }

    @Override
    public GatewayFilter apply(SelectStripPrefixGatewayFilterFactory.Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                addOriginalRequestUrl(exchange, request.getURI());
                String path = request.getURI().getRawPath();

                // checkStripPath  检查路径是否应该被截取

                if (!checkStripPath(path)) {
                    // 不对请求做任何处理
                    return chain.filter(exchange);
                }


                String[] originalParts = StringUtils.tokenizeToStringArray(path, "/");

                // all new paths start with /
                StringBuilder newPath = new StringBuilder("/");
                for (int i = 0; i < originalParts.length; i++) {
                    if (i >= config.getParts()) {
                        // only append slash if this is the second part or greater
                        if (newPath.length() > 1) {
                            newPath.append('/');
                        }
                        newPath.append(originalParts[i]);
                    }
                }
                if (newPath.length() > 1 && path.endsWith("/")) {
                    newPath.append('/');
                }

                ServerHttpRequest newRequest = request.mutate().path(newPath.toString()).build();

                exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

                return chain.filter(exchange.mutate().request(newRequest).build());
            }

            @Override
            public String toString() {
                return filterToStringCreator(SelectStripPrefixGatewayFilterFactory.this).append("parts",
                                config.getParts())
                        .toString();
            }
        };
    }

    private boolean checkStripPath(String path) {
        boolean match = PathMatchUtil.antMatch(PATTENS, path);
        return match;
    }


    @Data
    public static class Config {

        /**
         * 路径截取长度
         */
        private int parts = 1;


    }

}
