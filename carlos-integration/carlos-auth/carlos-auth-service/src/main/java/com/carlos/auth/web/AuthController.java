package com.carlos.auth.web;

import cn.hutool.core.util.StrUtil;
import com.carlos.auth.security.service.LoginService;
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
 * <p>
 *
 * </p>
 *
 * @author Carlos
 * @date 2022/11/13 21:14
 */
@AllArgsConstructor
@RestController
@RequestMapping("api/oauth")
@Tag(name = "OAuth2认证")
public class AuthController {

    private final OAuth2AuthorizationService authorizationService;

    private final LoginService loginService;
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
            return Result.ok();
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
        return Result.ok(client.getClientId());
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
            return Result.ok();
        }
        // 清空access token
        this.authorizationService.remove(authorization);
        return Result.ok();
    }
}
