package com.carlos.system.menu.apiimpl;

import cn.hutool.core.collection.CollectionUtil;
import com.carlos.core.response.Result;
import com.carlos.system.api.ApiMenu;
import com.carlos.system.enums.MenuType;
import com.carlos.system.menu.convert.MenuConvert;
import com.carlos.system.menu.manager.MenuManager;
import com.carlos.system.menu.pojo.dto.MenuDTO;
import com.carlos.system.menu.service.MenuService;
import com.carlos.system.pojo.ao.MenuAO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单 api接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sys/menu")
@Tag(name = "系统菜单Feign接口", hidden = true)
public class ApiMenuImpl implements ApiMenu {

    private final MenuManager menuManager;
    private final MenuService menuService;

    @Override
    @GetMapping
    @Operation(summary = "获取菜单信息")
    public Result<MenuAO> getMenuById(@RequestParam("id") String id) {
        MenuDTO dto = menuManager.getDtoById(id);
        MenuAO ao = MenuConvert.INSTANCE.toAO(dto);
        return Result.ok(ao);
    }


    @Override
    @PostMapping("list")
    @Operation(summary = "获取菜单信息")
    public Result<List<MenuAO>> listMenus(@RequestBody Set<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Result.ok(Collections.emptyList());
        }
        List<MenuDTO> menus = menuService.getByIds(ids);
        return Result.ok(MenuConvert.INSTANCE.toAOList(menus));
    }


    @GetMapping("all")
    @Operation(summary = "获取所有菜单")
    @Override
    public Result<List<MenuAO>> allMenu() {
        List<MenuDTO> menus = menuService.all();
        return Result.ok(MenuConvert.INSTANCE.toAOList(menus));
    }

    /**
     * 获取某个类型所有菜单
     *
     * @return com.carlos.core.response.Result<java.util.List < com.carlos.system.pojo.ao.MenuAO>>
     * @author Carlos
     * @date 2023/7/4 9:04
     */
    @Override
    @GetMapping(value = "type")
    public Result<List<MenuAO>> listMenuByType(MenuType menuType) {
        List<MenuDTO> menus = menuService.listByType(menuType);
        return Result.ok(MenuConvert.INSTANCE.toAOList(menus));
    }
}

