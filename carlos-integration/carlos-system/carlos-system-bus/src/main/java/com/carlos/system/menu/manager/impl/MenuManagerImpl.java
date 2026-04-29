package com.carlos.system.menu.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.system.enums.MenuType;
import com.carlos.system.menu.convert.MenuConvert;
import com.carlos.system.menu.manager.MenuManager;
import com.carlos.system.menu.mapper.MenuMapper;
import com.carlos.system.menu.pojo.dto.MenuDTO;
import com.carlos.system.menu.pojo.entity.Menu;
import com.carlos.system.menu.pojo.param.MenuPageParam;
import com.carlos.system.menu.pojo.vo.MenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单 查询封装实现�?
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MenuManagerImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuManager {

    @Override
    public boolean add(MenuDTO dto) {
        // 获取菜单级数
        Menu entity = MenuConvert.INSTANCE.toDO(dto);
        boolean success = this.save(entity);
        if (!success) {
            log.warn("Insert menu data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        log.debug("Insert menu data: id:{}", entity.getId());
        return true;
    }


    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = this.removeById(id);
        if (!success) {
            log.warn("Remove menu data fail, id:{}", id);
            return false;
        }
        log.debug("Remove menu data by id:{}", id);
        return true;

    }

    @Override
    public boolean modify(MenuDTO dto) {
        Menu entity = MenuConvert.INSTANCE.toDO(dto);
        boolean success = this.updateById(entity);
        if (!success) {
            log.warn("Update menu data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        log.debug("Update menu data by id:{}", dto.getId());
        return true;
    }

    @Override
    public MenuDTO getDtoById(Serializable id) {
        Menu entity = this.lambdaQuery().select(Menu::getId, Menu::getParentId, Menu::getTitle, Menu::getPath, Menu::getName, Menu::getIcon, Menu::getUrl, Menu::getState, Menu::getRemark, Menu::getSort, Menu::getMeta, Menu::getComponent, Menu::getLevel, Menu::getHidden, Menu::getMenuType).eq(Menu::getId, id).one();
        return MenuConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MenuVO> getPage(MenuPageParam param) {
        LambdaQueryWrapper<Menu> wrapper = this.queryWrapper();
        // wrapper.orderByAsc(Menu::getSort);
        wrapper.orderByDesc(Menu::getCreateTime);
        wrapper.select(Menu::getId, Menu::getParentId, Menu::getTitle, Menu::getPath, Menu::getName, Menu::getIcon, Menu::getMeta, Menu::getComponent, Menu::getLevel, Menu::getSort, Menu::getHidden, Menu::getCreateTime, Menu::getUpdateTime, Menu::getMenuType).eq(Menu::getParentId, param.getParentId() == null ? "0" : param.getParentId()).eq(Menu::getMenuType, param.getMenuType());
        if (StringUtils.isNotBlank(param.getKeyword())) {
            wrapper.like(Menu::getTitle, param.getKeyword()).or().like(Menu::getName, param.getKeyword());
        }
        if (param.getHidden() != null) {
            wrapper.eq(Menu::getHidden, param.getHidden());
        }
        if (param.getStart() != null) {
            wrapper.gt(Menu::getCreateTime, param.getStart());
        }
        if (param.getEnd() != null) {
            wrapper.lt(Menu::getCreateTime, param.getEnd());
        }
        wrapper.orderByAsc(Menu::getParentId);
        PageInfo<Menu> page = this.page(this.pageInfo(param), wrapper);
        return MybatisPage.convert(page, MenuConvert.INSTANCE::toVO);
    }

    @Override
    public List<MenuDTO> getMenusByParentId(Serializable parentId, boolean detail, MenuType menuType) {
        LambdaQueryWrapper<Menu> wrapper = this.queryWrapper()
            .eq(Menu::getParentId, parentId == null ? "0" : parentId)
            .eq(menuType != null, Menu::getMenuType, menuType)
            .orderByAsc(Menu::getSort)
            .orderByDesc(Menu::getCreateTime);
        if (detail) {
            wrapper.select(Menu::getId,
                Menu::getParentId,
                Menu::getTitle,
                Menu::getPath,
                Menu::getName,
                Menu::getIcon,
                Menu::getMeta,
                Menu::getComponent,
                Menu::getLevel,
                Menu::getSort,
                Menu::getState,
                Menu::getUrl,
                Menu::getHidden,
                Menu::getCreateTime,
                Menu::getUpdateTime,
                Menu::getMenuType);
        } else {
            wrapper.select(Menu::getId, Menu::getParentId, Menu::getTitle, Menu::getMenuType);
        }
        List<Menu> menus = this.list(wrapper);
        return MenuConvert.INSTANCE.toDTO(menus);
    }

    @Override
    public long getSubMenuCount(Serializable id) {
        if (id == null) {
            return 0;
        }
        return this.count(this.queryWrapper().eq(Menu::getParentId, id));
    }

    @Override
    public List<MenuDTO> getMenuTree(Serializable menuId, boolean detail, MenuType menuType) {
        // 获取一级菜单 根据顺序排序
        List<MenuDTO> menus = this.getMenusByParentId(menuId, detail, menuType);
        for (MenuDTO menu : menus) {
            Serializable id = menu.getId();
            List<MenuDTO> children = this.getMenuTree(id, detail, menuType);
            menu.setChildren(children);
        }
        return menus;
    }

    @Override
    public String getNameById(Serializable menuId) {
        LambdaQueryWrapper<Menu> wrapper = this.queryWrapper().eq(Menu::getId, menuId).select(Menu::getName);
        Menu entity = this.getOne(wrapper);
        if (entity == null) {
            return null;
        }
        return entity.getName();
    }

    @Override
    public List<MenuDTO> listByTitle(String title, MenuType menuType) {
        LambdaQueryWrapper<Menu> wrapper = this.queryWrapper().eq(Menu::getMenuType, menuType).like(StringUtils.isNotBlank(title), Menu::getTitle, title).orderByAsc(Menu::getSort).orderByDesc(Menu::getCreateTime);
        wrapper.select(Menu::getId, Menu::getParentId, Menu::getTitle, Menu::getPath, Menu::getName, Menu::getIcon, Menu::getMeta, Menu::getComponent, Menu::getLevel, Menu::getSort, Menu::getState, Menu::getUrl, Menu::getHidden, Menu::getCreateTime, Menu::getUpdateTime);

        List<Menu> menus = this.list(wrapper);
        return MenuConvert.INSTANCE.toDTO(menus);
    }

    @Override
    public List<MenuDTO> getDtoByIds(Set<String> ids) {
        List<Menu> list = this.lambdaQuery().in(Menu::getId, ids).list();
        return MenuConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<MenuDTO> listAll() {
        List<Menu> list = list();
        return MenuConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<MenuDTO> listByType(MenuType menuType) {
        List<Menu> list = this.lambdaQuery().eq(menuType != null, Menu::getMenuType, menuType).list();
        return MenuConvert.INSTANCE.toDTO(list);

    }
}
