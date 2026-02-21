package com.yunjin.org.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO;
import com.yunjin.org.pojo.entity.RoleResourceGroupRef;
import com.yunjin.org.pojo.param.RoleResourceGroupRefPageParam;
import com.yunjin.org.pojo.vo.RoleResourceGroupRefVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色资源组关联表 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
public interface RoleResourceGroupRefManager extends BaseService<RoleResourceGroupRef> {

    /**
     * 新增角色资源组关联表
     *
     * @param dto 角色资源组关联表数据
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean add(RoleResourceGroupRefDTO dto);

    /**
     * 删除角色资源组关联表
     *
     * @param id 角色资源组关联表id
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean delete(Serializable id);

    /**
     * 修改角色资源组关联表信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    boolean modify(RoleResourceGroupRefDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    RoleResourceGroupRefDTO getDtoById(Serializable id);

    /**
     * 获取角色权限组
     *
     * @param roleId 主键id
     * @return com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    RoleResourceGroupRefDTO getByRoleId(Serializable roleId);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2024-8-22 10:59:20
     */
    Paging<RoleResourceGroupRefVO> getPage(RoleResourceGroupRefPageParam param);

    /**
     * @Title: removeByRoleId
     * @Description: 根据角色id删除
     * @Date: 2024/8/22 15:45
     * @Parameters: [roleId]
     * @Return boolean
     */
    boolean removeByRoleId(String roleId);

    /**
     * @Title: listByRoleIds
     * @Description: 根据角色id获取
     * @Date: 2024/8/22 17:17
     * @Parameters: [roleIds]
     * @Return java.util.List<com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO>
     */
    List<RoleResourceGroupRefDTO> listByRoleIds(Set<String> roleIds);
}
