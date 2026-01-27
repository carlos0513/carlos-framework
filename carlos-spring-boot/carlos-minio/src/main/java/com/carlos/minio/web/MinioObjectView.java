package com.carlos.minio.web;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 * 将minio对象以流的方式写到前端
 * </p>
 *
 * @author yunjin
 * @date 2021/6/10 14:39
 */
public class MinioObjectView implements View {

    private final InputStream in;
    private final String attachmentName;

    public MinioObjectView(InputStream in, String attachmentName) {
        this.in = in;
        this.attachmentName = attachmentName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        // response.setHeader("Cache", "no-cache");
        // response.setHeader("Content-Type", "application/force-download");
        // response.setHeader("Content-Disposition", String.format("attachment; filename=%s", encodeFilename(attachmentName)));
        // response.setHeader("Content-Transfer-Encoding", "binary");
        // JakartaServletUtil.write(response, in);
        JakartaServletUtil.write(response, in, MediaType.APPLICATION_OCTET_STREAM_VALUE, attachmentName);
    }


}
