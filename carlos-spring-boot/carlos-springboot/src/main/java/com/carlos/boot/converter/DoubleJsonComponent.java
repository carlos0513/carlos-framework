package com.carlos.boot.converter;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.carlos.json.util.ConvertUtil;

import java.io.IOException;

/**
 * <p>
 * 使用@JsonComponent 之后就不需要手动将Jackson的序列化和反序列化手动加入ObjectMapper了。使用这个注解就够了 配置objectMapper的序列化和反序列化 Jackson 序列化和反序列化配置
 * </p>
 *
 * @author yunjin
 * @date 2020/4/14 14:26
 */
// @JsonComponent
public class DoubleJsonComponent {

    public static class DoubleJsonSerializer extends JsonSerializer<Double> {

        @Override
        public void serialize(final Double value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
            if (value == null) {
                return;
            }
            // 去除小数点后面的0
            final String s = NumberUtil.toStr(value, true);
            gen.writeString(s);
        }
    }

    public static class DoubleJsonDeserializer extends JsonDeserializer<Double> {

        @Override
        public Double deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
            final String string = jsonParser.getText();
            return ConvertUtil.string2Double(string);
        }
    }
}


