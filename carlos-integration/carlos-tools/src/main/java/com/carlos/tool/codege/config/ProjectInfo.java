package com.carlos.tool.codege.config;

import com.carlos.tool.codege.entity.TemplateConfig;
import com.carlos.tool.codege.enums.StructureTypeEnum;
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
     * 数据库类型
     */
    private StructureTypeEnum structure = StructureTypeEnum.SINGLE_MODULE;
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
     * artifactId   com.xxxx.artifactId
     */
    private String artifactId;
    /**
     * 生成代码保存路径
     */
    private String path;
    /**
     * 生成代码保存路径
     */
    private String author;
    /**
     * 生成代码保存路径
     */
    private String email;
    /**
     * 是否自动打开
     */
    private boolean autoOpen;
    /**
     * 模板信息
     *
     * @since 3.0
     */
    private TemplateConfig templateConfig;
}
