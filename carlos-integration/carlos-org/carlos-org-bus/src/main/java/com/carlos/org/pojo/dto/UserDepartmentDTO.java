package com.carlos.org.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户部门关联 DTO
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
public class UserDepartmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 是否主部门
     */
    private Boolean main;
}
