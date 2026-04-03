package com.carlos.boot.response;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *   springboot响应配置
 * </p>
 *
 * @author Carlos
 * @date 2026-03-27 22:51
 */
@Data
@ConfigurationProperties(prefix = "carlos.boot.response")
public class ResponseProperties {

    /**
     * 响应包装配置
     */
    private ResponseWrap wrap = new ResponseWrap();

    /**
     * Web 响应配置
     */
    @Data
    public static class ResponseWrap {
        /**
         * 是否启用响应包装
         */
        private boolean enable = true;

        /**
         * 需要排除包装的路径
         */
        private List<String> excludePaths = new ArrayList<>();
    }
}
