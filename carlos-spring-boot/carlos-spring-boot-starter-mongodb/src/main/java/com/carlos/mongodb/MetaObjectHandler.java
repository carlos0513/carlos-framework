package com.carlos.mongodb;

import java.util.Objects;

/**
 * * 元对象字段填充控制器抽象类，实现公共字段自动写入
 *
 * <p>*
 *
 * <p>* 所有入参的 MetaObject 必定是 entity 或其子类的 MetaObject
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
    default MetaObjectHandler setFieldValByName(
        final String fieldName, final Object fieldVal, final MetaObject metaObject) {
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
    static Object getFieldValByName(final String fieldName, final MetaObject metaObject) {
        return MetaObject.hasGetter(fieldName) ? MetaObject.getValue(fieldName) : null;
    }

    //
    // /**
    //  * find the tableInfo cache by metaObject </p>
    //  * 获取 TableInfo 缓存
    //  *
    //  * @param metaObject meta object parameter
    //  * @return TableInfo
    //  * @since 3.3.0
    //  */
    // default TableInfo findTableInfo(MetaObject metaObject) {
    //     return TableInfoHelper.getTableInfo(metaObject.getOriginalObject().getClass());
    // }
    //
    // /**
    //  * @param metaObject metaObject meta object parameter
    //  * @return this
    //  * @since 3.3.0
    //  */
    // default <T, E extends T> MetaObjectHandler strictInsertFill(MetaObject metaObject, String
    // fieldName, Class<T> fieldType, E fieldVal) {
    //     return strictInsertFill(findTableInfo(metaObject), metaObject,
    // Collections.singletonList(StrictFill.of(fieldName, fieldType, fieldVal)));
    // }
    //
    // /**
    //  * @param metaObject metaObject meta object parameter
    //  * @return this
    //  * @since 3.3.0
    //  */
    // default <T, E extends T> MetaObjectHandler strictInsertFill(MetaObject metaObject, String
    // fieldName, Supplier<E> fieldVal, Class<T> fieldType) {
    //     return strictInsertFill(findTableInfo(metaObject), metaObject,
    // Collections.singletonList(StrictFill.of(fieldName, fieldVal, fieldType)));
    // }
    //
    // /**
    //  * @param metaObject metaObject meta object parameter
    //  * @return this
    //  * @since 3.3.0
    //  */
    // default MetaObjectHandler strictInsertFill(TableInfo tableInfo, MetaObject metaObject,
    // List<StrictFill<?, ?>> strictFills) {
    //     return strictFill(true, tableInfo, metaObject, strictFills);
    // }
    //
    // /**
    //  * @param metaObject metaObject meta object parameter
    //  * @return this
    //  * @since 3.3.0
    //  */
    // default <T, E extends T> MetaObjectHandler strictUpdateFill(MetaObject metaObject, String
    // fieldName, Supplier<E> fieldVal, Class<T> fieldType) {
    //     return strictUpdateFill(findTableInfo(metaObject), metaObject,
    // Collections.singletonList(StrictFill.of(fieldName, fieldVal, fieldType)));
    // }
    //
    // /**
    //  * @param metaObject metaObject meta object parameter
    //  * @return this
    //  * @since 3.3.0
    //  */
    // default <T, E extends T> MetaObjectHandler strictUpdateFill(MetaObject metaObject, String
    // fieldName, Class<T> fieldType, E fieldVal) {
    //     return strictUpdateFill(findTableInfo(metaObject), metaObject,
    // Collections.singletonList(StrictFill.of(fieldName, fieldType, fieldVal)));
    // }
    //
    // /**
    //  * @param metaObject metaObject meta object parameter
    //  * @return this
    //  * @since 3.3.0
    //  */
    // default MetaObjectHandler strictUpdateFill(TableInfo tableInfo, MetaObject metaObject,
    // List<StrictFill<?, ?>> strictFills) {
    //     return strictFill(false, tableInfo, metaObject, strictFills);
    // }
    //
    // /**
    //  * 严格填充,只针对非主键的字段,只有该表注解了fill 并且 字段名和字段属性 能匹配到才会进行填充(null 值不填充)
    //  *
    //  * @param insertFill  是否验证在 insert 时填充
    //  * @param tableInfo   cache 缓存
    //  * @param metaObject  metaObject meta object parameter
    //  * @param strictFills 填充信息
    //  * @return this
    //  * @since 3.3.0
    //  */
    // default MetaObjectHandler strictFill(boolean insertFill, TableInfo tableInfo, MetaObject
    // metaObject, List<StrictFill<?, ?>> strictFills) {
    //     if ((insertFill && tableInfo.isWithInsertFill()) || (!insertFill &&
    // tableInfo.isWithUpdateFill())) {
    //         strictFills.forEach(i -> {
    //             final String fieldName = i.getFieldName();
    //             final Class<?> fieldType = i.getFieldType();
    //             tableInfo.getFieldList().stream()
    //                 .filter(j -> j.getProperty().equals(fieldName) &&
    // fieldType.equals(j.getPropertyType()) &&
    //                     ((insertFill && j.isWithInsertFill()) || (!insertFill &&
    // j.isWithUpdateFill()))).findFirst()
    //                 .ifPresent(j -> strictFillStrategy(metaObject, fieldName, i.getFieldVal()));
    //         });
    //     }
    //     return this;
    // }
    //
    // /**
    //  * 填充策略,默认有值不覆盖,如果提供的值为null也不填充
    //  *
    //  * @param metaObject metaObject meta object parameter
    //  * @param fieldName  java bean property name
    //  * @param fieldVal   java bean property value of Supplier
    //  * @return this
    //  * @since 3.3.0
    //  */
    // default MetaObjectHandler fillStrategy(MetaObject metaObject, String fieldName, Object
    // fieldVal) {
    //     if (getFieldValByName(fieldName, metaObject) == null) {
    //         setFieldValByName(fieldName, fieldVal, metaObject);
    //     }
    //     return this;
    // }
    //
    // /**
    //  * 严格模式填充策略,默认有值不覆盖,如果提供的值为null也不填充
    //  *
    //  * @param metaObject metaObject meta object parameter
    //  * @param fieldName  java bean property name
    //  * @param fieldVal   java bean property value of Supplier
    //  * @return this
    //  * @since 3.3.0
    //  */
    // default MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName,
    // Supplier<?> fieldVal) {
    //     if (metaObject.getValue(fieldName) == null) {
    //         Object obj = fieldVal.get();
    //         if (Objects.nonNull(obj)) {
    //             metaObject.setValue(fieldName, obj);
    //         }
    //     }
    //     return this;
    // }
}
