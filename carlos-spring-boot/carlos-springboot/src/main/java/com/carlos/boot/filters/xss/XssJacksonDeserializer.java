package com.carlos.boot.filters.xss;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.apache.commons.text.StringEscapeUtils;

/**
 * <p>
 * Jackson请求参数字符串转义处理
 * </p>
 *
 * @author yunjin
 * @date 2020/5/9 11:31
 */
public class XssJacksonDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return StringEscapeUtils.escapeHtml4(jsonParser.getText());
    }

}
