package com.carlos.org.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.base.BaseManager;
import com.carlos.org.pojo.entity.OrgUserRole;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色关联 Manager
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Component
public class OrgUserRoleManager extends BaseManager<OrgUserRole> {

    /**
     * 根据用户ID查询角色列表
     */
    public List<OrgUserRole> getByUserId(Long userId) {
        LambdaQueryWrapper<OrgUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUserRole::getUserId, userId);
        return list(wrapper);
    }

    /**
     * 批量保存用户角色关联
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoles(Long userId, List<Long> roleIds) {
        // 先删除原有关系
        deleteByUserId(userId);

        // 保存新关系
        if (roleIds != null && !roleIds.isEmpty()) {
            List<OrgUserRole> entities = roleIds.stream()
                .map(roleId -> {
                    OrgUserRole entity = new OrgUserRole();
                    entity.setUserId(userId);
                    entity.setRoleId(roleId);
                    return entity;
                })
                .collect(Collectors.toList());
            saveBatch(entities);
        }
    }

    /**
     * 根据用户ID删除关联
     */
    public void deleteByUserId(Long userId) {
        LambdaQueryWrapper<OrgUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUserRole::getUserId, userId);
        remove(wrapper);
    }
}
