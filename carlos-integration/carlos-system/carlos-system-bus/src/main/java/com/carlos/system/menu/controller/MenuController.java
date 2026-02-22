package com.carlos.system.menu.controller;


import cn.hutool.core.util.StrUtil;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
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
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/menu")
@Tag(name = "系统菜单")
public class MenuController {

    public static final String BASE_NAME = "系统菜单";

    private final MenuService menuService;

    private final MenuManager menuManager;


    @PostMapping

    @Operation(summary = "新增" + BASE_NAME)
    @Log(title = "新增" + BASE_NAME, businessType = BusinessType.INSERT)
    public void add(@RequestBody @Validated MenuCreateParam param) {
        MenuDTO dto = MenuConvert.INSTANCE.toDTO(param);
        this.menuService.addMenu(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    @Log(title = "删除" + BASE_NAME, businessType = BusinessType.DELETE)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        this.menuService.deleteMenu(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    @Log(title = "更新" + BASE_NAME, businessType = BusinessType.UPDATE)
    public void update(@RequestBody @Validated MenuUpdateParam param) {
        MenuDTO dto = MenuConvert.INSTANCE.toDTO(param);
        this.menuService.updateMenu(dto);
    }


    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    @Log(title = "获取菜单详情", businessType = BusinessType.QUERY_DETAIL)
    public MenuVO detail(@PathVariable String id) {
        return MenuConvert.INSTANCE.toVO(this.menuManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    @Hidden
    public Paging<MenuVO> page(MenuPageParam param) {
        return this.menuManager.getPage(param);
    }


    @GetMapping("tree/select")
    @Operation(summary = "菜单树形下拉列表", description = "菜单树形列表，包含详情信息")
    @Log(title = "菜单树形下拉列表", businessType = BusinessType.QUERY)
    public List<MenuTreeVO> select(@RequestParam(value = "menuType", required = false) MenuType menuType) {
        List<MenuDTO> menuTree = this.menuManager.getMenuTree(0L, false, menuType);
        return MenuConvert.INSTANCE.toTreeListVO(menuTree);
    }


    @GetMapping("tree/list")
    @Operation(summary = "菜单树形列表", description = "菜单下拉列表，仅包含基本信息")
    public List<MenuRecursionVO> treeList(String parentId, String title, @RequestParam(value = "menuType", required = false) MenuType menuType) {
        List<MenuDTO> menus;
        if (StrUtil.isNotBlank(title)) {
            menus = this.menuManager.listByTitle(title, menuType);
        } else {
            menus = this.menuManager.getMenuTree(parentId, true, menuType);
        }
        return MenuConvert.INSTANCE.toRecursionListVO(menus);
    }


    @GetMapping("list")
    @Operation(summary = "菜单列表", description = "逐级请求")
    public List<MenuVO> list(String parentId, @RequestParam(value = "menuType", required = false) MenuType menuType) {
        List<MenuDTO> menuTree = this.menuManager.getMenusByParentId(parentId, true, menuType);
        return MenuConvert.INSTANCE.toListVO(menuTree);
    }
}
