package com.carlos.system.menu.service;

import com.carlos.system.menu.manager.MenuOperateManager;
import com.carlos.system.menu.pojo.dto.MenuOperateDTO;
import com.carlos.system.menu.pojo.param.MenuOperateSearchParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 菜单操作 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuOperateService {

    private final MenuOperateManager menuOperateManager;

    public void addMenuOperate(MenuOperateDTO dto) {
        boolean success = menuOperateManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteMenuOperate(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = menuOperateManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateMenuOperate(MenuOperateDTO dto) {
        boolean success = menuOperateManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }


    /**
     * @Title: listByKeyword
     * @Description: 根据关键字查询
     * @Date: 2023/7/7 15:34
     * @Parameters: [param]
     * @Return java.util.List<com.carlos.system.menu.pojo.dto.MenuOperateDTO>
     */
    public List<MenuOperateDTO> listByKeyword(MenuOperateSearchParam param) {
        return menuOperateManager.listByKeyword(param.getKeyword());
    }

    /**
     * @Title: listByIds
     * @Description: 根据ids获取列表
     * @Date: 2023/7/7 15:43
     * @Parameters: [ids]
     * @Return java.util.List<com.carlos.system.menu.pojo.dto.MenuOperateDTO>
     */
    public List<MenuOperateDTO> listByIds(Set<String> ids) {
        return menuOperateManager.listById(ids);
    }

    /**
     * @Title: listByMenuId
     * @Description: 根据menuId获取
     * @Date: 2023/7/10 10:11
     * @Parameters: [menuId]
     * @Return java.util.List<com.carlos.system.menu.pojo.dto.MenuOperateDTO>
     */
    public List<MenuOperateDTO> listByMenuId(String menuId) {
        return menuOperateManager.listByMenuId(menuId);
    }

    /**
     * @Title: listByMenuIds
     * @Description: 根据menuIds获取
     * @Date: 2023/7/10 10:25
     * @Parameters: [menuIds]
     * @Return java.util.List<com.carlos.system.menu.pojo.dto.MenuOperateDTO>
     */
    public List<MenuOperateDTO> listByMenuIds(Set<String> menuIds) {
        return menuOperateManager.listByMenuIds(menuIds);
    }
}
