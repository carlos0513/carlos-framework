package com.yunjin.org.metric;


/**
 * <p>
 * 指标类型枚举
 * </p>
 *
 * @author Carlos
 * @date 2025-05-19 23:10
 */
public enum OrgMetricEnum {
    /**
     * 注册用户数
     */
    registerCount,
    /**
     * 禁用用户数
     */
    disableCount,
    /**
     * 活跃用户数
     */
    activeCount,
    /**
     * PC端活跃用户数
     */
    pcActiveCount,
    /**
     * 移动端活跃用户数
     */
    mobileActiveCount,
    /**
     * 活跃用户数V2(PC端活跃用户数+移动端活跃用户数)
     */
    activeCountV2,
    /**
     * 近一年注册用户数
     */
    registerCountInOneYear;
}
