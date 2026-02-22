package com.carlos.org.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 蓉政通属性配置
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:34
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "carlos.system")
public class MenuApiMappingProperties {


    private Map<String, List<MenuApiMapping>> menuApiMappings;


    @Data
    public static class MenuApiMapping {
        /** 接口id */
        private Serializable apiId;
        /** 接口路径 */
        private String apiUrl;
    }


}
