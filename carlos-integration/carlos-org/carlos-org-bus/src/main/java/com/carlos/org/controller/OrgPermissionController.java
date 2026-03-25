package com.carlos.org.controller;


import com.carlos.core.param.ParamIdSet;
import com.carlos.core.response.Result;
import com.carlos.org.convert.OrgPermissionConvert;
import com.carlos.org.manager.OrgPermissionManager;
import com.carlos.org.pojo.dto.OrgPermissionDTO;
import com.carlos.org.pojo.param.OrgPermissionCreateParam;
import com.carlos.org.pojo.param.OrgPermissionSortParam;
import com.carlos.org.pojo.param.OrgPermissionSyncApiParam;
import com.carlos.org.pojo.param.OrgPermissionUpdateParam;
import com.carlos.org.pojo.vo.OrgPermissionVO;
import com.carlos.org.service.OrgPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 权限 rest服务接口
 * </p>
 * <p>实现PM001-PM006所有权限管理需求</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/permission")
@Tag(name = "权限-PM001到PM006完整实现")
public class OrgPermissionController {

    public static final String BASE_NAME = "权限";

    private final OrgPermissionService permissionService;

    private final OrgPermissionManager permissionManager;


    /**
     * PM-001 权限树查询
     */
    @GetMapping("tree")
    @Operation(summary = "PM-001 " + BASE_NAME + "树")
    public Result<List<OrgPermissionVO>> tree() {
        List<OrgPermissionDTO> dtoList = permissionService.getPermissionTree();
        return Result.success(OrgPermissionConvert.INSTANCE.buildTreeVOList(dtoList));
    }


    /**
     * PM-002 新增权限
     */
    @PostMapping("add")
    @Operation(summary = "PM-002 新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgPermissionCreateParam param) {
        OrgPermissionDTO dto = OrgPermissionConvert.INSTANCE.toDTO(param);
        permissionService.addPermission(dto);
    }


    /**
     * PM-003 编辑权限
     */
    @PostMapping("update")
    @Operation(summary = "PM-003 更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgPermissionUpdateParam param) {
        OrgPermissionDTO dto = OrgPermissionConvert.INSTANCE.toDTO(param);
        permissionService.updatePermission(dto);
    }


    /**
     * PM-004 删除权限
     */
    @PostMapping("delete")
    @Operation(summary = "PM-004 删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        permissionService.deletePermission(param.getIds());
    }


    /**
     * PM-005 权限排序
     */
    @PostMapping("sort")
    @Operation(summary = "PM-005 " + BASE_NAME + "排序")
    public void sort(@RequestBody @Validated OrgPermissionSortParam param) {
        permissionService.sortPermission(param);
    }


    /**
     * PM-006 同步API权限
     */
    @PostMapping("syncApi")
    @Operation(summary = "PM-006 同步API" + BASE_NAME)
    public void syncApi(@RequestBody @Validated OrgPermissionSyncApiParam param) {
        permissionService.syncApiPermission(param);
    }


    /**
     * 启用/禁用权限
     */
    @PostMapping("changeState")
    @Operation(summary = "启用/禁用" + BASE_NAME)
    public void changeState(@RequestParam Serializable id, @RequestParam Integer state) {
        permissionService.changeState(id, state);
    }


    /**
     * 权限详情
     */
    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgPermissionVO detail(@RequestParam Serializable id) {
        OrgPermissionDTO dto = permissionManager.getDtoById(id);
        return OrgPermissionConvert.INSTANCE.toVO(dto);
    }

}
