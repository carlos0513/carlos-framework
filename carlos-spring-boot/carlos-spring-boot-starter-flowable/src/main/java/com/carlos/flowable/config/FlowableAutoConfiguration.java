package com.carlos.flowable.config;

import com.carlos.flowable.service.*;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.engine.*;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Flowable自动配置类
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass({ProcessEngine.class, SpringProcessEngineConfiguration.class})
@EnableConfigurationProperties(FlowableProperties.class)
@ConditionalOnProperty(prefix = "carlos.flowable", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FlowableAutoConfiguration {

    /**
     * Flowable流程引擎配置
     */
    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineConfiguration processEngineConfiguration(
        DataSource dataSource,
        FlowableProperties properties) throws IOException {

        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();

        // 数据源配置
        configuration.setDataSource(dataSource);

        // 数据库Schema更新策略
        configuration.setDatabaseSchemaUpdate("true");

        // 异步执行器配置
        configuration.setAsyncExecutorActivate(properties.isAsyncExecutorEnabled());

        // 历史记录配置
        configuration.setHistoryLevel(parseHistoryLevel(properties.getHistoryLevel()));

        // ID生成器配置
        configuration.setIdGenerator(new org.flowable.engine.impl.db.DbIdGenerator());

        // 自动部署流程定义
        if (properties.isAutoDeploy()) {
            configuration.setDeploymentResources(deployResources(properties.getProcessDefinitionLocation()));
        }

        // 异步执行器线程池配置
        if (properties.isAsyncExecutorEnabled()) {
            FlowableProperties.AsyncExecutorConfig asyncConfig = properties.getAsyncExecutor();
            configuration.setAsyncExecutorCorePoolSize(asyncConfig.getCorePoolSize());
            configuration.setAsyncExecutorMaxPoolSize(asyncConfig.getMaxPoolSize());
            configuration.setAsyncExecutorThreadPoolQueueSize(asyncConfig.getQueueSize());
        }

        log.info("Flowable流程引擎配置完成，历史级别：{}，异步执行器：{}",
            properties.getHistoryLevel(), properties.isAsyncExecutorEnabled());

        return configuration;
    }

    /**
     * Flowable流程引擎
     */
    @Bean
    @ConditionalOnMissingBean
    public ProcessEngine processEngine(SpringProcessEngineConfiguration configuration) {
        return configuration.buildProcessEngine();
    }

    /**
     * RepositoryService - 流程仓库服务
     */
    @Bean
    @ConditionalOnMissingBean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    /**
     * RuntimeService - 流程运行时服务
     */
    @Bean
    @ConditionalOnMissingBean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    /**
     * TaskService - 任务服务
     */
    @Bean
    @ConditionalOnMissingBean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    /**
     * HistoryService - 历史记录服务
     */
    @Bean
    @ConditionalOnMissingBean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    /**
     * ManagementService - 管理服务
     */
    @Bean
    @ConditionalOnMissingBean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    /**
     * IdentityService - 身份服务
     */
    @Bean
    @ConditionalOnMissingBean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    /**
     * DynamicBpmnService - 动态BPMN服务
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService();
    }

    /**
     * 解析历史级别
     */
    private HistoryLevel parseHistoryLevel(String level) {
        return switch (level.toLowerCase()) {
            case "none" -> HistoryLevel.NONE;
            case "activity" -> HistoryLevel.ACTIVITY;
            case "full" -> HistoryLevel.FULL;
            default -> HistoryLevel.AUDIT;
        };
    }

    /**
     * 加载流程定义资源
     */
    private Resource[] deployResources(String locationPattern) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();

        try {
            Resource[] bpmnResources = resolver.getResources(locationPattern);
            for (Resource resource : bpmnResources) {
                resources.add(resource);
                log.debug("加载流程定义文件：{}", resource.getFilename());
            }
        } catch (IOException e) {
            log.warn("未找到流程定义文件，路径：{}", locationPattern);
        }

        // 同时加载 .bpmn 文件
        if (locationPattern.endsWith(".bpmn20.xml")) {
            String bpmnPattern = locationPattern.replace(".bpmn20.xml", ".bpmn");
            try {
                Resource[] bpmnResources = resolver.getResources(bpmnPattern);
                for (Resource resource : bpmnResources) {
                    resources.add(resource);
                    log.debug("加载流程定义文件：{}", resource.getFilename());
                }
            } catch (IOException e) {
                log.debug("未找到 .bpmn 格式的流程定义文件");
            }
        }

        log.info("共加载 {} 个流程定义文件", resources.size());
        return resources.toArray(new Resource[0]);
    }

    /**
     * 流程定义服务
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowableProcessService flowableProcessService(RepositoryService repositoryService) {
        return new FlowableProcessService(repositoryService);
    }

    /**
     * 流程实例服务
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowableRuntimeService flowableRuntimeService(RuntimeService runtimeService, IdentityService identityService) {
        return new FlowableRuntimeService(runtimeService, identityService);
    }

    /**
     * 任务服务
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowableTaskService flowableTaskService(TaskService taskService, RuntimeService runtimeService) {
        return new FlowableTaskService(taskService, runtimeService);
    }

    /**
     * 历史记录服务
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowableHistoryService flowableHistoryService(HistoryService historyService) {
        return new FlowableHistoryService(historyService);
    }

    /**
     * Flowable综合服务（统一入口）
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowableService flowableService(FlowableProcessService processService,
                                           FlowableRuntimeService runtimeService,
                                           FlowableTaskService taskService,
                                           FlowableHistoryService historyService) {
        return new FlowableService(processService, runtimeService, taskService, historyService);
    }
}
