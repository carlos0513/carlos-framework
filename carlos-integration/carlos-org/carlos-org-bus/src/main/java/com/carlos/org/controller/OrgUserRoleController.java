package com.carlos.org.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.convert.OrgUserRoleConvert;
import com.carlos.org.manager.OrgUserRoleManager;
import com.carlos.org.pojo.dto.OrgUserRoleDTO;
import com.carlos.org.pojo.param.OrgUserRoleCreateParam;
import com.carlos.org.pojo.param.OrgUserRolePageParam;
import com.carlos.org.pojo.param.OrgUserRoleUpdateParam;
import com.carlos.org.pojo.vo.OrgUserRoleVO;
import com.carlos.org.service.OrgUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 用户角色 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/user/role")
@Tag(name = "用户角色")
public class OrgUserRoleController {

    public static final String BASE_NAME = "用户角色";

    private final OrgUserRoleService userRoleService;

    private final OrgUserRoleManager userRoleManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgUserRoleCreateParam param) {
        OrgUserRoleDTO dto = OrgUserRoleConvert.INSTANCE.toDTO(param);
        userRoleService.addOrgUserRole(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        userRoleService.deleteOrgUserRole(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgUserRoleUpdateParam param) {
        OrgUserRoleDTO dto = OrgUserRoleConvert.INSTANCE.toDTO(param);
        userRoleService.updateOrgUserRole(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgUserRoleVO detail(String id) {
        return OrgUserRoleConvert.INSTANCE.toVO(userRoleManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgUserRoleVO> page(OrgUserRolePageParam param) {
        return userRoleManager.getPage(param);
    }
}
