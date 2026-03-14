package com.carlos.flowable.service;

import com.carlos.flowable.dto.ProcessDefinitionDTO;
import com.carlos.flowable.exception.FlowableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * 流程定义服务
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowableProcessService {

    private final RepositoryService repositoryService;

    /**
     * 部署流程定义（通过文件）
     *
     * @param file        流程文件
     * @param name        部署名称
     * @param category    分类
     * @param description 描述
     * @return 部署ID
     */
    public String deploy(MultipartFile file, String name, String category, String description) {
        try (InputStream inputStream = file.getInputStream()) {
            Deployment deployment = repositoryService.createDeployment()
                .name(name)
                .category(category)
                .addInputStream(file.getOriginalFilename(), inputStream)
                .deploy();
            log.info("流程部署成功，部署ID：{}，名称：{}", deployment.getId(), name);
            return deployment.getId();
        } catch (IOException e) {
            throw new FlowableException("流程部署失败", e);
        }
    }

    /**
     * 部署流程定义（通过ZIP文件）
     *
     * @param file     ZIP文件
     * @param name     部署名称
     * @param category 分类
     * @return 部署ID
     */
    public String deployByZip(MultipartFile file, String name, String category) {
        try (ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream())) {
            Deployment deployment = repositoryService.createDeployment()
                .name(name)
                .category(category)
                .addZipInputStream(zipInputStream)
                .deploy();
            log.info("流程ZIP部署成功，部署ID：{}，名称：{}", deployment.getId(), name);
            return deployment.getId();
        } catch (IOException e) {
            throw new FlowableException("流程ZIP部署失败", e);
        }
    }

    /**
     * 通过classpath部署流程
     *
     * @param resourcePath 资源路径
     * @param name         部署名称
     * @param category     分类
     * @return 部署ID
     */
    public String deployByClasspath(String resourcePath, String name, String category) {
        Deployment deployment = repositoryService.createDeployment()
            .name(name)
            .category(category)
            .addClasspathResource(resourcePath)
            .deploy();
        log.info("流程Classpath部署成功，部署ID：{}，名称：{}", deployment.getId(), name);
        return deployment.getId();
    }

    /**
     * 获取流程定义列表
     *
     * @param key      流程定义Key
     * @param category 分类
     * @param suspended 是否挂起
     * @return 流程定义列表
     */
    public List<ProcessDefinitionDTO> listProcessDefinitions(String key, String category, Boolean suspended) {
        var query = repositoryService.createProcessDefinitionQuery();
        if (key != null) {
            query.processDefinitionKey(key);
        }
        if (category != null) {
            query.processDefinitionCategory(category);
        }
        if (suspended != null) {
            if (suspended) {
                query.suspended();
            } else {
                query.active();
            }
        }
        List<ProcessDefinition> list = query.orderByProcessDefinitionVersion().desc().list();
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 根据ID获取流程定义
     *
     * @param processDefinitionId 流程定义ID
     * @return 流程定义信息
     */
    public ProcessDefinitionDTO getProcessDefinitionById(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId)
            .singleResult();
        if (processDefinition == null) {
            throw new FlowableException("流程定义不存在：" + processDefinitionId);
        }
        return convertToDTO(processDefinition);
    }

    /**
     * 根据Key获取最新流程定义
     *
     * @param processDefinitionKey 流程定义Key
     * @return 流程定义信息
     */
    public ProcessDefinitionDTO getLatestProcessDefinition(String processDefinitionKey) {
        ProcessDefinition processDefinition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionKey(processDefinitionKey)
            .latestVersion()
            .singleResult();
        if (processDefinition == null) {
            throw new FlowableException("流程定义不存在：" + processDefinitionKey);
        }
        return convertToDTO(processDefinition);
    }

    /**
     * 挂起流程定义
     *
     * @param processDefinitionId 流程定义ID
     */
    public void suspendProcessDefinition(String processDefinitionId) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
        log.info("流程定义已挂起：{}", processDefinitionId);
    }

    /**
     * 激活流程定义
     *
     * @param processDefinitionId 流程定义ID
     */
    public void activateProcessDefinition(String processDefinitionId) {
        repositoryService.activateProcessDefinitionById(processDefinitionId);
        log.info("流程定义已激活：{}", processDefinitionId);
    }

    /**
     * 删除部署
     *
     * @param deploymentId 部署ID
     * @param cascade      是否级联删除
     */
    public void deleteDeployment(String deploymentId, boolean cascade) {
        repositoryService.deleteDeployment(deploymentId, cascade);
        log.info("部署已删除：{}，级联：{}", deploymentId, cascade);
    }

    /**
     * 获取流程定义XML
     *
     * @param processDefinitionId 流程定义ID
     * @return XML内容
     */
    public String getProcessDefinitionXml(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId)
            .singleResult();
        if (processDefinition == null) {
            throw new FlowableException("流程定义不存在：" + processDefinitionId);
        }
        try (InputStream inputStream = repositoryService
            .getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName())) {
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new FlowableException("获取流程定义XML失败", e);
        }
    }

    /**
     * 获取流程图片
     *
     * @param processDefinitionId 流程定义ID
     * @return 图片流
     */
    public InputStream getProcessDiagram(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId)
            .singleResult();
        if (processDefinition == null) {
            throw new FlowableException("流程定义不存在：" + processDefinitionId);
        }
        String diagramResourceName = processDefinition.getDiagramResourceName();
        if (diagramResourceName == null) {
            return null;
        }
        return repositoryService.getResourceAsStream(
            processDefinition.getDeploymentId(), diagramResourceName);
    }

    /**
     * 转换为DTO
     */
    private ProcessDefinitionDTO convertToDTO(ProcessDefinition processDefinition) {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setId(processDefinition.getId());
        dto.setKey(processDefinition.getKey());
        dto.setName(processDefinition.getName());
        dto.setDescription(processDefinition.getDescription());
        dto.setVersion(processDefinition.getVersion());
        dto.setCategory(processDefinition.getCategory());
        dto.setDeploymentId(processDefinition.getDeploymentId());
        dto.setResourceName(processDefinition.getResourceName());
        dto.setDiagramResourceName(processDefinition.getDiagramResourceName());
        dto.setSuspended(processDefinition.isSuspended());

        // 获取部署时间
        Deployment deployment = repositoryService
            .createDeploymentQuery()
            .deploymentId(processDefinition.getDeploymentId())
            .singleResult();
        if (deployment != null) {
            dto.setDeploymentTime(deployment.getDeploymentTime());
        }
        return dto;
    }
}
