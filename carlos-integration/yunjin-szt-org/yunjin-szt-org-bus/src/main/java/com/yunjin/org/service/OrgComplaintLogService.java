package com.yunjin.org.service;


import com.yunjin.org.manager.OrgComplaintLogManager;
import com.yunjin.org.pojo.dto.OrgComplaintLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 投诉建议处理节点日志 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgComplaintLogService {

    private final OrgComplaintLogManager complaintLogManager;

    public void addOrgComplaintLog(OrgComplaintLogDTO dto) {
        boolean success = complaintLogManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteOrgComplaintLog(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = complaintLogManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }


}
