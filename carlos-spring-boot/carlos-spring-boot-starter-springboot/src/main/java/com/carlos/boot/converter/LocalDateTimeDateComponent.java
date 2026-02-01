package com.carlos.boot.converter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.carlos.json.util.ConvertUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 使用@JsonComponent 之后就不需要手动将Jackson的序列化和反序列化手动加入ObjectMapper了。使用这个注解就够了 配置objectMapper的序列化和反序列化 Jackson 序列化和反序列化配置
 * </p>
 *
 * @author carlos
 * @date 2020/4/14 14:26
 */
// @JsonComponent
public class LocalDateTimeDateComponent {


    public static class Serializer extends JsonSerializer<LocalDateTime> implements ContextualSerializer {

        private String patten = DatePattern.NORM_DATETIME_PATTERN;

        @Override
        public void serialize(final LocalDateTime value, final JsonGenerator gen, final SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                final String s = DateUtil.format(value, patten);
                gen.writeString(s);
            }
        }

        @Override
        public JsonSerializer<?> createContextual(final SerializerProvider prov, final BeanProperty property) {
            Optional.ofNullable(property.getAnnotation(JsonFormat.class))
                    .map(JsonFormat::pattern)
                    .ifPresent(patten -> this.patten = patten);
            return this;
        }
    }

    public static class Deserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
            final String date = jp.getText();
            return ConvertUtil.string2LocalDateTime(date);
        }
    }
}


