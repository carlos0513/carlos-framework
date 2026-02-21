package com.yunjin.org.api;

import com.yunjin.core.base.UserInfo;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.fallback.FeignUserFallbackFactory;
import com.yunjin.org.pojo.ao.UserDeptRoleAO;
import com.yunjin.org.pojo.ao.UserDetailAO;
import com.yunjin.org.pojo.ao.UserLoginAO;
import com.yunjin.org.pojo.ao.UserOrgAO;
import com.yunjin.org.pojo.param.ApiUserDeptRoleParam;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统用户 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-20 14:07:16
 */
@FeignClient(value = ServiceNameConstant.USER, contextId = "user", path = "/api/org/user", fallbackFactory = FeignUserFallbackFactory.class)
public interface ApiUser {

    /**
     * 获取用户组织信息 包含用户基本信息、角色信息、部门信息
     */
    @GetMapping("getUserOrgInfo")
    Result<UserOrgAO> getUserOrgInfo(@RequestParam("deptCode") String deptCode, @RequestParam("userId") String userId);

    /**
     * 获取所有用户信息 包含部门信息
     *
     * @return com.yunjin.core.response.Result<java.util.List < com.yunjin.org.pojo.ao.UserDetailAO>>
     * @author Carlos
     * @date 2023/10/22 19:40
     */
    @GetMapping("allUser")
    Result<List<UserDetailAO>> allUser();

    /**
     * 根据id获取用户信息
     *
     * @param id 用户id
     * @return com.yunjin.core.base.UserInfo
     * @author yunjin
     * @date 2022/3/7 16:09
     */
    @GetMapping("getById")
    Result<UserInfo> getUserById(@RequestParam("id") String id);

    /**
     * 根据ids获取用户信息
     *
     * @param ids 用户id
     * @return com.yunjin.core.base.UserInfo
     * @author yunjin
     * @date 2022/7/12 15:09
     */
    @PostMapping("list")
    Result<List<UserInfo>> getUserByIds(@RequestBody ParamIdSet<String> ids);

    /**
     * 根据ids获取用户信息包括电话号码
     *
     * @param ids 用户id
     * @return com.yunjin.core.base.UserInfo
     * @author yunjin
     * @date 2022/7/12 15:09
     */
    @PostMapping("taskList")
    Result<List<UserDetailAO>> getTaskUserByIds(@RequestBody ParamIdSet<String> ids);


    /**
     * @desc 获取部门code下所属用户
     * @author GaoQiao
     * @date 2023/7/14 10:31
     * @params
     */
    @GetMapping("listByDeptCode")
    Result<List<UserInfo>> listByDeptCode(@RequestParam("deptCode") String deptCode);

    /**
     * @Title: getCurrentUserId
     * @Description: 获取当前登录用户id
     * @Date: 2023/6/28 17:09
     * @Parameters: []
     * @Return java.lang.String
     */
    @ApiOperation("获取当前登录用户id")
    @GetMapping("currentUserId")
    Result<String> getCurrentUserId();

    /**
     * @Title: getCurrentDepartmentCode
     * @Description: 获取当前登录用户所在部门code
     * @Date: 2023/6/28 17:11
     * @Parameters: []
     * @Return java.lang.String
     */
    @ApiOperation("获取当前登录用户所在部门code")
    @GetMapping("currentDepartmentCode")
    Result<String> getCurrentDepartmentCode();


    /**
     * @Title: getCurrentRegionCode
     * @Description: 获取当前登录用户所在区域code
     * @Date: 2023/6/28 17:14
     * @Parameters: []
     * @Return java.lang.String
     */
    @ApiOperation("获取当前登录用户所在区域code")
    @GetMapping("currentRegionCode")
    Result<String> getCurrentRegionCode();

    /**
     * 获取当前登录用户信息
     *
     * @return com.yunjin.core.response.Result<com.yunjin.org.pojo.ao.UserAO>
     * @author Carlos
     * @date 2023/7/16 23:46
     */
    @GetMapping("currentUser")
    Result<UserLoginAO> getCurrentUser();

    /**
     * 白名单接口获取当前登录用户信息
     *
     * @return com.yunjin.core.response.Result<com.yunjin.org.pojo.ao.UserAO>
     * @author Carlos
     * @date 2023/7/16 23:46
     */
    @GetMapping("currentUserByWhitList")
    Result<UserLoginAO> getCurrentUserByWhiteList();

    /**
     * 根据部门和角色获取用户信息
     *
     * @param param 参数0
     * @return com.yunjin.core.response.Result<com.yunjin.org.pojo.ao.UserDeptRoleAO>
     * @throws
     * @author Carlos
     * @date 2024/3/29 14:58
     */
    @PostMapping("getUserByDeptAndRole")
    Result<List<UserDeptRoleAO>> getUserByDeptAndRole(@RequestBody ApiUserDeptRoleParam param);

    @GetMapping("detail/{id}")
    Result<UserDetailAO> getUserDetailById(@PathVariable(value = "id") String id);

    /**
     * 根据userId判断用户是否为超级管理员
     * @param userId
     * @return
     */
    @GetMapping("/checkManager")
    Result<Boolean> checkManager(@RequestParam("userId") String userId);

    /**
     * 通过账号获取用户信息
     *
     * @param account 用户账号
     * @return com.yunjin.core.response.Result<com.yunjin.core.base.UserInfo>
     * @author Carlos
     * @date 2024-12-26 10:28
     */
    @GetMapping("getByAccount")
    Result<UserInfo> getUserByName(@RequestParam("account") String account);
}
