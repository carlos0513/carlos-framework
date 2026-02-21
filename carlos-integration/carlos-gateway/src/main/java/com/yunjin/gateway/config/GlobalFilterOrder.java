package com.carlos.gateway.config;

import org.springframework.core.Ordered;

/**
 * <p>
 *   gateway全局filter顺序定义，所有自定义的全局filter务必采用此接口获取顺序
 *   为了防止顺序冲突，请不要使用数字，而是使用枚举，顺序采用10000/20000间隔性定义，防止后续插入其他filter
 *
 * </p>
 *
 * @author Carlos
 * @date 2025-01-10 11:49
 */
public interface GlobalFilterOrder extends Ordered {


    int ORDER_LAST = -99;

    int ORDER_FIRST = 10000;

    int ORDER_SECOND = 20000;

    int ORDER_THIRD = 30000;

    int ORDER_FOURTH = 40000;

    int ORDER_FIFTH = 50000;

    int ORDER_SIXTH = 60000;

    int ORDER_SEVENTH = 70000;

}
