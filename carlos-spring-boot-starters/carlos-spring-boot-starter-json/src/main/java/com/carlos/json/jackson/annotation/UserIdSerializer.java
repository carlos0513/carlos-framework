package com.carlos.json.jackson.annotation;

import cn.hutool.extra.spring.SpringUtil;
import com.carlos.core.base.UserInfo;
import com.carlos.core.interfaces.ApplicationExtend;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;


/**
 * <p>
 * 字典字典序列化
 * </p>
 *
 * @author carlos
 * @date 2021/11/25 13:57
 */
@Slf4j
public class UserIdSerializer extends JsonSerializer<Serializable> implements ContextualSerializer {


    private UserIdField.SerializerType type = UserIdField.SerializerType.NAME;


    @Override
    public void serialize(Serializable value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            serializers.defaultSerializeNull(gen);
            return;
        }

        UserInfo user = getUser(value);
        if (user == null) {
            gen.writeObject(value);
            return;
        }

        switch (type) {
            case FULL:
                gen.writeObject(user);

                break;
            case NAME:
                gen.writeString(user.getName());
                break;
            case NICKNAME:
                gen.writeString(user.getNickName());
                break;
            case REALNAME:
                gen.writeString(user.getRealName());
                break;
            default:
                gen.writeObject(value);
        }
    }

    private UserInfo getUser(Serializable id) {
        ApplicationExtend applicationExtend;
        try {
            applicationExtend = SpringUtil.getBean(ApplicationExtend.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }

        UserInfo vo = applicationExtend.getUserById(id);
        if (vo == null) {
            log.warn("Failed to obtain user information, maybe you can override The method 'getUserById' in ApplicationExtend");
        }
        return vo;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return serializerProvider.findNullValueSerializer(property);
        }

        JavaType type = property.getType();
        if (type.isTypeOrSubTypeOf(String.class)) {
            UserIdField annotation = property.getAnnotation((UserIdField.class));
            if (annotation == null) {
                annotation = property.getContextAnnotation(UserIdField.class);
            }
            if (annotation != null) {
                UserIdSerializer serializer = new UserIdSerializer();
                serializer.type = annotation.type();
                return serializer;
            }
        }
        return this;
    }
}
