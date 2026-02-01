package com.carlos.mq.support;

import lombok.Getter;

/**
 * <p>
 * 消息延时级别
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
    NONE(0),
    /**
     * 1秒
     */
    SECOND_1(1),
    SECOND_5(2),
    SECOND_10(3),
    SECOND_30(4),
    MINUTE_1(5),
    MINUTE_2(6),
    MINUTE_3(7),
    MINUTE_4(8),
    MINUTE_5(9),
    MINUTE_6(10),
    MINUTE_7(11),
    MINUTE_8(12),
    MINUTE_9(13),
    MINUTE_10(14),
    MINUTE_20(15),
    MINUTE_30(16),
    HOUR_1(17),
    HOUR_2(18);
    private final int level;

    DelayLevel(int level) {
        this.level = level;
    }
}
