package com.carlos.fx.codege.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.fx.codege.config.CodegeConstant;
import com.carlos.fx.codege.config.ProjectInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 项目相关功能
 * </p>
 *
 * @author Carlos
 * @date 2020/9/9 16:56
 */
@Slf4j
public class ProjectService {

    /**
     * 项目相关信息
     */
    private final ProjectInfo projectInfo;

    public ProjectService(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
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
        File target = new File(projectRootPath.getPath() + CodegeConstant.SRC_MAIN_TEMP + projectInfo.getGroupId().replace(StrUtil.DOT, File.separator));
        try {
            if (log.isDebugEnabled()) {
                log.debug("3.1.将模板文件移动到临时目录：" + target.getAbsolutePath());
            }
            FileUtils.moveDirectory(source, target);
        } catch (IOException e) {
            log.error("文件移动失败！ " + source.getPath() + "->" + target.getPath());
            e.printStackTrace();
        }
        // 修改临时目录为正式的项目目录
        if (log.isDebugEnabled()) {
            log.debug("3.2.修改临时目录为正式的Java文件目录");
        }
        FileUtil.rename(new File(projectRootPath.getPath() + CodegeConstant.SRC_MAIN_TEMP), "java", true);
    }

}
