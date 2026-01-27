package com.carlos.json.jackson.annotation;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.carlos.core.base.RegionInfo;
import com.carlos.core.interfaces.ApplicationExtend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 区域id序列化
 * </p>
 *
 * @author yunjin
 * @date 2022/12/30 11:57
 */
@Slf4j
public class RegionIdSerializer extends JsonSerializer<Serializable> implements ContextualSerializer {


    private RegionIdField.SerializerType type = RegionIdField.SerializerType.NAME;

    private int limit;

    private String separator;


    @Override
    public void serialize(Serializable value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            serializers.defaultSerializeNull(gen);
            return;
        }
        try {
            RegionInfo regionInfo = getRegionInfo((String) value);
            if (regionInfo == null) {
                gen.writeObject(value);
                return;
            }
            switch (type) {
                case FULL:
                    gen.writeObject(regionInfo);
                    break;
                case NAME:
                    gen.writeString(regionInfo.getName());
                    break;
                case FULLNAME:
                    List<String> fullName = regionInfo.getFullName();
                    if (CollectionUtils.isEmpty(fullName)) {
                        gen.writeObject(value);
                    }
                    gen.writeString(String.join(separator, fullName));
                    break;
                default:
                    gen.writeObject(value);
            }
        } catch (IOException e) {
            log.error("DepartmentId parse failed");
            gen.writeObject(value);
        }
    }

    public RegionInfo getRegionInfo(String id) {
        ApplicationExtend applicationExtend;
        try {
            applicationExtend = SpringUtil.getBean(ApplicationExtend.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }

        RegionInfo vo = applicationExtend.getRegionInfo(id, limit);
        if (vo == null) {
            log.warn("Failed to obtain user information, maybe you can override The method 'getRegionInfo' in ApplicationExtend");
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
            RegionIdField annotation = property.getAnnotation((RegionIdField.class));
            if (annotation == null) {
                annotation = property.getContextAnnotation(RegionIdField.class);
            }
            if (annotation != null) {
                RegionIdSerializer serializer = new RegionIdSerializer();
                serializer.type = annotation.type();
                serializer.limit = annotation.limit();
                serializer.separator = annotation.separator();
                return serializer;
            }
        }
        return this;
    }

}
