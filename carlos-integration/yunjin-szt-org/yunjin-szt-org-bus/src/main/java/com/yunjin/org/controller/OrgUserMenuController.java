package com.yunjin.org.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.convert.OrgUserMenuConvert;
import com.yunjin.org.pojo.emuns.UserMenuCollectionTypeEnum;
import com.yunjin.org.pojo.param.OrgUserMenuCreateParam;
import com.yunjin.org.pojo.param.OrgUserMenuUpdateParam;
import com.yunjin.org.pojo.param.OrgUserMenuPageParam;
import com.yunjin.org.pojo.dto.OrgUserMenuDTO;
import com.yunjin.org.pojo.vo.OrgUserMenuVO;
import com.yunjin.org.service.OrgUserMenuService;
import com.yunjin.org.manager.OrgUserMenuManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 用户菜单收藏表 rest服务接口
 * </p>
 *
 * @author  yunjin
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


    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgUserMenuCreateParam param) {
        OrgUserMenuDTO dto = OrgUserMenuConvert.INSTANCE.toDTO(param);
        userMenuService.addOrgUserMenu(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("click")
    @Operation(summary = "点击" + BASE_NAME)
    public UserMenuCollectionTypeEnum click(@RequestBody @Validated OrgUserMenuCreateParam param) {
        OrgUserMenuDTO dto = OrgUserMenuConvert.INSTANCE.toDTO(param);
        return userMenuService.click(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("favorite")
    @Operation(summary = "判断是否收藏" + BASE_NAME)
    public UserMenuCollectionTypeEnum favorite(@RequestBody @Validated OrgUserMenuCreateParam param) {
        OrgUserMenuDTO dto = OrgUserMenuConvert.INSTANCE.toDTO(param);
        return userMenuService.favorite(dto);
    }
    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        userMenuService.deleteOrgUserMenu(param.getIds());
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgUserMenuUpdateParam param) {
        OrgUserMenuDTO dto = OrgUserMenuConvert.INSTANCE.toDTO(param);
        userMenuService.updateOrgUserMenu(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public OrgUserMenuVO detail(@PathVariable String id) {
        return OrgUserMenuConvert.INSTANCE.toVO(userMenuManager.getDtoById(id));
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<OrgUserMenuVO> page(OrgUserMenuPageParam param) {
        return userMenuManager.getPage(param);
    }
}
