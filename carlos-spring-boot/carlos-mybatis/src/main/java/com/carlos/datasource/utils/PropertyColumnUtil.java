package com.carlos.datasource.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author yunjin
 * @date 2020/3/2
 **/
public class PropertyColumnUtil {

    private static final Map<Class<?>, Map<String, String>> CACHE_MAP = new ConcurrentHashMap<>();

    public static Map<Class<?>, Map<String, String>> getMap() {
        return CACHE_MAP;
    }

    /**
     * 根据实体class，从mybatisplus中获取对应Table的属性列名Map
     */
    private static Map<String, String> getTableFieldMap(Class<?> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if (tableInfo == null) {
            return null;
        }
        List<TableFieldInfo> tableFieldInfos = tableInfo.getFieldList();
        if (CollectionUtil.isEmpty(tableFieldInfos)) {
            return null;
        }
        return tableFieldInfos.stream().collect(Collectors.toMap(TableFieldInfo::getProperty, TableFieldInfo::getColumn));
    }

    /**
     * 从本地缓存中获取属性列名map
     */
    public static Map<String, String> getPropertyColumnMap(Class<?> clazz) {
        Map<String, String> propertyColumnMap = CACHE_MAP.get(clazz);
        if (MapUtil.isEmpty(propertyColumnMap)) {
            // 从TableInfo中获取，并缓存到内存map中
            Map<String, String> fieldMap = getTableFieldMap(clazz);
            if (MapUtil.isEmpty(fieldMap)) {
                return null;
            } else {
                CACHE_MAP.put(clazz, fieldMap);
                return fieldMap;
            }
        } else {
            return propertyColumnMap;
        }
    }

    /**
     * 通过实体class类型和属性名称，从缓存中获取对应的列名
     *
     * @param clazz    对象Class
     * @param property 对象属性名称
     * @author yunjin
     */
    public static String getColumn(Class<?> clazz, String property) {
        Map<String, String> propertyColumnMap = getPropertyColumnMap(clazz);
        if (MapUtil.isEmpty(propertyColumnMap)) {
            throw new IllegalArgumentException("没有找到对应的实体映射对象");
        }
        String column = propertyColumnMap.get(property);
        if (StrUtil.isEmpty(column)) {
            throw new IllegalArgumentException("没有找到对应的列");
        }
        return column;
    }

}
