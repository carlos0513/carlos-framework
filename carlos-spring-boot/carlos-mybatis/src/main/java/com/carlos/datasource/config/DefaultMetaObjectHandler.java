package com.carlos.datasource.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.carlos.core.interfaces.ApplicationExtend;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

/**
 * <p>
 * 字段填充策略
 * </p>
 *
 * @author yunjin
 * @date 2021/2/18 14:25
 */
@Slf4j
@AllArgsConstructor
public class DefaultMetaObjectHandler implements MetaObjectHandler {

    private ApplicationExtend requestExtend;

    private MybatisCommonField commonField;

    @Override
    public void insertFill(MetaObject metaObject) {
        // 填充创建时间
        String createTimeFiledName = commonField.createTimeFiledName();
        if (StringUtils.isNoneBlank(createTimeFiledName)) {
            if (metaObject.hasSetter(createTimeFiledName)) {
                Object createTime = metaObject.getValue(createTimeFiledName);
                if (createTime == null) {
                    Class<?> timeFiledType = metaObject.getGetterType(createTimeFiledName);
                    if (timeFiledType.equals(Date.class)) {
                        this.strictInsertFill(metaObject, createTimeFiledName, Date::new, Date.class);
                    }
                    if (timeFiledType.equals(LocalDateTime.class)) {
                        this.strictInsertFill(metaObject, createTimeFiledName, LocalDateTime::now, LocalDateTime.class);
                    }
                    // 时间戳
                    if (timeFiledType.equals(Long.class)) {
                        this.strictInsertFill(metaObject, createTimeFiledName, System::currentTimeMillis, Long.class);
                    }
                }
            }
        }

        // 填充创建人
        String createUserFiledName = commonField.createUserFiledName();
        if (StringUtils.isNotBlank(createUserFiledName)) {
            if (metaObject.hasSetter(createUserFiledName)) {
                Object value = metaObject.getValue(createUserFiledName);
                if (value == null) {
                    Serializable userId = getUserId();
                    if (userId != null) {
                        Class<?> createUserIdType = metaObject.getGetterType(createUserFiledName);
                        while (true) {
                            if (createUserIdType == Long.class) {
                                this.strictInsertFill(metaObject, createUserFiledName, () -> (Long) userId, Long.class);
                                break;
                            }
                            if (createUserIdType == String.class) {
                                this.strictInsertFill(metaObject, createUserFiledName, () -> (String) userId, String.class);
                                break;
                            }
                            if (createUserIdType == Integer.class) {
                                this.strictInsertFill(metaObject, createUserFiledName, () -> (Integer) userId, Integer.class);
                                break;
                            }
                            if (createUserIdType == BigInteger.class) {
                                this.strictInsertFill(metaObject, createUserFiledName, () -> (BigInteger) userId, BigInteger.class);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String updateTimeFiledName = commonField.updateTimeFiledName();
        // 填充更新时间
        if (StringUtils.isNoneBlank(updateTimeFiledName)) {
            // 如果对象存在此字段
            if (metaObject.hasSetter(updateTimeFiledName)) {
                Object value = metaObject.getValue(updateTimeFiledName);
                if (value == null) {
                    Class<?> timeFiledType = metaObject.getGetterType(updateTimeFiledName);
                    if (timeFiledType.equals(Date.class)) {
                        this.strictUpdateFill(metaObject, updateTimeFiledName, Date::new, Date.class);
                    }
                    if (timeFiledType.equals(LocalDateTime.class)) {
                        this.strictUpdateFill(metaObject, updateTimeFiledName, LocalDateTime::now, LocalDateTime.class);
                    }
                    // 时间戳
                    if (timeFiledType.equals(Long.class)) {
                        this.strictUpdateFill(metaObject, updateTimeFiledName, Long.class, System.currentTimeMillis());
                    }
                }
            }
        }

        // 填充更新人
        String updateUserFiledName = commonField.updateUserFiledName();
        if (StringUtils.isNotBlank(updateUserFiledName)) {
            if (metaObject.hasSetter(updateUserFiledName)) {
                Object value = metaObject.getValue(updateUserFiledName);
                if (value == null) {
                    Serializable userId = getUserId();
                    if (userId != null) {
                        Class<?> updateUserIdType = metaObject.getGetterType(updateUserFiledName);
                        while (true) {
                            if (updateUserIdType == Long.class) {
                                this.strictUpdateFill(metaObject, updateUserFiledName, () -> (Long) userId, Long.class);
                                break;
                            }
                            if (updateUserIdType == String.class) {
                                this.strictUpdateFill(metaObject, updateUserFiledName, () -> (String) userId, String.class);
                                break;
                            }
                            if (updateUserIdType == Integer.class) {
                                this.strictUpdateFill(metaObject, updateUserFiledName, () -> (Integer) userId, Integer.class);
                                break;
                            }
                            if (updateUserIdType == BigInteger.class) {
                                this.strictUpdateFill(metaObject, updateUserFiledName, () -> (BigInteger) userId, BigInteger.class);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取当前登录用户id
     *
     * @return java.io.Serializable
     * @author yunjin
     * @date 2021/11/12 13:52
     */
    private Serializable getUserId() {
        if (requestExtend == null) {
            return null;
        }
        try {
            return requestExtend.getUserId();
        } catch (Exception e) {
            log.warn("RequestExtend getUserId failed", e);
            return null;
        }
    }
}

