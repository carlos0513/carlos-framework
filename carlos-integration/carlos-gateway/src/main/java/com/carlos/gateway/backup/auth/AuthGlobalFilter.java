package com.carlos.gateway.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.auth.AuthConstant;
import com.carlos.core.auth.UserContext;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.StatusCode;
import com.carlos.core.util.PathMatchUtil;
import com.carlos.gateway.config.GatewayConstant;
import com.carlos.gateway.resource.TokenUtil;
import com.carlos.redis.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 认证过滤器 将登录用户的JWT转化成用户信息的全局过滤器
 * </p>
 *
 * @author carlos
 * @date 2021/11/3 17:54
 */
@Slf4j
@AllArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final com.carlos.gateway.auth.GatewayAuthProperties authProperties;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 配置是否开启网关验证  配置文件配置
        if (!authProperties.isAuthenticate()) {
            return chain.filter(exchange);
        }
        Set<String> whitelist = authProperties.getWhitelist();

        //  判断当前访问接口是否在白名单中
        ServerHttpRequest request = exchange.getRequest();

        List<String> headers = request.getHeaders().get("access_token");
        if (CollUtil.isNotEmpty(headers)) {
            return chain.filter(exchange);
        }
        URI uri = request.getURI();
        String path = uri.getPath();
        if (PathMatchUtil.matchAny(whitelist, path)) {
            return chain.filter(exchange);
        }

        // token验证
        String token = TokenUtil.getFromHeader(request);
        if (token == null) {
            throw new ServiceException(StatusCode.NOT_PERMISSION);
        }
        token = StrUtil.removePrefix(token, "Bearer ");
        if (StrUtil.isEmpty(token)) {
            throw new ServiceException(StatusCode.NOT_PERMISSION);
        } else {
            Duration expire = authProperties.getTokenExpire();
            long minutes = expire.toMinutes();
            String contextCacheKey = String.format(GatewayConstant.LOGIN_USER_CONTEXT_CACHE, token);
            String tokenKey = String.format(GatewayConstant.LOGIN_USER_TOKEN_CACHE, token);
            String menuKey = String.format(GatewayConstant.LOGIN_USER_MENU_CACHE, token);
            RedisUtil.setExpire(tokenKey, minutes, TimeUnit.MINUTES);
            RedisUtil.setExpire(contextCacheKey, minutes, TimeUnit.MINUTES);
            RedisUtil.setExpire(menuKey, minutes, TimeUnit.MINUTES);

            if (log.isDebugEnabled()) {
                log.debug("append token [{}] expire time:{} minutes", token, minutes);
            }
            UserContext context = RedisUtil.getValue(contextCacheKey);
            if (context == null) {
                throw new ServiceException(StatusCode.NOT_PERMISSION);
                // return chain.filter(exchange);
            }

            ServerHttpRequest.Builder builder = request.mutate();
            Optional.ofNullable(context.getToken()).ifPresent(i -> builder.header(AuthConstant.USER_TOKEN, i));
            Optional.ofNullable(context.getAccount()).ifPresent(i -> builder.header(AuthConstant.USER_ACCOUNT, i));
            Optional.ofNullable(context.getUserId()).ifPresent(i -> builder.header(AuthConstant.USER_ID, String.valueOf(i)));
            Optional.ofNullable(context.getDepartmentId()).ifPresent(i -> builder.header(AuthConstant.DEPT_ID, String.valueOf(i)));
            Optional.ofNullable(context.getRoleId()).ifPresent(i -> builder.header(AuthConstant.ROLE_ID, String.valueOf(i)));
            Optional.ofNullable(context.getTenantId()).ifPresent(i -> builder.header(AuthConstant.TENANT_ID, String.valueOf(i)));
            Optional.ofNullable(context.getPhone()).ifPresent(i -> builder.header(AuthConstant.USER_PHONE, i));
            Optional.ofNullable(context.getRoleIds()).ifPresent(i -> builder.header(AuthConstant.ROLE_IDS, StrUtil.join(StrUtil.COMMA, i)));
            Optional.ofNullable(context.getDepartmentIds()).ifPresent(i -> builder.header(AuthConstant.DEPT_IDS, StrUtil.join(StrUtil.COMMA, i)));
            builder.header("Agent-Type", request.getHeaders().getFirst("Agent-Type"));
            exchange = exchange.mutate().request(builder.build()).build();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 过滤器执行顺序， 值越小，越先执行
        return 0;
    }
}
