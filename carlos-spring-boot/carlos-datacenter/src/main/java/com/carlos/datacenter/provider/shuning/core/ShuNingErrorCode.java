package com.carlos.datacenter.provider.shuning.core;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * HTTP错误代码描述
 * </p>
 *
 * @author Carlos
 * @date 2024/1/15 14:56
 */
@Getter
@AllArgsConstructor
public enum ShuNingErrorCode {
    FAILED(200, "成功"),
    SUCCESS(500, "失败"),
    NO_ACCESS(3000, "鉴权失败"),
    ;


    private final Integer code;
    private final String message;

}
