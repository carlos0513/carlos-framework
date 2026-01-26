package com.yunjin.tool.encrypt.enums;

/**
 * <p>
 * 工具类型
 * </p>
 *
 * @author Carlos
 * @date 2020/8/18 12:09
 * @since 3.0
 */
public enum ToolType {
    /**
     * 单模块工程
     */
    SM4_ENCRYPT("sm4加密"),
    SM4_DECRYPT("sm4解密"),
    ;


    /**
     * 描述
     */
    private final String desc;

    ToolType(String describe) {
        this.desc = describe;
    }

    public String getDesc() {
        return desc;
    }
}
