package com.carlos.flowable.service;

import com.carlos.flowable.dto.*;
import com.carlos.flowable.enums.TaskStatusEnum;
import com.carlos.flowable.exception.FlowableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务服务
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowableTaskService {

    private final TaskService taskService;
    private final RuntimeService runtimeService;

    /**
     * 获取待办任务列表
     *
     * @param assignee 处理人
     * @param param    查询参数
     * @return 任务列表
     */
    public List<TaskDTO> getTodoList(String assignee, TaskQueryParam param) {
        TaskQuery query = taskService.createTaskQuery()
            .taskAssignee(assignee)
            .orderByTaskCreateTime().desc();

        applyQueryParams(query, param);

        List<Task> tasks = query.listPage((param.getPageNum() - 1) * param.getPageSize(), param.getPageSize());
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 获取待办任务数量
     *
     * @param assignee 处理人
     * @param param    查询参数
     * @return 任务数量
     */
    public long getTodoCount(String assignee, TaskQueryParam param) {
        TaskQuery query = taskService.createTaskQuery().taskAssignee(assignee);
        applyQueryParams(query, param);
        return query.count();
    }

    /**
     * 获取候选任务列表
     *
     * @param userId      用户ID
     * @param groupIds    用户组列表
     * @param param       查询参数
     * @return 任务列表
     */
    public List<TaskDTO> getCandidateList(String userId, List<String> groupIds, TaskQueryParam param) {
        TaskQuery query = taskService.createTaskQuery();

        if (StringUtils.hasText(userId)) {
            query.taskCandidateUser(userId);
        }
        if (!CollectionUtils.isEmpty(groupIds)) {
            query.taskCandidateGroupIn(groupIds);
        }

        query.orderByTaskCreateTime().desc();
        applyQueryParams(query, param);

        List<Task> tasks = query.listPage((param.getPageNum() - 1) * param.getPageSize(), param.getPageSize());
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务信息
     */
    public TaskDTO getTaskById(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new FlowableException("任务不存在：" + taskId);
        }
        return convertToDTO(task);
    }

    /**
     * 签收任务
     *
     * @param taskId   任务ID
     * @param userId   用户ID
     */
    public void claimTask(String taskId, String userId) {
        taskService.claim(taskId, userId);
        log.info("任务签收成功，任务ID：{}，签收人：{}", taskId, userId);
    }

    /**
     * 取消签收
     *
     * @param taskId 任务ID
     */
    public void unclaimTask(String taskId) {
        taskService.unclaim(taskId);
        log.info("任务取消签收成功，任务ID：{}", taskId);
    }

    /**
     * 完成任务
     *
     * @param param 完成参数
     */
    public void completeTask(CompleteTaskParam param) {
        // 验证任务
        Task task = taskService.createTaskQuery().taskId(param.getTaskId()).singleResult();
        if (task == null) {
            throw new FlowableException("任务不存在：" + param.getTaskId());
        }

        // 设置当前处理人
        if (StringUtils.hasText(param.getAssignee())) {
            Authentication.setAuthenticatedUserId(param.getAssignee());
        }

        // 添加处理意见
        if (StringUtils.hasText(param.getComment())) {
            taskService.addComment(param.getTaskId(), task.getProcessInstanceId(), param.getComment());
        }

        // 完成任务
        if (param.getVariables() != null || param.getTransientVariables() != null) {
            taskService.complete(param.getTaskId(), param.getVariables(), param.getTransientVariables());
        } else {
            taskService.complete(param.getTaskId());
        }

        log.info("任务完成，任务ID：{}，处理人：{}", param.getTaskId(), param.getAssignee());
    }

    /**
     * 委托任务
     *
     * @param param 委托参数
     */
    public void delegateTask(DelegateTaskParam param) {
        Task task = taskService.createTaskQuery().taskId(param.getTaskId()).singleResult();
        if (task == null) {
            throw new FlowableException("任务不存在：" + param.getTaskId());
        }

        if (StringUtils.hasText(param.getAssignee())) {
            Authentication.setAuthenticatedUserId(param.getAssignee());
        }

        taskService.delegateTask(param.getTaskId(), param.getDelegateToUserId());
        log.info("任务委托成功，任务ID：{}，委托人：{}，被委托人：{}",
            param.getTaskId(), param.getAssignee(), param.getDelegateToUserId());
    }

    /**
     * 转办任务
     *
     * @param param 转办参数
     */
    public void transferTask(TransferTaskParam param) {
        Task task = taskService.createTaskQuery().taskId(param.getTaskId()).singleResult();
        if (task == null) {
            throw new FlowableException("任务不存在：" + param.getTaskId());
        }

        // 先设置当前处理人
        if (StringUtils.hasText(param.getAssignee())) {
            Authentication.setAuthenticatedUserId(param.getAssignee());
        }

        // 添加转办意见
        if (StringUtils.hasText(param.getReason())) {
            taskService.addComment(param.getTaskId(), task.getProcessInstanceId(),
                "转办原因：" + param.getReason());
        }

        // 设置新的处理人
        taskService.setAssignee(param.getTaskId(), param.getTransferToUserId());

        log.info("任务转办成功，任务ID：{}，转办人：{}，接收人：{}",
            param.getTaskId(), param.getAssignee(), param.getTransferToUserId());
    }

    /**
     * 驳回任务（上一节点）- 简化实现
     * 注意：实际驳回功能需要根据具体业务流程设计实现
     *
     * @param taskId   当前任务ID
     * @param assignee 处理人
     * @param reason   驳回原因
     */
    public void rejectTask(String taskId, String assignee, String reason) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new FlowableException("任务不存在：" + taskId);
        }

        // 设置当前处理人
        if (StringUtils.hasText(assignee)) {
            Authentication.setAuthenticatedUserId(assignee);
        }

        // 添加驳回意见
        if (StringUtils.hasText(reason)) {
            taskService.addComment(taskId, task.getProcessInstanceId(), "驳回原因：" + reason);
        }

        // 设置驳回变量
        taskService.setVariable(taskId, "rejected", true);
        taskService.setVariable(taskId, "rejectReason", reason);

        // 完成任务（流程需要在设计时考虑驳回路径）
        taskService.complete(taskId);

        log.info("任务驳回成功，任务ID：{}，驳回人：{}", taskId, assignee);
    }

    /**
     * 设置任务处理人
     *
     * @param taskId   任务ID
     * @param assignee 处理人
     */
    public void setAssignee(String taskId, String assignee) {
        taskService.setAssignee(taskId, assignee);
        log.info("设置任务处理人成功，任务ID：{}，处理人：{}", taskId, assignee);
    }

    /**
     * 添加任务候选人
     *
     * @param taskId     任务ID
     * @param candidateUser 候选人
     */
    public void addCandidateUser(String taskId, String candidateUser) {
        taskService.addCandidateUser(taskId, candidateUser);
    }

    /**
     * 删除任务候选人
     *
     * @param taskId        任务ID
     * @param candidateUser 候选人
     */
    public void deleteCandidateUser(String taskId, String candidateUser) {
        taskService.deleteCandidateUser(taskId, candidateUser);
    }

    /**
     * 获取任务变量
     *
     * @param taskId       任务ID
     * @param variableName 变量名
     * @return 变量值
     */
    public Object getVariable(String taskId, String variableName) {
        return taskService.getVariable(taskId, variableName);
    }

    /**
     * 获取任务所有变量
     *
     * @param taskId 任务ID
     * @return 变量Map
     */
    public Map<String, Object> getVariables(String taskId) {
        return taskService.getVariables(taskId);
    }

    /**
     * 设置任务变量
     *
     * @param taskId       任务ID
     * @param variableName 变量名
     * @param value        变量值
     */
    public void setVariable(String taskId, String variableName, Object value) {
        taskService.setVariable(taskId, variableName, value);
    }

    /**
     * 批量设置任务变量
     *
     * @param taskId    任务ID
     * @param variables 变量Map
     */
    public void setVariables(String taskId, Map<String, Object> variables) {
        taskService.setVariables(taskId, variables);
    }

    /**
     * 获取任务审批意见
     *
     * @param taskId 任务ID
     * @return 审批意见列表
     */
    public List<String> getTaskComments(String taskId) {
        return taskService.getTaskComments(taskId)
            .stream()
            .map(org.flowable.engine.task.Comment::getFullMessage)
            .collect(Collectors.toList());
    }

    /**
     * 应用查询参数
     */
    private void applyQueryParams(TaskQuery query, TaskQueryParam param) {
        if (StringUtils.hasText(param.getProcessDefinitionKey())) {
            query.processDefinitionKey(param.getProcessDefinitionKey());
        }
        if (StringUtils.hasText(param.getProcessInstanceId())) {
            query.processInstanceId(param.getProcessInstanceId());
        }
        if (StringUtils.hasText(param.getBusinessKey())) {
            query.processInstanceBusinessKey(param.getBusinessKey());
        }
        if (StringUtils.hasText(param.getTaskName())) {
            query.taskNameLike("%" + param.getTaskName() + "%");
        }
    }

    /**
     * 转换为DTO
     */
    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setTaskDefinitionKey(task.getTaskDefinitionKey());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setExecutionId(task.getExecutionId());
        dto.setAssignee(task.getAssignee());
        dto.setOwner(task.getOwner());
        dto.setPriority(task.getPriority());
        dto.setCreateTime(task.getCreateTime());
        dto.setDueDate(task.getDueDate());
        dto.setClaimTime(task.getClaimTime());

        // 设置状态
        if (task.getDelegationState() == DelegationState.PENDING) {
            dto.setStatus(TaskStatusEnum.DELEGATED);
        } else if (task.getAssignee() != null) {
            dto.setStatus(TaskStatusEnum.CLAIMED);
        } else {
            dto.setStatus(TaskStatusEnum.PENDING);
        }

        return dto;
    }
}
