package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.UserRoleDTO;
import com.carlos.org.pojo.entity.UserRole;
import com.carlos.org.pojo.param.UserRolePageParam;
import com.carlos.org.pojo.vo.UserRoleVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户角色 查询封装接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
public interface UserRoleManager extends BaseService<UserRole> {

    /**
     * 新增用户角色
     *
     * @param dto 用户角色数据
     * @return boolean
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    boolean add(List<UserRoleDTO> dto);

    /**
     * 新增用户角色
     *
     * @param dtos 用户角色数据
     * @return void
     * @author carlos
     * @date 2022-11-11
     */

    void addBatch(List<UserRoleDTO> dtos);

    /**
     * 删除用户角色
     *
     * @param id 用户角色id
     * @return boolean 19:21:46
     */
    boolean delete(Serializable id);

    /**
     * 修改用户角色信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    boolean modify(UserRoleDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.dto.user.UserRoleDTO
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    UserRoleDTO getDtoById(String id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    Paging<UserRoleVO> getPage(UserRolePageParam param);

    /**
     * 获取用户角色
     *
     * @param userId 用户id
     * @return java.util.List<com.carlos.org.dto.user.UserRoleDTO>
     * @author Carlos
     * @date 2022/11/14 18:29
     */
    List<UserRoleDTO> getRoleByUserId(String userId);

    /**
     * 获取角色的关联信息
     *
     * @param roleId 角色id
     * @author Carlos
     * @date 2022/11/16 15:27
     */
    List<UserRoleDTO> listByRoleId(String roleId);

    boolean deleteByUserId(String userId);


    boolean batchDeleteByUserId(Set<String> userIds);

    /**
     * 获取角色下的用户id
     *
     * @param roleId 参数0
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/11/23 12:47
     */
    Set<Serializable> listUserIdByRole(Serializable roleId);

    /**
     * 通过角色id批量获取用户id
     *
     * @param roleIds 参数0
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/11/23 13:19
     */
    Set<Serializable> listUserIdByRoleId(Set<Serializable> roleIds);

    /**
     * getByRoleIds
     *
     * @param roleIds 参数0
     * @return java.util.List<com.carlos.org.dto.user.UserRoleDTO>
     * @author Carlos
     * @date 2022/12/19 13:07
     */
    List<UserRoleDTO> getByRoleIds(Set<String> roleIds);

    /**
     * @Title: batchDeleteByRoleId
     * @Description: 根据角色id批量删除
     * @Date: 2023/4/1 9:51
     * @Parameters: [roleIds]
     * @Return void
     */
    boolean batchDeleteByRoleId(Set<String> roleIds);

    /**
     * @Title: removeByRoleId
     * @Description: 根据roleId删除
     * @Date: 2023/5/8 11:53
     * @Parameters: [roleIds]
     * @Return java.lang.Boolean
     */
    Boolean removeByRoleId(Set<String> roleIds);

    List<UserRoleDTO> listAll();

    /**
     * 批量获取用户角色
     *
     * @param userIds 用户ids
     * @return java.util.List<com.carlos.org.dto.user.UserRoleDTO>
     * @author Carlos
     * @date 2022/11/14 18:29
     */
    List<UserRoleDTO> getRoleByUserIds(List<String> userIds);
}
