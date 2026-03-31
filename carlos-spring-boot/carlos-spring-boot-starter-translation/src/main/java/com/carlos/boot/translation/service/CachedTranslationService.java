package com.carlos.boot.translation.service;

import com.carlos.boot.translation.cache.CacheKeys;
import com.carlos.boot.translation.cache.TranslationCacheManager;
import com.carlos.boot.translation.core.TranslationBatch;
import com.carlos.boot.translation.core.TranslationData;
import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.base.Dict;
import com.carlos.core.base.RegionInfo;
import com.carlos.core.base.UserInfo;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.enums.EnumUtil;
import com.carlos.core.interfaces.ApplicationExtend;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 翻译服务实现（带多级缓存）
 * </p>
 *
 * @author carlos
 * @date 2025/01/20
 */
@Service
@Slf4j
@AllArgsConstructor
public class CachedTranslationService implements TranslationService {

    private final ApplicationExtend applicationExtend;

    private final TranslationCacheManager cacheManager;

    @Override
    public TranslationData translate(TranslationBatch batch) {
        TranslationData result = new TranslationData();

        // 1. 翻译用户
        if (!batch.getUserIds().isEmpty()) {
            result.setUsers(translateUsers(batch.getUserIds()));
        }

        // 2. 翻译字典
        if (MapUtils.isNotEmpty(batch.getDictCodes())) {
            result.setDicts(translateDicts(batch.getDictCodes()));
        }

        // 3. 翻译部门
        if (!batch.getDeptIds().isEmpty()) {
            result.setDepts(translateDepts(batch.getDeptIds()));
        }

        // 4. 翻译区域
        if (!batch.getRegionCodes().isEmpty()) {
            result.setRegions(translateRegions(batch.getRegionCodes()));
        }

        // 5. 翻译枚举（枚举通常从内存获取，不需要远程查询）
        if (MapUtils.isNotEmpty(batch.getEnumValues())) {
            result.setEnums(translateEnums(batch.getEnumValues()));
        }

        return result;
    }

    /**
     * 翻译用户信息
     *
     * @param ids 用户ID集合
     * @return 用户数据
     */
    private Map<Serializable, UserInfo> translateUsers(Set<Serializable> ids) {
        Map<Serializable, UserInfo> result = new HashMap<>(ids.size());
        Set<Serializable> missIds = new HashSet<>();

        // L1 + L2 缓存查询
        for (Serializable id : ids) {
            String key = CacheKeys.userKey(id);
            UserInfo cached = cacheManager.get(key, UserInfo.class);

            if (cached != null) {
                result.put(id, cached);
            } else {
                missIds.add(id);
            }
        }

        // 批量查询缺失数据
        if (CollectionUtils.isNotEmpty(missIds)) {
            log.debug("Batch query users: {}", missIds);
            Map<Serializable, UserInfo> dbData = applicationExtend.getUserByIds(missIds);

            // 回填结果并写入缓存
            dbData.forEach((id, user) -> {
                result.put(id, user);
                cacheManager.put(CacheKeys.userKey(id), user);
            });
        }

        return result;
    }

    /**
     * 翻译字典信息
     *
     * @param typeCodes 字典类型和编码映射
     * @return 字典数据
     */
    private Map<String, Dict> translateDicts(Map<String, Set<String>> typeCodes) {
        Map<String, Dict> result = new HashMap<>();
        Map<String, Set<String>> missMap = new HashMap<>();

        // 缓存查询
        for (Map.Entry<String, Set<String>> entry : typeCodes.entrySet()) {
            String type = entry.getKey();
            for (String code : entry.getValue()) {
                String key = CacheKeys.dictKey(type, code);
                Dict cached = cacheManager.get(key, Dict.class);

                if (cached != null) {
                    result.put(type + ":" + code, cached);
                } else {
                    missMap.computeIfAbsent(type, k -> new HashSet<>()).add(code);
                }
            }
        }

        // 批量查询
        if (MapUtils.isNotEmpty(missMap)) {
            log.debug("Batch query dicts: {}", missMap);
            for (Map.Entry<String, Set<String>> entry : missMap.entrySet()) {
                String type = entry.getKey();
                Map<String, Dict> dbData = applicationExtend.getDictVos(type, entry.getValue());

                dbData.forEach((code, dict) -> {
                    String compositeKey = type + ":" + code;
                    result.put(compositeKey, dict);
                    cacheManager.put(CacheKeys.dictKey(type, code), dict);
                });
            }
        }

        return result;
    }

    /**
     * 翻译部门信息
     *
     * @param ids 部门ID集合
     * @return 部门数据
     */
    private Map<Serializable, DepartmentInfo> translateDepts(Set<Serializable> ids) {
        Map<Serializable, DepartmentInfo> result = new HashMap<>(ids.size());
        Set<Serializable> missIds = new HashSet<>();

        // 缓存查询
        for (Serializable id : ids) {
            String key = CacheKeys.deptKey(id);
            DepartmentInfo cached = cacheManager.get(key, DepartmentInfo.class);

            if (cached != null) {
                result.put(id, cached);
            } else {
                missIds.add(id);
            }
        }

        // 批量查询
        if (CollectionUtils.isNotEmpty(missIds)) {
            log.debug("Batch query departments: {}", missIds);
            Map<Serializable, DepartmentInfo> dbData = applicationExtend.getDepartmentByIds(missIds);

            dbData.forEach((id, dept) -> {
                result.put(id, dept);
                cacheManager.put(CacheKeys.deptKey(id), dept);
            });
        }

        return result;
    }

    /**
     * 翻译区域信息
     *
     * @param codes 区域编码集合
     * @return 区域数据
     */
    private Map<String, RegionInfo> translateRegions(Set<String> codes) {
        Map<String, RegionInfo> result = new HashMap<>(codes.size());
        Set<String> missCodes = new HashSet<>();

        // 缓存查询
        for (String code : codes) {
            String key = CacheKeys.regionKey(code);
            RegionInfo cached = cacheManager.get(key, RegionInfo.class);

            if (cached != null) {
                result.put(code, cached);
            } else {
                missCodes.add(code);
            }
        }

        // 批量查询
        if (CollectionUtils.isNotEmpty(missCodes)) {
            log.debug("Batch query regions: {}", missCodes);
            Map<String, RegionInfo> dbData = applicationExtend.getRegionByCodes(missCodes);

            dbData.forEach((code, region) -> {
                result.put(code, region);
                cacheManager.put(CacheKeys.regionKey(code), region);
            });
        }

        return result;
    }

    /**
     * 翻译枚举值
     *
     * @param enumValues 枚举值集合
     * @return 枚举数据
     */
    private Map<String, com.carlos.core.enums.Enum> translateEnums(
        Map<Class<? extends BaseEnum>, Set<Object>> enumValues) {
        Map<String, com.carlos.core.enums.Enum> result = new HashMap<>();

        for (Map.Entry<Class<? extends BaseEnum>, Set<Object>> entry : enumValues.entrySet()) {
            Class<? extends BaseEnum> enumClass = entry.getKey();

            for (Object code : entry.getValue()) {
                BaseEnum enumItem = EnumUtil.getByCode(enumClass, code);
                if (enumItem != null) {
                    com.carlos.core.enums.Enum vo = enumItem.getEnumVo();
                    result.put(enumClass.getName() + ":" + code, vo);
                }
            }
        }

        return result;
    }
}
