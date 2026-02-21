package com.carlos.system.api;

import com.carlos.core.response.Result;
import com.carlos.system.ServiceNameConstant;
import com.carlos.system.enums.MenuType;
import com.carlos.system.fallback.FeignMenuFallbackFactory;
import com.carlos.system.pojo.ao.MenuAO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@FeignClient(value = ServiceNameConstant.SYSTEM, contextId = "menu", path = "/api/sys/menu", fallbackFactory = FeignMenuFallbackFactory.class)
public interface ApiMenu {

    /**
     * 获取菜单信息
     *
     * @param id 菜单id
     * @return com.carlos.voice.common.dto.sys.MenuDTO
     * @author yunjin
     * @date 2022/1/6 14:52
     */
    @GetMapping(value = "/")
    Result<MenuAO> getMenuById(@RequestParam("id") String id);

    /**
     * 批量获取菜单信息
     *
     * @param ids 参数0
     * @return com.carlos.core.response.Result<java.util.List < com.carlos.system.pojo.ao.MenuAO>>
     * @author Carlos
     * @date 2023/7/3 23:19
     */
    @PostMapping(value = "list")
    Result<List<MenuAO>> listMenus(@RequestBody Set<String> ids);


    /**
     * 获取所有菜单
     *
     * @return com.carlos.core.response.Result<java.util.List < com.carlos.system.pojo.ao.MenuAO>>
     * @author Carlos
     * @date 2023/7/4 9:04
     */
    @GetMapping(value = "all")
    Result<List<MenuAO>> allMenu();

    /**
     * 获取某个类型所有菜单
     *
     * @return com.carlos.core.response.Result<java.util.List < com.carlos.system.pojo.ao.MenuAO>>
     * @author Carlos
     * @date 2023/7/4 9:04
     */
    @GetMapping(value = "type")
    Result<List<MenuAO>> listMenuByType(MenuType menuType);
}
