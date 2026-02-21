package com.yunjin.board.data.result;


import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 看板数据
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:09
 */
@Data
public class BoardDataResult implements Serializable {

    /**
     * 数据key
     */
    private String key;

    /**
     * 耗时
     */
    private String take;

    /**
     * 异常信息
     */
    private String errMsg;
}
