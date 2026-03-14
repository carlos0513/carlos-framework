package com.carlos.mq.annotation;

import com.carlos.mq.config.MqAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用 MQ 模块注解
 * <p>
 * 在启动类上添加此注解以启用 MQ 功能
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MqAutoConfiguration.class)
public @interface EnableMq {

    /**
     * 是否启用 MQ
     */
    boolean enabled() default true;

    /**
     * 扫描的包路径
     * <p>
     * 用于扫描 @MqListener 注解的方法
     * </p>
     */
    String[] basePackages() default {};

    /**
     * 扫描的类
     * <p>
     * 用于指定包含 @MqListener 注解的类
     * </p>
     */
    Class<?>[] basePackageClasses() default {};
}
