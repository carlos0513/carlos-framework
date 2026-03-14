package com.carlos.flowable.service;

import com.carlos.flowable.dto.ProcessInstanceDTO;
import com.carlos.flowable.dto.StartProcessParam;
import com.carlos.flowable.enums.ProcessStatusEnum;
import com.carlos.flowable.exception.FlowableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程实例服务
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowableRuntimeService {

    private final RuntimeService runtimeService;
    private final IdentityService identityService;

    /**
     * 启动流程实例
     *
     * @param param 启动参数
     * @return 流程实例信息
     */
    public ProcessInstanceDTO startProcessInstance(StartProcessParam param) {
        // 设置启动人
        if (param.getStartUserId() != null) {
            Authentication.setAuthenticatedUserId(param.getStartUserId());
        }

        // 构建启动参数
        var builder = runtimeService.createProcessInstanceBuilder()
            .processDefinitionKey(param.getProcessDefinitionKey());

        if (param.getBusinessKey() != null) {
            builder.businessKey(param.getBusinessKey());
        }

        if (param.getProcessInstanceName() != null) {
            builder.name(param.getProcessInstanceName());
        }

        if (param.getVariables() != null) {
            builder.variables(param.getVariables());
        }

        // 启动流程
        ProcessInstance processInstance = builder.start();

        log.info("流程实例启动成功，实例ID：{}，定义Key：{}，业务Key：{}",
            processInstance.getId(), param.getProcessDefinitionKey(), param.getBusinessKey());

        return convertToDTO(processInstance);
    }

    /**
     * 根据Key启动流程实例（简化版）
     *
     * @param processDefinitionKey 流程定义Key
     * @param businessKey          业务Key
     * @param variables            流程变量
     * @return 流程实例信息
     */
    public ProcessInstanceDTO startProcessInstanceByKey(String processDefinitionKey,
                                                        String businessKey,
                                                        Map<String, Object> variables) {
        ProcessInstance processInstance;
        if (variables != null) {
            processInstance = runtimeService
                .startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        } else {
            processInstance = runtimeService
                .startProcessInstanceByKey(processDefinitionKey, businessKey);
        }
        log.info("流程实例启动成功，实例ID：{}，定义Key：{}", processInstance.getId(), processDefinitionKey);
        return convertToDTO(processInstance);
    }

    /**
     * 根据ID启动流程实例
     *
     * @param processDefinitionId 流程定义ID
     * @param businessKey         业务Key
     * @param variables           流程变量
     * @return 流程实例信息
     */
    public ProcessInstanceDTO startProcessInstanceById(String processDefinitionId,
                                                       String businessKey,
                                                       Map<String, Object> variables) {
        ProcessInstance processInstance;
        if (variables != null) {
            processInstance = runtimeService
                .startProcessInstanceById(processDefinitionId, businessKey, variables);
        } else {
            processInstance = runtimeService
                .startProcessInstanceById(processDefinitionId, businessKey);
        }
        log.info("流程实例启动成功，实例ID：{}，定义ID：{}", processInstance.getId(), processDefinitionId);
        return convertToDTO(processInstance);
    }

    /**
     * 获取流程实例列表
     *
     * @param processDefinitionKey 流程定义Key
     * @param processDefinitionId  流程定义ID
     * @param businessKey          业务Key
     * @param involvedUser         涉及用户
     * @param suspended            是否挂起
     * @return 流程实例列表
     */
    public List<ProcessInstanceDTO> listProcessInstances(String processDefinitionKey,
                                                         String processDefinitionId,
                                                         String businessKey,
                                                         String involvedUser,
                                                         Boolean suspended) {
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();

        if (processDefinitionKey != null) {
            query.processDefinitionKey(processDefinitionKey);
        }
        if (processDefinitionId != null) {
            query.processDefinitionId(processDefinitionId);
        }
        if (businessKey != null) {
            query.processInstanceBusinessKey(businessKey);
        }
        if (involvedUser != null) {
            query.involvedUser(involvedUser);
        }
        if (suspended != null) {
            if (suspended) {
                query.suspended();
            } else {
                query.active();
            }
        }

        List<ProcessInstance> list = query.orderByStartTime().desc().list();
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 根据ID获取流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 流程实例信息
     */
    public ProcessInstanceDTO getProcessInstanceById(String processInstanceId) {
        ProcessInstance processInstance = runtimeService
            .createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
        if (processInstance == null) {
            throw new FlowableException("流程实例不存在：" + processInstanceId);
        }
        return convertToDTO(processInstance);
    }

    /**
     * 根据业务Key获取流程实例
     *
     * @param businessKey 业务Key
     * @return 流程实例信息
     */
    public List<ProcessInstanceDTO> getProcessInstanceByBusinessKey(String businessKey) {
        List<ProcessInstance> list = runtimeService
            .createProcessInstanceQuery()
            .processInstanceBusinessKey(businessKey)
            .list();
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 挂起流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    public void suspendProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
        log.info("流程实例已挂起：{}", processInstanceId);
    }

    /**
     * 激活流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    public void activateProcessInstance(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
        log.info("流程实例已激活：{}", processInstanceId);
    }

    /**
     * 终止流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param reason            终止原因
     */
    public void terminateProcessInstance(String processInstanceId, String reason) {
        runtimeService.deleteProcessInstance(processInstanceId, reason);
        log.info("流程实例已终止：{}，原因：{}", processInstanceId, reason);
    }

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param reason            删除原因
     * @param cascade           是否级联删除
     */
    public void deleteProcessInstance(String processInstanceId, String reason, boolean cascade) {
        runtimeService.deleteProcessInstance(processInstanceId, reason);
        log.info("流程实例已删除：{}，原因：{}", processInstanceId, reason);
    }

    /**
     * 设置流程变量
     *
     * @param processInstanceId 流程实例ID
     * @param variableName      变量名
     * @param value             变量值
     */
    public void setVariable(String processInstanceId, String variableName, Object value) {
        runtimeService.setVariable(processInstanceId, variableName, value);
    }

    /**
     * 批量设置流程变量
     *
     * @param processInstanceId 流程实例ID
     * @param variables         变量Map
     */
    public void setVariables(String processInstanceId, Map<String, Object> variables) {
        runtimeService.setVariables(processInstanceId, variables);
    }

    /**
     * 获取流程变量
     *
     * @param processInstanceId 流程实例ID
     * @param variableName      变量名
     * @return 变量值
     */
    public Object getVariable(String processInstanceId, String variableName) {
        return runtimeService.getVariable(processInstanceId, variableName);
    }

    /**
     * 获取所有流程变量
     *
     * @param processInstanceId 流程实例ID
     * @return 变量Map
     */
    public Map<String, Object> getVariables(String processInstanceId) {
        return runtimeService.getVariables(processInstanceId);
    }

    /**
     * 判断流程实例是否结束
     *
     * @param processInstanceId 流程实例ID
     * @return 是否结束
     */
    public boolean isProcessInstanceEnded(String processInstanceId) {
        ProcessInstance processInstance = runtimeService
            .createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
        return processInstance == null;
    }

    /**
     * 转换为DTO
     */
    private ProcessInstanceDTO convertToDTO(ProcessInstance processInstance) {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setId(processInstance.getId());
        dto.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        dto.setProcessDefinitionKey(getProcessDefinitionKey(processInstance.getProcessDefinitionId()));
        dto.setName(processInstance.getName());
        dto.setBusinessKey(processInstance.getBusinessKey());
        dto.setStartTime(processInstance.getStartTime());
        dto.setStartUserId(processInstance.getStartUserId());
        dto.setVariables(processInstance.getProcessVariables());

        // 判断流程状态
        if (processInstance.isEnded()) {
            dto.setStatus(ProcessStatusEnum.COMPLETED);
        } else if (processInstance.isSuspended()) {
            dto.setStatus(ProcessStatusEnum.SUSPENDED);
        } else {
            dto.setStatus(ProcessStatusEnum.RUNNING);
        }

        return dto;
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
