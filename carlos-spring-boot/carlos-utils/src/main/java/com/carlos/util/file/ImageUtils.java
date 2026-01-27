package com.carlos.util.file;

import com.carlos.util.exception.YJBaseException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * @author yunjin
 * @WebSite www.yunjin.cn
 * @Description: 图片工具
 */
public class ImageUtils {

    public ImageUtils() {
        throw new YJBaseException("This is a utility class and cannot be instantiated");
    }

    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    public static byte[] getImage(String imagePath) {
        try (InputStream is = getFile(imagePath)) {
            if (is != null) {
                return IOUtils.toByteArray(is);
            }
            return null;
        } catch (Exception e) {
            log.error("图片加载异常 {}", e.getMessage());
            return null;
        }
    }

    public static InputStream getFile(String imagePath) {
        try {
            byte[] result = readFile(imagePath);
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        } catch (Exception e) {
            log.error("获取图片异常", e);
        }
        return null;
    }


    /**
     * 读取文件为字节数据
     *
     * @return 字节数据
     */
    public static byte[] readFile(String url) {
        try {
            // 网络地址
            URL urlObj = new URL(url);
            URLConnection urlConnection = urlObj.openConnection();
            urlConnection.setConnectTimeout(30 * 1000);
            urlConnection.setReadTimeout(60 * 1000);
            urlConnection.setDoInput(true);

            try (InputStream in = urlConnection.getInputStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                return IOUtils.toByteArray(in);
            }
        } catch (Exception e) {
            log.error("访问文件异常，", e);
            return null;
        }
    }

}
