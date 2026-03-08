package com.carlos.org.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.carlos.org.manager.OrgUserPositionManager;
import com.carlos.org.pojo.entity.OrgUserPosition;
import com.carlos.org.pojo.param.OrgUserAssignPositionParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * <p>
 * 用户岗位 业务类
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgUserPositionService {

    private final OrgUserPositionManager userPositionManager;

    /**
     * 用户分配岗位
     * 实现UM-014需求
     *
     * @param param 分配参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignPosition(OrgUserAssignPositionParam param) {
        // 检查用户是否已存在该岗位
        OrgUserPosition exist = userPositionManager.getOne(new LambdaQueryWrapper<OrgUserPosition>()
            .eq(OrgUserPosition::getUserId, param.getUserId())
            .eq(OrgUserPosition::getPositionId, param.getPositionId())
            .eq(OrgUserPosition::getDepartmentId, param.getDepartmentId())
            .eq(OrgUserPosition::getDeleted, false));

        if (exist != null) {
            throw new RuntimeException("用户已分配该岗位，不能重复分配");
        }

        // 如果设置为主岗位，需要将其他岗位设为非主岗位
        if (Boolean.TRUE.equals(param.getIsMain())) {
            userPositionManager.update(new LambdaUpdateWrapper<OrgUserPosition>()
                .eq(OrgUserPosition::getUserId, param.getUserId())
                .eq(OrgUserPosition::getIsMain, true)
                .set(OrgUserPosition::getIsMain, false));
        }

        // 创建用户岗位关联
        OrgUserPosition userPosition = new OrgUserPosition();
        userPosition.setUserId(Long.valueOf(param.getUserId()));
        userPosition.setPositionId(Long.valueOf(param.getPositionId()));
        userPosition.setLevelId(Long.valueOf(param.getLevelId()));
        userPosition.setDepartmentId(Long.valueOf(param.getDepartmentId()));
        userPosition.setIsMain(param.getIsMain());
        userPosition.setPositionStatus(1); // 在职
        userPosition.setAppointDate(param.getAppointDate() != null ? param.getAppointDate() : LocalDate.now());

        boolean success = userPositionManager.save(userPosition);
        if (!success) {
            throw new RuntimeException("分配岗位失败");
        }

        log.info("用户分配岗位成功：userId={}, positionId={}", param.getUserId(), param.getPositionId());
    }

    /**
     * 用户卸任岗位
     *
     * @param userId     用户ID
     * @param positionId 岗位ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void resignPosition(String userId, String positionId) {
        boolean success = userPositionManager.update(new LambdaUpdateWrapper<OrgUserPosition>()
            .eq(OrgUserPosition::getUserId, userId)
            .eq(OrgUserPosition::getPositionId, positionId)
            .set(OrgUserPosition::getPositionStatus, 5) // 已卸任
            .set(OrgUserPosition::getDimissionDate, LocalDate.now()));

        if (!success) {
            throw new RuntimeException("卸任岗位失败");
        }

        log.info("用户卸任岗位成功：userId={}, positionId={}", userId, positionId);
    }

}
