package com.carlos.audit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 审计日志配置属性
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Data
@Component
@ConfigurationProperties(prefix = "carlos.audit")
public class AuditProperties {

    /**
     * 是否启用审计日志
     */
    private boolean enabled = true;

    /**
     * Disruptor 配置
     */
    private Disruptor disruptor = new Disruptor();

    /**
     * ClickHouse 配置
     */
    private ClickHouse clickhouse = new ClickHouse();

    /**
     * 批量写入配置
     */
    private BatchWriter batchWriter = new BatchWriter();

    /**
     * 归档配置
     */
    private Archive archive = new Archive();

    @Data
    public static class Disruptor {
        /**
         * RingBuffer 大小（2的幂次）
         */
        private int bufferSize = 1 << 20; // 1,048,576

        /**
         * 消费者线程数
         */
        private int consumerCount = 4;

        /**
         * 等待策略：blocking, busy_spin, lite_blocking, sleeping, yielding
         */
        private String waitStrategy = "blocking";

        /**
         * 是否启用多生产者模式
         */
        private boolean multiProducer = true;
    }

    @Data
    public static class ClickHouse {
        /**
         * 是否启用 ClickHouse
         */
        private boolean enabled = true;

        /**
         * 主机地址
         */
        private String host = "localhost";

        /**
         * 端口
         */
        private int port = 8123;

        /**
         * 数据库名
         */
        private String database = "default";

        /**
         * 用户名
         */
        private String username = "default";

        /**
         * 密码
         */
        private String password = "";

        /**
         * Socket 超时时间（毫秒）
         */
        private int socketTimeout = 300000;

        /**
         * 连接超时时间（毫秒）
         */
        private int connectionTimeout = 10000;
    }

    @Data
    public static class BatchWriter {
        /**
         * 批次大小
         */
        private int batchSize = 500;

        /**
         * 刷新间隔（毫秒）
         */
        private int flushInterval = 1000;

        /**
         * 最大缓冲区大小
         */
        private int maxBufferSize = 10000;

        /**
         * 重试次数
         */
        private int retryTimes = 3;

        /**
         * 失败时是否写入本地文件备份
         */
        private boolean localBackupOnFailure = true;

        /**
         * 本地备份路径
         */
        private String localBackupPath = "./audit-log-backup";
    }

    @Data
    public static class Archive {
        /**
         * 是否启用归档
         */
        private boolean enabled = true;

        /**
         * 冷数据天数（超过此天数的数据将被归档）
         */
        private int coldDays = 7;

        /**
         * 存储类型：OSS, S3, MINIO, LOCAL
         */
        private String storageType = "OSS";

        /**
         * OSS 配置
         */
        private Oss oss = new Oss();

        @Data
        public static class Oss {
            private String endpoint;
            private String accessKeyId;
            private String accessKeySecret;
            private String bucketName;
            private String prefix = "audit-logs/";
        }
    }
}
