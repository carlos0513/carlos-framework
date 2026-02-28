package com.carlos.org.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.convert.OrgDepartmentRoleConvert;
import com.carlos.org.manager.OrgDepartmentRoleManager;
import com.carlos.org.pojo.dto.OrgDepartmentRoleDTO;
import com.carlos.org.pojo.param.OrgDepartmentRoleCreateParam;
import com.carlos.org.pojo.param.OrgDepartmentRolePageParam;
import com.carlos.org.pojo.param.OrgDepartmentRoleUpdateParam;
import com.carlos.org.pojo.vo.OrgDepartmentRoleVO;
import com.carlos.org.service.OrgDepartmentRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 部门角色 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/department/role")
@Tag(name = "部门角色")
public class OrgDepartmentRoleController {

    public static final String BASE_NAME = "部门角色";

    private final OrgDepartmentRoleService departmentRoleService;

    private final OrgDepartmentRoleManager departmentRoleManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgDepartmentRoleCreateParam param) {
        OrgDepartmentRoleDTO dto = OrgDepartmentRoleConvert.INSTANCE.toDTO(param);
        departmentRoleService.addOrgDepartmentRole(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        departmentRoleService.deleteOrgDepartmentRole(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgDepartmentRoleUpdateParam param) {
        OrgDepartmentRoleDTO dto = OrgDepartmentRoleConvert.INSTANCE.toDTO(param);
        departmentRoleService.updateOrgDepartmentRole(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgDepartmentRoleVO detail(String id) {
        return OrgDepartmentRoleConvert.INSTANCE.toVO(departmentRoleManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgDepartmentRoleVO> page(OrgDepartmentRolePageParam param) {
        return departmentRoleManager.getPage(param);
    }
}
