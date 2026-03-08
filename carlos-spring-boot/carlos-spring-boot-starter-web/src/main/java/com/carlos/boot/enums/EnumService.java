package com.carlos.boot.enums;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.carlos.core.constant.CoreConstant;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.enums.Enum;
import com.google.common.collect.Sets;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 应用枚举配置，在应用启动时会扫描对应的枚举类，并且加入内存缓存中以供查询
 * </p>
 *
 * @author carlos
 */
@Slf4j
@RequiredArgsConstructor
public class EnumService {

    private final ApplicationEnumProperties enumProperties;

    /**
     * 枚举字典缓存
     */
    public static Map<String, List<Enum>> ENUM_CACHE = new ConcurrentHashMap<>();

    public static final String SPRING_BOOT_JAR_PREFIX = "BOOT-INF.classes.";


    /**
     * 枚举字典初始化
     */
    @PostConstruct
    public void init() {
        try {
            final Set<String> enumPackages = enumProperties.getScanPackage();
            if (CollectionUtil.isEmpty(enumPackages)) {
                return;
            }
            Set<String> packages = Sets.newHashSet(CoreConstant.PROJECT_BASE_PATH);
            packages.addAll(enumPackages);

            // 获取BaseEnum接口的所有实现
            if (log.isDebugEnabled()) {
                log.debug("枚举包:{}", packages);
            }

            FilterBuilder filterBuilder = new FilterBuilder();

            for (String aPackage : packages) {
                filterBuilder.includePackage(SPRING_BOOT_JAR_PREFIX + aPackage).includePackage(aPackage);
            }
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .forPackages(packages.toArray(new String[0]))
                .filterInputsBy(filterBuilder)
                .setScanners(Scanners.SubTypes);

            // 使用反射 扫描工程的基本包路径
            final Reflections reflections = new Reflections(configurationBuilder);
            // BaseEnum的子类
            final Set<Class<? extends BaseEnum>> baseEnums = reflections.getSubTypesOf(BaseEnum.class);
            log.info("Scan 'BaseEnum' subClass, count= {}", baseEnums.size());
            if (CollectionUtil.isEmpty(baseEnums)) {
                return;
            }
            // 循环获取BaseEnum枚举
            List<Enum> list;
            for (final Class<? extends BaseEnum> clazz : baseEnums) {
                final BaseEnum[] constants = clazz.getEnumConstants();
                list = new ArrayList<>();
                try {
                    Enum e;
                    for (final BaseEnum baseEnum : constants) {
                        e = new Enum();
                        e.setCode(baseEnum.getCode());
                        e.setDesc(baseEnum.getDesc().intern());
                        e.setName(baseEnum.toString());
                        list.add(e);
                    }
                    // 设置map
                    final AppEnum annotation = clazz.getAnnotation(AppEnum.class);
                    if (annotation == null) {
                        log.error("Class {} not annotation with @AppEnum", clazz.getSimpleName());
                        continue;
                    }
                    ENUM_CACHE.put(annotation.code(), list);
                } catch (Exception e) {
                    log.warn("枚举加载异常：{}", clazz.getSimpleName(), e);
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("字典已初始化");
            }
        } catch (final Exception e) {
            log.error("获取BaseEnum枚举map异常", e);
        }
    }


    /**
     * 获取枚举列表
     *
     * @return java.util.Map<java.lang.String, java.util.List < com.carlos.core.enums.EnumVo < com.carlos.core.enums.BaseEnum>>>
     */
    public Map<String, List<Enum>> getEnumList() {
        if (MapUtil.isEmpty(ENUM_CACHE)) {
            init();
        }
        return ENUM_CACHE;
    }

    /**
     * 获取某个枚举类的详情
     *
     * @param name 枚举类的名称
     */
    public List<Enum> getEnumInfo(final String name) {
        return ENUM_CACHE.get(name);
    }
}
