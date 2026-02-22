package com.carlos.org.service;

import com.carlos.org.pojo.dto.OrgUserMenuDTO;
import com.carlos.org.pojo.emuns.UserMenuCollectionTypeEnum;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户菜单收藏表 业务接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-2-28 11:10:01
 */
public interface OrgUserMenuService{

    /**
     * 新增用户菜单收藏表
     *
     * @param dto 用户菜单收藏表数据
     * @author  yunjin
     * @date    2024-2-28 11:10:01
     */
    void addOrgUserMenu(OrgUserMenuDTO dto);

    /**
     * 删除用户菜单收藏表
     *
     * @param ids 用户菜单收藏表id
     * @author  yunjin
     * @date    2024-2-28 11:10:01
     */
    void deleteOrgUserMenu(Set<String> ids);

    /**
     * 修改用户菜单收藏表信息
     *
     * @param dto 对象信息
     * @author  yunjin
     * @date    2024-2-28 11:10:01
     */
    void updateOrgUserMenu(OrgUserMenuDTO dto);

    List<OrgUserMenuDTO> getMenusByUserId(String userId);

    UserMenuCollectionTypeEnum click(OrgUserMenuDTO dto);

    UserMenuCollectionTypeEnum favorite(OrgUserMenuDTO dto);
}
