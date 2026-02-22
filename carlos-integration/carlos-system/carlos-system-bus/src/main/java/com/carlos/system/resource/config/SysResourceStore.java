package com.carlos.system.resource.config;

import com.carlos.boot.resource.ResourceStore;
import com.carlos.boot.resource.bean.ApplicationResource;
import com.carlos.boot.resource.bean.Resource;
import com.carlos.boot.resource.bean.ResourceCategory;
import com.carlos.core.dict.DictEnum;
import com.carlos.system.dict.SysResourceMethod;
import com.carlos.system.dict.SysResourceState;
import com.carlos.system.dict.SysResourceType;
import com.carlos.system.resource.pojo.dto.ResourceCategoryDTO;
import com.carlos.system.resource.pojo.dto.SysResourceDTO;
import com.carlos.system.resource.service.SysResourceCategoryService;
import com.carlos.system.resource.service.SysResourceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 系统资源存储自定义实现
 * </p>
 *
 * @author carlos
 * @date 2022/3/17 15:29
 */
@Component
@Slf4j
@AllArgsConstructor
public class SysResourceStore implements ResourceStore {

    private final SysResourceCategoryService categoryService;
    private final SysResourceService resourceService;

    @Override
    public void save(ApplicationResource resource) {
        // 系统名称作为一级类型
        String appName = resource.getAppName();
        ResourceCategoryDTO root = new ResourceCategoryDTO().setName(appName).setParentId("0");
        categoryService.addResourceCategory(root);

        List<ResourceCategory> subCategories = resource.getCategories();
        for (ResourceCategory sub : subCategories) {
            ResourceCategoryDTO subCategory = new ResourceCategoryDTO().setName(sub.getName()).setParentId(root.getId());
            categoryService.addResourceCategory(subCategory);
            String categoryId = subCategory.getId();
            List<Resource> resources = sub.getResources();
            for (Resource item : resources) {
                SysResourceDTO dto = new SysResourceDTO();
                dto.setCategoryId(categoryId);
                dto.setName(item.getName());
                dto.setPathPrefix(item.getPathPrefix());
                dto.setPath(item.getPath());
                DictEnum dict = SysResourceMethod.nameOf(item.getMethod().name());
                if (dict != null) {
                    dto.setMethod(dict.getCode());
                }
                dto.setType(SysResourceType.OTHER.getCode());
                dto.setState(SysResourceState.ENABLE.getCode());
                dto.setHidden(item.getHidden());
                dto.setDescription(item.getDescription());
                resourceService.addResource(dto);
            }
        }
    }
}
