package com.carlos.flowable.service;

import com.carlos.flowable.dto.HistoricTaskDTO;
import com.carlos.flowable.dto.ProcessInstanceDTO;
import com.carlos.flowable.enums.ProcessStatusEnum;
import com.carlos.flowable.exception.FlowableException;
import com.carlos.flowable.vo.HistoricActivityVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 历史记录服务
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowableHistoryService {

    private final HistoryService historyService;

    /**
     * 获取已办任务列表
     *
     * @param assignee   处理人
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 历史任务列表
     */
    public List<HistoricTaskDTO> getDoneList(String assignee, int pageNum, int pageSize) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
            .taskAssignee(assignee)
            .finished()
            .orderByHistoricTaskInstanceEndTime().desc()
            .listPage((pageNum - 1) * pageSize, pageSize);

        return list.stream().map(this::convertToHistoricTaskDTO).collect(Collectors.toList());
    }

    /**
     * 获取已办任务数量
     *
     * @param assignee 处理人
     * @return 任务数量
     */
    public long getDoneCount(String assignee) {
        return historyService.createHistoricTaskInstanceQuery()
            .taskAssignee(assignee)
            .finished()
            .count();
    }

    /**
     * 获取历史任务详情
     *
     * @param taskId 任务ID
     * @return 历史任务信息
     */
    public HistoricTaskDTO getHistoricTaskById(String taskId) {
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery()
            .taskId(taskId)
            .singleResult();
        if (task == null) {
            throw new FlowableException("历史任务不存在：" + taskId);
        }
        return convertToHistoricTaskDTO(task);
    }

    /**
     * 获取流程实例的历史任务列表
     *
     * @param processInstanceId 流程实例ID
     * @return 历史任务列表
     */
    public List<HistoricTaskDTO> getHistoricTasksByProcessInstanceId(String processInstanceId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(processInstanceId)
            .orderByHistoricTaskInstanceEndTime().desc()
            .list();

        return list.stream().map(this::convertToHistoricTaskDTO).collect(Collectors.toList());
    }

    /**
     * 获取历史流程实例列表
     *
     * @param processDefinitionKey 流程定义Key
     * @param businessKey          业务Key
     * @param startedBy            启动人
     * @param finished             是否已完成
     * @param pageNum              页码
     * @param pageSize             每页大小
     * @return 历史流程实例列表
     */
    public List<ProcessInstanceDTO> getHistoricProcessInstances(String processDefinitionKey,
                                                                String businessKey,
                                                                String startedBy,
                                                                Boolean finished,
                                                                int pageNum,
                                                                int pageSize) {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();

        if (StringUtils.hasText(processDefinitionKey)) {
            query.processDefinitionKey(processDefinitionKey);
        }
        if (StringUtils.hasText(businessKey)) {
            query.processInstanceBusinessKey(businessKey);
        }
        if (StringUtils.hasText(startedBy)) {
            query.startedBy(startedBy);
        }
        if (finished != null) {
            if (finished) {
                query.finished();
            } else {
                query.unfinished();
            }
        }

        List<HistoricProcessInstance> list = query
            .orderByProcessInstanceStartTime().desc()
            .listPage((pageNum - 1) * pageSize, pageSize);

        return list.stream().map(this::convertToProcessInstanceDTO).collect(Collectors.toList());
    }

    /**
     * 获取历史流程实例数量
     *
     * @param processDefinitionKey 流程定义Key
     * @param businessKey          业务Key
     * @param startedBy            启动人
     * @param finished             是否已完成
     * @return 数量
     */
    public long getHistoricProcessInstanceCount(String processDefinitionKey,
                                                String businessKey,
                                                String startedBy,
                                                Boolean finished) {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();

        if (StringUtils.hasText(processDefinitionKey)) {
            query.processDefinitionKey(processDefinitionKey);
        }
        if (StringUtils.hasText(businessKey)) {
            query.processInstanceBusinessKey(businessKey);
        }
        if (StringUtils.hasText(startedBy)) {
            query.startedBy(startedBy);
        }
        if (finished != null) {
            if (finished) {
                query.finished();
            } else {
                query.unfinished();
            }
        }

        return query.count();
    }

    /**
     * 获取历史流程实例详情
     *
     * @param processInstanceId 流程实例ID
     * @return 历史流程实例信息
     */
    public ProcessInstanceDTO getHistoricProcessInstanceById(String processInstanceId) {
        HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
        if (instance == null) {
            throw new FlowableException("历史流程实例不存在：" + processInstanceId);
        }
        return convertToProcessInstanceDTO(instance);
    }

    /**
     * 获取流程活动历史记录
     *
     * @param processInstanceId 流程实例ID
     * @return 活动历史列表
     */
    public List<HistoricActivityVO> getHistoricActivities(String processInstanceId) {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(processInstanceId)
            .orderByHistoricActivityInstanceStartTime().asc()
            .list();

        return list.stream().map(this::convertToActivityVO).collect(Collectors.toList());
    }

    /**
     * 获取流程变量历史
     *
     * @param processInstanceId 流程实例ID
     * @return 变量Map
     */
    public Map<String, Object> getHistoricVariables(String processInstanceId) {
        Map<String, Object> variables = new HashMap<>();
        historyService.createHistoricVariableInstanceQuery()
            .processInstanceId(processInstanceId)
            .list()
            .forEach(v -> variables.put(v.getVariableName(), v.getValue()));
        return variables;
    }

    /**
     * 删除历史流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    public void deleteHistoricProcessInstance(String processInstanceId) {
        historyService.deleteHistoricProcessInstance(processInstanceId);
        log.info("历史流程实例已删除：{}", processInstanceId);
    }

    /**
     * 转换为历史任务DTO
     */
    private HistoricTaskDTO convertToHistoricTaskDTO(HistoricTaskInstance task) {
        HistoricTaskDTO dto = new HistoricTaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setTaskDefinitionKey(task.getTaskDefinitionKey());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setAssignee(task.getAssignee());
        dto.setCreateTime(task.getCreateTime());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());
        dto.setDurationInMillis(task.getDurationInMillis());
        dto.setDeleteReason(task.getDeleteReason());
        return dto;
    }

    /**
     * 转换为流程实例DTO
     */
    private ProcessInstanceDTO convertToProcessInstanceDTO(HistoricProcessInstance instance) {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setId(instance.getId());
        dto.setProcessDefinitionId(instance.getProcessDefinitionId());
        dto.setProcessDefinitionKey(getProcessDefinitionKey(instance.getProcessDefinitionId()));
        dto.setProcessDefinitionName(instance.getProcessDefinitionName());
        dto.setName(instance.getName());
        dto.setBusinessKey(instance.getBusinessKey());
        dto.setStartTime(instance.getStartTime());
        dto.setEndTime(instance.getEndTime());
        dto.setStartUserId(instance.getStartUserId());

        // 设置状态
        if (instance.getEndTime() != null) {
            dto.setStatus(ProcessStatusEnum.COMPLETED);
        } else {
            dto.setStatus(ProcessStatusEnum.RUNNING);
        }

        return dto;
    }

    /**
     * 转换为活动VO
     */
    private HistoricActivityVO convertToActivityVO(HistoricActivityInstance activity) {
        HistoricActivityVO vo = new HistoricActivityVO();
        vo.setId(activity.getId());
        vo.setActivityName(activity.getActivityName());
        vo.setActivityType(activity.getActivityType());
        vo.setProcessInstanceId(activity.getProcessInstanceId());
        vo.setExecutionId(activity.getExecutionId());
        vo.setTaskId(activity.getTaskId());
        vo.setAssignee(activity.getAssignee());
        vo.setStartTime(activity.getStartTime());
        vo.setEndTime(activity.getEndTime());
        vo.setDurationInMillis(activity.getDurationInMillis());
        return vo;
    }

    /**
     * 从流程定义ID提取Key
     */
    private String getProcessDefinitionKey(String processDefinitionId) {
        if (processDefinitionId == null) {
            return null;
        }
        int colonIndex = processDefinitionId.indexOf(':');
        return colonIndex > 0 ? processDefinitionId.substring(0, colonIndex) : processDefinitionId;
    }
}
