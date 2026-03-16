package com.carlos.encrypt.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.carlos.encrypt.exception.EncryptException;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * Base64 编码工具类
 * </p>
 *
 * <p>
 * Base64 是一种基于 64 个可打印字符来表示二进制数据的编码方法，常用于在文本协议中传输二进制数据。
 * 注意：Base64 不是加密算法，只是编码方式，不提供安全性。
 * </p>
 *
 * @author Carlos
 * @date 2025/3/16
 */
public class Base64Util {

    private Base64Util() {
        // 工具类禁止实例化
    }

    // ==================== 字符串编码 ====================

    /**
     * Base64 编码
     *
     * @param data 待编码字符串
     * @return Base64 编码字符串
     */
    public static String encode(String data) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return Base64.encode(data);
    }

    /**
     * Base64 编码（URL 安全）
     *
     * @param data 待编码字符串
     * @return URL 安全的 Base64 编码字符串
     */
    public static String encodeUrlSafe(String data) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return Base64.encodeUrlSafe(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64 编码（无换行）
     *
     * @param data 待编码字符串
     * @return Base64 编码字符串（无换行）
     */
    public static String encodeWithoutPadding(String data) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return Base64.encodeWithoutPadding(data.getBytes(StandardCharsets.UTF_8));
    }

    // ==================== 字节数组编码 ====================

    /**
     * Base64 编码字节数组
     *
     * @param data 待编码字节数组
     * @return Base64 编码字符串
     */
    public static String encode(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return Base64.encode(data);
    }

    /**
     * Base64 编码字节数组（URL 安全）
     *
     * @param data 待编码字节数组
     * @return URL 安全的 Base64 编码字符串
     */
    public static String encodeUrlSafe(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return Base64.encodeUrlSafe(data);
    }

    // ==================== 字符串解码 ====================

    /**
     * Base64 解码
     *
     * @param base64Data Base64 编码字符串
     * @return 解码后的字符串
     * @throws EncryptException 解码失败时抛出
     */
    public static String decode(String base64Data) {
        if (StrUtil.isBlank(base64Data)) {
            return "";
        }
        try {
            return Base64.decodeStr(base64Data);
        } catch (Exception e) {
            throw new EncryptException("Base64 解码失败", e);
        }
    }

    /**
     * Base64 解码（URL 安全）
     *
     * @param base64Data URL 安全的 Base64 编码字符串
     * @return 解码后的字符串
     * @throws EncryptException 解码失败时抛出
     */
    public static String decodeUrlSafe(String base64Data) {
        if (StrUtil.isBlank(base64Data)) {
            return "";
        }
        try {
            return Base64.decodeStr(base64Data);
        } catch (Exception e) {
            throw new EncryptException("Base64 解码失败", e);
        }
    }

    // ==================== 字节数组解码 ====================

    /**
     * Base64 解码为字节数组
     *
     * @param base64Data Base64 编码字符串
     * @return 解码后的字节数组
     * @throws EncryptException 解码失败时抛出
     */
    public static byte[] decodeToBytes(String base64Data) {
        if (StrUtil.isBlank(base64Data)) {
            return new byte[0];
        }
        try {
            return Base64.decode(base64Data);
        } catch (Exception e) {
            throw new EncryptException("Base64 解码失败", e);
        }
    }

    // ==================== 文件编码 ====================

    /**
     * 将文件编码为 Base64
     *
     * @param file 文件
     * @return Base64 编码字符串
     * @throws EncryptException 编码失败时抛出
     */
    public static String encodeFile(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        try {
            return Base64.encode(file);
        } catch (Exception e) {
            throw new EncryptException("文件 Base64 编码失败", e);
        }
    }

    /**
     * 将输入流编码为 Base64
     *
     * @param inputStream 输入流
     * @return Base64 编码字符串
     * @throws EncryptException 编码失败时抛出
     */
    public static String encodeStream(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        try {
            return Base64.encode(inputStream);
        } catch (Exception e) {
            throw new EncryptException("流 Base64 编码失败", e);
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 判断字符串是否为 Base64 编码
     *
     * @param data 待判断字符串
     * @return 是否为 Base64 编码
     */
    public static boolean isBase64(String data) {
        if (StrUtil.isBlank(data)) {
            return false;
        }
        try {
            Base64.decode(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取 Base64 编码后的数据长度
     *
     * @param originalLength 原始数据长度
     * @return Base64 编码后的长度
     */
    public static int getEncodedLength(int originalLength) {
        return ((originalLength + 2) / 3) * 4;
    }
}
