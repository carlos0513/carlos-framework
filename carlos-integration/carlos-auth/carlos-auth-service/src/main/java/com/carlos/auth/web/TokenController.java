package com.carlos.auth.web;

import cn.hutool.core.util.StrUtil;
import com.carlos.boot.request.RequestInfo;
import com.carlos.boot.request.RequestUtil;
import com.carlos.core.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.*;

/**
 * Token 管理控制器
 *
 * <p>提供 OAuth2 Token 的管理接口，包括令牌检查、撤销等。</p>
 *
 * <p><strong>与 {@link com.carlos.auth.login.UserAuthController} 的区别：</strong></p>
 * <ul>
 *   <li>此类：面向 OAuth2 客户端/资源服务器，处理 Token 级别的操作（检查有效性、撤销令牌）</li>
 *   <li>UserAuthController：面向最终用户，处理用户登录、登出等用户级操作</li>
 * </ul>
 *
 * @author Carlos
 * @date 2022/11/13 21:14
 * @see com.carlos.auth.login.UserAuthController
 */
@AllArgsConstructor
@RestController
@RequestMapping("/oauth2/token")
@Tag(name = "OAuth2认证")
public class TokenController {

    private final OAuth2AuthorizationService authorizationService;

    private final RegisteredClientRepository clientService;

    /**
     * 退出并删除token
     *
     * @param authHeader Authorization
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<?> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        RequestInfo requestInfo = RequestUtil.getRequestInfo();
        String token = requestInfo.getToken();
        if (StrUtil.isBlank(token)) {
            return Result.success();
        }
        return this.removeToken(token);
    }

    @GetMapping("/checkToken")
    @Operation(summary = "检查Token")
    public Result<String> checkToken(@RequestParam("token") String token) {
        // RequestInfo requestInfo = RequestUtil.getRequestInfo();
        // String token = requestInfo.getToken();
        OAuth2Authorization authorization = this.authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        String registeredClientId = authorization.getRegisteredClientId();
        RegisteredClient client = clientService.findById(registeredClientId);
        // 如果令牌不存在 返回401
        return Result.success(client.getClientId());
    }

    /**
     * 删除token
     *
     * @return com.carlos.common.core.response.Result<?>
     * @author Carlos
     * @date 2022/11/13 21:05
     */
    @PostMapping("delete")
    public Result<?> removeToken(String token) {
        OAuth2Authorization authorization = this.authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
            return Result.success();
        }
        // 清空access token
        this.authorizationService.remove(authorization);
        return Result.success();
    }
}
