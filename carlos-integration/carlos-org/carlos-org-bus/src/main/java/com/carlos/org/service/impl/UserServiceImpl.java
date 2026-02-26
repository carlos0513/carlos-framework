package com.carlos.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.carlos.boot.request.RequestInfo;
import com.carlos.boot.request.RequestUtil;
import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.base.RegionInfo;
import com.carlos.core.base.UserInfo;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.core.response.Result;
import com.carlos.core.util.ExecutorUtil;
import com.carlos.org.UserUtil;
import com.carlos.org.config.OrgConstant;
import com.carlos.org.config.OrgProperties;
import com.carlos.org.convert.UserConvert;
import com.carlos.org.enums.SmsMessageEnum;
import com.carlos.org.login.PasswordConvertUtil;
import com.carlos.org.login.pojo.enums.SmsCodeTypeEnum;
import com.carlos.org.login.service.AuthService;
import com.carlos.org.manager.UserDepartmentManager;
import com.carlos.org.manager.UserManager;
import com.carlos.org.pojo.ao.UserLoginAO;
import com.carlos.org.pojo.dto.*;
import com.carlos.org.pojo.emuns.OrgUserDockingOperateTypeEnum;
import com.carlos.org.pojo.entity.User;
import com.carlos.org.pojo.enums.OrgDockingTypeEnum;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.carlos.org.pojo.excel.UserExcel;
import com.carlos.org.pojo.excel.UserPageExcel;
import com.carlos.org.pojo.excel.UserRegionInitExcel;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.param.UserDeptRoleDTO;
import com.carlos.org.pojo.vo.DepartmentBaseVO;
import com.carlos.org.pojo.vo.UserPageVO;
import com.carlos.org.pojo.vo.UserSessionVO;
import com.carlos.org.service.*;
import com.carlos.redis.util.RedisUtil;
import com.carlos.system.api.ApiFile;
import com.carlos.system.api.ApiMenu;
import com.carlos.system.api.ApiRegion;
import com.carlos.system.enums.MenuType;
import com.carlos.system.pojo.ao.FileInfoAO;
import com.carlos.system.pojo.ao.MenuAO;
import com.carlos.system.pojo.ao.SysRegionAO;
import com.carlos.util.easyexcel.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>
 * 系统用户 业务接口实现类
 * </p>
 *
 * @author carlos
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserManager userManager;
    private final AuthService authService;
    private final UserRoleService userRoleService;
    private final RoleService roleService;
    private final DepartmentRoleService departmentRoleService;
    private final DepartmentService departmentService;
    private final RoleMenuService roleMenuService;

    private final SmsCodeService smsCodeService;
    private final OrgDockingMappingService dockingMappingService;
    private final SmsMessageService smsMessageService;

    private final OrgProperties orgProperties;

    private final UserDepartmentService userDepartmentService;

    ThreadPoolExecutor pool = ExecutorUtil.get(2, 6, "user-create", 100, null);


    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO addUser(UserDTO dto) {
        log.info("new user info {}", dto);
        // 校验账户是否重复
        int count = this.userManager.getCountByAccount(dto.getAccount());
        log.debug("已存在相同用户名数量为{}个", count);
        if (count > 0) {
            throw new ServiceException("账号已存在!");
        }
        // 校验手机号是否重复
        int phoneCount = this.userManager.getCountByPhoneExcludeId(dto.getPhone(), null);
        log.debug("已存在相同手机号数量为{}个", phoneCount);
        if (phoneCount > 0) {
            throw new ServiceException("手机号已存在！!");
        }

        String tempPwd = dto.getPwd();
        // 如果没有密码生成默认密码
        if (CharSequenceUtil.isBlank(dto.getPwd())) {
            // 如果已配置默认密码 则使用用户 账号+配置密码作为新密码
            String defaultPassword = orgProperties.getDefaultPassword();
            if (StrUtil.isNotBlank(defaultPassword)) {
                tempPwd = defaultPassword;
                dto.setPwd(authService.encodePassword(Base64.encode(defaultPassword)));
            }
        } else {
            String pwd = PasswordConvertUtil.sm2ToBase64(dto.getPwd());
            tempPwd = Base64.decodeStr(pwd);
            dto.setPwd(this.authService.encodePassword(pwd));
        }
        dto.setPasswordCreateTime(LocalDateTime.now());
        dto.setState(UserStateEnum.ENABLE);
        dto.setAdmin(false);
        boolean success = this.userManager.add(dto);

        // 如果存在部门角色信息存在,添加用户角色,部门数据
        if (CollectionUtil.isNotEmpty(dto.getDeptRoles())) {
            this.userDepartmentService.addRelationByUserId(dto.getId(), dto.getDeptRoles());
        }

        if (!success) {
            // 保存失败的应对措施
            throw new ServiceException("新增用户失败！");
        }

        // 异步发送短信，不影响主流程
        String finalTempPwd = tempPwd;
        pool.submit(() -> {
            sendCreateUserMsg(dto, finalTempPwd);
        });


        // 保存完成的后续业务
        return dto;
    }

    private void sendCreateUserMsg(UserDTO dto, String finalTempPwd) {
        try {
            // 发送创建用户短信
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            // 需要对username和account进行判断，如果是纯数字，则需要进行脱敏处理
            String userName = dto.getRealname();
            String account = dto.getAccount();
            if (NumberUtil.isNumber(userName) && userName.length() == 11) {
                userName = CharSequenceUtil.hide(userName, 3, userName.length() - 4);
            }
            if (NumberUtil.isNumber(account) && account.length() == 11) {
                account = CharSequenceUtil.hide(account, 3, account.length() - 4);
            }
            map.put("username", userName);
            map.put("account", account);
            map.put("password", finalTempPwd);
            smsMessageService.sendSms(dto.getPhone(), SmsMessageEnum.CREATE_USER.getTemplateCode(), map);
        } catch (Exception e) {
            log.error("send create user sms failed, phone: {}, account: {}", dto.getPhone(), dto.getAccount(), e);
        }
    }


    @Override
    public void dockingUser(UserDTO user, OrgDockingMappingDTO mapping, OrgUserDockingOperateTypeEnum operateType) {
        if (StrUtil.isBlank(mapping.getTargetId())) {
            throw new ServiceException("targetId can't be null！");
        }
        if (StrUtil.isBlank(mapping.getTargetCode())) {
            throw new ServiceException("targetCode can't be null！");
        }
        OrgDockingTypeEnum dockingType = OrgDockingTypeEnum.USER;
        mapping.setDockingType(dockingType);
        OrgDockingMappingDTO dto;
        switch (operateType) {
            case ADD:
                user = addUser(user);
                String id = user.getId();
                mapping.setSystemId(id);
                dockingMappingService.addOrgDockingMapping(mapping);
                break;
            case UPDATE:
                // 根据信息找到系统id
                dto = dockingMappingService.getDockingMapping(dockingType, mapping.getTargetCode(), mapping.getTargetId());
                user.setId(dto.getSystemId());
                updateUser(user);
                break;
            case ENABLE:
                dto = dockingMappingService.getDockingMapping(dockingType, mapping.getTargetCode(), mapping.getTargetId());
                changeState(dto.getSystemId(), UserStateEnum.ENABLE);
                break;
            case DISABLE:
                dto = dockingMappingService.getDockingMapping(dockingType, mapping.getTargetCode(), mapping.getTargetId());
                changeState(dto.getSystemId(), UserStateEnum.DISABLE);
                break;
            case DELETE:
            case SIGN_OFF:
                dto = dockingMappingService.getDockingMapping(dockingType, mapping.getTargetCode(), mapping.getTargetId());
                deleteUser(Collections.singleton(dto.getSystemId()));
                // 解除绑定
                dockingMappingService.deleteOrgDockingMapping(Collections.singleton(dto.getId()));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + operateType);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Set<Serializable> ids) {
        // FIXME: Carlos 2023/11/20 将删除用户设置为注销用户
        // for (Serializable id : ids) {
        //     boolean success = this.userManager.delete(id);
        //     if (!success) {
        //         // 删除失败的措施
        //         return;
        //     }
        //     // 删除用户部门关联关系
        //     this.userRoleService.deleteByUserId(String.valueOf(id));
        //
        //     // 删除用户和角色关联关系
        //     this.userDepartmentService.deleteByUserId(String.valueOf(id));
        // }

/*        // 检查用户下是否存在任务和表单
        for (Serializable id : ids) {
            List<UserDepartmentDTO> existDepts = userDepartmentService.getByUserId(id.toString());
            Set<String> deptCodes = existDepts.stream().map(UserDepartmentDTO::getDepartmentCode).collect(Collectors.toSet());
            checkUserTaskAndForm(id.toString(), deptCodes);
        }*/

        writeOffUser(ids);
    }

    @Override
    public void writeOffUser(Set<Serializable> ids) {
        log.info("user will be written off: {}", ids);
        boolean b = userManager.writterOff(ids);
        if (!b) {
            log.info("user  written off failed: {}", ids);
        }
        log.info("user has be written off: {}", ids);
//        删除关系
        ids.stream().map(String::valueOf).forEach(userDepartmentService::deleteByUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserDTO dto) {
        UserDTO user = this.userManager.getUserByAccount(dto.getAccount());
        if (user != null && !Objects.equals(user.getId(), dto.getId())) {
            throw new ServiceException("账号已存在！");
        }

        // 校验手机号是否重复
        int phoneCount = this.userManager.getCountByPhoneExcludeId(dto.getPhone(), dto.getId());
        log.debug("已存在相同手机号数量为{}个", phoneCount);
        if (phoneCount > 0) {
            throw new ServiceException("手机号已存在！!");
        }

/*        List<UserDepartmentDTO> existDepts = userDepartmentService.getByUserId(user.getId());
        // 若存在组织机构和角色调整, 则检查用户下是否存在任务和表单
        if (ifExistOrgRoleChange(dto.getDeptRoles(), existDepts)) {
            Set<String> deptCodes = existDepts.stream().map(UserDepartmentDTO::getDepartmentCode).collect(Collectors.toSet());
            checkUserTaskAndForm(dto.getId(), deptCodes);
        }*/

        boolean success = this.userManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return false;
        }

        // 修改成功的后续操作
        // 如果存在角色信息存在添加用户角色数据
        if (CollectionUtil.isNotEmpty(dto.getDeptRoles())) {
            // 删除历史信息
            this.userDepartmentService.deleteByUserId(dto.getId());
            // 保存用户部门 角色信息
            this.userDepartmentService.addRelationByUserId(dto.getId(), dto.getDeptRoles());
        }


        // 缓存regionCode
        RequestInfo requestInfo = RequestUtil.getRequestInfo();
        UserContext userContext = requestInfo.getUserContext();
        UserDTO userRedis = RedisUtil.getValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, userContext.getToken()), UserDTO.class);
        RedisUtil.setValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, userContext.getToken()), userRedis);
        return true;
    }

    /**
     * 是否存在组织机构和角色调整
     */
    private Boolean ifExistOrgRoleChange(List<UserDeptRoleDTO> deptRoles, List<UserDepartmentDTO> existDeptRoles) {

        if (!Objects.equals(existDeptRoles.size(), deptRoles.size())) {
            return true;
        }

        // 判断用户是否存在部门和角色的变更
        Map<String, UserDeptRoleDTO> newDeptRoleMap = deptRoles.stream().collect(Collectors.toMap(UserDeptRoleDTO::getDepartmentId, Function.identity(), (key1, key2) -> key2));
        for (UserDepartmentDTO userDepartmentDTO : existDeptRoles) {
            UserDeptRoleDTO newDeptRole = newDeptRoleMap.get(userDepartmentDTO.getDepartmentId());
            // 不存在该部门角色信息 则表示存在变更
            if (newDeptRole == null) {
                return true;
                // 角色信息不一致，或部门层级不一致 则表示存在变更
            } else if (!Objects.equals(userDepartmentDTO.getRoleId(), newDeptRole.getRoleId())
                    || !Objects.equals(userDepartmentDTO.getDepartmentLevelCode(), newDeptRole.getDepartmentType())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public UserInfo getUserInfo(Serializable userId) {
        UserDTO dto = this.userManager.getDtoById(userId);
        return UserConvert.INSTANCE.toUser(dto);
    }

    @Override
    public UserDTO getUserByAccount(String account) {
        if (StrUtil.isBlank(account)) {
            return null;
        }
        UserDTO user = this.userManager.getUserByAccount(account);
        if (user == null) {
            return null;
        }
        String head = user.getHead();
        if (StrUtil.isNotBlank(head)) {
            try {
                ApiFile apiFile = SpringUtil.getBean(ApiFile.class);
                Result<FileInfoAO> Result = apiFile.getFile(head);
                Optional.ofNullable(Result).ifPresent(i -> {
                    if (Boolean.FALSE.equals(i.getSuccess())) {
                        log.error("Get file info fail:{}", i.getMessage());
                    }
                    Optional.ofNullable(i.getData()).ifPresent(data -> {
                        user.setHead(data.getUrl());
                    });
                });
            } catch (Exception e) {
                log.error("文件获取失败：id:{}", head, e);
                user.setHead(null);
            }
        }
        return user;
    }


    @Override
    public UserDTO getUserByCredentials(String credential) {
        if (StrUtil.isBlank(credential)) {
            return null;
        }
        UserDTO user = this.userManager.getUserByCredentials(credential);
        if (user == null) {
            return null;
        }
        String head = user.getHead();
        if (StrUtil.isNotBlank(head)) {
            try {
                ApiFile apiFile = SpringUtil.getBean(ApiFile.class);
                Result<FileInfoAO> Result = apiFile.getFile(head);
                Optional.ofNullable(Result).ifPresent(i -> {
                    if (Boolean.FALSE.equals(i.getSuccess())) {
                        log.error("Get file info fail:{}", i.getMessage());
                    }
                    Optional.ofNullable(i.getData()).ifPresent(data -> {
                        user.setHead(data.getUrl());
                    });
                });
            } catch (Exception e) {
                log.error("文件获取失败：id:{}", head, e);
                user.setHead(null);
            }
        }
        return user;
    }


    @Override
    public UserDTO getBaseInfo(String id, boolean roleInfo) {
        if (StrUtil.isBlank(id)) {
            return null;
        }
        UserDTO user = this.userManager.getDtoById(id);
        if (user == null) {
            return null;
        }
        if (StrUtil.isNotBlank(user.getHead())) {
            user.setHead(getFileUrl(user.getHead()));
        }
        if (StrUtil.isNotBlank(user.getSignature())) {
            // 签名字段数据存储格式https://carlos.minyisuban.com:40102/bbt-api/sys/file/load?id=2025051917221924395122177945600
            user.setSignature(getFileUrl(user.getSignature().split("=")[1]));
        }
        if (roleInfo) {
            Set<String> userRoleIds = this.userDepartmentService.getRoleIdsByUserId(user.getId());
            if (CollectionUtil.isNotEmpty(userRoleIds)) {
                List<RoleDTO> roles = roleService.getRoleByIds(userRoleIds);
                List<String> collect = roles.stream().map(RoleDTO::getName).collect(Collectors.toList());
                user.setRoleName(StrUtil.join(StrUtil.COMMA, collect));
                // 设置所属部门信息
                List<DepartmentDTO> departmentDTOS = userDepartmentService.getDepartmentsByUserId(user.getId());
                user.setDepartments(departmentDTOS);
            }
        }
        return user;
    }


    @Override
    public String getFileUrl(String fileId) {

        ApiFile api = SpringUtil.getBean(ApiFile.class);
        Result<FileInfoAO> result = null;
        try {
            result = api.getFileStreamInfo(fileId);
        } catch (Exception e) {
            log.error("文件信息读取失败！异常信息：{}", e.getMessage());
            return fileId;

        }
        if (result == null || !result.getSuccess()) {
            log.error("文件读取失败！");
            return fileId;
        }
        FileInfoAO file = result.getData();

        if (file != null) {
            String suffix = FileUtil.getSuffix(file.getName());

            // 常见图片格式
            if ("jpg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix) || "gif".equalsIgnoreCase(suffix) || "webp".equalsIgnoreCase(suffix) || "svg".equalsIgnoreCase(suffix)) {

                if (suffix.equalsIgnoreCase("svg")) {
                    suffix = "svg+xml";
                } else if (suffix.equalsIgnoreCase("jpg")) {
                    suffix = "jpeg";
                }
                return buildPicture(suffix, file.getBytes());
            }
        }
        return null;
    }

    private String buildPicture(String suffix, byte[] bytes) {
        return "data:image/" + suffix.toLowerCase() + ";base64," + Base64.encode(bytes);
    }

    @Override
    public String getPhoneByUserId(String userId) {
        return this.userManager.getPhoneByUserId(userId);
    }

    @Override
    public UserDTO getUserByPhone(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return null;
        }
        UserDTO user = null;
        try {
            user = this.userManager.getUserByPhone(mobile);
        } catch (Exception e) {
            log.error("get user info failed, phone:{}", mobile, e);
            throw new ServiceException("用户信息获取出错");
        }
        if (user == null) {
            return null;
        }
        if (StrUtil.isNotBlank(user.getHead())) {
            ApiFile api = SpringUtil.getBean(ApiFile.class);
            Result<FileInfoAO> result = null;
            try {
                result = api.getFile(user.getHead());
                if (result.getSuccess()) {
                    FileInfoAO file = result.getData();
                    if (file != null) {
                        user.setHead(file.getUrl());
                    }
                } else {
                    log.warn("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
                }
            } catch (Exception e) {
                log.warn("Api request failed,", e);
            }
        }
        return user;
    }

    @Override
    public List<UserRoleDTO> getUserRoles(String account) {
        UserDTO user = this.userManager.getUserByAccount(account);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        List<UserDeptRoleDTO> deptRolesByUserId = this.userDepartmentService.getDeptRolesByUserId(user.getId());
        if (CollectionUtil.isEmpty(deptRolesByUserId)) {
            throw new ServiceException("用户未分配部门及角色");
        }
        List<UserRoleDTO> res = new ArrayList<>();
        deptRolesByUserId.forEach(i -> {
            if (i.getRoleId() != null) {
                RoleDTO role = this.roleService.getById(i.getRoleId());
                UserRoleDTO userRole = new UserRoleDTO();
                userRole.setRoleCode(role.getCode());
                userRole.setRoleName(role.getName());
                userRole.setRoleId(role.getId());
                res.add(userRole);
            }
        });

        return res;
    }

    @Override
    public UserDTO getCurrentUser(MenuType menuType) {
        RequestInfo requestInfo = RequestUtil.getRequestInfo();
        UserContext userContext = requestInfo.getUserContext();
        if (userContext == null) {
            throw new ServiceException("用户信息获取失败");
        }

        UserDTO user = RedisUtil.getValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, userContext.getToken()), UserDTO.class);
        if (user == null) {
            throw new ServiceException("认证已过期，请重新登录");
        }

        // Serializable userId = ExtendInfoUtil.getUserId();
        // 获取用户角色信息
        List<UserRoleDTO> userRoles = getUserRoles(user.getAccount());
        Set<String> roleIds = userRoles.stream().map(UserRoleDTO::getRoleId).collect(Collectors.toSet());
        user.setRoleIds(roleIds);
        List<String> roleNames = userRoles.stream().map(UserRoleDTO::getRoleName).collect(Collectors.toList());
        user.setRoleName(StringUtils.join(roleNames, StrUtil.COMMA));
        // List<RoleDTO> roles = roleService.getByIds(roleIds);
        // 获取用户所属部门
        List<DepartmentDTO> department = departmentService.getByUserId(user.getId());
        user.setDepartments(department);

        // 获取角色菜单
        List<RoleMenuDTO> roleMenus = roleMenuService.getByRoleIds(roleIds);
        if (CollUtil.isNotEmpty(roleMenus)) {
            Set<String> menuIds = roleMenus.stream().map(RoleMenuDTO::getMenuId).collect(Collectors.toSet());
            ApiMenu api = SpringUtil.getBean(ApiMenu.class);
            Result<List<MenuAO>> result = api.listMenus(menuIds);
            if (!result.getSuccess()) {
                log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
                throw new ServiceException(result.getMessage());
            }

            List<MenuAO> menus = result.getData();

            // 根据入参筛选pc端或移动端菜单
            menus = menus.stream().filter(menu -> menuType == menu.getMenuType() && menu.getState()).collect(Collectors.toList());
            // 菜单树形整理 分组 排序
            Map<String, List<MenuAO>> map = menus.stream().collect(Collectors.groupingBy(MenuAO::getParentId));
            List<UserMenuDTO> menuDTO = handleMenus(map, roleIds);
            user.setMenus(menuDTO);
        } else {
            log.error("menu list is empty, role ids = {}", roleIds);
            throw new ServiceException("当前登录用户角色未分配菜单");
        }

        // 当登录过后生成部门信息，则获取当前部门角色信息，用于获取当前权限组
        String deptId = "";
        try {
            deptId = UserUtil.getDepartment().getId();
        } catch (Exception e) {
            log.warn("当前未生成部门");
        }
        if (StringUtils.isNotEmpty(deptId)) {
            List<UserDepartmentDTO> userdeptList = userDepartmentService.getByUserIdAndDeptId(user.getId(), deptId);
            if (CollUtil.isNotEmpty(userdeptList)) {
                List<String> currentRoleIds = userdeptList.stream().map(UserDepartmentDTO::getRoleId).collect(Collectors.toList());
                roleIds.clear();
                roleIds.addAll(currentRoleIds);
            }
        }
        return user;
    }

    private List<UserMenuDTO> handleMenus(String parentId, Map<String, List<MenuAO>> map, Set<String> roleIds) {
        if (MapUtil.isEmpty(map)) {
            return Collections.emptyList();
        }
        List<MenuAO> menuDTOS = map.get(parentId == null ? 0L : parentId);
        if (menuDTOS == null) {
            return Collections.emptyList();
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
            UserMenuDTO userMenu = new UserMenuDTO().setMeta(metaMap).setName(menuDTO.getName()).setPath(menuDTO.getPath()).setRoles(
                    roleIds).setComponent(menuDTO.getComponent());
            String id = menuDTO.getId();
            userMenu.setChildren(handleMenus(id, map, roleIds));
            list.add(userMenu);
        }
        return list;
    }

    private List<UserMenuDTO> handleMenus(Map<String, List<MenuAO>> map, Set<String> roleIds) {
        List<UserMenuDTO> list = new LinkedList<>();
        map.keySet().forEach(parentId -> {
            if (StrUtil.isNotBlank(parentId)) {
                List<MenuAO> menuDTOS = map.get(parentId);
                menuDTOS = menuDTOS.stream().sorted(Comparator.comparingInt(MenuAO::getSort)).collect(Collectors.toList());
                if (parentId.equalsIgnoreCase("0")) {
                    for (MenuAO menuDTO : menuDTOS) {
                        if (list.stream().map(UserMenuDTO::getId).collect(Collectors.toList()).contains(menuDTO.getId())) {
                            continue;
                        }
                        UserMenuDTO userMenuDTO = this.toUserMenuDTO(menuDTO, roleIds);
                        list.add(userMenuDTO);
                    }
                    return;
                }
                // List<MenuAO> menu = menuService.getByIds(Collections.singleton(parentId));
                List<UserMenuDTO> children = new ArrayList<>();
                for (MenuAO menuDTO : menuDTOS) {
                    UserMenuDTO userMenuDTO = this.toUserMenuDTO(menuDTO, roleIds);
                    children.add(userMenuDTO);
                }
                if (list.stream().map(UserMenuDTO::getId).collect(Collectors.toList()).contains(parentId)) {
                    list.stream().filter(userMenuDTO -> userMenuDTO.getId().equals(parentId)).findFirst().ifPresent(menuDTO -> {
                        menuDTO.setChildren(children);
                    });
                } else {
                    ApiMenu api = SpringUtil.getBean(ApiMenu.class);
                    Result<List<MenuAO>> resultList = api.listMenus(Collections.singleton(parentId));
                    if (!resultList.getSuccess()) {
                        log.error("Api request failed, message: {}, detail message:{}", resultList.getMessage(), resultList.getStack());
                        throw new ServiceException(resultList.getMessage());
                    }
                    List<MenuAO> menu = resultList.getData();
                    if (CollectionUtil.isEmpty(menu)) {
                        return;
                    }
                    MenuAO parent = menu.get(0);
                    UserMenuDTO result = this.toUserMenuDTO(parent, roleIds);
                    result.setChildren(children);
                    list.add(result);
                }
            }
        });
        return list.stream().sorted(Comparator.comparingInt(UserMenuDTO::getSort)).collect(Collectors.toList());
    }

    private UserMenuDTO toUserMenuDTO(MenuAO menuDTO, Set<String> roleIds) {
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
        UserMenuDTO userMenu = new UserMenuDTO().setId(menuDTO.getId()).setMeta(metaMap).setHidden(menuDTO.getHidden()).setName(menuDTO.getName()).setPath(
                menuDTO.getPath()).setRoles(roleIds).setIcon(menuDTO.getIcon()).setState(menuDTO.getState()).setSort(menuDTO.getSort()).setComponent(
                menuDTO.getComponent()).setUrl(menuDTO.getUrl());
        return userMenu;
    }

    @Override
    public List<UserDTO> getUserByRoleIds(Set<String> roleIds) {
        List<UserDepartmentDTO> userRoles = this.userDepartmentService.getByRoleIds(roleIds);
        if (CollUtil.isEmpty(userRoles)) {
            log.warn("Role {} not relate any user", roleIds);
            return Collections.emptyList();
        }
        return this.userManager.getByIds(userRoles.stream().map(UserDepartmentDTO::getUserId).collect(Collectors.toSet()));
    }

    @Override
    public boolean changePwd(UserChangePwdParam param) {
        // 旧密码判空
        if (StrUtil.isBlank(param.getPwdOld())) {
            throw new ServiceException("请输入旧密码!");
        }
        UserDTO dtoById = this.userManager.getDtoById(param.getId());
        if (null == dtoById) {
            throw new ServiceException("该用户不存在！");
        }
        // 旧密码校验
        if (!this.authService.checkPassword(PasswordConvertUtil.sm2ToBase64(param.getPwdOld()), dtoById.getPwd())) {
            throw new ServiceException("原密码错误!");
        }
        // if (!param.getPwdNew().matches(PW_PATTERN)) {
        //    throw new ServiceException("密码必须包含大小写字母、数字、特殊符号且长度为12~16");
        //}
        String encodePassword = this.authService.encodePassword(PasswordConvertUtil.sm2ToBase64(param.getPwdNew()));

        dtoById.setPwd(encodePassword);
        // 重置密码后重新生成密码创建时间
        dtoById.setPasswordCreateTime(LocalDateTime.now());
        boolean success = this.userManager.modify(dtoById);
        if (!success) {
            // 更改密码失败处理
        }
        return success;
    }

    @Override
    public boolean forceChangedPwd(UserChangePwdParam param) {
        RequestInfo requestInfo = RequestUtil.getRequestInfo();
        UserContext userContext = requestInfo.getUserContext();
        if (userContext == null) {
            throw new ServiceException("登录信息获取失败！");
        }
        // 对应登录，手机号和账号都可以进行处理
        if (!(userContext.getAccount().equals(param.getAccount()) || userContext.getPhone().equals(param.getAccount()))) {
            throw new ServiceException("账号不匹配！");
        }
        // 根据用户凭证信息获取
        UserDTO dtoById = this.userManager.getUserByCredentials(param.getAccount());
        if (dtoById == null) {
            throw new ServiceException("该用户不存在！");
        }
        String encodePassword = this.authService.encodePassword(PasswordConvertUtil.sm2ToBase64(param.getPwdNew()));
        dtoById.setPwd(encodePassword);
        dtoById.setLoginCount(dtoById.getLoginCount() + 1);
        dtoById.setLastLogin(LocalDateTime.now());
        // 重置密码后重新生成密码创建时间
        dtoById.setPasswordCreateTime(LocalDateTime.now());
        boolean success = this.userManager.modify(dtoById);
        if (!success) {
            // 更改密码失败处理
        }
        return success;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserInfo(UserInfoUpdateParam param) {
        UserDTO dtoById = this.userManager.getDtoById(param.getId());
        if (null == dtoById) {
            throw new ServiceException("该用户不存在！");
        }
        // 校验账号是否重复
        UserDTO userDTO = this.userManager.getUserByAccount(param.getAccount());
        if (userDTO != null && !Objects.equals(userDTO.getId(), param.getId())) {
            throw new ServiceException("账号已存在！");
        }
        // 校验手机号是否重复
        int phoneCount = this.userManager.getCountByPhoneExcludeId(param.getPhone(), param.getId());
        log.debug("已存在相同手机号数量为{}个", phoneCount);
        if (phoneCount > 0) {
            throw new ServiceException("手机号已存在！!");
        }
        dtoById.setRealname(param.getRealname());
        dtoById.setAccount(param.getAccount());
        dtoById.setPhone(param.getPhone());
        dtoById.setDescription(param.getDescription());
        dtoById.setGender(param.getGender());
        dtoById.setEmail(param.getEmail());
        Optional.ofNullable(param.getHead()).ifPresent(i -> dtoById.setHead(i.getId()));
        dtoById.setSignature(param.getSignature());

        boolean isSuccess = this.userManager.modify(dtoById);
        UserContext userContext = ExtendInfoUtil.getUserContext();
        String token = userContext.getToken();
        if (isSuccess && userContext.getUserId().equals(dtoById.getId())) {
            String key = String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, token);
            UserDTO user = RedisUtil.getValue(key);
            Optional.ofNullable(param.getRealname()).ifPresent(user::setRealname);
            Optional.ofNullable(param.getPhone()).ifPresent(user::setPhone);
            Optional.ofNullable(param.getDescription()).ifPresent(user::setDescription);
            Optional.ofNullable(param.getGender()).ifPresent(user::setGender);
            Optional.ofNullable(param.getEmail()).ifPresent(user::setEmail);
            Optional.ofNullable(param.getAccount()).ifPresent(user::setAccount);
            Optional.ofNullable(param.getHead()).ifPresent(i -> user.setHead(i.getId()));
            RedisUtil.setValue(key, user);
        }
        return isSuccess;
    }

    @Override
    public List<UserSessionVO> getUserByDeptId(Serializable departmentId) {
        if (departmentId == null) {
            return null;
        }
        Set<String> userIds = this.userDepartmentService.getUserIdByDepartmentId(String.valueOf(departmentId));
        if (CollectionUtil.isEmpty(userIds)) {
            return null;
        }

        List<UserDTO> users = this.userManager.getByIds(userIds);
        if (CollectionUtil.isEmpty(users)) {
            return null;
        }


        DepartmentDTO department = this.departmentService.getDepartmentById(String.valueOf(departmentId));
        return users.stream().map(i -> {
            UserSessionVO vo = UserConvert.INSTANCE.toVO(i);
            if (department != null) {
                vo.setDepartmentInfo(BeanUtil.copyProperties(department, DepartmentBaseVO.class));
            }
            return vo;
        }).collect(Collectors.toList());
    }


    @Override
    public UserDTO getUserById(String id, boolean getDeptRoles) {
        UserDTO user = this.userManager.getDtoById(id);
        user.setRegionCode(UserUtil.getRegionCode());
        if (getDeptRoles) {
            List<UserDeptRoleDTO> deptRolesByUserId = this.userDepartmentService.getDeptRolesByUserId(id);
            user.setDeptRoles(deptRolesByUserId);
            // 返回所有父级部门（包括自己）
            deptRolesByUserId.forEach(i -> {
                i.setAllParentIds(departmentService.getAllParentDepartmentIdsById(i.getDepartmentId()));
            });
        }

        return user;
    }

    @Override
    public UserDTO selectUserById(String id) {
        return this.userManager.getDtoById(id);
    }

    @Override
    public void resetPassword(String id, String pwd) {
        UserDTO user = this.userManager.getDtoById(id);
        if (user == null) {
            log.error("user not found:{}", id);
            throw new ServiceException("用户id错误，重置密码失败！");
        }
        String encodePassword = this.authService.encodePassword(PasswordConvertUtil.sm2ToBase64(pwd));
        user.setPwd(encodePassword);
        // 重置密码后将login_count 重置为0，用户使用重置密码第一次登录是要求强制修改密码
        user.setLoginCount(0L);
        // 重置密码后重新生成密码创建时间
        user.setPasswordCreateTime(LocalDateTime.now());
        boolean success = this.userManager.modify(user);
        if (!success) {
            // 修改失败操作
            log.error("reset password failed");
            throw new ServiceException("重置密码失败！");
        }
        log.info("reset user password success, id:{} pwd:{}", id, pwd);
    }


    @Override
    public void export(HttpServletResponse response, boolean isTemplate) {
        // 表格标题，就是模型的属性名
        String name = "用户--" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);

        List<UserExcel> data = Lists.newArrayList();
        if (!isTemplate) {
            // 获取数据
            List<UserDTO> list = userManager.listAll();
            data = UserConvert.INSTANCE.dtoToexcel(list);
        }
        try {
            ExcelUtil.download(response, name, UserExcel.class, data);
        } catch (Exception e) {
            throw new ServiceException("用户信息导出失败");
        }
    }

    @Override
    public void deleteAll() {
        boolean success = this.userManager.deleteAll();
        if (!success) {
            // 清空失败操作
            log.error("deleted All failed");
            throw new ServiceException("清空用户数据失败！");
        }
    }

    @Override
    public void forgetPassword(UserForgetPwdParam param) {
        // 验证短信验证码
        smsCodeService.checkSmsCode(param.getCode(), param.getPhone(), SmsCodeTypeEnum.UPDATE_PWD);
        // 根据手机号查询用户
        UserDTO user = userManager.getUserByPhone(param.getPhone());
        if (user == null) {
            throw new ServiceException("用户不存在！");
        }
        String s1 = PasswordConvertUtil.sm2ToBase64(param.getPwd());
        String s2 = PasswordConvertUtil.sm2ToBase64(param.getPwd2());
        // 判断两次密码是否一致
        if (!s1.equals(s2)) {
            throw new ServiceException("两次输入密码不一致");
        }
        user.setPwd(authService.encodePassword(s1));
        // 重置密码后重新生成密码创建时间
        user.setPasswordCreateTime(LocalDateTime.now());
        userManager.modify(user);
    }

    @Override
    public List<UserDTO> list(String keyword, Set<String> deptLevels) {
        List<UserDTO> result = new ArrayList<>();
        List<UserDTO> users = userManager.listByKeyword(keyword);
        if (CollUtil.isEmpty(users)) {
            log.info("根据关键词查:{}用户列表为空", keyword);
            return Collections.emptyList();
        }
        Map<String, List<UserDepartmentDTO>> userDepts;
        if (CollectionUtil.isNotEmpty(deptLevels)) {
            List<UserDepartmentDTO> userDepartments = userDepartmentService.getByLevels(deptLevels);
            // 取交集
            Set<String> userIds = userDepartments.parallelStream().map(UserDepartmentDTO::getUserId).collect(Collectors.toSet());
            users = users.stream().filter(i -> userIds.contains(i.getId())).collect(Collectors.toList());
            if (CollUtil.isEmpty(users)) {
                log.info("根据关键词查:{},层级:{}过滤后,用户列表为空", keyword, deptLevels);
                return Collections.emptyList();
            }
            // 最终用户的id
            Set<String> finalUserIds = users.parallelStream().map(UserDTO::getId).collect(Collectors.toSet());
            userDepts = userDepartments.parallelStream().filter(i -> finalUserIds.contains(i.getUserId())).collect(Collectors.groupingBy(UserDepartmentDTO::getUserId));

        } else {
            userDepts = userDepartmentService.getAllRef().parallelStream().collect(Collectors.groupingBy(UserDepartmentDTO::getUserId));
        }
        List<DepartmentDTO> departmentDTOS = departmentService.getDepartments();
        Map<String, DepartmentDTO> allDept = departmentDTOS.parallelStream().collect(Collectors.toMap(DepartmentDTO::getId, Function.identity()));
        // 查用户部门,填充
        users.parallelStream().forEachOrdered(i -> {
            List<UserDepartmentDTO> userDeptIds = userDepts.get(i.getId());

            if (userDeptIds.size() == 1) {
                String next = userDeptIds.iterator().next().getDepartmentId();
                DepartmentDTO e = allDept.get(next);
                i.setDepartmentId(e.getId()).setDepartmentName(e.getDeptName()).setDepartmentLevelCode(e.getDepartmentLevelCode());
                result.add(i);
            } else {
                userDeptIds.forEach(e -> {
                    UserDTO userDTO = BeanUtil.copyProperties(i, UserDTO.class);
                    DepartmentDTO departmentDTO = allDept.get(e.getDepartmentId());
                    userDTO.setDepartmentId(departmentDTO.getId()).setDepartmentName(departmentDTO.getDeptName()).setDepartmentLevelCode(departmentDTO.getDepartmentLevelCode());
                    result.add(userDTO);
                });
            }
        });
        return result;

    }

    @Override
    public List<UserDTO> completeMatchList(String keyword) {
        return userManager.listWithKeyword(keyword);
    }

    @Override
    public void changeState(String id, UserStateEnum state) {
        UserDTO user = new UserDTO().setId(id).setState(state);
        userManager.modify(user);
        log.info("用户状态切换成功： id: {}  state:{}", id, state.getDesc());
    }

    @Override
    public Paging<UserPageVO> listAuthLimit(UserPageParam param) {
        // 只查用户当前部门及以下的的用户
        UserLoginAO user = UserUtil.getUser();
        UserLoginAO.Department department = user.getDepartment();
        param.setDeptCode(department.getDeptCode());
        return userManager.getPage(param);
    }

    @Override
    public void export(UserPageParam param, HttpServletResponse response) {
        String name = "用户--" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        UserLoginAO.Department department = UserUtil.getDepartment();
        param.setDeptCode(department.getDeptCode());
        List<UserPageExcel> pageExcel = userManager.getParamAll(param);

        try {
            ExcelUtil.download(response, name, UserPageExcel.class, pageExcel);
        } catch (Exception e) {
            throw new ServiceException("用户信息导出失败");
        }
    }

    @Override
    public void sendCreateMsg(Set<String> phones) {
        List<UserDTO> userByPhones = userManager.getUserByPhones(phones);
        if (CollUtil.isEmpty(userByPhones)) {
            return;
        }
        for (UserDTO user : userByPhones) {
            String pwd = Base64.decodeStr(authService.decodePassword(user.getPwd()));
            sendCreateUserMsg(user, pwd);
        }
    }

    @Override
    public List<UserDTO> listByIds(Set<String> ids) {
        return userManager.listById(ids);
    }


    @Override
    public List<UserDTO> getAllUser() {
        List<UserDTO> users = userManager.listAll();

        List<UserDepartmentDTO> refs = this.userDepartmentService.getAllRef();
        Map<String, List<UserDepartmentDTO>> refGroups = refs.stream().collect(Collectors.groupingBy(UserDepartmentDTO::getUserId));
        List<UserDTO> list = new LinkedList<>();
        for (UserDTO user : users) {
            String id = user.getId();
            List<UserDepartmentDTO> userDepartments = refGroups.get(id);
            if (CollUtil.isEmpty(userDepartments)) {
                continue;
            }
            user.setDepartmentIds(userDepartments.stream().map(UserDepartmentDTO::getDepartmentId).collect(Collectors.toSet()));
            list.add(user);
        }
        return list;
    }

    @Override
    public String getDefaultPwd(String id) {
        UserDTO user = userManager.getDtoById(id);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        // 默认密码进行base64加密
        String defaultPassword = RandomUtil.randomString(12);
        return Base64.encode(defaultPassword);
    }

    @Override
    public void changeRegin(String regionName) {

        ThreadPoolExecutor pool = ExecutorUtil.get(2, 6, "user-region-init", 1000, null);
        // 获取所有的部门及用
        UserDepartmentManager userDepartmentManager = SpringUtil.getBean(UserDepartmentManager.class);
        List<UserDepartmentDTO> refs = userDepartmentManager.listAllRef();
        Map<String, UserDepartmentDTO> users = refs.stream()
                .collect(Collectors.toMap(UserDepartmentDTO::getUserId, i -> i, (k1, k2) -> {
                            if (k1.getDepartmentCode().contains(k2.getDepartmentCode())) {
                                return k2;
                            }
                            return k1;
                        })
                );

        // 获取所有区域
        ApiRegion api = SpringUtil.getBean(ApiRegion.class);
        Result<List<SysRegionAO>> result = api.all();
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        //
        ConcurrentHashMap<String, RegionInfo> map = new ConcurrentHashMap<>(4);
        List<SysRegionAO> regions = result.getData();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        List<Future<?>> submits = new ArrayList<>();
        for (SysRegionAO region : regions) {

            Future<?> submit = pool.submit(() -> {
                Result<RegionInfo> regionInfo = api.getRegionInfo(region.getRegionCode(), 20);
                if (!regionInfo.getSuccess()) {
                    log.error("Api request failed, message: {}, detail message:{}", regionInfo.getMessage(), regionInfo.getStack());
                    throw new ServiceException(regionInfo.getMessage());
                }
                RegionInfo data = regionInfo.getData();
                List<String> names = data.getFullName();
                String fullName = StrUtil.subAfter(StrUtil.join(StrUtil.DASHED, names), regionName, false);
                fullName = regionName + fullName;
                map.put(fullName, data);
                log.info("num:{}", atomicInteger.addAndGet(1));
            });
            submits.add(submit);
        }
        for (Future<?> submit : submits) {
            try {
                submit.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }


        // 获取所有区域
        List<UserRegionInitExcel> inits = new ArrayList<>();
        List<User> us = new ArrayList<>();
        for (UserDepartmentDTO i : users.values()) {
            User user = new User();
            user.setId(i.getUserId());
            String departmentId = i.getDepartmentId();
            List<String> list = departmentService.previewDepartmentName(departmentId, 20);
            String fullDeptName = StrUtil.join(StrUtil.DASHED, list);
            RegionInfo regionInfo = map.get(fullDeptName);
            UserRegionInitExcel excel = new UserRegionInitExcel();
            excel.setId(i.getUserId());
            excel.setAccount(i.getAccount());
            excel.setRealname(i.getRealname());
            excel.setPhone(i.getPhone());
            excel.setRegionCode("");
            excel.setDeptCode(i.getDepartmentCode());
            excel.setDeptName(i.getDepartmentName());
            if (regionInfo == null) {
                excel.setSuccess(false);
                // 未匹配到区域
                log.error("未匹配到区域 userId:{}, userName:{} deptName:{}", i.getUserId(), i.getRealname(), fullDeptName);
            } else {
                excel.setRegionCode(regionInfo.getCode());
                excel.setRegionName(StrUtil.subAfter(StrUtil.join(StrUtil.DASHED, regionInfo.getFullName()), regionName, false));
                excel.setSuccess(true);
//                user.setRegionCode(regionInfo.getCode());
            }
            us.add(user);
            inits.add(excel);
        }

        userManager.updateBatchById(us);
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter("/data/region/user_region_init_" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".xlsx");
        writer.write(inits);
        writer.flush();
        writer.close();
    }

    @Override
    public void resetUserDeptRole() {
        // 查所有用户部门关系
        List<UserDepartmentDTO> refs = userDepartmentService.listAll();
//        按照用户-部门层级-分组
        Map<String, Map<String, List<UserDepartmentDTO>>> userDept = refs.stream().filter(i -> StrUtil.isNotBlank(i.getDepartmentLevelCode()))
                .collect(Collectors.groupingBy(UserDepartmentDTO::getUserId,
                        Collectors.groupingBy(UserDepartmentDTO::getDepartmentLevelCode, Collectors.toList())));
        // 所有部门层级和角色关系
        List<DepartmentRoleDTO> departmentRoles = departmentRoleService.listAll();
        Map<String, Set<String>> levelRoles = departmentRoles.stream().collect(Collectors.groupingBy(DepartmentRoleDTO::getDepartmentType,
                Collectors.mapping(DepartmentRoleDTO::getRoleId, Collectors.toSet())));
        List<UserDepartmentDTO> res = new ArrayList<>();
        userDept.forEach((userId, depts) -> {
            // 查用户历史有的角色
            Set<String> roleIdsByUserId = userRoleService.getRoleIdsByUserId(userId);
            depts.forEach((level, roles) -> {
                Set<String> deptRole = levelRoles.get(level);
                if (CollUtil.isNotEmpty(deptRole)) {
                    // 如果用户有这个层级的角色，则则更新入userdept表
                    List<String> collect = roleIdsByUserId.stream()
                            .filter(deptRole::contains)
                            .collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(collect)) {
                        // 创建 Random 对象
                        Random random = new Random();
                        // 生成随机索引
                        int randomIndex = random.nextInt(collect.size());
                        List<UserDepartmentDTO> collect1 = roles.stream()
                                .filter(e -> e.getRoleId() == null || !deptRole.contains(e.getRoleId()))
                                .map(e -> e.setRoleId(collect.get(randomIndex)))
                                .collect(Collectors.toList());
                        res.addAll(collect1);
                    }
                } else {
                    log.info("当前部门层级:{},未配置角色信息", level);
                }
            });
        });
        // 批量更新
        userDepartmentService.batchUpdateUserDepartment(res);
    }

    @Override
    public UserDTO getUserOrgInfo(String deptCode, String userId) {
        UserDTO user = userManager.getDtoById(userId);
        if (user == null) {
            return null;
        }
        DepartmentInfo departmentInfo = departmentService.getDepartmentInfo(deptCode, 0);
        if (departmentInfo != null) {
            user.setDepartmentId(departmentInfo.getId().toString());
            user.setDepartmentName(departmentInfo.getName());
            user.setDeptFullNames(departmentInfo.getFullName());

            List<UserDepartmentDTO> userDept = userDepartmentService.getByUserIdAndDeptId(userId, departmentInfo.getId().toString());
            if (CollUtil.isNotEmpty(userDept)) {
                UserDepartmentDTO dto = userDept.get(0);
                user.setRoleId(dto.getRoleId());
                RoleDTO role = roleService.getById(dto.getRoleId());
                Optional.ofNullable(role).ifPresent(i -> user.setRoleName(i.getName()));
            }

        }
        return user;
    }
}
