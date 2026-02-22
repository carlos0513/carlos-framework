package com.carlos.org.pojo.ao;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门 AO对象
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
public class DepartmentAO {

    /**
     * 主键
     */
    private String id;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 上级名称
     */
    private String parentDeptName;
    /**
     * 部门编号
     */
    private String deptCode;
    /**
     * 行政区域编码
     */
    private String regionCode;
    /**
     * 管理员id
     */
    private String adminId;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 联系方式
     */
    private String tel;
    /**
     * 父id
     */
    private String parentId;
    /**
     * 部门层级
     */
    private Integer level;
    /**
     * 状态，0：禁用，1：启用
     */
    private String state;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String description;
    /**
     * 版本
     */
    private Long version;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改者
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 子部门信息
     */
    private List<DepartmentAO> children;

    /**
     * 用户信息
     */
    private List<UserDepartmentAO> users;
    /**
     * 机构类型对应层级
     */
    private String departmentLevelCode;
    /*
     * 组织机构类型
     */
    private String departmentType;
}
