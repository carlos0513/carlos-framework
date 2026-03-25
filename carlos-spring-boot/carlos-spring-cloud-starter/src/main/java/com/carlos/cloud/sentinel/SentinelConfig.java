package com.carlos.cloud.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.carlos.core.response.CommonErrorCode;
import com.carlos.core.response.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * <p>
 * Sentinel 限流熔断配置
 * </p>
 *
 * <p>
 * 提供 Sentinel 的自动配置，包括：
 * <ul>
 *   <li>注解支持（@SentinelResource）</li>
 *   <li>自定义限流异常处理</li>
 *   <li>与 Spring Cloud 集成</li>
 * </ul>
 * </p>
 *
 * @author carlos
 * @date 2024/01/15
 */
@Slf4j
@Configuration
@ConditionalOnClass(SentinelResourceAspect.class)
@ConditionalOnProperty(prefix = "spring.cloud.sentinel", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SentinelConfig {

    @PostConstruct
    public void init() {
        log.info("Sentinel 限流熔断组件已启用");
    }

    /**
     * Sentinel 注解切面
     */
    @Bean
    @ConditionalOnMissingBean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    /**
     * 自定义限流异常处理器
     * 统一处理 Sentinel 的各种 BlockException
     */
    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler blockExceptionHandler(ObjectMapper objectMapper) {
        return new CustomBlockExceptionHandler(objectMapper);
    }

    /**
     * 自定义 BlockException 处理器实现
     */
    @Slf4j
    public static class CustomBlockExceptionHandler implements BlockExceptionHandler {

        private final ObjectMapper objectMapper;

        public CustomBlockExceptionHandler(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, String resourceName, BlockException e) throws Exception {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            String message;
            if (e instanceof FlowException) {
                message = "访问过于频繁，请稍后重试";
            } else if (e instanceof DegradeException) {
                message = "服务暂时不可用，请稍后重试";
            } else if (e instanceof ParamFlowException) {
                message = "热点参数访问受限";
            } else if (e instanceof SystemBlockException) {
                message = "系统负载过高，请稍后重试";
            } else if (e instanceof AuthorityException) {
                message = "无权访问该资源";
            } else {
                message = "请求被拦截";
            }

            log.warn("Sentinel 拦截请求 - 资源: {}, 类型: {}, 原因: {}",
                resourceName, e.getClass().getSimpleName(), e.getMessage());

            Result<String> result = Result.error(CommonErrorCode.SERVICE_UNAVAILABLE, message);
            response.getWriter().write(objectMapper.writeValueAsString(result));
        }
    }
}
