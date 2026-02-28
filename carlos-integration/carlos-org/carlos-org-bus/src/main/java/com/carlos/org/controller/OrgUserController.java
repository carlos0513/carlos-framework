package com.carlos.org.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.convert.OrgUserConvert;
import com.carlos.org.manager.OrgUserManager;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.param.OrgUserCreateParam;
import com.carlos.org.pojo.param.OrgUserPageParam;
import com.carlos.org.pojo.param.OrgUserUpdateParam;
import com.carlos.org.pojo.vo.OrgUserVO;
import com.carlos.org.service.OrgUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 系统用户 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/user")
@Tag(name = "系统用户")
public class OrgUserController {

    public static final String BASE_NAME = "系统用户";

    private final OrgUserService userService;

    private final OrgUserManager userManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgUserCreateParam param) {
        OrgUserDTO dto = OrgUserConvert.INSTANCE.toDTO(param);
        userService.addOrgUser(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        userService.deleteOrgUser(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgUserUpdateParam param) {
        OrgUserDTO dto = OrgUserConvert.INSTANCE.toDTO(param);
        userService.updateOrgUser(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public OrgUserVO detail(String id) {
        return OrgUserConvert.INSTANCE.toVO(userManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgUserVO> page(OrgUserPageParam param) {
        return userManager.getPage(param);
    }
}
