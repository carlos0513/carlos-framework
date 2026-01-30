package com.carlos.mongodb;

/**
 * mybatis通用信息获取接口
 *
 * @author Carlos
 * @date 2020/6/3 11:09
 */
public interface MybatisCommonField {

    String DEFAULT_PRIMARY_KEY_FILED_NAME = "id";
    Class<Long> DEFAULT_PRIMARY_KEY_FILED_TYPE = Long.class;
    String DEFAULT_VERSION_FILED_NAME = "version";
    String DEFAULT_LOGIC_DELETE_FILED_NAME = "is_deleted";
    String DEFAULT_UPDATE_USER_FILED_NAME = "updateBy";
    String DEFAULT_UPDATE_TIME_FILED_NAME = "updateTime";
    String DEFAULT_CREATE_USER_FILED_NAME = "createBy";
    String DEFAULT_CREATE_TIME_FILED_NAME = "createTime";

    /**
     * 主键字段名称
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:37
     */
    String primaryKeyFiledName();

    /**
     * 主键字段类型
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:37
     */
    Class<?> primaryKeyFiledType();

    /**
     * 创建时间字段类型
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:37
     */
    Class<?> createTimeFiledType();

    /**
     * 更新时间字段类型
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:37
     */
    Class<?> updateTimeFiledType();

    /**
     * 修改时间字段名称
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:38
     */
    String updateTimeFiledName();

    /**
     * 修改人字段名称
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:38
     */
    String updateUserFiledName();

    /**
     * 创建时间字段名称
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:38
     */
    String createTimeFiledName();

    /**
     * 创建人字段名称
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:38
     */
    String createUserFiledName();

    /**
     * 逻辑删除字段名称
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:38
     */
    String logicDeleteFiledName();

    /**
     * 版本号字段名称
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2021/11/12 10:38
     */
    String versionFiledName();
}
