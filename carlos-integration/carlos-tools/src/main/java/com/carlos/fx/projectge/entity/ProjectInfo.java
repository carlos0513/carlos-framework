package com.carlos.fx.projectge.entity;

import com.carlos.fx.codege.entity.TemplateBaseInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 项目相关属性
 * </p>
 *
 * @author Carlos
 * @date 2021/10/28 13:58
 */
@Accessors(chain = true)
@Data
public class ProjectInfo {

    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目描述
     */
    private String describe;
    /**
     * groupId  com.xxxx
     */
    private String groupId;
    /**
     * groupId 拆分 com.xxxx
     */
    private List<String> groupItems;
    /**
     * artifactId   test-abc
     */
    private String artifactId;
    /**
     * 驼峰命名
     */
    private String camelName;
    /**
     * 下划线命名
     */
    private String underlineName;
    /**
     * 生成代码保存路径
     */
    private String path;
    /**
     * 生成代码保存路径
     */
    private String author;
    /**
     * 是否自动打开
     */
    private boolean autoOpen;
    /**
     * 模板信息
     *
     * @since 3.0
     */
    private TemplateBaseInfo selectTemplate;
}
