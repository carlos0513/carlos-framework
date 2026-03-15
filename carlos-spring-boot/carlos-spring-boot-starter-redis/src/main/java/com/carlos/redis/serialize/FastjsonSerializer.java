package com.carlos.redis.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * Fastjson JSON 序列化实现
 * <p>
 * 特点：性能较好，功能丰富，但需要注意安全性配置
 * </p>
 *
 * @author carlos
 * @date 2026-03-14
 */
@Slf4j
public class FastjsonSerializer implements RedisSerializerStrategy {

    private final FastJsonConfig fastJsonConfig;

    public FastjsonSerializer() {
        this.fastJsonConfig = createFastJsonConfig();
        // 配置白名单，防止反序列化漏洞
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        ParserConfig.getGlobalInstance().addAccept("com.carlos");
        ParserConfig.getGlobalInstance().addAccept("java.util");
        ParserConfig.getGlobalInstance().addAccept("java.lang");
        ParserConfig.getGlobalInstance().addAccept("java.time");
        ParserConfig.getGlobalInstance().addAccept("java.math");
    }

    /**
     * 创建 Fastjson 配置
     */
    private FastJsonConfig createFastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();

        // 序列化特性
        config.setSerializerFeatures(
            // 输出格式化
            // SerializerFeature.PrettyFormat,
            // 输出空值
            SerializerFeature.WriteMapNullValue,
            // 空字符串输出为 ""
            SerializerFeature.WriteNullStringAsEmpty,
            // List 空值输出为 []
            SerializerFeature.WriteNullListAsEmpty,
            // Boolean 空值输出为 false
            SerializerFeature.WriteNullBooleanAsFalse,
            // 禁用循环引用检测（保持对象结构）
            SerializerFeature.DisableCircularReferenceDetect,
            // 日期格式化
            SerializerFeature.WriteDateUseDateFormat
        );

        // 日期格式
        config.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        // 字符集
        config.setCharset(StandardCharsets.UTF_8);

        return config;
    }

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) {
            return new byte[0];
        }
        try {
            String json = JSON.toJSONString(obj, fastJsonConfig.getSerializeConfig(),
                fastJsonConfig.getSerializerFeatures());
            return json.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Fastjson serialize error", e);
            throw new SerializationException("Fastjson serialize error: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            String json = new String(bytes, StandardCharsets.UTF_8);
            return JSON.parseObject(json, clazz, fastJsonConfig.getParserConfig(),
                fastJsonConfig.getFeatures());
        } catch (Exception e) {
            log.error("Fastjson deserialize error", e);
            throw new SerializationException("Fastjson deserialize error: " + e.getMessage(), e);
        }
    }

    @Override
    public String getType() {
        return "fastjson";
    }

    /**
     * 序列化异常
     */
    public static class SerializationException extends RuntimeException {
        public SerializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
