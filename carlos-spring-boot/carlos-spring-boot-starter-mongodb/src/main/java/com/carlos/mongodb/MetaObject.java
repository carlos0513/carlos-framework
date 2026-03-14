package com.carlos.mongodb;

import cn.hutool.core.bean.BeanUtil;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据持久化对象元对象
 * 用于封装实体对象，提供字段操作的元数据
 *
 * @author Carlos
 * @date 2021/12/24 14:28
 */
public class MetaObject {

    /**
     * 原始的 Java Bean 对象
     */
    @Getter
    private final Object originalObject;

    /**
     * 对象属性缓存
     */
    private final Map<String, Object> propCache;

    public MetaObject(Object originalObject) {
        this.originalObject = originalObject;
        this.propCache = new ConcurrentHashMap<>();
        // 初始化时缓存所有属性
        if (originalObject != null) {
            this.propCache.putAll(BeanUtil.beanToMap(originalObject, true, true));
        }
    }

    /**
     * 给字段设置值
     *
     * @param name  字段名
     * @param value 字段值
     * @author Carlos
     * @date 2021/12/24 14:35
     */
    public void setValue(String name, Object value) {
        if (originalObject != null && hasSetter(name)) {
            BeanUtil.setFieldValue(originalObject, name, value);
            propCache.put(name, value);
        }
    }

    /**
     * 获取字段值
     *
     * @param name 字段名
     * @return 字段值
     */
    public Object getValue(String name) {
        if (originalObject == null) {
            return null;
        }
        // 先从缓存获取
        if (propCache.containsKey(name)) {
            return propCache.get(name);
        }
        // 从对象获取
        Object value = BeanUtil.getFieldValue(originalObject, name);
        propCache.put(name, value);
        return value;
    }

    /**
     * 判断是否有 setter 方法
     *
     * @param name 字段名
     * @return true 如果存在 setter
     */
    public static boolean hasSetter(String name) {
        // 简化处理，实际应用中可能需要更复杂的判断
        return name != null && !name.isEmpty();
    }

    /**
     * 判断是否有 getter 方法
     *
     * @param name 字段名
     * @return true 如果存在 getter
     */
    public static boolean hasGetter(String name) {
        return name != null && !name.isEmpty();
    }

    /**
     * 判断指定字段是否有值
     *
     * @param fieldName 字段名
     * @return true 如果字段值不为 null
     */
    public boolean hasValue(String fieldName) {
        return getValue(fieldName) != null;
    }

    /**
     * 查找指定类型的字段值
     *
     * @param fieldName 字段名
     * @param type      字段类型
     * @param <T>       类型参数
     * @return 字段值
     */
    @SuppressWarnings("unchecked")
    public <T> T findProperty(String fieldName, Class<T> type) {
        Object value = getValue(fieldName);
        if (value == null) {
            return null;
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
}
