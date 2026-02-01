package com.carlos.snowflake;

/**
 * <p>
 * 雪花算法相关常量
 * </p>
 *
 * @author carlos
 * @date 2021/12/13 11:46
 */
public interface SnowflakeConstant {

    // 最大支持机器节点数0~31，一共32个
    long MAX_WORKER_ID = 31;
    // 最大支持数据中心节点数0~31，一共32个
    long MAX_DATA_CENTER_ID = 31;


    String NAMESPACE = "snowflake";
}
