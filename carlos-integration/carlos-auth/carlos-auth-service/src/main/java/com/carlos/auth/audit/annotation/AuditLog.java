package com.carlos.auth.audit.annotation;

import java.lang.annotation.*;

/**
 * <p>操作审计注解</p>
 *
 * <p>标记需要记录审计日志的方法，会在方法执行前后记录相关数据</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    /**
     * 操作类型
     */
    String operationType();

    /**
     * 资源类型
     */
    String resourceType();

    /**
     * 资源ID参数索引（在方法参数中的位置，从0开始）
     * 默认为-1，表示从返回值中获取
     */
    int resourceIdParamIndex() default -1;

    /**
     * 是否记录返回值
     */
    boolean recordReturnValue() default true;
}
