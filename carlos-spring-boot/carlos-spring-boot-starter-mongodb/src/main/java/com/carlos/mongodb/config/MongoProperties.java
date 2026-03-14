package com.carlos.mongodb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * MongoDB 配置属性类
 * </p>
 *
 * @author carlos
 * @date 2024/01/15
 */
@Data
@ConfigurationProperties(prefix = "carlos.mongodb")
public class MongoProperties {

    /**
     * 是否启用 MongoDB 模块
     */
    private boolean enabled = true;

    /**
     * 是否启用字段自动填充
     */
    private boolean fieldFillEnabled = true;

    /**
     * 创建时间字段名
     */
    private String createTimeField = "createTime";

    /**
     * 更新时间字段名
     */
    private String updateTimeField = "updateTime";

    /**
     * 创建人字段名
     */
    private String createByField = "createBy";

    /**
     * 更新人字段名
     */
    private String updateByField = "updateBy";

    /**
     * 逻辑删除字段名
     */
    private String deletedField = "deleted";

    /**
     * 版本号字段名
     */
    private String versionField = "version";
}
