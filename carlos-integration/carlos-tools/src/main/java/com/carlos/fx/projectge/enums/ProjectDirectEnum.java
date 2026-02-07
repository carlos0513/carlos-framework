package com.carlos.fx.projectge.enums;

/**
 * <p>
 * 指令枚举
 * </p>
 *
 * @author Carlos
 * @date 2020/8/18 12:09
 * @since 3.0
 */
public enum DirectEnum {
    /**
     * 表示需要循环生成文件
     */
    ArtifactId("[ArtifactId]", "ArtifactId"),
    GroupId("[GroupId]", "GroupId"),
    CAMEL_NAME("[CamelName]", "项目名驼峰"),
    UNDERLINE_NAME("[UnderLineName]", "项目名下划线"),

    ;

    /**
     * 指令值
     */
    private final String value;

    /**
     * 描述
     */
    private final String describe;

    DirectEnum(String value, String describe) {
        this.value = value;
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

    public String getValue() {
        return value;
    }
}
