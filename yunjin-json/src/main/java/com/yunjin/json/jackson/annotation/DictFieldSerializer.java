package com.yunjin.json.jackson.annotation;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.yunjin.core.base.Dict;
import com.yunjin.core.interfaces.ApplicationExtend;
import com.yunjin.json.jackson.annotation.DictField.ValueType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * <p>
 * 字典字典序列化
 * </p>
 *
 * @author yunjin
 * @date 2021/11/25 13:57
 */
@Slf4j
public class DictFieldSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private DictField.SerializerType type = DictField.SerializerType.FULL;


    private DictField.ValueType valueType = DictField.ValueType.CODE;


    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            serializers.defaultSerializeNull(gen);
            return;
        }

        Dict vo = getDictVo(value, valueType);
        if (vo == null) {
            gen.writeString(value);
            return;
        }
        switch (type) {
            case FULL:
                gen.writeObject(vo);
                break;
            case NAME:
                gen.writeString(vo.getName());
                break;
            case CODE:
                gen.writeString(vo.getCode());
                break;
            default:
                gen.writeString(value);
        }
    }

    private Dict getDictVo(String value, DictField.ValueType valueType) {
        ApplicationExtend applicationExtend;
        try {
            applicationExtend = SpringUtil.getBean(ApplicationExtend.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
        Dict dict = null;
        if (valueType == DictField.ValueType.CODE) {
            try {
                dict = applicationExtend.getDictVo(value);
            } catch (Exception e) {
                log.error("get dict error: code:{}", value, e);
            }
        }
        if (valueType == ValueType.ID) {
            try {
                dict = applicationExtend.getDictById(value);
            } catch (Exception e) {
                log.error("get dict error: id:{}", value, e);
            }
        }
        return dict;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return serializerProvider.findNullValueSerializer(property);
        }

        JavaType type = property.getType();
        if (type.isTypeOrSubTypeOf(String.class)) {
            DictField annotation = property.getAnnotation((DictField.class));
            if (annotation == null) {
                annotation = property.getContextAnnotation(DictField.class);
            }
            if (annotation != null) {
                DictFieldSerializer serializer = new DictFieldSerializer();
                serializer.type = annotation.type();
                serializer.valueType = annotation.valueType();
                return serializer;
            }
        }
        return this;
    }
}
