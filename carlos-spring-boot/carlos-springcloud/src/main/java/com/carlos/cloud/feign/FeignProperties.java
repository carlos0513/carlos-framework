package com.carlos.cloud.feign;

import feign.Logger;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * feign配置
 *
 * @author yunjin
 * @date 2019-09-28
 **/
@Data
@ConfigurationProperties(prefix = "carlos.feign")
public class FeignProperties {


    /**
     * 日志配置
     */
    private final LogProperties log = new LogProperties();

    @Data
    public static class LogProperties {


        /**
         * 是否启用 默认启用
         */
        private boolean enable = true;

        /**
         * 日志级别 Feign支持4中级别： NONE：不记录任何日志，默认值 BASIC：仅记录请求的方法，URL以及响应状态码和执行时间 HEADERS：在BASIC基础上，额外记录了请求和响应的头信息 FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据
         */
        private Logger.Level level = Logger.Level.FULL;


    }

}
