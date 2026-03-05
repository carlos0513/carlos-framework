package com.carlos.audit.service;

import com.carlos.audit.manager.AuditLogDataChangeManager;
import com.carlos.audit.pojo.dto.AuditLogDataChangeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 审计日志-数据变更详情 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogDataChangeService {

    private final AuditLogDataChangeManager logDataChangeManager;

    /**
     * 新增审计日志-数据变更详情
     *
     * @param dto 审计日志-数据变更详情数据
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void addAuditLogDataChange(AuditLogDataChangeDTO dto) {
        boolean success = logDataChangeManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'AuditLogDataChange' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除审计日志-数据变更详情
     *
     * @param ids 审计日志-数据变更详情id
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void deleteAuditLogDataChange(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = logDataChangeManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改审计日志-数据变更详情信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void updateAuditLogDataChange(AuditLogDataChangeDTO dto) {
        boolean success = logDataChangeManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'AuditLogDataChange' data: id:{}", dto.getId());
    }

}
