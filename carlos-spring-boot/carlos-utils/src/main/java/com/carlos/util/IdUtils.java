package com.carlos.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * id工具类
 *
 * @author Carlos
 * @date 2022/12/2 13:15
 */
public class IdUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * 生成32位时间id
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2022/12/2 13:16
     */
    public static String date32Id() {
        Date date = new Date();
        // 17位长度
        return DateUtil.format(date, FORMATTER) + IdUtil.getSnowflakeNextIdStr();
    }

    public static String code24() {
        Date date = new Date();
        // 17位长度+7=24;
        return DateUtil.format(date, FORMATTER) + RandomUtil.randomNumbers(7);
    }
}
