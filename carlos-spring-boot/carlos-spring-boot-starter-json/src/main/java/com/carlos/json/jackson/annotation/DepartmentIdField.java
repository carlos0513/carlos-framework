package com.carlos.json.jackson.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 用户字段序列化
 * </p>
 *
 * @author carlos
 * @date 2021/11/25 18:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = DepartmentIdSerializer.class)
public @interface DepartmentIdField {

    /**
     * 序列化方式
     */
    SerializerType type() default SerializerType.NAME;

    /**
     * 分隔符
     */
    String separator() default "/";

    /**
     * 序列层级
     */
    int limit() default Integer.MAX_VALUE;

    /**
     * <p>
     * 字段序列化模式
     * </p>
     *
     * @author carlos
     * @date 2021/11/25 18:42
     */
    enum SerializerType {

        /**
         * 序列所有信息
         */
        FULL,

        /**
         * 序列当前部门名
         */
        NAME,
        /**
         * 序列每级部门
         */
        FULLNAME
    }

}
