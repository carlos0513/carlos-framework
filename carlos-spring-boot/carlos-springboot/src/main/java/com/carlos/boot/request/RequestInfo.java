package com.carlos.boot.request;


import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.carlos.core.auth.UserContext;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * Http请求信息
 * </p>
 *
 * @author yunjin
 * @date 2020/4/24 13:26
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RequestInfo implements Serializable {

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * content-path
     */
    private String contextPath;

    /**
     * 请求实际路径 去除content-path /foobar/add
     */
    private String realPath;

    /**
     * 请求IP地址
     */
    private String ip;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 是否是feign请求
     */
    private boolean rpc;

    /**
     * 请求方式，GET/POST
     */
    private String method;

    /**
     * 请求内容类型
     */
    private String contentType;

    /**
     * 请求参数
     */
    private Object param;

    /**
     * 用户信息
     */
    private UserContext userContext;


    /**
     * 请求时间
     */
    @JsonIgnore
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_MS_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MS_PATTERN)
    private LocalDateTime time;

    /**
     * 请求token
     */
    private String token;

    /**
     * 请求头信息
     */
    @JsonIgnore
    private Map<String, String> header;

    /**
     * 应用信息
     */
    private AppInfo appInfo;

}
