package com.carlos.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.UserConvert;
import com.carlos.org.manager.DepartmentManager;
import com.carlos.org.manager.UserDepartmentManager;
import com.carlos.org.manager.UserManager;
import com.carlos.org.mapper.DepartmentMapper;
import com.carlos.org.mapper.RoleMapper;
import com.carlos.org.mapper.UserMapper;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.dto.UserDeptRoleDTO;
import com.carlos.org.pojo.dto.UserExportDTO;
import com.carlos.org.pojo.entity.*;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.carlos.org.pojo.excel.UserPageExcel;
import com.carlos.org.pojo.param.ApiUserDeptRoleParam;
import com.carlos.org.pojo.param.UserExcludeDeptPageParam;
import com.carlos.org.pojo.param.UserPageParam;
import com.carlos.org.pojo.vo.UserBaseVO;
import com.carlos.org.pojo.vo.UserFloatCardInfoVO;
import com.carlos.org.pojo.vo.UserListVO;
import com.carlos.org.pojo.vo.UserPageVO;
import com.carlos.system.api.ApiRegion;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 查询封装实现类
 * </p>
 *
 * @author carlos
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UserManagerImpl extends BaseServiceImpl<UserMapper, User> implements UserManager {

    private final RoleMapper roleMapper;

    private final UserDepartmentManager userDepartmentManager;
    private final DepartmentManager departmentManager;

    private final DepartmentMapper departmentMapper;

    @Override
    public boolean add(UserDTO dto) {
        User entity = UserConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'User' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'User' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public UserDTO getUserByAccount(String account) {
        User user = lambdaQuery().eq(User::getAccount, account).ne(User::getState, UserStateEnum.DELETE).one();
        UserDTO dto = UserConvert.INSTANCE.toDTO(user);
        return dto;
    }

    @Override
    public UserDTO getUserByCredentials(String credential) {
        List<User> list = lambdaQuery().and(a -> a.eq(User::getAccount, credential).or(b -> b.eq(User::getPhone, credential)))
                .and(e -> e.ne(User::getState, UserStateEnum.DELETE))
                .list();
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            log.warn("login credential can get user more than one, credential:{}", credential);
        }
        return UserConvert.INSTANCE.toDTO(list.get(0));
    }

    @Override
    public UserDTO getUserByPhone(String mobile) {
        User user = lambdaQuery().eq(User::getPhone, mobile).ne(User::getState, UserStateEnum.DELETE).one();
        UserDTO dto = UserConvert.INSTANCE.toDTO(user);
        return dto;
    }

    @Override
    public List<UserDTO> getUserByPhones(Set<String> phones) {
        List<User> list = lambdaQuery().in(CollectionUtil.isNotEmpty(phones), User::getPhone, phones)
                .ne(User::getState, UserStateEnum.DELETE)
                .list();
        return UserConvert.INSTANCE.toDTO(list);
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'User' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'User' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean writterOff(Set<Serializable> ids) {
        if (CollUtil.isEmpty(ids)) {
            return false;
        }
        return lambdaUpdate().in(User::getId, ids).set(User::getState, UserStateEnum.DELETE).update();
    }

    @Override
    public boolean modify(UserDTO dto) {
        User entity = UserConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'User' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'User' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public UserDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        User entity = getBaseMapper().selectById(id);
        return UserConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<UserPageVO> getPage(UserPageParam param) {
        PageInfo<Object> pageInfo = new PageInfo<>(param);
        PageInfo<User> page = getBaseMapper().pageAsAuthLimit(pageInfo, param);
        return MybatisPage.convert(page, this::convertUsersToPageVOs);
    }

    private List<UserPageVO> convertUsersToPageVOs (List<User> items) {
        if (CollUtil.isEmpty(items)) {
            return Collections.emptyList();
        }
        // 获取外部服务
        ApiRegion api = SpringUtil.getBean(ApiRegion.class);

        // 批量获取所有用户的部门ID映射
        Set<String> userIds = items.stream().map(User::getId).collect(Collectors.toSet());

        // 批量获取用户关联部门id
        Map<String, Set<String>> userDepartmentMap = userDepartmentManager.getDepartmentIdsByUserIds(userIds);

        // 获取所有涉及的部门ID并批量获取部门信息
        Set<String> allDepartmentIds = userDepartmentMap.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        Map<String, DepartmentDTO> departmentMap = Collections.emptyMap();
        if (CollUtil.isNotEmpty(allDepartmentIds)) {
            List<DepartmentDTO> departments = departmentManager.getByIds(allDepartmentIds);
            departmentMap = departments.stream()
                    .collect(Collectors.toMap(DepartmentDTO::getId, dept -> dept, (d1, d2) -> d1));
        }

        // 批量获取所有父级部门信息
        Map<String, List<DepartmentDTO>> parentDepartmentMap = new HashMap<>();
        for (String deptId : allDepartmentIds) {
            List<DepartmentDTO> parentDepts = departmentManager.getParentDepartment(deptId, 1);
            parentDepartmentMap.put(deptId, parentDepts);
        }

        // 转换用户数据
        List<UserPageVO> result = new ArrayList<>();
        for (User item : items) {
            UserPageVO vo = UserConvert.INSTANCE.toPage(item);

            Set<String> departmentIds = userDepartmentMap.get(item.getId());
            if (CollUtil.isNotEmpty(departmentIds)) {
                List<DepartmentDTO> departments = departmentIds.stream()
                        .map(departmentMap::get)
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparing(DepartmentDTO::getSort)
                                .thenComparing(DepartmentDTO::getCreateTime, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

                if (CollUtil.isNotEmpty(departments)) {
                    List<String> deptNames = departments.stream()
                            .map(dept -> {
                                List<DepartmentDTO> parentDepts = parentDepartmentMap.getOrDefault(dept.getId(), Collections.emptyList());
                                List<String> names = parentDepts.stream()
                                        .map(DepartmentDTO::getDeptName)
                                        .collect(Collectors.toList());
                                return StrUtil.join(StrUtil.DASHED, names);
                            })
                            .collect(Collectors.toList());
                    vo.setDepartment(CharSequenceUtil.join(StrPool.COMMA, deptNames));
                }
            }

            result.add(vo);
        }

        return result;
    }

    @Override
    public List<UserPageExcel> getParamAll(UserPageParam param) {
        List<UserExportDTO> users = getBaseMapper().listAsAuthLimit(param);
        ApiRegion api = SpringUtil.getBean(ApiRegion.class);

        Set<String> userIds = users.stream().map(UserExportDTO::getId).collect(Collectors.toSet());
        // 并行获取用户部门关联信息
        Map<String, Set<String>> userDepartmentMap = new ConcurrentHashMap<>();
        if (CollUtil.isNotEmpty(userIds)) {
            userIds.parallelStream().forEach(userId -> {
                Set<String> departmentIds = userDepartmentManager.getDepartmentIdByUserId(userId);
                userDepartmentMap.put(userId, departmentIds);
            });
        }

        // 收集所有部门ID
        Set<String> allDepartmentIds = userDepartmentMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet());

        // 批量获取所有部门信息
        List<DepartmentDTO> allDepartments = CollUtil.isNotEmpty(allDepartmentIds) ? departmentManager.getByIds(allDepartmentIds) : Collections.emptyList();
        Map<String, DepartmentDTO> departmentMap = allDepartments.stream().collect(Collectors.toMap(DepartmentDTO::getId, d -> d, (d1, d2) -> d1));

        // 并行获取所有父级部门信息
        Map<String, List<DepartmentDTO>> parentDepartmentMap = new ConcurrentHashMap<>();
        if (CollUtil.isNotEmpty(allDepartmentIds)) {
            allDepartmentIds.parallelStream().forEach(deptId -> {
                List<DepartmentDTO> parentDepts = this.departmentManager.getParentDepartment(deptId, 1);
                parentDepartmentMap.put(deptId, parentDepts);
            });
        }


        // 并行处理每个用户的数据
        List<UserPageExcel> excelList = users.parallelStream().map(user -> {
            UserPageExcel excel = new UserPageExcel();
            excel.setAccount(user.getAccount());
            excel.setRealname(user.getRealname());
            excel.setPhone(user.getPhone());
            excel.setDeptName(user.getDeptName());
            excel.setRegionName(user.getRegionName());
            excel.setRoleNames(user.getRoleNames());
            excel.setState(user.getStateEnum().getDesc());
            excel.setCreateTime(user.getCreateTime());
            excel.setSort(user.getSort());

            Set<String> departmentIds = userDepartmentMap.get(user.getId());
            if (CollUtil.isNotEmpty(departmentIds)) {
                List<DepartmentDTO> departments = departmentIds.stream().map(departmentMap::get).filter(Objects::nonNull).collect(Collectors.toList());
                List<String> deptNames = new ArrayList<>();
                List<String> regionNames = new ArrayList<>();
                // 合并处理部门名称和区域信息
                for (DepartmentDTO dept : departments) {
                    // 处理部门名称
                    List<DepartmentDTO> parentDepts = parentDepartmentMap.getOrDefault(dept.getId(), Collections.emptyList());
                    List<String> names = parentDepts.stream()
                            .map(DepartmentDTO::getDeptName)
                            .collect(Collectors.toList());
                    deptNames.add(StrUtil.join(StrUtil.DASHED, names));
                }
                excel.setDeptName(CharSequenceUtil.join(StrPool.COMMA, deptNames));
                excel.setRegionName(CharSequenceUtil.join(StrPool.COMMA, regionNames));
            }
            return excel;
        }).collect(Collectors.toList());

        return excelList;
    }


    @Override
    public int getCountByAccount(String account) {
        return lambdaQuery().eq(User::getAccount, account).ne(User::getState, UserStateEnum.DELETE).count().intValue();
    }

    @Override
    public UserDTO selectByAccountAndId(String account, String id) {
        if (StrUtil.isNotBlank(id)) {
            lambdaQuery().eq(User::getId, id);
        }
        if (StrUtil.isNotBlank(account)) {
            lambdaQuery().eq(User::getAccount, account);
        }
        return UserConvert.INSTANCE.toDTO(lambdaQuery().eq(User::getId, id).one());
    }

    @Override
    public List<UserDTO> getByIds(Set<String> userIds) {
        if (CollectionUtil.isEmpty(userIds)) {
            return null;
        }
        return UserConvert.INSTANCE.toDTO(lambdaQuery().in(User::getId, userIds).list());
    }

    @Override
    public boolean deleteAll() {
        boolean success = remove(new QueryWrapper<>());
        if (!success) {
            log.warn("deleted all 'User' data fail");
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("deleted all 'User' data");
        }
        return true;
    }

    @Override
    public List<UserDTO> listAll() {
        return UserConvert.INSTANCE.toDTO(lambdaQuery().ne(User::getState, UserStateEnum.DELETE).orderByAsc(User::getSort).orderByDesc(User::getCreateTime).list());
    }

    @Override
    public List<UserDTO> listAllWithSelectedFields() {
        List<User> list = lambdaQuery()
                .select(User::getAccount, User::getState, User::getCreateTime, User::getId)
                .ne(User::getState, UserStateEnum.DELETE)
                .list();

        return UserConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<UserDTO> listByKeyword(String keyword) {

        List<User> list = lambdaQuery().ne(User::getState, UserStateEnum.DELETE)
                .and(StrUtil.isNotBlank(keyword), w -> w
                        .like(User::getRealname, keyword)
                        .or()
                        .like(User::getAccount, keyword)
                        .or()
                        .like(User::getPhone, keyword)
                )
                .orderByAsc(User::getSort)
                .orderByDesc(User::getCreateTime)
                .list();
        return UserConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<UserDTO> listWithKeyword(String keyword) {
        List<User> list = lambdaQuery().ne(User::getState, UserStateEnum.DELETE)
                .eq(StrUtil.isNotBlank(keyword), User::getRealname, keyword)
                .or()
                .eq(StrUtil.isNotBlank(keyword), User::getAccount, keyword)
                .or()
                .eq(StrUtil.isNotBlank(keyword), User::getPhone, keyword)
                .orderByAsc(User::getSort)
                .orderByDesc(User::getCreateTime)
                .list();
        return UserConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<UserDTO> listById(Set<String> ids) {
        List<User> list = lambdaQuery().in(CollectionUtil.isNotEmpty(ids), User::getId, ids).list();
        return UserConvert.INSTANCE.toDTO(list);
    }

    @Override
    public int getCountByPhoneExcludeId(String phone, String id) {
        return lambdaQuery().ne(StrUtil.isNotBlank(id), User::getId, id)
                .ne(User::getState, UserStateEnum.DELETE)
                .eq(User::getPhone, phone).count().intValue();
    }

    @Override
    public Paging<UserBaseVO> excludeByDept(UserExcludeDeptPageParam param) {
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
                .selectAs(User::getAccount, UserBaseVO::getAccount)
                .selectAs(User::getId, UserBaseVO::getId)
                .selectAs(User::getRealname, UserBaseVO::getRealname)
                .selectAs(User::getPhone, UserBaseVO::getPhone)
                .selectAs(User::getCreateTime, UserBaseVO::getCreateTime);
        String id = param.getId();
        wrapper.leftJoin(UserDepartment.class, UserDepartment::getUserId, User::getId).ne(UserDepartment::getDepartmentId, id)
                .orderByAsc(User::getSort).orderByDesc(User::getCreateTime);
        PageInfo<UserBaseVO> page = getBaseMapper().selectJoinPage(new PageInfo<>(param), UserBaseVO.class, wrapper);
        return MybatisPage.convert(page, items -> items);
    }

    @Override
    public List<UserDeptRoleDTO> getUserByDeptAndRole(ApiUserDeptRoleParam param) {
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .selectAs(Role::getId, UserDeptRoleDTO::getRoleId)
                .selectAs(Role::getName, UserDeptRoleDTO::getRoleName)
                .in(CollUtil.isNotEmpty(param.getRoleIds()), Role::getId, param.getRoleIds())
                .in(CollUtil.isNotEmpty(param.getRoleNames()), Role::getName, param.getRoleNames());

        wrapper.leftJoin(UserRole.class, UserRole::getRoleId, Role::getId)
                .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                .selectAs(User::getId, UserDeptRoleDTO::getUserId)
                .selectAs(User::getRealname, UserDeptRoleDTO::getUserName)
                .selectAs(User::getPhone, UserDeptRoleDTO::getPhone)
                .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                .selectAs(Department::getId, UserDeptRoleDTO::getDeptId)
                .selectAs(Department::getDeptName, UserDeptRoleDTO::getDeptName)
                .selectAs(Department::getDeptCode, UserDeptRoleDTO::getDeptCode)
                .in(CollUtil.isNotEmpty(param.getDeptCodes()), Department::getDeptCode, param.getDeptCodes());

        List<UserDeptRoleDTO> users = roleMapper.selectJoinList(UserDeptRoleDTO.class, wrapper);
        return users;
    }

    @Override
    public Paging<UserDTO> getInDeptUser(UserExcludeDeptPageParam param, Set<String> userSet) {
        String keyword = param.getKeyword();
        LambdaQueryWrapper<User> wrapper = queryWrapper().ne(User::getState, UserStateEnum.DELETE)
//                .and(w -> w.notIn(CollectionUtil.isNotEmpty(userSet), User::getId, userSet))
                .and(CollectionUtil.isNotEmpty(userSet), w -> w.in(User::getId, userSet));
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(User::getRealname, keyword)
                    .or(e -> e.like(User::getAccount, keyword))
                    .or(x -> x.like(User::getPhone, keyword)));
        }
        wrapper.orderByAsc(User::getSort);
        //增加时间降序
        wrapper.orderByDesc(User::getCreateTime);
        PageInfo<User> userPageInfo = page(new PageInfo<>(param), wrapper);
        return MybatisPage.convert(userPageInfo, UserConvert.INSTANCE::toDTO);
    }

    @Override
    public Long getActivityCount(String startTime, String endTime, Set<String> userIds) {
        return lambdaQuery().ne(User::getState, UserStateEnum.DELETE)
                .in(CollectionUtil.isNotEmpty(userIds), User::getId, userIds)
                .ge(StrUtil.isNotBlank(startTime), User::getLastLogin, startTime)
                .le(StrUtil.isNotBlank(endTime), User::getLastLogin, endTime).count();
    }

    @Override
    public String getPhoneByUserId(String userId) {
        User one = lambdaQuery().eq(User::getId, userId).one();
        return Optional.ofNullable(one).map(User::getPhone).orElse(null);
    }

    @Override
    public List<UserDTO> getByRoleId(String id) {
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
                .selectAll(User.class)
                .leftJoin(UserRole.class, UserRole::getUserId, User::getId)
                .eq(UserRole::getRoleId, id);
        return getBaseMapper().selectJoinList(UserDTO.class, wrapper);
    }

    @Override
    public List<UserDTO> getAdminUser() {
        LambdaQueryWrapper<User> wrapper = queryWrapper().eq(User::getIsAdmin, 1).orderByDesc(User::getLoginCount);
        List<User> list = list(wrapper);
        return UserConvert.INSTANCE.toDTO(list);
    }

    @Override
    public PageInfo<UserListVO> search(UserPageParam param) {
        String keyword = param.getKeyword();
        Set<String> deptLevels = param.getDeptLevels();

        PageInfo<UserListVO> userDTOPageInfo = getBaseMapper().selectJoinPage(new PageInfo<>(param), UserListVO.class,
                new MPJLambdaWrapper<User>()
                        .selectAs(User::getId, UserListVO::getId)
                        .selectAs(User::getAccount, UserListVO::getAccount)
                        .selectAs(User::getRealname, UserListVO::getRealname)
                        .selectAs(User::getPhone, UserListVO::getPhone)
                        .selectAs(User::getCreateTime, UserListVO::getCreateTime)
                        .selectAs(Department::getDeptName, UserListVO::getDepartmentName)
                        .selectAs(Department::getId, UserListVO::getDepartmentId)
                        .selectAs(Role::getName, UserListVO::getRoleName)
                        .leftJoin(UserDepartment.class, UserDepartment::getUserId, User::getId)
                        .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                        .leftJoin(User.class, User::getId, User::getCreateBy, ext -> ext
                                .selectAs(User::getRealname, UserListVO::getCreateBy))
                        .ne(User::getState, UserStateEnum.DELETE)
                        .and(StrUtil.isNotBlank(keyword), w -> w
                                .like(User::getRealname, keyword)
                                .or()
                                .like(User::getAccount, keyword)
                                .or()
                                .like(User::getPhone, keyword)
                        ));
        return userDTOPageInfo;
    }

    @Override
    public List<UserDTO> listUserByDeptCode(String deptCode) {
        List<UserDTO> users = getBaseMapper().selectJoinList(UserDTO.class,
                new MPJLambdaWrapper<User>()
                        .selectAll(User.class)
                        .selectAs(User::getId, UserDTO::getId)
                        .selectAs(User::getState, UserDTO::getState)
                        .selectAs(User::getLastLogin, UserDTO::getLastLogin)
                        .rightJoin(UserDepartment.class, UserDepartment::getUserId, User::getId)
                        .rightJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                        .likeRight(Department::getDeptCode, deptCode)
        );
        return users;
    }

    @Override
    public UserFloatCardInfoVO getUserCardInfo(String userId, String deptId, String deptCode) {
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
                .selectAs(User::getId, UserFloatCardInfoVO::getId)
                .selectAs(User::getRealname, UserFloatCardInfoVO::getRealname)
                .selectAs(User::getAccount, UserFloatCardInfoVO::getAccount)
                .selectAs(User::getHead, UserFloatCardInfoVO::getHead)
                .selectAs(User::getPhone, UserFloatCardInfoVO::getPhone)
                .selectAs(User::getEmail, UserFloatCardInfoVO::getEmail)
                .selectAs(User::getGender, UserFloatCardInfoVO::getGender);
        wrapper.leftJoin(UserDepartment.class, UserDepartment::getUserId, User::getId)
                .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                .leftJoin(Role.class, Role::getId, UserRole::getRoleId)
                .selectCollection(UserFloatCardInfoVO::getDepts, map ->
                        map.result(Department::getDeptCode, UserFloatCardInfoVO.DepartmentRole::getDeptCode)
                                .result(Department::getDeptName, UserFloatCardInfoVO.DepartmentRole::getDeptName)
                                .result(Role::getName, UserFloatCardInfoVO.DepartmentRole::getRoleName)
                );
        wrapper.eq(User::getId, userId);
        wrapper.eq(ObjectUtil.isNotEmpty(deptId), Department::getId, deptId);
        wrapper.eq(ObjectUtil.isNotEmpty(deptCode), Department::getDeptCode, deptCode);
        UserFloatCardInfoVO user = baseMapper.selectJoinOne(UserFloatCardInfoVO.class, wrapper);
        return user;
    }

    @Override
    public List<UserDTO> listIncludeSubUser(String departmentId) {
        Set<String> deptIds = Collections.singleton(departmentId);
        Set<String> deptIdsAll = new HashSet<>(deptIds);
        deptIds.forEach(e -> deptIdsAll.addAll(departmentManager.getAllSubDeptById(e.toString()).stream().map(DepartmentDTO::getId).collect(Collectors.toSet())));
        return baseMapper.listByDeptIds(deptIdsAll);
    }
}
