package com.carlos.flowable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Flowable工作流综合服务（统一入口）
 * <p>
 * 提供对各个子服务的便捷访问
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Slf4j
@RequiredArgsConstructor
public class FlowableService {

    /**
     * 流程定义服务
     */
    private final FlowableProcessService processService;

    /**
     * 流程实例服务
     */
    private final FlowableRuntimeService runtimeService;

    /**
     * 任务服务
     */
    private final FlowableTaskService taskService;

    /**
     * 历史记录服务
     */
    private final FlowableHistoryService historyService;

    /**
     * 获取流程定义服务
     */
    public FlowableProcessService process() {
        return processService;
    }

    /**
     * 获取流程实例服务
     */
    public FlowableRuntimeService runtime() {
        return runtimeService;
    }

    /**
     * 获取任务服务
     */
    public FlowableTaskService task() {
        return taskService;
    }

    /**
     * 获取历史记录服务
     */
    public FlowableHistoryService history() {
        return historyService;
    }
}
