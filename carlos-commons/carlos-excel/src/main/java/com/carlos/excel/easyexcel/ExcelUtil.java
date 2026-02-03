package com.carlos.excel.easyexcel;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelReader;
import cn.idev.excel.event.AnalysisEventListener;
import com.carlos.excel.exception.ExcelException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * excel相关工具
 *
 * @author Carlos
 * @date 2022/11/28 16:48
 */
public class ExcelUtil {

    /**
     * 表格下载 无具体对象
     *
     * @param response 请求响应对象
     * @param name     文件名称，不带扩展名
     * @param head     表头信息
     * @param data     数据内容
     * @author Carlos
     * @date 2022/11/28 16:24
     */
    public static void download(
            final HttpServletResponse response, final String name, final List<List<String>> head, final Collection<?> data) {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            final String fileName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader(
                    "Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream())
                    .autoCloseStream(Boolean.FALSE)
                    .head(head)
                    .sheet("sheet1")
                    .doWrite(data);
        } catch (final Exception e) {
            throw new ExcelException("数据导出失败", e);
        }
    }

    /**
     * 表格下载 有具体对象
     *
     * @param response 请求响应对象
     * @param name     文件名称，不带扩展名
     * @param head     表头信息
     * @param data     数据内容
     * @author Carlos
     * @date 2022/11/28 16:24
     */
    public static void download(
            final HttpServletResponse response, final String name, final Class head, final Collection<?> data) {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            final String fileName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader(
                    "Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), head)
                    .autoCloseStream(Boolean.TRUE)
                    .sheet("sheet1")
                    .doWrite(data);
        } catch (final Exception e) {
            throw new ExcelException("数据导出失败", e);
        }
    }

    public static void checkExcel(final String name) {
        final String extName = FileNameUtil.extName(name);
        if (!CharSequenceUtil.equalsAny(extName, "xls", "xlsx")) {
            throw new ExcelException("不支持的excel文件类型");
        }

    }


    /**
     * 返回 ExcelReader
     *
     * @param excel         需要解析的 Excel 文件
     * @param excelListener new ExcelListener()
     */
    private static ExcelReader getReader(MultipartFile excel, Class head, AnalysisEventListener excelListener) {
        String filename = excel.getOriginalFilename();

        if (filename == null || (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx"))) {
            return null;
        }
        InputStream inputStream;

        try {
            inputStream = new BufferedInputStream(excel.getInputStream());
            ExcelReader excelReader = EasyExcel.read(inputStream, head, excelListener).build();

            return excelReader;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断你一个类是否存在某个属性（字段）
     *
     * @param fieldName 字段
     * @param obj       类对象
     * @return true:存在，false:不存在, null:参数不合法
     */
    public static Boolean isExistField(String fieldName, Object obj) {
        if (obj == null || StringUtils.hasText(fieldName)) {
            return null;
        }
        // 获取这个类的所有属性
        Field[] fields = obj.getClass().getDeclaredFields();
        boolean flag = false;
        // 循环遍历所有的fields
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(fieldName)) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    private static ExcelReader getReaderNoHead(MultipartFile excel, AnalysisEventListener excelListener) {
        String filename = excel.getOriginalFilename();

        if (filename == null || (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx"))) {
            return null;
        }
        InputStream inputStream;

        try {
            inputStream = new BufferedInputStream(excel.getInputStream());
            ExcelReader excelReader = EasyExcel.read(inputStream, excelListener).build();

            return excelReader;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
