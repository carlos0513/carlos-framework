package com.carlos.flowable.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Flowable配置属性
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
@ConfigurationProperties(prefix = "carlos.flowable")
public class FlowableProperties {

    /**
     * 是否启用Flowable
     */
    private boolean enabled = false;

    /**
     * 是否自动部署流程定义
     */
    private boolean autoDeploy = true;

    /**
     * 流程定义文件位置
     */
    private String processDefinitionLocation = "classpath*:/processes/**/*.bpmn20.xml";

    /**
     * 是否启用历史记录
     */
    private boolean historyEnabled = true;

    /**
     * 历史记录级别: none, activity, audit, full
     */
    private String historyLevel = "audit";

    /**
     * 是否启用异步执行器
     */
    private boolean asyncExecutorEnabled = true;

    /**
     * 异步执行器线程池配置
     */
    private AsyncExecutorConfig asyncExecutor = new AsyncExecutorConfig();

    @Data
    public static class AsyncExecutorConfig {
        /**
         * 核心线程数
         */
        private int corePoolSize = 2;

        /**
         * 最大线程数
         */
        private int maxPoolSize = 10;

        /**
         * 队列容量
         */
        private int queueSize = 100;

        /**
         * 线程保持活动时间（秒）
         */
        private long keepAliveTime = 60;
    }
}
