package com.carlos.boot.resource;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 接口描述对象
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
public class ResourceController {

    /**
     * 类名
     */
    private String className;
    /**
     * 请求路径
     */
    private String[] path;

    /**
     * 资源名称
     */
    private String featureName;
}
