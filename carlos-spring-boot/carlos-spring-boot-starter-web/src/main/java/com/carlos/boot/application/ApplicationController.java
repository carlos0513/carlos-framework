package com.carlos.boot.application;

import com.carlos.boot.enums.EnumService;
import com.carlos.boot.resource.ResourceService;
import com.carlos.core.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 项目信息
 * </p>
 *
 * @author carlos
 */
@Slf4j
@RestController
@RequestMapping("/app")
@Tag(name = "应用信息")
public class ApplicationController {

    private final EnumService enumService;

    private final ResourceService resourceService;

    public ApplicationController(@Nullable EnumService enumService, ResourceService resourceService) {
        this.enumService = enumService;
        this.resourceService = resourceService;
    }

    @GetMapping("/enums")
    @Operation(summary = "枚举字典")
    public Result<?> enumList() {
        if (enumService == null) {
            return Result.error("枚举功能未开启");
        }
        return Result.success(enumService.getEnumList());
    }

    @GetMapping("/enum")
    @Operation(summary = "枚举详情")
    public Result<?> enumInfo(String name) {
        if (enumService == null) {
            return Result.error("枚举功能未开启");
        }
        return Result.success(enumService.getEnumInfo(name));
    }

    @GetMapping("init/resource")
    @Operation(summary = "初始化资源")
    public Result<?> initResource() {
        return Result.success(resourceService.init());
    }
}
