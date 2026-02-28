package com.carlos.org.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.convert.OrgRoleConvert;
import com.carlos.org.manager.OrgRoleManager;
import com.carlos.org.pojo.dto.OrgRoleDTO;
import com.carlos.org.pojo.param.OrgRoleCreateParam;
import com.carlos.org.pojo.param.OrgRolePageParam;
import com.carlos.org.pojo.param.OrgRoleUpdateParam;
import com.carlos.org.pojo.vo.OrgRoleVO;
import com.carlos.org.service.OrgRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 系统角色 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/role")
@Tag(name = "系统角色")
public class OrgRoleController {

    public static final String BASE_NAME = "系统角色";

    private final OrgRoleService roleService;

    private final OrgRoleManager roleManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgRoleCreateParam param) {
        OrgRoleDTO dto = OrgRoleConvert.INSTANCE.toDTO(param);
        roleService.addOrgRole(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        roleService.deleteOrgRole(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgRoleUpdateParam param) {
        OrgRoleDTO dto = OrgRoleConvert.INSTANCE.toDTO(param);
        roleService.updateOrgRole(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgRoleVO detail(String id) {
        return OrgRoleConvert.INSTANCE.toVO(roleManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgRoleVO> page(OrgRolePageParam param) {
        return roleManager.getPage(param);
    }
}
