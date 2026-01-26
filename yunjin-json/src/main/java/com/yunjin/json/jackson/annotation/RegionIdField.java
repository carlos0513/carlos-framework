package com.yunjin.json.jackson.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 区域id字段序列化
 * </p>
 *
 * @author yunjin
 * @date 2021/11/25 18:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = RegionIdSerializer.class)
public @interface RegionIdField {

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
     * @author yunjin
     * @date 2021/11/25 18:42
     */
    enum SerializerType {

        /**
         * 序列所有信息
         */
        FULL,

        /**
         * 序列当前区域名
         */
        NAME,
        /**
         * 序列每级区域名
         */
        FULLNAME
    }

}
