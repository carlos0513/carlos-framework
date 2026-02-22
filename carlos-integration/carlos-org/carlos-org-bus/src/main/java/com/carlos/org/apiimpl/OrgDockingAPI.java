package com.carlos.org.apiimpl;

import com.carlos.core.response.Result;
import com.carlos.org.api.ApiOrgDocking;
import com.carlos.org.convert.OrgDockingMappingConvert;
import com.carlos.org.pojo.ao.OrgDockingMappingAO;
import com.carlos.org.pojo.dto.OrgDockingMappingDTO;
import com.carlos.org.pojo.enums.OrgDockingTypeEnum;
import com.carlos.org.service.OrgDockingMappingService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/docking")
@Tag(name = "组织信息对接")
@Hidden
public class OrgDockingAPI implements ApiOrgDocking {

    private final OrgDockingMappingService dockingMappingService;

    @Override
    @GetMapping("/getDocking")
    @Operation(summary = "获取对接配置")
    public Result<OrgDockingMappingAO> getDocking(@RequestParam("systemId") String systemId, @RequestParam("targetCode") String targetCode, @RequestParam("type") OrgDockingTypeEnum type) {
        OrgDockingMappingDTO dockingMapping = dockingMappingService.getDockingMapping(type, targetCode, systemId);
        return Result.ok(OrgDockingMappingConvert.INSTANCE.toAO(dockingMapping));
    }


}
