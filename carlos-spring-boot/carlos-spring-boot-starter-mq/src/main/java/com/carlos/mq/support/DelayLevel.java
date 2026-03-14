package com.carlos.mq.support;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * 消息延迟级别枚举
 * <p>
 * 支持 RocketMQ 原生延迟级别，同时提供通用延迟时间定义
 * </p>
 *
 * @author Carlos
 * @date 2022/1/18 9:56
 */
@Getter
public enum DelayLevel {

    /**
     * 不延时
     */
    NONE(0, 0, TimeUnit.SECONDS),

    /**
     * 1秒
     */
    SECOND_1(1, 1, TimeUnit.SECONDS),

    /**
     * 5秒
     */
    SECOND_5(2, 5, TimeUnit.SECONDS),

    /**
     * 10秒
     */
    SECOND_10(3, 10, TimeUnit.SECONDS),

    /**
     * 30秒
     */
    SECOND_30(4, 30, TimeUnit.SECONDS),

    /**
     * 1分钟
     */
    MINUTE_1(5, 1, TimeUnit.MINUTES),

    /**
     * 2分钟
     */
    MINUTE_2(6, 2, TimeUnit.MINUTES),

    /**
     * 3分钟
     */
    MINUTE_3(7, 3, TimeUnit.MINUTES),

    /**
     * 4分钟
     */
    MINUTE_4(8, 4, TimeUnit.MINUTES),

    /**
     * 5分钟
     */
    MINUTE_5(9, 5, TimeUnit.MINUTES),

    /**
     * 6分钟
     */
    MINUTE_6(10, 6, TimeUnit.MINUTES),

    /**
     * 7分钟
     */
    MINUTE_7(11, 7, TimeUnit.MINUTES),

    /**
     * 8分钟
     */
    MINUTE_8(12, 8, TimeUnit.MINUTES),

    /**
     * 9分钟
     */
    MINUTE_9(13, 9, TimeUnit.MINUTES),

    /**
     * 10分钟
     */
    MINUTE_10(14, 10, TimeUnit.MINUTES),

    /**
     * 20分钟
     */
    MINUTE_20(15, 20, TimeUnit.MINUTES),

    /**
     * 30分钟
     */
    MINUTE_30(16, 30, TimeUnit.MINUTES),

    /**
     * 1小时
     */
    HOUR_1(17, 1, TimeUnit.HOURS),

    /**
     * 2小时
     */
    HOUR_2(18, 2, TimeUnit.HOURS);

    /**
     * RocketMQ 延迟级别
     */
    private final int level;

    /**
     * 延迟时间值
     */
    private final long time;

    /**
     * 时间单位
     */
    private final TimeUnit unit;

    DelayLevel(int level, long time, TimeUnit unit) {
        this.level = level;
        this.time = time;
        this.unit = unit;
    }

    /**
     * 获取延迟毫秒数
     *
     * @return 毫秒数
     */
    public long toMillis() {
        return unit.toMillis(time);
    }

    /**
     * 获取延迟秒数
     *
     * @return 秒数
     */
    public long toSeconds() {
        return unit.toSeconds(time);
    }

    /**
     * 根据延迟级别获取枚举
     *
     * @param level 级别
     * @return DelayLevel
     */
    public static DelayLevel fromLevel(int level) {
        for (DelayLevel delayLevel : values()) {
            if (delayLevel.level == level) {
                return delayLevel;
            }
        }
        return NONE;
    }

    /**
     * 根据毫秒数查找最接近的延迟级别
     *
     * @param millis 毫秒数
     * @return 最接近的 DelayLevel
     */
    public static DelayLevel fromMillis(long millis) {
        if (millis <= 0) {
            return NONE;
        }
        DelayLevel closest = NONE;
        long minDiff = Long.MAX_VALUE;
        for (DelayLevel delayLevel : values()) {
            long diff = Math.abs(delayLevel.toMillis() - millis);
            if (diff < minDiff) {
                minDiff = diff;
                closest = delayLevel;
            }
        }
        return closest;
    }
}
