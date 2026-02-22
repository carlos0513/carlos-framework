package com.carlos.gateway.config;

import com.carlos.gateway.auth.RoleCheckGlobalFilter;
import com.carlos.gateway.filter.PathPrefixFilter;
import com.carlos.gateway.filter.ReqHeaderFilter;
import com.carlos.gateway.filter.SelectStripPrefixGatewayFilterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.time.Duration;
import java.util.Collections;


/**
 * <p>
 * 网关配置
 * </p>
 *
 * @author carlos
 * @date 2021/11/4 9:43
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer.class)
@RequiredArgsConstructor
@AutoConfigureBefore({ErrorWebFluxAutoConfiguration.class,})
@EnableConfigurationProperties({GatewayProperties.class, ServerProperties.class, WebProperties.class})
public class GatewayConfig {


    private final GatewayProperties gatewayProperties;

    @Bean
    public GatewayRunnerWorker gatewayRunnerWorker() {
        return new GatewayRunnerWorker();
    }


    /**
     * 全局跨域配置
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setExposedHeaders(Collections.singletonList("*"));
        config.setMaxAge(Duration.ofDays(1));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }


    /**
     * gateway 全局异常处理 该配置会覆盖默认的异常处理 网关都是给接口做代理转发的，后端对应的都是REST
     * API，返回数据格式都是JSON。如果不做处理，当发生异常时，Gateway默认给出的错误信息是页面，不方便前端进行异常处理。需要对异常信息进行处理，返回JSON格式的数据给客户端
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler gatewayErrorWebExceptionHandler() {
        return new GatewayExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public DefaultErrorAttributes errorAttributes() {
        return new GatewayErrorAttributes();
    }

    /**
     * 注册路径选择性截取过滤器
     */
    @Bean
    public SelectStripPrefixGatewayFilterFactory selectStripPrefixGatewayFilterFactory() {
        return new SelectStripPrefixGatewayFilterFactory();
    }

    /**
     * 注册请求头过滤器
     */
    @Bean
    public ReqHeaderFilter reqHeaderFilter() {
        return new ReqHeaderFilter();
    }

    /**
     * 请求统一前缀
     */
    @Bean
    @Order(GlobalFilterOrder.ORDER_LAST)
    public WebFilter apiPrefixFilter() {
        return new PathPrefixFilter(gatewayProperties);

    }

    @Bean
    @Order(GlobalFilterOrder.ORDER_SECOND)
    @ConditionalOnProperty(name = "carlos.gateway.role-check", havingValue = "true")
    public GlobalFilter roleCheckFilter() {
        return new RoleCheckGlobalFilter(gatewayProperties);
    }

    @Bean
    @LoadBalanced // 关键注解，支持服务名称解析
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

}
