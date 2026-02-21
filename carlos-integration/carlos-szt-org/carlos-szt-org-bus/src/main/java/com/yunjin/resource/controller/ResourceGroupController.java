package com.yunjin.resource.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.resource.convert.ResourceGroupConvert;
import com.yunjin.resource.manager.ResourceGroupManager;
import com.yunjin.resource.pojo.dto.ResourceGroupDTO;
import com.yunjin.resource.pojo.param.ResourceGroupCreateParam;
import com.yunjin.resource.pojo.param.ResourceGroupPageParam;
import com.yunjin.resource.pojo.param.ResourceGroupUpdateParam;
import com.yunjin.resource.pojo.vo.ResourceGroupVO;
import com.yunjin.resource.service.ResourceGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 资源组 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/resource/group")
@Tag(name = "资源组")
public class ResourceGroupController {

    public static final String BASE_NAME = "资源组";

    private final ResourceGroupService resourceGroupService;

    private final ResourceGroupManager resourceGroupManager;


    @ApiOperationSupport(author = "yunjin")
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated ResourceGroupCreateParam param) {
        ResourceGroupDTO dto = ResourceGroupConvert.INSTANCE.toDTO(param);
        resourceGroupService.addResourceGroup(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        resourceGroupService.deleteResourceGroup(param.getIds());
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated ResourceGroupUpdateParam param) {
        ResourceGroupDTO dto = ResourceGroupConvert.INSTANCE.toDTO(param);
        resourceGroupService.updateResourceGroup(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public ResourceGroupVO detail(@PathVariable String id) {
        return ResourceGroupConvert.INSTANCE.toVO(resourceGroupManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<ResourceGroupVO> page(ResourceGroupPageParam param) {
        return resourceGroupManager.getPage(param);
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("list")
    @Operation(summary = BASE_NAME + "列表")
    public List<ResourceGroupVO> listAll() {
        return ResourceGroupConvert.INSTANCE.toVOS(resourceGroupService.listAll());
    }
}
