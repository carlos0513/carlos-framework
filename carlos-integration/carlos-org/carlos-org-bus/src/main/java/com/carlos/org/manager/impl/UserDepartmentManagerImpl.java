package com.carlos.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.UserDepartmentConvert;
import com.carlos.org.manager.UserDepartmentManager;
import com.carlos.org.manager.UserManager;
import com.carlos.org.mapper.UserDepartmentMapper;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.entity.Department;
import com.carlos.org.pojo.entity.User;
import com.carlos.org.pojo.entity.UserDepartment;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.carlos.org.pojo.param.CurDeptExecutorPageParam;
import com.carlos.org.pojo.param.CurSubExecutorPageParam;
import com.carlos.org.pojo.param.TaskExecutorPageMianYangParam;
import com.carlos.org.pojo.param.UserPageParam;
import com.carlos.redis.util.RedisUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户部门 查询封装实现类
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UserDepartmentManagerImpl extends BaseServiceImpl<UserDepartmentMapper, UserDepartment> implements UserDepartmentManager {

    private static final String DEPARTMENT_USER_ROLE = "user:dept_user_role:%s:%s:%s";


    private final UserDepartmentMapper userDepartmentMapper;

    @Override
    public void initCache() {
        // 分页处理大量数据，避免一次性加载过多数据到内存，也避免同时写入redis，造成redis阻塞
        int pageSize = 1000;
        int currentPage = 1;
        List<UserDepartmentDTO> list;
        Map<String, UserDepartmentDTO> cacheMap = new HashMap<>((int) (pageSize / 0.75f) + 1);
        long startTime = System.currentTimeMillis();
        int totalProcessed = 0;
        log.info("开始初始化UserDepartment缓存");

        try {
            do {
                PageInfo<UserDepartmentDTO> pageInfo = getBaseMapper().selectJoinPage(
                        new PageInfo<>(currentPage, pageSize),
                        UserDepartmentDTO.class,
                        new MPJLambdaWrapper<UserDepartment>()
                                .selectAll(UserDepartment.class)
                                .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
                                .selectAs(User::getId, UserDepartmentDTO::getUserId)
                                .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
                                .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
                                .selectAs(User::getState, UserDepartmentDTO::getState)
                                .selectAs(User::getSort, UserDepartmentDTO::getSort)
                                .selectAs(User::getCreateTime, UserDepartmentDTO::getCreateTime)
                                .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                );

                list = pageInfo.getRecords();

                for (UserDepartmentDTO dto : list) {
                    cacheMap.put(String.format(DEPARTMENT_USER_ROLE, dto.getDepartmentId(), dto.getUserId(), dto.getRoleId()), dto);
                }

                // 批量写入缓存，避免频繁操作Redis
                if (CollUtil.isNotEmpty(cacheMap)) {
                    RedisUtil.setValueList(cacheMap);
                    totalProcessed += cacheMap.size();
                    log.debug("成功写入{}条UserDepartment缓存记录到Redis", cacheMap.size());
                    cacheMap.clear();
                }
                currentPage++;
            } while (CollUtil.isNotEmpty(list));
            log.info("UserDepartment缓存初始化完成，共处理{}条记录，耗时:{}", totalProcessed, DateUtil.formatBetween(DateUtil.spendMs(startTime)));
        } catch (Exception e) {
            log.error("UserDepartment缓存初始化过程中发生异常", e);
            throw e;
        }
    }


    @Override
    public boolean add(List<UserDepartmentDTO> dtos) {
        List<UserDepartment> entity = UserDepartmentConvert.INSTANCE.toDO(dtos);
        boolean success = saveBatch(entity);
        if (!success) {
            log.warn("Insert 'UserDepartment' data fail, entity:{}", entity);
            return false;
        }
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'UserDepartment' data: entity:{}", entity);
        }
        dtos.forEach(i -> RedisUtil.setValue(String.format(DEPARTMENT_USER_ROLE, i.getDepartmentId(), i.getUserId(), i.getRoleId()), i));
        return true;
    }

    @Override
    public boolean deleteByUserId(String userId) {
        if (userId == null) {
            log.warn("userId can't be null");
            return false;
        }
        boolean success = lambdaUpdate().eq(UserDepartment::getUserId, userId).remove();
        if (!success) {
            log.warn("Remove 'UserDepartment' data fail, userId:{}", userId);
            return false;
        }
        // 删除缓存
        RedisUtil.deletePattern(String.format(DEPARTMENT_USER_ROLE, RedisUtil.ALL, userId, RedisUtil.ALL));
        if (log.isDebugEnabled()) {
            log.debug("Remove 'UserDepartment' data by userId:{}", userId);
        }
        return true;
    }

    @Override
    public boolean deleteBatch(List<UserDepartmentDTO> dtos) {
        for (UserDepartmentDTO dto : dtos) {
            boolean success = lambdaUpdate()
                    .eq(UserDepartment::getUserId, dto.getUserId())
                    .eq(Objects.nonNull(dto.getRoleId()), UserDepartment::getRoleId, dto.getRoleId())
                    .eq(UserDepartment::getDepartmentId, dto.getDepartmentId())
                    .remove();
            if (!success) {
                log.warn("Remove 'UserDepartment' data fail, userId:{} departmentId:{}", dto.getUserId(), dto.getDepartmentId());
                return false;
            }
            if (log.isDebugEnabled()) {
                log.debug("Remove 'UserDepartment' data success, userId:{} departmentId:{}", dto.getUserId(), dto.getDepartmentId());
            }
            RedisUtil.deletePattern(String.format(DEPARTMENT_USER_ROLE, dto.getDepartmentId(), dto.getUserId(), RedisUtil.ALL));
        }
        return false;
    }

    @Override
    public Set<String> getUserIdByDepartmentId(String departmentId) {
        List<UserDepartmentDTO> dtos = RedisUtil.getValueList(String.format(DEPARTMENT_USER_ROLE, departmentId, RedisUtil.ALL, RedisUtil.ALL));
        if (CollUtil.isNotEmpty(dtos)) {
            return dtos.stream().map(UserDepartmentDTO::getUserId).collect(Collectors.toSet());
        } else {
            return null;
        }
        // List<UserDepartment> list = lambdaQuery().select(UserDepartment::getUserId).eq(UserDepartment::getDepartmentId, departmentId).list();
        // if (CollectionUtil.isEmpty(list)) {
        //     return null;
        // }
        // return list.stream().map(UserDepartment::getUserId).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getDepartmentIdByUserId(String userId) {
        List<UserDepartment> list = lambdaQuery().select(UserDepartment::getDepartmentId).eq(UserDepartment::getUserId, userId).list();
        if (list == null) {
            return null;
        }
        return list.stream().map(UserDepartment::getDepartmentId).collect(Collectors.toSet());
    }

    @Override
    public Map<String, Set<String>> getDepartmentIdsByUserIds(Set<String> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyMap();
        }

        List<UserDepartment> list = lambdaQuery().select(UserDepartment::getUserId, UserDepartment::getDepartmentId)
                .in(UserDepartment::getUserId, userIds).list();
        // 按用户ID分组，收集部门ID
        return list.stream()
                .collect(Collectors.groupingBy(UserDepartment::getUserId,
                        Collectors.mapping(UserDepartment::getDepartmentId, Collectors.toSet())
                ));

    }

    @Override
    public Set<String> getRoleIdsByUserId(String userId) {
        List<UserDepartment> list = lambdaQuery().select(UserDepartment::getRoleId).eq(UserDepartment::getUserId, userId).list();
        if (list == null) {
            return null;
        }
        return list.stream().filter(Objects::nonNull).map(UserDepartment::getRoleId).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public Set<Serializable> getUserIdByDepartmentId(Set<Serializable> departmentIds) {
        List<UserDepartment> list = lambdaQuery().select(UserDepartment::getUserId).in(UserDepartment::getDepartmentId, departmentIds).list();
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.stream().map(UserDepartment::getUserId).collect(Collectors.toSet());
    }

    @Override
    public List<UserDepartmentDTO> listByDepartmentId(String id) {
        return getBaseMapper().selectJoinList(UserDepartmentDTO.class,
                new MPJLambdaWrapper<UserDepartment>()
                        .selectAll(UserDepartment.class)
                        .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
                        .selectAs(User::getId, UserDepartmentDTO::getUserId)
                        .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
                        .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
                        .selectAs(User::getState, UserDepartmentDTO::getState)
                        .selectAs(User::getSort, UserDepartmentDTO::getSort)
                        .selectAs(User::getCreateTime, UserDepartmentDTO::getCreateTime)
                        .selectAs(Department::getDepartmentLevelCode, UserDepartmentDTO::getDepartmentLevelCode)
                        .selectAs(Department::getDeptCode, UserDepartmentDTO::getDeptCode)
                        .selectAs(Department::getDeptCode, UserDepartmentDTO::getDepartmentCode)
                        .selectAs(Department::getRegionCode, UserDepartmentDTO::getRegionCode)
                        .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                        .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                        .eq(UserDepartment::getDepartmentId, id)
                        .orderByAsc(User::getSort).orderByDesc(User::getCreateTime));

    }

    @Override
    public List<DepartmentDTO> getDepartmentsByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            return Collections.emptyList();
        }

        MPJLambdaWrapper<UserDepartment> wrapper = new MPJLambdaWrapper<UserDepartment>()
                .select(Department::getDeptCode)
                .select(Department::getDeptName)
                .select(Department::getDescription)
                .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                .eq(UserDepartment::getUserId, userId);

        return userDepartmentMapper.selectJoinList(DepartmentDTO.class, wrapper);
    }

    @Override
    public List<UserDepartmentDTO> listAdminByDepartmentId(String id) {
        return getBaseMapper().selectJoinList(UserDepartmentDTO.class,
                new MPJLambdaWrapper<UserDepartment>().selectAll(UserDepartment.class).selectAs(User::getAccount,
                        UserDepartmentDTO::getAccount).selectAs(User::getId, UserDepartmentDTO::getUserId).selectAs(User::getRealname,
                        UserDepartmentDTO::getRealname).selectAs(User::getPhone, UserDepartmentDTO::getPhone).selectAs(User::getState,
                        UserDepartmentDTO::getState).leftJoin(User.class, User::getId, UserDepartment::getUserId).eq(UserDepartment::getDepartmentId,
                        id).eq(UserDepartment::getIsAdmin, true));
    }

    @Override
    public List<UserDepartmentDTO> listAdminByDepartmentIds(List<String> deptIds) {
        return getBaseMapper().selectJoinList(UserDepartmentDTO.class,
                new MPJLambdaWrapper<UserDepartment>().selectAll(UserDepartment.class).selectAs(User::getAccount,
                        UserDepartmentDTO::getAccount).selectAs(User::getId, UserDepartmentDTO::getUserId).selectAs(User::getRealname,
                        UserDepartmentDTO::getRealname).selectAs(User::getPhone, UserDepartmentDTO::getPhone).selectAs(User::getState,
                        UserDepartmentDTO::getState).leftJoin(User.class, User::getId, UserDepartment::getUserId).in(UserDepartment::getDepartmentId,
                        deptIds).eq(UserDepartment :: getIsAdmin, true));
    }

    @Override
    public List<UserDepartmentDTO> listAllRef() {
        return getBaseMapper().selectJoinList(UserDepartmentDTO.class,
                new MPJLambdaWrapper<UserDepartment>()
                        .selectAll(UserDepartment.class)
                        .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
                        .selectAs(User::getId, UserDepartmentDTO::getUserId)
                        .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
                        .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
                        .selectAs(User::getState, UserDepartmentDTO::getState)
                        .selectAs(User::getSort, UserDepartmentDTO::getSort)
                        .selectAs(User::getCreateTime, UserDepartmentDTO::getCreateTime)
                        .selectAs(Department::getDepartmentLevelCode, UserDepartmentDTO::getDepartmentLevelCode)
                        .selectAs(Department::getDeptCode, UserDepartmentDTO::getDepartmentCode)
                        .selectAs(Department::getDeptName, UserDepartmentDTO::getDepartmentName)
                        .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                        .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
        );
    }

    @Override
    public PageInfo<UserDepartmentDTO> listByDepartmentIdPage(String id, UserPageParam page) {

//        return getBaseMapper().selectJoinPage(new PageInfo<>(page), UserDepartmentDTO.class,
//                new MPJLambdaWrapper<UserDepartment>().selectAll(UserDepartment.class)
//                        .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
//                        .selectAs(User::getId, UserDepartmentDTO::getUserId)
//                        .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
//                        .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
//                        .selectAs(User::getState, UserDepartmentDTO::getState)
//                        .selectAs(User::getSort, UserDepartmentDTO::getSort)
//                        .selectAs(User::getCreateTime, UserDepartmentDTO::getCreateTime)
//                        .selectFunc(() -> "GROUP_CONCAT(DISTINCT %s SEPARATOR ',')", Role::getName, UserDepartmentDTO::getRoleNames)
//                        .leftJoin(User.class, User::getId, UserDepartment::getUserId)
//                        .leftJoin(DepartmentRole.class, DepartmentRole::getDepartmentId, UserDepartment::getDepartmentId)
//                        .leftJoin(UserRole.class, on -> on.eq(UserRole::getUserId, User::getId).eq(UserRole::getRoleId, DepartmentRole::getRoleId))
//                        .leftJoin(Role.class, Role::getId, UserRole::getRoleId)
//                        .eq(UserDepartment::getDepartmentId, id)
//                        .and(StrUtil.isNotEmpty(page.getKeyword()), e -> e.like(User::getAccount, page.getKeyword())
//                                .or().like(User::getRealname, page.getKeyword())
//                                .or().like(User::getPhone, page.getKeyword())
//                                .or().like(Role::getName, page.getKeyword()))
//                        .groupBy(UserDepartment::getId)
//                        .orderByAsc(User::getSort).orderByDesc(User::getCreateTime));
        // 由于上述使用mpj，会将默认is_deleted拼接上，会导致查询出来的数据缺失，故使用原始xml方式
        // is_deleted应该放在left join on 条件中
        return userDepartmentMapper.listByDepartmentIdPage(new PageInfo<>(page), id, page.getKeyword());
    }

    @Override
    public List<UserDepartmentDTO> listByDepartmentIdPage(List<String> ids){
        return userDepartmentMapper.listByDepartmentIdList(ids);
    }

    @Override
    public List<UserDepartmentDTO> listByDeptIds(Set<String> deptIds) {
        return getBaseMapper().selectJoinList(UserDepartmentDTO.class,
                new MPJLambdaWrapper<UserDepartment>().selectAll(UserDepartment.class)
                        .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
                        .selectAs(User::getId, UserDepartmentDTO::getUserId)
                        .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
                        .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
                        .selectAs(User::getState, UserDepartmentDTO::getState)
                        .selectAs(User::getSort, UserDepartmentDTO::getSort)
                        .selectAs(User::getCreateTime, UserDepartmentDTO::getCreateTime)
                        .selectAs(Department::getDepartmentLevelCode, UserDepartmentDTO::getDepartmentLevelCode)
                        .selectAs(Department::getDeptCode, UserDepartmentDTO::getDepartmentCode)
                        .selectAs(Department::getDeptName, UserDepartmentDTO::getDepartmentName)
                        .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                        .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                        .in(CollUtil.isNotEmpty(deptIds),UserDepartment::getDepartmentId, deptIds)
                        .ne(User::getState, UserStateEnum.DELETE)
                        .orderByAsc(User::getSort).orderByDesc(User::getCreateTime));
    }

    @Override
    public List<UserDepartmentDTO> listAll() {
        return RedisUtil.getValueList(String.format(DEPARTMENT_USER_ROLE, RedisUtil.ALL, RedisUtil.ALL, RedisUtil.ALL));
    }

    @Override
    public Paging<UserDepartmentDTO> listCurDept(CurDeptExecutorPageParam param) {
        return listByDepartmentCode(UserDepartmentConvert.INSTANCE.toSubParam(param));
    }

    @Override
    public Paging<UserDepartmentDTO> listByDepartmentCode(CurSubExecutorPageParam param) {
        PageInfo<UserDepartmentDTO> pageInfo = getBaseMapper().selectJoinPage(new PageInfo<>(param), UserDepartmentDTO.class, new MPJLambdaWrapper<UserDepartment>()
                .selectAll(UserDepartment.class)
                .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
                .selectAs(User::getId, UserDepartmentDTO::getUserId)
                .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
                .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
                .selectAs(User::getState, UserDepartmentDTO::getState)
                .selectAs(User :: getIsAdmin, UserDepartmentDTO :: getIsAdmin)
                .selectAs(Department::getDepartmentLevelCode, UserDepartmentDTO::getDepartmentLevelCode)
                .selectAs(Department::getDeptCode, UserDepartmentDTO::getDepartmentCode)
                .selectAs(Department::getDeptCode, UserDepartmentDTO::getDeptCode)
                .selectAs(Department::getDeptName, UserDepartmentDTO::getDepartmentName)
                .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                .likeRight(CharSequenceUtil.isNotBlank(param.getDeptCode()), Department::getDeptCode, param.getDeptCode())
                .in(CollUtil.isNotEmpty(param.getDeptIds()), Department::getId, param.getDeptIds())
                //只查询启用后的用户
                .eq(User::getState, "1")
                .and(StrUtil.isNotBlank(param.getUserName()), e -> {
                    //根据用户账号查询
                    e.like(StrUtil.isNotBlank(param.getUserName()), User::getAccount, param.getUserName()).or()
                            //根据用户姓名进行查询
                            .like(StrUtil.isNotBlank(param.getUserName()), User::getRealname, param.getUserName()).or()
                            //根据用户电话查询
                            .like(StrUtil.isNotBlank(param.getUserName()), User::getPhone, param.getUserName());
                })
                //不查询任务的创建者
                .ne(StrUtil.isNotBlank(param.getTaskCreateBy()), User::getId, param.getTaskCreateBy())
                //不查询任务下派人
                .ne(StrUtil.isNotBlank(param.getMyself()), User::getId, param.getMyself())
                .orderByDesc(User :: getIsAdmin)
                .orderByAsc("length(dept_code)")
                .orderByAsc(Department::getDeptCode)
                .orderByAsc(User::getSort)
                .orderByDesc(User::getCreateTime));
        return MybatisPage.convert(pageInfo, items -> items);
    }

    @Override
    public boolean deleteByRoleId(String roelId) {
        if (roelId == null) {
            log.warn("roelId can't be null");
            return false;
        }
        boolean success = lambdaUpdate().eq(UserDepartment::getRoleId, roelId).remove();
        if (!success) {
            log.warn("Remove 'UserDepartment' data fail, roelId:{}", roelId);
            return false;
        }
        // 删除缓存
        RedisUtil.deletePattern(String.format(DEPARTMENT_USER_ROLE, RedisUtil.ALL, RedisUtil.ALL, roelId));
        if (log.isDebugEnabled()) {
            log.debug("Remove 'UserDepartment' data by roelId:{}", roelId);
        }
        return true;
    }

    @Override
    public List<UserDepartmentDTO> getByLevels(Set<String> deptLevels) {
        return getBaseMapper().selectJoinList(UserDepartmentDTO.class,
                new MPJLambdaWrapper<UserDepartment>()
                        .selectAll(UserDepartment.class)
                        .selectAs(Department::getDeptName, UserDepartmentDTO::getDepartmentName)
                        .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                        .and(CollectionUtil.isNotEmpty(deptLevels), w -> w.in(UserDepartment::getDepartmentLevelCode, deptLevels))
        );
    }


    @Override
    public boolean isYxUser(String userId) {

        // 先判断用户id
        List<UserDepartmentDTO> yxUser = userDepartmentMapper.isYxUser(userId);
        if (!yxUser.isEmpty()) {
            return true;
        } else {
            // 再判断部门id
            List<UserDepartmentDTO> yxDept = userDepartmentMapper.isYxDept(userId);
            if (!yxDept.isEmpty()) {
                return true;
            } else {
                // 以下两位领导不进行任何钉钉有关的操作
                // 李栋（市委常委，市长） 账号：lidong；
                // 余青根（市秘书长）账号：yuqinggen；
                // wangxiao 王肖-绵阳市政府办公室
                // xushufeng 胥树锋-绵阳市政府办公室
                UserManager userManager = SpringUtil.getBean(UserManager.class);
                UserDTO userDTO = userManager.getDtoById(userId);
                if (Objects.nonNull(userDTO)) {
                    if ("lidong".equals(userDTO.getAccount()) || "yuqinggen".equals(userDTO.getAccount())
                            || "wangxiao".equals(userDTO.getAccount()) || "xushufeng".equals(userDTO.getAccount())) {
                        return true;
                    }
                }
                return false;
            }

        }
    }

    /**
     * 绵阳定开  批量获取部门用户
     * @param
     * @return
     */
    @Override
    public Map<String, List<UserDepartmentDTO>> getUsersByDepartmentIdsMianYang(Set<String> deptIds) {
        List<UserDepartmentDTO> list = getBaseMapper().selectJoinList(UserDepartmentDTO.class,
                new MPJLambdaWrapper<UserDepartment>().selectAll(UserDepartment.class)
                        .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
                        .selectAs(User::getId, UserDepartmentDTO::getUserId)
                        .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
                        .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
                        .selectAs(User::getState, UserDepartmentDTO::getState)
                        .selectAs(User::getSort, UserDepartmentDTO::getSort)
                        .selectAs(User::getCreateTime, UserDepartmentDTO::getCreateTime)
                        .selectAs(Department::getDepartmentLevelCode, UserDepartmentDTO::getDepartmentLevelCode)
                        .selectAs(Department::getDeptCode, UserDepartmentDTO::getDepartmentCode)
                        .selectAs(Department::getDeptName, UserDepartmentDTO::getDepartmentName)
                        .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                        .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                        .in(CollUtil.isNotEmpty(deptIds),UserDepartment::getDepartmentId, deptIds)
                        .ne(User::getState, UserStateEnum.DELETE)
                        .orderByAsc(User::getSort).orderByDesc(User::getCreateTime));
        return list.stream().collect(Collectors.groupingBy(UserDepartmentDTO::getDepartmentId));
    }

    @Override
    public Paging<UserDepartmentDTO> getUserPageByDeptId(TaskExecutorPageMianYangParam param) {
        PageInfo<UserDepartmentDTO> pageInfo = getBaseMapper().selectJoinPage(new PageInfo<>(param), UserDepartmentDTO.class, new MPJLambdaWrapper<UserDepartment>()
                .selectAll(UserDepartment.class)
                .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
                .selectAs(User::getId, UserDepartmentDTO::getUserId)
                .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
                .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
                .selectAs(User::getState, UserDepartmentDTO::getState)
                .selectAs(Department::getDepartmentLevelCode, UserDepartmentDTO::getDepartmentLevelCode)
                .selectAs(Department::getDeptCode, UserDepartmentDTO::getDepartmentCode)
                .selectAs(Department::getDeptCode, UserDepartmentDTO::getDeptCode)
                .selectAs(Department::getDeptName, UserDepartmentDTO::getDepartmentName)
                .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                .leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                .likeRight(Department::getDeptCode, param.getDeptCode())
                //只查询启用后的用户
                .eq(User::getState, "1")
                //只查询指定部门用户
                .eq(StrUtil.isNotBlank(param.getId()),UserDepartment::getDepartmentId, param.getId())
                //是否只查询管理员
                .eq(param.getIsAdmin(), UserDepartment :: getIsAdmin, param.getIsAdmin())
                .and(StrUtil.isNotBlank(param.getUserName()), e -> {
                    //根据用户账号查询
                    e.like(StrUtil.isNotBlank(param.getUserName()), User::getAccount, param.getUserName()).or()
                            //根据用户姓名进行查询
                            .like(StrUtil.isNotBlank(param.getUserName()), User::getRealname, param.getUserName()).or()
                            //根据用户电话查询
                            .like(StrUtil.isNotBlank(param.getUserName()), User::getPhone, param.getUserName());
                })
                //不查询任务的创建者
                .ne(StrUtil.isNotBlank(param.getTaskCreateBy()), User::getId, param.getTaskCreateBy())
                //不查询任务下派人
                .ne(StrUtil.isNotBlank(param.getMyself()), User::getId, param.getMyself())
                .orderByDesc(User :: getIsAdmin)
                .orderByAsc("length(dept_code)")
                .orderByAsc(Department::getDeptCode)
                .orderByAsc(User::getSort)
                .orderByDesc(User::getCreateTime));
        return MybatisPage.convert(pageInfo, items -> items);
    }

    @Override
    public UserDepartmentDTO getByUserIdAndDeptId(String userId, String deptId) {
        LambdaQueryWrapper<UserDepartment> queryWrapper = queryWrapper();
        queryWrapper.eq(UserDepartment::getUserId, userId)
                .eq(UserDepartment::getDepartmentId, deptId);
        UserDepartment userDepartment = getBaseMapper().selectOne(queryWrapper);
        return UserDepartmentConvert.INSTANCE.toDTO(userDepartment);
    }

    @Override
    public List<UserDepartmentDTO> getUserDeptInfoByRoleId(String roleId) {
        MPJLambdaWrapper<UserDepartment> wrapper = new MPJLambdaWrapper<UserDepartment>()
                .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
                .selectAs(User::getId, UserDepartmentDTO::getId)
                .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
                .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
                .selectAs(User::getCreateTime, UserDepartmentDTO::getCreateTime)
                .selectAs(Department::getDeptName, UserDepartmentDTO::getDepartmentName)
                .selectAs(UserDepartment::getId, UserDepartmentDTO::getDepartmentId)
                .selectAs(Department::getDepartmentLevelCode, UserDepartmentDTO::getDepartmentLevelCode)
                .selectAs(User::getCreateBy, UserDepartmentDTO::getCreateBy);

        wrapper.leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                .eq(UserDepartment::getRoleId, roleId)
                .orderByAsc(User::getSort)
                .orderByDesc(User::getCreateTime);
        return baseMapper.selectJoinList(UserDepartmentDTO.class, wrapper);
    }

    @Override
    public Paging<UserDepartmentDTO> getUserDeptInfoByRoleIdPage(String roleId, Integer current, Integer size, String keyword) {
        MPJLambdaWrapper<UserDepartment> wrapper = new MPJLambdaWrapper<UserDepartment>()
                .selectAs(User::getAccount, UserDepartmentDTO::getAccount)
                .selectAs(User::getId, UserDepartmentDTO::getId)
                .selectAs(UserDepartment::getUserId, UserDepartmentDTO::getUserId)
                .selectAs(User::getRealname, UserDepartmentDTO::getRealname)
                .selectAs(User::getPhone, UserDepartmentDTO::getPhone)
                .selectAs(User::getCreateTime, UserDepartmentDTO::getCreateTime)
                .selectAs(Department::getDeptName, UserDepartmentDTO::getDepartmentName)
                .selectAs(UserDepartment::getDepartmentId, UserDepartmentDTO::getDepartmentId)
                .selectAs(Department::getDepartmentLevelCode, UserDepartmentDTO::getDepartmentLevelCode)
                .selectAs(User::getCreateBy, UserDepartmentDTO::getCreateBy);

        wrapper.leftJoin(Department.class, Department::getId, UserDepartment::getDepartmentId)
                .leftJoin(User.class, User::getId, UserDepartment::getUserId)
                .eq(UserDepartment::getRoleId, roleId)
                .and(StrUtil.isNotBlank(keyword), w -> w
                        .like(User::getRealname, keyword)
                        .or()
                        .like(User::getAccount, keyword)
                        .or()
                        .like(User::getPhone, keyword)
                )
                .orderByAsc(User::getSort)
                .orderByDesc(User::getCreateTime);
        PageInfo<UserDepartmentDTO> pageInfo = baseMapper.selectJoinPage(new PageInfo<>(current, size), UserDepartmentDTO.class, wrapper);
        return MybatisPage.convert(pageInfo, items -> items);
    }


}
