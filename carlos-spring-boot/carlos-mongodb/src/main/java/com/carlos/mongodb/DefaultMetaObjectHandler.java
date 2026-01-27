package com.carlos.mongodb;

import com.carlos.core.interfaces.ApplicationExtend;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 字段填充策略
 *
 * @author Carlos
 * @date 2021/2/18 14:25
 */
@Slf4j
@AllArgsConstructor
public class DefaultMetaObjectHandler implements MetaObjectHandler {

    private ApplicationExtend requestExtend;

    private MybatisCommonField commonField;

    @Override
    public void insertFill(final MetaObject metaObject) {
        // 填充创建时间
        final String createTimeFiledName = this.commonField.createTimeFiledName();
        if (StringUtils.isNoneBlank(createTimeFiledName)) {
            final Class<?> timeFiledType = this.commonField.createTimeFiledType();
            // if (timeFiledType.equals(Date.class)) {
            //     this.strictInsertFill(metaObject, createTimeFiledName, Date::new, Date.class);
            // }
            // if (timeFiledType.equals(LocalDateTime.class)) {
            //     this.strictInsertFill(metaObject, createTimeFiledName, LocalDateTime::now,
            // LocalDateTime.class);
            // }
        }

        // 填充创建人
        final String createUserFiledName = this.commonField.createUserFiledName();
        final Serializable userId = this.getUserId();
        if (StringUtils.isNotBlank(createUserFiledName) && userId != null) {
            while (true) {
                // if (userId instanceof Long) {
                //     this.strictInsertFill(metaObject, createUserFiledName, () -> (Long) userId,
                // Long.class);
                //     break;
                // }
                // if (userId instanceof String) {
                //     this.strictInsertFill(metaObject, createUserFiledName, () -> (String) userId,
                // String.class);
                //     break;
                // }
                // if (userId instanceof Integer) {
                //     this.strictInsertFill(metaObject, createUserFiledName, () -> (Integer) userId,
                // Integer.class);
                //     break;
                // }
                // if (userId instanceof BigInteger) {
                //     this.strictInsertFill(metaObject, createUserFiledName, () -> (BigInteger) userId,
                // BigInteger.class);
                //     break;
                // }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("insert default field: ");
        }
    }

    @Override
    public void updateFill(final MetaObject metaObject) {
        final String updateTimeFiledName = this.commonField.updateTimeFiledName();
        if (StringUtils.isNoneBlank(updateTimeFiledName)) {
            final Class<?> timeFiledType = this.commonField.updateTimeFiledType();
            // if (timeFiledType.equals(Date.class)) {
            //     this.strictUpdateFill(metaObject, updateTimeFiledName, Date::new, Date.class);
            // }
            // if (timeFiledType.equals(LocalDateTime.class)) {
            //     this.strictUpdateFill(metaObject, updateTimeFiledName, LocalDateTime::now,
            // LocalDateTime.class);
            // }
        }

        final String updateUserFiledName = this.commonField.updateUserFiledName();
        final Serializable userId = this.getUserId();
        if (StringUtils.isNotBlank(updateUserFiledName) && userId != null) {
            // if (userId instanceof Long) {
            //     this.strictUpdateFill(metaObject, updateUserFiledName, () -> (Long) userId,
            // Long.class);
            // }
            // if (userId instanceof String) {
            //     this.strictUpdateFill(metaObject, updateUserFiledName, () -> (String) userId,
            // String.class);
            // }
            // if (userId instanceof Integer) {
            //     this.strictUpdateFill(metaObject, updateUserFiledName, () -> (Integer) userId,
            // Integer.class);
            // }
            // if (userId instanceof BigInteger) {
            //     this.strictUpdateFill(metaObject, updateUserFiledName, () -> (BigInteger) userId,
            // BigInteger.class);
            // }
        }

        if (log.isDebugEnabled()) {
            log.debug("update default field: ");
        }
    }

    /**
     * 获取当前登录用户id
     *
     * @return java.io.Serializable
     * @author Carlos
     * @date 2021/11/12 13:52
     */
    private Serializable getUserId() {
        if (this.requestExtend == null) {
            return null;
        }
        return this.requestExtend.getUserId();
    }
}
