package com.carlos.org.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户导出查询条件
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
public class UserExportQueryDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建开始时间
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建结束时间
     */
    private LocalDateTime createTimeEnd;
}
