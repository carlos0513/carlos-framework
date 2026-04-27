package com.carlos.auth.security.handle;

import com.carlos.boot.util.ResponseUtil;
import com.carlos.core.response.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * <p>
 * 自定义表单登录失败处理器
 * </p>
 *
 * <p>处理表单登录失败后的响应，包括：</p>
 * <ul>
 *   <li>记录登录失败日志</li>
 *   <li>解析 OAuth2 错误信息</li>
 *   <li>返回标准化的错误响应</li>
 * </ul>
 *
 * <p>适用场景：传统表单登录（/login）失败后的处理</p>
 *
 * @author carlos
 * @date 2022/11/4
 */
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) {
        String username = request.getParameter(OAuth2ParameterNames.USERNAME);
        String errorMessage = extractErrorMessage(exception);

        log.warn("用户登录失败: {}, 错误: {}", username, errorMessage);

        try {
            sendErrorResponse(response, exception, errorMessage);
        } catch (IOException e) {
            log.error("返回登录失败响应失败, 用户: {}", username, e);
        }
    }

    /**
     * 提取错误信息
     *
     * <p>从不同类型的认证异常中提取可读的错误描述</p>
     *
     * @param exception 认证异常
     * @return 错误描述信息
     */
    private String extractErrorMessage(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException oauth2Exception) {
            OAuth2Error error = oauth2Exception.getError();
            return error != null ? error.getDescription() : exception.getMessage();
        }
        return exception.getMessage();
    }

    /**
     * 发送错误响应
     *
     * <p>返回统一格式的错误响应给客户端</p>
     *
     * @param response      HTTP 响应
     * @param exception     认证异常
     * @param errorMessage  错误消息
     * @throws IOException 写入响应时发生 IO 异常
     */
    private void sendErrorResponse(HttpServletResponse response, AuthenticationException exception,
                                   String errorMessage) throws IOException {
        // 设置 HTTP 状态码
        HttpStatus status = determineHttpStatus(exception);
        response.setStatus(status.value());

        // 返回统一响应格式
        ResponseUtil.printJson(response, Result.error(errorMessage));
    }

    /**
     * 确定 HTTP 状态码
     *
     * <p>根据异常类型返回适当的 HTTP 状态码</p>
     *
     * @param exception 认证异常
     * @return HTTP 状态码
     */
    private HttpStatus determineHttpStatus(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException oauth2Exception) {
            OAuth2Error error = oauth2Exception.getError();
            if (error != null) {
                String errorCode = error.getErrorCode();
                return switch (errorCode) {
                    case "invalid_request", "invalid_grant" -> HttpStatus.BAD_REQUEST;
                    case "invalid_client", "unauthorized_client" -> HttpStatus.UNAUTHORIZED;
                    case "access_denied" -> HttpStatus.FORBIDDEN;
                    case "server_error" -> HttpStatus.INTERNAL_SERVER_ERROR;
                    default -> HttpStatus.UNAUTHORIZED;
                };
            }
        }
        return HttpStatus.UNAUTHORIZED;
    }
}
