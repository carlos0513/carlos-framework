package com.carlos.fx.codege.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.fx.codege.config.CodeGenerateInfo;
import com.carlos.fx.codege.config.CodegeConstant;
import com.carlos.fx.codege.config.DatabaseInfo;
import com.carlos.fx.codege.entity.TableBean;
import com.carlos.fx.codege.entity.TemplateBaseInfo;
import com.carlos.fx.codege.entity.TemplateInfo;
import com.carlos.fx.codege.enums.CodeDirectEnum;
import com.carlos.fx.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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
@Service
@Scope("prototype")
public class Generator {


    /**
     * 项目相关信息
     */
    private CodeGenerateInfo codeGenerateInfo;

    private DatabaseInfo databaseInfo;

    private final Map<String, Object> params = new HashMap<>();

    /**
     * 数据库信息
     */
    private List<TableBean> tables;



    /**
     * 初始化生成器配置
     *
     * @param databaseInfo 数据库信息
     * @param codeGenerateInfo 项目信息
     */
    public void init(DatabaseInfo databaseInfo, CodeGenerateInfo codeGenerateInfo) {
        this.databaseInfo = databaseInfo;
        this.codeGenerateInfo = codeGenerateInfo;
        params.put(CodegeConstant.FTL_PARAM_KEY_PROJECT, codeGenerateInfo);
        params.put(CodegeConstant.FTL_PARAM_KEY_DATABASDE, databaseInfo);
    }

    public void createObject() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("0.代码生成中.................................................................................");
        }
        try {
            TemplateBaseInfo templateBaseInfo = codeGenerateInfo.getTemplateBaseInfo();
            File templatePath = new File(templateBaseInfo.getPath() + File.separator + CodegeConstant.TEMPLATE_MAIN);
            File projectPath = new File(codeGenerateInfo.getOutputPath());
            // 复制模板文件到项目目录
            if (log.isDebugEnabled()) {
                log.debug("1.复制项目模板到指定的项目目录中");
            }
            FileUtil.copy(templatePath, projectPath, true);
            // 修改项目名称 工程根目录
            if (log.isDebugEnabled()) {
                log.debug("2.更改模板的目录名为项目名");
            }
            File projectRoot = FileUtil.rename(new File(codeGenerateInfo.getOutputPath() + File.separator + CodegeConstant.TEMPLATE_MAIN),
                    codeGenerateInfo.getProjectName(), true);
            // 创建基础包 并且把模板文件放入包中
            if (log.isDebugEnabled()) {
                log.debug("3.将模板文件移动至指定的包名路径：{}" + StrUtil.DOT + "{}", codeGenerateInfo.getPackageName(), codeGenerateInfo.getPackageName());
            }

            moveTemplate2PackagePath(projectRoot);

            // 获取需要生成代码的表的详情
            // 使用注入的DatabaseService，传入databaseInfo和templateConfig
            DatabaseService tempDatabaseService = new DatabaseService(databaseInfo, codeGenerateInfo.getTemplateBaseInfo());
            tables = tempDatabaseService.getTablesDetailInfo();

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
     * 将模板中源码 Java文件夹下的所有文件放入到指定的目录下，比如 java/com/carlos/test 目录下
     *
     * @param projectRootPath 项目的根目录
     * @author Carlos
     * @date 2020/9/9 17:31
     */
    public void moveTemplate2PackagePath(File projectRootPath) {
        // TODO: Carlos 2020/9/9 如果是多模块模板  这个目录下会有多个src/main/java目录
        File source = new File(projectRootPath.getPath() + CodegeConstant.SRC_MAIN_JAVA);
        File target = new File(projectRootPath.getPath() + CodegeConstant.SRC_MAIN_TEMP + codeGenerateInfo.getPackageName().replace(StrUtil.DOT, File.separator));
        try {
            if (log.isDebugEnabled()) {
                log.debug("3.1.将模板文件移动到临时目录：{}", target.getAbsolutePath());
            }
            FileUtils.moveDirectory(source, target);
        } catch (IOException e) {
            log.error("文件移动失败！ {}->{}", source.getPath(), target.getPath());
        }
        // 修改临时目录为正式的项目目录
        if (log.isDebugEnabled()) {
            log.debug("3.2.修改临时目录为正式的Java文件目录");
        }
        FileUtil.rename(new File(projectRootPath.getPath() + CodegeConstant.SRC_MAIN_TEMP), "java", true);
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
                log.debug("目录为空:{}", projectRoot.getPath());
            }
            return;
        }

        TemplateInfo templateInfo;
        for (File file : files) {
            if (file.isDirectory()) {
                if (log.isDebugEnabled()) {
                    log.debug("进入目录:{}", file.getPath());
                }
                // 递归目录
                createFile(file);
            }
            // 将模板文件处理成对应的文件
            if (log.isDebugEnabled()) {
                log.debug("开始处理模板文件：{}", file.getPath());
            }
            templateInfo = TemplateUtil.dealTemplateFile(file);
            if (templateInfo == null) {
                continue;
            }
            if (templateInfo.isLoop()) {
                // 如果文件需要循环处理
                for (TableBean table : tables) {
                    params.put(CodegeConstant.FTL_PARAM_KEY_TABLE, table);
                    templateInfo.setTargetName(StrUtil.replace(templateInfo.getPreName(),
                            CodeDirectEnum.BASE.getValue(), table.getClassPrefix()));
                    TemplateUtil.createClassFile(params, templateInfo);

                }
            } else {
                templateInfo.setTargetName(templateInfo.getPreName());
                TemplateUtil.createClassFile(params, templateInfo);
            }
            if (templateInfo.getFile().delete()) {
                log.info("删除模板文件:{}", templateInfo.getFile().getPath());
            }
        }
    }

}
