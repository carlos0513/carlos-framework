package com.carlos.docking.config;

import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ComponentException;
import feign.Logger;
import lombok.Data;

/**
 * feign配置
 *
 * @author Carlos
 * @date 2019-09-28
 **/
@Data
public class FeignBaseProperties {

    /**
     * 默认关闭
     */
    private boolean enabled = false;
    /**
     * 注册中心name
     */
    private String name;

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 相对路径
     */
    private String path;

    /**
     * id
     */
    private String contextId;

    public void check() {
        if (StrUtil.isBlank(name)) {
            throw new ComponentException("The property 'name' can't be null or ''");
        }
    }

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
