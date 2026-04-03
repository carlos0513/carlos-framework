package com.carlos.system.resource.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.constant.CoreConstant;
import com.carlos.core.exception.BusinessException;
import com.carlos.system.resource.manager.SysResourceCategoryManager;
import com.carlos.system.resource.pojo.dto.ResourceCategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 资源分类 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2022-1-5 17:23:27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysResourceCategoryService {

    private final SysResourceCategoryManager resourceCategoryManager;

    /**
     * 新增资源分类
     *
     * @param dto 资源分类数据
     * @return boolean
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    public void addResourceCategory(final ResourceCategoryDTO dto) {
        // 检查名称资源类型是否已经存在
        final Serializable id = resourceCategoryManager.getIdByName(dto.getParentId(), dto.getName());
        if (id != null) {
            dto.setId(Convert.toLong(id));
            log.warn("资源已存在：  parentId：{}  name:{}", dto.getParentId(), dto.getName());
            return;
        }
        final boolean success = resourceCategoryManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            throw new BusinessException("资源保存失败！");
        }
    }

    /**
     * 删除资源分类
     *
     * @param ids 资源分类id
     * @return boolean
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    public void deleteResourceCategory(final Set<Serializable> ids) {
        for (final Serializable id : ids) {
            final boolean success = resourceCategoryManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改资源分类信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    public void updateResourceCategory(final ResourceCategoryDTO dto) {
        final boolean success = resourceCategoryManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

    /**
     * 获取所有父级名称 分级之间用‘/’分割
     *
     * @param categoryId 分类id
     * @return java.lang.String
     * @author carlos
     * @date 2022/1/13 13:47
     */
    public String getParentName(final Serializable categoryId) {
        final ResourceCategoryDTO dto = resourceCategoryManager.getDtoById(categoryId);
        if (dto == null) {
            log.error("资源类型不存在: id：{}", categoryId);
            throw new BusinessException("资源类型不存在！");
        }
        if (dto.getParentId().equals(CoreConstant.PARENT_LONG_0)) {
            return dto.getName();
        } else {
            final String parentName = getParentName(dto.getParentId());
            return parentName + StrUtil.SLASH + dto.getName();
        }
    }

}
