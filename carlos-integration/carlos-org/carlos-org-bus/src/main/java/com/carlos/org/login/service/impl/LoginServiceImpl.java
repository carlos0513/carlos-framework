package com.carlos.org.login.service.impl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.carlos.boot.request.RequestInfo;
import com.carlos.boot.request.RequestUtil;
import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.boot.util.ResponseUtil;
import com.carlos.core.auth.AuthConstant;
import com.carlos.core.auth.Oauth2TokenDTO;
import com.carlos.core.auth.UserContext;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.Result;
import com.carlos.encrypt.EncryptUtil;
import com.carlos.org.UserUtil;
import com.carlos.org.config.MenuApiMappingProperties;
import com.carlos.org.config.OrgConstant;
import com.carlos.org.config.OrgProperties;
import com.carlos.org.config.RandoGemNumberGnerator;
import com.carlos.org.login.LoginThreadLocal;
import com.carlos.org.login.PasswordConvertUtil;
import com.carlos.org.login.ThridLogin;
import com.carlos.org.login.pojo.LoginCodeUserVO;
import com.carlos.org.login.pojo.LoginSuccessVO;
import com.carlos.org.login.pojo.LoginVerifyVO;
import com.carlos.org.login.pojo.enums.SmsCodeTypeEnum;
import com.carlos.org.login.pojo.enums.ThirdLoginType;
import com.carlos.org.login.pojo.param.LoginParam;
import com.carlos.org.login.pojo.param.ThirdLoginParam;
import com.carlos.org.login.service.AuthService;
import com.carlos.org.login.service.LoginService;
import com.carlos.org.manager.UserDepartmentManager;
import com.carlos.org.manager.UserManager;
import com.carlos.org.pojo.dto.*;
import com.carlos.org.pojo.enums.LoginType;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.carlos.org.service.*;
import com.carlos.redis.util.RedisUtil;
import com.carlos.system.api.ApiMenu;
import com.carlos.system.api.ApiSystemConfig;
import com.carlos.system.pojo.ao.MenuAO;
import com.carlos.system.pojo.ao.SysConfigAO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 登录服务实现类
 * </p>
 *
 * @author carlos
 * @date 2019-05-23
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String AUTH = "Authorization";
    private static final String VERIFY_KEY = "user:verify:%s";
    private final AuthService authService;
    private final UserService userService;
    private final RoleService roleService;
    private final DepartmentService departmentService;
    private final RoleMenuService roleMenuService;
    private final UserDepartmentService userDepartmentService;
    private final UserManager userManager;
    private final SmsCodeService smsCodeService;
    private final MenuApiMappingProperties menuApiMappingProperties;
    private final OrgProperties orgProperties;
    private static final String LOGIN_FAIL_COUNT_KEY = "user:login:fail:count:%s"; // 登录失败次数
    private static final String LOGIN_LOCK_KEY = "user:login:lock:%s"; // 登录锁定状态
    private final static RandoGemNumberGnerator GEM_NUMBER_GNERATOR = new RandoGemNumberGnerator(4);

    @Override
    public LoginSuccessVO login(LoginParam loginParam) {
        // 检查验证码
        checkVerify(loginParam);

        // 检查登录参数
        UserDTO user = checkLoginInfo(loginParam);

        return doLogin(user, loginParam);
    }

    private LoginSuccessVO doLogin(UserDTO user, LoginParam loginParam) {
        // 检查用户状态
        checkUserStatus(user);
        // 校验密码并生成 token 信息
        Oauth2TokenDTO tokenInfo = verifyAuthInfo(loginParam, user);
        // 获取用户角色信息
        List<UserRoleDTO> userRoles = userService.getUserRoles(user.getAccount());

        Set<String> roleIds = userRoles.stream().map(UserRoleDTO::getRoleId).collect(Collectors.toSet());
        user.setRoleIds(roleIds);
        List<String> roleNames = userRoles.stream().map(UserRoleDTO::getRoleName).collect(Collectors.toList());
        user.setRoleName(StringUtils.join(roleNames, StrUtil.COMMA));

        Set<String> departmentId = userDepartmentService.getDepartmentIdByUserId(user.getId());
        UserContext context = new UserContext();

        // 单部门用户直接缓存部门信息
        if (CollUtil.isNotEmpty(departmentId) && departmentId.size() == 1) {
            String deptId = departmentId.stream().findAny().orElse(null);
            DepartmentDTO department = departmentService.getDepartmentById(deptId);
            user.setDepartmentInfo(department);
            context.setDepartmentId(deptId);

            UserDepartmentManager userDepartmentManager = SpringUtil.getBean(UserDepartmentManager.class);
            UserDepartmentDTO userDepartmentDTO = userDepartmentManager.getByUserIdAndDeptId(user.getId(), deptId);
            user.setRoleId(userDepartmentDTO.getRoleId());
        }
        // 获取部门信息
        List<DepartmentDTO> departments = departmentService.listByIds(departmentId);
        user.setDepartments(departments);

        // 获取菜单信息
        List<RoleMenuDTO> roleMenus = roleMenuService.getByRoleIds(roleIds);
        if (CollUtil.isNotEmpty(roleMenus)) {
            Set<String> menuIds = roleMenus.stream().map(RoleMenuDTO::getMenuId).collect(Collectors.toSet());
            ApiMenu api = SpringUtil.getBean(ApiMenu.class);
            Result<List<MenuAO>> result = api.listMenus(menuIds);
            if (!result.getSuccess()) {
                log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
                throw new ServiceException(result.getMessage());
            }
            Map<String, List<MenuApiMappingProperties.MenuApiMapping>> mappings = menuApiMappingProperties.getMenuApiMappings();

            List<MenuAO> menus = result.getData();
            if (CollUtil.isNotEmpty(menus)) {
                List<UserMenuDTO> collect = menus.stream().map(i -> {
                    UserMenuDTO item = new UserMenuDTO();
                    item.setId(i.getId());
                    item.setPath(i.getPath());
                    item.setName(i.getName());
                    item.setComponent(i.getComponent());
                    item.setHidden(i.getHidden());
                    item.setState(i.getState());
                    item.setIcon(i.getIcon());
                    item.setSort(i.getSort());
                    if (CollUtil.isNotEmpty(mappings)) {
                        List<MenuApiMappingProperties.MenuApiMapping> apis = mappings.get(i.getPath());
                        if (CollUtil.isEmpty(apis)) {
                            List<SysApiDTO> collect1 = apis.stream().map(j -> {
                                SysApiDTO d = new SysApiDTO();
                                d.setId(j.getApiId());
                                d.setUrl(j.getApiUrl());
                                return d;
                            }).collect(Collectors.toList());
                            item.setApis(collect1);
                        }
                    }
                    return item;
                }).collect(Collectors.toList());
                RedisUtil.setValue(String.format(OrgConstant.LOGIN_USER_MENU_CACHE, tokenInfo.getToken()), JSONUtil.toJsonStr(collect), tokenInfo.getExpiresIn());
                user.setMenus(collect);
            }
        }

        // 缓存token 用户校验token
        // 设置用户信息缓存，缓存失效时间=token有效时间
        user.setLastLogin(LocalDateTime.now());
        RedisUtil.setValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, tokenInfo.getToken()), user, tokenInfo.getExpiresIn());

        context.setAccount(user.getAccount());
        context.setToken(tokenInfo.getToken());
        context.setUserId(user.getId());
        context.setPhone(user.getPhone());
        context.setRoleIds(userRoles.stream().map(UserRoleDTO::getRoleId).collect(Collectors.toSet()));
        context.setDepartmentIds(new HashSet<>(departmentId));
        RedisUtil.setValue(String.format(OrgConstant.LOGIN_USER_CONTEXT_CACHE, tokenInfo.getToken()), context, tokenInfo.getExpiresIn());

        LoginSuccessVO success = new LoginSuccessVO();
        success.setName(AUTH);
        success.setToken(tokenInfo.getToken());
        success.setRoutePath(LoginThreadLocal.getRoute());
        // 首次登录强制修改密码
        if (user.getLoginCount() == 0 && loginParam.getLoginType() == LoginType.PASSWORD) {
            log.warn("user phone:{} need reset password", user.getPhone());
            success.setFirstLogin(true);
            return success;
        }

        // 判断密码是否过期
        if (loginParam.getLoginType() == LoginType.PASSWORD) {
            boolean isPasswordExpired = isIsPasswordExpired(user.getPasswordCreateTime() == null ? LocalDateTime.now() : user.getPasswordCreateTime());
            if (isPasswordExpired) {
                log.warn("user phone:{} password expired", user.getPhone());
                // 强制修改密码
                success.setFirstLogin(true);
                return success;
            }
        }

        HttpServletResponse response = ResponseUtil.getResponse();
        response.setHeader(AuthConstant.USER_ACCOUNT, context.getAccount());
        response.setHeader(AuthConstant.USER_ID, context.getUserId().toString());

        UserDTO updateInfo = new UserDTO();
        updateInfo.setId(user.getId());
        updateInfo.setLastLogin(user.getLastLogin());

        if (loginParam.getLoginType() == LoginType.PASSWORD) {
            updateInfo.setLoginCount(user.getLoginCount() + 1);
        }
        // 更新用户登录时间和登录次数
        userManager.modify(updateInfo);
        log.info("login success: id：{}  name:{}  phone:{}", user.getId(), user.getRealname(), user.getPhone());
        return success;
    }

    private void checkUserStatus(UserDTO user) {
        if (user.getState() != UserStateEnum.ENABLE) {
            throw new ServiceException("用户已禁用，无法登录！");
        }
        // 检查用户
        String lockKey = String.format(LOGIN_LOCK_KEY, user.getId());
        if (RedisUtil.hasKey(lockKey)) {
            Long expire = RedisUtil.getExpire(lockKey);
            String dateStr = DateUtil.formatBetween(expire * 1000, BetweenFormatter.Level.SECOND);
            throw new ServiceException("用户已锁定，请" + dateStr + "后再试！");
        }
    }

    /**
     * 密码是否过期
     *
     * @param passwordCreateTime 密码创建时间
     * @return b
     */
    private boolean isIsPasswordExpired(LocalDateTime passwordCreateTime) {
        // 获取密码过期配置时间
        OrgProperties orgProperties = SpringUtil.getBean(OrgProperties.class);
        Duration passwordExpire = orgProperties.getPasswordExpire();
        long timeInterval = passwordExpire.toMinutes();
        // 获取密码创建时间
        long time = Duration.between(passwordCreateTime, LocalDateTime.now()).toMinutes();

        // 比较间隔天数是否大于配置的过期天数
        return time > timeInterval;
    }

    @Override
    public LoginSuccessVO thirdLogin(ThirdLoginParam loginParam) {
        ThridLogin thridLogin = ThirdLoginType.getLogin(loginParam.getLoginType());
        // 获取用户手机号
        // TODO 根据手机号统一登录
        String mobile = thridLogin.login(loginParam.getParam());
        // String mobile = code;
        if (StrUtil.isBlank(mobile)) {
            throw new ServiceException("未获取到用户手机号");
        }
        UserDTO user = userService.getUserByPhone(mobile);
        if (user == null) {
            throw new ServiceException("用户未注册");
        }
        LoginParam login = new LoginParam();
        login.setAccount(mobile);
        login.setLoginType(LoginType.PHONE);
        return doLogin(user, login);

    }

    @Override
    public boolean choiceUserDepartment(String departmentId) {
//        获取当前登录用户信息上下文
        UserContext userContext = ExtendInfoUtil.getUserContext();
        assert ObjectUtils.isNotEmpty(userContext);
//        查询当前用户所有部门
        UserDTO user = RedisUtil.getValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, userContext.getToken()));
        DepartmentDTO dto = user.getDepartments().stream().filter(o -> departmentId.equals(o.getId())).findAny().orElse(null);
//        仅当前用户部门选择有效
        if (ObjectUtils.isNotEmpty(dto)) {
//            缓存中获取token信息
//            设置用户部门信息 刷新缓存
            user.setDepartmentInfo(dto);
            user.setRegionCode(dto.getRegionCode());
            userContext.setDepartmentId(departmentId);
            UserDepartmentManager userDepartmentManager = SpringUtil.getBean(UserDepartmentManager.class);
            UserDepartmentDTO userDepartmentDTO = userDepartmentManager.getByUserIdAndDeptId(user.getId(), departmentId);
            user.setRoleId(userDepartmentDTO.getRoleId());

            RedisUtil.setValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, userContext.getToken()), user);
            RedisUtil.setValue(String.format(OrgConstant.LOGIN_USER_CONTEXT_CACHE, userContext.getToken()), userContext);
            return true;
        }
        return false;
    }

    @Override
    public void getSmsCode(String phone, String verifyToken, String code, SmsCodeTypeEnum verifyType) {
        // 校验图形验证码
        this.checkVerify(new LoginParam(verifyToken, code));
        // 发送短信之前 校验用户是否存在
        int count = userManager.getCountByPhoneExcludeId(phone, null);
        if (count == 0) {
            throw new ServiceException("该手机号未绑定相关用户信息");
        }
        if (count > 1) {
            throw new ServiceException("该手机号绑定了多个用户，请联系管理员处理");
        }
        smsCodeService.sendCode(phone, verifyType);
    }

    @Override
    public LoginVerifyVO getVerify(Integer width, Integer height, Integer codeCount) {
        if (width == null) {
            width = 150;
        }
        if (height == null) {
            height = 50;
        }
        if (codeCount == null) {
            codeCount = 4;
        }
        AbstractCaptcha captcha = new LineCaptcha(width, height, GEM_NUMBER_GNERATOR, 60);
        String imageBase64 = captcha.getImageBase64Data();
        String code = captcha.getCode();
        String token = IdUtil.randomUUID();
        RedisUtil.setValue(String.format(VERIFY_KEY, token), code, 5, TimeUnit.MINUTES);
        LoginVerifyVO verify = new LoginVerifyVO().setVerifyToken(token).setImage(imageBase64);
        return verify;
    }

    private void checkVerify(LoginParam loginParam) {
        String verifyToken = loginParam.getVerifyToken();
        String code = loginParam.getCode();
        // 短信登录检查短信验证码
        if (LoginType.SMS_CODE.name().equals(loginParam.getLoginType().name())) {
            smsCodeService.checkSmsCode(code, loginParam.getAccount(), SmsCodeTypeEnum.LOGIN);
        } else if (StrUtil.isNotBlank(verifyToken)) {
            String key = String.format(VERIFY_KEY, verifyToken);
            if (StrUtil.isBlank(code)) {
                throw new ServiceException("验证码不能为空");
            }
            String value = RedisUtil.getValue(key);
            try {
                if (value == null || !value.equalsIgnoreCase(code)) {
                    throw new ServiceException("验证码不正确");
                }
            } catch (ServiceException e) {
                throw new ServiceException(e.getMessage(), e);
            } finally {
                RedisUtil.delete(key);
            }
        }

    }

    /**
     * 验证用户登录信息
     *
     * @param loginParam 登录参数
     * @param user       用户信息
     * @return com.carlos.core.auth.Oauth2TokenDTO
     * @author Carlos
     * @date 2023/3/31 15:57
     */
    private Oauth2TokenDTO verifyAuthInfo(LoginParam loginParam, UserDTO user) {
        String userId = user.getId();
        LoginType loginType = loginParam.getLoginType();
        String account = loginParam.getAccount();
        switch (loginType) {
            case PASSWORD:
                if (!user.getPwd().equals(authService.encodePassword(PasswordConvertUtil.sm2ToBase64(loginParam.getPassword())))) {
                    // 密码错误，记录失败次数
                    int remainingAttempts = this.getRemainingAttempts(userId);
                    log.error("Failed to authenticate since password does not match stored value");
                    throw new ServiceException("账户或密码不正确，已输入" + (orgProperties.getLogin().getFailLimit() - remainingAttempts) + ",还剩" + remainingAttempts + "次尝试机会");
                }
                break;
            case PHONE:
                break;
            case EMAIL:
                break;
            case RZT:
                if (!loginParam.getAccount().equals(user.getPhone())) {

                }
                break;

            case BIG_LINKAGE:
                if (!loginParam.getAccount().equals(user.getPhone())) {

                }
                break;
            case EVENT:
                if (!loginParam.getAccount().equals(user.getPhone())) {

                }
                break;
            case DINGTALK:
                if (!loginParam.getAccount().equals(user.getPhone())) {

                }
                break;
            case SMS_CODE:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + loginType);
        }
        // 登录成功重置登录失败次数
        this.resetLoginFailCount(userId);
        Oauth2TokenDTO tokenInfo = new Oauth2TokenDTO();
        tokenInfo.setToken(IdUtil.randomUUID());
        tokenInfo.setTokenType("UUID");
        // 默认1天
        tokenInfo.setExpiresIn(60 * 60);
        return tokenInfo;
    }


    /**
     * 获取登录失败次数
     */
    public int getLoginFailCount(String userId) {
        String countKey = String.format(LOGIN_FAIL_COUNT_KEY, userId);
        OrgProperties.OrgLoginProperties loginProperties = orgProperties.getLogin();
        RedisUtil.incrementValue(countKey, 1);
        RedisUtil.setExpire(countKey, loginProperties.getCountIntervalTime());
        Integer count = RedisUtil.getValue(countKey);
        if (count == null) {
            // 登录次数的国企时间是24小时
            RedisUtil.setValue(countKey, 1, loginProperties.getCountIntervalTime());
        }
        if (count >= loginProperties.getFailLimit()) {
            String userState = String.format(LOGIN_LOCK_KEY, userId);
            RedisUtil.setValue(userState, userState, loginProperties.getLockTime() * 60); // setEx直接设置键值对和过期时间
            throw new ServiceException("账户被锁定，请" + loginProperties.getLockTime() + "分钟后再试");
        }
        return count == null ? 1 : count;
    }


    /**
     * 重置登录失败次数（登录成功时调用）
     */
    public void resetLoginFailCount(String account) {
        String countKey = String.format(LOGIN_FAIL_COUNT_KEY, account);
        String userState = String.format(LOGIN_LOCK_KEY, account);
        RedisUtil.delete(countKey);
        RedisUtil.delete(userState);
    }

    /**
     * 获取剩余尝试次数
     */
    public int getRemainingAttempts(String userId) {
        int failCount = getLoginFailCount(userId);
        return Math.max(0, (int) (orgProperties.getLogin().getFailLimit() - failCount));
    }

    @Override
    public UserDTO checkLoginInfo(LoginParam param) {
        String accout = param.getAccount();
        UserDTO user = userService.getUserByCredentials(accout);
        if (user == null) {
            throw new ServiceException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public void logout() {
        RequestInfo requestInfo = RequestUtil.getRequestInfo();
        UserContext context = requestInfo.getUserContext();
        Optional.of(context).ifPresent(i -> {

            RedisUtil.delete(String.format(OrgConstant.LOGIN_USER_CONTEXT_CACHE, i.getToken()));
            RedisUtil.delete(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, i.getToken()));
        });
    }


    @Override
    public UserDTO getLoginUserInfo(String account, String roleId) {
        // 获取用户基本信息
        UserDTO dto = userService.getUserByAccount(account);
        if (dto == null) {
            log.warn("User data don't exist, account:{}", account);
            throw new ServiceException("用户不存在!");
        }
        // 获取角色基本信息
        RoleDTO role = roleService.getById(roleId);
        dto.setRole(role);

        // 获取用户所属部门
        List<DepartmentDTO> department = departmentService.getByUserId(dto.getId());
        dto.setDepartments(department);

        // 获取角色菜单
        List<MenuAO> menus = roleMenuService.getByRoleId(roleId);
        // 菜单树形整理 分组 排序
        Map<String, List<MenuAO>> map = menus.stream().collect(Collectors.groupingBy(MenuAO::getParentId));

        List<UserMenuDTO> menuDTO = handleMenus("0", map, roleId);
        dto.setMenus(menuDTO);
        return dto;
    }


    private List<UserMenuDTO> handleMenus(String parentId, Map<String, List<MenuAO>> map, String roleId) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        List<MenuAO> menuDTOS = map.get(parentId == null ? 0L : parentId);
        if (menuDTOS == null) {
            return null;
        }
        menuDTOS = menuDTOS.stream().sorted(Comparator.comparingInt(MenuAO::getSort)).collect(Collectors.toList());
        List<UserMenuDTO> list = new LinkedList<>();
        for (MenuAO menuDTO : menuDTOS) {
            String meta = menuDTO.getMeta();
            Map<String, Object> metaMap = null;
            try {
                metaMap = JSONUtil.toBean(meta, Map.class);
            } catch (Exception e) {
                log.error("json 转换失败：{}", meta, e);
            }
            if (metaMap == null) {
                metaMap = new HashMap<>();
            }
            metaMap.put("title", menuDTO.getTitle());
            metaMap.put("icon", menuDTO.getIcon());
            metaMap.put("showLink", !menuDTO.getHidden());
            UserMenuDTO userMenu = new UserMenuDTO()
                    .setMeta(metaMap)
                    .setName(menuDTO.getName())
                    .setPath(menuDTO.getPath())
                    .setRoles(Sets.newHashSet(roleId))
                    .setComponent(menuDTO.getComponent());
            String id = menuDTO.getId();
            userMenu.setChildren(handleMenus(id, map, roleId));
            list.add(userMenu);
        }
        return list;
    }

    @Override
    public String sign() {
        RequestInfo requestInfo = RequestUtil.getRequestInfo();
        String ip = requestInfo.getIp();
        String timestamps = String.valueOf(DateUtil.currentSeconds());
        UserContext userContext = requestInfo.getUserContext();
        String token = userContext.getToken();
        ArrayList<String> params = Lists.newArrayList(ip, timestamps, token);
        String encrypt = EncryptUtil.encrypt(StrUtil.join(StrUtil.COMMA, params));
        return HexUtil.encodeHexStr(encrypt);
    }

    @Override
    public LoginCodeUserVO getUserByCode(String code) {
        if (StrUtil.isBlank(code)) {
            throw new ServiceException("code不能为空！");
        }
        UserDTO user = RedisUtil.getValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, code));
        if (user == null) {
            throw new ServiceException("无效code");
        }

        return new LoginCodeUserVO().setId(user.getId()).setAccount(user.getAccount()).setName(user.getRealname()).setPhone(user.getPhone());
    }

    @Override
    public Boolean loginCityFlag() {
        ApiSystemConfig sysConfigService = SpringUtil.getBean(ApiSystemConfig.class);
        Result<SysConfigAO> result = null;
        try {
            result = sysConfigService.getByCode("city-sso-roles");
        } catch (Exception e) {
            log.error("get sys config failed", e);
            return false;
        }
        if (Boolean.FALSE.equals(result.getSuccess())) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        SysConfigAO data = result.getData();
        if (data == null) {
            // 暂未此相关配置，返回false
            return false;
        }
        String configValue = data.getConfigValue();
        if (CharSequenceUtil.isBlank(configValue)) {
            // 暂未此相关配置，返回false
            return false;
        }
        Set<String> roleIds = UserUtil.getRoleId();
        if (CollUtil.isEmpty(roleIds)) {
            // 当前用户角色为空,返回false
            return false;
        }
        boolean isLogin = false;
        for (String roleId : roleIds) {
            isLogin = configValue.contains(roleId);
        }
        return isLogin;
    }
}
