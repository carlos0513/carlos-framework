package com.carlos.json.jackson;

import com.carlos.json.config.JsonProperties;
import com.carlos.json.core.JsonService;
import com.carlos.json.exception.JsonParseException;
import com.carlos.json.jackson.filter.EmptyStringPropertyFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Jackson 实现的 JSON 服务
 * </p>
 *
 * @author carlos
 * @date 2025/01/15
 */
@Slf4j
public class JacksonJsonService implements JsonService {

    private final ObjectMapper objectMapper;
    private final ObjectMapper prettyObjectMapper;
    private final ObjectMapper withNullsObjectMapper;

    public JacksonJsonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.prettyObjectMapper = objectMapper.copy().enable(SerializationFeature.INDENT_OUTPUT);
        this.withNullsObjectMapper = objectMapper.copy();
    }

    public JacksonJsonService(ObjectMapper objectMapper, JsonProperties properties) {
        this.objectMapper = configureMapper(objectMapper, properties);
        this.prettyObjectMapper = this.objectMapper.copy().enable(SerializationFeature.INDENT_OUTPUT);
        this.withNullsObjectMapper = this.objectMapper.copy();
    }

    private ObjectMapper configureMapper(ObjectMapper mapper, JsonProperties properties) {
        if (properties == null) {
            return mapper;
        }
        // 应用配置
        if (properties.getSerialization().isIndentOutput()) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        if (properties.getSerialization().isFailOnEmptyBeans()) {
            mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        } else {
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        }
        if (properties.isIgnoreNull()) {
            mapper.setDefaultPropertyInclusion(
                com.fasterxml.jackson.annotation.JsonInclude.Value.construct(
                    com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL,
                    com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS));
        }
        if (properties.getDeserialization().isFailOnUnknownProperties()) {
            mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        } else {
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
        if (properties.isIgnoreEmptyString()) {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter(EmptyStringPropertyFilter.FILTER_NAME, new EmptyStringPropertyFilter());
            mapper.setFilterProvider(filterProvider);
        }
        return mapper;
    }

    @Override
    public String toJson(Object object) {
        if (object == null) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Jackson 序列化失败", e);
        }
    }

    @Override
    public String toJsonPretty(Object object) {
        if (object == null) {
            return "{}";
        }
        try {
            return prettyObjectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Jackson 格式化序列化失败", e);
        }
    }

    @Override
    public byte[] toJsonBytes(Object object) {
        if (object == null) {
            return new byte[0];
        }
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Jackson 序列化为字节数组失败", e);
        }
    }

    @Override
    public String toJsonWithNulls(Object object) {
        if (object == null) {
            return "{}";
        }
        try {
            return withNullsObjectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Jackson 序列化（含 null）失败", e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Jackson 反序列化失败", e);
        }
    }

    @Override
    public <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Jackson 反序列化失败", e);
        }
    }

    @Override
    public <T> T fromJson(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new JsonParseException("Jackson 从字节数组反序列化失败", e);
        }
    }

    @Override
    public <T> T fromJson(byte[] bytes, TypeReference<T> typeReference) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, typeReference);
        } catch (IOException e) {
            throw new JsonParseException("Jackson 从字节数组反序列化失败", e);
        }
    }

    @Override
    public <T> T fromJson(InputStream inputStream, Class<T> clazz) {
        if (inputStream == null) {
            return null;
        }
        try {
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new JsonParseException("Jackson 从输入流反序列化失败", e);
        }
    }

    @Override
    public <T> List<T> toList(String json, Class<T> elementClass) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, elementClass);
            return objectMapper.readValue(json, listType);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Jackson 反序列化为 List 失败", e);
        }
    }

    @Override
    public Map<String, Object> toMap(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Jackson 反序列化为 Map 失败", e);
        }
    }

    @Override
    public <V> Map<String, V> toMap(String json, Class<V> valueClass) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            MapType mapType = objectMapper.getTypeFactory()
                .constructMapType(Map.class, String.class, valueClass);
            return objectMapper.readValue(json, mapType);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Jackson 反序列化为 Map 失败", e);
        }
    }

    @Override
    public Map<String, Object> objectToMap(Object object) {
        if (object == null) {
            return null;
        }
        return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }

    @Override
    public <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return objectMapper.convertValue(map, clazz);
    }

    @Override
    public <T> T convertValue(Object fromValue, Class<T> toValueType) {
        if (fromValue == null) {
            return null;
        }
        return objectMapper.convertValue(fromValue, toValueType);
    }

    @Override
    public <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        if (fromValue == null) {
            return null;
        }
        return objectMapper.convertValue(fromValue, toValueTypeRef);
    }

    @Override
    public boolean isValidJson(String json) {
        if (json == null || json.isEmpty()) {
            return false;
        }
        try {
            objectMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public boolean canSerialize(Object value) {
        if (value == null) {
            return true;
        }
        return objectMapper.canSerialize(value.getClass());
    }

    @Override
    public Object extract(String json, String path) {
        if (json == null || json.isEmpty() || path == null || path.isEmpty()) {
            return null;
        }
        try {
            return JsonPath.read(json, path);
        } catch (PathNotFoundException e) {
            log.warn("JSON path not found: {}", path);
            return null;
        }
    }

    @Override
    public <T> T extract(String json, String path, Class<T> clazz) {
        Object result = extract(json, path);
        if (result == null) {
            return null;
        }
        return convertValue(result, clazz);
    }

    @Override
    public String merge(String json1, String json2) {
        if (json1 == null || json1.isEmpty()) {
            return json2;
        }
        if (json2 == null || json2.isEmpty()) {
            return json1;
        }
        try {
            JsonNode node1 = objectMapper.readTree(json1);
            JsonNode node2 = objectMapper.readTree(json2);

            if (!node1.isObject() || !node2.isObject()) {
                throw new JsonParseException("只能合并 JSON 对象");
            }

            ObjectNode result = ((ObjectNode) node1).deepCopy();
            Iterator<Map.Entry<String, JsonNode>> fields = node2.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                result.set(field.getKey(), field.getValue());
            }
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("合并 JSON 失败", e);
        }
    }

    @Override
    public Object getEngine() {
        return objectMapper;
    }

    @Override
    public String getEngineName() {
        return "Jackson";
    }

    /**
     * 获取 ObjectMapper 实例
     *
     * @return ObjectMapper
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * 创建 ObjectNode
     *
     * @return ObjectNode
     */
    public ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    /**
     * 创建 ArrayNode
     *
     * @return ArrayNode
     */
    public ArrayNode createArrayNode() {
        return objectMapper.createArrayNode();
    }
}
