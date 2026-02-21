package com.yunjin.org.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yunjin.core.base.RegionInfo;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.response.Result;
import com.yunjin.core.util.ExecutorUtil;
import com.yunjin.docking.tftd.result.TfDeptInfoResult;
import com.yunjin.docking.tftd.result.TfUserInfoResult;
import com.yunjin.excel.exception.ExcelException;
import com.yunjin.org.config.OrgProperties;
import com.yunjin.org.listener.UserExcelListener;
import com.yunjin.org.manager.*;
import com.yunjin.org.pojo.dto.*;
import com.yunjin.org.pojo.entity.UserDepartment;
import com.yunjin.org.pojo.entity.UserImport;
import com.yunjin.org.pojo.enums.UserGenderEnum;
import com.yunjin.org.pojo.excel.UserImportExcel;
import com.yunjin.org.pojo.param.UserDeptRoleDTO;
import com.yunjin.org.pojo.vo.UserImportCheckVO;
import com.yunjin.system.api.ApiDict;
import com.yunjin.system.api.ApiFile;
import com.yunjin.system.api.ApiRegion;
import com.yunjin.system.pojo.ao.DictItemAO;
import com.yunjin.system.pojo.ao.FileInfoAO;
import com.yunjin.system.pojo.ao.SysRegionAO;
import com.yunjin.system.pojo.ao.UploadResultAO;
import com.yunjin.system.pojo.param.ApiFileUploadParam;
import com.yunjin.system.pojo.param.ApiSysRegionAddParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLException;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户导入 业务接口实现类
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserImportService {

    private final UserImportManager userImportManager;
    private final UserService userService;
    private final UserManager userManager;
    private final RoleService roleService;
    private final DepartmentService departmentService;
    private final UserDepartmentManager userDepartmentManager;
    private final DepartmentManager departmentManager;
    private final OrgProperties orgProperties;
    private final RoleManager roleManager;
    private final DepartmentRoleService departmentRoleService;

    private final static String BASE_DIR = "bbt/init/org/";
    private static final ThreadPoolExecutor DATA_POOL = ExecutorUtil.get(12, 10000, "user-import-", 1024, null);

    public void initAdmin() {

    }

    public Map<String, UserImport> init() {
        List<UserImport> list = userImportManager.list();

        Map<String, String> deptMap = Maps.newHashMap();
        Map<String, String> roleMap = Maps.newHashMap();

        Map<String, UserImport> failList = new HashMap<>(4);
        HashSet<String> objects = Sets.newHashSet();
        for (UserImport user : list) {
            String phone = user.getPhone();
            UserDTO exist = userService.getUserByPhone(phone);
            if (exist != null) {
                objects.add(phone);
                continue;

            }
            String roleString = user.getRole();
            List<String> roles = StrUtil.split(roleString, ",");
            HashSet<String> roleIds = Sets.newHashSet();
            for (String role : roles) {
                String roleId = roleMap.get(role);
                if (roleId == null) {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setName(role);
                    roleDTO = roleService.getOrAdd(roleDTO);
                    roleId = roleDTO.getId();
                    roleMap.put(role, roleId);
                }
                roleIds.add(roleId);
            }

            String regionCode = user.getRegionCode();
            String realname = user.getRealname();
            UserDTO dto = new UserDTO();
            dto.setAccount(user.getAccount());
            dto.setRealname(realname);
            dto.setPwd(Base64.encode(orgProperties.getDefaultPassword()));
            dto.setPhone(phone);
            dto.setAdmin(false);
            dto.setRoleIds(roleIds);
            dto.setRegionCode(regionCode);
            String gender = user.getGender();
            UserGenderEnum ge = UserGenderEnum.UNKNOWN;
            if (StrUtil.isNotBlank(gender)) {
                switch (gender) {
                    case "男":
                        ge = UserGenderEnum.MALE;
                        break;
                    case "女":
                        ge = UserGenderEnum.FEMALE;
                        break;
                    default:
                        ge = UserGenderEnum.UNKNOWN;
                }
            }
            dto.setGender(ge);

            String deptString = user.getDepartment();

            List<String> departments = StrUtil.split(deptString, ",");
            HashSet<String> deptIds = Sets.newHashSet();

            for (String department : departments) {
                String departmentId = deptMap.get(department);
                if (departmentId == null) {
                    // 添加新的部门
                    List<String> depts = StrUtil.split(department, StrUtil.DASHED);
                    List<String> newList = new ArrayList<>();
                    List<String> deptCom = new ArrayList<>();
                    for (String dept : depts) {
                        newList.add(dept);
                        deptCom.add(StrUtil.join(StrUtil.DASHED, newList));
                    }
                    String parentId = null;
                    for (int i = 0; i < deptCom.size(); i++) {
                        String s = deptCom.get(i);
                        String did = deptMap.get(s);
                        if (did == null) {
                            // 检查数据库是否存在此部门
                            String deptName = i == 0 ? s : StrUtil.subAfter(s, StrUtil.DASHED, true);
                            DepartmentDTO departmentDTO = departmentService.getDepartment(parentId, deptName);
                            if (departmentDTO == null) {
                                departmentDTO = new DepartmentDTO().setDeptName(deptName).setParentId(i == 0 ? "0" : parentId);
                                departmentDTO.setRegionCode(regionCode);
                                departmentDTO.setDepartmentType(user.getDepartmentType());
                                departmentService.saveOrUpdate(departmentDTO);
                            }
                            String id = departmentDTO.getId();
                            deptMap.put(s, id);
                            did = id;
                        }
                        parentId = did;
                    }
                    departmentId = parentId;
                }
                deptIds.add(departmentId);
            }

            dto.setDepartmentIds(deptIds);
            try {
                userService.addUser(dto);
            } catch (Exception e) {
                log.error("用户添加失败：{}", dto, e);
                failList.put(e.getMessage(), user);
            }

        }
        log.error("重复手机号：{}", StrUtil.join(",", objects));
        return failList;
    }


    public void export(HttpServletResponse response) {
        List<UserDTO> allUser = userService.getAllUser();
        List<RoleDTO> roles = roleService.getAll(null);
        List<UserDepartment> userRoles = userDepartmentManager.list();
        Map<String, List<UserDepartment>> userRoleMap = userRoles.stream().collect(Collectors.groupingBy(UserDepartment::getUserId));
        Map<String, String> roleMap = roles.stream().collect(Collectors.toMap(RoleDTO::getId, RoleDTO::getName));

        List<UserImport> newUser = new LinkedList<>();
        for (UserDTO user : allUser) {
            UserImport userImport = new UserImport();
            userImport.setId(user.getId());
            userImport.setAccount(user.getAccount());
            userImport.setRealname(user.getRealname());
            userImport.setIdentify(user.getIdentify());
            userImport.setPhone(user.getPhone());
            userImport.setRegionCode(user.getDepartmentInfo().getRegionCode());
            List<UserDepartment> roleRefs = userRoleMap.get(user.getId());
            if (CollUtil.isNotEmpty(roleRefs)) {
                UserDepartment userRole = roleRefs.get(0);
                String roleName = roleMap.get(userRole.getRoleId());
                userImport.setRole(roleName);
            }

            Set<String> departmentIds = user.getDepartmentIds();
            Set<String> deptNames = departmentIds.stream().map(i -> {
                List<String> strings = departmentService.previewDepartmentName(i, 50);
                return StrUtil.join(StrUtil.DASHED, strings);
            }).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(deptNames)) {
                userImport.setDepartment(StrUtil.join(StrUtil.COMMA, deptNames));
            }
            newUser.add(userImport);
        }
        String fileName = "test.xlsx";
        // try {
        //     response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //     response.setHeader("Content-Disposition", "attachment; filename=" +
        //             URLEncoder.encode(fileName, "UTF-8"));
        //     ExcelWriter writer = ExcelUtil.getWriter();
        //     writer.write(newUser, true);
        //     writer.flush(response.getOutputStream());
        //     writer.close();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        ExcelWriter writer = ExcelUtil.getWriter("D:\\trans\\yj.xlsx");
        writer.write(newUser, true);
        writer.flush();
        writer.close();

    }

    public void mergeUserInfo() {
        ExcelReader yjUserReader = ExcelUtil.getReader("D:\\trans\\yj_user.xlsx");
        ExcelReader yjDeptReader = ExcelUtil.getReader("D:\\trans\\yj_dept.xlsx");
        ExcelReader hbUserReader = ExcelUtil.getReader("D:\\trans\\hb_user_role.xlsx");
        ExcelReader hbDeptReader = ExcelUtil.getReader("D:\\trans\\hb_dept.xlsx");


        List<UserDeptMapDTO> yjUserSheet = yjUserReader.readAll(UserDeptMapDTO.class);
        List<UserDeptMapDTO> yjDeptSheet = yjDeptReader.readAll(UserDeptMapDTO.class);
        List<UserDeptMapDTO> hbUserSheet = hbUserReader.readAll(UserDeptMapDTO.class);
        List<UserDeptMapDTO> hbDeptSheet = hbDeptReader.readAll(UserDeptMapDTO.class);

        Map<String, UserDeptMapDTO> yjUserMap = yjUserSheet.stream().collect(Collectors.toMap(UserDeptMapDTO::getAccount, i -> i, (a, b) -> a));
        Map<String, UserDeptMapDTO> yjDeptMap = yjDeptSheet.stream().collect(Collectors.toMap(UserDeptMapDTO::getDept, i -> i, (a, b) -> a));
        Map<String, UserDeptMapDTO> hbUserMap = hbUserSheet.stream().collect(Collectors.toMap(UserDeptMapDTO::getAccount, i -> i, (a, b) -> a));
        Map<String, UserDeptMapDTO> hbDeptMap = hbDeptSheet.stream().collect(Collectors.toMap(UserDeptMapDTO::getDept, i -> i, (a, b) -> a));


        ExcelReader reader = ExcelUtil.getReader("D:\\trans\\final-exec.xlsx");
        ExcelReader sheet2Reader = reader.setSheet(1);
        List<UserMergeImportDTO> sheet2User = sheet2Reader.readAll(UserMergeImportDTO.class);

        List<UserDeptMapDTO> userMapping = new ArrayList<>();
        List<UserDeptMapDTO> deptMapping = new ArrayList<>();
        List<UserDeptMapDTO> roleMapping = new ArrayList<>();


        Map<String, String> roleMap = Maps.newHashMap();

        // 更改用户
        for (UserMergeImportDTO user : sheet2User) {
            UserDeptMapDTO hbUser = hbUserMap.get(user.getAccount());
            if (hbUser == null) {
                log.warn("未找到用户：{}", user.getAccount());
                continue;
            }

            UserDeptMapDTO userMap = new UserDeptMapDTO();
            userMap.setYjUserId(user.getId());
            userMap.setHbUserId(hbUser.getHbUserId());
            userMap.setAccount(user.getAccount());
            userMap.setPhone(user.getPhone());
            userMap.setRealname(user.getRealname());
            List<DepartmentDTO> dds = departmentService.getByUserId(user.getId());
            if (CollUtil.isNotEmpty(dds)) {
                userMap.setDeptCode(dds.get(0).getDeptCode());
                userMap.setYjDeptId(dds.get(0).getId());
                userMap.setDept(dds.get(0).getDeptName());
            }

            // 合并汇编和云津用户角色  todo
            List<UserDepartment> yjUrserRoles = userDepartmentManager.list(new LambdaQueryWrapper<>(UserDepartment.class).eq(UserDepartment::getUserId, user.getId()));
            Set<String> yjRoleIds = yjUrserRoles.stream().map(UserDepartment::getRoleId).collect(Collectors.toSet());
            String hbRoleId = hbUser.getHbRoleId();
            String hbRoleName = hbUser.getHbRoleName();
            List<String> hbRoleNames = StrUtil.split(hbRoleName, ",");
            List<String> hbRoleIds = StrUtil.split(hbRoleId, ",");

            for (int i = 0; i < hbRoleNames.size(); i++) {
                String role = hbRoleNames.get(i);
                String yjRoleId = roleMap.get(role);
                if (yjRoleId == null) {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setName(role);
                    roleDTO = roleService.getOrAdd(roleDTO);
                    yjRoleId = roleDTO.getId();
                    roleMap.put(role, yjRoleId);

                    // 添加角色映射
                    UserDeptMapDTO rMap = new UserDeptMapDTO();
                    rMap.setHbRoleName(role);
                    rMap.setHbRoleId(hbRoleIds.get(i));
                    rMap.setYjRoleId(yjRoleId);
                    roleMapping.add(rMap);
                }
                yjRoleIds.add(yjRoleId);
            }

            UserDTO yjUser = new UserDTO();
            yjUser.setId(user.getId());
            yjUser.setAccount(user.getAccount());
            yjUser.setRoleIds(yjRoleIds);
            userService.updateUser(yjUser);
            userMapping.add(userMap);
        }


        // 处理新增的用户
        ExcelReader sheet1Reader = reader.setSheet(0);
        List<UserMergeImportDTO> sheet1User = sheet1Reader.readAll(UserMergeImportDTO.class);
        Map<String, String> deptMap = Maps.newHashMap();

        HashSet<String> objects = Sets.newHashSet();
        for (UserMergeImportDTO user : sheet1User) {
            String account = user.getAccount();

            UserDeptMapDTO hbUser = hbUserMap.get(account);
            if (hbUser == null) {
                log.warn("未找到用户：{}", account);
                continue;
            }

            HashSet<String> yjRoleIds = Sets.newHashSet();
            String hbRoleId = hbUser.getHbRoleId();
            String hbRoleName = hbUser.getHbRoleName();
            List<String> hbRoleNames = StrUtil.split(hbRoleName, ",");
            List<String> hbRoleIds = StrUtil.split(hbRoleId, ",");

            for (int i = 0; i < hbRoleNames.size(); i++) {
                String role = hbRoleNames.get(i);
                String yjRoleId = roleMap.get(role);
                if (yjRoleId == null) {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setName(role);
                    roleDTO = roleService.getOrAdd(roleDTO);
                    yjRoleId = roleDTO.getId();
                    roleMap.put(role, yjRoleId);

                    // 添加角色映射
                    UserDeptMapDTO rMap = new UserDeptMapDTO();
                    rMap.setHbRoleName(role);
                    rMap.setHbRoleId(hbRoleIds.get(i));
                    rMap.setYjRoleId(yjRoleId);
                    roleMapping.add(rMap);
                }
                yjRoleIds.add(yjRoleId);
            }


            String deptString = user.getDepartment();

            List<String> departments = StrUtil.split(deptString, ",");
            HashSet<String> deptIds = Sets.newHashSet();

            for (String department : departments) {
                String departmentId = deptMap.get(department);
                if (departmentId == null) {
                    // 添加新的部门
                    List<String> depts = StrUtil.split(department, StrUtil.DASHED);
                    List<String> newList = new ArrayList<>();
                    List<String> deptCom = new ArrayList<>();
                    for (String dept : depts) {
                        newList.add(dept);
                        deptCom.add(StrUtil.join(StrUtil.DASHED, newList));
                    }
                    String parentId = null;
                    for (int i = 0; i < deptCom.size(); i++) {
                        String s = deptCom.get(i);
                        String did = deptMap.get(s);
                        if (did == null) {
                            // 检查数据库是否存在此部门
                            String deptName = i == 0 ? s : StrUtil.subAfter(s, StrUtil.DASHED, true);
                            DepartmentDTO departmentDTO = departmentService.getDepartment(parentId, deptName);
                            if (departmentDTO == null) {
                                departmentDTO = new DepartmentDTO().setDeptName(deptName).setParentId(i == 0 ? "0" : parentId).setCreateBy("system");
                                departmentService.saveOrUpdate(departmentDTO);
                            }
                            String id = departmentDTO.getId();
                            deptMap.put(s, id);

                            log.info("deptName:{}", s);
                            UserDeptMapDTO dMap = hbDeptMap.get(s);
                            if (dMap != null) {
                                dMap.setDeptCode(departmentDTO.getDeptCode());
                                dMap.setYjDeptId(departmentDTO.getId());
                                deptMapping.add(dMap);
                            } else {
                                log.warn("deptname  null:{}", s);
                            }
                            did = id;
                        }
                        parentId = did;
                    }
                    departmentId = parentId;
                }
                deptIds.add(departmentId);
            }

            UserDTO dto = userService.getUserByCredentials(account);
            if (dto != null) {
                objects.add(account);
                log.warn("用户重复,保存映射关系：{}", account);
            } else {
                dto = new UserDTO();
                dto.setAccount(account);
                dto.setRealname(user.getRealname());
                dto.setPwd(Base64.encode("Chq@bbt2023"));
                dto.setPhone(StrUtil.subWithLength(account, 0, 11));
                dto.setAdmin(false);
                dto.setRoleIds(yjRoleIds);
                dto.setCreateBy("system");
                dto.setDepartmentIds(deptIds);
                try {
                    userService.addUser(dto);
                } catch (Exception e) {
                    log.error("用户添加失败  message:{} user：{}", e.getMessage(), dto);
                    continue;
                }
            }

            UserDeptMapDTO userMap = new UserDeptMapDTO();
            userMap.setYjUserId(dto.getId());
            userMap.setHbUserId(Optional.ofNullable(hbUserMap.get(user.getAccount())).map(UserDeptMapDTO::getHbUserId).orElse(""));
            userMap.setAccount(user.getAccount());
            userMap.setPhone(user.getPhone());
            userMap.setRealname(user.getRealname());
            deptIds.stream().findFirst().ifPresent(did -> {
                DepartmentDTO dd = departmentService.getDepartmentById(did);
                userMap.setDept(dd.getDeptName());
                userMap.setDeptCode(dd.getDeptCode());
                userMap.setYjDeptId(did);
            });
            userMapping.add(userMap);
        }
        log.warn("用户重复：{}", StrUtil.join(StrUtil.COMMA, objects));
        ExcelWriter writer = ExcelUtil.getWriter("D:\\trans\\mapping_info.xlsx");
        ExcelWriter sheet1 = writer.setSheet("用户");
        sheet1.write(userMapping);
        ExcelWriter sheet2 = writer.setSheet("部门");
        sheet2.write(deptMapping);
        ExcelWriter sheet3 = writer.setSheet("角色");
        sheet3.write(roleMapping);
        writer.flush();
    }


    public void syncTfUser(List<TfDeptInfoResult> tfDepts, List<TfUserInfoResult> users) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("默认角色");
        roleDTO = roleService.getOrAdd(roleDTO);

        Map<String, String> tfDeptMap = new HashMap<>(4);

        Map<String, String> deptMap = Maps.newHashMap();

        // 部门信息解析
        parseDepartmentInfo(tfDepts, tfDeptMap);
        // 递归读取部门信息
        List<UserDTO> list = new LinkedList<>();
        for (TfUserInfoResult item : users) {
            String phone = item.getPhone();
            UserDTO dto = userService.getUserByPhone(phone);
            if (dto != null) {
                continue;
            }
            dto = new UserDTO();
            dto.setAccount(item.getUsername());
            dto.setRealname(item.getName());
            dto.setPwd(Base64.encode(phone));
            dto.setPhone(phone);
            dto.setAdmin(false);
            dto.setRoleIds(Collections.singleton(roleDTO.getId()));
            String gender = item.getSex();
            UserGenderEnum ge = UserGenderEnum.UNKNOWN;
            if (StrUtil.isNotBlank(gender)) {
                switch (gender) {
                    case "0":
                        ge = UserGenderEnum.MALE;
                        break;
                    case "1":
                        ge = UserGenderEnum.FEMALE;
                        break;
                    default:
                        ge = UserGenderEnum.UNKNOWN;
                }
            }
            dto.setGender(ge);

            // 处理用户部门信息
            List<TfDeptInfoResult> tfDept = item.getDepts();
            HashSet<String> deptIds = Sets.newHashSet();

            for (TfDeptInfoResult tfDeptItem : tfDept) {
                // 判断部门是否已存在
                String deptFullName = tfDeptMap.get(tfDeptItem.getDeptId());
                // 对部门名称做处理
                deptFullName = handleDeptName(deptFullName);


                // 根据完整名找map
                String departmentId = deptMap.get(deptFullName);
                if (departmentId == null) {
                    // 添加新的部门
                    List<String> depts = StrUtil.split(deptFullName, StrUtil.DASHED);
                    List<String> newList = new ArrayList<>();
                    List<String> deptCom = new ArrayList<>();
                    for (String dept : depts) {
                        newList.add(dept);
                        deptCom.add(StrUtil.join(StrUtil.DASHED, newList));
                    }
                    String parentId = null;
                    for (int i = 0; i < deptCom.size(); i++) {
                        String s = deptCom.get(i);
                        String did = deptMap.get(s);
                        if (did == null) {
                            // 检查数据库是否存在此部门
                            String deptName = i == 0 ? s : StrUtil.subAfter(s, StrUtil.DASHED, true);
                            DepartmentDTO departmentDTO = departmentService.getDepartment(parentId, deptName);
                            if (departmentDTO == null) {
                                departmentDTO = new DepartmentDTO().setDeptName(deptName).setParentId(i == 0 ? "0" : parentId).setCreateBy("system");
                                departmentService.saveOrUpdate(departmentDTO);
                            }
                            String id = departmentDTO.getId();
                            deptMap.put(s, id);
                            did = id;
                        }
                        parentId = did;
                    }
                    departmentId = parentId;
                    deptIds.add(departmentId);
                }


            }
            dto.setDepartmentIds(deptIds);
            list.add(dto);
            try {
                userService.addUser(dto);
            } catch (Exception e) {
                log.error("用户添加失败：{}", dto, e);
            }
        }
        String s = JSONUtil.toJsonPrettyStr(list);
        log.info("tf add new user:{}", s);
        FileUtil.writeString(s, "temp/tf-sync-user-" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".json",
                StandardCharsets.UTF_8);
    }

    private String handleDeptName(String deptFullName) {
        String newDeptFullName = deptFullName;
        newDeptFullName = StrUtil.replace(newDeptFullName, "蓉政通-四川天府新区", "天府新区");
        if (!StrUtil.startWith(newDeptFullName, "天府新区")) {
            newDeptFullName = "天府新区-" + newDeptFullName;
        }
        log.info("tf deptName convern {}  -> {}", deptFullName, newDeptFullName);
        return newDeptFullName;
    }

    private void parseDepartmentInfo(List<TfDeptInfoResult> depts, Map<String, String> map) {
        for (TfDeptInfoResult dept : depts) {
            String parentId = dept.getParentId();
            String parentFullName = map.get(parentId);
            String currentDeptFullName;
            if (StrUtil.isBlank(parentFullName)) {
                currentDeptFullName = dept.getName();
            } else {
                currentDeptFullName = parentFullName + StrUtil.DASHED + dept.getName();
            }
            map.put(dept.getDeptId(), currentDeptFullName);
            List<TfDeptInfoResult> childrens = dept.getChildrens();
            if (CollUtil.isNotEmpty(childrens)) {
                parseDepartmentInfo(childrens, map);
            }
        }
    }

    /**
     * 初始化部门区域
     *
     * @author Carlos
     * @date 2024/9/14 15:55
     */
    public void initDeptRegion() {
        String sqlFormat = "UPDATE org_department SET region_code = NULL WHERE id = '%s';";

        List<DepartmentDTO> depts = departmentManager.listAll();
        Map<String, DepartmentDTO> deptMap = depts.stream().collect(Collectors.toMap(DepartmentDTO::getId, k -> k));

        ApiRegion apiRegion = SpringUtil.getBean(ApiRegion.class);
        Result<List<SysRegionAO>> result = apiRegion.all();
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        List<SysRegionAO> regions = result.getData();

        Map<String, List<SysRegionAO>> regionNameMap = regions.stream().collect(Collectors.groupingBy(SysRegionAO::getRegionName));

        List<DeptRegionExcel> excels = new ArrayList<>();
        int size = depts.size();
        for (int j = 0; j < size; j++) {
            DepartmentDTO dept = depts.get(j);
            DeptRegionExcel excel = new DeptRegionExcel();
            log.info("处理部门{}/{}:{}:{}", j, size, dept.getId(), dept.getDeptName());
            String id = dept.getId();
            excel.setId(id);
            excel.setDeptName(dept.getDeptName());
            excel.setDeptCode(dept.getDeptCode());

            String regionCode = dept.getRegionCode();
            // 如果部门已经存在区域，直接跳过
            if (StrUtil.isNotBlank(regionCode)) {
                excel.setDeal(false);
                excel.setMessage("使用部门已有区域");
                RegionInfo region = getReregion(regionCode);
                excel.setSourceRegionCode(regionCode);
                excel.setSourceRegionName(StrUtil.join(StrUtil.DASHED, region.getFullName()));
                excels.add(excel);
                continue;
            }
            // 获取部门下的用户信息
            List<UserDTO> deptUsers = getDeptUsers(id);
            if (CollUtil.isNotEmpty(deptUsers)) {
                // 取用户中区域最小的，及区域code最长的区域，如果长度一致的编码有多个，此选择机制无效，进入下一轮选择
                List<String> strings = deptUsers.stream().map(UserDTO::getRegionCode).filter(StrUtil::isNotBlank).collect(Collectors.toList());
                List<String> regionCodes = findLongestStrings(strings);
                if (regionCodes.size() == 1) {
                    // 通过用户筛选出唯一的区域
                    regionCode = regionCodes.get(0);
                    RegionInfo region = getReregion(regionCode);
                    excel.setTargetRegionCode(regionCode);
                    excel.setTargetRegionName(StrUtil.join(StrUtil.DASHED, region.getFullName()));
                    excel.setDealRemark("根据部门下的人员选择区域编码最长");
                    excel.setDeal(true);
                    excel.setRollbackSql(StrUtil.format(sqlFormat, id));
                    excels.add(excel);
                    DepartmentDTO dto = new DepartmentDTO();
                    dto.setId(id);
                    dto.setRegionCode(excel.getTargetRegionCode());
                    departmentService.updateDepartment(dto);
                    continue;
                }
            }

            String[] keywords = {"省", "市", "区", "县", "街道", "镇", "网格", "社区", "村"};
            // 用部门名称和区域名称匹配
            String deptName = dept.getDeptName();
            if (!StrUtil.containsAny(deptName, keywords)) {
                excel.setDeal(false);
                excel.setMessage("该部门名称与区域不具备关联关系");
                excels.add(excel);
                continue;
            }

            List<SysRegionAO> matchRegions = regionNameMap.get(deptName);
            if (matchRegions == null) {
                // 未匹配到区域
                excel.setDeal(false);
                excel.setMessage("该部门名称未匹配到区域");
                excels.add(excel);
                continue;
            }

            // 匹配到唯一的名称
            if (matchRegions.size() == 1) {
                SysRegionAO r = matchRegions.get(0);
                excel.setTargetRegionCode(r.getRegionCode());
                excel.setTargetRegionName(r.getRegionName());
                excel.setDealRemark("部门名称匹配到唯一的区域名称");
                excel.setDeal(true);
                excel.setRollbackSql(StrUtil.format(sqlFormat, id));
                DepartmentDTO dto = new DepartmentDTO();
                dto.setId(id);
                dto.setRegionCode(excel.getTargetRegionCode());
                dto.setParentId(dept.getParentId());
                departmentService.updateDepartment(dto);
                excels.add(excel);
                continue;
            } else {
                // 再次往上匹配一级
                String parentId = dept.getParentId();
                DepartmentDTO parentDept = deptMap.get(parentId);
                String parentDeptName = parentDept.getDeptName();
                List<SysRegionAO> collect = matchRegions.stream().filter(a -> a.getRegionName().equals(parentDeptName)).collect(Collectors.toList());
                if (CollUtil.isEmpty(collect)) {
                    // 往上未匹配到合适的区域
                    // 未匹配到区域
                    excel.setDeal(false);
                    excel.setMessage("进行两级匹配时未命中区域");
                    excels.add(excel);
                    continue;
                }
                if (collect.size() == 1) {
                    SysRegionAO r = collect.get(0);
                    excel.setTargetRegionCode(r.getRegionCode());
                    excel.setTargetRegionName(r.getRegionName());
                    excel.setDealRemark("两级名称匹配命中区域");
                    excel.setDeal(true);
                    excel.setRollbackSql(StrUtil.format(sqlFormat, id));
                    excels.add(excel);
                    DepartmentDTO dto = new DepartmentDTO();
                    dto.setId(id);
                    dto.setParentId(dept.getParentId());
                    dto.setRegionCode(excel.getTargetRegionCode());
                    departmentService.updateDepartment(dto);
                    continue;
                } else {
                    excel.setDeal(false);
                    excel.setMessage("进行两级匹配时命中多个，需要优化代码继续匹配");
                    excels.add(excel);
                }
            }
        }

        ExcelUtil.getWriter(BASE_DIR + "部门区域初始化" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".xlsx").write(excels, true).flush().close();

    }

    public static List<String> findLongestStrings(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return new ArrayList<>();
        }

        int maxLength = 0;
        for (String str : strings) {
            maxLength = Math.max(maxLength, str.length());
        }

        List<String> longest = new ArrayList<>();
        for (String str : strings) {
            if (str.length() == maxLength) {
                longest.add(str);
            }
        }
        return longest;
    }

    /**
     * 获取部门下的用户
     *
     * @param deptId 参数0
     * @return java.util.List<com.yunjin.org.pojo.dto.UserDTO>
     * @throws
     * @author Carlos
     * @date 2024/9/14 13:16
     */
    private List<UserDTO> getDeptUsers(String deptId) {
        Set<String> userIds = this.userDepartmentManager.getUserIdByDepartmentId(deptId);
        if (CollectionUtil.isEmpty(userIds)) {
            return null;
        }

        return this.userManager.getByIds(userIds);
    }


    /**
     * 获取区域信息
     *
     * @param regionCode 参数0
     * @return com.yunjin.core.base.RegionInfo
     * @author Carlos
     * @date 2024/9/14 13:05
     */
    private RegionInfo getReregion(String regionCode) {
        ApiRegion apiRegion = SpringUtil.getBean(ApiRegion.class);
        Result<RegionInfo> result = apiRegion.getRegionInfo(regionCode, 20);
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        return result.getData();

    }

    @Transactional(rollbackFor = Exception.class)
    public String handleUserData(MultipartFile file) {
        Integer headerIndex = 1;
        // 保存导入的数据文件
        String filename = file.getOriginalFilename();
        if (CharSequenceUtil.isBlank(filename)) {
            throw new ServiceException("文件名不能为空");
        }
        Map<Integer, String> header;
        List<Map<Integer, String>> batchData;
        try {
            // 适配多级表头
            headerIndex = headerIndex == null ? 1 : headerIndex;
            com.yunjin.excel.easyexcel.ExcelUtil.checkExcel(filename);
            List<Map<Integer, String>> maps = EasyExcel.read(file.getInputStream()).sheet().headRowNumber(headerIndex - 1).doReadSync();
            // 检查参数
            if (maps.size() < 2) {
                throw new ServiceException("文件数据为空");
            }
            header = maps.get(0);
            batchData = maps.subList(1, maps.size());
        } catch (IOException e) {
            throw new ServiceException("数据导入失败!");
        } catch (POIXMLException e) {
            throw new ServiceException("文件损坏，无法解析!");
        }
        List<String> userImportCol = new ArrayList<>();
        userImportCol.add("id");
        userImportCol.add("account");
        userImportCol.add("realname");
        userImportCol.add("identify");
        userImportCol.add("phone");
        userImportCol.add("department");
        userImportCol.add("role");
        userImportCol.add("department_type");
        userImportCol.add("department_type_name");
        userImportCol.add("region_code");
        userImportCol.add("gender");
        userImportCol.add("email");
        // 导入模板判断
        Collection<String> values = header.values();
        Set<String> missingFields = Sets.newHashSet();
        userImportCol.forEach(i -> {
            if (!values.contains(i)) {
                missingFields.add(i);
            }
        });
        if (CollUtil.isNotEmpty(missingFields)) {
            log.error("template error, missing fields:{}:", missingFields);
            throw new ServiceException("模板错误，请使用正确的模板! 缺失字段：" + CharSequenceUtil.join(StrPool.COMMA, missingFields));
        }
        List<String> insertColList = new ArrayList<>();
        for (String head : header.values()) {
            if (userImportCol.contains(head)) {
                insertColList.add(head);
            }
        }
        // 构建dept_type关系
        Map<String, String> departmentTypeMap = buildTypeMap();
        // 查询已有的region
        Map<String, String> regionMap = buildRegionMap();

        List<UserImport> valuesStrList = new ArrayList<>();
//        List<UserImport> list = userImportManager.list();
//        List<String> ids = new ArrayList<>();
//        for (UserImport userImport:list){
//            ids.add(userImport.getId());
//        }
//        userImportManager.removeByIds(ids);
        for (Map<Integer, String> row : batchData) {
            UserImport userImport = new UserImport();
            for (Integer lieNum : row.keySet()) {
                String tempHead = header.get(lieNum);
                if (insertColList.contains(tempHead)) {
                    String valStr = row.get(lieNum);
                    // 处理空格
                    if (StrUtil.isNotBlank(valStr)) {
                        valStr = valStr.replaceAll("\\s+", "");
                    }
                    switch (tempHead) {
                        case "id":
                            userImport.setId(valStr);
                            break;
                        case "account":
                            userImport.setAccount(valStr);
                            break;
                        case "realname":
                            userImport.setRealname(valStr);
                            break;
                        case "identify":
                            userImport.setIdentify(valStr);
                            break;
                        case "phone":
                            userImport.setPhone(valStr);
                            break;
                        case "department":
                            userImport.setDepartment(valStr);
                            break;
                        case "role":
                            userImport.setRole(valStr);
                            break;
                        case "department_type":
                            userImport.setDepartmentType(valStr);
                            break;
                        case "department_type_name":
                            userImport.setDepartmentTypeName(valStr);
                            break;
                        case "region_code":
                            userImport.setRegionCode(valStr);
                            break;
                        case "gender":
                            userImport.setGender(valStr);
                            break;
                        case "email":
                            userImport.setEmail(valStr);
                            break;
                        default:
                            break;

                    }
                }
            }
            // 设置department_type
            String departmentTypeName = userImport.getDepartmentTypeName();
            String departmentType = departmentTypeMap.get(departmentTypeName);
            if (StrUtil.isBlank(userImport.getDepartmentType())) {
                userImport.setDepartmentType(departmentType);
            } else {
                if (StrUtil.isNotBlank(departmentType)) {
                    userImport.setDepartmentType(departmentType);
                }
            }
            if (StrUtil.isBlank(userImport.getPhone())) {
                throw new ServiceException("电话号码缺失，异常行信息：" + userImport.toString());
            }
            if (StrUtil.isBlank(userImport.getId())) {
                throw new ServiceException("id缺失，异常行信息：" + userImport.toString());
            }
            if (StrUtil.isBlank(userImport.getRole())) {
//                throw new ServiceException("角色缺失，异常行信息："+userImport.toString());
                userImport.setRole("普通用户");
            }
            if (StrUtil.isBlank(userImport.getAccount())) {
                throw new ServiceException("用户名缺失，异常行信息：" + userImport.toString());
            }
            if (StrUtil.isBlank(userImport.getDepartmentTypeName())) {
                throw new ServiceException("部门分类名缺失（department_type_name），异常行信息：" + userImport.toString());
            }
            if (StrUtil.isBlank(userImport.getDepartment())) {
                throw new ServiceException("部门名缺失（department），异常行信息：" + userImport.toString());
            }
            if (StrUtil.isBlank(userImport.getRegionCode())) {
                throw new ServiceException("区域编码缺失（region_code），异常行信息：" + userImport.toString());
            }
            if (StrUtil.isBlank(userImport.getDepartmentType())) {
                throw new ServiceException("部门类型映射失败(department_type_name查department_type)，异常行id：" + userImport.getId() + ",对应部门：" + userImport.getDepartment());
            }
            Set<String> deptTypeSet = new HashSet<>(departmentTypeMap.values());
            if (!deptTypeSet.contains(userImport.getDepartmentType())) {
                throw new ServiceException("未知部门类型（department_type），异常行id：" + userImport.getId() + ",对应部门：" + userImport.getDepartment());
            }
            String regionCode = userImport.getRegionCode();
            if (!regionMap.containsKey(regionCode)) {
                throw new ServiceException("未知区域编码，异常行id：" + userImport.getId() + ",对应部门：" + userImport.getDepartment());
            }
            valuesStrList.add(userImport);
//            if(valuesStrList.size()>=1000){
//                userImportManager.saveBatch(valuesStrList);
//                valuesStrList = new ArrayList<>();
//            }
        }
//        if(valuesStrList.size()>0){
//            userImportManager.saveBatch(valuesStrList);
//        }
        log.info("表头：" + header.toString());

//        init();
        importUser(valuesStrList);
        return header.toString();
    }

    private void importUser(List<UserImport> list) throws ServiceException {
        String createBy = "0";
        List<UserDTO> adminUserList = userManager.getAdminUser();
        if (adminUserList != null && adminUserList.size() > 0) {
            UserDTO userDTO = adminUserList.get(0);
            createBy = userDTO.getId();
        }
        Map<String, String> deptMap = Maps.newHashMap();
        Map<String, String> roleMap = Maps.newHashMap();

        for (UserImport user : list) {
            String phone = user.getPhone();
            UserDTO exist = userService.getUserByPhone(phone);
            if (exist != null) {
                throw new ServiceException("系统已有手机号：" + phone + ",异常行id:" + user.getId() + "。");
//                objects.add(phone);
//                continue;

            }
            String account = user.getAccount();
            UserDTO existAccount = userService.getUserByAccount(account);
            if (existAccount != null) {
                throw new ServiceException("系统已有账号：" + account + ",异常行id:" + user.getId() + "。");
            }

            String regionCode = user.getRegionCode();
            String realname = user.getRealname();
            UserDTO dto = new UserDTO();
            dto.setAccount(user.getAccount());
            dto.setRealname(realname);
            dto.setPwd(Base64.encode(orgProperties.getDefaultPassword()));
            dto.setPhone(phone);
            dto.setAdmin(false);
//            dto.setRoleIds(roleIds);
            dto.setRegionCode(regionCode);
            dto.setCreateBy(createBy);
            String gender = user.getGender();
            UserGenderEnum ge = UserGenderEnum.UNKNOWN;
            if (StrUtil.isNotBlank(gender)) {
                switch (gender) {
                    case "男":
                        ge = UserGenderEnum.MALE;
                        break;
                    case "女":
                        ge = UserGenderEnum.FEMALE;
                        break;
                    default:
                        ge = UserGenderEnum.UNKNOWN;
                }
            }
            dto.setGender(ge);

            String deptString = user.getDepartment();

            List<String> departments = StrUtil.split(deptString, ",");
            HashSet<String> deptIds = Sets.newHashSet();
            List<UserDeptRoleDTO> deptRoles = new ArrayList<>();
            for (String department : departments) {
                String departmentId = deptMap.get(department);
                if (departmentId == null) {
                    // 添加新的部门
                    List<String> depts = StrUtil.split(department, StrUtil.DASHED);
                    List<String> newList = new ArrayList<>();
                    List<String> deptCom = new ArrayList<>();
                    for (String dept : depts) {
                        newList.add(dept);
                        deptCom.add(StrUtil.join(StrUtil.DASHED, newList));
                    }
                    String parentId = null;
                    for (int i = 0; i < deptCom.size(); i++) {
                        String s = deptCom.get(i);
                        String did = deptMap.get(s);
                        if (did == null) {
                            // 检查数据库是否存在此部门
                            String deptName = i == 0 ? s : StrUtil.subAfter(s, StrUtil.DASHED, true);
                            DepartmentDTO departmentDTO = departmentService.getDepartment(parentId, deptName);
                            if (departmentDTO == null) {
                                if (StrUtil.equals(s, department)) {
                                    departmentDTO = new DepartmentDTO().setDeptName(deptName).setParentId(i == 0 ? "0" : parentId);
                                    departmentDTO.setRegionCode(regionCode);
                                    departmentDTO.setDepartmentType(user.getDepartmentType());
                                    departmentDTO.setDepartmentLevelCode(departmentDTO.getDepartmentType().substring(departmentDTO.getDepartmentType().lastIndexOf("-") + 1));
                                    departmentDTO.setCreateBy(createBy);
                                    departmentService.saveOrUpdate(departmentDTO);
                                } else {
                                    throw new ServiceException("上级部门不存在，缺失部门：" + s + "，异常行信息：" + user.toString());
                                }

                            }
                            String id = departmentDTO.getId();
                            deptMap.put(s, id);
                            did = id;
                        }
                        parentId = did;
                    }
                    departmentId = parentId;
                }
                DepartmentDTO departmentDTO = departmentService.getDepartmentById(departmentId);
                // 处理部门下的角色
                String roleString = user.getRole();
                List<String> roles = StrUtil.split(roleString, ",");

                for (String role : roles) {
                    String roleId = roleMap.get(role);
                    if (roleId == null) {
                        RoleDTO roleDTO = new RoleDTO();
                        roleDTO.setName(role);
                        roleDTO.setCreateBy(createBy);
                        roleDTO = roleManager.getByName(role);
                        if (roleDTO == null) {
                            throw new ServiceException("导入角色不存在或普通用户角色不存在，异常行id为：" + user.getId());
                        }
                        roleId = roleDTO.getId();
                        roleMap.put(role, roleId);
                    }
                    // 校验部门层级下是否存在改角色,如果存在就添加,不存就就略过
                    boolean existRelation = departmentRoleService.existRelation(departmentDTO.getDepartmentLevelCode(), roleId);
                    if (existRelation) {
                        UserDeptRoleDTO deptRoleDto = new UserDeptRoleDTO().setDepartmentId(departmentDTO.getId()).setRoleId(roleId).setDepartmentType(departmentDTO.getDepartmentLevelCode());
                        deptRoles.add(deptRoleDto);
                    } else {
                        log.error("部门层级下不存在该角色，请检查角色下部门层级关系，部门层级：{}，角色：{}",
                                departmentDTO.getDepartmentLevelCode(), role);
                        throw new ServiceException("部门层级下不存在该角色，请检查角色下部门层级关系，" +
                                "角色：" + role + "部门层级:" + departmentDTO.getDepartmentLevelCode() +
                                "，异常行id为：" + user.getId());
                    }
                }
                deptIds.add(departmentId);
            }
            dto.setDeptRoles(deptRoles);
            if (deptRoles.isEmpty()) {
                throw new ServiceException("导入角色不存在，异常行id为：" + user.getId());
            }
            dto.setDepartmentIds(deptIds);
            try {
                userService.addUser(dto);
            } catch (Exception e) {
                log.error("用户添加失败：{}", dto, e);
                throw new ServiceException("用户添加失败!失败行id：" + user.getId() + "，失败用户信息：" + dto.toString());
            }

        }
    }

    private Map<String, String> buildTypeMap() {
        Map<String, String> map = new HashMap<>();
        ApiDict api = SpringUtil.getBean(ApiDict.class);
        Result<List<DictItemAO>> result = api.list("ORG_TYPE");

        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        Optional.ofNullable(result.getData()).ifPresent(dict -> {
            for (DictItemAO item : dict) {
                map.put(item.getItemName(), item.getItemCode());
            }
        });
        return map;
    }

    public String handleDeptData(MultipartFile file) {
        String createBy = "0";
        List<UserDTO> adminUserList = userManager.getAdminUser();
        if (adminUserList != null && adminUserList.size() > 0) {
            UserDTO userDTO = adminUserList.get(0);
            createBy = userDTO.getId();
        }
        Integer headerIndex = 1;
        // 保存导入的数据文件
        String filename = file.getOriginalFilename();
        if (CharSequenceUtil.isBlank(filename)) {
            throw new ServiceException("文件名不能为空");
        }
        Map<Integer, String> header;
        List<Map<Integer, String>> batchData;
        try {
            // 适配多级表头
            headerIndex = headerIndex == null ? 1 : headerIndex;
            com.yunjin.excel.easyexcel.ExcelUtil.checkExcel(filename);
            List<Map<Integer, String>> maps = EasyExcel.read(file.getInputStream()).sheet().headRowNumber(headerIndex - 1).doReadSync();
            // 检查参数
            if (maps.size() < 2) {
                throw new ServiceException("文件数据为空");
            }
            header = maps.get(0);
            batchData = maps.subList(1, maps.size());
        } catch (IOException e) {
            throw new ServiceException("数据导入失败!");
        } catch (POIXMLException e) {
            throw new ServiceException("文件损坏，无法解析!");
        }
        List<String> userImportCol = new ArrayList<>();

        userImportCol.add("department");
        userImportCol.add("department_type");
        userImportCol.add("region_code");
        // 导入模板判断
        Collection<String> values = header.values();
        Set<String> missingFields = Sets.newHashSet();
        userImportCol.forEach(i -> {
            if (!values.contains(i)) {
                missingFields.add(i);
            }
        });
        if (CollUtil.isNotEmpty(missingFields)) {
            log.error("template error, missing fields:{}:", missingFields);
            throw new ServiceException("模板错误，请使用正确的模板! 缺失字段：" + CharSequenceUtil.join(StrPool.COMMA, missingFields));
        }
        List<String> insertColList = new ArrayList<>();
        for (String head : header.values()) {
            if (userImportCol.contains(head)) {
                insertColList.add(head);
            }
        }
        // 构建dept_type关系
        Map<String, String> departmentTypeMap = buildTypeMap();
        Map<String, String> regionMap = buildRegionMap();

        for (Map<Integer, String> row : batchData) {
            UserImport userImport = new UserImport();
            for (Integer lieNum : row.keySet()) {
                String tempHead = header.get(lieNum);
                String valStr = row.get(lieNum);
                // 处理空格
                if (StrUtil.isNotBlank(valStr)) {
                    valStr = valStr.replaceAll("\\s+", "");
                }
                switch (tempHead) {
                    case "id":
                        userImport.setId(valStr);
                        break;
                    case "department":
                        userImport.setDepartment(valStr);
                        break;
                    case "department_type":
                        userImport.setDepartmentType(valStr);
                        break;
                    case "department_type_name":
                        userImport.setDepartmentTypeName(valStr);
                        break;
                    case "region_code":
                        userImport.setRegionCode(valStr);
                        break;
                    default:
                        break;

                }
            }
            // 设置department_type
            String departmentTypeName = userImport.getDepartmentTypeName();
            String departmentType = departmentTypeMap.get(departmentTypeName);
            if (StrUtil.isBlank(userImport.getDepartmentType())) {
                userImport.setDepartmentType(departmentType);
            } else {
                if (StrUtil.isNotBlank(departmentType)) {
                    userImport.setDepartmentType(departmentType);
                }
            }
            if (StrUtil.isBlank(userImport.getDepartmentType())) {
                throw new ServiceException("部门类型映射失败(department_type_name查department_type)，异常行id：" + userImport.getId() + ",对应部门：" + userImport.getDepartment());
            }
            Set<String> deptTypeSet = new HashSet<>(departmentTypeMap.values());
            if (!deptTypeSet.contains(userImport.getDepartmentType())) {
                throw new ServiceException("未知部门类型（department_type），异常行id：" + userImport.getId() + ",对应部门：" + userImport.getDepartment());
            }
            String regionCode = userImport.getRegionCode();
            if (!regionMap.containsKey(regionCode)) {
                throw new ServiceException("未知区域编码(region_code)，异常行id：" + userImport.getId() + ",对应部门：" + userImport.getDepartment());
            }

            if (StrUtil.isBlank(userImport.getId())) {
                throw new ServiceException("id缺失，异常行信息：" + userImport.toString());
            }
            if (StrUtil.isBlank(userImport.getDepartmentTypeName())) {
                throw new ServiceException("部门分类名缺失（department_type_name），异常行信息：" + userImport.toString());
            }
            if (StrUtil.isBlank(userImport.getDepartment())) {
                throw new ServiceException("部门名缺失（department），异常行信息：" + userImport.toString());
            }
            if (StrUtil.isBlank(userImport.getRegionCode())) {
                throw new ServiceException("区域编码缺失（region_code），异常行信息：" + userImport.toString());
            }
            String deptString = userImport.getDepartment();
            Map<String, String> deptMap = Maps.newHashMap();
            List<String> departments = StrUtil.split(deptString, ",");
            HashSet<String> deptIds = Sets.newHashSet();

            for (String department : departments) {
                String departmentId = deptMap.get(department);
                if (departmentId == null) {
                    // 添加新的部门
                    List<String> depts = StrUtil.split(department, StrUtil.DASHED);
                    List<String> newList = new ArrayList<>();
                    List<String> deptCom = new ArrayList<>();
                    for (String dept : depts) {
                        newList.add(dept);
                        deptCom.add(StrUtil.join(StrUtil.DASHED, newList));
                    }
                    String parentId = null;
                    for (int i = 0; i < deptCom.size(); i++) {
                        String s = deptCom.get(i);
                        String did = deptMap.get(s);
                        if (did == null) {
                            // 检查数据库是否存在此部门
                            String deptName = i == 0 ? s : StrUtil.subAfter(s, StrUtil.DASHED, true);
                            DepartmentDTO departmentDTO = departmentService.getDepartment(parentId, deptName);
                            if (departmentDTO == null) {
                                if (StrUtil.equals(s, department)) {
                                    departmentDTO = new DepartmentDTO().setDeptName(deptName).setParentId(i == 0 ? "0" : parentId);
                                    departmentDTO.setRegionCode(userImport.getRegionCode());
                                    departmentDTO.setDepartmentType(userImport.getDepartmentType());
                                    departmentDTO.setCreateBy(createBy);
                                    departmentService.saveOrUpdate(departmentDTO);
                                } else {

                                    throw new ServiceException("上级部门不存在，缺失部门：" + s + "，异常行id：" + userImport.getId() +
                                            ",部门名称：" + department);
                                }

                            }
                            String id = departmentDTO.getId();
                            deptMap.put(s, id);
                            did = id;
                        }
                        parentId = did;
                    }
                    departmentId = parentId;
                }
                deptIds.add(departmentId);
            }
        }
        return header.toString();
    }

    private Map<String, String> buildRegionMap() {
        Map<String, String> map = new HashMap<>();
        ApiRegion api = SpringUtil.getBean(ApiRegion.class);
        Result<List<SysRegionAO>> result = api.all();
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        Optional.ofNullable(result.getData()).ifPresent(regions -> {
            for (SysRegionAO region : regions) {
                map.put(region.getRegionCode(), region.getRegionName());
            }
        });
        return map;
    }

    @Transactional
    public String handleRegionData(MultipartFile file) {
        String createBy = "0";
        List<UserDTO> adminUserList = userManager.getAdminUser();
        if (adminUserList != null && adminUserList.size() > 0) {
            UserDTO userDTO = adminUserList.get(0);
            createBy = userDTO.getId();
        }
        Integer headerIndex = 1;
        // 保存导入的数据文件
        String filename = file.getOriginalFilename();
        if (CharSequenceUtil.isBlank(filename)) {
            throw new ServiceException("文件名不能为空");
        }
        Map<Integer, String> header;
        List<Map<Integer, String>> batchData;
        try {
            // 适配多级表头
            headerIndex = headerIndex == null ? 1 : headerIndex;
            com.yunjin.excel.easyexcel.ExcelUtil.checkExcel(filename);
            List<Map<Integer, String>> maps = EasyExcel.read(file.getInputStream()).sheet().headRowNumber(headerIndex - 1).doReadSync();
            // 检查参数
            if (maps.size() < 2) {
                throw new ServiceException("文件数据为空");
            }
            header = maps.get(0);
            batchData = maps.subList(1, maps.size());
        } catch (IOException e) {
            throw new ServiceException("数据导入失败!");
        } catch (POIXMLException e) {
            throw new ServiceException("文件损坏，无法解析!");
        }
        List<String> regionImportCol = new ArrayList<>();

        regionImportCol.add("region_code");
        regionImportCol.add("region_name");
        regionImportCol.add("parent_code");
//        regionImportCol.add("sort");
        // 导入模板判断
        Collection<String> values = header.values();
        Set<String> missingFields = Sets.newHashSet();
        regionImportCol.forEach(i -> {
            if (!values.contains(i)) {
                missingFields.add(i);
            }
        });
        if (CollUtil.isNotEmpty(missingFields)) {
            log.error("template error, missing fields:{}:", missingFields);
            throw new ServiceException("模板错误，请使用正确的模板! 缺失字段：" + CharSequenceUtil.join(StrPool.COMMA, missingFields));
        }
        List<String> insertColList = new ArrayList<>();
        for (String head : header.values()) {
            if (regionImportCol.contains(head)) {
                insertColList.add(head);
            }
        }

        ApiRegion api = SpringUtil.getBean(ApiRegion.class);

        // 构建sys_region关系
        Map<String, String> regionMap = buildRegionMap();

        for (Map<Integer, String> row : batchData) {
            ApiSysRegionAddParam regionCreateParam = new ApiSysRegionAddParam();
            for (Integer lieNum : row.keySet()) {
                String tempHead = header.get(lieNum);
                String valStr = row.get(lieNum);
                // 处理空格
                if (StrUtil.isNotBlank(valStr)) {
                    valStr = valStr.replaceAll("\\s+", "");
                }
                // 映射字段
                regionCreateParam = convertRegionCreateInfo(tempHead, valStr, regionCreateParam);

            }
            if (regionMap.containsKey(regionCreateParam.getRegionCode())) {
                String basicRegionName = regionMap.get(regionCreateParam.getRegionCode());
                if (Objects.equals(basicRegionName, regionCreateParam.getRegionName())) {
                    continue;
                } else {
                    throw new ServiceException("已存在的行政区域编码：" + regionCreateParam.getRegionCode());
                }
            }
            String parentCode = regionCreateParam.getParentCode();
            if (!regionMap.containsKey(parentCode)) {
                if (!"0".equals(parentCode)) {
                    throw new ServiceException("父级行政区域编码不存在：" + regionCreateParam.getParentCode() + ",0为起始父级编码");
                }
            }
            if (StrUtil.isBlank(regionCreateParam.getRegionCode())) {
                throw new ServiceException("行政区域编码不能为空！为空行信息：" + regionCreateParam.toString());
            }
            if (StrUtil.isBlank(regionCreateParam.getRegionName())) {
                throw new ServiceException("行政区域名称不能为空！行信息：" + regionCreateParam.toString());
            }
            if (StrUtil.isBlank(regionCreateParam.getParentCode())) {
                throw new ServiceException("父级行政区域编码不能为空！行信息：" + regionCreateParam.toString());
            }

            api.addRegion(regionCreateParam);
            regionMap = buildRegionMap();
        }
        return header.toString();
    }

    private ApiSysRegionAddParam convertRegionCreateInfo(String tempHead, String valStr, ApiSysRegionAddParam regionCreateParam) {
        switch (tempHead) {
            case "region_code":
                regionCreateParam.setRegionCode(valStr);
                break;
            case "region_name":
                regionCreateParam.setRegionName(valStr);
                break;
            case "parent_code":
                regionCreateParam.setParentCode(valStr);
                break;
            case "sort":
                Integer sort = 0;
                if (StrUtil.isNotBlank(valStr)) {
                    try {
                        sort = Integer.parseInt(valStr);
                    } catch (Exception e) {
                        throw new ServiceException("排序字段(sort)类型异常！异常值：" + valStr);
                    }
                }
                regionCreateParam.setSort(sort);
                break;
//            case "父级code集合":
//                regionCreateParam.setParentCode(valStr);
//                break;
//            case "备注":
//                regionCreateParam.setRemark(valStr);
//                break;
//            case "区域类型":
//                regionCreateParam.setRegionType(valStr);
//                break;
//            case "区域面积(KM2)":
//                regionCreateParam.setRegionArea(valStr);
//                break;
//            case "区域人口":
//                regionCreateParam.setRegionPeopleNumber(valStr);
//                break;
//            case "区域级别":
//                regionCreateParam.setRegionLevel(valStr);
//                break;
//            case "区域负责人":
//                regionCreateParam.setRegionLeader(valStr);
//                break;
//            case "扩展字段值":
//                regionCreateParam.setExtend(valStr);
//                break;
            default:
                break;

        }
        return regionCreateParam;
    }

    public String exportUserTemplate(HttpServletResponse response) {
        // 表格标题，就是模型的属性名
        String name = "用户导入模板" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        List<String> excelPropertyFields = getExcelPropertyFields(UserImportExcel.class);
        List<List<String>> headers = ListUtils.newArrayList();
        for (String excelPropertyField : excelPropertyFields) {
            if ("错误信息".equals(excelPropertyField)) {
                continue;
            }
            List<String> field = new ArrayList<>(Collections.singletonList(excelPropertyField));
            headers.add(field);
        }
        List<List<Object>> sheetData = ListUtils.newArrayList();
        try {
            com.yunjin.excel.easyexcel.ExcelUtil.download(response, name, headers, sheetData);
        } catch (Exception e) {
            log.error("用户导入模板导出失败", e);
            throw new ServiceException("用户导入模板导出失败");
        }
        return null;
    }

    public static List<String> getExcelPropertyFields(Class<?> clazz) {
        List<String> result = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                String[] value = annotation.value();
                result.add(CollUtil.getFirst(Arrays.asList(value)));
            }
        }
        log.info("getExcelPropertyFields:{}", JSONUtil.toJsonPrettyStr(result));
        return result;
    }

    public String exportDeptTemplate(HttpServletResponse response) {
        // 表格标题，就是模型的属性名
        String name = "部门导入模板" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        List<List<String>> headers = ListUtils.newArrayList();
        List<String> id = new ArrayList<>(Arrays.asList("id"));
        List<String> department = new ArrayList<>(Arrays.asList("department"));
        List<String> department_type = new ArrayList<>(Arrays.asList("department_type"));
        List<String> department_type_name = new ArrayList<>(Arrays.asList("department_type_name"));
        List<String> region_code = new ArrayList<>(Arrays.asList("region_code"));
        headers.add(id);
        headers.add(department);
        headers.add(department_type);
        headers.add(department_type_name);
        headers.add(region_code);
        List<List<Object>> sheetData = ListUtils.newArrayList();
        try {
            com.yunjin.excel.easyexcel.ExcelUtil.download(response, name, headers, sheetData);
        } catch (Exception e) {
            log.error("部门导入模板导出失败", e);
            throw new ServiceException("部门导入模板导出失败");
        }
        return null;
    }

    public void exportRegionTemplate(HttpServletResponse response) {
        // 表格标题，就是模型的属性名
        String name = "区域导入模板" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        List<List<String>> headers = ListUtils.newArrayList();
        List<String> regionCode = new ArrayList<>(Arrays.asList("region_code"));
        List<String> regionName = new ArrayList<>(Arrays.asList("region_name"));
        List<String> parentCode = new ArrayList<>(Arrays.asList("parent_code"));
        List<String> sort = new ArrayList<>(Arrays.asList("sort"));
        headers.add(regionCode);
        headers.add(regionName);
        headers.add(parentCode);
        headers.add(sort);
        List<List<Object>> sheetData = ListUtils.newArrayList();
        try {
            com.yunjin.excel.easyexcel.ExcelUtil.download(response, name, headers, sheetData);
        } catch (Exception e) {
            log.error("部门导入模板导出失败", e);
            throw new ServiceException("部门导入模板导出失败");
        }
    }

    /**
     * @Title: importUserData
     * @Description: 用户信息导入
     * @Date: 2025/12/2 15:08
     * @Parameters: [file]
     * @Return java.lang.String
     */
    @Transactional(rollbackFor = Exception.class)
    public UserImportCheckVO importUserData(MultipartFile file) {
        // 保存导入的数据文件
        String filename = file.getOriginalFilename();
        if (CharSequenceUtil.isBlank(filename)) {
            throw new ServiceException("文件名不能为空");
        }
        // Map<Integer, String> header = new HashMap<>();
        // List<UserImportExcel> batchData;
        // 适配多级表头
        String extName = FileUtil.extName(filename);
        if (!CharSequenceUtil.equalsAny(extName, "xls", "xlsx")) {
            throw new ExcelException("不支持的excel文件类型");
        }
        UserExcelListener userExcelListener = new UserExcelListener(userService, UserImportExcel.class);
        try {
            EasyExcel.read(file.getInputStream(), UserImportExcel.class, userExcelListener).sheet().headRowNumber(1).doRead();
        } catch (IOException e) {
            throw new ServiceException("数据导入失败!");
        } catch (POIXMLException e) {
            throw new ServiceException("文件损坏，无法解析!");
        }
        List<UserImportExcel> dataList = userExcelListener.getCachedDataList();
        UserImportCheckVO vo = new UserImportCheckVO();

        // 存在错误信息
        if (CollUtil.isNotEmpty(userExcelListener.getErrorList())) {
            log.error("数据校验异常:{}", JSONUtil.toJsonPrettyStr(userExcelListener.getErrorList()));
            // throw new ServiceException("数据校验异常：" + JSONUtil.toJsonPrettyStr(userExcelListener.getErrorList()));

            // TODO 根据excel对象导出模版
            // return userErrorInfoExport(response, dataList);
            UserImportCheckVO.FileInfo fileInfo = userErrorInfoExport(filename, dataList);
            vo.setCheckResult(false);
            vo.setFileInfo(fileInfo);
            return vo;
        }
        List<UserImportExcel> errorList = importUserInfo(dataList);
        if (CollUtil.isNotEmpty(errorList)) {
            // 线程池执行部门缓存回滚
            DATA_POOL.execute(departmentService::initCache);
            // 手动rollback事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // TODO 根据excel对象导出模版
            // return userErrorInfoExport(response, dataList);
            UserImportCheckVO.FileInfo fileInfo = userErrorInfoExport(filename, dataList);
            vo.setCheckResult(false);
            vo.setFileInfo(fileInfo);
            return vo;
        }
        vo.setCheckResult(true);
        return vo;
    }

    @Nullable
    private String userErrorInfoExport(HttpServletResponse response, List<UserImportExcel> dataList) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=userImport.xlsx");

        try {
            EasyExcel.write(response.getOutputStream(), UserImportExcel.class)
                    .sheet("用户导入信息" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN))
                    .doWrite(dataList);
            return null;
        } catch (IOException e) {
            log.error("用户导入错误信息导出失败", e);
            throw new ServiceException("用户导入错误信息导出失败");
        }
    }

    @Nullable
    private UserImportCheckVO.FileInfo userErrorInfoExport(String filename, List<UserImportExcel> dataList) {
        // String waterMarkContent = UserUtil.getRealName() + "    " + phone;
        // 创建一个流，等待写入excel文件内容
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 将excel文件写入byteArrayOutputStream中
        EasyExcelFactory.write(byteArrayOutputStream)
                .inMemory(true)
                .autoCloseStream(Boolean.FALSE)
                .head(UserImportExcel.class)
                .sheet("sheet1")
                // 设置水印
                //.registerWriteHandler(new WaterMarkHandler(waterMarkContent))
                .doWrite(dataList);
        String fileName = String.format("%s_%s_%s", "导入用户", "错误数据", DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMATTER));

        // 上传导出的文件数据
        String exportBuketName = "user-import-error-data";
        ApiFile apiFile = SpringUtil.getBean(ApiFile.class);
        ApiFileUploadParam uploadParam = new ApiFileUploadParam().setNamespace(exportBuketName)
                .setFiles(Collections.singletonList(new ApiFileUploadParam.UploadFile()
                        .setName(String.format("%s.xlsx", fileName)).setBytes(byteArrayOutputStream.toByteArray())));
        Result<UploadResultAO> r = apiFile.upload(uploadParam);
        if (Boolean.FALSE.equals(r.getSuccess())) {
            log.error("import data error info upload failed: {}", r.getMessage());
            throw new ServiceException("导入用户信息-错误信息上传失败");
        }
        log.info("import user data error info upload file success");
        UploadResultAO data = r.getData();
        if (data == null) {
            return null;
        }
        log.info("import user data error info upload file:{}", r.getData().getGroupId());
        List<FileInfoAO> files = data.getFiles();
        if (CollUtil.isEmpty(files)) {
            return null;
        }
        FileInfoAO ao = files.get(0);
        UserImportCheckVO.FileInfo info = new UserImportCheckVO.FileInfo();
        info.setId(ao.getId());
        info.setGroupId(ao.getGroupId());
        info.setName(ao.getName());
        info.setUrl(ao.getUrl());
        return info;
    }

    /**
     * @Title: importUserInfo
     * @Description: 用户信息及对应角色、部门信息导入
     * @Date: 2025/12/2 17:29
     * @Parameters: [dataList]
     * @Return java.util.Map<java.lang.String, com.yunjin.org.pojo.entity.UserImport>
     */
    private List<UserImportExcel> importUserInfo(List<UserImportExcel> dataList) {

        Map<String, String> deptMap = Maps.newHashMap();
        Map<String, String> roleMap = Maps.newHashMap();
        Set<String> phones = new HashSet<>();

        // Map<String, UserImportExcel> failList = new HashMap<>(4);
        List<UserImportExcel> errorList = new ArrayList<>(dataList.size());
        HashSet<String> objects = Sets.newHashSet();
        for (UserImportExcel user : dataList) {
            String phone = user.getPhone();
            if (StrUtil.length(phone) > 11) {
                user.setErrorMsg("手机号不符合规范，请核对修改");
                errorList.add(user);
                continue;
            }
            if (!phones.add(phone)) {
                continue;
            }
            UserDTO exist = userService.getUserByPhone(phone);
            if (exist != null) {
                objects.add(phone);
                user.setErrorMsg("重复手机号");
                errorList.add(user);
                continue;

            }
            String roleString = user.getRole();

            List<String> roles = StrUtil.split(roleString, ",");
            HashSet<String> roleIds = Sets.newHashSet();
            for (String role : roles) {
                String roleId = roleMap.get(role);
                if (roleId == null) {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setName(role);
                    roleDTO = roleService.getOrAdd(roleDTO);
                    roleId = roleDTO.getId();
                    roleMap.put(role, roleId);
                }
                roleIds.add(roleId);
            }

            String regionCode = user.getRegionCode();
            String realname = user.getRealname();
            UserDTO dto = new UserDTO();
            dto.setAccount(user.getAccount());
            dto.setRealname(realname);
            dto.setPhone(phone);
            dto.setAdmin(false);
            dto.setRoleIds(roleIds);
            dto.setRegionCode(regionCode);
            String gender = user.getGender();
            UserGenderEnum ge = UserGenderEnum.UNKNOWN;
            if (StrUtil.isNotBlank(gender)) {
                switch (gender) {
                    case "男":
                        ge = UserGenderEnum.MALE;
                        break;
                    case "女":
                        ge = UserGenderEnum.FEMALE;
                        break;
                    default:
                        ge = UserGenderEnum.UNKNOWN;
                }
            }
            dto.setGender(ge);

            String deptString = user.getDepartment();

            List<String> departments = StrUtil.split(deptString, ",");
            HashSet<String> deptIds = Sets.newHashSet();
            Integer deptSort = user.getDeptSort() == null ? 0 : user.getDeptSort();
            try {
                for (String department : departments) {
                    String departmentId = deptMap.get(department);
                    if (departmentId == null) {
                        // 添加新的部门
                        List<String> depts = StrUtil.split(department, StrUtil.DASHED);
                        List<String> newList = new ArrayList<>();
                        List<String> deptCom = new ArrayList<>();
                        for (String dept : depts) {
                            newList.add(dept);
                            deptCom.add(StrUtil.join(StrUtil.DASHED, newList));
                        }
                        String parentId = null;
                        for (int i = 0; i < deptCom.size(); i++) {
                            String s = deptCom.get(i);
                            String did = deptMap.get(s);
                            if (did == null) {
                                // 检查数据库是否存在此部门
                                String deptName = i == 0 ? s : StrUtil.subAfter(s, StrUtil.DASHED, true);
                                DepartmentDTO departmentDTO = departmentService.getDepartment(parentId, deptName);
                                if (departmentDTO == null) {
                                    departmentDTO = new DepartmentDTO().setDeptName(deptName).setParentId(i == 0 ? "0" : parentId);
                                    departmentDTO.setRegionCode(regionCode);
                                    departmentDTO.setDepartmentType(user.getDepartmentType());
                                    departmentDTO.setSort(deptSort);
                                    departmentService.saveOrUpdate(departmentDTO);
                                }
                                String id = departmentDTO.getId();
                                deptMap.put(s, id);
                                did = id;
                            }
                            parentId = did;
                        }
                        departmentId = parentId;
                    }
                    deptIds.add(departmentId);
                }
            } catch (Exception e) {
                user.setErrorMsg("部门添加失败:" + e.getMessage());
                errorList.add(user);
                continue;
            }

            dto.setDepartmentIds(deptIds);
            // 设置部门角色
            List<UserDeptRoleDTO> deptRoles = new ArrayList<>();
            // TODO 部门、角色目前仅支持单个传入
            String departmentTypeLevel = user.getDepartmentType().substring(user.getDepartmentType().lastIndexOf("-") + 1);
            for (String deptId : deptIds) {
                for (String roleId : roleIds) {
                    UserDeptRoleDTO deptRole = new UserDeptRoleDTO();
                    deptRole.setDepartmentId(deptId);
                    deptRole.setDepartmentType(departmentTypeLevel);
                    deptRole.setRoleId(roleId);
                    deptRoles.add(deptRole);
                }
            }
            dto.setDeptRoles(deptRoles);
            Integer userSort = user.getUserSort() == null ? 0 : user.getUserSort();
            dto.setSort(userSort);
            try {
                userService.addUser(dto);
            } catch (Exception e) {
                log.error("用户添加失败：{}", dto, e);
                user.setErrorMsg("用户添加失败:" + e.getMessage());
                // failList.put(e.getMessage(), user);
                errorList.add(user);
            }

        }
        log.error("重复手机号：{}", StrUtil.join(",", objects));
        // return failList;
        return errorList;
    }


    /* ================ 1. 正则池 ================= */
    private static final Pattern PROVINCE = Pattern.compile(".+?(省|自治区|维吾尔自治区|壮族自治区|宁夏回族自治区|西藏自治区|特别行政区|北京市|重庆市|天津市|上海市)$");
    private static final Pattern CITY = Pattern.compile(".+?(市|地区|自治州|盟)$");
    private static final Pattern COUNTY = Pattern.compile(".+?(区|县|自治县|旗|自治旗|县级市|林区|特区)$");
    private static final Pattern STREET = Pattern.compile(".+?(街道|镇|乡|民族乡|苏木|民族苏木)$");
    private static final Pattern COMMUNITY = Pattern.compile(".+?(社区|村|居委会|村委会|居委|村委)$");


    /* ================ 2. 核心方法 ================ */
    public static int level(String name) {
        if (name == null || name.isEmpty()) {
            return -1;
        }
        String n = name.trim();
        if (PROVINCE.matcher(n).find()) {
            return 0;
        }
        if (CITY.matcher(n).find()) {
            return 1;
        }
        if (COUNTY.matcher(n).find()) {
            return 2;
        }
        if (STREET.matcher(n).find()) {
            return 3;
        }
        if (COMMUNITY.matcher(n).find()) {
            return 4;
        }
        return -1;
    }


    // 常规角色
    List<String> roleIds = Lists.newArrayList("部门管理员", "普通用户");


    List<String> depts = Lists.newArrayList("人民政府", "组织部", "财政局", "人社局", "教育局", "卫健局",
            "住建局", "市场监管局", "行政审批局", "综合执法局",
            "司法局", "民政局", "交通局", "农业农村局", "商务局",
            "科技局", "文广旅局", "审计局", "统计局", "应急局",
            "信访局", "投促局", "规自局", "生态环境局", "国资委");

    Map<String, String> levelMap = new HashMap<String, String>() {
        {
            put("00", "省");
            put("01", "市");
            put("02", "区");
            put("03", "镇（街道）");
            put("04", "村（社区）");
            put("05", "网格");
        }
    };


    /**
     * 根据区域生成测试用户信息（主要用于测试）
     *
     * @param response 参数0
     * @throws
     * @author Carlos
     * @date 2025-12-31 13:59
     */
    public void generate(HttpServletResponse response) {
        ApiRegion apiRegion = SpringUtil.getBean(ApiRegion.class);
        Result<List<SysRegionAO>> result = apiRegion.getRegionTree();
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }


        List<SysRegionAO> data = result.getData();
        String parentName = "";
        List<UserImportExcel> list = new ArrayList<>();

        // 递归生成
        generateRev(data, parentName, list);
        userErrorInfoExport(response, list);
    }

    private void generateRev(List<SysRegionAO> data, String parentName, List<UserImportExcel> list) {
        int deptSort = 0;
        for (SysRegionAO re : data) {
            String regionCode = re.getRegionCode();
            String regionName = re.getRegionName();
            int level = level(regionName);
            if (level == -1) {
                continue;
            }
            String deptName;
            // 省级单位，重置父级
            if (level == 0) {
                parentName = "";
            }
            // level前补0
            String deptType = "0" + level;
            if (StrUtil.isBlank(parentName)) {
                deptName = regionName;
            } else {
                deptName = parentName + "-" + regionName;
            }
            String levelName = levelMap.get(deptType);
            if (StrUtil.isBlank(levelName)) {
                continue;
            }

            String suffix = "-" + levelName + "-" + deptType;
            String typeFullName = "01" + suffix;
            // 添加该部门的直属人员
            for (String roleId : roleIds) {
                String name = RandomPersonUtil.randomName();
                String phone = RandomPersonUtil.randomMobile();
                UserImportExcel excel = new UserImportExcel();
                excel.setAccount(phone);
                excel.setRealname(name);
                excel.setPhone(phone);
                excel.setRole(roleId);
                excel.setDepartment(deptName);
                excel.setDepartmentType(deptType);
                excel.setDepartmentTypeName(typeFullName);
                excel.setRegionCode(regionCode);
                excel.setUserSort(1);
                excel.setDeptSort(deptSort);
                list.add(excel);
            }

            deptSort++;

            // 省市区下有委办局, 所以该地区之下需要添加额外的部门
            if (level == 0 || level == 1 || level == 2) {
                int sort = deptSort;
                for (String dept : depts) {
                    for (String roleId : roleIds) {
                        String name = RandomPersonUtil.randomName();
                        String phone = RandomPersonUtil.randomMobile();
                        UserImportExcel excel = new UserImportExcel();
                        excel.setAccount(phone);
                        excel.setRealname(name);
                        excel.setPhone(phone);
                        excel.setRole(roleId);
                        excel.setDepartment(deptName + "-" + dept);
                        excel.setDepartmentType(deptType);
                        excel.setDepartmentTypeName("02" + suffix);
                        excel.setRegionCode(regionCode);
                        excel.setUserSort(100);
                        excel.setDeptSort(sort);
                        list.add(excel);
                    }
                    sort++;
                }
            }

            List<SysRegionAO> children = re.getChildren();
            if (children != null && !children.isEmpty()) {
                generateRev(children, deptName, list);
            }
        }


    }
}
