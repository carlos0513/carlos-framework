package com.yunjin.json.jackson.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 字典字段序列化
 * </p>
 *
 * @author yunjin
 * @date 2021/11/25 18:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = DictFieldSerializer.class)
public @interface DictField {

    /**
     * 值类型
     */
    ValueType valueType() default ValueType.CODE;

    /**
     * 枚举序列化方式
     */
    SerializerType type() default SerializerType.FULL;


    /**
     * <p>
     * 枚举字段序列化模式
     * </p>
     *
     * @author yunjin
     * @date 2021/11/25 18:42
     */
    enum ValueType {

        /**
         * 字典code
         */
        CODE,

        /**
         * 字典id
         */
        ID
    }

    /**
     * <p>
     * 枚举字段序列化模式
     * </p>
     *
     * @author yunjin
     * @date 2021/11/25 18:42
     */
    enum SerializerType {

        /**
         * 序列化字典所有信息
         */
        FULL,

        /**
         * 序列化成字典选项名称
         */
        NAME,

        /**
         * 序列化成字典选项code
         */
        CODE,
    }

}
