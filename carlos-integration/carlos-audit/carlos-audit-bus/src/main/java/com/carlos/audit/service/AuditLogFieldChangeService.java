package com.carlos.audit.service;

import com.carlos.audit.manager.AuditLogFieldChangeManager;
import com.carlos.audit.pojo.dto.AuditLogFieldChangeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 审计日志-字段级变更明细 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogFieldChangeService {

    private final AuditLogFieldChangeManager logFieldChangeManager;

    /**
     * 新增审计日志-字段级变更明细
     *
     * @param dto 审计日志-字段级变更明细数据
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void addAuditLogFieldChange(AuditLogFieldChangeDTO dto) {
        boolean success = logFieldChangeManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'AuditLogFieldChange' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除审计日志-字段级变更明细
     *
     * @param ids 审计日志-字段级变更明细id
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void deleteAuditLogFieldChange(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = logFieldChangeManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改审计日志-字段级变更明细信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void updateAuditLogFieldChange(AuditLogFieldChangeDTO dto) {
        boolean success = logFieldChangeManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'AuditLogFieldChange' data: id:{}", dto.getId());
    }

}
