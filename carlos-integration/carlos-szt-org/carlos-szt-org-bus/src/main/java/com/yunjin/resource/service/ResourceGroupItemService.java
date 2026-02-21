package com.yunjin.resource.service;

import com.yunjin.resource.manager.ResourceGroupItemManager;
import com.yunjin.resource.pojo.dto.ResourceGroupItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 资源组详情项 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceGroupItemService {

    private final ResourceGroupItemManager resourceGroupItemManager;

    public void addResourceGroupItem(ResourceGroupItemDTO dto) {
        boolean success = resourceGroupItemManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteResourceGroupItem(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = resourceGroupItemManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateResourceGroupItem(ResourceGroupItemDTO dto) {
        boolean success = resourceGroupItemManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }


    public List<ResourceGroupItemDTO> listByGroupIds(Set<String> resourceGroupIds) {
        return resourceGroupItemManager.listByGroupIds(resourceGroupIds);
    }
}
