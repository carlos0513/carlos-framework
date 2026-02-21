package com.yunjin.org.service;

import com.yunjin.org.pojo.dto.UserRoleDTO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户角色 业务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
public interface UserRoleService {


    /**
     * 添加用户角色关联关系
     *
     * @param id      参数0
     * @param roleIds 参数1
     * @author Carlos
     * @date 2022/12/21 15:09
     */
    void addUserRole(String id, Set<String> roleIds);

    /**
     * 批量添加用户角色关联关系
     *
     * @param ids      用户id
     * @param roleIds  角色id
     * @author Carlos
     * @date 2022/12/21 15:09
     */
    void batchAddUserRole(Set<String> ids, Set<String> roleIds);
    /**
     * 删除用户关联角色
     *
     * @param userId 参数0
     * @author Carlos
     * @date 2022/12/21 15:29
     */
    void deleteByUserId(String userId);

    List<UserRoleDTO> getByRoleId(Set<String> roleIds);

    /**
     * 获取用户所有的角色
     *
     * @param userId 用户di
     * @return java.util.List<com.yunjin.org.dto.user.UserRoleDTO>
     * @author Carlos
     * @date 2022/12/21 15:39
     */
    Set<String> getRoleIdsByUserId(String userId);

    /**
     * @Title: removeByRoleId
     * @Description: 根据roleId删除
     * @Date: 2023/5/8 11:52
     * @Parameters: [roleIds]
     * @Return java.lang.Boolean
     */
    Boolean removeByRoleId(Set<String> roleIds);


}
