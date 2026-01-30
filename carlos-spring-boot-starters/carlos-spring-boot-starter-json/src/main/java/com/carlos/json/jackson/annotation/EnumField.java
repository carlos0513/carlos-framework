package com.carlos.json.jackson.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 枚举字段注解
 * </p>
 *
 * @author carlos
 * @date 2021/11/25 18:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = EnumFieldSerializer.class)
public @interface EnumField {

    /**
     * 枚举序列化方式
     */
    SerializerType type() default SerializerType.FULL;


    /**
     * <p>
     * 枚举字段序列化模式
     * </p>
     *
     * @author carlos
     * @date 2021/11/25 18:42
     */
    enum SerializerType {

        /**
         * 序列化枚举所有信息
         */
        FULL,

        /**
         * 序列化成枚举描述
         */
        DESC,

        /**
         * 序列化成枚举code
         */
        CODE,
    }

}
