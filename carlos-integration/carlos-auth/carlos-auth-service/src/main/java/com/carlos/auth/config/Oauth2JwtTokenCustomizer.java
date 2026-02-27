package com.carlos.auth.config;

import com.carlos.auth.service.ExtendUserDetailsService;
import com.carlos.core.auth.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.stream.Collectors;

/**
 * Carlos JWT Token 自定义增强器
 *
 * <p>向 JWT Token 中添加自定义声明（Claims），如用户 ID、角色、权限等业务信息。</p>
 *
 * <h3>添加到 Token 的声明：</h3>
 * <table border="1">
 *   <tr><th>Claim</th><th>说明</th><th>示例值</th></tr>
 *   <tr><td>user_id</td><td>用户唯一标识</td><td>10001</td></tr>
 *   <tr><td>username</td><td>用户名/账号</td><td>zhangsan</td></tr>
 *   <tr><td>tenant_id</td><td>租户/客户端 ID</td><td>tenant_01</td></tr>
 *   <tr><td>dept_id</td><td>部门 ID</td><td>100</td></tr>
 *   <tr><td>role_ids</td><td>角色 ID 列表</td><td>[1, 2, 3]</td></tr>
 *   <tr><td>authorities</td><td>权限列表（逗号分隔）</td><td>read,write,admin</td></tr>
 * </table>
 *
 * <h3>Token 示例（解码后）：</h3>
 * <pre>{@code
 * {
 *   "iss": "http://localhost:8080",
 *   "sub": "zhangsan",
 *   "aud": "client-app",
 *   "exp": 1709123456,
 *   "iat": 1709119856,
 *   "jti": "unique-token-id",
 *   "scope": "read write",
 *   "user_id": 10001,
 *   "username": "zhangsan",
 *   "tenant_id": "tenant_01",
 *   "role_ids": [1, 2],
 *   "authorities": "read,write,ROLE_1,ROLE_2"
 * }
 * }</pre>
 *
 * <h3>在资源服务器中使用：</h3>
 * <pre>{@code
 * @RestController
 * public class UserController {
 *
 *     @GetMapping("/user/info")
 *     public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
 *         Long userId = jwt.getClaim("user_id");
 *         String username = jwt.getClaim("username");
 *         List<Long> roleIds = jwt.getClaim("role_ids");
 *
 *         return Map.of(
 *             "userId", userId,
 *             "username", username,
 *             "roleIds", roleIds
 *         );
 *     }
 * }
 * }</pre>
 *
 * <h3>自定义扩展：</h3>
 * <p>如果需要添加更多自定义声明，可以注册自己的 TokenCustomizer：</p>
 * <pre>{@code
 * @Bean
 * public OAuth2TokenCustomizer<JwtEncodingContext> myTokenCustomizer() {
 *     return context -> {
 *         // 调用默认增强器
 *         carlosJwtTokenCustomizer.customize(context);
 *
 *         // 添加自定义声明
 *         context.getClaims().claim("app_version", "3.0.0");
 *         context.getClaims().claim("env", "production");
 *     };
 * }
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 * @see OAuth2TokenCustomizer
 * @see ExtendUserDetailsService
 */
@Slf4j
@RequiredArgsConstructor
public class Oauth2JwtTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    /**
     * 用户详情服务，用于加载用户业务信息
     */
    private final ExtendUserDetailsService userDetailsService;

    /**
     * 自定义 JWT Token
     *
     * <p>只在生成访问令牌（Access Token）时添加自定义声明，
     * 刷新令牌（Refresh Token）和 ID Token 不添加。</p>
     *
     * @param context JWT 编码上下文
     */
    @Override
    public void customize(JwtEncodingContext context) {
        // 只处理访问令牌
        if (!OAuth2ParameterNames.ACCESS_TOKEN.equals(context.getTokenType().getValue())) {
            return;
        }

        // 获取用户名
        String username = context.getPrincipal().getName();
        if (username == null || username.isEmpty()) {
            log.warn("Cannot customize JWT token: username is empty");
            return;
        }

        try {
            // 加载用户业务信息
            LoginUserInfo userInfo = userDetailsService.loadLoginUserInfo(username);

            if (userInfo == null) {
                log.debug("No additional user info found for: {}", username);
                // 至少添加用户名
                context.getClaims().claim("username", username);
                return;
            }

            // 添加用户 ID
            if (userInfo.getId() != null) {
                context.getClaims().claim("user_id", userInfo.getId());
            }

            // 添加用户名
            context.getClaims().claim("username", userInfo.getAccount());

            // 添加租户/客户端 ID（多租户场景）
            if (userInfo.getClientId() != null) {
                context.getClaims().claim("tenant_id", userInfo.getClientId());
            }

            // 添加部门 ID（数据权限场景）
            if (userInfo.getDepartmentId() != null) {
                context.getClaims().claim("dept_id", userInfo.getDepartmentId());
            }

            // 添加角色 ID 列表
            if (userInfo.getRoleIds() != null && !userInfo.getRoleIds().isEmpty()) {
                // 转换为 List 以便 JSON 序列化
                context.getClaims().claim("role_ids",
                        new java.util.ArrayList<>(userInfo.getRoleIds()));
            }

            // 添加权限列表
            if (context.getPrincipal().getAuthorities() != null &&
                    !context.getPrincipal().getAuthorities().isEmpty()) {
                String authorities = context.getPrincipal().getAuthorities().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
                if (!authorities.isEmpty()) {
                    context.getClaims().claim("authorities", authorities);
                }
            }

            log.debug("Enhanced JWT token for user: {} (user_id: {})",
                    username, userInfo.getId());

        } catch (Exception e) {
            // 增强失败不影响 Token 生成，只记录日志
            log.error("Failed to enhance JWT token for user: {}", username, e);
            // 至少添加用户名
            context.getClaims().claim("username", username);
        }
    }
}
