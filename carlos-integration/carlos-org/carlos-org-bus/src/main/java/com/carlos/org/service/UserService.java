package com.carlos.org.service;

import com.carlos.core.base.UserInfo;
import com.carlos.core.pagination.Paging;
import com.carlos.org.pojo.dto.OrgDockingMappingDTO;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.dto.UserRoleDTO;
import com.carlos.org.pojo.emuns.OrgUserDockingOperateTypeEnum;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.carlos.org.pojo.param.UserChangePwdParam;
import com.carlos.org.pojo.param.UserForgetPwdParam;
import com.carlos.org.pojo.param.UserInfoUpdateParam;
import com.carlos.org.pojo.param.UserPageParam;
import com.carlos.org.pojo.vo.UserPageVO;
import com.carlos.org.pojo.vo.UserSessionVO;
import com.carlos.system.enums.MenuType;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户 业务接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
public interface UserService {

    /**
     * 新增系统用户
     *
     * @param dto 系统用户数据
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    UserDTO addUser(UserDTO dto);

    /**
     * 对接用户数据
     *
     * @param user        用户信息
     * @param mapping     映射信息
     * @param operateType 操作类型
     * @author Carlos
     * @date 2025-02-28 11:38
     */
    void dockingUser(UserDTO user, OrgDockingMappingDTO mapping, OrgUserDockingOperateTypeEnum operateType);

    /**
     * 删除系统用户
     *
     * @param ids 系统用户id
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    void deleteUser(Set<Serializable> ids);

    /**
     * 注销用户
     *
     * @param ids 参数0
     * @author Carlos
     * @date 2023/11/20 0:13
     */
    void writeOffUser(Set<Serializable> ids);

    /**
     * 修改系统用户信息
     *
     * @param dto 对象信息
     * @author carlos
     * @date 2022-11-11 18:19:17
     */
    boolean updateUser(UserDTO dto);

    /**
     * 获取用户信息
     *
     * @param userId 参数0
     * @return com.carlos.core.base.UserInfo
     * @author Carlos
     * @date 2022/3/7 16:33
     */
    UserInfo getUserInfo(Serializable userId);


    /**
     * 根据用户账号获取用户信息
     *
     * @param account 用户账号
     * @return com.carlos.org.dto.user.UserDTO
     * @author Carlos
     * @date 2022/11/14 18:14
     */
    UserDTO getUserByAccount(String account);

    /**
     * 根据用户凭证获取用户信息
     *
     * @param credential 用户凭证信息  手机号或者账号
     * @return com.carlos.org.pojo.dto.UserDTO
     * @author Carlos
     * @date 2023/10/25 14:53
     */
    UserDTO getUserByCredentials(String credential);


    /**
     * 获取用户角色
     *
     * @param account 用户账号
     * @return com.carlos.org.dto.user.UserRoleDTO
     * @author Carlos
     * @date 2022/11/14 18:22
     */
    List<UserRoleDTO> getUserRoles(String account);

    /**
     * 根据token获取当前登录用户信息
     *
     * @return com.carlos.org.dto.user.UserDTO
     * @author Carlos
     * @date 2022/11/15 9:18
     */
    UserDTO getCurrentUser(MenuType menuType);

    /**
     * 获取角色用户
     *
     * @param roleIds 角色id
     * @return java.util.List<com.carlos.org.dto.user.UserDTO>
     * @author Carlos
     * @date 2023/2/9 23:35
     */
    List<UserDTO> getUserByRoleIds(Set<String> roleIds);

    boolean changePwd(UserChangePwdParam param);

    /**
     * 第一次登录强制修改密码
     */
    boolean forceChangedPwd(UserChangePwdParam param);

    /**
     * 更新用户个人资料
     */
    boolean updateUserInfo(UserInfoUpdateParam param);

    /**
     * 获取部门下的用户信息
     *
     * @param departmentId 部门id
     * @return java.util.List<com.carlos.org.pojo.vo.UserVO>
     * @author Carlos
     * @date 2022/12/19 12:42
     */
    List<UserSessionVO> getUserByDeptId(Serializable departmentId);


    /**
     * 获取用户信息
     *
     * @param id           用户id
     * @param getDeptRoles 是否获取用户部门
     * @return com.carlos.org.dto.user.UserDTO
     * @author Carlos
     * @date 2023/1/10 10:08
     */
    UserDTO getUserById(String id, boolean getDeptRoles);

    /**
     * @Title: resetPassword
     * @Description: 重置密码
     * @Date: 2023/1/11 13:14
     * @Parameters: [dto]
     * @Return void
     */
    void resetPassword(String id, String pwd);

    /**
     * @Title: export
     * @Description: 导出数据
     * @Date: 2023/2/20 15:20
     * @Parameters: [response, isTemplate]
     * @Return void
     */
    void export(HttpServletResponse response, boolean isTemplate);

    /**
     * @Title: deleteAll
     * @Description: 清空数据
     * @Date: 2023/2/20 17:23
     * @Parameters: []
     * @Return void
     */
    void deleteAll();

    /**
     * 忘记密码
     *
     * @param param 参数0
     * @author Carlos
     * @date 2023/4/3 0:00
     */
    void forgetPassword(UserForgetPwdParam param);

    /**
     * 获取用户列表
     *
     * @param keyword    参数0
     * @param deptLevels
     * @return java.util.List<com.carlos.org.pojo.dto.UserDTO>
     * @author Carlos
     * @date 2023/4/3 14:51
     */
    List<UserDTO> list(String keyword, Set<String> deptLevels);

    /**
     * 用户搜索完全匹配
     *
     * @param keyword 参数0
     * @return java.util.List<com.carlos.org.pojo.dto.UserDTO>
     * @author Carlos
     * @date 2023/4/5 21:34
     */
    List<UserDTO> completeMatchList(String keyword);

    /**
     * 更改用户状态
     *
     * @param id    用户id
     * @param state 用户状态
     * @author Carlos
     * @date 2023/4/4 14:34
     */
    void changeState(String id, UserStateEnum state);

    /**
     * 通过手机号获取用户信息
     *
     * @param mobile 手机号码
     * @return com.carlos.org.pojo.dto.UserDTO
     * @author Carlos
     * @date 2023/4/12 13:53
     */
    UserDTO getUserByPhone(String mobile);


    String getFileUrl(String fileId);

    /**
     * 用户基本信息
     *
     * @param id    用户id
     * @param roleInfo 是否查角色
     * @return com.carlos.org.pojo.dto.UserDTO
     * @author Carlos
     * @date 2023/5/8 10:31
     */
    UserDTO getBaseInfo(String id, boolean roleInfo);

    /**
     * 获取当前登录人权限内的用户
     */
    Paging<UserPageVO> listAuthLimit(UserPageParam param);

    /**
     * @Title: listByIds
     * @Description: 根据用户ids获取
     * @Date: 2023/7/12 15:08
     * @Parameters: [ids]
     * @Return java.util.List<com.carlos.org.pojo.dto.UserDTO>
     */
    List<UserDTO> listByIds(Set<String> ids);


    /**
     * 获取所有用户及部门信息
     *
     * @return java.util.List<com.carlos.org.pojo.dto.UserDTO>
     * @author Carlos
     * @date 2023/10/22 19:43
     */
    List<UserDTO> getAllUser();

    /**
     * 获取默认密码
     */
    String getDefaultPwd(String id);

    /**
     * 批量修改
     *
     * @throws
     * @author Carlos
     * @date 2024/4/8 9:44
     */
    void changeRegin(String regionName);

    void resetUserDeptRole();

    /**
     * 获取用户组织信息
     *
     * @param deptCode 部门编码
     * @param userId   用户id
     * @return com.carlos.org.pojo.dto.UserDTO
     * @author Carlos
     * @date 2025-07-24 00:53
     */
    UserDTO getUserOrgInfo(String deptCode, String userId);

    void export(UserPageParam param, HttpServletResponse response);

    /**
     * 发送创建用户消息
     *
     * @param phones 手机号列表
     * @return com.carlos.org.pojo.vo.UserDetailVO
     * @author Carlos
     * @date 2025-08-14 14:28
     */
    void sendCreateMsg(Set<String> phones);
}
