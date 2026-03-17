package com.carlos.datasource.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>
 * 字段填充策略处理器（优化版）
 * </p>
 *
 * <p>
 * 使用策略模式替代冗长的 if-else 和 while(true) 代码块
 * 支持配置驱动的字段填充
 * </p>
 *
 * @author carlos
 * @date 2021/2/18 14:25
 */
@Slf4j
public class DefaultMetaObjectHandler implements MetaObjectHandler {

    private final com.carlos.core.interfaces.ApplicationExtend requestExtend;
    private final MybatisCommonField commonField;
    private final MybatisProperties mybatisProperties;

    /**
     * 时间字段填充策略映射
     */
    private static final Map<Class<?>, Supplier<?>> TIME_FILLER_MAP = new HashMap<>();

    /**
     * 用户ID字段填充策略映射
     */
    private static final Map<Class<?>, Function<Serializable, ?>> USER_ID_FILLER_MAP = new HashMap<>();

    static {
        // 初始化时间填充策略
        TIME_FILLER_MAP.put(Date.class, Date::new);
        TIME_FILLER_MAP.put(LocalDateTime.class, LocalDateTime::now);
        TIME_FILLER_MAP.put(Long.class, System::currentTimeMillis);

        // 初始化用户ID填充策略（类型转换）
        USER_ID_FILLER_MAP.put(Long.class, userId -> userId);
        USER_ID_FILLER_MAP.put(String.class, userId -> String.valueOf(userId));
        USER_ID_FILLER_MAP.put(Integer.class, userId -> Integer.parseInt(String.valueOf(userId)));
        USER_ID_FILLER_MAP.put(BigInteger.class, userId -> new BigInteger(String.valueOf(userId)));
    }

    public DefaultMetaObjectHandler(com.carlos.core.interfaces.ApplicationExtend requestExtend,
                                    MybatisCommonField commonField,
                                    MybatisProperties mybatisProperties) {
        this.requestExtend = requestExtend;
        this.commonField = commonField;
        this.mybatisProperties = mybatisProperties;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        if (!mybatisProperties.getFill().isEnableInsertFill()) {
            return;
        }

        log.debug("执行插入字段填充");

        // 填充创建时间
        fillTimeField(metaObject, commonField.createTimeFiledName(), true);

        // 填充更新时间
        fillTimeField(metaObject, commonField.updateTimeFiledName(), true);

        // 填充创建人
        fillUserField(metaObject, commonField.createUserFiledName(), true);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (!mybatisProperties.getFill().isEnableUpdateFill()) {
            return;
        }

        log.debug("执行更新字段填充");

        // 填充更新时间
        fillTimeField(metaObject, commonField.updateTimeFiledName(), false);

        // 填充更新人
        fillUserField(metaObject, commonField.updateUserFiledName(), false);
    }

    /**
     * 填充时间字段
     *
     * @param metaObject 元对象
     * @param fieldName  字段名
     * @param isInsert   是否插入操作
     */
    @SuppressWarnings("unchecked")
    private void fillTimeField(MetaObject metaObject, String fieldName, boolean isInsert) {
        if (StringUtils.isBlank(fieldName) || !metaObject.hasSetter(fieldName)) {
            return;
        }

        Object currentValue = metaObject.getValue(fieldName);
        if (currentValue != null) {
            // 已有值，不覆盖
            return;
        }

        Class<?> fieldType = metaObject.getGetterType(fieldName);
        Supplier<?> filler = TIME_FILLER_MAP.get(fieldType);

        if (filler != null) {
            if (isInsert) {
                if (fieldType == Date.class) {
                    strictInsertFill(metaObject, fieldName, (Class<Date>) fieldType, (Date) filler.get());
                } else if (fieldType == LocalDateTime.class) {
                    strictInsertFill(metaObject, fieldName, (Class<LocalDateTime>) fieldType, (LocalDateTime) filler.get());
                } else if (fieldType == Long.class) {
                    strictInsertFill(metaObject, fieldName, (Class<Long>) fieldType, (Long) filler.get());
                }
            } else {
                if (fieldType == Date.class) {
                    strictUpdateFill(metaObject, fieldName, (Class<Date>) fieldType, (Date) filler.get());
                } else if (fieldType == LocalDateTime.class) {
                    strictUpdateFill(metaObject, fieldName, (Class<LocalDateTime>) fieldType, (LocalDateTime) filler.get());
                } else if (fieldType == Long.class) {
                    strictUpdateFill(metaObject, fieldName, (Class<Long>) fieldType, (Long) filler.get());
                }
            }
            log.debug("填充时间字段: {} = {}", fieldName, filler.get());
        } else {
            log.warn("不支持的时间字段类型: {}.{} = {}",
                metaObject.getOriginalObject().getClass().getSimpleName(),
                fieldName, fieldType.getSimpleName());
        }
    }

    /**
     * 填充用户字段
     *
     * @param metaObject 元对象
     * @param fieldName  字段名
     * @param isInsert   是否插入操作
     */
    @SuppressWarnings("unchecked")
    private void fillUserField(MetaObject metaObject, String fieldName, boolean isInsert) {
        if (StringUtils.isBlank(fieldName) || !metaObject.hasSetter(fieldName)) {
            return;
        }

        Object currentValue = metaObject.getValue(fieldName);
        if (currentValue != null) {
            // 已有值，不覆盖
            return;
        }

        Serializable userId = getUserId();
        if (userId == null) {
            log.debug("无法获取当前用户ID，跳过填充字段: {}", fieldName);
            return;
        }

        Class<?> fieldType = metaObject.getGetterType(fieldName);
        Function<Serializable, ?> converter = USER_ID_FILLER_MAP.get(fieldType);

        if (converter != null) {
            try {
                Object value = converter.apply(userId);
                if (isInsert) {
                    if (fieldType == Long.class) {
                        strictInsertFill(metaObject, fieldName, (Class<Long>) fieldType, (Long) value);
                    } else if (fieldType == String.class) {
                        strictInsertFill(metaObject, fieldName, (Class<String>) fieldType, (String) value);
                    } else if (fieldType == Integer.class) {
                        strictInsertFill(metaObject, fieldName, (Class<Integer>) fieldType, (Integer) value);
                    } else if (fieldType == BigInteger.class) {
                        strictInsertFill(metaObject, fieldName, (Class<BigInteger>) fieldType, (BigInteger) value);
                    }
                } else {
                    if (fieldType == Long.class) {
                        strictUpdateFill(metaObject, fieldName, (Class<Long>) fieldType, (Long) value);
                    } else if (fieldType == String.class) {
                        strictUpdateFill(metaObject, fieldName, (Class<String>) fieldType, (String) value);
                    } else if (fieldType == Integer.class) {
                        strictUpdateFill(metaObject, fieldName, (Class<Integer>) fieldType, (Integer) value);
                    } else if (fieldType == BigInteger.class) {
                        strictUpdateFill(metaObject, fieldName, (Class<BigInteger>) fieldType, (BigInteger) value);
                    }
                }
                log.debug("填充用户字段: {} = {}", fieldName, value);
            } catch (Exception e) {
                log.warn("用户ID类型转换失败: {} -> {}", userId, fieldType.getSimpleName(), e);
            }
        } else {
            log.warn("不支持的用户ID字段类型: {}.{} = {}",
                metaObject.getOriginalObject().getClass().getSimpleName(),
                fieldName, fieldType.getSimpleName());
        }
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    private Serializable getUserId() {
        if (requestExtend == null) {
            return null;
        }
        try {
            return requestExtend.getUserId();
        } catch (Exception e) {
            log.warn("获取当前用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 注册自定义的时间字段填充策略
     *
     * @param type   字段类型
     * @param filler 填充函数
     */
    public static void registerTimeFiller(Class<?> type, Supplier<?> filler) {
        TIME_FILLER_MAP.put(type, filler);
    }

    /**
     * 注册自定义的用户ID字段填充策略
     *
     * @param type      字段类型
     * @param converter 转换函数
     */
    public static void registerUserIdConverter(Class<?> type, Function<Serializable, ?> converter) {
        USER_ID_FILLER_MAP.put(type, converter);
    }
}
