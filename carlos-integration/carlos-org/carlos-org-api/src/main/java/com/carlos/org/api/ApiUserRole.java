package com.carlos.org.api;

import com.carlos.core.response.Result;
import com.carlos.org.ServiceNameConstant;
import com.carlos.org.fallback.FeignUserFallbackFactory;
import com.carlos.org.pojo.ao.UserRoleAO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 系统用户 feign 提供接口
 * </p>
 *
 * @author carlos
 * @date 2021-12-20 14:07:16
 */
@FeignClient(value = ServiceNameConstant.USER, contextId = "userRole", path = "/api/org/user/role", fallbackFactory = FeignUserFallbackFactory.class)
public interface ApiUserRole {


    /**
     * @Title: allUserRole
     * @Description: 获取所有用户角色对应信息
     * @Date: 2023/11/27 11:39
     * @Parameters: []
     * @Return com.carlos.core.response.Result<java.util.List < com.carlos.org.pojo.ao.UserRoleAO>>
     */
    @GetMapping("allUserRole")
    Result<List<UserRoleAO>> allUserRole();

    /**
     * 获取用户角色
     *
     * @param userIds 用户ID集合
     * @return 用户角色
     */
    @PostMapping("getUserRole")
    Result<List<UserRoleAO>> getUserRole(@RequestBody List<String> userIds);

    /**
     * 根据用户ID查询角色名称
     * @param userId
     * @return
     */
    @GetMapping("{userId}")
    Result<List<String>> getUserRoleNameByUserId(@PathVariable(value = "userId") String userId);
}
