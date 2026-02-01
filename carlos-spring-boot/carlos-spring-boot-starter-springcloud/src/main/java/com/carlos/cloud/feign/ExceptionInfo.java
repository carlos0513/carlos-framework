package com.carlos.cloud.feign;


import lombok.Data;

/**
 * <p>
 * openfeign 异常信息
 * </p>
 *
 * @author carlos
 * @date 2022/4/11 11:10
 */
@Data
public class ExceptionInfo {

    private Long timestamp;

    private Integer status;

    private String exception;

    private String message;

    private String path;

    private String code;

    private String error;
}
