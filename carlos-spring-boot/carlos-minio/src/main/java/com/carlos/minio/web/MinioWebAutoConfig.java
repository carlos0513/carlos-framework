package com.carlos.minio.web;


import com.carlos.minio.config.MinioConfig;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 接口操作minio时对象处理
 * </p>
 *
 * @author yunjin
 * @date 2021/6/10 14:39
 */
@ConditionalOnProperty(prefix = "yunjin.minio", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureAfter(MinioConfig.class)
public class MinioWebAutoConfig implements WebMvcConfigurer {


    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new MinioObjectHandlerMethodReturnValueHandler());
    }

}
