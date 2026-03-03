package com.carlos.org.position.service;

import com.carlos.org.position.manager.OrgPositionManager;
import com.carlos.org.position.pojo.dto.OrgPositionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 岗位表 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgPositionService {

    private final OrgPositionManager positionManager;

    /**
     * 新增岗位表
     *
     * @param dto 岗位表数据
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    public void addOrgPosition(OrgPositionDTO dto) {
        boolean success = positionManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'OrgPosition' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除岗位表
     *
     * @param ids 岗位表id
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    public void deleteOrgPosition(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = positionManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改岗位表信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月3日 下午11:19:10
     */
    public void updateOrgPosition(OrgPositionDTO dto) {
        boolean success = positionManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'OrgPosition' data: id:{}", dto.getId());
    }

}
