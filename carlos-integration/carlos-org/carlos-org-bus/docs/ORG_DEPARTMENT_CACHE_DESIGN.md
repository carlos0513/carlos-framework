# OrgDepartment 缓存设计方案

## 1. 设计目标

参照 `SysRegionManagerImpl`（行政区划）的缓存实现，为 `OrgDepartmentManagerImpl`（组织部门）引入分布式 Redis 缓存，解决以下痛点：

- **高频树形查询**：部门架构是典型的多级树形结构，业务频繁查询“某节点的下级/全部子孙/祖先链”。
- **重复数据库访问**：`getChildrenByParentId`、`getByDeptCode`、`listAll` 等接口在并发场景下易成为瓶颈。
- **跨服务调用**：部门数据常被用户、权限等模块通过 Feign 引用，缓存可减少跨服务数据库压力。

## 2. 核心设计原则

1. **Cache-Aside 模式**：数据以 DB 为锚，写操作先写库再同步缓存；读操作先读缓存，未命中再读库并回填。
2. **空间换时间**：利用 Redis 的 Hash、Set、List 多种数据结构，将树形关系以 O(1) ~ O(N) 的时间复杂度直接读出，避免递归 SQL。
3. **批量 + Pipeline**：全量预热与批量写入使用 `RedisPipeline`，降低 RT。
4. **最小侵入**：仅改动 `OrgDepartmentManagerImpl`，不改动 Mapper 与 XML；对外 Service/Controller 无感知。

## 3. 缓存 Key 设计

以 `dept:` 为命名空间（namespace），所有 Key 统一前缀，便于批量清理与扫描。

| Key 模式                     | Redis 类型 | 说明                                                                         | 示例                                        |
|----------------------------|----------|----------------------------------------------------------------------------|-------------------------------------------|
| `dept:hash:{id}`           | Hash     | 部门实体完整字段存储（id、parentId、deptName、deptCode、path、level、sort、state、tenantId 等） | `dept:hash:10001`                         |
| `dept:code:{deptCode}`     | String   | 业务编码 → 主键 id 的映射，加速 `getByDeptCode` 查询                                     | `dept:code:DEV-001` → `"10001"`           |
| `dept:children:{parentId}` | Set      | 某父节点下的**直接子部门** id 集合                                                      | `dept:children:10000` → {10001, 10002}    |
| `dept:desc:{parentId}`     | Set      | 某父节点下的**全部子孙部门** id 集合（含跨层级）                                               | `dept:desc:10000` → {10001, 10002, 10003} |
| `dept:ancestors:{id}`      | List     | 某节点的**祖先链**（自顶向下，如集团 → 事业部 → 部门）                                           | `dept:ancestors:10003` → [10000, 10001]   |

### 3.1 为什么用 Set 存储 children / desc？

- 业务最常问“某节点下一级是谁”，`SMEMBERS` 一次返回，时间复杂度 O(N) 且 N 就是子节点数（通常 < 500）。
- 插入/删除子节点只需 `SADD/SREM`，O(1) 原子完成；若用 List 会留下空洞或需全表重排。
- 批量导入/同步场景下，Set 天然幂等去重。

### 3.2 为什么同时维护 hash 与 code 映射？

- 部门树的所有关系型索引（children、desc、ancestors）均以**数据库主键 `id`**（Long）为锚，保证唯一性与连续性。
- 但业务层存在 `getByDeptCode(String deptCode)` 接口，因此额外维护一条 `dept:code:{deptCode}` → `id` 的 String
  映射，命中后可直接通过 `dept:hash:{id}` 读取实体。

## 4. 数据结构与序列化

- **Hash**：使用 `HSETALL` / `HMGET` 存储 `OrgDepartmentDTO`，内部序列化为 `Map<String, Object>`（利用 Hutool
  `BeanUtil.beanToMap`）。
- **Set / List**：元素均为 `String` 类型的 `id`（`String.valueOf(id)`），避免不同序列化器导致的类型兼容问题。
- **String**：`dept:code` 的值存储 `String` 类型的 `id`。

## 5. 缓存生命周期与维护策略

### 5.1 初始化（initCache）

```java
public void initCache() {
    // 分页参数
    int pageSize = 5000;
    int current = 1;
    boolean hasMore = true;

    while (hasMore) {
        PageInfo<OrgDepartment> pageInfo = this.page(
            new PageInfo<>(current, pageSize, false),
            queryWrapper()
                .select(OrgDepartment::getId, OrgDepartment::getParentId,
                        OrgDepartment::getDeptName, OrgDepartment::getDeptCode,
                        OrgDepartment::getPath, OrgDepartment::getLevel,
                        OrgDepartment::getSort, OrgDepartment::getState,
                        OrgDepartment::getLeaderId, OrgDepartment::getTenantId)
        );

        List<OrgDepartment> records = pageInfo.getRecords();
        if (CollUtil.isEmpty(records)) break;

        RedisUtil.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                records.forEach(dept -> {
                    OrgDepartmentDTO dto = OrgDepartmentConvert.INSTANCE.toDTO(dept);
                    addCache(ops, dto);
                });
                return null;
            }
        });

        hasMore = records.size() >= pageSize;
        current++;
    }
    log.info("OrgDepartment cache init success with pagination");
}
```

- 按 `id` 升序分页，避免深分页性能问题。
- 每页在 Pipeline 内完成所有 Key 的写入，减少网络往返。

### 5.2 新增（add → putCache）

```
1. 插入 DB
2. 通过 getDtoById 查询完整 DTO（含自动生成的 id）
3. putCache(dto)
   ├── HSET  dept:hash:{id}
   ├── SET   dept:code:{deptCode} = {id}
   ├── SADD  dept:children:{parentId}  {id}
   ├── 解析 path 得到 ancestors，对每个 ancestor SADD dept:desc:{ancestorId} {id}
   └── RPUSH dept:ancestors:{id}  [ancestors...]
```

### 5.3 修改（modify → updateCache）

区分两种场景：

| 场景                                                                 | 处理逻辑                                                                                                                  |
|--------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| **基础信息变更**（deptName、leaderId、sort、state、description 等，parentId 不变） | 1. 更新 DB<br>2. `updateCache(dto)`：仅覆盖 `dept:hash:{id}`，其他关系 Key 无需变动。                                                 |
| **跨级调整**（parentId 变更）                                              | 1. 更新 DB<br>2. 先 `deleteCache(oldDto)` 清理旧关系<br>3. 重新查询 `getDtoById` 得到新 path / level<br>4. `putCache(newDto)` 重建全部索引 |

> **一期建议**：与 `SysRegionManagerImpl` 保持一致，先实现“基础信息变更”场景；跨级调整可通过先删后增原子事务实现。

### 5.4 删除（delete → deleteCache）

```
1. 根据 id 查询出 OrgDepartmentDTO（用于获取 parentId、deptCode、path）
2. 逻辑删除 DB（@TableLogic 自动处理）
3. deleteCache(dto)
   ├── DEL  dept:hash:{id}
   ├── DEL  dept:code:{deptCode}
   ├── DEL  dept:children:{id}       （即使业务保证无子节点，也清理残留）
   ├── DEL  dept:desc:{id}
   ├── DEL  dept:ancestors:{id}
   ├── SREM dept:children:{parentId}  {id}
   ├── 解析 path 得到 ancestors，对每个 ancestor SREM dept:desc:{ancestorId} {id}
   └── 返回 exec 结果
```

### 5.5 清空（clearCache）

```java
public long clearCache() {
    long deleteCount = RedisUtil.deleteSpace("dept:");
    log.info("OrgDepartment cache has been cleaned, delete count: {}", deleteCount);
    return deleteCount;
}
```

利用 `SCAN + DEL` 批量删除以 `dept:` 为前缀的所有 Key。

## 6. 读操作缓存改造

### 6.1 getDtoById(Long id)

```java
@Override
public OrgDepartmentDTO getDtoById(Serializable id) {
    if (id == null) {
        log.warn("id is null");
        return null;
    }
    // 1. 读缓存
    OrgDepartmentDTO dto = RedisUtil.getHash(String.format(SELF_KEY, id), OrgDepartmentDTO.class);
    if (dto != null) {
        return dto;
    }
    // 2. 读库
    OrgDepartment entity = getBaseMapper().selectById(id);
    if (entity == null) {
        return null;
    }
    dto = OrgDepartmentConvert.INSTANCE.toDTO(entity);
    // 3. 回填
    this.putCache(dto);
    return dto;
}
```

### 6.2 getByDeptCode(String deptCode)

```java
@Override
public OrgDepartmentDTO getByDeptCode(String deptCode) {
    // 1. 读 code 映射
    String idStr = RedisUtil.getValue(String.format(CODE_KEY, deptCode));
    if (StrUtil.isNotBlank(idStr)) {
        return getDtoById(Long.valueOf(idStr));
    }
    // 2. 读库
    LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(OrgDepartment::getDeptCode, deptCode).last("LIMIT 1");
    OrgDepartment entity = getBaseMapper().selectOne(wrapper);
    if (entity == null) {
        return null;
    }
    OrgDepartmentDTO dto = OrgDepartmentConvert.INSTANCE.toDTO(entity);
    // 3. 回填
    this.putCache(dto);
    return dto;
}
```

### 6.3 getChildrenByParentId(Serializable parentId)

```java
@Override
public List<OrgDepartmentDTO> getChildrenByParentId(Serializable parentId) {
    // 1. 读 children Set
    Set<String> childrenIds = RedisUtil.getSet(String.format(CHILDREN_KEY, parentId));
    if (CollUtil.isNotEmpty(childrenIds)) {
        List<String> hashKeys = childrenIds.stream()
            .map(cid -> String.format(SELF_KEY, cid))
            .collect(Collectors.toList());
        Map<String, OrgDepartmentDTO> maps = RedisUtil.hashMultiGetAll(hashKeys, OrgDepartmentDTO.class);
        if (CollUtil.isNotEmpty(maps)) {
            return new ArrayList<>(maps.values());
        }
    }
    // 2. 读库
    LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(OrgDepartment::getParentId, parentId);
    List<OrgDepartment> list = list(wrapper);
    return list.stream()
        .map(OrgDepartmentConvert.INSTANCE::toDTO)
        .collect(Collectors.toList());
}
```

### 6.4 listAll()

```java
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
    // 兜底读库
    LambdaQueryWrapper<OrgDepartment> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByAsc(OrgDepartment::getSort);
    List<OrgDepartment> list = list(wrapper);
    return list.stream()
        .map(OrgDepartmentConvert.INSTANCE::toDTO)
        .collect(Collectors.toList());
}
```

### 6.5 新增接口：祖先链与子孙集查询

参考 `SysRegionManager`，在 `OrgDepartmentManager` 接口中新增以下方法，供上层构建树、面包屑、级联选择器：

```java
/**
 * 获取指定部门的祖先链（自顶向下）
 *
 * @param id    部门主键
 * @param limit 限制层级，<=0 表示不限制
 * @return 祖先 id 列表
 */
List<Long> getAncestorIdsFromCache(Long id, long limit);

/**
 * 获取指定部门的全部子孙 id 集合
 *
 * @param id 部门主键
 * @return 子孙 id 集合
 */
Set<Long> getDescIdsFromCache(Long id);

/**
 * 从缓存中批量获取部门对象（按需指定字段）
 *
 * @param ids    部门 id 列表
 * @param fields 指定字段，null 表示全部
 * @return 部门 DTO 列表
 */
List<OrgDepartmentDTO> listDeptFromCache(List<Long> ids, List<String> fields);
```

## 7. 接口变更清单

### 7.1 OrgDepartmentManager（接口）

新增方法：

- `void initCache()`
- `long clearCache()`
- `List<Long> getAncestorIdsFromCache(Long id, long limit)`
- `Set<Long> getDescIdsFromCache(Long id)`
- `List<OrgDepartmentDTO> listDeptFromCache(List<Long> ids, List<String> fields)`

### 7.2 OrgDepartmentManagerImpl（实现类）

- 实现 `ICacheManager<OrgDepartmentDTO>`。
- 定义 5 个 Key 常量：`PREFIX`、`SELF_KEY`、`CODE_KEY`、`CHILDREN_KEY`、`DESC_KEY`、`ANC_KEY`。
- 重写 `add`、`delete`、`modify`、`getDtoById`、`getByDeptCode`、`getChildrenByParentId`、`listAll`，嵌入缓存读写逻辑。
- 新增私有方法 `addCache(RedisOperations, OrgDepartmentDTO)`，供 `putCache` 与 `initCache` 复用。

## 8. 与 SysRegion 缓存方案的差异对比

| 对比维度       | SysRegion（行政区划）           | OrgDepartment（组织部门）                    |
|------------|---------------------------|----------------------------------------|
| **业务主键**   | `regionCode`（String）      | `id`（Long）为主键，`deptCode`（String）为业务编码  |
| **Key 锚定** | 所有 Key 以 `regionCode` 为后缀 | 关系型 Key 以 `id` 为后缀，额外维护 `dept:code` 映射 |
| **祖先链来源**  | `parents` 字段（逗号分隔字符串）     | `path` 字段（部门路径，如 `/1/2/3/`）            |
| **多租户**    | 无                         | 有 `tenantId`，Hash 中存储该字段，由业务层/数据权限过滤   |
| **逻辑删除**   | 物理删除（无 @TableLogic）       | 逻辑删除（@TableLogic），删除时同步清理缓存            |
| **数据规模**   | 全国约 70w 条，相对稳定            | 企业级通常 < 10w 条，变更频率更高                   |

## 9. 风险与兜底策略

1. **缓存与 DB 不一致**：
    - 写操作使用 `MULTI/EXEC` 事务保证缓存多 Key 更新的原子性。
    - 若事务失败，记录 ERROR 日志，由监控告警人工介入；或预留定时任务 `initCache` 做全量修复。

2. **path 字段延迟写入**：
    - 部分系统 `path` 由 DB 触发器或事后任务生成。若 `add` 时 `path` 为空，则 `ancestors` 链不写入，待 `modify` 更新 `path`
      后重新 `putCache` 补齐。

3. **跨级移动部门**：
    - 一期建议限制为“先禁用/删除，再重建”的两次操作，避免复杂事务。
    - 若需支持，在 `modify` 中检测 `parentId` 变更，执行 `deleteCache` + `putCache` 的原子事务。

4. **Redis 故障**：
    - 读缓存异常时捕获并降级到 DB，保证服务可用性。
    - 写缓存异常不影响写 DB 成功，但需记录日志并触发告警。

## 10. 实施步骤

1. **Step 1**：在 `OrgDepartmentManager` 接口中声明新增方法。
2. **Step 2**：在 `OrgDepartmentManagerImpl` 中实现 `ICacheManager<OrgDepartmentDTO>`，定义 Key 常量，实现 `initCache`、
   `putCache`、`updateCache`、`deleteCache`、`clearCache`。
3. **Step 3**：改造 `getDtoById`、`getByDeptCode`、`getChildrenByParentId`、`listAll`，增加缓存读写逻辑。
4. **Step 4**：在 `add`、`modify`、`delete` 中嵌入缓存同步逻辑。
5. **Step 5**：新增 `getAncestorIdsFromCache`、`getDescIdsFromCache`、`listDeptFromCache` 实现。
6. **Step 6**：补充单元测试（Mock RedisUtil 或集成测试），验证缓存命中、失效、预热逻辑。
7. **Step 7**：上线后观察 Redis 内存增长与缓存命中率，按需调整预热批次大小。

---

**文档版本**：v1.0  
**作者**：AI Assistant  
**日期**：2026-04-29
