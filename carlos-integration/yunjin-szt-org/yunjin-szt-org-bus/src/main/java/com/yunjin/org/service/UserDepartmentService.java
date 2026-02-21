package com.yunjin.org.service;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.org.pojo.dto.DepartmentDTO;
import com.yunjin.org.pojo.dto.UserDepartmentDTO;
import com.yunjin.org.pojo.param.CurDeptExecutorPageParam;
import com.yunjin.org.pojo.param.CurSubExecutorPageParam;
import com.yunjin.org.pojo.param.DepartmentUserListParam.DepartmentUserModify;
import com.yunjin.org.pojo.param.UserDeptRoleDTO;
import com.yunjin.org.pojo.param.UserPageParam;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户部门 业务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
public interface UserDepartmentService {

    /**
     * 将用户添加到部门
     *
     * @param userId        用户id
     * @param departmentIds 部门id
     * @author Carlos
     * @date 2022/12/21 14:35
     */
    void addRelationByUserId(String userId, List<UserDeptRoleDTO> departmentIds);


    void addRelationByRoleId(String userId, List<UserDeptRoleDTO> deptRoleDTOS);

    /**
     * 部门添加用户
     *
     * @param departmentId 部门id
     * @param users        参数1
     * @author Carlos
     * @date 2023/4/2 23:31
     */
    void addDepartmentUser(String departmentId, List<DepartmentUserModify> users);

    /**
     * 移除部门用户
     *
     * @param departmentId 参数0
     * @param users        参数1
     * @author Carlos
     * @date 2023/4/2 23:36
     */
    void removeDepartmentUser(String departmentId, List<DepartmentUserModify> users);


    /**
     * 删除用户和部门的关联信息
     *
     * @param userId 用户id
     * @author Carlos
     * @date 2022/12/21 15:30
     */
    void deleteByUserId(String userId);

    /**
     * 通过用户id获取部门id
     *
     * @param userId 用户id
     * @return java.lang.String
     * @author Carlos
     * @date 2022/12/21 15:31
     */

    Set<String> getDepartmentIdByUserId(String userId);

    /**
     * 获取部门下用户id
     *
     * @param departmentId 部门id
     * @return java.util.Set<java.lang.String>
     * @author Carlos
     * @date 2022/12/21 17:15
     */
    Set<String> getUserIdByDepartmentId(String departmentId);

    /**
     * 批量获取多个部门下的用户id
     *
     * @param departmentIds 参数0
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/12/21 17:23
     */
    Set<Serializable> getUserIdByDepartmentId(Set<Serializable> departmentIds);

    /**
     * 获取所有关联关系
     *
     * @return java.util.List<com.yunjin.org.pojo.dto.UserDepartmentDTO>
     * @author Carlos
     * @date 2023/1/17 13:46
     */
    List<UserDepartmentDTO> getAllRef();

    /**
     * 获取部门用户信息
     *
     * @param id 参数0
     * @return java.util.List<com.yunjin.org.pojo.dto.UserDepartmentDTO>
     * @author Carlos
     * @date 2023/4/2 15:37
     */
    List<UserDepartmentDTO> getByDepartmentId(String id);

    PageInfo<UserDepartmentDTO> getDepartmentUserPage(String id, UserPageParam page);

    /**
     * 获取部门管理员
     *
     * @param id 参数0
     * @return java.util.List<com.yunjin.org.pojo.dto.UserDepartmentDTO>
     * @author Carlos
     * @date 2023/4/15 11:13
     */
    List<UserDepartmentDTO> getDepartmentAdmin(String id);

    /**
     * 获取用户所属机构名称（a-b模式）
     *
     * @param userId 用户ID
     * @return java.util.List<java.lang.String>
     */
    List<String> getUserDepartmentName(String userId);

    List<UserDepartmentDTO> getByUserId(String userId);

    Paging<UserDepartmentDTO> getCurSubUser(CurSubExecutorPageParam param);

    List<UserDeptRoleDTO> getDeptRolesByUserId(String id);

    List<UserDepartmentDTO> listAll();

    Set<String> getRoleIdsByUserId(String id);

    List<DepartmentDTO> getDepartmentsByUserId(String id);

    List<UserDepartmentDTO> getByRoleIds(Set<String> roleIds);

    void batchUpdateUserDepartment(List<UserDepartmentDTO> res);

    void deleteByRoleId(String roelId);

    void removeUserRole(UserDeptRoleDTO param);

    List<UserDepartmentDTO> getByLevels(Set<String> deptLevels);

    List<UserDepartmentDTO> getByUserIdAndDeptId(String userId, String deptId);

    Paging<UserDepartmentDTO> getCurDeptUser(CurDeptExecutorPageParam param);
}
