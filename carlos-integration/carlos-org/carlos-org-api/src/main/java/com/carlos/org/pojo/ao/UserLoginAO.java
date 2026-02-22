package com.carlos.org.pojo.ao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 系统用户 数据传输对象，service和manager向外传输对象
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
public class UserLoginAO {

    /**
     * 主键
     */
    private String id;
    /**
     * 用户名
     */
    private String account;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 证件号码
     */
    private String identify;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 行政区域编码
     */
    private String regionCode;
    /**
     * 是否是机构管理员
     */
    private Boolean admin;
    /**
     * 角色id列表 todo  userutil
     */
    private Set<String> roleIds;
    /**
     * 部门信息
     */
    private Department department;
    /**
     * 角色信息
     */
    private String roleId;
    /**
     * 角色名称，多个角色以英文逗号分割
     */
    private String roleName;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLogin;

    @Data
    @Accessors(chain = true)
    public static class Department {

        /**
         * 主键
         */
        private String id;
        /**
         * 父id
         */
        private String parentId;
        /**
         * 部门名称
         */
        private String deptName;
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
         * 备注
         */
        private String description;
        /**
         * 区域编码
         */
        private String regionCode;
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
         * 部门层级
         */
        private Integer level;
        /**
         * 组织机构类型
         */
        private String departmentType;
        /**
         * 组织机构层级类型
         */
        private String departmentLevelCode;

    }

    @Data
    @Accessors(chain = true)
    public static class Role {

        /**
         * 角色id
         */
        private String roleId;
        /**
         * 角色名称
         */
        private String roleName;
        /**
         * 角色名称
         */
        private String roleCode;
    }


}
