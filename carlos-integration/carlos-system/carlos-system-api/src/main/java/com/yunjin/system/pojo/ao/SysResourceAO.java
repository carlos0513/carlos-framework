package com.carlos.system.pojo.ao;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统资源 数据传输对象，service和manager向外传输对象
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
public class SysResourceAO {

    /**
     * 主键
     */
    private String id;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 接口路径前缀
     */
    private String pathPrefix;
    /**
     * 接口路径
     */
    private String path;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 图标
     */
    private String icon;
    /**
     * 资源类型，按钮
     */
    private String type;
    /**
     * 显示和隐藏，0：显示，1：隐藏
     */
    private Boolean hidden;
    /**
     * 资源描述
     */
    private String description;
}
