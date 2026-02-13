package com.carlos.boot.resource;

import com.carlos.boot.resource.bean.ApplicationResource;

/**
 * <p>
 * 资源存储接口
 * </p>
 *
 * @author carlos
 * @date 2022/1/11 18:59
 */
public interface ResourceStore {

    /**
     * 保存资源
     *
     * @param resource 系统资源
     * @author carlos
     * @date 2022/1/12 14:20
     */
    void save(ApplicationResource resource);


}
