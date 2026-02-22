package com.carlos.gateway.auth;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.StatusCode;
import com.carlos.core.util.PathMatchUtil;
import com.carlos.gateway.config.GatewayConstant;
import com.carlos.gateway.config.GatewayProperties;
import com.carlos.gateway.resource.TokenUtil;
import com.carlos.redis.util.RedisUtil;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 认证过滤器 将登录用户的JWT转化成用户信息的全局过滤器
 * </p>
 *
 * @author yunjin
 * @date 2021/11/3 17:54
 */
@Slf4j
@AllArgsConstructor
public class RoleCheckGlobalFilter implements GlobalFilter {

    private final GatewayProperties gatewayProperties;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取当前请求URL
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();
        URI uri = request.getURI();
        String path = uri.getPath();
        // 获取菜单和api映射关系
        List<MenuApiMapping> mapping = gatewayProperties.getMappings();
        HashSet<String> needleMenu = Sets.newHashSet();
        for (MenuApiMapping entry : mapping) {
            for (ApiInfo apiInfo : entry.getApis()) {
                if (apiInfo.getMethod().equals(method) && PathMatchUtil.match(apiInfo.getUrl(), path)) {
                    needleMenu.add(entry.getMenuPath());
                }
            }
        }

        if (needleMenu.isEmpty()) {
            // 如果未匹配到对应的菜单，则正常放行
            return chain.filter(exchange);
        }

        // 判断用户是否有该菜单的权限
        String token = TokenUtil.getFromHeader(request);
        if (token == null) {
            throw new ServiceException(StatusCode.NOT_PERMISSION);
        }
        token = StrUtil.removePrefix(token, "Bearer ");
        // 该缓存内容为json字符串，方便跨服务的反序列化
        String tokenKey = String.format(GatewayConstant.LOGIN_USER_MENU_CACHE, token);
        if (RedisUtil.hasKey(tokenKey)) {
            // 如果有缓存，则直接从缓存中获取
            String cacheValue = RedisUtil.getValue(tokenKey);
            List<UserMenu> menus = JSONUtil.toList(cacheValue, UserMenu.class);
            // 获取当前请求的菜单ID
            for (UserMenu menu : menus) {
                if (needleMenu.stream().anyMatch(m -> m.equals(menu.getPath()))) {
                    return chain.filter(exchange);
                }
            }
        }
        log.error("api [{}] not permission for {}", path, needleMenu);
        throw new ServiceException(StatusCode.NOT_PERMISSION);
    }
}
