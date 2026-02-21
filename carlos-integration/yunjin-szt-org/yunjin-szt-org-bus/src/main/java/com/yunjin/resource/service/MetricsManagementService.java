package com.yunjin.resource.service;

import com.yunjin.resource.manager.MetricsManagementManager;
import com.yunjin.resource.pojo.dto.MetricsManagementDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 系统指标管理 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsManagementService {

    private final MetricsManagementManager metricsManagementManager;

    public void addMetricsManagement(MetricsManagementDTO dto) {
        boolean success = metricsManagementManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteMetricsManagement(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = metricsManagementManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateMetricsManagement(MetricsManagementDTO dto) {
        boolean success = metricsManagementManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

}
