package com.carlos.mongodb;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 元对象字段填充控制器抽象类，实现公共字段自动写入
 * <p>
 * 所有入参的 MetaObject 必定是 entity 或其子类的 MetaObject
 *
 * @author Carlos
 * @date 2021/12/24 14:25
 */
public interface MetaObjectHandler {

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    void insertFill(MetaObject metaObject);

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    void updateFill(MetaObject metaObject);

    /**
     * 通用填充
     *
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value
     * @param metaObject meta object parameter
     */
    default MetaObjectHandler setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject) {
        if (Objects.nonNull(fieldVal) && MetaObject.hasSetter(fieldName)) {
            metaObject.setValue(fieldName, fieldVal);
        }
        return this;
    }

    /**
     * get value from java bean by propertyName
     *
     * @param fieldName  java bean property name
     * @param metaObject parameter wrapper
     * @return 字段值
     */
    default Object getFieldValByName(String fieldName, MetaObject metaObject) {
        return MetaObject.hasGetter(fieldName) ? metaObject.getValue(fieldName) : null;
    }

    /**
     * 严格填充(有值不覆盖)
     *
     * @param metaObject 元对象
     * @param fieldName  字段名
     * @param fieldVal   字段值
     * @return this
     */
    default MetaObjectHandler strictFill(MetaObject metaObject, String fieldName, Object fieldVal) {
        if (getFieldValByName(fieldName, metaObject) == null) {
            setFieldValByName(fieldName, fieldVal, metaObject);
        }
        return this;
    }

    /**
     * 严格填充(有值不覆盖,使用 Supplier)
     *
     * @param metaObject 元对象
     * @param fieldName  字段名
     * @param supplier   值提供者
     * @return this
     */
    default MetaObjectHandler strictFill(MetaObject metaObject, String fieldName, Supplier<?> supplier) {
        if (getFieldValByName(fieldName, metaObject) == null) {
            Object value = supplier.get();
            if (Objects.nonNull(value)) {
                setFieldValByName(fieldName, value, metaObject);
            }
        }
        return this;
    }

    /**
     * 根据字段类型自动填充时间
     *
     * @param metaObject 元对象
     * @param fieldName  时间字段名
     * @param fieldType  字段类型
     */
    default void fillTime(MetaObject metaObject, String fieldName, Class<?> fieldType) {
        if (fieldType == null || fieldName == null) {
            return;
        }

        if (fieldType.equals(LocalDateTime.class)) {
            strictFill(metaObject, fieldName, LocalDateTime::now);
        } else if (fieldType.equals(Date.class)) {
            strictFill(metaObject, fieldName, Date::new);
        }
    }
}
