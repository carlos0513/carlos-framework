package com.carlos.org.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户岗位关联 DTO
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
public class UserPositionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 是否主岗位
     */
    private Boolean main;
}
