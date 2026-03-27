package com.carlos.json.jackson.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * <p>
 * 空字符串属性过滤器
 * </p>
 *
 * <p>用于在序列化时跳过值为空字符串的字段</p>
 *
 * @author carlos
 * @date 2025/01/15
 */
public class EmptyStringPropertyFilter extends SimpleBeanPropertyFilter {

    public static final String FILTER_NAME = "emptyStringFilter";

    @SuppressWarnings("deprecation")
    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
        throws Exception {
        Object value = writer.getMember().getValue(pojo);
        if (value instanceof String str && str.isEmpty()) {
            // 跳过空字符串字段
            return;
        }
        super.serializeAsField(pojo, jgen, provider, writer);
    }
}
