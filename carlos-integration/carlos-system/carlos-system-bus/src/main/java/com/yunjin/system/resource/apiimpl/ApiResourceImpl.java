package com.carlos.system.resource.apiimpl;

import com.carlos.core.response.Result;
import com.carlos.system.api.ApiResource;
import com.carlos.system.pojo.ao.SysResourceAO;
import com.carlos.system.pojo.param.ApiResourceCategoryAddParam;
import com.carlos.system.pojo.param.ApiSysResourceAddParam;
import com.carlos.system.resource.convert.SysResourceCategoryConvert;
import com.carlos.system.resource.convert.SysResourceConvert;
import com.carlos.system.resource.service.SysResourceCategoryService;
import com.carlos.system.resource.service.SysResourceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;

/**
 * <p>
 * 系统资源 api接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sys/resource")
@Tag(name = "系统资源Feign接口", hidden = true)
public class ApiResourceImpl implements ApiResource {


    private final SysResourceService resourceService;

    private final SysResourceCategoryService categoryService;

    @Override
    @GetMapping
    @Operation(summary = "获取资源信息")
    public Result<SysResourceAO> getResourceById(String id) {
        return Result.ok(resourceService.getSysResource(id));
    }

    @Override
    @PostMapping
    @Operation(summary = "添加资源")
    public Result<Boolean> addResource(@RequestBody ApiSysResourceAddParam param) {
        resourceService.addResource(SysResourceConvert.INSTANCE.toDTO(param));
        return Result.ok(true);
    }

    @Override
    @PostMapping("category")
    @Operation(summary = "添加资源类型")
    public Result<Boolean> addResourceCategory(@RequestBody ApiResourceCategoryAddParam param) {
        categoryService.addResourceCategory(SysResourceCategoryConvert.INSTANCE.toDTO(param));
        return Result.ok(true);
    }
}
