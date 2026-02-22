package com.carlos.system.resource.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.system.resource.pojo.dto.ResourceCategoryDTO;
import com.carlos.system.resource.pojo.entity.SysResourceCategory;
import com.carlos.system.resource.pojo.param.SysResourceCategoryPageParam;
import com.carlos.system.resource.pojo.vo.SysResourceCategoryVO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 资源分类 查询封装接口
 * </p>
 *
 * @author carlos
 * @date 2022-1-5 17:23:27
 */
public interface SysResourceCategoryManager extends BaseService<SysResourceCategory> {

    /**
     * 新增资源分类
     *
     * @param dto 资源分类数据
     * @return boolean
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    boolean add(ResourceCategoryDTO dto);

    /**
     * 删除资源分类
     *
     * @param id 资源分类id
     * @return boolean
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    boolean delete(Serializable id);

    /**
     * 修改资源分类信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    boolean modify(ResourceCategoryDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.voice.common.dto.sys.ResourceCategoryDTO
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    ResourceCategoryDTO getDtoById(String id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author carlos
     * @date 2022-1-5 17:23:27
     */
    Paging<SysResourceCategoryVO> getPage(SysResourceCategoryPageParam param);

    /**
     * 获取所有的分类
     *
     * @param categoryId 分类id
     * @param detail     是否获取详情信息
     * @return java.util.List<com.carlos.voice.common.dto.sys.ResourceCategoryDTO>
     * @author carlos
     * @date 2022/1/5 16:46
     */
    List<ResourceCategoryDTO> getCategoryTree(String categoryId, boolean detail);

    /**
     * 获取子类型
     *
     * @param parentId 父id
     * @param detail   是否获取详情
     * @return java.util.List<com.carlos.voice.common.dto.sys.ResourceCategoryDTO>
     * @author carlos
     * @date 2022/1/5 17:47
     */
    List<ResourceCategoryDTO> getCategoryByParentId(String parentId, boolean detail);

    /**
     * 通过id获取名称
     *
     * @param categoryId 类型id
     * @return java.lang.String
     * @author carlos
     * @date 2022/1/5 17:18
     */
    String getNameById(Serializable categoryId);

    /**
     * 是否存在子类型
     *
     * @param categoryId 分类id
     * @return java.lang.Boolean
     * @author carlos
     * @date 2022/1/6 10:46
     */
    boolean existChildren(Serializable categoryId);

    /**
     * 获取资源类型id
     *
     * @param parentId 父id
     * @param name     类型名称
     * @return java.lang.Long
     * @author carlos
     * @date 2022/1/12 16:48
     */
    String getIdByName(String parentId, String name);
}
