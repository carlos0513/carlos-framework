package com.carlos.oss.web;

import com.carlos.oss.config.OssAutoConfiguration;
import com.carlos.oss.core.OssTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * OSS Web 自动配置
 * 注册 OssObject 返回值处理器
 * </p>
 *
 * @author carlos
 * @date 2026-03-06
 */
@Configuration
@ConditionalOnProperty(prefix = "carlos.oss", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureAfter(OssAutoConfiguration.class)
public class OssWebAutoConfiguration implements WebMvcConfigurer {

    private final OssTemplate ossTemplate;

    public OssWebAutoConfiguration(OssTemplate ossTemplate) {
        this.ossTemplate = ossTemplate;
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new OssObjectHandlerMethodReturnValueHandler(ossTemplate));
    }
}
