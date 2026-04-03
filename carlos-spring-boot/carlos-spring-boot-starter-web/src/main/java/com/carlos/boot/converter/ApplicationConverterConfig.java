package com.carlos.boot.converter;


import com.carlos.json.jackson.config.JacksonConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * WebMvc配置
 *
 * @author carlos
 * @date 2018-11-08
 */
@Slf4j
@Configuration
@AutoConfigureAfter(JacksonConfig.class)
@AllArgsConstructor
public class ApplicationConverterConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

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

    // org.springframework.format.Formatter只能做String类型到其他类型的转换。
    // org.springframework.core.convert.converter.Converter可以做任意类型的转换。
    // 如果两者定义同一种类型 Formatter优先

    // ----------------------  转换器配置 适用于通过formDate进行传参 start  ------------------------

    @Bean
    public Converter<String, Date> stringToDateConverter() {
        return new SpringBootFormDataConverter.StringToDateConverter();
    }

    @Bean
    public Converter<String, LocalDateTime> localDateTimeConvert() {
        return new SpringBootFormDataConverter.StringToLocalDateTimeConverter();
    }

    @Bean
    public Converter<String, LocalTime> localTimeConvert() {
        return new SpringBootFormDataConverter.StringToLocalTimeConverter();
    }

    @Bean
    public Converter<String, LocalDate> localDateConvert() {
        return new SpringBootFormDataConverter.StringToLocalDateConverter();
    }

    // ----------------------   转换器配置 end   ------------------------

}
