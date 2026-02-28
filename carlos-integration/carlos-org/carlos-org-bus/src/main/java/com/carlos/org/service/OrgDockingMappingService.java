package com.carlos.org.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.org.manager.OrgDockingMappingManager;
import com.carlos.org.pojo.dto.OrgDockingMappingDTO;
import com.carlos.org.pojo.emuns.OrgDockingTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 用户信息对接关联表 业务接口实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-2-27 15:41:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgDockingMappingService {

    private final OrgDockingMappingManager dockingMappingManager;

    public void addOrgDockingMapping(OrgDockingMappingDTO dto) {
        boolean success = dockingMappingManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteOrgDockingMapping(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = dockingMappingManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }


    /**
     * 获取映射信息
     *
     * @param type       对接实体类型
     * @param targetCode 目标编码
     * @param targetId   目标数据id
     * @return com.carlos.org.pojo.dto.OrgDockingMappingDTO
     * @author Carlos
     * @date 2025-02-28 11:31
     */
    public OrgDockingMappingDTO getDockingMapping(OrgDockingTypeEnum type, String targetCode, String targetId) {
        if (type == null) {
            throw new ServiceException("OrgDockingType can't be null！");
        }
        if (StrUtil.isBlank(targetId)) {
            throw new ServiceException("targetId can't be null！");
        }
        if (StrUtil.isBlank(targetCode)) {
            throw new ServiceException("targetCode can't be null！");
        }
        OrgDockingMappingDTO dockingMapping = dockingMappingManager.getDockingMapping(type, targetCode, targetId);
        return dockingMapping;
    }

    /**
     * 获取映射信息
     * @param targetId   目标数据id
     * @return com.carlos.org.pojo.dto.OrgDockingMappingDTO
     * @author Carlos
     * @date 2025-02-28 11:31
     */
    public OrgDockingMappingDTO getDockingMappingByTargetId(String targetId) {
        if (StrUtil.isBlank(targetId)) {
            throw new ServiceException("targetId can't be null！");
        }
        OrgDockingMappingDTO dockingMapping = dockingMappingManager.getDockingMappingByTargetId(targetId);
        return dockingMapping;
    }

    public List<OrgDockingMappingDTO> listByTargetIds(Set<String> targetIds) {
        if (CollUtil.isEmpty(targetIds)) {
            throw new ServiceException("目标系统ids为空，用户信息对接关联信息失败");
        }
        return dockingMappingManager.listByTargetIds(targetIds);
    }


    /**
     * @Title: listBySystemIds
     * @Description: 根据系统id获取
     * @Date: 2025/8/20 15:33
     * @Parameters: [aystemIds]
     * @Return java.util.List<com.carlos.org.pojo.dto.OrgDockingMappingDTO>
     */
    public List<OrgDockingMappingDTO> listBySystemIds(Set<String> systemIds) {
        if (CollUtil.isEmpty(systemIds)) {
            throw new ServiceException("系统ids为空，用户信息对接关联信息失败");
        }
        return dockingMappingManager.listBySystemIds(systemIds);
    }
}
