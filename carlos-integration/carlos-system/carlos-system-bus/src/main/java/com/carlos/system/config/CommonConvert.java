package com.carlos.system.config;

import cn.hutool.extra.spring.SpringUtil;
import com.carlos.system.menu.manager.MenuManager;
import com.carlos.system.resource.manager.SysResourceCategoryManager;
import org.mapstruct.Named;

import java.io.Serializable;

/**
 * <p>
 * 通用转换器
 * </p>
 *
 * @author yunjin
 * @date 2022-11-8 19:30:24
 */
@Named("CommonConvert")
public class CommonConvert {


    @Named("toMenuName")
    public String toMenuName(final Serializable menuId) {
        if (menuId == null) {
            return null;
        }
        final MenuManager menuManager = SpringUtil.getBean(MenuManager.class);
        return menuManager.getNameById(menuId);
    }

    @Named("toCategoryName")
    public String toCategoryName(final Serializable categoryId) {
        if (categoryId == null) {
            return null;
        }
        final SysResourceCategoryManager categoryManager = SpringUtil.getBean(SysResourceCategoryManager.class);
        return categoryManager.getNameById(categoryId);
    }

    @Named("haveChildrenMenu")
    public Boolean haveChildrenMenu(final Serializable menuId) {
        if (menuId == null) {
            return null;
        }
        final MenuManager menuManager = SpringUtil.getBean(MenuManager.class);
        return menuManager.getSubMenuCount(menuId) > 0L;
    }

    @Named("haveChildrenCategory")
    public Boolean haveChildrenCategory(final Serializable categoryId) {
        if (categoryId == null) {
            return null;
        }
        final SysResourceCategoryManager categoryManager = SpringUtil.getBean(SysResourceCategoryManager.class);
        return categoryManager.existChildren(categoryId);
    }

}
