package com.carlos.org.service;

import com.carlos.org.manager.RoleOperateManager;
import com.carlos.org.pojo.dto.RoleOperateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 角色菜单操作表 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleOperateService {

    private final RoleOperateManager roleOperateManager;

    public void addRoleOperate(RoleOperateDTO dto) {
        boolean success = roleOperateManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteRoleOperate(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = roleOperateManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateRoleOperate(RoleOperateDTO dto) {
        boolean success = roleOperateManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

}
