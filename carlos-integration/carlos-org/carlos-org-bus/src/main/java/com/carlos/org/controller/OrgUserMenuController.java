package com.carlos.org.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.convert.OrgUserMenuConvert;
import com.carlos.org.manager.OrgUserMenuManager;
import com.carlos.org.pojo.dto.OrgUserMenuDTO;
import com.carlos.org.pojo.emuns.UserMenuCollectionTypeEnum;
import com.carlos.org.pojo.param.OrgUserMenuCreateParam;
import com.carlos.org.pojo.param.OrgUserMenuPageParam;
import com.carlos.org.pojo.param.OrgUserMenuUpdateParam;
import com.carlos.org.pojo.vo.OrgUserMenuVO;
import com.carlos.org.service.OrgUserMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 用户菜单收藏表 rest服务接口
 * </p>
 *
 * @author carlos
 * @date    2024-2-28 11:10:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/user/menu")
@Tag(name = "用户菜单收藏表")
public class OrgUserMenuController {

    public static final String BASE_NAME = "用户菜单收藏表";

    private final OrgUserMenuService userMenuService;

    private final OrgUserMenuManager userMenuManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgUserMenuCreateParam param) {
        OrgUserMenuDTO dto = OrgUserMenuConvert.INSTANCE.toDTO(param);
        userMenuService.addOrgUserMenu(dto);
    }


    @PostMapping("click")
    @Operation(summary = "点击" + BASE_NAME)
    public UserMenuCollectionTypeEnum click(@RequestBody @Validated OrgUserMenuCreateParam param) {
        OrgUserMenuDTO dto = OrgUserMenuConvert.INSTANCE.toDTO(param);
        return userMenuService.click(dto);
    }


    @PostMapping("favorite")
    @Operation(summary = "判断是否收藏" + BASE_NAME)
    public UserMenuCollectionTypeEnum favorite(@RequestBody @Validated OrgUserMenuCreateParam param) {
        OrgUserMenuDTO dto = OrgUserMenuConvert.INSTANCE.toDTO(param);
        return userMenuService.favorite(dto);
    }

    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        userMenuService.deleteOrgUserMenu(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgUserMenuUpdateParam param) {
        OrgUserMenuDTO dto = OrgUserMenuConvert.INSTANCE.toDTO(param);
        userMenuService.updateOrgUserMenu(dto);
    }


    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public OrgUserMenuVO detail(@PathVariable String id) {
        return OrgUserMenuConvert.INSTANCE.toVO(userMenuManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgUserMenuVO> page(OrgUserMenuPageParam param) {
        return userMenuManager.getPage(param);
    }
}
