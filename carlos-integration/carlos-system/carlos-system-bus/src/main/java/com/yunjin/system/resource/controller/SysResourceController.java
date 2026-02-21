package com.carlos.system.resource.controller;


import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.system.resource.convert.SysResourceConvert;
import com.carlos.system.resource.manager.SysResourceManager;
import com.carlos.system.resource.pojo.dto.SysResourceDTO;
import com.carlos.system.resource.pojo.dto.SysResourceGroupDTO;
import com.carlos.system.resource.pojo.dto.SysResourceTreeDTO;
import com.carlos.system.resource.pojo.param.SysResourceCreateParam;
import com.carlos.system.resource.pojo.param.SysResourcePageParam;
import com.carlos.system.resource.pojo.param.SysResourceUpdateParam;
import com.carlos.system.resource.pojo.vo.SysResourceGroupVO;
import com.carlos.system.resource.pojo.vo.SysResourceTreeVO;
import com.carlos.system.resource.pojo.vo.SysResourceVO;
import com.carlos.system.resource.service.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统资源 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/resource")
@Api(tags = "系统资源")
public class SysResourceController {

    public static final String BASE_NAME = "系统资源";

    private final SysResourceService resourceService;

    private final SysResourceManager resourceManager;


    @PostMapping
    @ApiOperation(value = "新增" + BASE_NAME)
    @Log(title = "新增" + BASE_NAME, businessType = BusinessType.INSERT)
    public void add(@RequestBody @Validated SysResourceCreateParam param) {
        SysResourceDTO dto = SysResourceConvert.INSTANCE.toDTO(param);
        this.resourceService.addResource(dto);
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    @Log(title = "删除" + BASE_NAME, businessType = BusinessType.DELETE)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        this.resourceService.deleteResource(param.getIds());
    }

    @PostMapping("update")
    @ApiOperation(value = "更新" + BASE_NAME)
    @Log(title = "更新" + BASE_NAME, businessType = BusinessType.UPDATE)
    public void update(@RequestBody @Validated SysResourceUpdateParam param) {
        SysResourceDTO dto = SysResourceConvert.INSTANCE.toDTO(param);
        this.resourceService.updateResource(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = BASE_NAME + "详情")
    public SysResourceVO detail(@PathVariable String id) {
        return SysResourceConvert.INSTANCE.toVO(this.resourceManager.getDtoById(id));
    }

    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "列表")
    public Paging<SysResourceVO> page(SysResourcePageParam param) {
        return this.resourceManager.getPage(param);
    }

    @GetMapping("tree")
    @ApiOperation(value = "资源Tree下拉列表", notes = "菜单资源树形列表")
    public List<SysResourceTreeVO> resourceTree() {
        List<SysResourceTreeDTO> resourceTree = this.resourceManager.getResourceTree(null);
        return SysResourceConvert.INSTANCE.toTreeVO(resourceTree);
    }

    @GetMapping("group")
    @ApiOperation(value = "获取资源分组列表", notes = "资源分组列表")
    public List<SysResourceGroupVO> group() {
        List<SysResourceGroupDTO> resourceGroup = this.resourceService.getResourceGroup();
        return SysResourceConvert.INSTANCE.toGroupVO(resourceGroup);
    }
}
