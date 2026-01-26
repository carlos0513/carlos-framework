package com.yunjin.openapi;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.AllArgsConstructor;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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
        final OpenApiProperties.ContactProperties contact = properties.getContact();
        Contact c = new Contact();
        c.setName(contact.getName());
        c.setEmail(contact.getEmail());
        c.setUrl(contact.getUrl());
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getTitle())
                        .version(properties.getVersion())
                        .description(properties.getDescription())
                        .termsOfService(properties.getTermsOfServiceUrl())
                        .contact(c)
                        .license(new License()
                                .name(properties.getLicense())
                                .url(properties.getLicenseUrl())));
    }


    @Bean
    public Knife4jRunnerWorker swaggerRunnerWorker() {
        return new Knife4jRunnerWorker();
    }


}
