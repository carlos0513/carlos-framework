package com.carlos.org.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门 数据传输对象，service和manager向外传输对象
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
public class DepartmentDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 上级名称
     */
    private String parentDeptName;

    /**
     * 全部上级名称
     */
    private String fullDeptName;
    /**
     * 部门编号
     */
    private String deptCode;
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
    private Long parentId;
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
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 修改者
     */
    private Long updateBy;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 子部门信息
     */
    private List<DepartmentDTO> children;

    /**
     * 用户信息
     */
    private List<UserDepartmentDTO> users;

    /** 子级数目 */
    private Integer childNum;

}
