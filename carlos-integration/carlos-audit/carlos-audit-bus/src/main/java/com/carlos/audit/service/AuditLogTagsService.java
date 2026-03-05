package com.carlos.audit.service;

import com.carlos.audit.manager.AuditLogTagsManager;
import com.carlos.audit.pojo.dto.AuditLogTagsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 审计日志-动态标签 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogTagsService {

    private final AuditLogTagsManager logTagsManager;

    /**
     * 新增审计日志-动态标签
     *
     * @param dto 审计日志-动态标签数据
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void addAuditLogTags(AuditLogTagsDTO dto) {
        boolean success = logTagsManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'AuditLogTags' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除审计日志-动态标签
     *
     * @param ids 审计日志-动态标签id
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void deleteAuditLogTags(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = logTagsManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改审计日志-动态标签信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月5日 下午11:36:54
     */
    public void updateAuditLogTags(AuditLogTagsDTO dto) {
        boolean success = logTagsManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'AuditLogTags' data: id:{}", dto.getId());
    }

}
