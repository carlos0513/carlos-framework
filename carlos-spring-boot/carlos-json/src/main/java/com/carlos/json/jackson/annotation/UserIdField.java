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
 * @author yunjin
 * @date 2021/11/25 18:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = UserIdSerializer.class)
public @interface UserIdField {

    /**
     * 序列化方式
     */
    SerializerType type() default SerializerType.NAME;


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
         * 序列化成用户名
         */
        NAME,

        /**
         * 昵称
         */
        NICKNAME,

        /**
         * 真实名字
         */
        REALNAME
    }

}
