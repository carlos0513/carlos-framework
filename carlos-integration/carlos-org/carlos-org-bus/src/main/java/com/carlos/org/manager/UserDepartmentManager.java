package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.entity.UserDepartment;
import com.carlos.org.pojo.param.CurDeptExecutorPageParam;
import com.carlos.org.pojo.param.CurSubExecutorPageParam;
import com.carlos.org.pojo.param.TaskExecutorPageMianYangParam;
import com.carlos.org.pojo.param.UserPageParam;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户部门 查询封装接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
public interface UserDepartmentManager extends BaseService<UserDepartment> {

    /**
     * 新增用户部门
     *
     * @param dtos 用户部门数据
     * @return boolean
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    boolean add(List<UserDepartmentDTO> dtos);

    /**
     * 删除用户部门
     *
     * @param userId 用户部门id
     * @return boolean
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    boolean deleteByUserId(String userId);


    /**
     * 获取部门下关联用户id
     *
     * @param departmentId 部门id
     * @return java.util.Set<java.lang.String>
     * @author Carlos
     * @date 2022/12/21 14:24
     */
    Set<String> getUserIdByDepartmentId(String departmentId);


    /**
     * 获取用户关联部门id
     *
     * @param userId 参数0
     * @return java.lang.String
     * @author Carlos
     * @date 2022/12/21 14:23
     */
    Set<String> getDepartmentIdByUserId(String userId);

    /**
     * 批量获取用户关联部门id
     * @param userIds
     * @return
     */
    Map<String, Set<String>> getDepartmentIdsByUserIds(Set<String> userIds);


    /**
     * 获取部门下关联用户id
     *
     * @param departmentIds 部门id
     * @return java.util.Set<java.lang.String>
     * @author Carlos
     * @date 2022/12/21 14:24
     */
    Set<Serializable> getUserIdByDepartmentId(Set<Serializable> departmentIds);

    /**
     * 初始化缓存
     *
     * @author Carlos
     * @date 2023/2/24 18:30
     */
    void initCache();

    /**
     * 获取部门用户
     *
     * @param id 参数0
     * @return java.util.List<com.carlos.org.pojo.dto.UserDepartmentDTO>
     * @author Carlos
     * @date 2023/4/2 15:39
     */
    List<UserDepartmentDTO> listByDepartmentId(String id);

    /**
     * 根据用户ID连表查询部门详细信息
     *
     * @param userId 用户ID
     * @return 部门信息列表
     */
    List<DepartmentDTO> getDepartmentsByUserId(String userId);


    List<UserDepartmentDTO> listAdminByDepartmentId(String id);

    /**
     * 批量删除
     *
     * @param collect 参数0
     * @return boolean
     * @author Carlos
     * @date 2023/4/2 23:39
     */
    boolean deleteBatch(List<UserDepartmentDTO> collect);

    /**
     * 获取所有关联关系
     *
     * @return java.util.List<com.carlos.org.pojo.dto.UserDepartmentDTO>
     * @author Carlos
     * @date 2024/4/8 10:11
     */
    List<UserDepartmentDTO> listAllRef();

    PageInfo<UserDepartmentDTO> listByDepartmentIdPage(String id, UserPageParam page);

    List<UserDepartmentDTO> listByDepartmentIdPage(List<String> ids);

    List<UserDepartmentDTO> listByDeptIds(Set<String> deptIds);

    List<UserDepartmentDTO> listAll();

    Paging<UserDepartmentDTO> listByDepartmentCode(CurSubExecutorPageParam param);

    boolean deleteByRoleId(String roelId);

    /**
     * 绵阳定开  批量获取部门用户
     *
     * @param
     * @return
     */
    Map<String, List<UserDepartmentDTO>> getUsersByDepartmentIdsMianYang(Set<String> deptIds);

    Paging<UserDepartmentDTO> getUserPageByDeptId(TaskExecutorPageMianYangParam param);

    UserDepartmentDTO getByUserIdAndDeptId(String userId, String deptId);

    Paging<UserDepartmentDTO> listCurDept(CurDeptExecutorPageParam param);

    // 分页版本
    Paging<UserDepartmentDTO> getUserDeptInfoByRoleIdPage(String roleId, Integer current, Integer size, String keyWord);
}
