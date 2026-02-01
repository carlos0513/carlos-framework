package com.carlos.oauth2.util;

import com.carlos.core.auth.UserContext;
import com.carlos.oauth2.constant.OAuth2Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OAuth2工具类
 * 用于从JWT Token中提取用户信息
 *
 * @author carlos
 * @date 2026-01-25
 */
@Slf4j
public class OAuth2Util {

    /**
     * 获取当前认证信息
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前JWT Token
     */
    public static Jwt getCurrentJwt() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getToken();
        }
        return null;
    }

    /**
     * 从JWT中提取用户上下文
     */
    public static UserContext extractUserContext() {
        Jwt jwt = getCurrentJwt();
        if (jwt == null) {
            return null;
        }

        UserContext userContext = new UserContext();

        // 提取用户ID
        Long userId = jwt.getClaim(OAuth2Constant.Claims.USER_ID);
        userContext.setUserId(userId);

        // 提取用户名
        String userName = jwt.getClaim(OAuth2Constant.Claims.USER_NAME);
        userContext.setAccount(userName);

        // 提取租户ID
        String tenantId = jwt.getClaim(OAuth2Constant.Claims.TENANT_ID);
        userContext.setClientId(tenantId);

        // 提取部门ID
        Long deptId = jwt.getClaim(OAuth2Constant.Claims.DEPT_ID);
        userContext.setDepartmentId(deptId);

        // 提取角色ID列表
        List<?> roleIdsList = jwt.getClaim(OAuth2Constant.Claims.ROLE_IDS);
        if (roleIdsList != null) {
            Set<Serializable> roleIds = new HashSet<>();
            for (Object roleId : roleIdsList) {
                if (roleId instanceof Serializable) {
                    roleIds.add((Serializable) roleId);
                }
            }
            userContext.setRoleIds(roleIds);
        }

        // 提取Token
        userContext.setToken(jwt.getTokenValue());

        return userContext;
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        Jwt jwt = getCurrentJwt();
        if (jwt == null) {
            return null;
        }
        return jwt.getClaim(OAuth2Constant.Claims.USER_ID);
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUserName() {
        Jwt jwt = getCurrentJwt();
        if (jwt == null) {
            return null;
        }
        String userName = jwt.getClaim(OAuth2Constant.Claims.USER_NAME);
        if (userName != null) {
            return userName;
        }
        return jwt.getSubject();
    }

    /**
     * 获取当前租户ID
     */
    public static String getCurrentTenantId() {
        Jwt jwt = getCurrentJwt();
        if (jwt == null) {
            return null;
        }
        return jwt.getClaim(OAuth2Constant.Claims.TENANT_ID);
    }

    /**
     * 获取当前部门ID
     */
    public static Long getCurrentDeptId() {
        Jwt jwt = getCurrentJwt();
        if (jwt == null) {
            return null;
        }
        return jwt.getClaim(OAuth2Constant.Claims.DEPT_ID);
    }

    /**
     * 获取当前用户角色ID列表
     */
    public static Set<Serializable> getCurrentRoleIds() {
        Jwt jwt = getCurrentJwt();
        if (jwt == null) {
            return null;
        }
        List<?> roleIdsList = jwt.getClaim(OAuth2Constant.Claims.ROLE_IDS);
        if (roleIdsList == null) {
            return null;
        }
        Set<Serializable> roleIds = new HashSet<>();
        for (Object roleId : roleIdsList) {
            if (roleId instanceof Serializable) {
                roleIds.add((Serializable) roleId);
            }
        }
        return roleIds;
    }

    /**
     * 获取当前用户权限列表
     */
    public static List<String> getCurrentAuthorities() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    /**
     * 判断当前用户是否有指定权限
     */
    public static boolean hasAuthority(String authority) {
        List<String> authorities = getCurrentAuthorities();
        return authorities != null && authorities.contains(authority);
    }

    /**
     * 判断当前用户是否有指定角色
     */
    public static boolean hasRole(String role) {
        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return hasAuthority(roleWithPrefix);
    }

    /**
     * 判断当前用户是否有任意一个指定权限
     */
    public static boolean hasAnyAuthority(String... authorities) {
        List<String> currentAuthorities = getCurrentAuthorities();
        if (currentAuthorities == null) {
            return false;
        }
        for (String authority : authorities) {
            if (currentAuthorities.contains(authority)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前用户是否有任意一个指定角色
     */
    public static boolean hasAnyRole(String... roles) {
        String[] rolesWithPrefix = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            rolesWithPrefix[i] = roles[i].startsWith("ROLE_") ? roles[i] : "ROLE_" + roles[i];
        }
        return hasAnyAuthority(rolesWithPrefix);
    }
}
