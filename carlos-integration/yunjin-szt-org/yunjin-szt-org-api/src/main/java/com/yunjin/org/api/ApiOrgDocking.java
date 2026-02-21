package com.yunjin.org.api;

import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.fallback.FeignOrgDockingFallbackFactory;
import com.yunjin.org.pojo.ao.OrgDockingMappingAO;
import com.yunjin.org.pojo.enums.OrgDockingTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 系统用户 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-20 14:07:16
 */
@FeignClient(value = ServiceNameConstant.USER, path = "/api/org/docking", contextId = "docking", fallbackFactory = FeignOrgDockingFallbackFactory.class)
public interface ApiOrgDocking {

    /**
     * 获取对接信息
     *
     * @param systemId 系统id
     * @param type     对接数据类型
     * @return com.yunjin.core.response.Result<com.yunjin.org.pojo.ao.OrgDockingMappingAO>
     * @author Carlos
     * @date 2025-04-16 09:56
     */
    @GetMapping("/getDocking")
    @Operation(summary = "获取对接配置")
    Result<OrgDockingMappingAO> getDocking(@RequestParam("systemId") String systemId, @RequestParam("targetCode") String targetCode, @RequestParam("type") OrgDockingTypeEnum type);
}
