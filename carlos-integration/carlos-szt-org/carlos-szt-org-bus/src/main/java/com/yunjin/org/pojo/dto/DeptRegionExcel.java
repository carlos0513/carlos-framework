package com.yunjin.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户导入 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Data
@Accessors(chain = true)
public class DeptRegionExcel implements Serializable {

    /**
     * 部门id
     */
    private Serializable id;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门编号
     */
    private String deptCode;
    /**
     * 行政区域编码
     */
    private String sourceRegionCode;
    /**
     * 行政区域编码
     */
    private String sourceRegionName;

    /**
     * 行政区域编码
     */
    private String targetRegionCode;
    /**
     * 行政区域编码
     */
    private String targetRegionName;
    /**
     * 处理备注
     */
    private String dealRemark;
    /**
     * 是否处理
     */
    private Boolean deal;
    /**
     * 处理信息
     */
    private String message;
    /**
     * 回滚sql
     */
    private String rollbackSql;

}
