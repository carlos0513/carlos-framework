package com.yunjin.boot.converter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.yunjin.json.util.ConvertUtil;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * 使用@JsonComponent 之后就不需要手动将Jackson的序列化和反序列化手动加入ObjectMapper了。使用这个注解就够了 配置objectMapper的序列化和反序列化 Jackson 序列化和反序列化配置
 * </p>
 *
 * @author yunjin
 * @date 2020/4/14 14:26
 */
@JsonComponent
public class DateJsonComponent {

    public static class JsonDateSerializer extends JsonSerializer<Date> implements ContextualSerializer {

        private String patten = DatePattern.NORM_DATETIME_PATTERN;

        @Override
        public void serialize(final Date date, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
            final String string;
            if (date != null) {
                string = DateUtil.format(date, patten);
                jsonGenerator.writeString(string);
            }
        }

        @Override
        public JsonSerializer<?> createContextual(final SerializerProvider prov, final BeanProperty property) {
            Optional.ofNullable(property)
                    .map(p -> property.getAnnotation(JsonFormat.class))
                    .map(JsonFormat::pattern)
                    .ifPresent(patten -> this.patten = patten);
            return this;
        }
    }


    public static class JsonDateDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
            final String date = jp.getText();
            if (NumberUtil.isLong(date)) {
                return new Date(Long.parseLong(date));
            }
            return ConvertUtil.string2Date(date);
        }
    }

}


