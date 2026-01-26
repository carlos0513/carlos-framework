package com.yunjin.datasource.config;

/**
 * <p>
 * 默认mybatis共同字段
 * </p>
 *
 * @author yunjin
 * @date 2020/6/3 11:09
 */
public class DefaultMybatisCommonField extends AbstractMybatisCommonField {

    @Override
    public String primaryKeyFiledName() {
        return DEFAULT_PRIMARY_KEY_FILED_NAME;
    }

    @Override
    public String updateTimeFiledName() {
        return DEFAULT_UPDATE_TIME_FILED_NAME;
    }

    @Override
    public String updateUserFiledName() {
        return DEFAULT_UPDATE_USER_FILED_NAME;
    }

    @Override
    public String createTimeFiledName() {
        return DEFAULT_CREATE_TIME_FILED_NAME;
    }

    @Override
    public String createUserFiledName() {
        return DEFAULT_CREATE_USER_FILED_NAME;
    }

    @Override
    public String logicDeleteFiledName() {
        return DEFAULT_LOGIC_DELETE_FILED_NAME;
    }

    @Override
    public String versionFiledName() {
        return DEFAULT_VERSION_FILED_NAME;
    }
}
