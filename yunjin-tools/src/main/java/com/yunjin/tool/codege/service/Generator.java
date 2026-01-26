package com.yunjin.tool.codege.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yunjin.tool.codege.config.Constant;
import com.yunjin.tool.codege.config.DatabaseInfo;
import com.yunjin.tool.codege.config.ProjectInfo;
import com.yunjin.tool.codege.entity.TableBean;
import com.yunjin.tool.codege.entity.TemplateConfig;
import com.yunjin.tool.codege.entity.TemplateInfo;
import com.yunjin.tool.codege.enums.DirectEnum;
import com.yunjin.tool.codege.utils.TemplateUtil;
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


    private final DatabaseService databaseService;

    private final ProjectService projectService;

    /**
     * 项目相关信息
     */
    private final ProjectInfo projectInfo;

    private final Map<String, Object> params = new HashMap<>();


    /**
     * 数据库信息
     */
    private List<TableBean> tables;

    public Generator(DatabaseInfo databaseInfo, ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
        params.put(Constant.FTL_PARAM_KEY_PROJECT, projectInfo);
        params.put(Constant.FTL_PARAM_KEY_DATABASDE, databaseInfo);
        projectService = new ProjectService(projectInfo);


        databaseService = new DatabaseService(databaseInfo, projectInfo.getTemplateConfig());
    }

    public void createObject() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("0.代码生成中.................................................................................");
        }
        try {
            TemplateConfig templateConfig = projectInfo.getTemplateConfig();
            File templatePath = new File(templateConfig.getPath() + File.separator + Constant.TEMPLATE_MAIN);
            File projectPath = new File(projectInfo.getPath());
            // 复制模板文件到项目目录
            if (log.isDebugEnabled()) {
                log.debug("1.复制项目模板到指定的项目目录中");
            }
            FileUtil.copy(templatePath, projectPath, true);
            // 修改项目名称 工程根目录
            if (log.isDebugEnabled()) {
                log.debug("2.更改模板的目录名为项目名");
            }
            File projectRoot = FileUtil.rename(new File(projectInfo.getPath() + File.separator + Constant.TEMPLATE_MAIN),
                    projectInfo.getProjectName(), true);
            // 创建基础包 并且把模板文件放入包中
            if (log.isDebugEnabled()) {
                log.debug("3.将模板文件移动至指定的包名路径：" + projectInfo.getGroupId() + StrUtil.DOT + projectInfo.getArtifactId());
            }
            projectService.moveTemplate2PackagePath(projectRoot);

            // 获取需要生成代码的表的详情
            tables = databaseService.getTablesDetailInfo();

            if (log.isDebugEnabled()) {
                log.debug("4.根据模板文件生成对应的项目文件");
            }
            // 根据模板文件生成对应的Java文件
            createFile(projectRoot);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }

    /**
     * 递归遍历父目录下的模板文件
     *
     * @param projectRoot 项目的根目录
     * @author Carlos
     * @date 2020/9/9 23:00
     */
    private void createFile(File projectRoot) {
        File[] files = projectRoot.listFiles();
        if (files == null) {
            if (log.isDebugEnabled()) {
                log.debug("目录为空:" + projectRoot.getPath());
            }
            return;
        }

        TemplateInfo templateInfo;
        for (File file : files) {
            if (file.isDirectory()) {
                if (log.isDebugEnabled()) {
                    log.debug("进入目录:" + file.getPath());
                }
                // 递归目录
                createFile(file);
            }
            // 将模板文件处理成对应的文件
            if (log.isDebugEnabled()) {
                log.debug("开始处理模板文件：" + file.getPath());
            }
            templateInfo = TemplateUtil.dealTemplateFile(file);
            if (templateInfo == null) {
                continue;
            }
            if (templateInfo.isLoop()) {
                // 如果文件需要循环处理
                for (TableBean table : tables) {
                    params.put(Constant.FTL_PARAM_KEY_TABLE, table);
                    templateInfo.setTargetName(StrUtil.replace(templateInfo.getPreName(),
                            DirectEnum.BASE.getValue(), table.getClassPrefix()));
                    TemplateUtil.createClassFile(params, templateInfo);

                }
            } else {
                templateInfo.setTargetName(templateInfo.getPreName());
                TemplateUtil.createClassFile(params, templateInfo);
            }
            if (templateInfo.getFile().delete()) {
                log.info("删除模板文件:" + templateInfo.getFile().getPath());
            }
        }
    }

}
