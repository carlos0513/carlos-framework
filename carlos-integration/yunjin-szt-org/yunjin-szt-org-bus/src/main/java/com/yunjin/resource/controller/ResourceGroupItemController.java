package com.yunjin.resource.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.resource.convert.ResourceGroupItemConvert;
import com.yunjin.resource.manager.ResourceGroupItemManager;
import com.yunjin.resource.pojo.dto.ResourceGroupItemDTO;
import com.yunjin.resource.pojo.param.ResourceGroupItemCreateParam;
import com.yunjin.resource.pojo.param.ResourceGroupItemPageParam;
import com.yunjin.resource.pojo.param.ResourceGroupItemUpdateParam;
import com.yunjin.resource.pojo.vo.ResourceGroupItemVO;
import com.yunjin.resource.service.ResourceGroupItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 资源组详情项 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/resource/group/item")
@Tag(name = "资源组详情项")
public class ResourceGroupItemController {

    public static final String BASE_NAME = "资源组详情项";

    private final ResourceGroupItemService resourceGroupItemService;

    private final ResourceGroupItemManager resourceGroupItemManager;


    @ApiOperationSupport(author = "yunjin")
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated ResourceGroupItemCreateParam param) {
        ResourceGroupItemDTO dto = ResourceGroupItemConvert.INSTANCE.toDTO(param);
        resourceGroupItemService.addResourceGroupItem(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        resourceGroupItemService.deleteResourceGroupItem(param.getIds());
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated ResourceGroupItemUpdateParam param) {
        ResourceGroupItemDTO dto = ResourceGroupItemConvert.INSTANCE.toDTO(param);
        resourceGroupItemService.updateResourceGroupItem(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public ResourceGroupItemVO detail(@PathVariable String id) {
        return ResourceGroupItemConvert.INSTANCE.toVO(resourceGroupItemManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<ResourceGroupItemVO> page(ResourceGroupItemPageParam param) {
        return resourceGroupItemManager.getPage(param);
    }
}
