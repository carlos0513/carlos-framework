package com.carlos.system.menu.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.constant.CoreConstant;
import com.carlos.core.exception.BusinessException;
import com.carlos.system.enums.MenuType;
import com.carlos.system.menu.manager.MenuManager;
import com.carlos.system.menu.pojo.dto.MenuDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * <p>
 * 系统菜单 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuManager menuManager;

    /**
     * 新增系统菜单
     *
     * @param dto 系统菜单数据
     * @author carlos
     * @date 2021-12-28 15:26:57
     */

    public void addMenu(MenuDTO dto) {
        if (ObjectUtil.isEmpty(dto.getParentId())) {
            dto.setParentId(CoreConstant.PARENT_LONG_0);
        }
        Optional.ofNullable(dto.getMeta()).ifPresent(i -> {
            if (StrUtil.isBlank(i)) {
                dto.setMeta(null);
            }
        });

        // 检查部门code是否重复
        // 获取菜单级数
        dto.setLevel(getMenuLevel(dto.getParentId()));
        boolean success = menuManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }


    /**
     * 删除系统菜单
     *
     * @param ids 系统菜单id
     * @author carlos
     * @date 2021-12-28 15:26:57
     */

    public void deleteMenu(Set<Serializable> ids) {
        // 检查菜单下是否有资源和其他菜单
        for (Serializable id : ids) {
            checkSubMenus(id);
            // 只能删除已启用的功能菜单
            MenuDTO menu = menuManager.getDtoById(id);
            if (menu.getState()) {
                throw new BusinessException("已启用的菜单无法删除");
            }
            boolean success = menuManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改系统菜单信息
     *
     * @param dto 对象信息
     * @author carlos
     * @date 2021-12-28 15:26:57
     */

    public void updateMenu(MenuDTO dto) {
        if (ObjectUtil.isEmpty(dto.getParentId())) {
            dto.setParentId(CoreConstant.PARENT_LONG_0);
        }
        Optional.ofNullable(dto.getMeta()).ifPresent(i -> {
            if (StrUtil.isBlank(i)) {
                dto.setMeta(null);
            }
        });
        boolean success = menuManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

    /**
     * 获取菜单级数
     *
     * @param parentId 父id
     * @return java.lang.Integer
     * @author carlos
     * @date 2021/12/28 16:08
     */

    public Integer getMenuLevel(Serializable parentId) {

        if (parentId == null || "0".equals(parentId)) {
            return 1;
        }
        MenuDTO dto = menuManager.getDtoById(parentId);
        if (dto == null) {
            log.error("Not found menu data, id:{}", parentId);
            throw new BusinessException("数据不存在！");
        }
        return dto.getLevel() + 1;
    }

    /**
     * 检查菜单下的直接子菜单是否为空
     *
     * @param id 菜单id
     * @author carlos
     * @date 2021/12/28 17:10
     */
    private void checkSubMenus(Serializable id) {
        if (menuManager.getSubMenuCount(id) > 0) {
            throw new BusinessException("菜单下存在其他菜单！");
        }
    }

    /**
     * @Title: getByIds
     * @Description: 根据ids获取菜单列表
     * @Date: 2023/3/31 15:58
     * @Parameters: [ids]
     * @Return java.util.List<com.carlos.system.pojo.dto.MenuDTO>
     */

    public List<MenuDTO> getByIds(Set<String> ids) {
        return menuManager.getDtoByIds(ids);
    }

    /**
     * 获取所有的菜单
     *
     * @return java.util.List<com.carlos.system.pojo.dto.MenuDTO>
     * @author Carlos
     * @date 2023/5/25 18:29
     */

    public List<MenuDTO> all() {
        return menuManager.listAll();
    }

    public List<MenuDTO> listByType(MenuType menuType) {
        return menuManager.listByType(menuType);
    }
}
