package com.carlos.mq.config.conditional;

import com.carlos.mq.support.MqType;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * MQ 类型条件注解
 * <p>
 * 当指定的 MQ 类型匹配时才会加载 Bean
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMqTypeCondition.class)
public @interface ConditionalOnMqType {

    /**
     * MQ 类型
     *
     * @return MQ 类型
     */
    MqType value();
}
