package com.carlos.tool.projectge.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateConfig.ResourceMode;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.carlos.tool.projectge.config.Constant;
import com.carlos.tool.projectge.config.ProjectInfo;
import com.carlos.tool.projectge.entity.SelectTemplate;
import com.carlos.tool.projectge.entity.TemplateInfo;
import com.carlos.tool.projectge.enums.DirectEnum;
import com.carlos.tool.projectge.utils.NameUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 代码生成类
 * </p>
 *
 * @author Carlos
 * @date 2019/10/19 23:45 ---------     -------------   --------------------------------------
 */
@Slf4j
public class Generator {


    /**
     * 项目相关信息
     */
    private final ProjectInfo projectInfo;

    private final Map<String, Object> params = new HashMap<>();


    public Generator(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
        String artifactId = projectInfo.getArtifactId();
        String name = StrUtil.replace(artifactId, StrUtil.DASHED, StrUtil.UNDERLINE);
        String camelName = NameUtil.getCamelName(name, false);
        projectInfo.setCamelName(camelName);
        projectInfo.setUnderlineName(name);
        params.put(Constant.FTL_PARAM_KEY_PROJECT, projectInfo);
    }

    public void createObject() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("0.生成中.................................................................................");
        }
        try {
            SelectTemplate selectTemplate = projectInfo.getSelectTemplate();

            File templatePath = new File(selectTemplate.getPath() + File.separator + Constant.TEMPLATE_MAIN);
            File projectPath = new File(projectInfo.getPath());
            String projectPathAbsolutePath = projectPath.getAbsolutePath();
            String templatePathAbsolutePath = templatePath.getAbsolutePath();

            List<String> groupItems = projectInfo.getGroupItems();
            String groupDir = StrUtil.join(File.separator, groupItems);

            DirectEnum[] values = DirectEnum.values();

            // 遍历目录及目录名
            generateDir(templatePath, templatePathAbsolutePath, groupDir, projectPathAbsolutePath, values);
        } catch (Exception e) {
            log.error("生成失败！", e);
            throw new Exception(e);
        }

    }

    /**
     * createFile
     *
     * @param templateDir      模板中的文件目录
     * @param templateRootPath 模板根路径
     * @param groupDir         groupId所组成的目录
     * @param targetRootPath   目标位置根目录
     * @param values           参数4
     * @author Carlos
     * @date 2023/7/2 21:07
     */
    private void generateDir(File templateDir, String templateRootPath, String groupDir, String targetRootPath, DirectEnum[] values) {

        File[] files = templateDir.listFiles();
        if (files == null) {
            if (log.isDebugEnabled()) {
                log.debug("目录为空:" + templateDir.getPath());
            }
            return;
        }

        for (File file : files) {
            // 在对应的目标文件夹下创建目录
            String absolutePath = file.getAbsolutePath();
            String path = absolutePath.replace(templateRootPath, targetRootPath);
            for (DirectEnum value : values) {
                switch (value) {
                    case ArtifactId:
                        path = path.replace(value.getValue(), projectInfo.getArtifactId());
                        break;
                    case GroupId:
                        path = path.replace(value.getValue(), groupDir);
                        break;
                    case CAMEL_NAME:
                        path = path.replace(value.getValue(), projectInfo.getCamelName());
                        break;
                    case UNDERLINE_NAME:
                        path = path.replace(value.getValue(), projectInfo.getUnderlineName());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + value);
                }
            }


            if (FileUtil.isDirectory(file)) {
                FileUtil.mkdir(path);
                if (file.isDirectory()) {
                    if (log.isDebugEnabled()) {
                        log.debug("进入目录:" + file.getPath());
                    }
                    // 递归目录
                    generateDir(file, templateRootPath, groupDir, targetRootPath, values);
                }
                log.info("创建目录：" + path);
                continue;
            }
            if (FileUtil.isFile(file)) {
                log.info("开始处理文件:" + file.getPath());
                TemplateInfo templateInfo = handleFile(file);
                if (templateInfo == null) {
                    FileUtil.copyFile(file, new File(path));
                    log.info("复制文件：" + file.getPath() + "  ->  " + path);
                } else {
                    path = path.replace(".ftl", "");
                    try {
                        templateInfo.setTargetPath(FileUtil.getParent(path, 1));
                        templateInfo.setTargetName(templateInfo.getPreName());
                        TemplateConfig templateConfig = new TemplateConfig(templateInfo.getPath(), ResourceMode.FILE);
                        TemplateEngine engine = TemplateUtil.createEngine(templateConfig);
                        Template template = engine.getTemplate(templateInfo.getName());
                        template.render(params, new File(path));
                    } catch (Exception e) {
                        log.error("文件生成失败: 路径:" + templateInfo.getFile(), e);
                    }
                }
            }
        }
    }

    private TemplateInfo handleFile(File file) {
        // 处理非模板文件 非模板文件不用处理
        String extName = FileUtil.extName(file);
        if (!extName.equals(Constant.TEMPLATE_FILE_EXT)) {
            log.warn("非模板文件, 忽略处理:" + file.getPath());
            return null;
        }

        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setFile(file);
        templateInfo.setName(FileUtil.getName(file));
        templateInfo.setPath(file.getParent());
        // 文件处理前的名字
        String preName = FileUtil.mainName(file);
        templateInfo.setPreName(preName);
        return templateInfo;
    }

}
