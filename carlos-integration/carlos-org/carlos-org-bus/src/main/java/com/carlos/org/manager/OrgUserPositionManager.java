package com.carlos.org.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.base.BaseManager;
import com.carlos.org.pojo.dto.UserPositionDTO;
import com.carlos.org.pojo.entity.OrgUserPosition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户岗位关联 Manager
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Component
public class OrgUserPositionManager extends BaseManager<OrgUserPosition> {

    /**
     * 根据用户ID查询岗位列表
     */
    public List<OrgUserPosition> getByUserId(Long userId) {
        LambdaQueryWrapper<OrgUserPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUserPosition::getUserId, userId);
        return list(wrapper);
    }

    /**
     * 批量保存用户岗位关联
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveUserPositions(Long userId, List<UserPositionDTO> positions) {
        // 先删除原有关系
        deleteByUserId(userId);

        // 保存新关系
        if (positions != null && !positions.isEmpty()) {
            List<OrgUserPosition> entities = positions.stream()
                .map(dto -> {
                    OrgUserPosition entity = new OrgUserPosition();
                    entity.setUserId(userId);
                    entity.setPositionId(dto.getPositionId());
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
        LambdaQueryWrapper<OrgUserPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUserPosition::getUserId, userId);
        remove(wrapper);
    }

    /**
     * 获取用户主岗位ID
     */
    public Long getMainPositionId(Long userId) {
        LambdaQueryWrapper<OrgUserPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgUserPosition::getUserId, userId)
               .eq(OrgUserPosition::getIsMain, 1)
               .last("LIMIT 1");
        OrgUserPosition entity = getOne(wrapper);
        return entity != null ? entity.getPositionId() : null;
    }
}
