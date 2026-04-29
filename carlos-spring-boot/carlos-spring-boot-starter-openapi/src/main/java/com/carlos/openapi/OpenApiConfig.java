package com.carlos.openapi;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AllArgsConstructor;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 *   openAPI  + Knife4J 全局配置
 * </p>
 *
 * @author Carlos
 * @date 2025-10-13 18:42
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OpenApiProperties.class)
@ConditionalOnProperty(prefix = "openapi", name = "enable", havingValue = "true")
@AllArgsConstructor
public class OpenApiConfig {

    private final OpenApiProperties properties;

    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("x-order", RandomUtil.randomInt(0, 100));
                    tag.setExtensions(map);
                });
            }
            if (openApi.getPaths() != null) {
                openApi.addExtension("x-test123", "333");
                openApi.getPaths().addExtension("x-abb", RandomUtil.randomInt(1, 100));
            }

        };
    }

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI()
            // 修复：添加默认 Server 配置，使用相对路径，避免中文被用作 host
            .servers(List.of(new Server().url("/").description("当前服务")))
            .info(new Info()
                .title(properties.getTitle())
                .version(properties.getVersion())
                .description(properties.getDescription())
                .termsOfService(properties.getTermsOfServiceUrl())
                .license(new License()
                    .name(properties.getLicense())
                    .url(properties.getLicenseUrl())));
        // 联系方式
        Optional.ofNullable(properties.getContact()).ifPresent(contact -> openAPI.getInfo().contact(new Contact().name(contact.getName()).email(contact.getEmail()).url(contact.getUrl())));
        return openAPI;
    }


    @Bean
    public Knife4jRunnerWorker swaggerRunnerWorker() {
        return new Knife4jRunnerWorker();
    }


}
