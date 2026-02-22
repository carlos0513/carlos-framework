package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.dto.UserDeptRoleDTO;
import com.carlos.org.pojo.entity.User;
import com.carlos.org.pojo.excel.UserPageExcel;
import com.carlos.org.pojo.param.ApiUserDeptRoleParam;
import com.carlos.org.pojo.param.UserExcludeDeptPageParam;
import com.carlos.org.pojo.param.UserPageParam;
import com.carlos.org.pojo.vo.UserBaseVO;
import com.carlos.org.pojo.vo.UserFloatCardInfoVO;
import com.carlos.org.pojo.vo.UserListVO;
import com.carlos.org.pojo.vo.UserPageVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户 查询封装接口
 * </p>
 *
 * @author yunjin
 */
public interface UserManager extends BaseService<User> {

    /**
     * 新增系统用户
     *
     * @param dto 系统用户数据
     * @return boolean
     */
    boolean add(UserDTO dto);

    /**
     * 使用账号获取用户信息
     *
     * @param account 账号
     * @return com.carlos.user.pojo.dto.UserDTO
     */
    UserDTO getUserByAccount(String account);


    /**
     * 通过手机号获取用户信息
     *
     * @param mobile 手机号
     * @return com.carlos.org.pojo.dto.UserDTO
     */
    UserDTO getUserByPhone(String mobile);

    /**
     * 通过手机号集合获取用户信息
     *
     * @param phones 手机号集合
     * @return java.util.List<com.carlos.org.pojo.dto.UserDTO>
     */
    List<UserDTO> getUserByPhones(Set<String> phones);

    /**
     * 删除系统用户
     *
     * @param id 系统用户id
     * @return boolean
     */
    boolean delete(Serializable id);


    boolean writterOff(Set<Serializable> ids);

    /**
     * 修改系统用户信息
     *
     * @param dto 对象信息
     * @return boolean
     */
    boolean modify(UserDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.dto.user.UserDTO
     */
    UserDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     */
    Paging<UserPageVO> getPage(UserPageParam param);

    /**
     * 根据条件获取所有用户数据
     *
     * @param param 分页参数
     * @return com.carlos.org.pojo.vo.UserPageVO
     */
    List<UserPageExcel> getParamAll(UserPageParam param);

    /**
     * 根据账号获取数据总数
     *
     * @param account 用户名
     * @return int
     */
    int getCountByAccount(String account);

    /***
     *
     * @param account
     * @param id
     * @return com.carlos.org.dto.user.UserDTO
     * @author yunjin
     * @date 20212/11/14 14:51
     */
    UserDTO selectByAccountAndId(String account, String id);


    /**
     * 批量获取用户信息
     *
     * @param userIds 用户id
     * @return java.util.List<com.carlos.org.dto.user.UserDTO>
     */
    List<UserDTO> getByIds(Set<String> userIds);

    /**
     *
     */
    boolean deleteAll();

    /**
     *
     */
    List<UserDTO> listAll();

    // 新增方法：查询全部用户，只返回指定字段
    List<UserDTO> listAllWithSelectedFields();

    /**
     * 通过关键字查找用户
     *
     * @param keyword 关键字
     * @return java.util.List<com.carlos.org.pojo.dto.UserDTO>
     */
    List<UserDTO> listByKeyword(String keyword);

    /**
     * 使用关键字全匹配
     *
     * @param keyword 参数0
     * @return java.util.List<com.carlos.org.pojo.dto.UserDTO>
     */
    List<UserDTO> listWithKeyword(String keyword);

    /**
     *
     */
    List<UserDTO> listById(Set<String> ids);

    /**
     * 根据手机号查询用户数量，如果存在用户id则排除该id
     *
     * @param phone 手机号
     * @param id    排除用户id
     */
    int getCountByPhoneExcludeId(String phone, String id);

    /**
     * 根据账号和手机号同时登录
     *
     * @param credential 参数0
     * @return com.carlos.org.pojo.dto.UserDTO
     */
    UserDTO getUserByCredentials(String credential);

    /**
     * 获取不包含在某个部门下的用户
     *
     * @return com.carlos.core.pagination.Paging<com.carlos.org.pojo.vo.UserPageVO>
     */
    Paging<UserBaseVO> excludeByDept(UserExcludeDeptPageParam param);

    /**
     * 根据部门和角色获取用户信息
     *
     * @param param 参数0
     * @return java.util.List<com.carlos.org.pojo.dto.UserDeptRoleDTO>
     */

    List<UserDeptRoleDTO> getUserByDeptAndRole(ApiUserDeptRoleParam param);

    Paging<UserDTO> getInDeptUser(UserExcludeDeptPageParam param, Set<String> userSet);

    Long getActivityCount(String startTime, String endTime, Set<String> userIds);

    String getPhoneByUserId(String userId);

    List<UserDTO> getByRoleId(String id);

    List<UserDTO> getAdminUser();

    PageInfo<UserListVO> search(UserPageParam param);

    /**
     * 获取部门及下级用户
     *
     * @param deptCode 参数0
     * @return java.util.List<com.carlos.org.pojo.dto.UserDTO>
     */
    List<UserDTO> listUserByDeptCode(String deptCode);

    /**
     * 获取用户悬浮卡片信息(该方法仅用于前端展示，禁止用于业务)
     *
     * @param userId   参数0
     * @param deptId   参数1
     * @param deptCode 参数2
     * @return com.carlos.org.pojo.vo.UserFloatCardInfoVO
     */
    UserFloatCardInfoVO getUserCardInfo(String userId, String deptId, String deptCode);

    // 用户当前部门及以下的的用户
    List<UserDTO> listIncludeSubUser(String departmentId);
}
