package com.yunjin.json.jackson.annotation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.enums.Enum;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 枚举字段序列化
 * </p>
 *
 * @author yunjin
 * @date 2021/11/26 9:49
 */
@Slf4j
public class EnumFieldSerializer extends JsonSerializer<BaseEnum> implements ContextualSerializer {


    private EnumField.SerializerType type = EnumField.SerializerType.FULL;

    @Override
    public void serialize(BaseEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            serializers.defaultSerializeNull(gen);
            return;
        }
        switch (type) {
            case FULL:
                Enum vo = value.getEnumVo();
                gen.writeObject(vo);
                break;
            case CODE:
                gen.writeNumber(value.getCode());
                break;
            case DESC:
                gen.writeString(value.getDesc());
                break;
            default:
                gen.writeString(value.toString());
        }
    }


    /**
     * ContextualSerializer  在序列化的时候我们难免需要获取字段或者类的一些信息(字段名, 字段类型, 字段注解, 类名, 类注解 …)
     *
     * @param serializerProvider 参数0
     * @param beanProperty       参数1
     * @return com.fasterxml.jackson.databind.JsonSerializer<?>
     * @author yunjin
     * @date 2021/11/26 9:51
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(beanProperty);
        }

        JavaType type = beanProperty.getType();
        if (type.isTypeOrSubTypeOf(BaseEnum.class)) {
            EnumField annotation = beanProperty.getAnnotation((EnumField.class));
            if (annotation == null) {
                annotation = beanProperty.getContextAnnotation(EnumField.class);
            }
            if (annotation != null) {
                EnumFieldSerializer enumFieldSerializer = new EnumFieldSerializer();
                enumFieldSerializer.type = annotation.type();
                return enumFieldSerializer;
            }
        }
        return this;

    }

}
