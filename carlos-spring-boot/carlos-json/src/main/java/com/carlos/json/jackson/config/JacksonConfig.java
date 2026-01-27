package com.carlos.json.jackson.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigInteger;

/**
 * <p>
 * 前后端交互数据序列化配置, 自定义消息转换器
 * </p>
 *
 * @author yunjin
 * @date 2020/4/9 16:26
 */
@Configuration(proxyBeanMethods = false)
public class JacksonConfig {


    /**
     * 解决Jackson导致Long型数据精度丢失问题 使用时 注入 ObjectMapper即可
     */
    @Bean("jackson2ObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Jackson2ObjectMapperBuilderCustomizer customizer = new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder builder) {
                builder
                        .serializerByType(BigInteger.class, ToStringSerializer.instance)
                        .serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Long.TYPE, ToStringSerializer.instance)
                        .modules(new JavaTimeModel())
                ;
            }
        };
        return customizer;
    }

}