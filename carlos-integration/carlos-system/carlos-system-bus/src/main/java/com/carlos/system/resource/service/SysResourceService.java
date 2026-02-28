package com.carlos.system.resource.service;

import com.carlos.core.exception.ServiceException;
import com.carlos.system.dict.SysResourceMethod;
import com.carlos.system.dict.SysResourceType;
import com.carlos.system.pojo.ao.SysResourceAO;
import com.carlos.system.resource.convert.SysResourceConvert;
import com.carlos.system.resource.manager.SysResourceManager;
import com.carlos.system.resource.pojo.dto.SysResourceDTO;
import com.carlos.system.resource.pojo.dto.SysResourceGroupDTO;
import com.carlos.system.resource.pojo.entity.SysResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 系统资源 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysResourceService {

    private final SysResourceManager resourceManager;

    private final SysResourceCategoryService categoryService;

    /**
     * 新增系统资源
     *
     * @param dto 系统资源数据
     * @author carlos
     * @date 2021-12-28 15:26:57
     */
    public void addResource(final SysResourceDTO dto) {
        final Serializable id = resourceManager.getIdByDto(dto);
        if (id != null) {
            log.warn("资源已存在：id:{}", id);
            return;
        }
        // 检查资源是否已经存在
        final boolean success = resourceManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            throw new ServiceException("资源保存失败！");
        }
    }

    /**
     * 删除系统资源
     *
     * @param ids 系统资源id
     * @author carlos
     * @date 2021-12-28 15:26:57
     */
    public void deleteResource(final Set<Serializable> ids) {
        for (final Serializable id : ids) {
            final boolean success = resourceManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }


    /**
     * 修改系统资源信息
     *
     * @param dto 对象信息
     * @author carlos
     * @date 2021-12-28 15:26:57
     */
    public void updateResource(final SysResourceDTO dto) {
        final boolean success = resourceManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作

    }

    /**
     * 获取资源分组
     *
     * @return java.util.List<com.carlos.sys.pojo.dto.ResourceGroupDTO>
     * @author carlos
     * @date 2022/1/13 13:11
     */
    public List<SysResourceGroupDTO> getResourceGroup() {
        final List<SysResource> resources = resourceManager.list();
        final Map<Serializable, List<SysResource>> collect = resources.stream().collect(Collectors.groupingBy(SysResource::getCategoryId));
        final List<SysResourceGroupDTO> list = new LinkedList<>();
        final Set<Map.Entry<Serializable, List<SysResource>>> entries = collect.entrySet();
        for (final Map.Entry<Serializable, List<SysResource>> entry : entries) {
            final Serializable categoryId = entry.getKey();
            final String categoryName = categoryService.getParentName(categoryId);
            list.add(new SysResourceGroupDTO().setName(categoryName).setResources(SysResourceConvert.INSTANCE.toDTO(entry.getValue())));
        }
        return list;
    }


    /**
     * 获取系统资源信息
     *
     * @param id 资源id
     * @return com.carlos.voice.common.dto.sys.SysResource
     * @author carlos
     * @date 2022/1/13 15:06
     */
    public SysResourceAO getSysResource(final String id) {
        final SysResourceDTO resource = resourceManager.getDtoById(id);
        final SysResourceAO sysResource = new SysResourceAO();
        sysResource.setId(resource.getId());
        sysResource.setName(resource.getName());
        sysResource.setPath(resource.getPath());
        sysResource.setPathPrefix(resource.getPathPrefix());
        sysResource.setMethod(SysResourceMethod.ofCode(resource.getMethod()).getName());
        sysResource.setIcon(resource.getIcon());
        sysResource.setType(SysResourceType.ofCode(resource.getType()).getName());
        sysResource.setHidden(resource.getHidden());
        sysResource.setDescription(resource.getDescription());
        return sysResource;
    }


}
