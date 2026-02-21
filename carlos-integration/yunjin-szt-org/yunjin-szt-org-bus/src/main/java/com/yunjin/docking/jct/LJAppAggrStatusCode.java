package com.yunjin.docking.jct;

import lombok.Getter;

/**
 * <p>
 * 市级任务状态码
 * </p>
 *
 * @author Carlos
 * @date 2023/9/27 13:46
 */
@Getter
public enum LJAppAggrStatusCode {

    /**
     * 操作成功
     */
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    /**
     * 不是授权对象
     */
    NOT_PERMISSION_USER(403, "不是授权对象");

    private final int code;

    private final String message;

    LJAppAggrStatusCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据状态码获取状态码枚举对象
     *
     * @param code 状态码
     * @author yunjin
     * @date 2020/6/30 13:50
     */
    public static LJAppAggrStatusCode ofCode(int code) {
        LJAppAggrStatusCode[] ecs = LJAppAggrStatusCode.values();
        for (LJAppAggrStatusCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }

}
