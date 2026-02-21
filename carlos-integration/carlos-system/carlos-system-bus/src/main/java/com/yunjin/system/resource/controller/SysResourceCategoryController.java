package com.carlos.system.resource.controller;


import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.system.resource.convert.SysResourceCategoryConvert;
import com.carlos.system.resource.manager.SysResourceCategoryManager;
import com.carlos.system.resource.pojo.dto.ResourceCategoryDTO;
import com.carlos.system.resource.pojo.param.SysResourceCategoryCreateParam;
import com.carlos.system.resource.pojo.param.SysResourceCategoryPageParam;
import com.carlos.system.resource.pojo.param.SysResourceCategoryUpdateParam;
import com.carlos.system.resource.pojo.vo.SysResourceCategoryRecursionVO;
import com.carlos.system.resource.pojo.vo.SysResourceCategoryVO;
import com.carlos.system.resource.service.SysResourceCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 资源分类 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-1-5 17:23:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/resource/category")
@Api(tags = "资源分类")
public class SysResourceCategoryController {

    public static final String BASE_NAME = "资源分类";

    private final SysResourceCategoryService resourceCategoryService;

    private final SysResourceCategoryManager resourceCategoryManager;


    @PostMapping
    @ApiOperation(value = "新增" + BASE_NAME)
    @Log(title = "新增" + BASE_NAME, businessType = BusinessType.INSERT)
    public void add(@RequestBody @Validated SysResourceCategoryCreateParam param) {
        ResourceCategoryDTO dto = SysResourceCategoryConvert.INSTANCE.toDTO(param);
        this.resourceCategoryService.addResourceCategory(dto);
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    @Log(title = "删除" + BASE_NAME, businessType = BusinessType.DELETE)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        this.resourceCategoryService.deleteResourceCategory(param.getIds());
    }

    @PostMapping("update")
    @ApiOperation(value = "更新" + BASE_NAME)
    @Log(title = "更新" + BASE_NAME, businessType = BusinessType.UPDATE)
    public void update(@RequestBody @Validated SysResourceCategoryUpdateParam param) {
        ResourceCategoryDTO dto = SysResourceCategoryConvert.INSTANCE.toDTO(param);
        this.resourceCategoryService.updateResourceCategory(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = BASE_NAME + "详情")
    public SysResourceCategoryVO detail(@PathVariable String id) {
        return SysResourceCategoryConvert.INSTANCE.toVO(this.resourceCategoryManager.getDtoById(id));
    }

    @GetMapping("list")
    @ApiOperation(value = BASE_NAME + "列表")
    public List<SysResourceCategoryVO> list(String parentId) {
        return SysResourceCategoryConvert.INSTANCE.toListVO(this.resourceCategoryManager.getCategoryTree(parentId, true));
    }

    @GetMapping("tree/select")
    @ApiOperation(value = BASE_NAME + "下拉选项")
    public List<SysResourceCategoryRecursionVO> select() {
        return SysResourceCategoryConvert.INSTANCE.toRecursionListVO(this.resourceCategoryManager.getCategoryTree(null, false));
    }

    @GetMapping("tree/list")
    @ApiOperation(value = BASE_NAME + "树形列表")
    public List<SysResourceCategoryRecursionVO> treeList() {
        return SysResourceCategoryConvert.INSTANCE.toRecursionListVO(this.resourceCategoryManager.getCategoryTree(null, true));
    }

    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表", hidden = true)
    public Paging<SysResourceCategoryVO> page(SysResourceCategoryPageParam param) {
        return this.resourceCategoryManager.getPage(param);
    }
}
