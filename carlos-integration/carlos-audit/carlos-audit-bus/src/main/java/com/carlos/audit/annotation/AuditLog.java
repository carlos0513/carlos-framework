package com.carlos.audit.annotation;

import com.carlos.audit.api.pojo.enums.AuditLogCategoryEnum;

import java.lang.annotation.*;

/**
 * <p>
 * 审计日志注解
 * 标注在方法上，自动记录审计日志
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    /**
     * 日志类型（必填），如 "USER_LOGIN"、"ORDER_PAY"、"DATA_EXPORT"
     * 建议格式：业务域_操作，如 ORDER_CREATE、INVOICE_DELETE
     */
    String type();

    /**
     * 日志大类
     */
    AuditLogCategoryEnum category() default AuditLogCategoryEnum.BUSINESS;

    /**
     * 操作描述（SpEL表达式）
     * 例如："'订单支付: ' + #param.orderNo"
     */
    String operation() default "";

    /**
     * 是否记录请求参数
     */
    boolean recordParams() default true;

    /**
     * 是否记录返回值
     */
    boolean recordResult() default false;

    /**
     * 是否异步记录
     */
    boolean async() default true;

    /**
     * 风险等级（0-100）
     * 0-无风险, 30-低风险, 50-中风险, 70-高风险, 100-极高风险
     */
    int riskLevel() default 0;

    /**
     * 目标对象ID（SpEL表达式）
     * 例如："#param.orderNo" 或 "#id"
     */
    String targetId() default "";

    /**
     * 目标对象类型
     */
    String targetType() default "OTHER";

    /**
     * 业务渠道（SpEL表达式）
     */
    String bizChannel() default "";

    /**
     * 业务场景（SpEL表达式）
     */
    String bizScene() default "";
}
