package com.carlos.boot.resource.bean;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 资源分类
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
public class ResourceCategory {

    /**
     * 资源类型
     */
    private String name;

    /**
     * 资源集合
     */
    private List<Resource> resources;


}
