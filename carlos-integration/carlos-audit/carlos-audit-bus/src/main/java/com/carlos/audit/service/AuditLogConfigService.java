package com.carlos.audit.service;

import com.carlos.audit.manager.AuditLogConfigManager;
import com.carlos.audit.pojo.dto.AuditLogConfigDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 审计日志配置（动态TTL与采样策略） 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogConfigService {

    private final AuditLogConfigManager logConfigManager;

    /**
     * 新增审计日志配置（动态TTL与采样策略）
     *
     * @param dto 审计日志配置（动态TTL与采样策略）数据
     * @author Carlos
     * @date 2026年3月6日 下午9:31:12
     */
    public void addAuditLogConfig(AuditLogConfigDTO dto) {
        boolean success = logConfigManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'AuditLogConfig' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除审计日志配置（动态TTL与采样策略）
     *
     * @param ids 审计日志配置（动态TTL与采样策略）id
     * @author Carlos
     * @date 2026年3月6日 下午9:31:12
     */
    public void deleteAuditLogConfig(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = logConfigManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改审计日志配置（动态TTL与采样策略）信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年3月6日 下午9:31:12
     */
    public void updateAuditLogConfig(AuditLogConfigDTO dto) {
        boolean success = logConfigManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'AuditLogConfig' data: id:{}", dto.getId());
    }

}
