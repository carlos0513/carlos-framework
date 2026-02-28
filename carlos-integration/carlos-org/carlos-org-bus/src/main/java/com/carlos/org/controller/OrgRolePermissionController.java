package com.carlos.org.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.convert.OrgRolePermissionConvert;
import com.carlos.org.manager.OrgRolePermissionManager;
import com.carlos.org.pojo.dto.OrgRolePermissionDTO;
import com.carlos.org.pojo.param.OrgRolePermissionCreateParam;
import com.carlos.org.pojo.param.OrgRolePermissionPageParam;
import com.carlos.org.pojo.param.OrgRolePermissionUpdateParam;
import com.carlos.org.pojo.vo.OrgRolePermissionVO;
import com.carlos.org.service.OrgRolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 角色权限 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/role/permission")
@Tag(name = "角色权限")
public class OrgRolePermissionController {

    public static final String BASE_NAME = "角色权限";

    private final OrgRolePermissionService rolePermissionService;

    private final OrgRolePermissionManager rolePermissionManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgRolePermissionCreateParam param) {
        OrgRolePermissionDTO dto = OrgRolePermissionConvert.INSTANCE.toDTO(param);
        rolePermissionService.addOrgRolePermission(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        rolePermissionService.deleteOrgRolePermission(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgRolePermissionUpdateParam param) {
        OrgRolePermissionDTO dto = OrgRolePermissionConvert.INSTANCE.toDTO(param);
        rolePermissionService.updateOrgRolePermission(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgRolePermissionVO detail(String id) {
        return OrgRolePermissionConvert.INSTANCE.toVO(rolePermissionManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgRolePermissionVO> page(OrgRolePermissionPageParam param) {
        return rolePermissionManager.getPage(param);
    }
}
