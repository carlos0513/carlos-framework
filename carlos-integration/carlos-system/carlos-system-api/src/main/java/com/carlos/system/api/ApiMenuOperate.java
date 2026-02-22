package com.carlos.system.api;

import com.carlos.core.response.Result;
import com.carlos.system.ServiceNameConstant;
import com.carlos.system.fallback.ApiMenuOperateFallbackFactory;
import com.carlos.system.pojo.ao.MenuOperateAO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单操作 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@FeignClient(value = ServiceNameConstant.SYSTEM, contextId = "menuOperate", path = "/api/sys/menu/operate", fallbackFactory = ApiMenuOperateFallbackFactory.class)
public interface ApiMenuOperate {

    /**
     * @Title: listByIds
     * @Description: 根据ids获取菜单操作列表
     * @Date: 2023/7/7 16:12
     * @Parameters: [ids]
     * @Return com.carlos.core.response.Result<com.carlos.system.pojo.ao.MenuAO>
     */
    @GetMapping(value = "/list")
    Result<List<MenuOperateAO>> listByIds(@RequestParam("ids") Set<String> ids);

    /**
     * @Title: listByMenuId
     * @Description: 根据menuId获取菜单操作列表
     * @Date: 2023/7/7 16:12
     * @Parameters: [ids]
     * @Return com.carlos.core.response.Result<com.carlos.system.pojo.ao.MenuAO>
     */
    @GetMapping(value = "/list/menuId")
    Result<List<MenuOperateAO>> listByMenuId(@RequestParam("menuId") String id);

    /**
     * @Title: listByMenuId
     * @Description: 根据menuIds获取菜单操作列表
     * @Date: 2023/7/7 16:12
     * @Parameters: [ids]
     * @Return com.carlos.core.response.Result<com.carlos.system.pojo.ao.MenuAO>
     */
    @GetMapping(value = "/list/menuIds")
    Result<List<MenuOperateAO>> listByMenuIds(@RequestParam("menuIds") Set<String> menuIds);
}
