package com.carlos.flowable.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程定义 DTO
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
public class ProcessDefinitionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义ID
     */
    private String id;

    /**
     * 流程定义Key
     */
    private String key;

    /**
     * 流程定义名称
     */
    private String name;

    /**
     * 流程定义描述
     */
    private String description;

    /**
     * 流程定义版本
     */
    private Integer version;

    /**
     * 分类
     */
    private String category;

    /**
     * 部署ID
     */
    private String deploymentId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 图片资源名称
     */
    private String diagramResourceName;

    /**
     * 是否挂起
     */
    private Boolean suspended;

    /**
     * 部署时间
     */
    private Date deploymentTime;
}
