package com.carlos.org.manager;

import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.OrgPermissionDTO;
import com.carlos.org.pojo.entity.OrgPermission;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 权限 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
public interface OrgPermissionManager extends BaseService<OrgPermission> {

    /**
     * 新增权限
     *
     * @param dto 权限数据
     * @return boolean
     */
    boolean add(OrgPermissionDTO dto);

    /**
     * 删除权限
     *
     * @param id 权限id
     * @return boolean
     */
    boolean delete(Serializable id);

    /**
     * 修改权限信息
     *
     * @param dto 对象信息
     * @return boolean
     */
    boolean modify(OrgPermissionDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return OrgPermissionDTO
     */
    OrgPermissionDTO getDtoById(Serializable id);

    /**
     * 获取所有权限列表
     *
     * @return List<OrgPermissionDTO>
     */
    List<OrgPermissionDTO> listAll();

    /**
     * 获取子权限列表
     *
     * @param parentId 父权限ID
     * @return List<OrgPermissionDTO>
     */
    List<OrgPermissionDTO> getChildrenByParentId(Serializable parentId);

    /**
     * 根据权限编码获取权限
     *
     * @param permCode 权限编码
     * @return OrgPermissionDTO
     */
    OrgPermissionDTO getByPermCode(String permCode);

    /**
     * 获取权限被角色使用的数量
     *
     * @param permId 权限ID
     * @return int
     */
    int getRoleUseCount(Serializable permId);

}
