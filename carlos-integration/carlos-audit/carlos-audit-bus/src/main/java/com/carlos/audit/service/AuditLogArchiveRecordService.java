package com.carlos.audit.service;

import com.carlos.audit.manager.AuditLogArchiveRecordManager;
import com.carlos.audit.pojo.dto.AuditLogArchiveRecordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 审计日志归档记录 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogArchiveRecordService {

    private final AuditLogArchiveRecordManager logArchiveRecordManager;

    /**
     * 新增审计日志归档记录
     *
     * @param dto 审计日志归档记录数据
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void addAuditLogArchiveRecord(AuditLogArchiveRecordDTO dto) {
        boolean success = logArchiveRecordManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'AuditLogArchiveRecord' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除审计日志归档记录
     *
     * @param ids 审计日志归档记录id
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void deleteAuditLogArchiveRecord(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = logArchiveRecordManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改审计日志归档记录信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void updateAuditLogArchiveRecord(AuditLogArchiveRecordDTO dto) {
        boolean success = logArchiveRecordManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'AuditLogArchiveRecord' data: id:{}", dto.getId());
    }

}
