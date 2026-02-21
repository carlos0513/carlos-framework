package com.carlos.system.api;

import com.carlos.core.response.Result;
import com.carlos.system.ServiceNameConstant;
import com.carlos.system.fallback.FeignResourceFallbackFactory;
import com.carlos.system.pojo.ao.SysResourceAO;
import com.carlos.system.pojo.param.ApiResourceCategoryAddParam;
import com.carlos.system.pojo.param.ApiSysResourceAddParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 系统资源 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@FeignClient(value = ServiceNameConstant.SYSTEM, contextId = "resource", path = "/api/sys/resource", fallbackFactory = FeignResourceFallbackFactory.class)
public interface ApiResource {

    /**
     * 获取资源信息
     *
     * @param id 资源id
     * @return java.util.List<com.carlos.sys.pojo.dto.ResourceDTO>
     * @author yunjin
     * @date 2022/1/4 15:30
     */
    @GetMapping(value = "/")
    Result<SysResourceAO> getResourceById(@RequestParam("id") String id);

    /**
     * 新增资源
     *
     * @param dto 资源
     * @author yunjin
     * @date 2022/1/4 15:30
     */
    @PostMapping(value = "/")
    Result<Boolean> addResource(@RequestBody ApiSysResourceAddParam dto);

    /**
     * 新增资源类型
     *
     * @param dto 资源类型
     * @author yunjin
     * @date 2022/1/4 15:30
     */
    @PostMapping(value = "category")
    Result<Boolean> addResourceCategory(@RequestBody ApiResourceCategoryAddParam dto);

}
