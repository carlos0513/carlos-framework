package com.carlos.system.menu.controller;


import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.system.AuthorConstant;
import com.carlos.system.enums.MenuType;
import com.carlos.system.menu.convert.MenuConvert;
import com.carlos.system.menu.manager.MenuManager;
import com.carlos.system.menu.pojo.dto.MenuDTO;
import com.carlos.system.menu.pojo.param.MenuCreateParam;
import com.carlos.system.menu.pojo.param.MenuPageParam;
import com.carlos.system.menu.pojo.param.MenuUpdateParam;
import com.carlos.system.menu.pojo.vo.MenuRecursionVO;
import com.carlos.system.menu.pojo.vo.MenuTreeVO;
import com.carlos.system.menu.pojo.vo.MenuVO;
import com.carlos.system.menu.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统菜单 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/menu")
@Api(tags = "系统菜单")
public class MenuController {

    public static final String BASE_NAME = "系统菜单";

    private final MenuService menuService;

    private final MenuManager menuManager;


    @PostMapping
    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @ApiOperation(value = "新增" + BASE_NAME)
    @Log(title = "新增" + BASE_NAME, businessType = BusinessType.INSERT)
    public void add(@RequestBody @Validated MenuCreateParam param) {
        MenuDTO dto = MenuConvert.INSTANCE.toDTO(param);
        this.menuService.addMenu(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    @Log(title = "删除" + BASE_NAME, businessType = BusinessType.DELETE)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        this.menuService.deleteMenu(param.getIds());
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("update")
    @ApiOperation(value = "更新" + BASE_NAME)
    @Log(title = "更新" + BASE_NAME, businessType = BusinessType.UPDATE)
    public void update(@RequestBody @Validated MenuUpdateParam param) {
        MenuDTO dto = MenuConvert.INSTANCE.toDTO(param);
        this.menuService.updateMenu(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("{id}")
    @ApiOperation(value = BASE_NAME + "详情")
    @Log(title = "获取菜单详情", businessType = BusinessType.QUERY_DETAIL)
    public MenuVO detail(@PathVariable String id) {
        return MenuConvert.INSTANCE.toVO(this.menuManager.getDtoById(id));
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表", hidden = true)
    public Paging<MenuVO> page(MenuPageParam param) {
        return this.menuManager.getPage(param);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("tree/select")
    @ApiOperation(value = "菜单树形下拉列表", notes = "菜单树形列表，包含详情信息")
    @Log(title = "菜单树形下拉列表", businessType = BusinessType.QUERY)
    public List<MenuTreeVO> select(@RequestParam(value = "menuType", required = false) MenuType menuType) {
        List<MenuDTO> menuTree = this.menuManager.getMenuTree(0L, false, menuType);
        return MenuConvert.INSTANCE.toTreeListVO(menuTree);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("tree/list")
    @ApiOperation(value = "菜单树形列表", notes = "菜单下拉列表，仅包含基本信息")
    public List<MenuRecursionVO> treeList(String parentId, String title, @RequestParam(value = "menuType", required = false) MenuType menuType) {
        List<MenuDTO> menus;
        if (StrUtil.isNotBlank(title)) {
            menus = this.menuManager.listByTitle(title, menuType);
        } else {
            menus = this.menuManager.getMenuTree(parentId, true, menuType);
        }
        return MenuConvert.INSTANCE.toRecursionListVO(menus);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("list")
    @ApiOperation(value = "菜单列表", notes = "逐级请求")
    public List<MenuVO> list(String parentId, @RequestParam(value = "menuType", required = false) MenuType menuType) {
        List<MenuDTO> menuTree = this.menuManager.getMenusByParentId(parentId, true, menuType);
        return MenuConvert.INSTANCE.toListVO(menuTree);
    }
}
