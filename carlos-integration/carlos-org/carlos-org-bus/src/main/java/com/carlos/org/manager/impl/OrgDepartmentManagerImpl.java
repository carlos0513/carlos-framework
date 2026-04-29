package com.carlos.org.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.exception.BusinessException;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.OrgDepartmentConvert;
import com.carlos.org.manager.OrgDepartmentManager;
import com.carlos.org.mapper.OrgDepartmentMapper;
import com.carlos.org.pojo.dto.OrgDepartmentDTO;
import com.carlos.org.pojo.dto.OrgDepartmentUserDTO;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.entity.OrgDepartment;
import com.carlos.org.pojo.param.OrgDepartmentPageParam;
import com.carlos.redis.ICacheManager;
import com.carlos.redis.util.RedisUtil;
import com.google.common.collect.Lists;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgDepartmentManagerImpl extends BaseServiceImpl<OrgDepartmentMapper, OrgDepartment>
    implements OrgDepartmentManager, ICacheManager<OrgDepartmentDTO> {

    /** 缓存空间 */
    public static final String PREFIX = "dept:";

    /** 本体 Hash → 参数：部门id */
    public static final String SELF_KEY = PREFIX + "hash:%s";

    /** 部门编码映射 → 参数：deptCode */
    public static final String CODE_KEY = PREFIX + "code:%s";

    /** 直接子列表 → Set；参数：父部门id */
    public static final String CHILDREN_KEY = PREFIX + "children:%s";

    /** 全部子孙列表 → Set；参数：部门id */
    public static final String DESC_KEY = PREFIX + "desc:%s";

    /** 祖先链 / 面包屑 → List；参数：部门id */
    public static final String ANC_KEY = PREFIX + "ancestors:%s";

    @Override
    public boolean add(OrgDepartmentDTO dto) {
        OrgDepartment entity = OrgDepartmentConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgDepartment' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        log.debug("Insert 'OrgDepartment' data: id:{}", entity.getId());
        OrgDepartmentDTO dept = getDtoById(entity.getId());
        if (dept != null) {
            this.putCache(dept);
        }
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        OrgDepartmentDTO dept = this.getDtoById(id);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'OrgDepartment' data fail, id:{}", id);
            return false;
        }
        log.debug("Remove 'OrgDepartment' data by id:{}", id);
        this.deleteCache(dept);
        return true;
    }

    @Override
    public boolean modify(OrgDepartmentDTO dto) {
        // 先获取旧数据，用于判断 parentId 是否变更
        OrgDepartmentDTO oldDept = this.getDtoById(dto.getId());
        OrgDepartment entity = OrgDepartmentConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgDepartment' data fail, entity:{}", entity);
            return false;
        }
        log.debug("Update 'OrgDepartment' data by id:{}", dto.getId());

        // 直接从 DB 读取最新数据，避免缓存未刷新导致读到旧值
        OrgDepartment newEntity = getBaseMapper().selectById(dto.getId());
        if (newEntity == null) {
            return true;
        }
        OrgDepartmentDTO newDept = OrgDepartmentConvert.INSTANCE.toDTO(newEntity);

        // 若发生跨级移动（parentId 变更），先清理旧缓存关系再重建
        if (oldDept != null && dto.getParentId() != null
            && !dto.getParentId().equals(oldDept.getParentId())) {
            this.deleteCache(oldDept);
            this.putCache(newDept);
        } else {
            this.updateCache(newDept);
        }
        return true;
    }

    @Override
    public OrgDepartmentDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        String key = String.format(SELF_KEY, id);
        OrgDepartmentDTO dto = RedisUtil.getHash(key, OrgDepartmentDTO.class);
        if (dto != null) {
            return dto;
        }
        OrgDepartment entity = getBaseMapper().selectById(id);
        if (entity == null) {
            return null;
        }
        dto = OrgDepartmentConvert.INSTANCE.toDTO(entity);
        this.putCache(dto);
        return dto;
    }

    @Override
    public Paging<OrgDepartmentDTO> getPage(OrgDepartmentPageParam param) {
        LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
        if (param.getParentId() != null) {
            wrapper.eq(OrgDepartment::getParentId, param.getParentId());
        }
        wrapper.orderByAsc(OrgDepartment::getSort);
        PageInfo<OrgDepartment> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, entities -> entities.stream()
            .map(OrgDepartmentConvert.INSTANCE::toDTO)
            .collect(Collectors.toList()));
    }

    @Override
    public List<OrgDepartmentDTO> listAll() {
        Set<String> keys = RedisUtil.scanKeys(String.format(SELF_KEY, RedisUtil.ALL), 500);
        if (CollUtil.isNotEmpty(keys)) {
            Map<String, OrgDepartmentDTO> maps = RedisUtil.hashMultiGetAll(
                Lists.newArrayList(keys), 2000, OrgDepartmentDTO.class);
            if (CollUtil.isNotEmpty(maps)) {
                return new ArrayList<>(maps.values());
            }
        }
        LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(OrgDepartment::getSort);
        List<OrgDepartment> list = list(wrapper);
        return list.stream()
            .map(OrgDepartmentConvert.INSTANCE::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public OrgDepartmentDTO getByDeptCode(String deptCode) {
        if (StrUtil.isBlank(deptCode)) {
            return null;
        }
        String codeKey = String.format(CODE_KEY, deptCode);
        String idStr = RedisUtil.getValue(codeKey, String.class);
        if (StrUtil.isNotBlank(idStr)) {
            return getDtoById(Long.valueOf(idStr));
        }
        LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgDepartment::getDeptCode, deptCode);
        wrapper.last("LIMIT 1");
        OrgDepartment entity = getBaseMapper().selectOne(wrapper);
        if (entity == null) {
            return null;
        }
        OrgDepartmentDTO dto = OrgDepartmentConvert.INSTANCE.toDTO(entity);
        this.putCache(dto);
        return dto;
    }

    @Override
    public List<OrgDepartmentDTO> getChildrenByParentId(Serializable parentId) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        String parentKey = String.valueOf(parentId);
        Set<String> childrenIds = RedisUtil.getSet(String.format(CHILDREN_KEY, parentKey));
        if (CollUtil.isNotEmpty(childrenIds)) {
            List<String> hashKeys = childrenIds.stream()
                .map(cid -> String.format(SELF_KEY, cid))
                .collect(Collectors.toList());
            Map<String, OrgDepartmentDTO> maps = RedisUtil.hashMultiGetAll(hashKeys, OrgDepartmentDTO.class);
            if (CollUtil.isNotEmpty(maps)) {
                return new ArrayList<>(maps.values());
            }
        }
        LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgDepartment::getParentId, parentId);
        List<OrgDepartment> list = list(wrapper);
        return list.stream()
            .map(OrgDepartmentConvert.INSTANCE::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<OrgUserDTO> getUsersByDeptId(Serializable deptId) {
        List<OrgDepartmentUserDTO> users = getBaseMapper().getUsersByDeptId(deptId);
        return Collections.emptyList();
    }

    @Override
    public Paging<OrgDepartmentUserDTO> getUsersByDeptId(Serializable deptId, OrgDepartmentPageParam param) {
        List<OrgDepartmentUserDTO> allUsers = getBaseMapper().getUsersByDeptId(deptId);

        int total = allUsers.size();
        int pages = (int) Math.ceil((double) total / param.getSize());
        int fromIndex = (param.getCurrent() - 1) * param.getSize();
        int toIndex = Math.min(fromIndex + param.getSize(), total);

        List<OrgDepartmentUserDTO> records = fromIndex < total
            ? allUsers.subList(fromIndex, toIndex)
            : Collections.emptyList();

        Paging<OrgDepartmentUserDTO> paging = new Paging<>();
        paging.setCurrent(param.getCurrent());
        paging.setSize(param.getSize());
        paging.setTotal(total);
        paging.setPages(pages);
        paging.setRecords(records);
        return paging;
    }

    @Override
    public void initCache() {
        try {
            int pageSize = 5000;
            int current = 1;
            boolean hasMore = true;

            while (hasMore) {
                PageInfo<OrgDepartment> pageInfo = this.page(
                    new PageInfo<>(current, pageSize, false),
                    queryWrapper().select(
                        OrgDepartment::getId,
                        OrgDepartment::getParentId,
                        OrgDepartment::getDeptName,
                        OrgDepartment::getDeptCode,
                        OrgDepartment::getPath,
                        OrgDepartment::getLevel,
                        OrgDepartment::getSort,
                        OrgDepartment::getState,
                        OrgDepartment::getLeaderId,
                        OrgDepartment::getTenantId,
                        OrgDepartment::getDescription,
                        OrgDepartment::getVersion,
                        OrgDepartment::getCreateBy,
                        OrgDepartment::getCreateTime,
                        OrgDepartment::getUpdateBy,
                        OrgDepartment::getUpdateTime
                    )
                );

                List<OrgDepartment> records = pageInfo.getRecords();
                if (CollUtil.isEmpty(records)) {
                    break;
                }

                RedisUtil.executePipelined(new SessionCallback<Object>() {
                    @Override
                    public Object execute(RedisOperations operations) throws DataAccessException {
                        records.forEach(dept -> {
                            OrgDepartmentDTO dto = OrgDepartmentConvert.INSTANCE.toDTO(dept);
                            addCache(operations, dto);
                        });
                        return null;
                    }
                });

                hasMore = records.size() >= pageSize;
                current++;
            }
            log.info("OrgDepartment cache init success with pagination");
        } catch (Exception e) {
            log.error("Failed to init OrgDepartment cache with pagination", e);
            throw new BusinessException("初始化部门缓存失败", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putCache(OrgDepartmentDTO bean) {
        RedisUtil.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(@NotNull RedisOperations ops) throws DataAccessException {
                ops.multi();
                addCache(ops, bean);
                return ops.exec();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void addCache(RedisOperations ops, OrgDepartmentDTO bean) {
        String id = String.valueOf(bean.getId());
        String parentId = bean.getParentId() == null ? null : String.valueOf(bean.getParentId());
        String selfKey = String.format(SELF_KEY, id);
        String codeKey = String.format(CODE_KEY, bean.getDeptCode());

        // 1. 写本体 Hash
        ops.opsForHash().putAll(selfKey, BeanUtil.beanToMap(bean));
        // 2. 写 code -> id 映射
        if (StrUtil.isNotBlank(bean.getDeptCode())) {
            ops.opsForValue().set(codeKey, id);
        }
        // 3. 加入父节点的 children Set
        if (StrUtil.isNotBlank(parentId)) {
            ops.opsForSet().add(String.format(CHILDREN_KEY, parentId), id);
        }
        // 4. 处理祖先链与子孙集合
        List<String> ancestors = parseAncestorIds(bean);
        if (CollUtil.isNotEmpty(ancestors)) {
            String ancKey = String.format(ANC_KEY, id);
            // 所有祖先的 desc 集合加入当前节点
            ancestors.forEach(ancestorId -> {
                ops.opsForSet().add(String.format(DESC_KEY, ancestorId), id);
            });
            // 写自己的祖先链 List
            ops.opsForList().rightPushAll(ancKey, ancestors);
        }
    }

    @Override
    public void updateCache(OrgDepartmentDTO bean) {
        RedisUtil.putHash(String.format(SELF_KEY, bean.getId()), bean);
        // 若 deptCode 变更，同步更新映射
        if (StrUtil.isNotBlank(bean.getDeptCode())) {
            String codeKey = String.format(CODE_KEY, bean.getDeptCode());
            RedisUtil.setValue(codeKey, String.valueOf(bean.getId()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteCache(OrgDepartmentDTO bean) {
        RedisUtil.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(@NotNull RedisOperations ops) throws DataAccessException {
                ops.multi();
                String id = String.valueOf(bean.getId());
                String parentId = bean.getParentId() == null ? null : String.valueOf(bean.getParentId());
                String selfKey = String.format(SELF_KEY, id);
                String codeKey = String.format(CODE_KEY, bean.getDeptCode());
                String descKey = String.format(DESC_KEY, id);
                String ancKey = String.format(ANC_KEY, id);

                // 1. 删除本体
                ops.delete(selfKey);
                // 2. 删除 code 映射
                if (StrUtil.isNotBlank(bean.getDeptCode())) {
                    ops.delete(codeKey);
                }
                // 3. 删除自己的 children 集合
                ops.delete(String.format(CHILDREN_KEY, id));
                // 4. 删除自己的祖先链
                ops.delete(ancKey);
                // 5. 删除自己的子孙集合
                ops.delete(descKey);
                // 6. 从父节点的 children 中移除自己
                if (StrUtil.isNotBlank(parentId)) {
                    ops.opsForSet().remove(String.format(CHILDREN_KEY, parentId), id);
                }
                // 7. 从所有祖先的 desc 集合中移除自己
                List<Long> ancestors = getAncestorIdsFromCache(bean.getId(), 0);
                if (CollUtil.isNotEmpty(ancestors)) {
                    ancestors.forEach(ancestorId -> {
                        ops.opsForSet().remove(String.format(DESC_KEY, String.valueOf(ancestorId)), id);
                    });
                }
                return ops.exec();
            }
        });
    }

    @Override
    public List<Long> getAncestorIdsFromCache(Long id, long limit) {
        if (id == null) {
            return Collections.emptyList();
        }
        if (limit <= 0) {
            limit = Integer.MAX_VALUE;
        }
        List<String> list = RedisUtil.getList(String.format(ANC_KEY, id), -limit, -1L);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream()
            .map(Long::valueOf)
            .collect(Collectors.toList());
    }

    @Override
    public Set<Long> getDescIdsFromCache(Long id) {
        if (id == null) {
            return Collections.emptySet();
        }
        Set<String> set = RedisUtil.getSet(String.format(DESC_KEY, id));
        if (CollUtil.isEmpty(set)) {
            return Collections.emptySet();
        }
        return set.stream()
            .map(Long::valueOf)
            .collect(Collectors.toSet());
    }

    @Override
    public List<OrgDepartmentDTO> listDeptFromCache(List<Long> ids, List<String> fields) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<String> keys = ids.stream()
            .map(id -> String.format(SELF_KEY, id))
            .collect(Collectors.toList());
        Map<String, OrgDepartmentDTO> maps;
        if (CollUtil.isEmpty(fields)) {
            maps = RedisUtil.hashMultiGetAll(keys, OrgDepartmentDTO.class);
        } else {
            maps = RedisUtil.hashMultiGet(keys, fields, OrgDepartmentDTO.class);
        }
        if (CollUtil.isEmpty(maps)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(maps.values());
    }

    @Override
    public long clearCache() {
        long deleteCount = RedisUtil.deleteSpace(PREFIX);
        log.info("OrgDepartment cache has been cleaned, delete count: {}", deleteCount);
        return deleteCount;
    }

    /**
     * 从 path 解析祖先 id 列表（自顶向下，不含自身）
     *
     * @param bean 部门 DTO
     * @return 祖先 id 字符串列表
     */
    private List<String> parseAncestorIds(OrgDepartmentDTO bean) {
        String path = bean.getPath();
        if (StrUtil.isBlank(path)) {
            return Collections.emptyList();
        }
        Long selfId = bean.getId();
        // 按 / 分割并去空
        List<String> parts = StrUtil.splitTrim(path, '/');
        if (CollUtil.isEmpty(parts)) {
            return Collections.emptyList();
        }
        // 过滤掉自身 id（若 path 包含自身）
        return parts.stream()
            .filter(StrUtil::isNotBlank)
            .filter(part -> !part.equals(String.valueOf(selfId)))
            .collect(Collectors.toList());
    }
}
