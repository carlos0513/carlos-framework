package com.carlos.system.menu.apiimpl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.response.Result;
import com.carlos.system.api.ApiMenuOperate;
import com.carlos.system.menu.convert.MenuOperateConvert;
import com.carlos.system.menu.pojo.dto.MenuOperateDTO;
import com.carlos.system.menu.service.MenuOperateService;
import com.carlos.system.pojo.ao.MenuOperateAO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单操作 api接口
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@ApiIgnore
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sys/menu/operate")
@Api(tags = "菜单操作Feign接口", hidden = true)
public class ApiMenuOperateImpl implements ApiMenuOperate {


    private final MenuOperateService menuOperateService;

    @Override
    @GetMapping("/list")
    @ApiOperation(value = "根据ids获取菜单操作列表")
    public Result<List<MenuOperateAO>> listByIds(Set<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Result.ok(Collections.emptyList());
        }
        List<MenuOperateDTO> menuOperates = menuOperateService.listByIds(ids);
        return Result.ok(MenuOperateConvert.INSTANCE.toAOList(menuOperates));

    }

    @Override
    @GetMapping(value = "/api/menu/operate/list/menuId")
    @ApiOperation(value = "根据menuId获取菜单操作列表")
    public Result<List<MenuOperateAO>> listByMenuId(String menuId) {
        if (StrUtil.isEmpty(menuId)) {
            return Result.ok(Collections.emptyList());
        }
        List<MenuOperateDTO> menuOperates = menuOperateService.listByMenuId(menuId);
        return Result.ok(MenuOperateConvert.INSTANCE.toAOList(menuOperates));
    }

    @Override
    public Result<List<MenuOperateAO>> listByMenuIds(Set<String> menuIds) {
        if (CollectionUtil.isEmpty(menuIds)) {
            return Result.ok(Collections.emptyList());
        }
        List<MenuOperateDTO> menuOperates = menuOperateService.listByMenuIds(menuIds);
        return Result.ok(MenuOperateConvert.INSTANCE.toAOList(menuOperates));
    }
}
