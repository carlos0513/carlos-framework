package com.carlos.boot.application;

import lombok.Data;

/**
 * <p>
 * 应用基本信息配置
 * </p>
 *
 * @author yunjin
 * @date 2020/4/9 14:01
 */
@Data
public class ApplicationInfoProperties {

    /**
     * groupId  com.xxxx
     */
    private String groupId;

    /**
     * artifactId   com.xxxx.artifactId
     */
    private String artifactId;
    /**
     * 版本号
     */
    private String version;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 域名
     */
    private String domain;
    /**
     * 所有者
     */
    private String author;

    /**
     * 源码编码格式
     */
    private String sourceEncoding;

    /**
     * 启动包名称
     */
    private String bootName;
}
