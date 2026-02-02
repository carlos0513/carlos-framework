package com.carlos.fx.projectge.config;

import java.io.File;

/**
 * <p>
 * 代码生成器常量
 * </p>
 *
 * @author Carlos
 * @date 2021/11/22 16:31
 */
public interface Constant {

    /**
     * 删除字段名称
     */
    String FTL_PARAM_KEY_PROJECT = "maven";


    /**
     * 模板跟目录
     */
    String TEMPLATE_ROOT_PATH = "projectge/project-templates";

    /**
     * 模板描述文件文件名
     */
    String TEMPLATE_DESCRIBE_FILE_NAME = "template.xml";

    /**
     * java文件后缀
     */
    String JAVA_FILE_EXT = ".java";

    /**
     * 模板文件后缀
     */
    String TEMPLATE_FILE_EXT = "ftl";

    /**
     * 某个模板文件的根目录
     */
    String TEMPLATE_MAIN = "template";

    /**
     * 源码相对工程根目录的路径
     */
    String SRC_MAIN_JAVA = File.separator + "src" + File.separator + "main" + File.separator +
            "java" + File.separator;

    /**
     * 模板零时存储文件夹
     */
    String SRC_MAIN_TEMP = File.separator + "src" + File.separator + "main" + File.separator +
            "temp" + File.separator;
}
