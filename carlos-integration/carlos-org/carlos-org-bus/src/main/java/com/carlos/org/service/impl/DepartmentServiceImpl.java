package com.carlos.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.core.util.ExecutorUtil;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.UserUtil;
import com.carlos.org.convert.DepartmentConvert;
import com.carlos.org.enums.DeptRelationEnum;
import com.carlos.org.manager.DepartmentManager;
import com.carlos.org.manager.OrgDockingMappingManager;
import com.carlos.org.manager.UserDepartmentManager;
import com.carlos.org.manager.UserManager;
import com.carlos.org.mapper.DepartmentMapper;
import com.carlos.org.pojo.ao.UserLoginAO;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.entity.Department;
import com.carlos.org.pojo.entity.UserDepartment;
import com.carlos.org.pojo.enums.OrgDockingTypeEnum;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.vo.DepartmentBaseVO;
import com.carlos.org.pojo.vo.DepartmentStepTreeVO;
import com.carlos.org.service.DepartmentService;
import com.carlos.org.service.OrgDockingMappingService;
import com.carlos.org.service.UserDepartmentService;
import com.google.common.collect.Lists;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


/**
 * <p>
 * 部门 业务接口实现类
 * </p>
 *
 * @author carlos
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {


    private final DepartmentManager departmentManager;
    private final UserDepartmentService userDepartmentService;
    private final UserDepartmentManager userDepartmentManager;
    private final UserManager userManager;
    private final OrgDockingMappingManager orgDockingMappingManager;
    private final OrgDockingMappingService orgDockingMappingService;

    private final DepartmentMapper departmentMapper;

    private static final ThreadPoolExecutor DEPARTMENT_OPERATE_POOL = ExecutorUtil.get(2, 5, "departmentOperate-", 20, null);

    @Override
    public void saveOrUpdate(DepartmentDTO dto) {
        String parentId = dto.getParentId();
        if (CharSequenceUtil.isBlank(parentId) || "0".equals(parentId)) {
            dto.setParentId("0");
        }
        // 名称重复校验
        checkDeptName(dto);
        // 新增场景
        if (StrUtil.isBlank(dto.getId())) {
            String parentCode = CharSequenceUtil.EMPTY;
            if (!"0".equals(parentId)) {
                DepartmentDTO parent = departmentManager.getDtoById(parentId);
                parentCode = parent.getDeptCode();
            }
            // 获取部门编号
            dto.setDeptCode(parentCode + getNextDepartmentCode(parentCode));
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
    public void initCache() {
        this.userDepartmentManager.initCache();
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
    public Paging<UserDepartmentDTO> getCurSubUser(CurSubExecutorPageParam param) {
        // 获取用户信息
        Paging<UserDepartmentDTO> departmentUsers = userDepartmentService.getCurSubUser(param);
        return departmentUsers;
    }

    @Override
    public Paging<UserDepartmentDTO> getCurDeptUser(CurDeptExecutorPageParam param) {
        return userDepartmentService.getCurDeptUser(param);
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
        return dtos;
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

    @Override
    public Paging<UserDepartmentDTO> getUserPageByDeptId(TaskExecutorPageMianYangParam param) {
        // 获取用户信息
        Paging<UserDepartmentDTO> departmentUsers = userDepartmentManager.getUserPageByDeptId(param);
        return departmentUsers;
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
    public Set<String> getAllDepartCodes(String parentId) {
        List<String> allDepartCodes = departmentMapper.getAllDepartCodes(parentId);
        Set<String> deptCodeSet = new HashSet<>(allDepartCodes); // 如果需要Set，可以轻松转换
        return deptCodeSet;
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











