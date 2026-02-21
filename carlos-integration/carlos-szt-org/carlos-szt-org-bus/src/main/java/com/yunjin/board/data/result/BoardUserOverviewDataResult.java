package com.yunjin.board.data.result;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 看板数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@Data
@Accessors(chain = true)
public class BoardUserOverviewDataResult extends BoardDataResult {
    /**
     * 注册用户数
     */
    private int registerCount;
    /**
     * 禁用用户数
     */
    private int disableCount;
    /**
     * 活跃用户数
     */
    private int activeCount;
    /**
     * PC端活跃用户数
     */
    private int pcActiveCount;
    /**
     * 移动端活跃用户数
     */
    private int mobileActiveCount;
    /**
     * 活跃用户数V2(PC端活跃用户数+移动端活跃用户数)
     */
    private int activeCountV2;
    /**
     * 近一年注册用户数
     */
    private int registerCountInOneYear;
}
