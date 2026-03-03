package com.carlos.org.position.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.position.convert.OrgPositionRoleConvert;
import com.carlos.org.position.manager.OrgPositionRoleManager;
import com.carlos.org.position.pojo.dto.OrgPositionRoleDTO;
import com.carlos.org.position.pojo.param.OrgPositionRoleCreateParam;
import com.carlos.org.position.pojo.param.OrgPositionRolePageParam;
import com.carlos.org.position.pojo.param.OrgPositionRoleUpdateParam;
import com.carlos.org.position.pojo.vo.OrgPositionRoleVO;
import com.carlos.org.position.service.OrgPositionRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 岗位角色关联表（岗位默认权限配置） rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/position/role")
@Tag(name = "岗位角色关联表（岗位默认权限配置）")
public class OrgPositionRoleController {

    public static final String BASE_NAME = "岗位角色关联表（岗位默认权限配置）";

    private final OrgPositionRoleService positionRoleService;

    private final OrgPositionRoleManager positionRoleManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgPositionRoleCreateParam param) {
        OrgPositionRoleDTO dto = OrgPositionRoleConvert.INSTANCE.toDTO(param);
        positionRoleService.addOrgPositionRole(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        positionRoleService.deleteOrgPositionRole(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgPositionRoleUpdateParam param) {
        OrgPositionRoleDTO dto = OrgPositionRoleConvert.INSTANCE.toDTO(param);
        positionRoleService.updateOrgPositionRole(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgPositionRoleVO detail(String id) {
        return OrgPositionRoleConvert.INSTANCE.toVO(positionRoleManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgPositionRoleVO> page(OrgPositionRolePageParam param) {
        return positionRoleManager.getPage(param);
    }
}
