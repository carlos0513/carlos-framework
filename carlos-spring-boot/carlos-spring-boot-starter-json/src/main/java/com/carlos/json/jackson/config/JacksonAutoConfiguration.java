package com.carlos.json.jackson.config;

import com.carlos.json.JsonFactory;
import com.carlos.json.config.JsonProperties;
import com.carlos.json.jackson.JacksonJsonService;
import com.carlos.json.jackson.JacksonUtil;
import com.carlos.json.jackson.filter.EmptyStringPropertyFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * <p>
 * Jackson 自动配置类（增强版）
 * </p>
 *
 * @author carlos
 * @date 2025/01/15
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@ConditionalOnProperty(prefix = "carlos.json", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class JacksonAutoConfiguration {

    private final JsonProperties jsonProperties;

    /**
     * 配置 Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    @ConditionalOnMissingBean(name = "jackson2ObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 应用日期格式配置
            String dateFormat = jsonProperties.getDateFormat();
            if (dateFormat != null && !dateFormat.isEmpty()) {
                builder.dateFormat(new SimpleDateFormat(dateFormat));
            }

            // 应用时区配置
            String timeZone = jsonProperties.getTimeZone();
            if (timeZone != null && !timeZone.isEmpty()) {
                builder.timeZone(TimeZone.getTimeZone(ZoneId.of(timeZone)));
            }

            // Long 转 String 配置（解决精度丢失问题）
            if (jsonProperties.getSerialization().isLongToString()) {
                builder.serializerByType(BigInteger.class, ToStringSerializer.instance)
                    .serializerByType(Long.class, ToStringSerializer.instance)
                    .serializerByType(Long.TYPE, ToStringSerializer.instance);
            }

            // 序列化配置
            JsonProperties.Serialization serialization = jsonProperties.getSerialization();
            if (serialization.isIndentOutput()) {
                builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
            }
            if (serialization.isFailOnEmptyBeans()) {
                builder.featuresToEnable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            } else {
                builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            }

            // null 值处理
            if (jsonProperties.isIgnoreNull()) {
                builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            }

            // 反序列化配置
            JsonProperties.Deserialization deserialization = jsonProperties.getDeserialization();
            if (deserialization.isFailOnUnknownProperties()) {
                builder.featuresToEnable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            } else {
                builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            }

            // 字段命名策略
            String namingStrategy = jsonProperties.getFieldMapping().getPropertyNamingStrategy();
            if (namingStrategy != null) {
                PropertyNamingStrategies.NamingBase strategy = getNamingStrategy(namingStrategy);
                if (strategy != null) {
                    builder.propertyNamingStrategy(strategy);
                }
            }

            // 注册 JavaTimeModel 模块
            builder.modules(new JavaTimeModel());

            log.debug("Jackson ObjectMapper 已配置");
        };
    }

    /**
     * 获取命名策略
     */
    private PropertyNamingStrategies.NamingBase getNamingStrategy(String strategy) {
        return switch (strategy.toUpperCase()) {
            case "SNAKE_CASE" -> new PropertyNamingStrategies.SnakeCaseStrategy();
            case "KEBAB_CASE" -> new PropertyNamingStrategies.KebabCaseStrategy();
            case "LOWER_CASE" -> new PropertyNamingStrategies.LowerCaseStrategy();
            case "UPPER_CAMEL_CASE" -> new PropertyNamingStrategies.UpperCamelCaseStrategy();
            case "LOWER_CAMEL_CASE" -> new PropertyNamingStrategies.LowerCamelCaseStrategy();
            default -> null;
        };
    }

    /**
     * 配置 ObjectMapper
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 应用额外的 Jackson 特性配置
        JsonProperties.Deserialization deserialization = jsonProperties.getDeserialization();

        // 允许单引号
        if (deserialization.isAllowSingleQuotes()) {
            objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        }

        // 允许注释
        if (deserialization.isAllowComments()) {
            objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        }

        // 允许未转义的控制字符
        if (deserialization.isAllowUnescapedControlChars()) {
            objectMapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        }

        // 允许反斜杠转义任何字符
        if (deserialization.isAllowBackslashEscapingAnyChar()) {
            objectMapper.enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature());
        }

        // 忽略空字符串配置
        if (jsonProperties.isIgnoreEmptyString()) {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter(EmptyStringPropertyFilter.FILTER_NAME, new EmptyStringPropertyFilter());
            objectMapper.setFilterProvider(filterProvider);
        }

        // 设置全局 ObjectMapper
        JsonFactory.setGlobalObjectMapper(objectMapper);

        log.info("Jackson ObjectMapper 已创建并配置");
        return objectMapper;
    }

    /**
     * 配置 JacksonJsonService
     */
    @Bean
    @ConditionalOnMissingBean(JacksonJsonService.class)
    public JacksonJsonService jacksonJsonService(ObjectMapper objectMapper) {
        return new JacksonJsonService(objectMapper, jsonProperties);
    }

    /**
     * 配置 JacksonUtil
     */
    @Bean
    @ConditionalOnMissingBean(JacksonUtil.class)
    public JacksonUtil jacksonUtil(ObjectMapper objectMapper) {
        return new JacksonUtil(objectMapper);
    }

    /**
     * 配置 JsonFactory
     */
    @Bean
    public JsonFactory jsonFactoryInitializer(ObjectMapper objectMapper) {
        JsonFactory.setGlobalObjectMapper(objectMapper);
        JsonFactory.setGlobalProperties(jsonProperties);
        return new JsonFactory();
    }
}
