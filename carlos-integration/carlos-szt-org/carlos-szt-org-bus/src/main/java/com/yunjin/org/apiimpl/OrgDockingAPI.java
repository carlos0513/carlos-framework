package com.yunjin.org.apiimpl;

import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiOrgDocking;
import com.yunjin.org.convert.OrgDockingMappingConvert;
import com.yunjin.org.pojo.ao.OrgDockingMappingAO;
import com.yunjin.org.pojo.dto.OrgDockingMappingDTO;
import com.yunjin.org.pojo.enums.OrgDockingTypeEnum;
import com.yunjin.org.service.OrgDockingMappingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 组织信息对接
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/docking")
@Tag(name = "组织信息对接", hidden = true)
public class OrgDockingAPI implements ApiOrgDocking {

    private final OrgDockingMappingService dockingMappingService;

    @GetMapping("/getDocking")
    @Operation(summary = "获取对接配置")
    public Result<OrgDockingMappingAO> getDocking(@RequestParam("systemId") String systemId, @RequestParam("targetCode") String targetCode, @RequestParam("type") OrgDockingTypeEnum type) {
        OrgDockingMappingDTO dockingMapping = dockingMappingService.getDockingMapping(type, targetCode, systemId);
        return Result.ok(OrgDockingMappingConvert.INSTANCE.toAO(dockingMapping));
    }


}
