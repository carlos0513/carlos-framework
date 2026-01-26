package com.yunjin.json.jackson.annotation;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.yunjin.core.base.DepartmentInfo;
import com.yunjin.core.interfaces.ApplicationExtend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.List;


/**
 * <p>
 * 部门序列化
 * </p>
 *
 * @author yunjin
 * @date 2022/12/30 11:57
 */
@Slf4j
public class DepartmentCodeSerializer extends JsonSerializer<String> implements ContextualSerializer {


    private DepartmentCodeField.SerializerType type = DepartmentCodeField.SerializerType.NAME;

    private int limit;
    private String separator;


    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            serializers.defaultSerializeNull(gen);
            return;
        }
        try {
            DepartmentInfo department = getDepartment(value);
            if (department == null) {
                gen.writeObject(value);
                return;
            }
            switch (type) {
                case FULL:
                    gen.writeObject(department);
                    break;
                case NAME:
                    gen.writeString(department.getName());
                    break;
                case FULLNAME:
                    List<String> fullName = department.getFullName();
                    if (CollectionUtils.isEmpty(fullName)) {
                        gen.writeObject(value);
                    }
                    gen.writeString(String.join(separator, fullName));
                    break;
                default:
                    gen.writeObject(value);
            }
        } catch (IOException e) {
            log.error("DepartmentCode parse failed");
            gen.writeObject(value);
        }
    }

    private DepartmentInfo getDepartment(String code) {
        ApplicationExtend applicationExtend;
        try {
            applicationExtend = SpringUtil.getBean(ApplicationExtend.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }

        DepartmentInfo vo = applicationExtend.getDepartmentByCode(code, limit);
        if (vo == null) {
            log.warn("Failed to obtain department information, maybe you can override The method 'getDepartmentByCode' in ApplicationExtend");
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
            DepartmentCodeField annotation = property.getAnnotation((DepartmentCodeField.class));
            if (annotation == null) {
                annotation = property.getContextAnnotation(DepartmentCodeField.class);
            }
            if (annotation != null) {
                DepartmentCodeSerializer serializer = new DepartmentCodeSerializer();
                serializer.type = annotation.type();
                serializer.limit = annotation.limit();
                serializer.separator = annotation.separator();
                return serializer;
            }
        }
        return this;
    }
}
