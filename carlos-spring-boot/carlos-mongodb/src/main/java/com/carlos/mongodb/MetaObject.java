package com.carlos.mongodb;

/**
 * 数据持久化对象元对象
 *
 * @author Carlos
 * @date 2021/12/24 14:28
 */
public class MetaObject {

    /**
     * 给字段设置值
     *
     * @param name  字段名
     * @param value 字段值
     * @author Carlos
     * @date 2021/12/24 14:35
     */
    public void setValue(final String name, final Object value) {
    }

    public static Object getValue(final String name) {
        return null;
    }

    public static boolean hasSetter(final String name) {
        return true;
    }

    public static boolean hasGetter(final String name) {
        return true;
    }
}
