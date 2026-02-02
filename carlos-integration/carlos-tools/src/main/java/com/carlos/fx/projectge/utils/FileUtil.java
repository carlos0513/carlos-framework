package com.carlos.fx.projectge.utils;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.HashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author Carlos
 * @date 2020/1/3 16:15
 * @ModificationHistory Who  When  What ---------     -------------   --------------------------------------
 */
public class FileUtil {

    /**
     * 遍历目录
     *
     * @author Carlos
     * @date 2020/1/3 16:17
     */
    public static HashMap<String, String> listDirectory(File dir, File file2) {
        HashMap<String, String> map = new HashMap<>(6);
        if (!dir.exists()) {
            throw new IllegalArgumentException("目录：" + dir + "不存在.");
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + "不是目录");
        }
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    map.putAll(listDirectory(file, file2));
                } else {
                    map.put(file.getName(), getRelativeDir(file.getParent(), file2.getPath()));
                }
            }
        }
        return map;
    }

    /**
     * 获取两个路径的相对位置
     *
     * @param path1 长路径
     * @param path2 段路径
     * @author Carlos
     * @date 2020/1/3 17:31
     */
    public static String getRelativeDir(String path1, String path2) {
        path1 = StrUtil.replace(path1, "\\", "&").replace('/', '&');
        path2 = StrUtil.replace(path2, "\\", "&").replace('/', '&');
        String newPath = path1.replaceAll(path2, "");
        return StrUtil.replace(newPath, "&", File.separator);
    }
}
