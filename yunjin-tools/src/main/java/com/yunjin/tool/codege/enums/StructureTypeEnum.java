package com.yunjin.tool.codege.enums;

/**
 * <p>
 * 项目模块结构枚举
 * </p>
 *
 * @author Carlos
 * @date 2020/8/18 12:09
 * @since 3.0
 */
public enum StructureTypeEnum {
    /**
     * 单模块工程
     */
    SINGLE_MODULE("单模块"),
    /**
     * 单模块工程，不按照功能模块分包，适合不带表名前缀的工程
     */
    MODULE_WITH_LEVEL("三层架构分模块"),
    /**
     * 表名按照功能增加前缀，按照功能进行模块划分
     */
    MODULE_WITH_FEATURE("按照功能分模块");


    /**
     * 描述
     */
    private final String describe;

    StructureTypeEnum(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }
}
