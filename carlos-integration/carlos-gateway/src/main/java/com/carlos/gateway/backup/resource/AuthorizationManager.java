// package com.carlos.gateway.resource;
//
// import cn.hutool.core.util.StrUtil;
// import com.carlos.core.auth.AuthConstant;
// import com.carlos.core.util.PathMatchUtil;
// import com.carlos.gateway.auth.GatewayAuthProperties;
// import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.security.authorization.AuthorizationDecision;
// import org.springframework.security.authorization.ReactiveAuthorizationManager;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.web.server.authorization.AuthorizationContext;
// import reactor.core.publisher.Mono;
//
// import java.net.URI;
// import java.util.Set;
// import java.util.function.Predicate;
//
// /**
//  * <p>
//  * 鉴权管理器，用于判断是否有资源的访问权限
//  * </p>
//  *
//  * @author carlos
//  * @date 2021/11/3 18:06
//  */
// @Slf4j
// @AllArgsConstructor
// public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
//
//     private final GatewayAuthProperties authProperties;
//
//     public static final String ROLE_RESOURCE_KEY = "test:user:role_resource:%s:*";
//
//
//     @Override
//     public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
//         ServerHttpRequest request = authorizationContext.getExchange().getRequest();
//         URI uri = request.getURI();
//         String path = uri.getPath();
//         HttpMethod method = request.getMethod();
//         //白名单路径直接放行
//         Set<String> whitelist = authProperties.getWhitelist();
//         if (PathMatchUtil.antMatch(whitelist, path)) {
//             if (log.isDebugEnabled()) {
//                 log.debug("{} in whitelist", path);
//             }
//             return Mono.just(new AuthorizationDecision(true));
//         }
//
//         //对应跨域的预检请求直接放行
//         if (request.getMethod() == HttpMethod.OPTIONS) {
//             return Mono.just(new AuthorizationDecision(true));
//         }
//
//         String tokenId = TokenUtil.getFromHeader(request);
//
//         if (StrUtil.isEmpty(tokenId)) {
//             log.error("Token can't be empty, path:{}", path);
//             return Mono.just(new AuthorizationDecision(false));
//         }
//
//         // if(!TokenUtil.checkTokenId(tokenId)){
//         //     return Mono.just(new AuthorizationDecision(false));
//         // }
// //
// //        boolean isValid = feignAuth.checkToKenId(tokenId);
// //        if(!isValid){
// //            return Mono.just(new AuthorizationDecision(false));
// //        }
// //
// //
// //
// //        TokenPayload tokenInfo;
// //
// //
// //        // 获取登录用户信息
// //        try {
// //            tokenInfo = feignAuth.getTokenInfo(tokenId);
// //
// //            if (log.isDebugEnabled()) {
// //                log.debug("Token info:{}", tokenInfo);
// //            }
// //        } catch (RuntimeException e) {
// //            log.error("Token parse exception: tokenId:{}, message:{}", tokenId, e.getMessage());
// //            return Mono.just(new AuthorizationDecision(false));
// //        }
//
//         Predicate<String> predicate = authorityRoleId -> {
//             // if (!authProperties.authorize) {
//             //     return true;
//             // }
//             String roleId = authorityRoleId.replace(AuthConstant.AUTHORITY_PREFIX, StrUtil.EMPTY);
//             // 根据角色id获取可访问的资源信息
//             String key = String.format(ROLE_RESOURCE_KEY, roleId);
//             // List<SysResource> resources = RedisUtil.getValueList(key);
//             // if (CollectionUtil.isEmpty(resources)) {
//             //     return false;
//             // }
//             //
//             // long count = resources.stream().filter(r -> {
//             //     if (method != null) {
//             //         if (!(r.getMethod().equals(method.name()))) {
//             //             return false;
//             //         }
//             //     }
//             //
//             //     if (!StrUtil.startWith(path, r.getPathPrefix())) {
//             //         return false;
//             //     }
//             //
//             //     return PathMatchUtil.antMatch(path, r.getPathPrefix() + r.getPath());
//             // }).count();
//             // return count > 0;
//             return true;
//         };
//
//
//         //认证通过且角色匹配的用户可访问当前路径
//         return mono
//                 .filter(Authentication::isAuthenticated)
//                 .flatMapIterable(Authentication::getAuthorities)
//                 .map(GrantedAuthority::getAuthority)
//                 .any(predicate)
//                 .map(AuthorizationDecision::new)
//                 .defaultIfEmpty(new AuthorizationDecision(false));
//     }
//
// }
