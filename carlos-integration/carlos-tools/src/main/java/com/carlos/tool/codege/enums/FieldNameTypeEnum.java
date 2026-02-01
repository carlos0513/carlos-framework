package com.carlos.tool.codege.enums;

/**
 * <p>
 * 数据库字段命名方式枚举
 * </p>
 *
 * @author Carlos
 * @date 2019/12/25 15:19 database
 */

public enum FieldNameTypeEnum {

    /**
     * 没有前缀名，全小写（create_time -> createTime）
     */
    NOT_PREFIX_AND_UNDERLINE("不含前缀且下划线小写"),
    /**
     * 没有前缀名，小驼峰命名（createTime -> createTime）
     */
    NOT_PREFIX_AND_CAMEL("不含前缀且小驼峰"),
    /**
     * 前缀名，下划线隔开全小写（fd_create_time -> createTime）
     */
    PREFIX_AND_UNDERLINE("含前缀且下划线小写"),
    /**
     * 前缀名，小驼峰命名（fd_createTime -> createTime）
     */
    PREFIX_AND_CAMEL("含前缀且小驼峰"),
    /**
     * 没有前缀且全部大写（CREATE_TIME -> createTime CREATETIME -> createtime）
     */
    ALL_UPPER("全部大写");

    private final String describe;

    FieldNameTypeEnum(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

}
