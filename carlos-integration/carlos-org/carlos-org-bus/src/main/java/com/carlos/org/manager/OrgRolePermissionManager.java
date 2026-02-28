package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.OrgRolePermissionDTO;
import com.carlos.org.pojo.entity.OrgRolePermission;
import com.carlos.org.pojo.param.OrgRolePermissionPageParam;
import com.carlos.org.pojo.vo.OrgRolePermissionVO;

import java.io.Serializable;

/**
 * <p>
 * 角色权限 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
public interface OrgRolePermissionManager extends BaseService<OrgRolePermission> {

    /**
     * 新增角色权限
     *
     * @param dto 角色权限数据
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean add(OrgRolePermissionDTO dto);

    /**
     * 删除角色权限
     *
     * @param id 角色权限id
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean delete(Serializable id);

    /**
     * 修改角色权限信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean modify(OrgRolePermissionDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.pojo.dto.OrgRolePermissionDTO
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgRolePermissionDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    Paging<OrgRolePermissionVO> getPage(OrgRolePermissionPageParam param);
}
