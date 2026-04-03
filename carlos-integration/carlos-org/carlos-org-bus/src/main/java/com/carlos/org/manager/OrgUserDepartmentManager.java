package com.carlos.org.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.carlos.core.base.BaseManager;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.entity.OrgUserDepartment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户部门关联 Manager
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Component
public class OrgUserDepartmentManager extends BaseManager<OrgUserDepartment> {

    /**
     * 根据用户ID查询部门列表
     */
    public List<OrgUserDepartment> getByUserId(Long userId) {
        LambdaQueryWrapper<OrgUserDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUserDepartment::getUserId, userId);
        return list(wrapper);
    }

    /**
     * 批量保存用户部门关联
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveUserDepartments(Long userId, List<UserDepartmentDTO> departments) {
        // 先删除原有关系
        deleteByUserId(userId);

        // 保存新关系
        if (departments != null && !departments.isEmpty()) {
            List<OrgUserDepartment> entities = departments.stream()
                .map(dto -> {
                    OrgUserDepartment entity = new OrgUserDepartment();
                    entity.setUserId(userId);
                    entity.setDepartmentId(dto.getDepartmentId());
                    entity.setIsMain(dto.getMain() != null && dto.getMain() ? 1 : 0);
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
        LambdaQueryWrapper<OrgUserDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUserDepartment::getUserId, userId);
        remove(wrapper);
    }

    /**
     * 获取用户主部门ID
     */
    public Long getMainDepartmentId(Long userId) {
        LambdaQueryWrapper<OrgUserDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUserDepartment::getUserId, userId)
               .eq(OrgUserDepartment::getIsMain, 1)
               .last("LIMIT 1");
        OrgUserDepartment entity = getOne(wrapper);
        return entity != null ? entity.getDepartmentId() : null;
    }
}
