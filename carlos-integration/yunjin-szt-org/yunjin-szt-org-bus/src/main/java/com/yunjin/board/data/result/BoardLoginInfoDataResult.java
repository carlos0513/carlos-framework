package com.yunjin.board.data.result;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
public class BoardLoginInfoDataResult extends BoardDataResult {

    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 用户账号
     */
    private String account;
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 部门名称
     */
    private String deptName;
}
