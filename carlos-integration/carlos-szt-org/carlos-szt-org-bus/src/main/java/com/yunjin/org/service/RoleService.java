package com.yunjin.org.service;

import com.yunjin.core.pagination.Paging;
import com.yunjin.org.pojo.dto.RoleDTO;
import com.yunjin.org.pojo.param.RolePageParam;
import com.yunjin.org.pojo.param.UserDeptRoleDTO;
import com.yunjin.org.pojo.vo.RoleDetailVO;
import com.yunjin.org.pojo.vo.RolePageVO;
import com.yunjin.org.pojo.vo.UserRoleVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统角色 业务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
public interface RoleService {

    /**
     * 新增系统角色
     *
     * @param dto 系统角色数据
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    Boolean addRole(RoleDTO dto);

    /**
     * 获取下一个角色编码
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/1/16 9:40
     */
    String getRoleNextCode();

    /**
     * 删除系统角色
     *
     * @param ids 系统角色id
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    Boolean deleteRole(Set<String> ids);

    /**
     * 修改系统角色信息
     *
     * @param dto 对象信息
     * @author yunjin
     * @date 2022-11-11 18:19:17
     */
    Boolean updateRole(RoleDTO dto);


    /**
     * 获取角色信息
     *
     * @param roleId 角色id
     * @return com.yunjin.org.dto.user.RoleDTO
     * @author Carlos
     * @date 2022/11/14 19:18
     */
    RoleDTO getById(Serializable roleId);

    /**
     * 获取角色列表
     *
     * @param name 角色名称
     * @return java.util.List<com.yunjin.org.dto.user.RoleDTO>
     * @author Carlos
     * @date 2022/12/21 17:59
     */
    List<RoleDTO> getAll(String name);

    /**
     * @Title: getDetail
     * @Description: 获取详情
     * @Date: 2023/4/1 10:32
     * @Parameters: [id]
     * @Return com.yunjin.org.pojo.dto.RoleDTO
     */
    RoleDTO getDetail(String id);
    List<RoleDTO> getRoleByIds(Set<String> roleIds);

    /**
     * 初始化角色权限
     *
     * @author Carlos
     * @date 2023/5/25 18:27
     */
    void initRoleMenu();

    /**
     * 获取或者添加角色
     *
     * @param roleDTO 参数0
     * @author Carlos
     * @date 2023/5/29 23:23
     */
    RoleDTO getOrAdd(RoleDTO roleDTO);

    Paging<RolePageVO> getPage(RolePageParam param);

    void initMobileRoleMenu();

    void specifiedMenuForAllRole(List<String> menuIds);

    /**
     * @Title: listAll
     * @Description: 获取全量角色列表
     * @Date: 2024/9/13 16:04
     * @Parameters: [keyword]
     * @Return java.util.List<com.yunjin.org.pojo.dto.RoleDTO>
     */
    List<RoleDTO> listAll(String keyword);

    void removeUserRole(UserDeptRoleDTO param);

    UserRoleVO checkUserDeptRole(String userId, String deptId);


    /**
     * @Title: roleUserPage
     * @Description: 获取角色用户分页列表
     * @Date: 2024/9/13 16:04
     * @Parameters: [id, current, size]
     * @Return com.yunjin.core.pagination.Paging<com.yunjin.org.pojo.vo.RoleDetailVO.UserInfo>
     */
    Paging<RoleDetailVO.UserInfo> roleUserPage(String id, Integer current, Integer size, String keyWord);

    /**
     * @Title: addUserRole
     * @Description: 添加角色用户
     * @Date: 2024/9/13 16:04
     * @Parameters: [param]
     * @Return void
     */
    void addUserRole(UserDeptRoleDTO param);
}
