package com.carlos.org.pojo.dto;

import com.carlos.org.pojo.param.UserDeptRoleDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 系统角色 数据传输对象，service和manager向外传输对象
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
public class RoleDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色唯一编码
     */
    private String code;
    /**
     * 角色状态， 禁用， 启用
     */
    private String state;
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
     * 用户id
     */
    private List<UserDeptRoleDTO> userDeptRoles;

    /**
     * 菜单id
     */
    private Set<String> menuIds;


    /**
     * 组织机构层级
     */
    private Set<String> departmentTypes;

    /**
     * 用户列表
     */
    private List<UserInfo> userList;
    /**
     * 机构列表
     */
    private List<DepartmentDTO> departmentList;
    /**
     * 资源组id
     */
    private String resourceGroupId;

    @Data
    @Accessors(chain = true)
    public static class UserInfo implements Serializable {

        private static final long serialVersionUID = 3403139329432080440L;
        /**
         * 主键
         */
        private Long id;
        /**
         * 用户名
         */
        private String account;
        /**
         * 真实姓名
         */
        private String realname;
        /**
         * 电话号码
         */
        private String phone;
        /**
         * 部门名称
         */
        private String departmentName;
        private String departmentId;
        private String departmentLevelCode;
        /**
         * 创建者
         */
        private Long createBy;
        /**
         * 创建时间
         */
        private LocalDateTime createTime;
    }
}
