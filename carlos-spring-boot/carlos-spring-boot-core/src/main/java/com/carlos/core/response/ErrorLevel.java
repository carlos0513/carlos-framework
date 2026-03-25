package com.carlos.core.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 错误级别枚举
 * <p>
 * 错误码的第一位数字表示错误级别：
 * <ul>
 *   <li>0 - 成功</li>
 *   <li>1 - 客户端错误（请求参数问题）</li>
 *   <li>2 - 业务错误（业务规则限制）</li>
 *   <li>3 - 第三方服务错误</li>
 *   <li>5 - 系统错误（内部故障）</li>
 * </ul>
 *
 * @author carlos
 * @since 3.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ErrorLevel {

    /**
     * 成功
     */
    SUCCESS('0', "成功"),

    /**
     * 客户端错误 - 用户请求参数问题
     */
    CLIENT_ERROR('1', "客户端错误"),

    /**
     * 业务错误 - 业务规则限制
     */
    BUSINESS_ERROR('2', "业务错误"),

    /**
     * 第三方服务错误 - 外部服务调用失败
     */
    THIRD_PARTY_ERROR('3', "第三方服务错误"),

    /**
     * 系统错误 - 系统内部故障
     */
    SYSTEM_ERROR('5', "系统错误");

    private final char code;
    private final String description;

    /**
     * 根据代码字符获取错误级别
     *
     * @param code 级别代码字符
     * @return ErrorLevel
     */
    public static ErrorLevel fromCode(char code) {
        for (ErrorLevel level : values()) {
            if (level.code == code) {
                return level;
            }
        }
        return SYSTEM_ERROR;
    }

    /**
     * 根据代码字符串获取错误级别
     *
     * @param codeStr 级别代码字符串
     * @return ErrorLevel
     */
    public static ErrorLevel fromCode(String codeStr) {
        if (codeStr == null || codeStr.isEmpty()) {
            return SYSTEM_ERROR;
        }
        return fromCode(codeStr.charAt(0));
    }
}
