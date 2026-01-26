package com.yunjin.snowflake;


import cn.hutool.core.lang.Snowflake;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 雪花算法工具类
 * </p>
 *
 * @author yunjin
 * @date 2021/10/2 0:13
 */
@Component
public class SnowflakeUtil {

    private static Snowflake snowflake;

    public SnowflakeUtil(Snowflake snowflake) {
        SnowflakeUtil.snowflake = snowflake;
    }

    /**
     * 获取长整型的id
     *
     * @return long
     * @author yunjin
     * @date 2020/10/26 0:00
     */
    public static long longId() {
        return snowflake.nextId();
    }

    /**
     * 获取字符串id
     *
     * @return java.lang.String
     * @author yunjin
     * @date 2020/10/26 0:00
     */
    public static String strId() {
        return snowflake.nextIdStr();
    }

}
