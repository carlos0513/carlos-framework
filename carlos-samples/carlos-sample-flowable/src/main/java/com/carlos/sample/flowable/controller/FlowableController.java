package com.carlos.sample.flowable.controller;

import com.carlos.boot.flowable.core.dto.*;
import com.carlos.boot.flowable.core.param.*;
import com.carlos.boot.flowable.core.vo.HistoricActivityVO;
import com.carlos.boot.flowable.service.FlowableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *   Flowable工作流演示控制器
 * </p>
 *
 * <p>演示流程部署、启动、任务查询等Flowable工作流功能</p>
 *
 * @author Carlos
 * @date 2026/03/15
 */
@RestController
@RequestMapping("flowable")
@Tag(name = "Flowable工作流示例接口")
@Slf4j
@RequiredArgsConstructor
public class FlowableController {

    private final FlowableService flowableService;

    // ==================== 流程定义管理 ====================

    /**
     * 部署流程定义（通过文件）
     */
    @PostMapping("process/deploy")
    @Operation(summary = "部署流程定义（通过文件）")
    @Parameter(name = "file", description = "BPMN流程定义文件")
    @Parameter(name = "name", description = "流程名称")
    @Parameter(name = "category", description = "流程分类")
    @Parameter(name = "description", description = "流程描述")
    public Result<String> deployProcess(
        @RequestParam("file") MultipartFile file,
        @RequestParam("name") String name,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "description", required = false) String description) {
        try {
            String deploymentId = flowableService.process().deploy(file, name, category, description);
            log.info("流程部署成功，部署ID: {}", deploymentId);
            return Result.ok(deploymentId);
        } catch (Exception e) {
            log.error("流程部署失败", e);
            return Result.fail("流程部署失败: " + e.getMessage());
        }
    }

    /**
     * 获取流程定义列表
     */
    @GetMapping("process/list")
    @Operation(summary = "获取流程定义列表")
    @Parameter(name = "key", description = "流程定义Key")
    @Parameter(name = "category", description = "流程分类")
    public Result<List<ProcessDefinitionDTO>> listProcessDefinitions(
        @RequestParam(value = "key", required = false) String key,
        @RequestParam(value = "category", required = false) String category) {
        List<ProcessDefinitionDTO> list = flowableService.process().listProcessDefinitions(key, category, null);
        return Result.ok(list);
    }

    /**
     * 获取流程定义详情
     */
    @GetMapping("process/detail/{processDefinitionId}")
    @Operation(summary = "获取流程定义详情")
    @Parameter(name = "processDefinitionId", description = "流程定义ID")
    public Result<ProcessDefinitionDTO> getProcessDefinition(@PathVariable String processDefinitionId) {
        ProcessDefinitionDTO definition = flowableService.process().getProcessDefinitionById(processDefinitionId);
        return Result.ok(definition);
    }

    /**
     * 挂起流程定义
     */
    @PostMapping("process/suspend/{processDefinitionId}")
    @Operation(summary = "挂起流程定义")
    @Parameter(name = "processDefinitionId", description = "流程定义ID")
    public Result<Void> suspendProcessDefinition(@PathVariable String processDefinitionId) {
        flowableService.process().suspendProcessDefinition(processDefinitionId);
        return Result.ok();
    }

    /**
     * 激活流程定义
     */
    @PostMapping("process/activate/{processDefinitionId}")
    @Operation(summary = "激活流程定义")
    @Parameter(name = "processDefinitionId", description = "流程定义ID")
    public Result<Void> activateProcessDefinition(@PathVariable String processDefinitionId) {
        flowableService.process().activateProcessDefinition(processDefinitionId);
        return Result.ok();
    }

    /**
     * 删除部署
     */
    @DeleteMapping("process/deploy/{deploymentId}")
    @Operation(summary = "删除部署")
    @Parameter(name = "deploymentId", description = "部署ID")
    public Result<Void> deleteDeployment(@PathVariable String deploymentId) {
        flowableService.process().deleteDeployment(deploymentId, true);
        return Result.ok();
    }

    // ==================== 流程实例管理 ====================

    /**
     * 启动流程实例
     */
    @PostMapping("instance/start")
    @Operation(summary = "启动流程实例")
    public Result<ProcessInstanceDTO> startProcessInstance(@RequestBody StartProcessRequest request) {
        StartProcessParam param = new StartProcessParam();
        param.setProcessDefinitionKey(request.getProcessDefinitionKey());
        param.setBusinessKey(request.getBusinessKey());
        param.setProcessInstanceName(request.getProcessInstanceName());
        param.setStartUserId(request.getStartUserId());
        param.setVariables(request.getVariables());

        ProcessInstanceDTO instance = flowableService.runtime().startProcessInstance(param);
        log.info("流程实例启动成功，实例ID: {}", instance.getProcessInstanceId());
        return Result.ok(instance);
    }

    /**
     * 获取流程实例列表
     */
    @GetMapping("instance/list")
    @Operation(summary = "获取流程实例列表")
    @Parameter(name = "processDefinitionKey", description = "流程定义Key")
    public Result<List<ProcessInstanceDTO>> listProcessInstances(
        @RequestParam(value = "processDefinitionKey", required = false) String processDefinitionKey) {
        List<ProcessInstanceDTO> list = flowableService.runtime().listProcessInstances(
            processDefinitionKey, null, null, null, null);
        return Result.ok(list);
    }

    /**
     * 获取流程实例详情
     */
    @GetMapping("instance/detail/{instanceId}")
    @Operation(summary = "获取流程实例详情")
    @Parameter(name = "instanceId", description = "流程实例ID")
    public Result<ProcessInstanceDTO> getProcessInstance(@PathVariable String instanceId) {
        ProcessInstanceDTO instance = flowableService.runtime().getProcessInstanceById(instanceId);
        return Result.ok(instance);
    }

    /**
     * 终止流程实例
     */
    @PostMapping("instance/terminate/{instanceId}")
    @Operation(summary = "终止流程实例")
    @Parameter(name = "instanceId", description = "流程实例ID")
    @Parameter(name = "reason", description = "终止原因")
    public Result<Void> terminateProcessInstance(
        @PathVariable String instanceId,
        @RequestParam(value = "reason", defaultValue = "用户终止") String reason) {
        flowableService.runtime().terminateProcessInstance(instanceId, reason);
        return Result.ok();
    }

    /**
     * 删除流程实例
     */
    @DeleteMapping("instance/{instanceId}")
    @Operation(summary = "删除流程实例")
    @Parameter(name = "instanceId", description = "流程实例ID")
    @Parameter(name = "reason", description = "删除原因")
    public Result<Void> deleteProcessInstance(
        @PathVariable String instanceId,
        @RequestParam(value = "reason", defaultValue = "用户删除") String reason) {
        flowableService.runtime().deleteProcessInstance(instanceId, reason, true);
        return Result.ok();
    }

    // ==================== 任务管理 ====================

    /**
     * 获取待办任务列表
     */
    @GetMapping("task/todo/{userId}")
    @Operation(summary = "获取待办任务列表")
    @Parameter(name = "userId", description = "用户ID")
    public Result<List<TaskDTO>> getTodoList(@PathVariable String userId) {
        TaskQueryParam param = new TaskQueryParam();
        List<TaskDTO> list = flowableService.task().getTodoList(userId, param);
        return Result.ok(list);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("task/detail/{taskId}")
    @Operation(summary = "获取任务详情")
    @Parameter(name = "taskId", description = "任务ID")
    public Result<TaskDTO> getTaskById(@PathVariable String taskId) {
        TaskDTO task = flowableService.task().getTaskById(taskId);
        return Result.ok(task);
    }

    /**
     * 签收任务
     */
    @PostMapping("task/claim/{taskId}")
    @Operation(summary = "签收任务")
    @Parameter(name = "taskId", description = "任务ID")
    @Parameter(name = "userId", description = "用户ID")
    public Result<Void> claimTask(
        @PathVariable String taskId,
        @RequestParam("userId") String userId) {
        flowableService.task().claimTask(taskId, userId);
        return Result.ok();
    }

    /**
     * 取消签收
     */
    @PostMapping("task/unclaim/{taskId}")
    @Operation(summary = "取消签收任务")
    @Parameter(name = "taskId", description = "任务ID")
    public Result<Void> unclaimTask(@PathVariable String taskId) {
        flowableService.task().unclaimTask(taskId);
        return Result.ok();
    }

    /**
     * 完成任务
     */
    @PostMapping("task/complete")
    @Operation(summary = "完成任务")
    public Result<Void> completeTask(@RequestBody CompleteTaskRequest request) {
        CompleteTaskParam param = new CompleteTaskParam();
        param.setTaskId(request.getTaskId());
        param.setAssignee(request.getAssignee());
        param.setComment(request.getComment());
        param.setVariables(request.getVariables());
        flowableService.task().completeTask(param);
        return Result.ok();
    }

    /**
     * 驳回任务（返回上一节点）
     */
    @PostMapping("task/reject")
    @Operation(summary = "驳回任务")
    public Result<Void> rejectTask(@RequestBody RejectTaskRequest request) {
        flowableService.task().rejectTask(request.getTaskId(), request.getAssignee(), request.getReason());
        return Result.ok();
    }

    /**
     * 委托任务
     */
    @PostMapping("task/delegate")
    @Operation(summary = "委托任务")
    public Result<Void> delegateTask(@RequestBody DelegateTaskRequest request) {
        DelegateTaskParam param = new DelegateTaskParam();
        param.setTaskId(request.getTaskId());
        param.setAssignee(request.getAssignee());
        param.setDelegateToUserId(request.getDelegateToUserId());
        param.setReason(request.getReason());
        flowableService.task().delegateTask(param);
        return Result.ok();
    }

    /**
     * 转办任务
     */
    @PostMapping("task/transfer")
    @Operation(summary = "转办任务")
    public Result<Void> transferTask(@RequestBody TransferTaskRequest request) {
        TransferTaskParam param = new TransferTaskParam();
        param.setTaskId(request.getTaskId());
        param.setAssignee(request.getAssignee());
        param.setTransferToUserId(request.getTransferToUserId());
        param.setReason(request.getReason());
        flowableService.task().transferTask(param);
        return Result.ok();
    }

    // ==================== 历史记录查询 ====================

    /**
     * 获取已办任务列表
     */
    @GetMapping("history/done/{userId}")
    @Operation(summary = "获取已办任务列表")
    @Parameter(name = "userId", description = "用户ID")
    @Parameter(name = "pageNum", description = "页码")
    @Parameter(name = "pageSize", description = "每页大小")
    public Result<List<HistoricTaskDTO>> getDoneList(
        @PathVariable String userId,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<HistoricTaskDTO> list = flowableService.history().getDoneList(userId, pageNum, pageSize);
        return Result.ok(list);
    }

    /**
     * 获取流程历史活动记录
     */
    @GetMapping("history/activities/{instanceId}")
    @Operation(summary = "获取流程历史活动记录")
    @Parameter(name = "instanceId", description = "流程实例ID")
    public Result<List<HistoricActivityVO>> getHistoricActivities(@PathVariable String instanceId) {
        List<HistoricActivityVO> activities = flowableService.history().getHistoricActivities(instanceId);
        return Result.ok(activities);
    }

    /**
     * 获取历史流程实例
     */
    @GetMapping("history/instance/{instanceId}")
    @Operation(summary = "获取历史流程实例")
    @Parameter(name = "instanceId", description = "流程实例ID")
    public Result<ProcessInstanceDTO> getHistoricProcessInstance(@PathVariable String instanceId) {
        ProcessInstanceDTO instance = flowableService.history().getHistoricProcessInstanceById(instanceId);
        return Result.ok(instance);
    }

    // ==================== 请求参数类 ====================

    /**
     * 启动流程请求
     */
    public record StartProcessRequest(
        String processDefinitionKey,
        String businessKey,
        String processInstanceName,
        String startUserId,
        Map<String, Object> variables
    ) {
    }

    /**
     * 完成任务请求
     */
    public record CompleteTaskRequest(
        String taskId,
        String assignee,
        String comment,
        Map<String, Object> variables
    ) {
    }

    /**
     * 驳回任务请求
     */
    public record RejectTaskRequest(
        String taskId,
        String assignee,
        String reason
    ) {
    }

    /**
     * 委托任务请求
     */
    public record DelegateTaskRequest(
        String taskId,
        String assignee,
        String delegateToUserId,
        String reason
    ) {
    }

    /**
     * 转办任务请求
     */
    public record TransferTaskRequest(
        String taskId,
        String assignee,
        String transferToUserId,
        String reason
    ) {
    }

}
