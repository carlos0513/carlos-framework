package com.yunjin.org.manager;

import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.pojo.dto.RoleResourceDTO;
import com.yunjin.org.pojo.entity.RoleResource;

import java.io.Serializable;

/**
 * <p>
 * 角色资源 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
public interface RoleResourceManager extends BaseService<RoleResource> {

    /**
     * 新增角色资源
     *
     * @param dto 角色资源数据
     * @return boolean
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    boolean add(RoleResourceDTO dto);

    /**
     * 删除角色对应的资源
     *
     * @param roleId 角色id
     * @return boolean
     * @author Carlos
     * @date 2022/1/6 14:34
     */
    boolean deleteByRoleId(Serializable roleId);


    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.org.pojo.dto.RoleResourceDTO
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    RoleResourceDTO getDtoById(String id);

}
