package com.carlos.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.DepartmentConvert;
import com.carlos.org.manager.DepartmentManager;
import com.carlos.org.manager.OrgDockingMappingManager;
import com.carlos.org.mapper.DepartmentMapper;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.entity.Department;
import com.carlos.org.pojo.param.DepartmentPageParam;
import com.carlos.org.pojo.vo.DepartmentVO;
import com.carlos.org.pojo.vo.ThirdDepartmentVO;
import com.carlos.redis.ICacheManager;
import com.carlos.redis.util.RedisUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DepartmentManagerImpl extends BaseServiceImpl<DepartmentMapper, Department> implements DepartmentManager, ICacheManager<DepartmentDTO> {

    private final DepartmentMapper departmentMapper;

    private final OrgDockingMappingManager orgDockingMappingManager;

    @Override
    public boolean addOrUpdate(DepartmentDTO dto) {
        Department entity = DepartmentConvert.INSTANCE.toDO(dto);
        boolean success = saveOrUpdate(entity);
        if (!success) {
            log.warn("Insert 'Department' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'Department' data: id:{}", entity.getId());
        }
        if (ObjectUtil.isNull(dto.getSort())) {
            dto.setSort(0);
        }
        dto.setState(entity.getState());
        dto.setCreateBy(entity.getCreateBy());
        dto.setCreateTime(entity.getCreateTime());
        dto.setVersion(entity.getVersion());
        putCache(dto);
        return true;
    }


    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        Department department = getById(id);
        if (department == null) {
            log.warn("department {} not exist", id);
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'Department' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'Department' data by id:{}", id);
        }
        deleteCache(DepartmentConvert.INSTANCE.toDTO(department));
        return true;
    }

    @Override
    public boolean deleteByIds(Set<Serializable> ids) {
        for (Serializable id : ids) {
            delete(id);
        }
        return true;
    }

    @Override
    public boolean modify(DepartmentDTO dto) {
        Department entity = DepartmentConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'Department' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'Department' data by id:{}", dto.getId());
        }
        updateCache(dto);
        return true;
    }

    @Override
    public DepartmentDTO getDtoById(String id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        DepartmentDTO department = getCacheByDepartmentId(id);
        if (department != null) {
            return department;
        }
        Department entity = getBaseMapper().selectById(id);
        return DepartmentConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<DepartmentVO> getPage(DepartmentPageParam param) {
        LambdaQueryWrapper<Department> wrapper = queryWrapper();
        wrapper.select(
                Department::getId,
                Department::getDeptName,
                Department::getDeptCode,
                Department::getRegionCode,
                Department::getAddress,
                Department::getTel,
                Department::getParentId,
                Department::getLevel,
                Department::getState,
                Department::getSort,
                Department::getDescription,
                Department::getCreateBy,
                Department::getCreateTime,
                Department::getUpdateBy,
                Department::getUpdateTime
        ).orderByAsc(Department::getSort).orderByDesc(Department::getCreateTime);
        PageInfo<Department> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, DepartmentConvert.INSTANCE::toVO);
    }

    @Override
    public Paging<ThirdDepartmentVO> getThirdPage(DepartmentPageParam param, Set<String> ids) {
        LambdaQueryWrapper<Department> wrapper = queryWrapper();
        wrapper.select(
                        Department::getId,
                        Department::getDeptName,
                        Department::getDeptCode,
                        Department::getRegionCode,
                        Department::getAddress,
                        Department::getDepartmentType,
                        Department::getDepartmentLevelCode,
                        Department::getParentId,
                        Department::getLevel,
                        Department::getSort,
                        Department::getDescription
                ).in(Department::getId, ids)
                .and(StrUtil.isNotBlank(param.getKeyword()), w -> w
                        .like(Department::getDeptName, param.getKeyword())
                        .or()
                        .like(Department::getDeptCode, param.getKeyword())
                ).orderByAsc(Department::getSort).orderByDesc(Department::getCreateTime);
        PageInfo<Department> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, DepartmentConvert.INSTANCE::toThirdDeptVO);
    }

    @Override
    public DepartmentDTO getDepartmentByName(String parentId, String deptName) {
        Department department = baseMapper.selectOne(queryWrapper()
                .eq(Department::getParentId, StrUtil.isBlank(parentId) ? 0 : parentId)
                .eq(Department::getDeptName, deptName)
        );

        return DepartmentConvert.INSTANCE.toDTO(department);
    }

    @Override
    public List<DepartmentVO> getDepartments(List<String> deptIds) {
        List<Department> departments = baseMapper.selectList(queryWrapper().in(Department::getId, deptIds).eq(Department::getDeleted, false));
        return DepartmentConvert.INSTANCE.toVO(departments);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByParentId(String parentId) {
        List<DepartmentDTO> depts = getCacheByParentId(parentId);
        if (CollUtil.isNotEmpty(depts)) {
            return depts;
        }

        LambdaQueryWrapper<Department> queryWrapper = queryWrapper();
        queryWrapper.eq(Department::getParentId, parentId == null ? "0" : parentId)
                .orderByAsc(Department::getSort).orderByDesc(Department::getCreateTime);
        List<Department> list = list(queryWrapper);
        return DepartmentConvert.INSTANCE.toDTO(list);
    }

    @Override
    public void listAllDepartment(Serializable id, Set<Serializable> ids) {
        List<Department> list = lambdaQuery()
                .select(Department::getId)
                .eq(Department::getParentId, id)
                .list();
        for (Department department : list) {
            ids.add(department.getId());
            listAllDepartment(department.getId(), ids);
        }
    }

    @Override
    public DepartmentDTO getDepartmentByCode(String deptCode) {
        DepartmentDTO department = getCacheByCode(deptCode);
        if (department == null) {
            Department department2 = baseMapper.selectOne(queryWrapper()
                    .eq(Department::getDeptCode, deptCode)
            );
            department = DepartmentConvert.INSTANCE.toDTO(department2);
            if (department2 != null) {
                putCache(department);
            }
        }
        return department;
    }

    @Override
    public List<DepartmentDTO> getByIds(Set<String> ids) {
        return DepartmentConvert.INSTANCE.toDTO(listByIds(ids));
    }

    @Override
    public List<DepartmentDTO> listAll() {
        List<DepartmentDTO> depts;
        // 从缓存中获取所有部门,没有就重新设置
        Set<String> keys = RedisUtil.scanKeys(generateKey(CACHE_ID, RedisUtil.ALL), 300);
        // 判断key是否为空
        Map<String, DepartmentDTO> stringDepartmentDTOMap = new HashMap<>();
        if (CollUtil.isNotEmpty(keys)) {
//            stringDepartmentDTOMap = RedisUtil.hashMultiGetAll(new ArrayList<>(keys), DepartmentDTO.class);
            List<String> fields = new ArrayList<>();
            // 获取特定字段名称
            Field[] allFields = ReflectUtil.getFields(DepartmentDTO.class);
            for (Field field : allFields) {
                //  根据字段名过滤
                if (Arrays.asList("id", "deptName", "deptCode", "regionCode", "address", "tel", "parentId", "level", "state", "sort", "departmentType", "departmentLevelCode").contains(field.getName())) {
                    fields.add(field.getName());
                }
            }
            stringDepartmentDTOMap = RedisUtil.hashMultiGet(new ArrayList<>(keys), fields, DepartmentDTO.class);
        }
        // 判断stringDepartmentDTOMap是否为空
        if (CollUtil.isNotEmpty(stringDepartmentDTOMap)) {
            depts = new ArrayList<>(stringDepartmentDTOMap.values());
//            stringDepartmentDTOMap.forEach((key, value) -> depts.add(value));
        } else {
            // 使用分页处理大量数据，避免一次性加载过多数据到内存
            int pageSize = 1000;
            long currentPage = 1;
            List<Department> list;
            List<DepartmentDTO> dtos = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            log.info("开始初始化DepartmentListAll缓存");

            do {
                IPage<Department> page = new Page<>(currentPage, pageSize);
                IPage<Department> pageResult = this.page(page, new LambdaQueryWrapper<Department>()
                        .orderByAsc(Department::getSort)
                        .orderByDesc(Department::getCreateTime));

                list = pageResult.getRecords();

                for (Department dept : list) {
                    DepartmentDTO dto = DepartmentConvert.INSTANCE.toDTO(dept);
                    dtos.add(dto);
                    // 写入Hash缓存，主要用于读取
                    RedisUtil.putHash(generateKey(CACHE_ID, dept.getId()), dto);
                    RedisUtil.putHash(generateKey(CACHE_CODE, dept.getDeptCode()), dto);
                }
                currentPage++;
            } while (CollUtil.isNotEmpty(list));
            log.info("DepartmentListAll缓存初始化完成，共处理{}条记录，耗时:{}", dtos.size(), DateUtil.formatBetween(DateUtil.spendMs(startTime)));
            return dtos;
        }
        return depts;
    }

    @Override
    public List<DepartmentDTO> getAllDepartment() {
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<Department>()
                .orderByAsc(Department::getSort)
                .orderByDesc(Department::getCreateTime);
        return DepartmentConvert.INSTANCE.toDTO(list(queryWrapper));
    }

    @Override
    public boolean addBatch(List<DepartmentDTO> departments) {
        List<Department> depts = DepartmentConvert.INSTANCE.toDOS(departments);
        boolean success = saveBatch(depts);
        if (!success) {
            log.warn("save batch 'Department' data fail, entitys:{}", depts);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("save batch  'Department' data by ");
        }
        departments = DepartmentConvert.INSTANCE.toDTO(depts);
        for (DepartmentDTO department : departments) {
            putCache(department);
        }
        return true;
    }

    @Override
    public String getDepartmentName(String id) {
        DepartmentDTO department = getDtoById(id);
        if (department == null) {
            return null;
        }
        return department.getDeptName();
    }

    @Override
    public List<DepartmentDTO> getParentDepartment(String id, Integer limit) {
        if (limit == null) {
            limit = 0;
        }
        LinkedList<DepartmentDTO> list = new LinkedList<>();
        DepartmentDTO department = getDtoById(id);
        if (department == null) {
            throw new ServiceException("部门不存在");
        }
        list.addFirst(department);
        String parentId = department.getParentId();
        for (int i = 1; i <= limit; i++) {
            if (parentId.equals("0")) {
                break;
            }
            DepartmentDTO parent = getDtoById(parentId);
            // 当父级为空自动停止
            if (parent == null) {
                break;
            }
            parentId = parent.getParentId();
            list.addFirst(parent);
        }
        return list;
    }


    @Override
    public Map<String, List<DepartmentDTO>> getParentDepartmentMap(Set<String> ids, Integer limit) {
        if (limit == null) {
            limit = 0;
        }

        Map<String, List<DepartmentDTO>> resultMap = new HashMap<>();

        // 批量获取所有部门信息
        List<DepartmentDTO> departments = getByIds(ids);
        Map<String, DepartmentDTO> departmentMap = departments.stream()
                .collect(Collectors.toMap(DepartmentDTO::getId, Function.identity()));

        for (String id : ids) {
            LinkedList<DepartmentDTO> nameList = new LinkedList<>();
            DepartmentDTO department = departmentMap.get(id);

            if (department == null) {
                resultMap.put(id, Collections.emptyList());
                continue;
            }

            nameList.addFirst(department);
            String parentId = department.getParentId();
            int count = 0;

            while (count < limit && !parentId.equals("0")) {
                DepartmentDTO parent = departmentMap.get(parentId);
                // 如果在已获取的部门中找不到，则单独查询
                if (parent == null) {
                    parent = getDtoById(parentId);
                    if (parent == null) {
                        break;
                    }
                    departmentMap.put(parentId, parent);
                }

                nameList.addFirst(parent);
                parentId = parent.getParentId();
                count++;
            }

            resultMap.put(id, new ArrayList<>(nameList));
        }

        return resultMap;
    }

    @Override
    public List<DepartmentDTO> getParentDepartmentByCode(String code, Integer limit) {
        if (limit == null) {
            limit = 0;
        }
        LinkedList<DepartmentDTO> list = new LinkedList<>();
        DepartmentDTO department = getDepartmentByCode(code);
        if (department == null) {
            throw new ServiceException("部门不存在");
        }
        list.addFirst(department);
        String parentId = department.getParentId();
        for (int i = 1; i <= limit; i++) {
            if (parentId.equals("0")) {
                break;
            }
            DepartmentDTO parent = getDtoById(parentId);
            // 当父级为空自动停止
            if (parent == null) {
                break;
            }
            parentId = parent.getParentId();
            list.addFirst(parent);
        }
        return list;
    }

    public static final String CACHE = "user:department:%s:%s";
    public static final String CACHE_ID = "id";
    public static final String CACHE_CODE = "code";

    @Override
    public void initCache() {
        RedisUtil.deletePattern(generateKey(RedisUtil.ALL, RedisUtil.ALL));
        long startTime = System.currentTimeMillis();
        log.info("开始初始化Department缓存");
        try {
            // 使用分页处理大量数据，避免一次性加载过多数据到内存，也避免同时写入redis，造成redis阻塞
            int pageSize = 1000;
            long currentPage = 1;
            int totalProcessed = 0;
            List<Department> list;

            do {
                IPage<Department> page = new Page<>(currentPage, pageSize);
                IPage<Department> pageResult = this.page(page, new LambdaQueryWrapper<Department>()
                        .orderByAsc(Department::getSort)
                        .orderByDesc(Department::getCreateTime));

                list = pageResult.getRecords();

                for (Department dept : list) {
                    DepartmentDTO dto = DepartmentConvert.INSTANCE.toDTO(dept);
                    // 写入Hash缓存，主要用于读取
                    RedisUtil.putHash(generateKey(CACHE_ID, dept.getId()), dto);
                    RedisUtil.putHash(generateKey(CACHE_CODE, dept.getDeptCode()), dto);
                    totalProcessed += list.size();
                }
                currentPage++;
            } while (CollUtil.isNotEmpty(list));
            log.info("Department缓存初始化完成，共处理{}条记录，耗时:{}", totalProcessed, DateUtil.formatBetween(DateUtil.spendMs(startTime)));
        } catch (Exception e) {
            log.error("Department缓存初始化过程中发生异常", e);
            throw e;
        }
    }


    @Override
    public List<DepartmentDTO> listByLevel(int level) {
        return DepartmentConvert.INSTANCE.toDTO(lambdaQuery().eq(Department::getLevel, level).list());
    }

    @Override
    public List<DepartmentDTO> getCurrentGridByDeptCode(Set<String> deptCodes) {
        List<Department> list = this.lambdaQuery().in(Department::getDeptCode, deptCodes).and(wrapper -> {
            wrapper.eq(Department::getLevel, 4).or().eq(Department::getLevel, 5);
        }).list();
        return DepartmentConvert.INSTANCE.toDTO(list);
    }

    @Override
    public Set<String> getCurrentAndAllSubDepartmentId(String deptCode) {
        if (StrUtil.isEmpty(deptCode)) {
            return Sets.newHashSet();
        }
        return departmentMapper.getAllSubDeptIdsByDeptCode(deptCode);
    }

    @Override
    public Long getSubCount(String parentId) {
        if (StrUtil.isBlank(parentId)) {
            return 0L;
        }
        Long count = lambdaQuery().eq(Department::getParentId, parentId).count();
        return count;
    }

    @Override
    public Long getDeptCodeCountWithDeleted(Integer level, String parentCode) {
        return departmentMapper.getDeptCodeCountWithDeleted(level, parentCode);
    }

    @Override
    public List<UserDepartmentDTO> getCurrentAndChildrenDepartmentUserIds(String id) {
        if (StrUtil.isBlank(id)) {
            return Collections.emptyList();
        }
        return departmentMapper.getCurrentAndChildrenDepartmentUserIds(id);
    }

    @Override
    public void putCache(DepartmentDTO bean) {
        // 写入Hash缓存，主要用于读取
        String idKey = generateKey(CACHE_ID, bean.getId());
        String codeKey = generateKey(CACHE_CODE, bean.getDeptCode());
        RedisUtil.putHash(idKey, bean);
        RedisUtil.putHash(codeKey, bean);
        log.info("Add department cache{}", idKey);
        log.info("Add department cache{}", codeKey);
    }

    @Override
    public void updateCache(DepartmentDTO bean) {
        putCache(bean);
    }

    @Override
    public DepartmentDTO getCache(Serializable... keys) {
        return RedisUtil.getHash(generateKey(keys), DepartmentDTO.class);
    }

    public DepartmentDTO getCacheByDepartmentId(Serializable departmentId) {
        DepartmentDTO dept = RedisUtil.getHash(generateKey(CACHE_ID, departmentId), DepartmentDTO.class);
        if (ObjectUtil.isEmpty(dept)) {
            return null;
        }
        return dept;
    }

    /**
     * 获取直接子集
     *
     * @param parentId 参数0
     * @return java.util.List<com.carlos.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2023/2/26 10:53
     */
    @Override
    public List<DepartmentDTO> getCacheByParentId(Serializable parentId) {
        List<Department> departments = lambdaQuery()
                .eq(Department::getParentId, parentId)
                .orderByAsc(Department::getSort)
                .orderByDesc(Department::getCreateTime)
                .list();
        if (CollUtil.isEmpty(departments)) {
            return null;
        }
        return DepartmentConvert.INSTANCE.toDTO(departments);
    }

    @Override
    public int getSubCountParentId(Serializable parentId) {
        Long count = lambdaQuery()
                .eq(Department::getParentId, parentId)
                .count();
        return Math.toIntExact(count);
    }

    /**
     * 获取直接子集
     *
     * @param departmentCode 参数0
     * @return java.util.List<com.carlos.org.dto.user.DepartmentDTO>
     * @author Carlos
     * @date 2023/2/26 10:53
     */
    public DepartmentDTO getCacheByCode(String departmentCode) {
        DepartmentDTO department = RedisUtil.getHash(generateKey(CACHE_CODE, departmentCode), DepartmentDTO.class);
        if (ObjectUtil.isEmpty(department)) {
            return null;
        }
        return department;
    }

    @Override
    public void deleteCache(DepartmentDTO bean) {
//        String key = generateKey(bean.getId(), bean.getParentId(), bean.getDeptCode());
        String idKey = generateKey(CACHE_ID, bean.getId());
        String codeKey = generateKey(CACHE_CODE, bean.getDeptCode());
        RedisUtil.delete(idKey);
        RedisUtil.delete(codeKey);
        log.info("Delete department cache{}", idKey);
        log.info("Delete department cache{}", codeKey);
    }

    @Override
    public String generateKey(Serializable... keys) {
        return String.format(CACHE, keys);
    }

    /**
     * 递归获取部门id
     *
     * @param departmentId 当前部门id
     * @param parentMap    所有部门集合
     * @author Carlos
     * @date 2023/1/31 13:42
     */
    public void getDepartmentRecursion(Serializable departmentId, Map<String, List<DepartmentDTO>> parentMap, Set<DepartmentDTO> departmentDTOS) {
        List<DepartmentDTO> subs = parentMap.get(departmentId);
        if (CollectionUtil.isEmpty(subs)) {
            return;
        }
        for (DepartmentDTO sub : subs) {
            departmentDTOS.add(sub);
            this.getDepartmentRecursion(sub.getId(), parentMap, departmentDTOS);
        }
    }

    @Override
    public Set<DepartmentDTO> getAllSubDeptById(String departmentId) {
        List<DepartmentDTO> all = listAll();
        Map<String, List<DepartmentDTO>> parentMap = all.stream().collect(Collectors.groupingBy(DepartmentDTO::getParentId));
        Set<DepartmentDTO> dtos = Sets.newHashSet();
        this.getDepartmentRecursion(departmentId, parentMap, dtos);
        return dtos;
    }

    @Override
    public Set<DepartmentDTO> getAllSubDeptByIds(Set<String> departmentIds) {
        if (CollectionUtil.isEmpty(departmentIds)) {
            return Collections.emptySet();
        }

        // 先获取这些 ID 对应的部门对象
        List<DepartmentDTO> rootDepts = getByIds(departmentIds);

        // 提取deptCode
        Set<String> rootDeptCodes = rootDepts.stream()
                .map(DepartmentDTO::getDeptCode)
                .collect(Collectors.toSet());

        // 查询所有子部门（包含自身）
        List<Department> allSubDepts = departmentMapper.selectAllSubDepartmentsByCodes(rootDeptCodes);

        return allSubDepts.stream()
                .map(DepartmentConvert.INSTANCE::toDTO)
                .collect(Collectors.toSet());

    }

    @Override
    public List<DepartmentDTO> getAllSubDeptByDeptCode(String deptCode) {
        LambdaQueryWrapper<Department> like = queryWrapper().likeRight(Department::getDeptCode, deptCode);
        List<Department> list = list(like);
        return DepartmentConvert.INSTANCE.toDTO(list);
    }

    @Override
    public DepartmentDTO getDepartmentByRegionCode(String regionCode) {
        Department department2 = baseMapper.selectOne(queryWrapper()
                .eq(Department::getRegionCode, regionCode)
        );
        return DepartmentConvert.INSTANCE.toDTO(department2);
    }

    @Override
    public List<DepartmentDTO> listAllByName(String name) {
        LambdaQueryWrapper<Department> like = queryWrapper().like(CharSequenceUtil.isNotBlank(name), Department::getDeptName, name);
        List<Department> list = list(like);
        List<DepartmentDTO> dtos = DepartmentConvert.INSTANCE.toDTO(list);
        return dtos;
    }

    @Override
    public List<DepartmentDTO> getSubDepartmentByTypeLike(String deptTypeListStr) {
        if (StrUtil.isEmpty(deptTypeListStr)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Department> like = queryWrapper().likeLeft(Department::getDepartmentType, deptTypeListStr);
        return DepartmentConvert.INSTANCE.toDTO(list(like));
    }

    @Override
    public List<DepartmentDTO> listByLevels(List<Integer> level) {
        return DepartmentConvert.INSTANCE.toDTO(lambdaQuery().in(Department::getLevel, level).list());
    }


    @Override
    public String add(DepartmentDTO dto) {
        Department entity = DepartmentConvert.INSTANCE.toDO(dto);
        boolean success = saveOrUpdate(entity);
        if (!success) {
            log.warn("Insert 'Department' data fail, entity:{}", entity);
            return "";
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'Department' data: id:{}", entity.getId());
        }
        dto.setSort(0);
        dto.setState(entity.getState());
        dto.setCreateBy(entity.getCreateBy());
        dto.setCreateTime(entity.getCreateTime());
        dto.setVersion(entity.getVersion());
        putCache(dto);
        return entity.getId();
    }

    @Override
    public List<String> getAllParentDeptCodeByRecursive(String deptCode) {
        return departmentMapper.getAllParentDeptCodeByRecursive(deptCode);
    }

    @Override
    public List<DepartmentDTO> listDepartmentByRegionCode(String regionCode) {
        if (StrUtil.isBlank(regionCode)) {
            return Collections.emptyList();
        }
        List<Department> list = lambdaQuery()
                .select(Department::getId,
                        Department::getDeptName,
                        Department::getDeptCode,
                        Department::getParentId,
                        Department::getSort,
                        Department::getDepartmentType,
                        Department::getDepartmentLevelCode)
                .eq(Department::getRegionCode, regionCode).list();
        return DepartmentConvert.INSTANCE.toDTO(list);

    }

    @Override
    public List<DepartmentDTO> getAllSubDepartment(String deptCode) {
        if (StrUtil.isBlank(deptCode)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<Department> queryWrapper = queryWrapper()
                .likeRight(Department::getDeptCode, deptCode);
        List<Department> list = list(queryWrapper);

        return DepartmentConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByParentId(String parentId, String regionCode, Integer level) {
        LambdaQueryWrapper<Department> queryWrapper = queryWrapper();
        queryWrapper.eq(Department::getParentId, parentId == null ? "0" : parentId).eq(Department::getRegionCode, regionCode)
                .eq(level != null, Department::getLevel, level)
                .orderByAsc(Department::getSort).orderByDesc(Department::getCreateTime);
        List<Department> list = list(queryWrapper);
        return DepartmentConvert.INSTANCE.toDTO(list);
    }

    //--------------------------------新处理缓存内容，不用可以删掉---------------------------------
    private static final String HASH_CACHE = "user:department:hash:%s";
    private static final String CHILDREN_CACHE = "user:department:children:%s";
    private static final String PARENT_CACHE = "user:department:parent:%s";


    public String generateHashKey(String cache, Serializable... keys) {
        return String.format(cache, keys);
    }

    @Override
    public void initCacheHash() {
        // 删除所有
        RedisUtil.deletePattern(generateKey(RedisUtil.ALL, RedisUtil.ALL));
        List<DepartmentDTO> list = listAll();
        for (DepartmentDTO departmentDTO : list) {
            RedisUtil.putHash(generateKey(CACHE_ID, departmentDTO.getId()), departmentDTO);
            RedisUtil.putHash(generateKey(CACHE_CODE, departmentDTO.getDeptCode()), departmentDTO);
        }
    }

    @Override
    public void initCacheParentList() {
        RedisUtil.deletePattern(generateHashKey(PARENT_CACHE, RedisUtil.ALL));
        List<DepartmentDTO> list = listAll();

        // 创建部门ID到部门对象的映射，方便快速查找
        Map<String, DepartmentDTO> departmentMap = list.stream()
                .collect(Collectors.toMap(DepartmentDTO::getId, department -> department));

        // 为每个部门计算所有父级ID列表
        for (DepartmentDTO department : list) {
            List<String> parentIds = getAllParentIds(department.getId(), departmentMap);
            if (parentIds.isEmpty()) {
                continue;
            }
            String key = generateHashKey(PARENT_CACHE, department.getId());
            RedisUtil.pushList(key, new ArrayList<>(parentIds));
        }
    }

    @Override
    public void initCacheChildrenSet() {
        RedisUtil.deletePattern(generateHashKey(CHILDREN_CACHE, RedisUtil.ALL));
        List<DepartmentDTO> list = listAll();
        // 按父ID分组
        Map<String, List<DepartmentDTO>> parentGroup = list.stream()
                .collect(Collectors.groupingBy(DepartmentDTO::getParentId));

        // 递归计算每个部门的所有子部门并存入Redis
        for (DepartmentDTO dept : list) {
            Set<String> allChildren = getAllChildrenIds(dept.getId(), parentGroup);
            if (allChildren.isEmpty()) {
                continue;
            }
            RedisUtil.addSet(generateHashKey(CHILDREN_CACHE, dept.getId()), allChildren);
        }
    }


    @Override
    public Map<String, List<DepartmentDTO>> getParentMapByCodes(Set<String> deptCodes, Integer limit) {
        if (deptCodes == null || deptCodes.isEmpty()) {
            return new HashMap<>();
        }

        if (limit == null) {
            limit = 0;
        }

        Map<String, List<DepartmentDTO>> resultMap = new HashMap<>();

        // 批量获取所有部门信息
        List<DepartmentDTO> departments = this.getByCodes(deptCodes);

        // 建立ID和部门的映射关系
        Map<String, DepartmentDTO> departmentIdMap = departments.stream()
                .collect(Collectors.toMap(DepartmentDTO::getId, Function.identity()));

        // 建立编码和部门的映射关系
        Map<String, DepartmentDTO> departmentCodeMap = departments.stream()
                .collect(Collectors.toMap(DepartmentDTO::getDeptCode, Function.identity()));

        final String ROOT_PARENT_ID = "0";

        for (String code : deptCodes) {
            LinkedList<DepartmentDTO> nameList = new LinkedList<>();
            DepartmentDTO department = departmentCodeMap.get(code);

            if (department == null) {
                resultMap.put(code, Collections.emptyList());
                continue;
            }

            nameList.addFirst(department);
            String parentId = department.getParentId();
            int count = 0;

            while (count < limit && !ROOT_PARENT_ID.equals(parentId)) {
                DepartmentDTO parent = departmentIdMap.get(parentId);
                // 如果在已获取的部门中找不到，则单独查询
                if (parent == null) {
                    parent = getDtoById(parentId);
                    if (parent == null) {
                        break;
                    }
                    departmentIdMap.put(parentId, parent);
                }

                nameList.addFirst(parent);
                parentId = parent.getParentId();
                count++;
            }

            resultMap.put(code, new ArrayList<>(nameList));
        }

        return resultMap;
    }

    @Override
    public Set<String> listLevelCodeByTopParentDeptCode(String deptCode) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(Department::getDeptCode, deptCode)
                .select(Department::getDepartmentLevelCode);
        return this.list(wrapper).stream()
                .map(Department::getDepartmentLevelCode)
                .collect(Collectors.toSet());
    }

    private List<DepartmentDTO> getByCodes(Set<String> deptCodes) {
        if (deptCodes == null || deptCodes.isEmpty()) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<Department> queryWrapper = queryWrapper()
                .in(Department::getDeptCode, deptCodes);

        List<Department> departments = list(queryWrapper);
        return DepartmentConvert.INSTANCE.toDTO(departments);
    }

    /**
     * 递归获取所有子部门ID
     *
     * @param parentId    父部门ID
     * @param parentGroup 按父ID分组的部门映射
     * @return 所有子部门ID集合
     */
    private Set<String> getAllChildrenIds(String parentId, Map<String, List<DepartmentDTO>> parentGroup) {
        Set<String> result = new HashSet<>();
        List<DepartmentDTO> directChildren = parentGroup.get(parentId);
        if (directChildren != null && !directChildren.isEmpty()) {
            for (DepartmentDTO child : directChildren) {
                result.add(child.getId());
                // 递归获取孙部门及以下
                result.addAll(getAllChildrenIds(child.getId(), parentGroup));
            }
        }
        return result;
    }

    /**
     * 递归获取所有父级部门ID
     *
     * @param departmentId  部门ID
     * @param departmentMap 部门映射
     * @return 所有父级部门ID列表
     */
    private List<String> getAllParentIds(String departmentId, Map<String, DepartmentDTO> departmentMap) {
        List<String> parentIds = new ArrayList<>();
        DepartmentDTO department = departmentMap.get(departmentId);

        if (department != null && !"0".equals(department.getParentId())) {
            String parentId = department.getParentId();
            parentIds.add(parentId);

            // 递归获取更上级的部门
            parentIds.addAll(getAllParentIds(parentId, departmentMap));
        }

        return parentIds;
    }

    @Override
    public List<DepartmentDTO> getDepartmentsWithChildrenByParentId(String parentId, String regionCode, Integer level) {
        // 先获取符合条件的所有部门
        List<DepartmentDTO> departments = this.getDepartmentsByParentId(parentId, regionCode, level);
        
        // 如果没有部门数据，直接返回空列表
        if (CollUtil.isEmpty(departments)) {
            return Collections.emptyList();
        }
        
        // 获取这些部门的所有子部门（一次性查询，避免N+1问题）
        Set<String> parentIds = departments.stream()
                .map(DepartmentDTO::getId)
                .collect(Collectors.toSet());
        
        // 递归获取所有子部门
        List<DepartmentDTO> allSubDepartments = getAllSubDepartments(parentIds, regionCode);
        
        // 构建ID到部门的映射，方便快速查找
        Map<String, DepartmentDTO> departmentMap = new HashMap<>();
        // 先放入顶级部门
        for (DepartmentDTO department : departments) {
            departmentMap.put(department.getId(), department);
        }
        // 再放入所有子部门
        for (DepartmentDTO department : allSubDepartments) {
            departmentMap.put(department.getId(), department);
        }
        
        // 构建树形结构
        buildDepartmentTreeFromMap(departments, departmentMap);
        
        return departments;
    }


    @Override
    public List<String> getAllParentDepartmentIds(String deptCode) {
        // 根据部门编码获取部门信息
        DepartmentDTO department = this.getDepartmentByCode(deptCode);
        if (department == null) {
            return Lists.newArrayList();
        }
        return getParentIds(department);
    }


    @Override
    public List<String> getAllParentDepartmentIdsById(String deptId) {
        // 根据部门编码获取部门信息
        DepartmentDTO department = this.getDtoById(deptId);
        if (department == null) {
            return Lists.newArrayList();
        }
        return getParentIds(department);
    }

    /**
     * 递归获取所有父部门ID
     *
     * @param department 部门信息
     * @return 所有父部门ID列表
     */
    private List<String> getParentIds(DepartmentDTO department) {
        // 用于存储所有父部门ID的列表
        List<String> parentIds = Lists.newArrayList();

        // 递归获取所有父部门
        String currentParentId = department.getParentId();
        while (StrUtil.isNotBlank(currentParentId) && !StrUtil.equals(currentParentId, "0")) {
            DepartmentDTO parentDepartment = this.getDtoById(currentParentId);
            if (parentDepartment == null) {
                break;
            }

            // 添加到列表开头，以保证从最顶层到当前层的顺序
            parentIds.add(0, currentParentId);
            currentParentId = parentDepartment.getParentId();
        }
        // 添加当前部门ID
        parentIds.add(department.getId());
        return parentIds;
    }

    /**
     * 递归获取所有子部门（通过一次数据库查询）
     *
     * @param parentIds 父级部门ID集合
     * @param regionCode 区域编码
     * @return 所有子部门列表
     */
    private List<DepartmentDTO> getAllSubDepartments(Set<String> parentIds, String regionCode) {
        if (CollUtil.isEmpty(parentIds)) {
            return Collections.emptyList();
        }
        
        // 用于收集所有子部门
        List<DepartmentDTO> result = new ArrayList<>();
        
        // 逐层查询所有子部门，避免递归导致的多次方法调用
        Set<String> currentLevelParentIds = new HashSet<>(parentIds);
        
        do {
            // 查询当前层级的子部门
            List<Department> departments = lambdaQuery()
                    .eq(Department::getRegionCode, regionCode)
                    .in(Department::getParentId, currentLevelParentIds)
                    .list();
            
            List<DepartmentDTO> currentLevelDepartments = DepartmentConvert.INSTANCE.toDTO(departments);
            
            if (CollUtil.isNotEmpty(currentLevelDepartments)) {
                // 添加到结果中
                result.addAll(currentLevelDepartments);
                
                // 准备下一轮查询的父ID
                currentLevelParentIds = currentLevelDepartments.stream()
                        .map(DepartmentDTO::getId)
                        .collect(Collectors.toSet());
            } else {
                // 没有更多子部门了，结束循环
                currentLevelParentIds.clear();
            }
        } while (!currentLevelParentIds.isEmpty());
        
        return result;
    }
    
    /**
     * 从映射中构建部门树形结构
     *
     * @param departments 顶级部门列表
     * @param departmentMap ID到部门的映射
     */
    private void buildDepartmentTreeFromMap(List<DepartmentDTO> departments, Map<String, DepartmentDTO> departmentMap) {
        // 按父ID分组所有部门
        Map<String, List<DepartmentDTO>> parentGroup = departmentMap.values().stream()
                .collect(Collectors.groupingBy(DepartmentDTO::getParentId));
        
        // 为每个顶级部门构建子树
        for (DepartmentDTO department : departments) {
            buildSubTree(department, parentGroup);
        }
    }
    
    /**
     * 递归构建子树
     *
     * @param department 当前部门
     * @param parentGroup 按父ID分组的部门映射
     */
    private void buildSubTree(DepartmentDTO department, Map<String, List<DepartmentDTO>> parentGroup) {
        List<DepartmentDTO> children = parentGroup.get(department.getId());
        
        if (CollUtil.isNotEmpty(children)) {
            department.setChildren(children);
            
            // 递归构建每个子部门的子树
            for (DepartmentDTO child : children) {
                buildSubTree(child, parentGroup);
            }
        } else {
            department.setChildren(Collections.emptyList());
        }
    }
}
