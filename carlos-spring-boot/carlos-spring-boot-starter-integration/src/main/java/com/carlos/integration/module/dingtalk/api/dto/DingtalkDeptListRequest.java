package com.carlos.integration.module.dingtalk.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 获取子部门列表请�?
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DingtalkDeptListRequest extends DingtalkBaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 父部门ID（填1表示根部门）
     */
    private Long deptId = 1L;

    /**
     * 语言
     */
    private String language = "zh_CN";
}

