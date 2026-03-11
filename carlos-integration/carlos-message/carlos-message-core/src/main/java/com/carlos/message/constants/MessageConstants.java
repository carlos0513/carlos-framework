package com.carlos.message.constants;

/**
 * <p>
 * 消息中心常量定义
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
public interface MessageConstants {

    /**
     * 消息ID前缀
     */
    String MESSAGE_ID_PREFIX = "MSG";

    /**
     * 默认优先级
     */
    int DEFAULT_PRIORITY = 3;

    /**
     * 最大重试次数
     */
    int DEFAULT_MAX_RETRY = 3;

    /**
     * 默认重试间隔（毫秒）
     */
    long DEFAULT_RETRY_INTERVAL = 1000L;

    /**
     * 队列相关常量
     */
    interface Queue {
        /**
         * Redis Stream 前缀
         */
        String STREAM_PREFIX = "stream:message";

        /**
         * 延时队列Key
         */
        String DELAY_KEY = "message:delay";

        /**
         * 延时队列数据Key前缀
         */
        String DELAY_DATA_PREFIX = "message:delay:data:";

        /**
         * 消费者组
         */
        String CONSUMER_GROUP = "message-consumer-group";
    }

    /**
     * 缓存相关常量
     */
    interface Cache {
        /**
         * 模板缓存Key前缀
         */
        String TEMPLATE_PREFIX = "message:template:";

        /**
         * 渠道缓存Key前缀
         */
        String CHANNEL_PREFIX = "message:channel:";

        /**
         * 模板缓存时间（分钟）
         */
        long TEMPLATE_EXPIRE = 30;

        /**
         * 渠道缓存时间（分钟）
         */
        long CHANNEL_EXPIRE = 10;
    }

    /**
     * 反馈类型
     */
    interface FeedbackType {
        /**
         * 无
         */
        String NONE = "none";

        /**
         * 详情
         */
        String DETAIL = "detail";

        /**
         * 站内跳转
         */
        String INNER = "inner";

        /**
         * 外链
         */
        String URL = "url";
    }
}
