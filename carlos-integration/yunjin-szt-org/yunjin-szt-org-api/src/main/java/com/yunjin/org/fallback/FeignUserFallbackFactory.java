package com.yunjin.org.fallback;

import com.yunjin.core.base.UserInfo;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiUser;
import com.yunjin.org.pojo.ao.UserDeptRoleAO;
import com.yunjin.org.pojo.ao.UserDetailAO;
import com.yunjin.org.pojo.ao.UserLoginAO;
import com.yunjin.org.pojo.ao.UserOrgAO;
import com.yunjin.org.pojo.param.ApiUserDeptRoleParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


/**
 * <p>
 * 用户降级服务
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:57
 */

@Slf4j
public class FeignUserFallbackFactory implements FallbackFactory<ApiUser> {

    @Override
    public ApiUser create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("用户服务调用失败: message:{}", message);
        return new ApiUser() {


            @Override
            public Result<UserOrgAO> getUserOrgInfo(String deptCode, String userId) {
                return Result.fail("用户组织信息获取失败", message);
            }

            @Override
            public Result<List<UserDetailAO>> allUser() {
                return Result.fail("用户信息获取失败", message);
            }

            @Override
            public Result<UserInfo> getUserById(String id) {
                return Result.fail("用户信息获取失败", message);
            }

            @Override
            public Result<List<UserInfo>> getUserByIds(ParamIdSet<String> ids) {
                return Result.fail("用户信息获取失败", message);
            }

            @Override
            public Result<List<UserDetailAO>> getTaskUserByIds(ParamIdSet<String> ids) {
                return Result.fail("用户信息获取失败", message);
            }

            @Override
            public Result<List<UserInfo>> listByDeptCode(String deptCode) {
                return Result.fail("获取部门code下所属用户", message);
            }


            @Override
            public Result<String> getCurrentUserId() {
                return Result.fail("用户区域信息获取失败", message);
            }

            @Override
            public Result<String> getCurrentDepartmentCode() {
                return Result.fail("用户区域信息获取失败", message);
            }

            @Override
            public Result<String> getCurrentRegionCode() {
                return Result.fail("用户区域信息获取失败", message);
            }

            @Override
            public Result<UserLoginAO> getCurrentUser() {
                return Result.fail("用户信息获取失败", message);
            }

            @Override
            public Result<UserLoginAO> getCurrentUserByWhiteList() {
                return Result.fail("用户信息获取失败", message);
            }

            @Override
            public Result<List<UserDeptRoleAO>> getUserByDeptAndRole(ApiUserDeptRoleParam param) {
                return Result.fail("用户信息获取失败", message);
            }

            @Override
            public Result<UserDetailAO> getUserDetailById(String id) {
                return Result.fail("用户详细信息获取失败", message);
            }

            @Override
            public Result<Boolean> checkManager(String userId) {
                return Result.fail("用户详细信息获取失败", message);
            }

            @Override
            public Result<UserInfo> getUserByName(String name) {
                return Result.fail("用户详细信息获取失败", message);
            }
        };
    }
}