package com.carlos.audit.service;

import com.carlos.audit.manager.AuditLogMainManager;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 审计日志主表 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogMainService {

    private final AuditLogMainManager logMainManager;

    /**
     * 新增审计日志主表
     *
     * @param dto 审计日志主表数据
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void addAuditLogMain(AuditLogMainDTO dto) {
        boolean success = logMainManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'AuditLogMain' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除审计日志主表
     *
     * @param ids 审计日志主表id
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void deleteAuditLogMain(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = logMainManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改审计日志主表信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void updateAuditLogMain(AuditLogMainDTO dto) {
        boolean success = logMainManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'AuditLogMain' data: id:{}", dto.getId());
    }

}
