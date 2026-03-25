package com.carlos.json.web;

import com.carlos.core.response.Result;
import com.carlos.json.JsonUtils;
import com.carlos.json.config.JsonProperties;
import com.carlos.json.jackson.JacksonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * <p>
 * JSON Web 配置类
 * </p>
 *
 * @author carlos
 * @date 2025/01/15
 */
@Slf4j
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
@RequiredArgsConstructor
public class JsonWebConfiguration implements WebMvcConfigurer {

    private final JsonProperties jsonProperties;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        log.info("JSON Web 配置已加载，当前引擎: {}", jsonProperties.getEngine());
        // 设置全局属性
        JsonUtils.setProperties(jsonProperties);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 配置 MappingJackson2HttpMessageConverter
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                // 使用配置的 ObjectMapper
                jacksonConverter.setObjectMapper(objectMapper);
                log.debug("配置 Jackson HttpMessageConverter");
            }
        }
    }

    /**
     * 响应包装处理器
     */
    @Slf4j
    @RestControllerAdvice
    @ConditionalOnProperty(prefix = "carlos.json.web-response", name = "wrap-response", havingValue = "true")
    @RequiredArgsConstructor
    public static class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

        private final JsonProperties properties;

        @Override
        public boolean supports(MethodParameter returnType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
            // 检查是否需要排除的路径
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body,
                                      MethodParameter returnType,
                                      MediaType selectedContentType,
                                      Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                      ServerHttpRequest request,
                                      ServerHttpResponse response) {
            // 检查是否需要包装响应
            if (!properties.getWebResponse().isWrapResponse()) {
                return body;
            }

            // 检查排除路径
            String path = request.getURI().getPath();
            List<String> excludePaths = properties.getWebResponse().getExcludePaths();
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

            // 包装响应
            return Result.success(body);
        }
    }
}
