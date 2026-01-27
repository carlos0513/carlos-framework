package com.carlos.boot.resource.bean;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>
 * 接口描述对象
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
public class Resource {

    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源路径前缀
     */
    private String pathPrefix;
    /**
     * 接口路径
     */
    private String path;
    /**
     * 请求方式
     */
    private RequestMethod method;
    /**
     * 显示和隐藏，0：显示，1：隐藏
     */
    private Boolean hidden;
    /**
     * 资源描述
     */
    private String description;


}
