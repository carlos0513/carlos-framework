package com.carlos.gateway.security;

import com.carlos.core.response.CommonErrorCode;
import com.carlos.gateway.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Web 应用防火墙（WAF）过滤器
 * 防护：SQL 注入、XSS、CSRF、路径遍历、敏感文件访问、命令注入
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 * @updated 2026/3/24 优化异常处理，统一响应格式
 */
@Slf4j
@Component
public class WafFilter implements GlobalFilter, Ordered {

    private final WafProperties properties;
    private final ObjectMapper objectMapper;

    // SQL 注入检测模式
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "('|\"|%27|%22|%25%27|%25%22)|(--|%2D%2D|#|%23)|(/\\*|%2F\\*)|(\\*/|%2A%2F)|" +
            "(union|UNION|%75%6E%69%6F%6E|%55%4E%49%4F%4E)|(select|SELECT|%73%65%6C%65%63%74)|" +
            "(insert|INSERT|%69%6E%73%65%72%74)|(delete|DELETE|%64%65%6C%65%74%65)|" +
            "(update|UPDATE|%75%70%64%61%74%65)|(drop|DROP|%64%72%6F%70)|(exec|EXEC|%65%78%65%63)",
        Pattern.CASE_INSENSITIVE
    );

    // XSS 检测模式
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "(<script|SCRIPT|&lt;script|%3Cscript)|(javascript:|JAVASCRIPT:|%6A%61%76%61%73%63%72%69%70%74)|" +
            "(onerror|onload|onclick|onmouseover|ONERROR|ONLOAD)=|(<iframe|IFRAME|&lt;iframe)|" +
            "(alert\\(|confirm\\(|prompt\\()",
        Pattern.CASE_INSENSITIVE
    );

    // 路径遍历检测模式
    private static final Pattern PATH_TRAVERSAL_PATTERN = Pattern.compile(
        "(\\.\\./|\\.\\.\\\\|%2e%2e%2f|%2e%2e%5c)",
        Pattern.CASE_INSENSITIVE
    );

    // 敏感文件访问模式
    private static final Pattern SENSITIVE_FILE_PATTERN = Pattern.compile(
        "\\.(git|svn|htaccess|env|ini|log|sql|bak|backup|swp|old|dist)(/|$)|" +
            "(WEB-INF|META-INF|web\\.xml|server\\.xml|config\\.xml)|" +
            "/(api-docs|swagger-ui|actuator|health|metrics|prometheus)(/|$)",
        Pattern.CASE_INSENSITIVE
    );

    // 命令注入检测模式
    private static final Pattern COMMAND_INJECTION_PATTERN = Pattern.compile(
        "(;|\\||&&|`\\$\\(\\(|%3B|%7C|%26%26|%60)|" +
            "(cat|ls|pwd|whoami|id|uname|wget|curl|nc|netcat|bash|sh|cmd|powershell)(\\s|%20)",
        Pattern.CASE_INSENSITIVE
    );

    public WafFilter(WafProperties properties) {
        this.properties = properties;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. 检查路径
        if (checkPath(path)) {
            return blockRequest(exchange, "PATH_TRAVERSAL", "suspicious_path",
                "检测到可疑路径访问，请求已被拦截");
        }

        // 2. 检查查询参数
        String query = request.getURI().getQuery();
        if (query != null) {
            Matcher sqlMatcher = SQL_INJECTION_PATTERN.matcher(query);
            if (properties.isSqlInjectionProtection() && sqlMatcher.find()) {
                return blockRequest(exchange, "SQL_INJECTION", "query_string",
                    "检测到 SQL 注入攻击，请求已被拦截");
            }

            Matcher xssMatcher = XSS_PATTERN.matcher(query);
            if (properties.isXssProtection() && xssMatcher.find()) {
                return blockRequest(exchange, "XSS", "query_string",
                    "检测到 XSS 攻击，请求已被拦截");
            }

            Matcher cmdMatcher = COMMAND_INJECTION_PATTERN.matcher(query);
            if (properties.isCommandInjectionProtection() && cmdMatcher.find()) {
                return blockRequest(exchange, "COMMAND_INJECTION", "query_string",
                    "检测到命令注入攻击，请求已被拦截");
            }
        }

        // 3. 检查请求头
        String headerViolation = checkHeaders(request.getHeaders());
        if (headerViolation != null) {
            return blockRequest(exchange, "MALICIOUS_HEADER", headerViolation,
                "检测到恶意请求头，请求已被拦截");
        }

        // 4. 检查请求体（POST/PUT 请求）
        if (properties.isCheckBody() &&
            (request.getMethod() == org.springframework.http.HttpMethod.POST ||
                request.getMethod() == org.springframework.http.HttpMethod.PUT ||
                request.getMethod() == org.springframework.http.HttpMethod.PATCH)) {
            return checkRequestBody(exchange, chain);
        }

        // 5. CSRF 检查
        if (properties.isCsrfProtection() && isCsrfRequest(request)) {
            if (!checkCsrfToken(request)) {
                return blockRequest(exchange, "CSRF", "invalid_token",
                    "CSRF Token 验证失败，请求已被拦截");
            }
        }

        return chain.filter(exchange);
    }

    /**
     * 检查路径
     */
    private boolean checkPath(String path) {
        if (properties.isPathTraversalProtection() && PATH_TRAVERSAL_PATTERN.matcher(path).find()) {
            log.warn("WAF: Path traversal detected: {}", path);
            return true;
        }
        if (properties.isSensitiveFileProtection() && SENSITIVE_FILE_PATTERN.matcher(path).find()) {
            log.warn("WAF: Sensitive file access detected: {}", path);
            return true;
        }
        return false;
    }

    /**
     * 检查请求头
     * @return 违规类型，如果没有违规返回 null
     */
    private String checkHeaders(HttpHeaders headers) {
        for (String headerName : headers.keySet()) {
            List<String> values = headers.get(headerName);
            if (values != null) {
                for (String value : values) {
                    if (properties.isSqlInjectionProtection() && SQL_INJECTION_PATTERN.matcher(value).find()) {
                        log.warn("WAF: SQL injection in header {}: {}", headerName, value);
                        return headerName;
                    }
                    if (properties.isXssProtection() && XSS_PATTERN.matcher(value).find()) {
                        log.warn("WAF: XSS in header {}: {}", headerName, value);
                        return headerName;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 检查请求体
     */
    private Mono<Void> checkRequestBody(ServerWebExchange exchange, GatewayFilterChain chain) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
            .flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);

                String body = new String(bytes, StandardCharsets.UTF_8);

                if (properties.isSqlInjectionProtection() && SQL_INJECTION_PATTERN.matcher(body).find()) {
                    return blockRequest(exchange, "SQL_INJECTION", "request_body",
                        "检测到 SQL 注入攻击（请求体），请求已被拦截");
                }
                if (properties.isXssProtection() && XSS_PATTERN.matcher(body).find()) {
                    return blockRequest(exchange, "XSS", "request_body",
                        "检测到 XSS 攻击（请求体），请求已被拦截");
                }
                if (properties.isCommandInjectionProtection() && COMMAND_INJECTION_PATTERN.matcher(body).find()) {
                    return blockRequest(exchange, "COMMAND_INJECTION", "request_body",
                        "检测到命令注入攻击（请求体），请求已被拦截");
                }

                // 重新包装请求体
                DataBuffer newBuffer = exchange.getResponse().bufferFactory().wrap(bytes);
                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return Flux.just(newBuffer);
                    }
                };

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });
    }

    /**
     * 判断是否是 CSRF 风险请求
     */
    private boolean isCsrfRequest(ServerHttpRequest request) {
        String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        return contentType != null && (
            contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) ||
                contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)
        );
    }

    /**
     * 检查 CSRF Token
     */
    private boolean checkCsrfToken(ServerHttpRequest request) {
        String csrfToken = request.getHeaders().getFirst("X-CSRF-Token");
        String origin = request.getHeaders().getFirst(HttpHeaders.ORIGIN);
        String referer = request.getHeaders().getFirst(HttpHeaders.REFERER);

        // 简单的 CSRF 检查：验证 Origin 或 Referer
        if (origin != null && !origin.startsWith(properties.getAllowedOrigin())) {
            log.warn("WAF: CSRF check failed - invalid origin: {}", origin);
            return false;
        }
        if (referer != null && !referer.startsWith(properties.getAllowedOrigin())) {
            log.warn("WAF: CSRF check failed - invalid referer: {}", referer);
            return false;
        }

        return true;
    }

    /**
     * 拦截请求，返回统一格式的错误响应
     */
    private Mono<Void> blockRequest(ServerWebExchange exchange, String ruleType, String ruleName, String message) {
        String path = exchange.getRequest().getURI().getPath();
        String clientIp = exchange.getRequest().getRemoteAddress() != null
            ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
            : "unknown";

        log.warn("WAF blocked request from [{}] to [{}]: {} - ruleType={}, ruleName={}",
            clientIp, path, message, ruleType, ruleName);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 构建标准化的错误响应
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .code(CommonErrorCode.FORBIDDEN.getCode())
            .msg(message)
            .path(path)
            .method(exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "UNKNOWN")
            .extra(java.util.Map.of(
                "ruleType", ruleType,
                "ruleName", ruleName,
                "clientIp", clientIp
            ))
            .build();

        return response.writeWith(Mono.fromSupplier(() -> {
            try {
                byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
                return response.bufferFactory().wrap(bytes);
            } catch (Exception e) {
                log.error("Failed to serialize WAF block response", e);
                String fallback = String.format(
                    "{\"success\":false,\"status\":403,\"code\":5403,\"message\":\"%s\"}",
                    message);
                return response.bufferFactory().wrap(fallback.getBytes(StandardCharsets.UTF_8));
            }
        }));
    }

    @Override
    public int getOrder() {
        return -5000; // 最高优先级，最先执行
    }
}
