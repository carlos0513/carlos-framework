package com.carlos.mongodb;

import com.carlos.core.interfaces.ApplicationExtend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 字段填充策略默认实现
 *
 * @author Carlos
 * @date 2021/2/18 14:25
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultMetaObjectHandler implements MetaObjectHandler {

    private final ApplicationExtend requestExtend;
    private final MybatisCommonField commonField;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始执行插入字段自动填充");

        // 填充创建时间
        fillCreateTime(metaObject);

        // 填充更新时间
        fillUpdateTime(metaObject);

        // 填充创建人
        fillCreateBy(metaObject);

        // 填充更新人
        fillUpdateBy(metaObject);

        log.debug("插入字段自动填充完成");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始执行更新字段自动填充");

        // 填充更新时间
        fillUpdateTime(metaObject);

        // 填充更新人
        fillUpdateBy(metaObject);

        log.debug("更新字段自动填充完成");
    }

    /**
     * 填充创建时间
     */
    private void fillCreateTime(MetaObject metaObject) {
        String fieldName = commonField.createTimeFiledName();
        if (StringUtils.isBlank(fieldName)) {
            return;
        }

        Class<?> fieldType = commonField.createTimeFiledType();
        fillTime(metaObject, fieldName, fieldType);
    }

    /**
     * 填充更新时间
     */
    private void fillUpdateTime(MetaObject metaObject) {
        String fieldName = commonField.updateTimeFiledName();
        if (StringUtils.isBlank(fieldName)) {
            return;
        }

        Class<?> fieldType = commonField.updateTimeFiledType();
        fillTime(metaObject, fieldName, fieldType);
    }

    /**
     * 填充创建人
     */
    private void fillCreateBy(MetaObject metaObject) {
        String fieldName = commonField.createUserFiledName();
        if (StringUtils.isBlank(fieldName)) {
            return;
        }

        Serializable userId = getUserId();
        if (userId == null) {
            return;
        }

        fillUserId(metaObject, fieldName, userId);
    }

    /**
     * 填充更新人
     */
    private void fillUpdateBy(MetaObject metaObject) {
        String fieldName = commonField.updateUserFiledName();
        if (StringUtils.isBlank(fieldName)) {
            return;
        }

        Serializable userId = getUserId();
        if (userId == null) {
            return;
        }

        fillUserId(metaObject, fieldName, userId);
    }

    /**
     * 根据用户ID类型填充
     */
    private void fillUserId(MetaObject metaObject, String fieldName, Serializable userId) {
        if (userId instanceof Long) {
            strictFill(metaObject, fieldName, () -> (Long) userId);
        } else if (userId instanceof String) {
            strictFill(metaObject, fieldName, () -> (String) userId);
        } else if (userId instanceof Integer) {
            strictFill(metaObject, fieldName, () -> (Integer) userId);
        } else if (userId instanceof BigInteger) {
            strictFill(metaObject, fieldName, () -> (BigInteger) userId);
        } else {
            // 其他类型直接填充
            strictFill(metaObject, fieldName, () -> userId);
        }
    }

    /**
     * 获取当前登录用户id
     *
     * @return java.io.Serializable
     */
    private Serializable getUserId() {
        if (this.requestExtend == null) {
            return null;
        }
        return this.requestExtend.getUserId();
    }
}
