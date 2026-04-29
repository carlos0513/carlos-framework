package com.carlos.redis.util;

import cn.hutool.crypto.digest.DigestUtil;
import com.carlos.redis.config.CacheProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 缓存 Key 生成器
 * <p>
 * 提供安全的 Key 生成策略，防止 Key 过长或包含非法字符
 * </p>
 *
 * @author carlos
 * @date 2026-03-14
 */
@Slf4j
public class CacheKeyGenerator {

    /**
     * 默认最大 Key 长度
     */
    private static final int DEFAULT_MAX_LENGTH = 256;

    /**
     * 生成缓存 Key
     *
     * @param target     目标对象
     * @param methodName 方法名
     * @param params     参数数组
     * @return 缓存 Key
     */
    public static String generate(Object target, String methodName, Object[] params) {
        return generate(target, methodName, params, DEFAULT_MAX_LENGTH, "truncate");
    }

    /**
     * 生成缓存 Key
     *
     * @param target            目标对象
     * @param methodName        方法名
     * @param params            参数数组
     * @param maxLength         最大长度
     * @param overflowStrategy  溢出策略：truncate、md5、sha1
     * @return 缓存 Key
     */
    public static String generate(Object target, String methodName, Object[] params,
                                  int maxLength, String overflowStrategy) {
        StringBuilder redisKey = new StringBuilder();
        redisKey.append(target.getClass().getName()).append(":");
        redisKey.append(methodName);

        if (params.length > 0) {
            redisKey.append(":");
            // 使用简化格式，避免 deepToString 产生过长字符串
            String paramStr = simplifyParams(params);
            redisKey.append(paramStr);
        }

        String key = redisKey.toString();

        // 处理超长 Key
        if (key.length() > maxLength) {
            key = handleOverflow(key, maxLength, overflowStrategy);
        }

        return key;
    }

    /**
     * 简化参数表示
     *
     * @param params 参数数组
     * @return 简化后的字符串
     */
    private static String simplifyParams(Object[] params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object param = params[i];
            if (param == null) {
                sb.append("null");
            } else if (param instanceof String) {
                // 字符串截断
                String str = (String) param;
                if (str.length() > 50) {
                    sb.append(str, 0, 50).append("...");
                } else {
                    sb.append(str);
                }
            } else if (param instanceof Number || param instanceof Boolean) {
                sb.append(param);
            } else if (param.getClass().isArray()) {
                // 数组类型只显示长度
                if (param instanceof Object[]) {
                    sb.append("Object[").append(((Object[]) param).length).append("]");
                } else if (param instanceof int[]) {
                    sb.append("int[").append(((int[]) param).length).append("]");
                } else if (param instanceof long[]) {
                    sb.append("long[").append(((long[]) param).length).append("]");
                } else {
                    sb.append(param.getClass().getSimpleName());
                }
            } else {
                // 其他类型使用类名 + hashCode
                sb.append(param.getClass().getSimpleName()).append("@").append(param.hashCode());
            }
        }
        return sb.toString();
    }

    /**
     * 处理 Key 过长的情况
     *
     * @param key              原始 Key
     * @param maxLength        最大长度
     * @param overflowStrategy 溢出策略
     * @return 处理后的 Key
     */
    private static String handleOverflow(String key, int maxLength, String overflowStrategy) {
        log.warn("Cache key length {} exceeds max {}, applying strategy: {}",
            key.length(), maxLength, overflowStrategy);

        return switch (overflowStrategy.toLowerCase()) {
            case "md5" ->
                // 使用 MD5 摘要（32 位）
                "MD5:" + DigestUtil.md5Hex(key);
            case "sha1" ->
                // 使用 SHA1 摘要（40 位）
                "SHA1:" + DigestUtil.sha1Hex(key);
            default ->
                // 截断并添加后缀
                key.substring(0, maxLength - 3) + "...";
        };
    }

    /**
     * 根据 CacheProperties 生成 Key
     *
     * @param target   目标对象
     * @param method   方法名
     * @param params   参数
     * @param properties 配置属性
     * @return 缓存 Key
     */
    public static String generate(Object target, String method, Object[] params, CacheProperties properties) {
        return generate(target, method, params, properties.getKeyMaxLength(), properties.getKeyOverflowStrategy());
    }
}
