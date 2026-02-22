package com.carlos.org.service.impl;

import com.carlos.org.convert.OrgUserMenuConvert;
import com.carlos.org.manager.OrgUserMenuManager;
import com.carlos.org.pojo.dto.OrgUserMenuDTO;
import com.carlos.org.pojo.emuns.UserMenuCollectionTypeEnum;
import com.carlos.org.pojo.entity.OrgUserMenu;
import com.carlos.org.service.OrgUserMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 用户菜单收藏表 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date    2024-2-28 11:10:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgUserMenuServiceImpl implements OrgUserMenuService {

    private final OrgUserMenuManager userMenuManager;

    @Override
    public void addOrgUserMenu(OrgUserMenuDTO dto){
        boolean success = userMenuManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    @Override
    public void deleteOrgUserMenu(Set<String> ids){
        for (Serializable id : ids) {
            boolean success = userMenuManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    @Override
    public void updateOrgUserMenu(OrgUserMenuDTO dto){
        boolean success = userMenuManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

    @Override
    public List<OrgUserMenuDTO> getMenusByUserId(String userId) {
        List<OrgUserMenu> list = userMenuManager.lambdaQuery().eq(OrgUserMenu::getUserId, userId)
                .eq(OrgUserMenu::getStatus, UserMenuCollectionTypeEnum.MARKED).list();
        return OrgUserMenuConvert.INSTANCE.toDTO(list);
    }

    @Override
    public UserMenuCollectionTypeEnum click(OrgUserMenuDTO dto) {
        List<OrgUserMenu> list = userMenuManager.lambdaQuery()
                .eq(OrgUserMenu::getUserId, dto.getUserId())
                .eq(OrgUserMenu::getMenuId, dto.getMenuId())
                .list();
        if(list == null || list.isEmpty()){
            userMenuManager.add(dto.setStatus(UserMenuCollectionTypeEnum.MARKED));
            return UserMenuCollectionTypeEnum.MARKED;
        }else {
            if(list.get(0).getStatus() == UserMenuCollectionTypeEnum.MARKED){
                userMenuManager.modify(dto.setId(list.get(0).getId()).setStatus(UserMenuCollectionTypeEnum.UNMARKED));
                return UserMenuCollectionTypeEnum.UNMARKED;
            }else{
                userMenuManager.modify(dto.setId(list.get(0).getId()).setStatus(UserMenuCollectionTypeEnum.MARKED));
                return UserMenuCollectionTypeEnum.MARKED;
            }
        }
    }

    @Override
    public UserMenuCollectionTypeEnum favorite(OrgUserMenuDTO dto) {
        List<OrgUserMenu> list = userMenuManager.lambdaQuery()
                .eq(OrgUserMenu::getUserId, dto.getUserId())
                .eq(OrgUserMenu::getMenuId, dto.getMenuId()).list();
        if(list ==null || list.isEmpty()){
            return UserMenuCollectionTypeEnum.UNMARKED;
        }
        return  list.get(0).getStatus();
    }
}
