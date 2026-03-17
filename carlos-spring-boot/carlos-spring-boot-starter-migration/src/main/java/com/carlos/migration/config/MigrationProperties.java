package com.carlos.migration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库迁移配置属性
 * 支持 Liquibase 多数据源配置
 *
 * @author carlos
 * @since 3.0.0
 */
@Data
@ConfigurationProperties(prefix = "carlos.migration")
public class MigrationProperties {

    /**
     * 是否启用迁移功能
     */
    private boolean enabled = true;

    /**
     * 默认数据源配置
     */
    private DataSourceConfig primary = new DataSourceConfig();

    /**
     * 多数据源配置（key为数据源名称）
     */
    private Map<String, DataSourceConfig> multi = new HashMap<>();

    /**
     * 是否启用迁移管理端点（需要spring-web支持）
     */
    private boolean endpointEnabled = false;

    /**
     * 迁移端点路径前缀
     */
    private String endpointPath = "/migration";

    /**
     * 是否启用异步迁移
     */
    private boolean async = false;

    /**
     * 异步迁移线程池大小
     */
    private int asyncThreadPoolSize = 2;

    /**
     * 数据源配置
     */
    @Data
    public static class DataSourceConfig {

        /**
         * 是否启用该数据源的迁移
         */
        private boolean enabled = true;

        /**
         * 变更日志主文件路径
         * 默认：db/changelog/db.changelog-master.yaml
         */
        private String changeLog = "db/changelog/db.changelog-master.yaml";

        /**
         * 数据库上下文环境（用于区分不同环境的变更）
         * 如：develop, test, production
         */
        private String contexts = "";

        /**
         * 标签过滤器
         */
        private String labels = "";

        /**
         * 默认 Schema
         */
        private String defaultSchema;

        /**
         * 是否先清空数据库（危险操作，仅用于开发环境）
         */
        private boolean dropFirst = false;

        /**
         * 是否校验变更集 checksum
         */
        private boolean validateOnMigrate = true;

        /**
         * 执行迁移时是否忽略失败的变更
         */
        private boolean ignoreFailedFutureMigrations = false;

        /**
         * 是否在启动时执行迁移
         */
        private boolean runOnStartup = true;

        /**
         * 回滚时保留的变更集数量
         */
        private int rollbackCount = 1;

        /**
         * 参数配置（用于替换变更日志中的变量）
         */
        private Map<String, String> parameters = new HashMap<>();

        /**
         * Liquibase 数据库变更日志表名
         */
        private String databaseChangeLogTable = "DATABASECHANGELOG";

        /**
         * Liquibase 数据库变更日志锁定表名
         */
        private String databaseChangeLogLockTable = "DATABASECHANGELOGLOCK";
    }
}
