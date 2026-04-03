package com.carlos.boot.response;

import com.carlos.boot.annotation.ClientApi;
import com.carlos.boot.interceptors.ApplicationInterceptorProperties;
import com.carlos.boot.interceptors.GlobalInterceptorProperties;
import com.carlos.boot.request.RequestUtil;
import com.carlos.core.response.Result;
import com.carlos.json.jackson.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * <p>
 *   统一响应处理 ResponseBodyAdvice接口是在Controller执行return之后， 在response返回给浏览器或者APP客户端之前，执行的对response的一些处理。可以实现对response数据的一些统一封装或者加密等操作。 对请求进行统一封装
 * </p>
 *
 * @author Carlos
 * @date 2026-03-27 23:04
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(prefix = "carlos.boot.response.wrap", name = "enable", havingValue = "true")
@RequiredArgsConstructor
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    private final ResponseProperties properties;

    private final ApplicationInterceptorProperties interceptorProperties;

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        if (!properties.getWrap().isEnable()) {
            return false;
        }

        if (returnType == null) {
            return false;
        }
        // TODO: carlos 2022/10/21 此处配置不需要进行统一响应处理的路径
        final Class<?> declaringClass = returnType.getDeclaringClass();
        // 过滤掉swagger请求
        if (declaringClass.getName().contains("springdoc")) {
            return false;
        }
        // 过滤掉 OpenAPI 相关类
        if (declaringClass.getName().startsWith("org.springdoc") ||
            declaringClass.getName().startsWith("io.swagger")) {
            return false;
        }
        // 判断是否要执行beforeBodyWrite方法，true为执行，false不执行
        final RestController annotation = declaringClass.getAnnotation(RestController.class);
        final ClientApi apiAnnotation = declaringClass.getAnnotation(ClientApi.class);
        return (apiAnnotation == null && annotation != null);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        ResponseProperties.ResponseWrap wrap = properties.getWrap();

        // 检查排除路径
        String path = request.getURI().getPath();
        List<String> excludePaths = wrap.getExcludePaths();

        if (excludePaths != null) {
            for (String excludePath : excludePaths) {
                if (path.startsWith(excludePath) || path.matches(excludePath)) {
                    return body;
                }
            }
        }

        // 如果已经是包装类型，直接返回
        if (body instanceof Result) {
            return body;
        }

        // 如果返回类型是 String，需要特殊处理
        if (body instanceof String) {
            // 转换为 JSON 字符串
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            try {
                return JacksonUtil.toJson(Result.success(body));
            } catch (Exception e) {
                log.error("包装 String 响应失败", e);
                return body;
            }
        }

        final GlobalInterceptorProperties.PrintType printType = interceptorProperties.getGlobalInterceptor().getPrintType();
        if (printType == GlobalInterceptorProperties.PrintType.RESPONSE
            || printType == GlobalInterceptorProperties.PrintType.BOTH_ORDER
            || printType == GlobalInterceptorProperties.PrintType.BOTH_TOGETHER) {
            if (printType == GlobalInterceptorProperties.PrintType.BOTH_TOGETHER) {
                RequestUtil.printRequestInfo();
            }

            // 打印响应信息
            assert body instanceof Result<?>;
            RequestUtil.printResponseInfo((Result<?>) body);
        }

        // 包装响应
        return Result.success(body);
    }
}
