package com.carlos.json.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * JSON 配置属性类
 * </p>
 *
 * @author carlos
 * @date 2025/01/15
 */
@Data
@ConfigurationProperties(prefix = "carlos.json")
public class JsonProperties {

    /**
     * 是否启用 JSON 自动配置
     */
    private boolean enabled = true;

    /**
     * JSON 引擎类型: jackson, fastjson, gson
     */
    private JsonEngineType engine = JsonEngineType.JACKSON;

    /**
     * 编码格式，默认 UTF-8
     */
    private String charset = StandardCharsets.UTF_8.name();

    /**
     * 日期格式
     */
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时区
     */
    private String timeZone = "GMT+8";

    /**
     * 是否格式化输出（美化 JSON）
     */
    private boolean prettyPrint = false;

    /**
     * 是否忽略 null 值字段
     */
    private boolean ignoreNull = true;

    /**
     * 是否忽略空字符串
     */
    private boolean ignoreEmptyString = false;

    /**
     * 序列化配置
     */
    private Serialization serialization = new Serialization();

    /**
     * 反序列化配置
     */
    private Deserialization deserialization = new Deserialization();

    /**
     * Web 响应配置
     */
    private WebResponse webResponse = new WebResponse();

    /**
     * 字段映射配置
     */
    private FieldMapping fieldMapping = new FieldMapping();

    /**
     * JSON 引擎类型枚举
     */
    public enum JsonEngineType {
        /**
         * Jackson 引擎（Spring Boot 默认）
         */
        JACKSON,
        /**
         * Fastjson2 引擎（阿里巴巴，性能优先）
         */
        FASTJSON2,
        /**
         * Gson 引擎（Google）
         */
        GSON
    }

    /**
     * 序列化配置
     */
    @Data
    public static class Serialization {
        /**
         * 是否输出缩进（美化）
         */
        private boolean indentOutput = false;

        /**
         * 遇到空 Bean 是否失败
         */
        private boolean failOnEmptyBeans = false;

        /**
         * 是否将 Long/BigInteger 转为 String（解决精度丢失）
         */
        private boolean longToString = true;

        /**
         * 需要排除的字段名列表
         */
        private List<String> excludeFields = new ArrayList<>();

        /**
         * 只包含的字段名列表（优先级高于 excludeFields）
         */
        private List<String> includeFields = new ArrayList<>();
    }

    /**
     * 反序列化配置
     */
    @Data
    public static class Deserialization {
        /**
         * 遇到未知属性是否失败
         */
        private boolean failOnUnknownProperties = false;

        /**
         * 是否允许单引号
         */
        private boolean allowSingleQuotes = true;

        /**
         * 是否允许注释
         */
        private boolean allowComments = false;

        /**
         * 是否允许未转义的控制字符
         */
        private boolean allowUnescapedControlChars = true;

        /**
         * 是否允许反斜杠转义任何字符
         */
        private boolean allowBackslashEscapingAnyChar = true;
    }

    /**
     * Web 响应配置
     */
    @Data
    public static class WebResponse {
        /**
         * 是否启用响应包装
         */
        private boolean wrapResponse = false;

        /**
         * 响应包装类全限定名
         */
        private String wrapperClass = "com.carlos.json.wrapper.ApiResponse";

        /**
         * 成功状态码字段名
         */
        private String successCodeField = "code";

        /**
         * 成功状态码值
         */
        private int successCode = 200;

        /**
         * 消息字段名
         */
        private String messageField = "message";

        /**
         * 数据字段名
         */
        private String dataField = "data";

        /**
         * 是否包装原始字符串响应
         */
        private boolean wrapRawString = false;

        /**
         * 需要排除包装的路径
         */
        private List<String> excludePaths = new ArrayList<>();
    }

    /**
     * 字段映射配置
     */
    @Data
    public static class FieldMapping {
        /**
         * 是否启用下划线转驼峰
         */
        private boolean underscoreToCamel = false;

        /**
         * 是否启用驼峰转下划线
         */
        private boolean camelToUnderscore = false;

        /**
         * 属性命名策略：SNAKE_CASE, LOWER_CAMEL_CASE, KEBAB_CASE, LOWER_CASE, UPPER_CAMEL_CASE
         */
        private String propertyNamingStrategy = "LOWER_CAMEL_CASE";
    }
}
