package com.yunjin.org.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.org.convert.RoleResourceGroupRefConvert;
import com.yunjin.org.manager.RoleResourceGroupRefManager;
import com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO;
import com.yunjin.org.pojo.param.RoleResourceGroupRefCreateParam;
import com.yunjin.org.pojo.param.RoleResourceGroupRefPageParam;
import com.yunjin.org.pojo.param.RoleResourceGroupRefUpdateParam;
import com.yunjin.org.pojo.vo.RoleResourceGroupRefVO;
import com.yunjin.org.service.RoleResourceGroupRefService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 角色资源组关联表 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("role/resource/group/ref")
@Tag(name = "角色资源组关联表")
public class RoleResourceGroupRefController {

    public static final String BASE_NAME = "角色资源组关联表";

    private final RoleResourceGroupRefService roleResourceGroupRefService;

    private final RoleResourceGroupRefManager roleResourceGroupRefManager;


    @ApiOperationSupport(author = "yunjin")
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated RoleResourceGroupRefCreateParam param) {
        RoleResourceGroupRefDTO dto = RoleResourceGroupRefConvert.INSTANCE.toDTO(param);
        roleResourceGroupRefService.addRoleResourceGroupRef(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        roleResourceGroupRefService.deleteRoleResourceGroupRef(param.getIds());
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated RoleResourceGroupRefUpdateParam param) {
        RoleResourceGroupRefDTO dto = RoleResourceGroupRefConvert.INSTANCE.toDTO(param);
        roleResourceGroupRefService.updateRoleResourceGroupRef(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public RoleResourceGroupRefVO detail(@PathVariable String id) {
        return RoleResourceGroupRefConvert.INSTANCE.toVO(roleResourceGroupRefManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<RoleResourceGroupRefVO> page(RoleResourceGroupRefPageParam param) {
        return roleResourceGroupRefManager.getPage(param);
    }
}
