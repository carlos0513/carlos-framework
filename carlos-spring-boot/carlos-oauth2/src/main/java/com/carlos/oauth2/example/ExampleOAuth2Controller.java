package com.carlos.oauth2.example;

import com.carlos.core.auth.UserContext;
import com.carlos.core.response.Result;
import com.carlos.oauth2.util.OAuth2Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * OAuth2使用示例Controller
 *
 * @author yunjin
 * @date 2026-01-25
 */
@Slf4j
@RestController
@RequestMapping("/api/oauth2/example")
public class ExampleOAuth2Controller {

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current-user")
    public Result<UserContext> getCurrentUser() {
        UserContext userContext = OAuth2Util.extractUserContext();
        return Result.ok(userContext);
    }

    /**
     * 获取当前用户详细信息
     */
    @GetMapping("/user-info")
    public Result<Map<String, Object>> getUserInfo() {
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("userId", OAuth2Util.getCurrentUserId());
        userInfo.put("userName", OAuth2Util.getCurrentUserName());
        userInfo.put("tenantId", OAuth2Util.getCurrentTenantId());
        userInfo.put("deptId", OAuth2Util.getCurrentDeptId());
        userInfo.put("roleIds", OAuth2Util.getCurrentRoleIds());
        userInfo.put("authorities", OAuth2Util.getCurrentAuthorities());

        return Result.ok(userInfo);
    }

    /**
     * 需要ADMIN角色才能访问
     */
    @PreAuthorize("hasRole('1')")
    @GetMapping("/admin-only")
    public Result<String> adminOnly() {
        return Result.ok("This is admin only endpoint");
    }

    /**
     * 需要特定权限才能访问
     */
    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/with-permission")
    public Result<String> withPermission() {
        return Result.ok("This endpoint requires user:read permission");
    }

    /**
     * 需要任意一个角色
     */
    @PreAuthorize("hasAnyRole('1', '2', '3')")
    @GetMapping("/any-role")
    public Result<String> anyRole() {
        Set<Serializable> roleIds = OAuth2Util.getCurrentRoleIds();
        return Result.ok("Current user has roles: " + roleIds);
    }

    /**
     * 公开接口，不需要认证
     */
    @GetMapping("/public")
    public Result<String> publicEndpoint() {
        return Result.ok("This is a public endpoint");
    }

    /**
     * 检查权限示例
     */
    @GetMapping("/check-permission")
    public Result<Map<String, Boolean>> checkPermission() {
        Map<String, Boolean> permissions = new HashMap<>();

        permissions.put("hasRole1", OAuth2Util.hasRole("1"));
        permissions.put("hasRole2", OAuth2Util.hasRole("2"));
        permissions.put("hasAnyRole", OAuth2Util.hasAnyRole("1", "2", "3"));
        permissions.put("hasUserReadAuthority", OAuth2Util.hasAuthority("user:read"));
        permissions.put("hasUserWriteAuthority", OAuth2Util.hasAuthority("user:write"));

        return Result.ok(permissions);
    }
}
