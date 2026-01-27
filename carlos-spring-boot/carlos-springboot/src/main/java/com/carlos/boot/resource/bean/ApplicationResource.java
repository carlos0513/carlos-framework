package com.carlos.boot.resource.bean;


import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统资源
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
public class ApplicationResource {

    /**
     * 系统名称
     */
    private String appName;

    /**
     * 系统资源分类
     */
    private List<ResourceCategory> categories;
}
