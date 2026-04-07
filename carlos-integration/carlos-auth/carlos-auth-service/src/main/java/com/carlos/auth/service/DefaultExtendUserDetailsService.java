package com.carlos.auth.service;

import com.carlos.core.auth.LoginUserInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * <p><strong>警告：此类仅用于开发和测试！</strong></p>
 *
 * <p>该实现仅在内存中创建一个测试用户，不连接任何数据库或外部存储。
 * 生产环境必须实现 {@link ExtendUserDetailsService} 接口提供自定义实现。</p>
 *
 * <h3>默认测试用户：</h3>
 * <table border="1">
 *   <tr><th>属性</th><th>值</th></tr>
 *   <tr><td>用户名</td><td>admin</td></tr>
 *   <tr><td>密码</td><td>admin123</td></tr>
 *   <tr><td>用户ID</td><td>1</td></tr>
 *   <tr><td>角色</td><td>ADMIN, USER</td></tr>
 *   <tr><td>状态</td><td>启用</td></tr>
 * </table>
 *
 * <h3>生产环境配置示例：</h3>
 * <pre>{@code
 * @Service
 * @Primary  // 确保优先使用自定义实现
 * public class MyUserDetailsService implements ExtendUserDetailsService {
 *
 *     @Autowired
 *     private UserRepository userRepository;
 *
 *     @Override
 *     public UserDetails loadUserByUsername(String username) {
 *         // 从数据库加载用户
 *         User user = userRepository.findByUsername(username)
 *             .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
 *
 *         // 转换为 UserDetails
 *         return convertToUserDetails(user);
 *     }
 *
 *     @Override
 *     public LoginUserInfo loadLoginUserInfo(String username) {
 *         // 从数据库加载用户业务信息
 *         return userRepository.findLoginUserInfoByUsername(username);
 *     }
 * }
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 * @see ExtendUserDetailsService
 */
@Slf4j
public class DefaultExtendUserDetailsService implements ExtendUserDetailsService {

    /**
     * 内存中的测试用户存储
     */
    private final Set<TestUser> testUsers = new HashSet<>();

    /**
     * 密码编码器
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 构造函数 - 初始化测试用户
     *
     * @param passwordEncoder 密码编码器
     */
    public DefaultExtendUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        initTestUsers();

        log.warn("=================================================================");
        log.warn(" Using DefaultExtendUserDetailsService - FOR DEVELOPMENT ONLY!   ");
        log.warn("=================================================================");
        log.warn("Default test user created:");
        log.warn("  Username: admin");
        log.warn("  Password: admin123");
        log.warn("");
        log.warn("Please implement ExtendUserDetailsService for production use!");
        log.warn("=================================================================");
    }

    /**
     * 初始化测试用户
     */
    private void initTestUsers() {
        // 管理员用户
        TestUser admin = new TestUser();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEnabled(true);
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.setRoleIds(new HashSet<>(Collections.singletonList(1L)));
        testUsers.add(admin);

        // 普通用户
        TestUser user = new TestUser();
        user.setId(2L);
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRoleIds(new HashSet<>(Collections.singletonList(2L)));
        testUsers.add(user);
    }

    /**
     * 根据用户名加载用户信息
     *
     * @param username 用户名
     * @return UserDetails 用户详情
     * @throws UsernameNotFoundException 用户不存在时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TestUser user = findUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoleIds().stream()
                .map(String::valueOf)
                .toArray(String[]::new))
            .accountExpired(!user.isAccountNonExpired())
            .accountLocked(!user.isAccountNonLocked())
            .credentialsExpired(!user.isCredentialsNonExpired())
            .disabled(!user.isEnabled())
            .build();
    }

    /**
     * 加载用户业务信息
     *
     * @param username 用户名
     * @return LoginUserInfo 登录用户信息
     */
    @Override
    public LoginUserInfo loadLoginUserInfo(String username) {
        TestUser user = findUser(username);
        if (user == null) {
            return null;
        }

        LoginUserInfo info = new LoginUserInfo();
        info.setId(user.getId());
        info.setAccount(user.getUsername());
        info.setPassword(user.getPassword());
        info.setEnable(user.isEnabled());
        info.setRoleIds(new HashSet<>(user.getRoleIds()));
        info.setClientId(null); // 单租户场景
        info.setDepartmentId(null);

        return info;
    }

    /**
     * 查找用户
     *
     * @param username 用户名
     * @return TestUser 测试用户，不存在返回 null
     */
    private TestUser findUser(String username) {
        return testUsers.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

    /**
     * 测试用户内部类
     */
    @Data
    private static class TestUser {
        private Long id;
        private String username;
        private String password;
        private boolean enabled;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private Set<Long> roleIds;
    }
}
