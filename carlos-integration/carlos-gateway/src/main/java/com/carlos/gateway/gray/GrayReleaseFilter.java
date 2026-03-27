package com.carlos.gateway.gray;

import com.carlos.core.constant.HttpHeadersConstant;
import com.carlos.gateway.constant.GatewayHeaderConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 灰度发布过滤器
 * 支持基于用户、IP、权重的灰度策略
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
public class GrayReleaseFilter implements GlobalFilter, Ordered {

    private final GrayReleaseProperties properties;
    private final ReactiveDiscoveryClient discoveryClient;

    // 灰度策略缓存
    private final ConcurrentHashMap<String, GrayStrategy> strategyCache = new ConcurrentHashMap<>();

    public GrayReleaseFilter(GrayReleaseProperties properties,
                             ReactiveDiscoveryClient discoveryClient) {
        this.properties = properties;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        String serviceName = exchange.getAttribute(
            ServerWebExchangeUtils.GATEWAY_PREDICATE_ROUTE_ATTR);
        if (serviceName == null) {
            return chain.filter(exchange);
        }

        GrayStrategy strategy = getGrayStrategy(serviceName);
        if (strategy == null || !strategy.isEnabled()) {
            return chain.filter(exchange);
        }

        // 判断是否是灰度用户
        boolean isGrayUser = isGrayUser(exchange, strategy);

        // 设置灰度标记
        exchange.getAttributes().put(GatewayHeaderConstants.GRAY_RELEASE_ATTR, isGrayUser);

        if (isGrayUser) {
            log.debug("Gray release enabled for service: {}, request: {}",
                serviceName, exchange.getRequest().getURI());

            // 添加灰度标记头
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(GatewayHeaderConstants.X_GRAY_RELEASE, "true")
                .build();
            ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

            // 选择灰度实例
            return selectGrayInstance(serviceName, strategy)
                .flatMap(instance -> {
                    if (instance != null) {
                        URI uri = URI.create("http://" + instance.getHost() + ":" + instance.getPort());
                        mutatedExchange.getAttributes().put(
                            ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, uri);
                    }
                    return chain.filter(mutatedExchange);
                })
                .switchIfEmpty(chain.filter(mutatedExchange));
        }

        return chain.filter(exchange);
    }

    /**
     * 判断是否是灰度用户
     */
    private boolean isGrayUser(ServerWebExchange exchange, GrayStrategy strategy) {
        ServerHttpRequest request = exchange.getRequest();

        // 1. 基于权重的灰度
        if (strategy.getWeight() > 0) {
            int hash = hashRequest(request, strategy);
            return (hash % 100) < strategy.getWeight();
        }

        // 2. 基于用户的灰度
        String userId = request.getHeaders().getFirst(HttpHeadersConstant.X_USER_ID);
        if (userId != null && strategy.getUserIds() != null) {
            return strategy.getUserIds().contains(userId);
        }

        // 3. 基于 IP 的灰度
        String ip = request.getRemoteAddress() != null
            ? request.getRemoteAddress().getAddress().getHostAddress()
            : null;
        if (ip != null && strategy.getIpRanges() != null) {
            return strategy.getIpRanges().stream().anyMatch(ip::startsWith);
        }

        // 4. 基于请求头的灰度
        if (strategy.getHeaders() != null) {
            for (Map.Entry<String, String> entry : strategy.getHeaders().entrySet()) {
                String headerValue = request.getHeaders().getFirst(entry.getKey());
                if (entry.getValue().equals(headerValue)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 计算请求的哈希值（用于权重分配）
     */
    private int hashRequest(ServerHttpRequest request, GrayStrategy strategy) {
        String key = switch (strategy.getHashKey()) {
            case USER -> request.getHeaders().getFirst(HttpHeadersConstant.X_USER_ID);
            case IP -> request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress()
                : "0.0.0.0";
            case HEADER -> request.getHeaders().getFirst(strategy.getHeaderName());
            default -> UUID.randomUUID().toString();
        };

        if (key == null) {
            key = UUID.randomUUID().toString();
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes());
            return Math.abs(Arrays.hashCode(digest));
        } catch (NoSuchAlgorithmException e) {
            return Math.abs(key.hashCode());
        }
    }

    /**
     * 选择灰度实例
     */
    private Mono<ServiceInstance> selectGrayInstance(String serviceName, GrayStrategy strategy) {
        return discoveryClient.getInstances(serviceName)
            .collectList()
            .map(instances -> {
                // 优先选择带有灰度标签的实例
                List<ServiceInstance> grayInstances = instances.stream()
                    .filter(instance -> {
                        Map<String, String> metadata = instance.getMetadata();
                        return "true".equals(metadata.get(GatewayHeaderConstants.GRAY_METADATA_KEY)) ||
                            "true".equals(metadata.get(GatewayHeaderConstants.GRAY_METADATA_SHORT));
                    })
                    .toList();

                if (!grayInstances.isEmpty()) {
                    // 随机选择一个灰度实例
                    return grayInstances.get(new Random().nextInt(grayInstances.size()));
                }

                // 如果没有灰度实例，使用版本匹配的实例
                if (strategy.getVersion() != null) {
                    List<ServiceInstance> versionInstances = instances.stream()
                        .filter(instance -> strategy.getVersion()
                            .equals(instance.getMetadata().get("version")))
                        .toList();
                    if (!versionInstances.isEmpty()) {
                        return versionInstances.get(new Random().nextInt(versionInstances.size()));
                    }
                }

                return null;
            });
    }

    private GrayStrategy getGrayStrategy(String serviceName) {
        return properties.getStrategies().get(serviceName);
    }

    @Override
    public int getOrder() {
        return -100; // 在负载均衡之前执行
    }


}
