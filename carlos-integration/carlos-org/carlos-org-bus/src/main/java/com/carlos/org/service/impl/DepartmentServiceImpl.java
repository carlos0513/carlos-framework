package com.carlos.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.base.RegionInfo;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.core.response.Result;
import com.carlos.core.util.ExecutorUtil;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.UserUtil;
import com.carlos.org.convert.DepartmentConvert;
import com.carlos.org.enums.DeptRelationEnum;
import com.carlos.org.manager.*;
import com.carlos.org.mapper.DepartmentMapper;
import com.carlos.org.param.DepartmentCreateOrUpdateParam;
import com.carlos.org.pojo.ao.CommonCustomAO;
import com.carlos.org.pojo.ao.UserLoginAO;
import com.carlos.org.pojo.dto.*;
import com.carlos.org.pojo.entity.Department;
import com.carlos.org.pojo.entity.UserDepartment;
import com.carlos.org.pojo.enums.OrgDockingTypeEnum;
import com.carlos.org.pojo.excel.UserRegionInitExcel;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.vo.DepartmentBaseVO;
import com.carlos.org.pojo.vo.DepartmentStepTreeVO;
import com.carlos.org.pojo.vo.ThirdDepartmentVO;
import com.carlos.org.service.DepartmentRoleService;
import com.carlos.org.service.DepartmentService;
import com.carlos.org.service.OrgDockingMappingService;
import com.carlos.org.service.UserDepartmentService;
import com.carlos.system.api.ApiRegion;
import com.carlos.system.api.ApiSystemConfig;
import com.carlos.system.pojo.ao.SysConfigAO;
import com.carlos.system.pojo.ao.SysRegionAO;
import com.google.common.collect.Lists;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.util.Strings;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>
 * 部门 业务接口实现类
 * </p>
 *
 * @author yunjin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final static String DATA_STEWARD = "数据专员";

    private final DepartmentManager departmentManager;
    private final DepartmentRoleService departmentRoleService;
    private final UserDepartmentService userDepartmentService;
    private final UserDepartmentManager userDepartmentManager;
    private final UserManager userManager;
    private final OrgDockingMappingManager orgDockingMappingManager;
    private final OrgDockingMappingService orgDockingMappingService;
    private final RoleManager roleManager;

    private final DepartmentMapper departmentMapper;

    private static final ThreadPoolExecutor DEPARTMENT_OPERATE_POOL = ExecutorUtil.get(2, 5, "departmentOperate-", 20, null);

    @Override
    public void saveOrUpdate(DepartmentDTO dto) {
        String parentId = dto.getParentId();
        if (CharSequenceUtil.isBlank(parentId) || "0".equals(parentId)) {
            dto.setParentId("0");
            dto.setLevel(1);
        }
        // 名称重复校验
        checkDeptName(dto);
        //处理 departmentLevelCode 的对应关系
        dto.setDepartmentLevelCode(dto.getDepartmentType().substring(dto.getDepartmentType().lastIndexOf("-") + 1));
        // 新增场景
        if (StrUtil.isBlank(dto.getId())) {
            String parentCode = CharSequenceUtil.EMPTY;
            if (!"0".equals(parentId)) {
                DepartmentDTO parent = departmentManager.getDtoById(parentId);
                dto.setLevel(parent.getLevel() + 1);
                parentCode = parent.getDeptCode();
            }
            // 获取部门编号
            dto.setDeptCode(parentCode + getNextDepartmentCode(parentCode, dto.getLevel()));
            if (!this.departmentManager.addOrUpdate(dto)) {
                // 保存失败的应对措施
                throw new ServiceException("部门新增失败");
            }
        } else {
            // 编辑场景
            updateDepartment(dto);
        }
    }

    /**
     * 校验名字是否存在
     *
     * @param dto
     */
    private void checkDeptName(DepartmentDTO dto) {
        String parentId = dto.getParentId();
        String deptName = dto.getDeptName();
        if (!parentId.equals("0")) {
            List<DepartmentDTO> dtos = this.departmentManager.getParentDepartment(parentId, 20);
            // 判断上级
            Optional<DepartmentDTO> anyMatch = dtos.stream().filter(d -> d.getDeptName().equals(deptName)).findAny();
            anyMatch.ifPresent(d -> {
                if ((StrUtil.isNotBlank(dto.getId()) && !dto.getId().equals(d.getId())) || StrUtil.isBlank(dto.getId())) {
                    throw new ServiceException("机构名称已经在上级部门中存在！");
                }
            });
        }
        List<DepartmentDTO> sameLevelDepartments = getSubDepartment(parentId);
        Optional<DepartmentDTO> anyMatch = sameLevelDepartments.stream().filter(d -> d.getDeptName().equals(deptName)).findAny();
        anyMatch.ifPresent(d -> {
            if ((StrUtil.isNotBlank(dto.getId()) && !dto.getId().equals(d.getId())) || StrUtil.isBlank(dto.getId())) {
                throw new ServiceException("机构名称已经在同级别部门中存在！");
            }
        });

    }

    /**
     * 获取下一个部门编号
     *
     * @param parentCode 上级部门编号
     * @param level      部门级别
     * @return java.lang.String
     * @author Carlos
     * @date 2023/4/2 14:42
     */
    private synchronized String getNextDepartmentCode(String parentCode, Integer level) {
        // 避免code 重复，使用 synchronized 修饰
        Integer codeLength = parentCode.length();
        Long count = departmentManager.getDeptCodeCountWithDeleted(codeLength + 4, parentCode);
        if (count == null) {
            count = 0L;
        }
        String prefix = numberToLetter(level);
        String number = CharSequenceUtil.padPre((++count + ""), 3, "0");
        return prefix + number;
    }


    /**
     * 数值转化为字母
     */
    public static String numberToLetter(int num) {
        if (num <= 0) {
            return null;
        }
        StringBuilder letter = new StringBuilder();
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            letter.insert(0, ((char) (num % 26 + (int) 'A')));
            num = ((num - num % 26) / 26);
        } while (num > 0);

        return letter.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(Set<Serializable> ids) {
        for (Serializable id : ids) {
            this.deleteRecursion(id);
        }
    }

    @Override
    public void deleteRecursion(Serializable departmentId) {
        List<DepartmentDTO> departments = this.departmentManager.listAll();
        Map<String, List<DepartmentDTO>> parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        List<UserDepartmentDTO> refs = this.userDepartmentService.getAllRef();

        Map<String, List<UserDepartmentDTO>> refGroups = refs.stream().collect(Collectors.groupingBy(UserDepartmentDTO::getDepartmentId));

        DepartmentDTO department = this.departmentManager.getDtoById(String.valueOf(departmentId));
        if (department == null) {
            throw new ServiceException("部门不存在或已删除");
        }
        this.deleteRecursion(Lists.newArrayList(department), parentGroup, refGroups);
    }

    private void deleteRecursion(List<DepartmentDTO> currentLevel, Map<String, List<DepartmentDTO>> parentGroup,
                                 Map<String, List<UserDepartmentDTO>> refGroups) {
        for (DepartmentDTO item : currentLevel) {
            // 找出子集
            String id = item.getId();

            List<DepartmentDTO> subs = parentGroup.get(id);
            if (CollectionUtil.isNotEmpty(subs)) {
                this.deleteRecursion(subs, parentGroup, refGroups);
            }
            List<UserDepartmentDTO> users = refGroups.get(id);
            if (CollectionUtil.isNotEmpty(users)) {
                throw new ServiceException("“" + item.getDeptName() + "”下存在绑定用户，无法删除！");
            }
            this.departmentManager.delete(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDepartment(DepartmentDTO dto) {
        DepartmentDTO oldInfo = departmentManager.getDtoById(dto.getId());
        dto.setDeptCode(oldInfo.getDeptCode());
        String newParentId = dto.getParentId();
        // 获取子部门ids
        String id = dto.getId();
        if (newParentId.equals(id)) {
            throw new ServiceException("上级组织机构不能为当前修改的组织机构！");
        }
        // 部门名称校验
        DepartmentDTO dept = this.departmentManager.getDepartmentByName(dto.getParentId(), dto.getDeptName());
        if (dept != null && !Objects.equals(dept.getId(), dto.getId())) {
            throw new ServiceException("机构名称已经存在！");
        }
        if (!newParentId.equals(oldInfo.getParentId())) {
            // 父级发生变化
            Set<DepartmentDTO> allSubDeptById = departmentManager.getAllSubDeptById(id);
            // 编辑场景，先查本部门下原有的子/孙机构，不能直接选子机构为父
            if (allSubDeptById.stream().anyMatch(departmentDTO -> departmentDTO.getId().equals(newParentId))) {
                throw new ServiceException("上级组织机构不能为当前修改的组织机构的下级组织机构！");
            }
            if (!newParentId.equals("0")) {
                DepartmentDTO newParent = departmentManager.getDtoById(newParentId);
                dto.setLevel(newParent.getLevel() + 1);
            }
            // 有结构变动,重新计算level
            int levelChanged = dto.getLevel() - oldInfo.getLevel();
            allSubDeptById.forEach(item -> item.setLevel(item.getLevel() + levelChanged));

            departmentManager.saveOrUpdateBatch(DepartmentConvert.INSTANCE.toDOS(allSubDeptById));

        }
        String oldLevel = oldInfo.getDepartmentLevelCode();
        String newLevel = dto.getDepartmentLevelCode();
        // 部门层级有变动,处理角色层级关系
        if (!oldLevel.equals(newLevel)) {
            // 所有的用户关系
            List<UserDepartmentDTO> byDepartmentId = userDepartmentService.getByDepartmentId(id);
            List<DepartmentRoleDTO> departmentRoleDTOS = departmentRoleService.listAll();
            Map<String, Set<String>> levelRoles = departmentRoleDTOS.stream()
                    .collect(Collectors.groupingBy(DepartmentRoleDTO::getDepartmentType, Collectors.mapping(DepartmentRoleDTO::getRoleId, Collectors.toSet())));
            Set<String> oldRoles = levelRoles.get(oldLevel);
            Set<String> newRoles = levelRoles.get(newLevel);
            // 找出老层级有但是新层级没有的角色
            Set<String> removeRoles = oldRoles.stream().filter(item -> !newRoles.contains(item)).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(removeRoles)) {
                // 删除角色
                List<UserDepartmentDTO> collect = byDepartmentId.stream().peek(item -> {
                    item.setDepartmentLevelCode(newLevel);
                    if (item.getRoleId() != null && removeRoles.contains(item.getRoleId())) {
                        item.setRoleId(null);
                    }
                }).collect(Collectors.toList());
                userDepartmentService.batchUpdateUserDepartment(collect);
            }
        }
        boolean success = this.departmentManager.modify(dto);
        // 涉及批量修改，刷新部门缓存
        DEPARTMENT_OPERATE_POOL.submit(departmentManager::initCache);
        if (!success) {
            // 修改失败操作
            throw new ServiceException("机构修改失败");
        }
    }

    @Override
    public DepartmentDTO getDepartmentById(String id) {
        return this.departmentManager.getDtoById(id);
    }

    @Override
    public List<DepartmentDTO> departmentTree(Serializable departmentId, boolean userFlag) {
        UserContext userContext = ExtendInfoUtil.getUserContext();
        List<DepartmentDTO> departments = this.departmentManager.listAll();
        Map<String, List<DepartmentDTO>> parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        List<DepartmentDTO> currentLevel;
        if (departmentId != null) {
            if (departmentId.equals("0")) {
                currentLevel = departmentManager.getDepartmentsByParentId("0");
            } else {
                DepartmentDTO department = this.departmentManager.getDtoById(String.valueOf(departmentId));
                if (department != null) {
                    currentLevel = Lists.newArrayList(department);
                } else {
                    return Collections.emptyList();
                }
            }
        } else {
//            获取当前用户所在机构id列表
            Set<Serializable> departmentIds;
            if (userContext.getDepartmentId() != null) {
                departmentIds = new HashSet<>();
                departmentIds.add(userContext.getDepartmentId());
            } else {
                departmentIds = userContext.getDepartmentIds();
            }

//            找到部门id列表所在位置
            currentLevel = departments.stream().filter(o -> departmentIds.contains(o.getId())).collect(Collectors.toList());
            List<DepartmentDTO> list = new ArrayList<>(currentLevel);
//            筛选 移除有包含关系的子部门
            for (DepartmentDTO dto : list) {
                Set<Serializable> allSubDepartmentId = getAllSubDepartmentId(dto.getId());
                currentLevel.removeIf(o -> allSubDepartmentId.contains(o.getId()));
            }
//            currentLevel = parentGroup.get("0");
            currentLevel.sort(Comparator.nullsLast(Comparator.comparing(DepartmentDTO::getSort)).thenComparing(DepartmentDTO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())));
        }
        Map<String, List<UserDepartmentDTO>> users = null;
        if (userFlag) {
            // 获取用户信息
            List<UserDepartmentDTO> refs = userDepartmentManager.listAllRef();
            users = refs.stream().collect(Collectors.groupingBy(UserDepartmentDTO::getUserId));
        }
        long start = System.currentTimeMillis();

        this.buildTree(currentLevel, parentGroup, users);
        if (log.isDebugEnabled()) {
            log.debug("method buildTree time:{}", DateUtil.formatBetween(DateUtil.spendMs(start)));
        }
        return currentLevel;
    }

    @Override
    public List<DepartmentDTO> subTree(Boolean userFlag) {
        List<DepartmentDTO> departments = this.departmentManager.listAll();
        Map<String, List<DepartmentDTO>> parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        List<DepartmentDTO> currentLevel;

        UserLoginAO.Department department = UserUtil.getDepartment();
        if (department == null) {
            log.error("current user department is null");
            throw new ServiceException("当前用户部门信息为空");
        }

        currentLevel = parentGroup.get(department.getId());
        if (CollUtil.isEmpty(currentLevel)) {
            return Collections.emptyList();
        }
        List<DepartmentDTO> list = new ArrayList<>(currentLevel);
//            筛选 移除有包含关系的子部门
        for (DepartmentDTO dto : list) {
            Set<Serializable> allSubDepartmentId = getAllSubDepartmentId(dto.getId());
            currentLevel.removeIf(o -> allSubDepartmentId.contains(o.getId()));
        }
        currentLevel.sort(Comparator.nullsLast(Comparator.comparing(DepartmentDTO::getSort)).thenComparing(DepartmentDTO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())));

        Map<String, List<UserDepartmentDTO>> users = null;
        if (userFlag) {
            // 获取用户信息
            List<UserDepartmentDTO> refs = userDepartmentManager.listAllRef();
            users = refs.stream().collect(Collectors.groupingBy(UserDepartmentDTO::getUserId));
        }
        long start = System.currentTimeMillis();

        this.buildTree(currentLevel, parentGroup, users);
        if (log.isDebugEnabled()) {
            log.debug("method buildTree time:{}", DateUtil.formatBetween(DateUtil.spendMs(start)));
        }
        return currentLevel;
    }

    @Override
    public DepartmentDTO getCurrentAndSubLeveDept() {
        String currentDeptId = UserUtil.getDepartment().getId();
        DepartmentDTO currentDepartment = getDepartmentById(currentDeptId);
        List<DepartmentDTO> subDepartment = getSubDepartment(currentDeptId);
        currentDepartment.setChildren(subDepartment);
        return currentDepartment;
    }

    @Override
    public List<DepartmentDTO> getSameAndSubLeveDept() {
        List<DepartmentDTO> departments = this.departmentManager.getDepartmentsByParentId("0");
        if (CollUtil.isEmpty(departments)) {
            return Collections.emptyList();
        }
        return getSameAndSubLeveDept(departments.get(0).getId());
    }

    private List<DepartmentDTO> getSameAndSubLeveDept(String deptId) {
        List<DepartmentDTO> sameLevelDepartment = getSameLevelDepartment(deptId, true, true);
        if (CollUtil.isEmpty(sameLevelDepartment)) {
            return Collections.emptyList();
        }
        sameLevelDepartment.forEach(this::getSubDepartment);
        return sameLevelDepartment;
    }

    private void getSubDepartment(DepartmentDTO dept) {
        List<DepartmentDTO> subDepartment = getSubDepartment(dept.getId());
        RoleDTO roleByName = roleManager.getByName(DATA_STEWARD);
        subDepartment.forEach(i -> {
            List<UserDepartmentDTO> users = userDepartmentService.getByDepartmentId(i.getId());
            // 只筛选数据专员用户
            Map<String, List<UserDepartmentDTO>> collect = users.stream().collect(Collectors.groupingBy(UserDepartmentDTO::getRoleId));
            if (collect.containsKey(roleByName.getId())) {
                i.setUsers(collect.get(roleByName.getId()));
            }
            getSubDepartment(i);
        });
        dept.setChildren(subDepartment);
    }

    @Override
    public DepartmentDTO parentDepartment(String deptCode) {
        DepartmentDTO department = departmentManager.getDepartmentByCode(deptCode);
        if (department == null) {
            return department;
        }
        return departmentManager.getDtoById(department.getParentId());
    }

    @Override
    public Set<String> getCurrentAndAllSubDepartmentId(String deptCode) {
        return departmentManager.getCurrentAndAllSubDepartmentId(deptCode);
    }

    private void buildTree(List<DepartmentDTO> currentLevel, Map<String, List<DepartmentDTO>> parentGroup, Map<String, List<UserDepartmentDTO>> users) {
        for (DepartmentDTO item : currentLevel) {
            // 找出子集
            if (users != null) {
                item.setUsers(users.get(item.getId()));
            }
            List<DepartmentDTO> subs = parentGroup.get(item.getId());
            if (CollUtil.isEmpty(subs)) {
                continue;
            }
            subs.sort(Comparator.nullsLast(Comparator.comparing(DepartmentDTO::getSort)).thenComparing(DepartmentDTO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())));
            item.setChildren(subs);

            this.buildTree(subs, parentGroup, users);
        }
    }

    @Override
    public List<DepartmentDTO> getSameLevelDepartment(Serializable departmentId, boolean userFlag, boolean dataStewardUserFlag) {
        DepartmentDTO dept = this.departmentManager.getDtoById(String.valueOf(departmentId));
        if (dept == null) {
            return Collections.emptyList();
        }
        List<DepartmentDTO> departments = this.departmentManager.getDepartmentsByParentId(dept.getParentId());
        RoleDTO byName = roleManager.getByName(DATA_STEWARD);
        // 排除当前部门
        departments.forEach(i -> {
            if (userFlag) {
                List<UserDepartmentDTO> users = userDepartmentService.getByDepartmentId(i.getId());
                // 只筛选数据专员用户
                if (dataStewardUserFlag) {
                    Map<String, List<UserDepartmentDTO>> collect = users.stream().collect(Collectors.groupingBy(UserDepartmentDTO::getRoleId));
                    if (collect.containsKey(byName.getId())) {
                        users = collect.get(byName.getId());
                    }
                }
                i.setUsers(users);
            }
        });
        return departments;
    }

    @Override
    public List<DepartmentDTO> getPeerLevelDepartment(Serializable departmentId) {
        DepartmentDTO dept = this.departmentManager.getDtoById(String.valueOf(departmentId));
        if (dept == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getRegionCode, dept.getRegionCode());
        List<Department> departments = departmentMapper.selectList(queryWrapper);
        return DepartmentConvert.INSTANCE.toDTO(departments);
    }

    @Override
    public List<String> previewDepartmentName(String id, Integer limit) {
        if (CharSequenceUtil.isEmpty(id)) {
            return Collections.emptyList();
        }

        List<DepartmentDTO> dtos = this.departmentManager.getParentDepartment(id, limit);
        if (CollUtil.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(DepartmentDTO::getDeptName).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<DepartmentDTO>> getParentDepartmentMap(Set<String> ids, Integer limit) {
        return this.departmentManager.getParentDepartmentMap(ids, limit);
    }

    @Override
    public List<DepartmentDTO> getByUserId(String userId) {
        Set<String> deptIds = this.userDepartmentService.getDepartmentIdByUserId(userId);
        if (CollUtil.isEmpty(deptIds)) {
            throw new ServiceException("用户未与部门关联");
        }
        return this.departmentManager.getByIds(deptIds);
    }

    @Override
    public DepartmentInfo getDepartmentInfo(Serializable departmentId, Integer limit) {
        DepartmentDTO department = this.departmentManager.getDtoById(String.valueOf(departmentId));
        if (department == null) {
            return null;
        }
        return new DepartmentInfo().setId(departmentId).setCode(department.getDeptCode()).setName(department.getDeptName()).setFullName(
                this.previewDepartmentName(department.getId(), limit));
    }

    @Override
    public DepartmentInfo getDepartmentInfo(String deptCode, Integer limit) {
        DepartmentDTO department = this.departmentManager.getDepartmentByCode(deptCode);
        if (department == null) {
            return null;
        }
        return new DepartmentInfo().setId(department.getId()).setCode(department.getDeptCode()).setName(department.getDeptName()).setFullName(
                this.previewDepartmentName(department.getId(), limit));
    }

    @Override
    @Scheduled(cron = "0 0/5 * * * ?")
    public List<DepartmentDTO> getDepartments() {

        return this.departmentManager.listAll();
    }

    @Override
    public Set<Serializable> getAllSubDepartmentId(Serializable departmentId) {
        Set<DepartmentDTO> dtos = departmentManager.getAllSubDeptById(departmentId.toString());
        return dtos.stream().map(DepartmentDTO::getId).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAllSubDeptByIds(Set<String> ids) {
        Set<DepartmentDTO> dtos = departmentManager.getAllSubDeptByIds(ids);
        return dtos.stream().map(DepartmentDTO::getId).filter(id -> !ids.contains(id)).collect(Collectors.toSet());
    }

    @Override
    public DepartmentDTO getDepartmentDetail(String id, UserPageParam param) {
        DepartmentDTO department = departmentManager.getDtoById(id);
        // 获取用户信息
        PageInfo<UserDepartmentDTO> departmentUsers = userDepartmentService.getDepartmentUserPage(id, param);
        department.setUserPages(departmentUsers);
        department.setParentDeptName(departmentManager.getDepartmentName(department.getParentId()));
        if (!department.getParentId().equals("0")) {
            department.setFullDeptName(StrUtil.join(StrPool.SLASH, previewDepartmentName(department.getParentId(), 20)));
        }

        return department;
    }

    @Override
    public List<UserDepartmentDTO> getUser(String id) {
        // 获取用户信息
        List<UserDepartmentDTO> departmentUsers = userDepartmentService.getByDepartmentId(id);
        return departmentUsers;
    }

    @Override
    public List<DepartmentDTO> listByIds(Set<String> departmentId) {
        return departmentManager.getByIds(departmentId);
    }

    @Override
    public DepartmentDTO getDepartmentByCode(String code) {
        if (StrUtil.isEmpty(code)) {
            return null;
        }
        return departmentManager.getDepartmentByCode(code);
    }

    @Override
    public Paging<UserDTO> getNotInDeptUser(UserExcludeDeptPageParam param) {
////        // 获取用户信息
//        List<UserDepartmentDTO> departmentUsers = userDepartmentService.getByDepartmentId(param.getId());

        //获取当前部门及下面所有用户信息id,包含无部门的,不含本部门
        List<UserDepartmentDTO> currentAndChildrenDepartmentUserIds = departmentManager.getCurrentAndChildrenDepartmentUserIds(param.getId());
        // 在组织机构下的用户id
        Set<String> userSet = currentAndChildrenDepartmentUserIds.stream().map(UserDepartmentDTO::getUserId).collect(Collectors.toSet());

        // 分页获取所有符合信息的用户
        Paging<UserDTO> users = new Paging<>();
        users = userManager.getInDeptUser(param, userSet);
        if (CollUtil.isNotEmpty(users.getRecords())) {
            for (UserDTO dto : users.getRecords()) {
                Set<String> departmentIds = userDepartmentService.getDepartmentIdByUserId(dto.getId());
                if (CollUtil.isNotEmpty(departmentIds)) {
                    List<DepartmentDTO> department = departmentManager.getByIds(departmentIds);
                    if (CollUtil.isNotEmpty(department)) {
                        dto.setDepartment(
                                CharSequenceUtil.join(StrPool.COMMA, department.stream().map(DepartmentDTO::getDeptName).collect(Collectors.toList())));
                    }
                }
            }
        }
        return users;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDeptAdmin(DepartmentAdminParam param) {
        List<UserDepartment> entities = userDepartmentManager.lambdaQuery().eq(UserDepartment::getDepartmentId, param.getDeptId()).list();
        UserDepartment entity = null;
        for (UserDepartment item : entities) {
            if (item.getUserId().equals(param.getUserId())) {
                entity = item;
            }
            if (item.getIsAdmin() && param.getAdminFlag()) {
                throw new ServiceException("该部门已存在管理员，无法再次设置");
            }
        }
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("请选择正确的部门及用户");
        }

        entity.setIsAdmin(param.getAdminFlag());
        return userDepartmentManager.updateById(entity);
    }

    @Override
    public DepartmentDTO getDepartment(String parentId, String deptName) {
        if (StrUtil.isBlank(parentId)) {
            parentId = "0";
        }
        return this.departmentManager.getDepartmentByName(parentId, deptName);
    }

    @Override
    public List<DepartmentDTO> getSubDepartment(String parentId) {
        if (StrUtil.isBlank(parentId)) {
            return Collections.emptyList();
        }
        List<DepartmentDTO> dtos = departmentManager.getDepartmentsByParentId(parentId);
        if (CollUtil.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos;
    }

    @Override
    public List<DepartmentDTO> getDepartmentByLevel(int level) {
        return departmentManager.listByLevel(level);
    }

    @Override
    public List<DepartmentBaseVO> getCurrDepartments() {
        UserContext userContext = ExtendInfoUtil.getUserContext();
        assert ObjectUtils.isNotEmpty(userContext);
        List<DepartmentDTO> dtos = departmentManager.getDepartmentsByParentId((String) userContext.getDepartmentId());
        return BeanUtil.copyToList(dtos, DepartmentBaseVO.class);
    }

    @Override
    public Set<String> getDepartmentIdsRecurById(String root) {
        Set<Serializable> ids = new HashSet<>();
        ids.add(root);
        departmentManager.listAllDepartment(root, ids);
        return ids.stream().map(o -> (String) o).collect(Collectors.toSet());
    }

    @Override
    public List<DepartmentDTO> currentGrid() {
        List<DepartmentDTO> dtos = departmentManager.getCurrentGridByDeptCode(allSubDepartmentCode(UserUtil.getDepartment().getId(), true));
        // 网格级
        List<DepartmentDTO> grid = dtos.stream().filter(item -> item.getLevel().equals(4)).collect(Collectors.toList());
        // 微网格级
        Map<String, List<DepartmentDTO>> microgridMap = dtos.stream().filter(item -> item.getLevel().equals(5)).collect(
                Collectors.groupingBy(DepartmentDTO::getParentId));
        grid.forEach(item -> {
            item.setChildren(microgridMap.get(item.getId()));
        });
        return grid;
    }

    @Override
    public void changeRegion(String regionName) {
        ThreadPoolExecutor pool = ExecutorUtil.get(50, 50, "dept-region-init", 1000, null);

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
                // 处理四川省-成都市
                String fullName = StrUtil.join(StrUtil.DASHED, names);
                if (!fullName.contains(regionName)) {
                    return;
                }
                fullName = StrUtil.subAfter(fullName, regionName, false);
                fullName = regionName + fullName;
                map.put(fullName, data);
                log.info("num:{}", atomicInteger.addAndGet(1));
            });
            submits.add(submit);
        }
        for (Future<?> submit : submits) {
            try {
                submit.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }


        // 获取所有的部门
        List<DepartmentDTO> depts = departmentManager.listAll();
        // 获取所有区域
        List<UserRegionInitExcel> inits = new ArrayList<>();
        List<Department> entities = new ArrayList<>();
        for (DepartmentDTO i : depts) {
            Department user = new Department();
            user.setId(i.getId());
            List<String> list = previewDepartmentName(i.getId(), 20);
            String fullDeptName = StrUtil.join(StrUtil.DASHED, list);
            RegionInfo regionInfo = map.get(fullDeptName);
            if ((i.getLevel().equals(1) || i.getLevel().equals(2)) && !StrUtil.containsAny(i.getDeptName(), "街道", "镇")) {
                regionInfo = map.get(regionName);
            }
            UserRegionInitExcel excel = new UserRegionInitExcel();
            excel.setDeptCode(i.getDeptCode());
            excel.setDeptName(i.getDeptName());
            if (regionInfo == null) {
                excel.setSuccess(false);
                inits.add(excel);
                // 未匹配到区域
                log.error("未匹配到区域 deptName:{}", fullDeptName);
                continue;
            }
            excel.setRegionCode(regionInfo.getCode());
            excel.setRegionName(StrUtil.join(StrUtil.DASHED, regionInfo.getFullName()));
            excel.setSuccess(true);
            user.setRegionCode(regionInfo.getCode());
            inits.add(excel);
            entities.add(user);
        }

        departmentManager.updateBatchById(entities);
        ExcelWriter writer = ExcelUtil.getWriter("/data/region/dept_region_init_" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".xlsx");
        writer.write(inits);
        writer.flush();
        writer.close();
    }

    @Override
    public void initCache() {
        this.userDepartmentManager.initCache();
    }

    @Override
    public List<CommonCustomAO> getAactivityRatio(String startTime, String endTime, List<String> deptIds) {
        List<CommonCustomAO> res = new ArrayList<>(deptIds.size());
        for (String deptId : deptIds) {
            CommonCustomAO commonCustomAO = new CommonCustomAO();
            commonCustomAO.put("deptId", deptId);
            commonCustomAO.put("activityRatio", getAactivityRatioById(startTime, endTime, deptId));
            res.add(commonCustomAO);
        }
        return res;
    }

    @Override
    public Set<String> allSubDepartmentCode(String departmentId, boolean addSelf) {

        if (StrUtil.isBlank(departmentId)) {
            log.warn("departmentId is blank");
            return new HashSet<>();
        }

        Set<DepartmentDTO> dtos = departmentManager.getAllSubDeptById(departmentId);
        Set<String> allSub = dtos.stream().map(DepartmentDTO::getDeptCode).collect(Collectors.toSet());
        if (addSelf) {
            DepartmentDTO currentDept = getDepartmentById(departmentId);
            if (currentDept != null && StrUtil.isNotBlank(currentDept.getDeptCode())) {
                allSub.add(currentDept.getDeptCode());
            } else {
                log.warn("部门不存在或部门编码为空，部门ID: {}", departmentId);
            }
        }
        return allSub;
    }

    @Override
    public DepartmentDTO getDepartmentByRegionCode(String regionCode) {
        if (StrUtil.isEmpty(regionCode)) {
            return null;
        }
        return departmentManager.getDepartmentByRegionCode(regionCode);
    }

    // 获取单个部门的人员活跃度
    private String getAactivityRatioById(String startTime, String endTime, String deptId) {
        // 获取部门内人员ID
        Set<String> userIdByDepartmentId = userDepartmentService.getUserIdByDepartmentId(deptId);
        int total = CollUtil.emptyIfNull(userIdByDepartmentId).size();
        // 获取这段时间内登录的人员数目
        Long activity = userManager.getActivityCount(startTime, endTime, userIdByDepartmentId);
        return getRatio(activity, total);
    }

    private String getRatio(long activity, int total) {
        if (activity == 0L || total == 0) {
            return "0.00%";
        }
        BigDecimal numerator = new BigDecimal(activity);
        BigDecimal denominator = new BigDecimal(total);
        BigDecimal percentage = numerator.divide(denominator, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
        BigDecimal roundedPercentage = percentage.setScale(2, BigDecimal.ROUND_HALF_UP);
        return roundedPercentage + "%";
    }

    @Override
    public List<DepartmentDTO> allDepartmentByName(String name) {
        return this.departmentManager.listAllByName(name);
    }

    @Override
    public List<DepartmentDTO> getDepartmentByCodes(List<String> codes) {
        List<DepartmentDTO> list = new ArrayList<>();
        for (String code : codes) {
            DepartmentDTO departmentByCode = this.getDepartmentByCode(code);
            list.add(departmentByCode);
        }
        return list;
    }

    @Override
    public List<DepartmentDTO> treeAndUser() {
        List<DepartmentDTO> departments = this.departmentManager.listAll();
        if (CollUtil.isEmpty(departments)) {
            return Collections.emptyList();
        }

        List<OrgDockingMappingDTO> orgDockingMappingDTOS = orgDockingMappingManager.getDockingMappingList(OrgDockingTypeEnum.DEPARTMENT);
        Map<String, OrgDockingMappingDTO> mapping = orgDockingMappingDTOS.stream().collect(Collectors.toMap(OrgDockingMappingDTO::getSystemId, Function.identity()));
        departments.stream().forEach(item -> {
            if (mapping.containsKey(item.getId())) {
                OrgDockingMappingDTO dto = mapping.get(item.getId());
                item.setThirdDeptId(dto.getTargetId());
                item.setTargetCode(dto.getTargetCode());
            }
        });
        // 由于前端不再需要user内容，暂时注释
        // List<UserDepartmentDTO> refs = userDepartmentManager.listAll();
        // if (CollUtil.isNotEmpty(refs)) {
        // 查数据专员角色信息
        // RoleDTO roleByName = roleManager.getByName(DATA_STEWARD);
        // Map<String, Map<String, List<UserDepartmentDTO>>> deptUserGroup = refs.stream()
        //         .filter(u -> Objects.nonNull(u.getRoleId()))
        //         .collect(Collectors.groupingBy(UserDepartmentDTO::getDepartmentId, Collectors.groupingBy(UserDepartmentDTO::getRoleId)));
        // Set<String> userIds = new HashSet<>();
        // 排除当前部门
        // departments.forEach(i -> {
        //     if (deptUserGroup.containsKey(i.getId())) {
        //         // 只筛选数据专员用户
        //         if (deptUserGroup.get(i.getId()).containsKey(roleByName.getId())) {
        //             List<UserDepartmentDTO> users = deptUserGroup.get(i.getId()).get(roleByName.getId());
        //             i.setUsers(users);
        //             userIds.addAll(users.stream().map(UserDepartmentDTO::getUserId).collect(Collectors.toSet()));
        //         }
        //     }
        // });
        // ApiUser apiUser = SpringUtil.getBean(ApiUser.class);
        // Result<List<UserInfo>> userByIds = apiUser.getUserByIds(userIds);
        // List<UserInfo> userInfos = ResultHandleUtil.handleResult(userByIds);
        // departments.stream().filter(i -> i.getUsers() != null).forEach(dept -> {
        //     dept.getUsers().forEach(user -> {
        //         userInfos.stream().filter(u -> u.getId().equals(user.getUserId())).findFirst().ifPresent(u -> {
        //             user.setName(u.getName());
        //             user.setRealname(u.getRealName());
        //         });
        //     });
        // });
        // }

        Map<String, List<DepartmentDTO>> parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));
        List<DepartmentDTO> currentLevel = parentGroup.get("0");
        if (CollUtil.isEmpty(currentLevel)) {
            return Collections.emptyList();
        }
        long start = System.currentTimeMillis();

        this.buildTree(currentLevel, parentGroup, null);
        if (log.isDebugEnabled()) {
            log.debug("method buildTree time:{}", DateUtil.formatBetween(DateUtil.spendMs(start)));
        }
        return currentLevel;
    }


    @Override
    public List<DepartmentDTO> deptTree(DeptRelationEnum deptRelationEnum, Integer level) {
        UserLoginAO.Department department = UserUtil.getUser().getDepartment();

        List<DepartmentDTO> departments = this.departmentManager.listAll();
        if (CollUtil.isEmpty(departments)) {
            return Collections.emptyList();
        }

        List<OrgDockingMappingDTO> orgDockingMappingDTOS = orgDockingMappingManager.getDockingMappingList(OrgDockingTypeEnum.DEPARTMENT);
        Map<String, OrgDockingMappingDTO> mapping = orgDockingMappingDTOS.stream().collect(Collectors.toMap(OrgDockingMappingDTO::getSystemId, Function.identity()));
        departments.forEach(item -> {
            if (mapping.containsKey(item.getId())) {
                OrgDockingMappingDTO dto = mapping.get(item.getId());
                item.setThirdDeptId(dto.getTargetId());
                item.setTargetCode(dto.getTargetCode());
            }
        });

        List<DepartmentDTO> currentLevel = Lists.newArrayList();

        // 根据部门关系，获取部门树
        switch (deptRelationEnum) {
            case ALL:// 所有部门
                currentLevel = getDeptAll(departments, currentLevel);
                break;
            case CurrentAndSubset:// 当前部门和子集
                currentLevel = getDeptCurrentAndSubset(departments, department, currentLevel);
                break;
            case CurrentAndPeerLevel:// 当前部门和平级
                currentLevel = getDeptCurrentAndPeerLevel(departments, department, currentLevel);
                break;
            case CurrentAndPeerLevelAndSubset:// 当前部门和平级和子集
                currentLevel = getDeptCurrentAndPeerLevelAndSubset(departments, department, currentLevel);
                break;
            case SuperiorPeerLevelAndSubset:// 上级的平级的下级
                currentLevel = getDeptSuperiorPeerLevelAndSubset(departments, department, currentLevel, level);
                break;
            case CurrentAndPeerLevelAndSuperior:// 当前部门和平级和上级
                currentLevel = getDeptCurrentAndPeerLevelAndSuperior(departments, department, currentLevel);
                break;
            default:
                throw new ServiceException("不支持的部门关系类型: " + deptRelationEnum);
        }
        return currentLevel;
    }

    /**
     * 获取当前部门和平级和上级
     *
     * @param departments  所有部门
     * @param department   当前部门
     * @param currentLevel
     * @return list
     */
    private List<DepartmentDTO> getDeptCurrentAndPeerLevelAndSuperior(List<DepartmentDTO> departments, UserLoginAO.Department department, List<DepartmentDTO> currentLevel) {
        // 获取当前部门的平级部门（包括自身）
        List<DepartmentDTO> peerDepartments = departments.stream()
                .filter(d -> Objects.equals(d.getLevel(), department.getLevel())
                        && Objects.equals(d.getParentId(), department.getParentId()))
                .collect(Collectors.toList());

        // 收集所有需要展示的部门ID
        Set<String> needToShowDeptIds = new HashSet<>();

        // 添加平级部门（包括当前部门）
        peerDepartments.forEach(dept -> needToShowDeptIds.add(dept.getId()));

        // 获取这些部门的所有上级部门ID
        for (DepartmentDTO peer : peerDepartments) {
            List<DepartmentDTO> parents = getParentDepartments(peer.getId(), departments);
            parents.forEach(parent -> needToShowDeptIds.add(parent.getId()));
        }

        // 过滤出需要展示的部门
        List<DepartmentDTO> filteredDepartment = departments.stream()
                .filter(dept -> needToShowDeptIds.contains(dept.getId()))
                .collect(Collectors.toList());

        // 构建父子关系
        Map<String, List<DepartmentDTO>> parentGroup = filteredDepartment.stream()
                .collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        // 获取最顶层部门作为当前层级
        List<DepartmentDTO> topLevelDepartments = getParentDepartments(department.getId(), departments);
        String levelId = "0"; // 默认为根节点
        if (!topLevelDepartments.isEmpty()) {
            levelId = topLevelDepartments.get(0).getParentId();
        } else if (!peerDepartments.isEmpty()) {
            levelId = peerDepartments.get(0).getParentId();
        }

        currentLevel = parentGroup.get(levelId);
        if (CollUtil.isEmpty(currentLevel)) {
            return Collections.emptyList();
        }
        // 构建树形结构
        this.buildTree(currentLevel, parentGroup, null);
        return currentLevel;
    }

    /**
     * 上级的平级的下级
     * @param departments  所有部门
     * @param department   当前部门
     * @param currentLevel 当前层级数据
     * @param  level 要获取的上级级别（1表示直接上级，2表示上级的上级，以此类推）
     * @return list
     */
    private List<DepartmentDTO> getDeptSuperiorPeerLevelAndSubset(List<DepartmentDTO> departments, UserLoginAO.Department department, List<DepartmentDTO> currentLevel, Integer level) {
        Map<String, List<DepartmentDTO>> parentGroup;
        Map<String, DepartmentDTO> deptMap = departments.stream()
                .collect(Collectors.toMap(DepartmentDTO::getId, Function.identity()));

        // 获取指定级别的上级部门
        DepartmentDTO targetParentDept = getTargetParentDepartment(department, deptMap, level);


        if (targetParentDept != null) {
            // 获取上级部门的平级部门（包括上级部门本身）
            List<DepartmentDTO> parentPeerDepts = departments.stream()
                    .filter(dept -> Objects.equals(dept.getLevel(), targetParentDept.getLevel())
                            && Objects.equals(dept.getParentId(), targetParentDept.getParentId()))
                    .collect(Collectors.toList());

            // 收集这些平级部门的所有子部门ID
            Set<Serializable> subsetDeptIds = new HashSet<>();
            for (DepartmentDTO peerDept : parentPeerDepts) {
                Set<Serializable> subIds = getAllSubDepartmentId(peerDept.getId());
                subsetDeptIds.addAll(subIds);
            }

            // 过滤出需要展示的子部门
            List<DepartmentDTO> filteredDepartments = departments.stream()
                    .filter(dept -> subsetDeptIds.contains(dept.getId()))
                    .collect(Collectors.toList());

            // 合并平级部门和它们的子部门
            List<DepartmentDTO> allDeptsToShow = new ArrayList<>(parentPeerDepts);
            allDeptsToShow.addAll(filteredDepartments);

            // 构建父子关系
            parentGroup = allDeptsToShow.stream()
                    .collect(Collectors.groupingBy(DepartmentDTO::getParentId));

            // 当前层级为上级部门的父级
            String topLevelId = targetParentDept.getParentId();
            currentLevel = parentGroup.get(topLevelId);
        } else {
            // 如果找不到上级部门，所有的部门
            parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));
            currentLevel = parentGroup.get("0");
        }
        if (CollUtil.isEmpty(currentLevel)) {
            return Collections.emptyList();
        }
        // 构建树形结构
        this.buildTree(currentLevel, parentGroup, null);
        return currentLevel;
    }

    /**
     * 获取指定级别的上级部门
     *
     * @param department 当前部门
     * @param deptMap 部门映射
     * @param level 要获取的上级级别（1表示直接上级，2表示上级的上级，以此类推）
     * @return 指定级别的上级部门
     */
    private DepartmentDTO getTargetParentDepartment(UserLoginAO.Department department,
                                                    Map<String, DepartmentDTO> deptMap,
                                                    Integer level) {
        if (level == null || level <= 0) {
            level = 1; // 默认获取直接上级
        }

        DepartmentDTO currentDept = deptMap.get(department.getId());
        if (currentDept == null) {
            return null;
        }

        DepartmentDTO targetParent = currentDept;
        // 向上追溯指定级别
        for (int i = 0; i < level; i++) {
            if ("0".equals(targetParent.getParentId())) {
                return null; // 已经到顶级或无法继续追溯
            }
            targetParent = deptMap.get(targetParent.getParentId());
            if (targetParent == null) {
                return null;
            }
        }
        return targetParent;
    }

    /**
     * 当前部门和平级和子集
     * @param departments 所有部门
     * @param department  当前 部门
     * @param currentLevel 当前层级
     * @return list
     */
    private List<DepartmentDTO> getDeptCurrentAndPeerLevelAndSubset(List<DepartmentDTO> departments, UserLoginAO.Department department, List<DepartmentDTO> currentLevel) {
        // 过滤层级相同的部门，并且父级部门id相同,获取所有子部门
        Map<String, List<DepartmentDTO>> parentGroup = getParentGroup(departments, department);

        // 获取最顶层部门作为当前层级（即当前部门的父级）
        String topLevelId = department.getParentId();
        currentLevel = parentGroup.get(topLevelId);
        if (CollUtil.isEmpty(currentLevel)) {
            return Collections.emptyList();
        }
        // 构建树形结构
        this.buildTree(currentLevel, parentGroup, null);
        return currentLevel;
    }

    /**
     * 当前部门和平级
     * @param departments 所有部门
     * @param department  当前 部门
     * @param currentLevel 当前层级
     * @return list
     */
    private List<DepartmentDTO> getDeptCurrentAndPeerLevel(List<DepartmentDTO> departments, UserLoginAO.Department department, List<DepartmentDTO> currentLevel) {
        // 过滤层级相同的部门，并且父级部门id相同
        departments = departments.stream().filter(a -> Objects.equals(a.getLevel(), department.getLevel())
                && Objects.equals(a.getParentId(), department.getParentId())).collect(Collectors.toList());
        Map<String, List<DepartmentDTO>> parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));
        currentLevel = parentGroup.get(department.getParentId());
        if (CollUtil.isEmpty(currentLevel)) {
            return Collections.emptyList();
        }
        // 构建树形结构
        this.buildTree(currentLevel, parentGroup, null);
        return currentLevel;
    }

    /**
     * 当前部门和子集
     * @param departments 所有部门
     * @param department  当前 部门
     * @param currentLevel 当前层级
     * @return list
     */
    private List<DepartmentDTO> getDeptCurrentAndSubset(List<DepartmentDTO> departments, UserLoginAO.Department department, List<DepartmentDTO> currentLevel) {
        Map<String, DepartmentDTO> deptMaps = departments.stream().collect(Collectors.toMap(DepartmentDTO::getId, item -> item));
        Map<String, List<DepartmentDTO>> parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));
        DepartmentDTO departmentDTO = deptMaps.get(department.getId());
        currentLevel.add(departmentDTO);
        if (CollUtil.isEmpty(currentLevel)) {
            return Collections.emptyList();
        }
        // 构建树形结构
        this.buildTree(currentLevel, parentGroup, null);
        return currentLevel;
    }


    /**
     * 获取所有部门
     * @param departments 所有部门
     * @param currentLevel 当前层级
     * @return list
     */
    private List<DepartmentDTO> getDeptAll(List<DepartmentDTO> departments, List<DepartmentDTO> currentLevel) {
        Map<String, List<DepartmentDTO>> parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));
        currentLevel = parentGroup.get("0");
        if (CollUtil.isEmpty(currentLevel)) {
            return Collections.emptyList();
        }
        // 构建树形结构
        this.buildTree(currentLevel, parentGroup, null);
        return currentLevel;
    }

    /**
     * 过滤层级相同的部门，并且父级部门id相同,获取所有子部门
     * @param departments 所有部门
     * @param department 当前用户部门
     * @return map
     */
    private Map<String, List<DepartmentDTO>> getParentGroup(List<DepartmentDTO> departments, UserLoginAO.Department department) {
        // 获取当前部门的平级部门（包括自身）
        List<DepartmentDTO> peerDepartments = departments.stream()
                .filter(d -> Objects.equals(d.getLevel(), department.getLevel())
                        && Objects.equals(d.getParentId(), department.getParentId()))
                .collect(Collectors.toList());

        // 获取当前部门的所有子部门
        Set<Serializable> subsetDeptIds = getAllSubDepartmentId(department.getId());

        // 收集所有需要展示的部门ID
        Set<Serializable> needToShowDeptIds = new HashSet<>();

        // 添加平级部门（包括当前部门）
        peerDepartments.forEach(dept -> needToShowDeptIds.add(dept.getId()));

        // 添加当前部门的所有子部门
        needToShowDeptIds.addAll(subsetDeptIds);

        // 过滤出需要展示的部门
        List<DepartmentDTO> filteredDepartments = departments.stream()
                .filter(dept -> needToShowDeptIds.contains(dept.getId()))
                .collect(Collectors.toList());

        // 构建父子关系
        return filteredDepartments.stream()
                .collect(Collectors.groupingBy(DepartmentDTO::getParentId));
    }

    /**
     * 获取指定部门的所有上级部门
     *
     * @param departmentId 部门ID
     * @param allDepartments 所有部门列表
     * @return 所有上级部门列表，按层级从高到低排序
     */
    private List<DepartmentDTO> getParentDepartments(String departmentId, List<DepartmentDTO> allDepartments) {
        Map<String, DepartmentDTO> deptMap = allDepartments.stream()
                .collect(Collectors.toMap(DepartmentDTO::getId, Function.identity()));

        List<DepartmentDTO> parents = new ArrayList<>();
        String currentId = departmentId;

        while (currentId != null && !currentId.equals("0")) {
            DepartmentDTO currentDept = deptMap.get(currentId);
            if (currentDept == null) {
                break;
            }

            String parentId = currentDept.getParentId();
            if (parentId != null && !parentId.equals("0")) {
                DepartmentDTO parentDept = deptMap.get(parentId);
                if (parentDept != null) {
                    parents.add(0, parentDept); // 添加到列表开头，保持从顶级到上级的顺序
                }
            }
            currentId = parentId;
        }

        return parents;
    }


    public List<DepartmentDTO> filterDispatchDepartment(DepartmentDTO currentDepartment, List<DepartmentDTO> departments) {
        if (CollUtil.isEmpty(departments) || currentDepartment == null) {
            return null;
        }
        Integer level = currentDepartment.getLevel();
        switch (level) {
            case 1:
            case 2:
                return departments.stream().filter(node -> node.getLevel() == 2).collect(Collectors.toList());
            case 3:
                if (currentDepartment.getDeptName().endsWith("街道")) {
                    // 街道只能下发给社区
                    return departments.stream().filter(node -> node.getLevel() == 4 && node.getParentId().equals(currentDepartment.getId())).collect(Collectors.toList());
                } else {
                    // 某委办局可下发至街道、下属科室
                    return departments.stream().filter(node -> (node.getLevel() == 3 && node.getDeptName().contains("街道"))
                            || (node.getId().equals(currentDepartment.getId()))).collect(Collectors.toList());
                }
            case 4:
                if (currentDepartment.getDeptName().endsWith("社区")) {
                    // 社区只能下发给网格
                    return departments.stream().filter(node -> node.getParentId().equals(currentDepartment.getId())).collect(Collectors.toList());
                } else {
                    // 某委办局科室可下发至街道
                    return departments.stream().filter(node -> (node.getLevel() == 3 && node.getDeptName().contains("街道"))
                            || (node.getId().equals(currentDepartment.getId()))).collect(Collectors.toList());
                }
            case 5:
                return departments.stream().filter(node -> node.getParentId().equals(currentDepartment.getId())).collect(Collectors.toList());
            default:
                throw new ServiceException("Unsupported level");
        }
    }

    @Override
    public List<DepartmentDTO> getMeToFourthLevelDept() {
        String id = UserUtil.getDepartment().getId();
        DepartmentDTO curDept = departmentManager.getDtoById(id);
        Integer level = curDept.getLevel();
        ArrayList<DepartmentDTO> departments = new ArrayList<>();
        for (int i = level; i <= 5; i++) {
            List<DepartmentDTO> departmentByLevel = getDepartmentByLevels(Arrays.asList(i));
            departments.addAll(departmentByLevel);
        }
        List<DepartmentDTO> curDepartment = departments.stream().filter(e -> (e.getLevel() > level || id.equals(e.getId()))).collect(Collectors.toList());
        Map<String, List<DepartmentDTO>> parentGroup = curDepartment.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));
        curDepartment.forEach(department -> department.setChildren(parentGroup.get(department.getId())));
        List<DepartmentDTO> collect = curDepartment.stream().filter(e -> e.getId().equals(id)).collect(Collectors.toList());
        return collect.stream().sorted(Comparator.comparing(DepartmentDTO::getSort).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDTO> getDepartmentByLevels(List<Integer> level) {
        return departmentManager.listByLevels(level);
    }

    @Override
    public Paging<UserDepartmentDTO> getCurSubUser(CurSubExecutorPageParam param) {
        // 获取用户信息
        Paging<UserDepartmentDTO> departmentUsers = userDepartmentService.getCurSubUser(param);
        return departmentUsers;
    }

    @Override
    public Paging<UserDepartmentDTO> getCurDeptUser(CurDeptExecutorPageParam param) {
        return userDepartmentService.getCurDeptUser(param);
    }

    private DepartmentDTO getDepartmentByThridDeptId(String thirdDeptId) {
        OrgDockingMappingDTO dockingMapping = orgDockingMappingManager.getSystemIdByTargetId(thirdDeptId);
        if (Objects.isNull(dockingMapping)) {
            return null;
        }
        return departmentManager.getDtoById(dockingMapping.getSystemId());
    }

    @Override
    @Transactional
    public void saveOrUpdateForThird(DepartmentDTO dto) {
        String parentId = dto.getParentId();
        String regionCode = dto.getRegionCode();
        dto.setCreateBy(regionCode);
        if (CharSequenceUtil.isBlank(parentId) || "0".equals(parentId)) {
            dto.setParentId("0");
            dto.setLevel(1);
        } else {
            DepartmentDTO parent = getDepartmentByThridDeptId(dto.getParentId());
            dto.setParentDeptName(parent.getDeptName());
            dto.setParentId(parent.getId());
        }
        // 名称重复校验
        checkDeptName(dto);
        // 处理 departmentLevelCode 的对应关系
        setThirdDeptDefaultLevel(dto);
        // dto.setDepartmentLevelCode(dto.getDepartmentType().substring(dto.getDepartmentType().lastIndexOf("-") + 1));
        // 新增场景
        DepartmentDTO department = getDepartmentByThridDeptId(dto.getThirdDeptId());
        if (Objects.isNull(department)) {
            String parentCode = CharSequenceUtil.EMPTY;
            if (!"0".equals(dto.getParentId())) {
                DepartmentDTO parent = departmentManager.getDtoById(dto.getParentId());
                dto.setLevel(parent.getLevel() + 1);
                parentCode = parent.getDeptCode();
                dto.setParentDeptName(parent.getDeptName());
                dto.setParentId(parent.getId());
            }
            // 获取部门编号
            dto.setDeptCode(parentCode + getNextDepartmentCode(parentCode, dto.getLevel()));
            String id = this.departmentManager.add(dto);
            if (Strings.isBlank(id)) {
                // 保存失败的应对措施
                throw new ServiceException("部门新增失败");
            } else {
                OrgDockingMappingDTO dockingMappingDTO = new OrgDockingMappingDTO()
                        .setSystemId(id)
                        .setDockingType(OrgDockingTypeEnum.DEPARTMENT)
                        .setTargetId(dto.getThirdDeptId())
                        // 联调用如果暂时查不到appid，先填充一个默认值
//                        .setTargetCode(Strings.isNotBlank(appInfo.getAppId()) ? appInfo.getAppId() : "1902538363395637248");
                        .setTargetCode(dto.getTargetCode());
                orgDockingMappingManager.add(dockingMappingDTO);
            }
        } else {
            dto.setId(department.getId());
            BeanUtil.copyProperties(dto, department);
            updateDepartment(department);
        }
    }

    /**
     * @Title: setThirdDeptDefaultLevel
     * @Description: 设置三方部门推送默认层级
     * @Date: 2025/10/14 16:00
     * @Parameters: [dto]
     * @Return void
     */
    private void setThirdDeptDefaultLevel(DepartmentDTO dto) {
        ApiSystemConfig api = SpringUtil.getBean(ApiSystemConfig.class);
        Result<SysConfigAO> result = api.getByCode("ThirdDeptDefaultLevel");
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        SysConfigAO configAO = result.getData();
        if (configAO == null) {
            // 未配置，需后台手动处理数据库
            return;
        }
        String configValue = configAO.getConfigValue();
        JSON configJson = JSONUtil.parse(configValue);
        String departmentType = (String) configJson.getByPath("departmentType");
        String departmentLevelCode = (String) configJson.getByPath("departmentLevelCode");
        dto.setDepartmentType(departmentType);
        dto.setDepartmentLevelCode(departmentLevelCode);
    }

    @Override
    public void deleteForThird(String deptId) {
        DepartmentDTO dto = getDepartmentByThridDeptId(deptId);
        if (Objects.isNull(dto)) {
            throw new ServiceException("部门id不存在,无法删除");
        }
        deleteDepartment(Collections.singleton(dto.getId()));
        orgDockingMappingManager.deleteByTargetId(deptId);
    }

    @Override
    public void batchSaveOrUpdateForThird(Set<DepartmentCreateOrUpdateParam> param) {
        for (DepartmentCreateOrUpdateParam dto : param) {
            saveOrUpdateForThird(DepartmentConvert.INSTANCE.paramToDTO(dto));
        }
    }

    /**
     * 绵阳定开 提升部门组织树查询速度
     *
     * @param departmentId
     * @param userFlag
     * @return
     */

    @Override
    public List<DepartmentDTO> departmentTreeMianYang(Serializable departmentId, boolean userFlag) {
        long start2 = System.currentTimeMillis();
        // 如果需要用户信息则 查询缓存

        UserContext userContext = ExtendInfoUtil.getUserContext();
        List<DepartmentDTO> departments = this.departmentManager.getAllDepartment();
        log.info("报表注册1用时：{}", DateUtil.formatBetween(DateUtil.spendMs(start2)));
        // 获取到所有三方id的集合，过滤掉三方部门信息
        List<String> thirdIds = orgDockingMappingManager.getDockingMappingList(OrgDockingTypeEnum.DEPARTMENT).stream().map(e -> e.getSystemId()).collect(Collectors.toList());
        departments.removeIf(e -> thirdIds.contains(e.getId()));
        Map<String, List<DepartmentDTO>> parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        List<DepartmentDTO> currentLevel;
        if (departmentId != null) {
            if (departmentId.equals("0")) {
                currentLevel = departmentManager.getDepartmentsByParentId("0");
            } else {
                DepartmentDTO department = this.departmentManager.getDtoById(String.valueOf(departmentId));
                if (department != null) {
                    currentLevel = Lists.newArrayList(department);
                } else {
                    return Collections.emptyList();
                }
            }
        } else {
//            获取当前用户所在机构id列表
            Set<Serializable> departmentIds;
            if (userContext.getDepartmentId() != null) {
                departmentIds = new HashSet<>();
                departmentIds.add(userContext.getDepartmentId());
            } else {
                departmentIds = userContext.getDepartmentIds();
            }

//            找到部门id列表所在位置
            currentLevel = departments.stream().filter(o -> departmentIds.contains(o.getId())).collect(Collectors.toList());
            List<DepartmentDTO> list = new ArrayList<>(currentLevel);
//            筛选 移除有包含关系的子部门
            Set<String> subIds = list.stream().map(DepartmentDTO::getId).collect(Collectors.toSet());
            Set<String> allSubDeptByIds = getAllSubDeptByIds(subIds);
            currentLevel.removeIf(o -> allSubDeptByIds.contains(o.getId()));
/*            for (DepartmentDTO dto : list) {
                Set<Serializable> allSubDepartmentId = getAllSubDepartmentId(dto.getId());
                currentLevel.removeIf(o -> allSubDepartmentId.contains(o.getId()));
            }*/
//            currentLevel = parentGroup.get("0");
            currentLevel.sort(Comparator.nullsLast(Comparator.comparing(DepartmentDTO::getSort)).thenComparing(DepartmentDTO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())));
        }
        long start = System.currentTimeMillis();
        this.buildTreeMianYang(currentLevel, parentGroup, userFlag);
        if (log.isDebugEnabled()) {
            log.debug("method buildTree time:{}", DateUtil.formatBetween(DateUtil.spendMs(start)));
        }
        return currentLevel;
    }

    @Override
    public void treeExport(String departmentId, Boolean userFlag, HttpServletResponse response) {
        try {
            List<DepartmentDTO> departmentDTOS = this.departmentTreeMianYang(departmentId, userFlag);

            // 生成文本树结构
            String content = generateFormattedTextTree(departmentDTOS);
            response.setContentType("text/plain;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            String fileName = "部门组织架构.txt";
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"); // 替换+号为%20
            response.setHeader("Content-Disposition", "attachment; filename*=utf-8''" + encodedFileName);

            // 写入响应
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (Exception e) {
            log.error("导出部门树形结构失败", e);
            throw new ServiceException("导出部门树形结构失败: " + e.getMessage());
        }
    }

    /**
     * 生成美化后的文本树形结构
     * @param departments 部门列表
     * @return 树形文本
     */
    private String generateFormattedTextTree(List<DepartmentDTO> departments) {
        StringBuilder sb = new StringBuilder();
        sb.append("部门组织架构树\n");
        sb.append("===========================\n\n");

        if (CollUtil.isNotEmpty(departments)) {
            for (int i = 0; i < departments.size(); i++) {
                appendFormattedTextNode(departments.get(i), sb, "", i == departments.size() - 1);
            }
        } else {
            sb.append("暂无部门数据\n");
        }

        return sb.toString();
    }

    /**
     * 递归添加美化后的节点到文本
     * @param department 部门信息
     * @param sb StringBuilder
     * @param prefix 前缀
     * @param isLast 是否为最后一个节点
     */
    private void appendFormattedTextNode(DepartmentDTO department, StringBuilder sb, String prefix, boolean isLast) {
        // 添加当前节点
        sb.append(prefix);

        // 如果不是根节点，添加树形符号
        if (!prefix.isEmpty()) {
            sb.append(isLast ? "└── " : "├── ");
        }

        sb.append(department.getDeptName());
        sb.append("\n");

        // 处理子部门
        if (CollUtil.isNotEmpty(department.getChildren())) {
            String childPrefix = prefix + (isLast ? "    " : "│   ");
            List<DepartmentDTO> children = department.getChildren();
            for (int i = 0; i < children.size(); i++) {
                appendFormattedTextNode(children.get(i), sb, childPrefix, i == children.size() - 1);
            }
        }
    }


    @Override
    public List<DepartmentDTO> autoDispatchDept() {
        List<DepartmentDTO> departments = deptTree(DeptRelationEnum.SuperiorPeerLevelAndSubset, 1);
        List<DepartmentDTO> flatList = CollUtil.newArrayList();
        departments.forEach(dept -> flatDepartments(dept, flatList));
        Map<String, List<DepartmentDTO>> parentGroup = flatList.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));


        long start = System.currentTimeMillis();
        this.buildTreeMianYang(departments, parentGroup, true);
        if (log.isDebugEnabled()) {
            log.debug("method buildTree time:{}", DateUtil.formatBetween(DateUtil.spendMs(start)));
        }
        return departments;
    }

    @Override
    public List<DepartmentDTO> deptTreeLoad(DeptRelationEnum deptRelationEnum, Integer level) {
        UserLoginAO.Department department = UserUtil.getUser().getDepartment();
        String deptId = department.getId();
        String parentId = department.getParentId();

        List<DepartmentDTO> currentLevel;
        // 根据部门关系，获取部门树
        switch (deptRelationEnum) {
            case ALL:// 根部门为顶层组织
                currentLevel = getByParentId(null, false);
                break;
            case CurrentAndSubset:// 当前部门和子集 首级为自己的部门
                DepartmentDTO dept = getDepartmentById(deptId);
                if (dept != null) {
                    dept.setChildNum(departmentManager.getSubCountParentId(deptId));
                }
                currentLevel = Lists.newArrayList(dept);
                break;
            case CurrentAndPeerLevel:// 当前部门和平级
                List<DepartmentDTO> depts = getByParentId(parentId, false);
                for (DepartmentDTO item : depts) {
                    if (ObjUtil.notEqual(item.getId(), deptId)) {
                        item.setChildNum(0);
                    }
                }
                currentLevel = depts;
                break;
            case CurrentAndPeerLevelAndSubset:// 当前部门和平级和子集 包含平级的子级
                currentLevel = getByParentId(parentId, false);
                break;
            case SuperiorPeerLevelAndSubset:// 上级的平级的下级
                currentLevel = getDeptSuperiorPeerLevelAndSubset2(deptId, parentId, level);
                break;
            default:
                throw new ServiceException("不支持的部门关系类型: " + deptRelationEnum);
        }
        return currentLevel;
    }


    /**
     * 上级的平级的下级（逐级加载）
     *
     * @param deptId
     * @param parentId 当前部门
     * @param level    层级
     * @return list
     */
    private List<DepartmentDTO> getDeptSuperiorPeerLevelAndSubset2(String deptId, String parentId, Integer level) {
        // 获取当前部门信息
        DepartmentDTO dept = getDepartmentById(deptId);
        if (dept != null) {
            dept.setChildNum(departmentManager.getSubCountParentId(deptId));
        }
        List<DepartmentDTO> depts;
        // 如果level为null或小于等于0，或者当前部门为顶级，则默认为当前级
        if (level == null || level <= 0 || ObjUtil.equals(parentId, "0")) {
            return Lists.newArrayList(dept);
        }

        // 根据向上查找的级数查找上级
        DepartmentDTO parent = null;
        for (int i = 0; i < level; i++) {
            parent = getDepartmentById(parentId);
            if (parent == null) {
                break;
            }
            if (ObjUtil.equals(parent.getParentId(), "0")) {
                // 已经找到顶级，终止查找
                break;
            }
        }
        if (parent == null) {
            return Lists.newArrayList(dept);
        }

        // 获取上级的平级
        String rootParentId = parent.getParentId();
        depts = getByParentId(rootParentId, false);
        return depts;
    }


    // 新增递归展平方法
    private void flatDepartments(DepartmentDTO node, List<DepartmentDTO> result) {
        if (node == null) {
            return;
        }
        result.add(node);
        if (CollUtil.isNotEmpty(node.getChildren())) {
            node.getChildren().forEach(child -> flatDepartments(child, result));
        }
    }

    @Override
    public List<DepartmentDTO> getByParentId(String parentId, Boolean thirdFlag) {
        if (StrUtil.isBlank(parentId)) {
            parentId = "0";
        }
        List<DepartmentDTO> dtos = this.departmentManager.getCacheByParentId(parentId);
        if (CollUtil.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        dtos.sort(Comparator.comparing(DepartmentDTO::getSort));

        // 批量获取所有子部门数量，替代循环单次查询
        List<String> deptIds = dtos.stream().map(DepartmentDTO::getId).collect(Collectors.toList());
        Map<String, Integer> childCountMap = getChildCountBatch(deptIds);
        dtos.forEach(dto -> dto.setChildNum(childCountMap.getOrDefault(dto.getId(), 0)));

        // 设置三方推送部门相关参数
        List<DepartmentDTO> departmentDTOS = setThirdDeptInfo(dtos, thirdFlag);
        return departmentDTOS;
    }

    /**
     * 批量获取部门的子部门数量
     * @param parentIds 父部门ID列表
     * @return Map<部门ID, 子部门数量>
     */
    private Map<String, Integer> getChildCountBatch(List<String> parentIds) {
        // 通过一次数据库查询获取所有相关子部门
        List<Department> allChildren = departmentManager.lambdaQuery()
                .in(Department::getParentId, parentIds)
                .list();

        // 按父部门ID分组并统计数量
        return allChildren.stream()
                .collect(Collectors.groupingBy(
                        Department::getParentId,
                        Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
    }

    /**
     * @Title: setThirdDeptInfo
     * @Description: 设置三方部门信息
     * @Date: 2025/10/22 15:14
     * @Parameters: [dtos]
     * @Return void
     */
    private List<DepartmentDTO> setThirdDeptInfo(List<DepartmentDTO> departmentList, Boolean thirdFlag) {
        if (CollUtil.isEmpty(departmentList)) {
            return Collections.emptyList();
        }
        Set<String> deptIds = departmentList.stream().map(DepartmentDTO::getId).collect(Collectors.toSet());
        List<OrgDockingMappingDTO> orgDockingMappingList = orgDockingMappingManager.listBySystemIds(deptIds);
        if (CollUtil.isEmpty(orgDockingMappingList)) {
            log.info("third dept mapping is null, deptIds:{}", JSONUtil.toJsonStr(deptIds));
            return departmentList;
        }
        // 获取到是三方系统的id集合
        List<String> collect = orgDockingMappingList.stream().map(e -> e.getSystemId()).collect(Collectors.toList());
        if (thirdFlag) {
            Map<String, OrgDockingMappingDTO> orgMappingMap = orgDockingMappingList.stream().collect(Collectors.toMap(OrgDockingMappingDTO::getSystemId, t -> t));
            for (DepartmentDTO department : departmentList) {
                OrgDockingMappingDTO dockingMapping = orgMappingMap.get(department.getId());
                if (dockingMapping == null) {
                    continue;
                }
                department.setTargetCode(dockingMapping.getTargetCode());
                department.setThirdDeptId(dockingMapping.getTargetId());
            }
            // 过滤掉不是三方的部门
            List<DepartmentDTO> thirDeptList = departmentList.stream().filter(e -> collect.contains(e.getId())).collect(Collectors.toList());
            return thirDeptList;
        } else {
            // 过滤掉是三方的部门
            List<DepartmentDTO> sysDeptList = departmentList.stream().filter(e -> !collect.contains(e.getId())).collect(Collectors.toList());
            return sysDeptList;
        }

    }

    /**
     * 查询本机部门以及本机的下级部门以及平级部门和平级部门的下级部门
     *
     * @param departmentId
     * @return
     */
    @Override
    public List<DepartmentDTO> getFullDepartment(String departmentId) {
        UserContext userContext = ExtendInfoUtil.getUserContext();
        List<DepartmentDTO> allDepartments = this.departmentManager.listAll();
        Map<String, List<DepartmentDTO>> parentGroup = allDepartments.stream()
                .collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        String targetDepartmentId = departmentId != null ? departmentId :
                (userContext.getDepartmentId() != null ?
                        String.valueOf(userContext.getDepartmentId()) : null);
        if (targetDepartmentId == null) {
            return Collections.emptyList();
        }
        DepartmentDTO targetDepartment = this.departmentManager.getDtoById(targetDepartmentId);
        if (targetDepartment == null) {
            return Collections.emptyList();
        }
        List<DepartmentDTO> peerDepartments = parentGroup.getOrDefault(
                targetDepartment.getParentId(), Collections.emptyList());
        Set<String> departmentIdsToInclude = new HashSet<>();
        departmentIdsToInclude.add(targetDepartment.getId());
        departmentIdsToInclude.addAll(getAllSubDepartmentId2(targetDepartment.getId()));
        for (DepartmentDTO peer : peerDepartments) {
            departmentIdsToInclude.add(peer.getId());
            departmentIdsToInclude.addAll(getAllSubDepartmentId2(peer.getId()));
        }
        List<DepartmentDTO> result = allDepartments.stream()
                .filter(d -> departmentIdsToInclude.contains(d.getId()))
                .collect(Collectors.toList());
        List<DepartmentDTO> parentLevel = parentGroup.getOrDefault(
                targetDepartment.getParentId(), Collections.emptyList());
        parentLevel.sort(Comparator.nullsLast(
                        Comparator.comparing(DepartmentDTO::getSort))
                .thenComparing(DepartmentDTO::getCreateTime,
                        Comparator.nullsLast(Comparator.reverseOrder())));
        this.buildTreeMianYang(parentLevel, parentGroup, false);
        return parentLevel.stream()
                .filter(d -> departmentIdsToInclude.contains(d.getId()))
                .collect(Collectors.toList());
    }

    /**
     递归获取某个部门的所有下级部门ID
     *
     */
    private Set<String> getAllSubDepartmentId2(String departmentId) {
        Set<String> result = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(departmentId);

        Map<String, List<DepartmentDTO>> parentGroup = departmentManager.listAll().stream()
                .collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        while (!stack.isEmpty()) {
            String currentId = stack.pop();
            List<DepartmentDTO> children = parentGroup.getOrDefault(currentId, Collections.emptyList());
            for (DepartmentDTO child : children) {
                result.add(child.getId());
                stack.push(child.getId());
            }
        }
        return result;
    }

    /**
     * 绵阳定开 优化组织机构树查询速度
     */
    // 类级别声明排序器（线程安全）
    private static final Comparator<DepartmentDTO> DEPT_COMPARATOR =
            Comparator.nullsLast(Comparator.comparing(DepartmentDTO::getSort))
                    .thenComparing(
                            DepartmentDTO::getCreateTime,
                            Comparator.nullsLast(Comparator.reverseOrder())
                    );

    private void buildTreeMianYang(List<DepartmentDTO> currentLevel,
                                   Map<String, List<DepartmentDTO>> parentGroup, boolean userFlag) {
        Map<String, List<UserDepartmentDTO>> userMap = null;
        // 批量预加载用户数据（解决N+1问题）
        if (userFlag) {
            userMap = userDepartmentManager.getUsersByDepartmentIdsMianYang(null);
        }
        if (null == userMap) {
            userMap = Collections.emptyMap();
        }
        // 使用迭代代替递归（防止栈溢出）
        Deque<List<DepartmentDTO>> stack = new ArrayDeque<>();
        stack.push(currentLevel);

        while (!stack.isEmpty()) {
            List<DepartmentDTO> level = stack.pop();

            for (DepartmentDTO item : level) {
                // 设置用户信息（内存操作）
                item.setUsers(userMap.getOrDefault(item.getId(), Collections.emptyList()));
                // 获取并处理子部门
                List<DepartmentDTO> subs = parentGroup.getOrDefault(item.getId(), Collections.emptyList());
                if (subs.isEmpty()) {
                    continue;
                }
                // 单次排序（提升性能）
                subs.sort(DEPT_COMPARATOR);
                item.setChildren(subs);
                stack.push(subs); // 迭代压栈
            }
        }
    }

    /**
     * 构建完整的部门树结构
     * @param departments 当前层级部门列表
     * @param deptRelationEnum 部门关系枚举
     */
    private void buildFullDepartmentTree(List<DepartmentDTO> departments, DeptRelationEnum deptRelationEnum) {
        if (CollUtil.isEmpty(departments)) {
            return;
        }

        // 获取所有部门数据
        List<DepartmentDTO> allDepartments = this.departmentManager.listAll();
        if (CollUtil.isEmpty(allDepartments)) {
            return;
        }

        // 构建父子关系映射
        Map<String, List<DepartmentDTO>> parentGroup = allDepartments.stream()
                .collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        // 根据不同关系类型构建树
        switch (deptRelationEnum) {
            case ALL:
                // 构建完整树结构
                this.buildTreeMianYang(departments, parentGroup, false);
                break;
            case CurrentAndSubset:
                // 构建当前部门及其子集的完整树
                if (CollUtil.isNotEmpty(departments) && departments.get(0) != null) {
                    String rootId = departments.get(0).getId();
                    Set<String> includedIds = new HashSet<>();
                    includedIds.add(rootId);
                    collectAllSubDepartmentIds(rootId, parentGroup, includedIds);

                    List<DepartmentDTO> filteredDepartments = allDepartments.stream()
                            .filter(dept -> includedIds.contains(dept.getId()))
                            .collect(Collectors.toList());

                    Map<String, List<DepartmentDTO>> filteredParentGroup = filteredDepartments.stream()
                            .collect(Collectors.groupingBy(DepartmentDTO::getParentId));

                    this.buildTreeMianYang(departments, filteredParentGroup, false);
                }
                break;
            case CurrentAndPeerLevelAndSubset:
                // 对于CurrentAndPeerLevelAndSubset，需要构建子部门树
                this.buildTreeMianYang(departments, parentGroup, false);
                break;
            case SuperiorPeerLevelAndSubset:
                // 对于SuperiorPeerLevelAndSubset，需要构建子部门树
                this.buildTreeMianYang(departments, parentGroup, false);
                break;
            case CurrentAndPeerLevel:
                // 这些情况已经按需加载，不需要构建完整树
                break;
            default:
                break;
        }
    }

    /**
     * 递归收集所有子部门ID
     * @param parentId 父部门ID
     * @param parentGroup 父子关系映射
     * @param result 结果集合
     */
    private void collectAllSubDepartmentIds(String parentId, Map<String, List<DepartmentDTO>> parentGroup, Set<String> result) {
        List<DepartmentDTO> children = parentGroup.getOrDefault(parentId, Collections.emptyList());
        for (DepartmentDTO child : children) {
            result.add(child.getId());
            collectAllSubDepartmentIds(child.getId(), parentGroup, result);
        }
    }

    @Override
    public Paging<UserDepartmentDTO> getUserPageByDeptId(TaskExecutorPageMianYangParam param) {
        // 获取用户信息
        Paging<UserDepartmentDTO> departmentUsers = userDepartmentManager.getUserPageByDeptId(param);
        return departmentUsers;
    }

    @Override
    public List<DepartmentDTO> subscribingDepartment() {
        // 获取当前用户ID和部门ID
        UserLoginAO.Department currentUserDept = UserUtil.getDepartment();
        if (currentUserDept == null) {
            return Collections.emptyList();
        }
        String currentDeptId = currentUserDept.getId();

        // 查找最后一个相同层级的上级部门
        DepartmentDTO lastSameLevelParent = findLastSameLevelParent(currentUserDept);
        if (lastSameLevelParent != null) {
            log.info("找到最后一个相同层级的上级部门: {}, 层级编码: {}",
                    lastSameLevelParent.getDeptName(),
                    lastSameLevelParent.getDepartmentLevelCode());
            currentDeptId = lastSameLevelParent.getId();
        } else {
            log.info("未找到相同层级的上级部门");
        }
        // 查询所有部门
        List<DepartmentDTO> allDepartments = this.departmentManager.listAll();
        if (CollUtil.isEmpty(allDepartments)) {
            return Collections.emptyList();
        }
        // 找到当前用户部门
        String finalCurrentDeptId = currentDeptId;
        DepartmentDTO currentDept = allDepartments.stream()
                .filter(dept -> dept.getId().equals(finalCurrentDeptId))
                .findFirst()
                .orElse(null);
        if (currentDept == null) {
            return Collections.emptyList();
        }
        // 找出所有部门的父子关系
        Map<String, List<DepartmentDTO>> parentGroup = allDepartments.stream()
                .collect(Collectors.groupingBy(DepartmentDTO::getParentId));
        // 准备结果集合
        List<DepartmentDTO> result = new ArrayList<>();
        // 使用Set记录已添加的部门ID
        Set<String> addedDeptIds = new HashSet<>();
        // 获取当前部门的父部门ID
        String parentId = currentDept.getParentId();
        // 如果有父部门，找出所有同级部门
        if (StrUtil.isNotBlank(parentId) && parentGroup.containsKey(parentId)) {
            // 添加所有同级部门（包括当前部门）
            for (DepartmentDTO dept : parentGroup.get(parentId)) {
                if (Objects.isNull(dept.getDepartmentLevelCode())) {
                    continue;
                }
                if (Integer.parseInt(currentDept.getDepartmentLevelCode()) <= Integer.parseInt(dept.getDepartmentLevelCode())) {
                    if (addedDeptIds.add(dept.getId())) { // 确保不重复添加
                        result.add(dept);
                    }
                }

            }
        } else {
            // 如果没有父部门或找不到同级部门，只添加当前部门
            addedDeptIds.add(currentDept.getId());
            result.add(currentDept);
        }
        // 构建树
        long start = System.currentTimeMillis();
        buildTreeWithoutDuplicates(result, parentGroup, addedDeptIds);
        if (log.isDebugEnabled()) {
            log.debug("method buildTree time:{}", DateUtil.formatBetween(DateUtil.spendMs(start)));
        }
        return result;
    }

    /**
     * 递归查询上级部门，返回具有相同层级编码的最后一个部门
     *
     * @param currentUserDept 当前用户部门
     * @return 最后一个相同层级的部门
     */
    public DepartmentDTO findLastSameLevelParent(UserLoginAO.Department currentUserDept) {
        if (currentUserDept == null || StrUtil.isBlank(currentUserDept.getId())) {
            return null;
        }

        // 获取当前用户部门完整信息
        DepartmentDTO currentDept = departmentManager.getDtoById(currentUserDept.getId());
        if (currentDept == null) {
            return null;
        }

        // 获取当前部门的层级编码
        String currentLevelCode = currentDept.getDepartmentLevelCode();

        // 最后一个相同层级的部门初始为当前部门
        DepartmentDTO[] lastSameLevelDept = new DepartmentDTO[]{currentDept};

        // 递归查找父部门
        findLastSameLevelParentRecursive(currentDept.getParentId(), currentLevelCode, lastSameLevelDept);

        // 返回找到的最后一个相同层级部门
        return lastSameLevelDept[0];
    }

    /**
     * 递归查找最后一个相同层级的父部门
     *
     * @param parentId          当前要查找的父部门ID
     * @param originalLevelCode 原始部门的层级编码
     * @param lastSameLevelDept 数组引用，保存最后一个相同层级的部门
     */
    private void findLastSameLevelParentRecursive(String parentId, String originalLevelCode, DepartmentDTO[] lastSameLevelDept) {
        // 如果父ID为空，则已到顶层，结束递归
        if (StrUtil.isBlank(parentId)) {
            return;
        }

        // 查询父部门
        DepartmentDTO parentDept = departmentManager.getDtoById(parentId);
        if (parentDept == null) {
            return;
        }

        // 比较层级编码
        String parentLevelCode = parentDept.getDepartmentLevelCode();

        // 如果层级编码相同，更新最后一个相同层级的部门
        if (StrUtil.isNotBlank(parentLevelCode) &&
                StrUtil.isNotBlank(originalLevelCode) &&
                parentLevelCode.equals(originalLevelCode)) {

            lastSameLevelDept[0] = parentDept;

            // 继续向上查找
            findLastSameLevelParentRecursive(parentDept.getParentId(), originalLevelCode, lastSameLevelDept);
        }
        // 如果层级编码不同，则结束递归（已找到最后一个相同层级的部门）
    }

    private void buildTreeWithoutDuplicates(List<DepartmentDTO> departments,
                                            Map<String, List<DepartmentDTO>> parentGroup,
                                            Set<String> addedDeptIds) {
        if (CollUtil.isEmpty(departments)) {
            return;
        }

        for (DepartmentDTO dept : departments) {
            // 查找子部门
            List<DepartmentDTO> children = parentGroup.get(dept.getId());
            if (CollUtil.isNotEmpty(children)) {
                // 过滤掉已添加的部门
                List<DepartmentDTO> newChildren = children.stream()
                        .filter(child -> addedDeptIds.add(child.getId()))
                        .collect(Collectors.toList());

                if (CollUtil.isNotEmpty(newChildren)) {
                    dept.setChildren(newChildren);
                    // 递归构建子树
                    buildTreeWithoutDuplicates(newChildren, parentGroup, addedDeptIds);
                }
            }
        }
    }

    @Override
    public List<String> previewDepartmentNameByCode(String code, Integer limit) {
        if (CharSequenceUtil.isEmpty(code)) {
            return Collections.emptyList();
        }

        List<DepartmentDTO> dtos = this.departmentManager.getParentDepartmentByCode(code, limit);
        if (CollUtil.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(DepartmentDTO::getDeptName).collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDTO> sameAndSuperiorDept() {
        UserLoginAO.Department department = UserUtil.getDepartment();
        if (department == null) {
            throw new ServiceException("当前用户部门为空!");
        }

        List<DepartmentDTO> departments = this.departmentManager.listAll();
        if (CollUtil.isEmpty(departments)) {
            return Collections.emptyList();
        }
        // 获取当前层级到顶级的departmentLevelCode
        Map<String, DepartmentDTO> deptMap = departments.stream().collect(Collectors.toMap(DepartmentDTO::getId, dto -> dto));

        String sameLevelParentDept = department.getId();
        String parentId = department.getParentId();
        String currentId = department.getId();
        String currentDeptLevel = department.getDepartmentLevelCode();
        // 获取当前部门同层级上级部门
        List<DepartmentDTO> currentLevel = new ArrayList<>();
        for (DepartmentDTO dto : departments) {
            DepartmentDTO parentDepartment = deptMap.get(parentId);
            DepartmentDTO departmentInfo = deptMap.get(currentId);
            if (parentDepartment == null || !currentDeptLevel.equals(parentDepartment.getDepartmentLevelCode())) {
                break;
            }
            sameLevelParentDept = parentDepartment.getId();
            parentId = parentDepartment.getParentId();
            parentDepartment.setChildren(Lists.newArrayList(departmentInfo));
        }
        DepartmentDTO departmentDTO = deptMap.get(sameLevelParentDept);
        currentLevel.add(departmentDTO);
        return currentLevel;
    }


    @Override
    public List<DepartmentDTO> listByThirdIds(Set<String> thirdIds) {
        if (CollUtil.isEmpty(thirdIds)) {
            throw new ServiceException("三方部门ids为空，获取部门信息失败");
        }
        List<OrgDockingMappingDTO> mappingDTOList = orgDockingMappingService.listByTargetIds(thirdIds);
        if (CollUtil.isEmpty(mappingDTOList)) {
            return Collections.emptyList();
        }

        Set<String> ids = mappingDTOList.stream().map(OrgDockingMappingDTO::getSystemId).collect(Collectors.toSet());
        Map<String, OrgDockingMappingDTO> mappingMap = mappingDTOList.stream().collect(Collectors.toMap(OrgDockingMappingDTO::getSystemId, d -> d));
        List<DepartmentDTO> deptList = this.departmentManager.getByIds(ids);
        deptList.forEach(e -> {
            OrgDockingMappingDTO dockingMappingDTO = mappingMap.get(e.getId());
            e.setThirdDeptId(dockingMappingDTO.getTargetId());
            e.setTargetCode(dockingMappingDTO.getTargetCode());
        });

        return deptList;
    }

    @Override
    public List<DepartmentDTO> thirdDeptTree() {
        // 获取三方关于组织机构的数据
        List<OrgDockingMappingDTO> orgDockingMappingDTOS = orgDockingMappingManager.getDockingMappingList(OrgDockingTypeEnum.DEPARTMENT);
        if (CollUtil.isEmpty(orgDockingMappingDTOS)) {
            return Collections.emptyList();
        }

        // 获取本系统满足条件的组织机构信息
        Set<String> ids = orgDockingMappingDTOS.stream().map(OrgDockingMappingDTO::getSystemId).collect(Collectors.toSet());
        List<DepartmentDTO> departmentDTOS = departmentManager.listAll().stream()
                .filter(departmentDTO -> "0".equals(departmentDTO.getParentId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(departmentDTOS)) {
            log.info("三方系统组织信息父id为0的组织信息为空");
            return Collections.emptyList();
        }
        ids.add(departmentDTOS.get(0).getId());
        List<DepartmentDTO> departments = this.departmentManager.getByIds(ids);

        // 数据组装
        List<DepartmentDTO> currentLevel = Lists.newArrayList();
        currentLevel = getThirdDeptAll(departments, currentLevel);

        // 三方字段赋值
        Map<String, OrgDockingMappingDTO> thirdMap = orgDockingMappingDTOS.stream()
                .collect(Collectors.toMap(OrgDockingMappingDTO::getSystemId, Function.identity()));
        // 递归处理
        processThirdInfo(currentLevel, thirdMap);

        return currentLevel;
    }

    @Override
    public List<DepartmentDTO> systemDeptTree() {
        List<DepartmentDTO> currentLevel = Lists.newArrayList();

        // 获取本系统组织机构信息
        List<DepartmentDTO> departments = departmentManager.listAll();
        if (CollUtil.isEmpty(departments)) {
            return currentLevel;
        }

        // 子集部门数据组装
        currentLevel = getThirdDeptAll(departments, currentLevel);

        // 处理部门管理员信息
        List<String> deptIds = departments.stream().map(DepartmentDTO::getId).collect(Collectors.toList());
        List<UserDepartmentDTO> userDepartmentDTOS = userDepartmentManager.listAdminByDepartmentIds(deptIds);
        if (CollUtil.isNotEmpty(userDepartmentDTOS)) {
            Map<String, String> adminMap = userDepartmentDTOS.stream()
                    .collect(Collectors.toMap(UserDepartmentDTO::getDepartmentId, UserDepartmentDTO::getUserId, (k1, k2) -> k1));
            // 递归处理
            processAdminUser(currentLevel, adminMap);
        }

        return currentLevel;
    }


    /**
     * 递归处理部门管理员信息
     *
     * @param currentLevel 需要处理的部门信息
     * @param adminMap     管理员map
     */
    public void processAdminUser(List<DepartmentDTO> currentLevel, Map<String, String> adminMap) {
        if (currentLevel == null || currentLevel.isEmpty()) {
            return;
        }
        for (DepartmentDTO departmentDTO : currentLevel) {
            if (adminMap.containsKey(departmentDTO.getId())) {
                departmentDTO.setOperatorId(adminMap.get(departmentDTO.getId()));
            }
            // 递归处理子节点
            processAdminUser(departmentDTO.getChildren(), adminMap);
        }
    }

    /**
     * 递归处理部门管理员信息
     * @param currentLevel 需要处理的部门信息
     * @param thirdMap 三方map
     */
    public void processThirdInfo(List<DepartmentDTO> currentLevel, Map<String, OrgDockingMappingDTO> thirdMap) {
        if (currentLevel == null || currentLevel.isEmpty()) {
            return;
        }
        for (DepartmentDTO departmentDTO : currentLevel) {
            if (thirdMap.containsKey(departmentDTO.getId())) {
                OrgDockingMappingDTO thirdInfo = thirdMap.get(departmentDTO.getId());
                departmentDTO.setThirdDeptId(thirdInfo.getTargetId());
                departmentDTO.setTargetCode(thirdInfo.getTargetCode());
            }
            // 递归处理子节点
            processThirdInfo(departmentDTO.getChildren(), thirdMap);
        }
    }


    @Override
    public Paging<ThirdDepartmentVO> thirdPage(DepartmentPageParam param) {
        Paging<ThirdDepartmentVO> paging = new Paging<>();

        // 获取三方关于组织机构的数据
        List<OrgDockingMappingDTO> orgDockingMappingDTOS = orgDockingMappingManager.getDockingMappingList(OrgDockingTypeEnum.DEPARTMENT);
        if (CollUtil.isEmpty(orgDockingMappingDTOS)) {
            log.info("三方系统组织机构信息为空");
            return paging;
        }

        // 判断该组织机构在本系统是否存在
        Set<String> ids = orgDockingMappingDTOS.stream().map(OrgDockingMappingDTO::getSystemId).collect(Collectors.toSet());
        departmentManager.getDepartmentsByParentId("0").stream().findFirst()
                .ifPresent(department -> ids.add(department.getId()));
        if (!ids.contains(param.getDeptId())) {
            log.info("本系统组织机构未找到该部门：{}", param.getDeptId());
            return paging;
        }

        // 分页处理
        return dealPage(param);
    }

    @Override
    public Paging<ThirdDepartmentVO> deptInfoPage(DepartmentPageParam param) {
        Paging<ThirdDepartmentVO> paging = new Paging<>();

        // 获取当前部门信息
        Department department = departmentManager.getById(param.getDeptId());
        if (ObjectUtils.isEmpty(department)) {
            log.info("未在本系统找到该部门信息：{}", param.getDeptId());
            return paging;
        }

        // 分页处理
        return dealPage(param);
    }

    /**
     * 处理部门分页数据
     * @param param 分区请求参数
     * @return Paging<ThirdDepartmentVO> 分页数据
     */
    public Paging<ThirdDepartmentVO> dealPage(DepartmentPageParam param) {
        // 递归获取当前部门id及其下级id列表
        Set<String> deptAllIds = getDepartmentIdsRecurById(param.getDeptId());

        // 获取部门相关信息
        Paging<ThirdDepartmentVO> pageInfo = departmentManager.getThirdPage(param, deptAllIds);
        pageInfo.getRecords().forEach(x -> x.setParentDeptName(departmentManager.getDepartmentName(x.getParentId())));

        return pageInfo;
    }

    /**
     * 获取所有部门
     * @param departments 所有部门
     * @param currentLevel 当前层级
     * @return list
     */
    private List<DepartmentDTO> getThirdDeptAll(List<DepartmentDTO> departments, List<DepartmentDTO> currentLevel) {
        Map<String, List<DepartmentDTO>> parentGroup = departments.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));
        currentLevel = parentGroup.get("0");
        if (CollUtil.isEmpty(currentLevel)) {
            return Collections.emptyList();
        }
        // 构建树形结构
        this.buildTree(currentLevel, parentGroup, null);
        return currentLevel;
    }

    @Override
    public List<DepartmentDTO> listThirdInfoByIds(Set<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new ServiceException("部门ids为空，获取三方部门信息失败");
        }
        List<OrgDockingMappingDTO> mappingDTOList = orgDockingMappingService.listBySystemIds(ids);
        if (CollUtil.isEmpty(mappingDTOList)) {
            return Collections.emptyList();
        }

        Map<String, OrgDockingMappingDTO> mappingMap = mappingDTOList.stream().collect(Collectors.toMap(OrgDockingMappingDTO::getSystemId, d -> d));
        List<DepartmentDTO> deptList = this.departmentManager.getByIds(ids);
        deptList.forEach(e -> {
            OrgDockingMappingDTO dockingMappingDTO = mappingMap.get(e.getId());
            e.setThirdDeptId(dockingMappingDTO.getTargetId());
            e.setTargetCode(dockingMappingDTO.getTargetCode());
        });

        return deptList;
    }

    @Override
    public List<DepartmentDTO> getAllSubDepartment(String deptCode) {
        return departmentManager.getAllSubDepartment(deptCode);
    }

    @Override
    public List<String> getAllParentDepartmentIds(String deptCode) {
        return departmentManager.getAllParentDepartmentIds(deptCode);
    }

    @Override
    public List<String> getAllParentDepartmentIdsById(String deptId) {
        return departmentManager.getAllParentDepartmentIdsById(deptId);
    }

    @Override
    public Map<String, List<DepartmentDTO>> getParentMapByCodes(Set<String> deptCodes, Integer limit) {
        return departmentManager.getParentMapByCodes(deptCodes, limit);
    }

    @Override
    public List<DepartmentDTO> getAllParentDepartments(String deptCode, String deptId) {
        // 传code就以 code查询，传id就以id查询
        // 根据code，获取上级部门,传入C,并按照A、B、C有序返回
        List<String> allParentDepartmentIds;
        if (StrUtil.isNotEmpty(deptCode)) {
            allParentDepartmentIds = getAllParentDepartmentIds(deptCode);
        } else {
            allParentDepartmentIds = getAllParentDepartmentIdsById(deptId);
        }

        if (CollUtil.isEmpty(allParentDepartmentIds)) {
            return Collections.emptyList();
        }
        // 需要建列父子结构
        Set<String> departmentIdSet = new HashSet<>(allParentDepartmentIds);
        List<DepartmentDTO> departments = departmentManager.getByIds(departmentIdSet);

        // 构建父子级结构，根据层级关系重新组织部门
        Map<String, DepartmentDTO> deptMap = departments.stream()
                .collect(Collectors.toMap(DepartmentDTO::getId, dept -> dept, (existing, replacement) -> existing));

        // 创建根部门列表
        List<DepartmentDTO> rootDepartments = new ArrayList<>();

        // 遍历所有部门，构建父子关系
        for (DepartmentDTO dept : departments) {
            dept.setChildNum(departmentManager.getSubCountParentId(deptId));
            String parentId = dept.getParentId();
            if ("0".equals(parentId) || !deptMap.containsKey(parentId)) {
                // 如果是根部门或父部门不在列表中，则添加到根部门列表
                rootDepartments.add(dept);
            } else {
                // 否则，将当前部门添加到其父部门的子部门列表中
                DepartmentDTO parentDept = deptMap.get(parentId);
                if (parentDept.getChildren() == null) {
                    parentDept.setChildren(new ArrayList<>());
                }
                parentDept.getChildren().add(dept);
            }
        }

        return rootDepartments;
    }

    @Override
    public Set<String> getAllDepartCodes(String parentId) {
        List<String> allDepartCodes = departmentMapper.getAllDepartCodes(parentId);
        Set<String> deptCodeSet = new HashSet<>(allDepartCodes); // 如果需要Set，可以轻松转换
        return deptCodeSet;
    }

    @Override
    public List<DepartmentDTO> getCurrentAndAllSubset() {
        UserContext userContext = ExtendInfoUtil.getUserContext();
        assert ObjectUtils.isNotEmpty(userContext);
        Department curDepartment = departmentManager.getById(userContext.getDepartmentId());
        if (ObjectUtils.isEmpty(curDepartment)) {
            return Collections.emptyList();
        }

        ArrayList<Department> currentDepts = new ArrayList<>();
        String deptTypePrefix = curDepartment.getDepartmentType().substring(0, 2);
        //如果组织机构类型前缀不是01，则需要查找其01开头的顶级部门（01表示同级别的最高层级）
        if (!deptTypePrefix.equals("01")) {
            String departmentType = "01" + curDepartment.getDepartmentType().substring(2);
            LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Department::getRegionCode, curDepartment.getRegionCode());
            queryWrapper.eq(Department::getDepartmentType, departmentType);
            queryWrapper.orderByAsc(Department::getCreateTime);
            queryWrapper.orderByAsc(Department::getDeptCode);
            List<Department> departments = departmentMapper.selectList(queryWrapper);
            if (ObjectUtils.isEmpty(curDepartment)) {
                return Collections.emptyList();
            }
            // 理论上同一个regionCode开头的，01开头的应该只有一个（使用list为了避免报错）
            currentDepts.addAll(departments);
        }

        // 构建树形结构
        if (CollectionUtil.isEmpty(currentDepts)) {
            currentDepts.add(curDepartment);
        }

        // 获取所有子部门
        List<DepartmentDTO> allSubDeptByIds = departmentManager.getAllSubDeptByDeptCode(currentDepts.get(0).getDeptCode());
        Map<String, List<DepartmentDTO>> parentGroup = allSubDeptByIds.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        List<DepartmentDTO> currentLevel = DepartmentConvert.INSTANCE.toDTO(currentDepts);
        this.buildTree(currentLevel, parentGroup, null);
        return currentLevel;
    }

    @Override
    public DepartmentStepTreeVO getTreeStep(String departmentId) {
        DepartmentStepTreeVO treeStepVO = new DepartmentStepTreeVO();
        List<DepartmentStepTreeVO.DeptInfo> currentLevel = new ArrayList<>();
        // 未传部门id时，查询当前部门及用户
        if (StrUtil.isEmpty(departmentId)) {
            String deptId = UserUtil.getDepartment().getId();
            DepartmentDTO department = this.departmentManager.getDtoById(deptId);
            currentLevel = DepartmentConvert.INSTANCE.toStepTreeVO(Collections.singletonList(department));
        } else {
            // 当传入部门id时，查询子部门及用户
            List<DepartmentDTO> departments = departmentManager.getDepartmentsByParentId(departmentId);
            currentLevel = DepartmentConvert.INSTANCE.toStepTreeVO(departments);
            List<UserDepartmentDTO> userDepartmentDTOS = userDepartmentManager.listByDeptIds(Collections.singleton(departmentId));
            List<DepartmentStepTreeVO.User> userList = convertUserInfo(userDepartmentDTOS);
            treeStepVO.setUserList(userList);
        }
        treeStepVO.setDeptList(currentLevel);
        return treeStepVO;

    }

    /**
     * 转换用户信息
     * @param userDepartmentDTOS
     * @return
     */
    private List<DepartmentStepTreeVO.User> convertUserInfo(List<UserDepartmentDTO> userDepartmentDTOS) {
        List<DepartmentStepTreeVO.User> userList = new ArrayList<>();
        for (UserDepartmentDTO userDepartmentDTO : userDepartmentDTOS) {
            DepartmentStepTreeVO.User user = new DepartmentStepTreeVO.User();
            user.setId(userDepartmentDTO.getId());
            user.setUserId(userDepartmentDTO.getUserId());
            user.setAccount(userDepartmentDTO.getAccount());
            user.setName(userDepartmentDTO.getName());
            user.setRealname(userDepartmentDTO.getRealname());
            userList.add(user);
        }
        return userList;
    }
}











