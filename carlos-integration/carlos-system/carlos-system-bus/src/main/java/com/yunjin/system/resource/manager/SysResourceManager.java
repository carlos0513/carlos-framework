package com.carlos.system.resource.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.system.resource.pojo.dto.SysResourceDTO;
import com.carlos.system.resource.pojo.dto.SysResourceTreeDTO;
import com.carlos.system.resource.pojo.entity.SysResource;
import com.carlos.system.resource.pojo.param.SysResourcePageParam;
import com.carlos.system.resource.pojo.vo.SysResourceVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统资源 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
public interface SysResourceManager extends BaseService<SysResource> {

    /**
     * 新增系统资源
     *
     * @param dto 系统资源数据
     * @return boolean
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    boolean add(SysResourceDTO dto);

    /**
     * 删除系统资源
     *
     * @param id 系统资源id
     * @return boolean
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    boolean delete(Serializable id);

    /**
     * 修改系统资源信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    boolean modify(SysResourceDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.user.pojo.dto.ResourceDTO
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    SysResourceDTO getDtoById(String id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    Paging<SysResourceVO> getPage(SysResourcePageParam param);

    /**
     * 获取指定 menu 下的资源
     *
     * @param categoryId 分类id
     * @return java.util.List<com.carlos.user.pojo.dto.ResourceDTO>
     * @date 2021/12/27 17:31
     */
    List<SysResourceDTO> getResourceByCategoryId(String categoryId);

    /**
     * 获取菜单下的资源数目
     *
     * @param menuId 菜单id
     * @return boolean
     * @author yunjin
     * @date 2021/12/28 17:12
     */
    long getResourceCountByMenuId(Serializable menuId);

    /**
     * 获取资源树形数据
     *
     * @param menuId 菜单id
     * @return java.util.List<com.carlos.sys.pojo.dto.MenuTreeDTO>
     * @author yunjin
     * @date 2022/1/4 14:49
     */
    List<SysResourceTreeDTO> getResourceTree(String menuId);

    /**
     * 根据id批量获取资源
     *
     * @param ids 资源id集合
     * @return java.util.List<com.carlos.sys.pojo.dto.ResourceDTO>
     * @author yunjin
     * @date 2022/1/4 15:25
     */
    List<SysResourceDTO> getDtoByIds(Set<String> ids);

    /**
     * 根据对象信息查找资源
     *
     * @param dto 资源信息
     * @return java.lang.Long
     * @author yunjin
     * @date 2022/1/12 17:44
     */
    String getIdByDto(SysResourceDTO dto);
}
