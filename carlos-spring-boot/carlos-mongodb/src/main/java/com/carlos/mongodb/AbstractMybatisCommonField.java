package com.carlos.mongodb;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * mybatis通用信息获取接口
 *
 * @author Carlos
 * @date 2020/6/3 11:09
 */
public abstract class AbstractMybatisCommonField implements MybatisCommonField {

    @Override
    public String primaryKeyFiledName() {
        return null;
    }

    @Override
    public Class<?> primaryKeyFiledType() {
        return Long.class;
    }

    @Override
    public Class<?> updateTimeFiledType() {
        return Date.class;
    }

    @Override
    public Class<?> createTimeFiledType() {
        return LocalDateTime.class;
    }

    @Override
    public String updateTimeFiledName() {
        return null;
    }

    @Override
    public String updateUserFiledName() {
        return null;
    }

    @Override
    public String createTimeFiledName() {
        return null;
    }

    @Override
    public String createUserFiledName() {
        return null;
    }

    @Override
    public String logicDeleteFiledName() {
        return null;
    }

    @Override
    public String versionFiledName() {
        return null;
    }
}
