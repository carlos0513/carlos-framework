package com.carlos.org.position.service;

import com.carlos.org.position.manager.OrgUserPositionManager;
import com.carlos.org.position.pojo.dto.OrgUserPositionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 用户岗位职级关联表（核心任职信息） 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgUserPositionService {

    private final OrgUserPositionManager userPositionManager;

    /**
     * 新增用户岗位职级关联表（核心任职信息）
     *
     * @param dto 用户岗位职级关联表（核心任职信息）数据
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    public void addOrgUserPosition(OrgUserPositionDTO dto) {
        boolean success = userPositionManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'OrgUserPosition' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除用户岗位职级关联表（核心任职信息）
     *
     * @param ids 用户岗位职级关联表（核心任职信息）id
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    public void deleteOrgUserPosition(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = userPositionManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改用户岗位职级关联表（核心任职信息）信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    public void updateOrgUserPosition(OrgUserPositionDTO dto) {
        boolean success = userPositionManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'OrgUserPosition' data: id:{}", dto.getId());
    }

}
