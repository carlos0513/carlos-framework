package com.carlos.org.pojo.ao;

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
public class RoleAO {

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
    private Set<String> userIds;

    /**
     * 菜单id
     */
    private Set<String> menuIds;
    /**
     * 表单权限
     */
    private List<PermissionAO> permissions;

    /**
     * 用户列表
     */
    private List<UserInfo> userList;

    /**
     * 表单权限列表
     */
    private List<FormPermission> permissionList;


    @Data
    @Accessors(chain = true)
    public static class PermissionAO implements Serializable {

        private static final long serialVersionUID = 1L;
        /**
         * 表id
         */
        private String tableId;
        /**
         * 操作类型ids
         */
        private Set<String> operateIds;

    }

    @Data
    @Accessors(chain = true)
    public static class UserInfo implements Serializable {

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
        /**
         * 创建者
         */
        private Long createBy;
        /**
         * 创建时间
         */
        private LocalDateTime createTime;
    }

    @Data
    @Accessors(chain = true)
    public static class FormPermission implements Serializable {
        /**
         * 表单列表
         */
        FormTable table;
        /**
         * 操作集合
         */
        Set<String> operates;
    }

    @Data
    @Accessors(chain = true)
    public static class FormTable implements Serializable {
        /**
         * 主键ID
         */
        private Long id;
        /**
         * 数据表名
         */
        private String tableName;
        /**
         * 数据表中文名
         */
        private String tableLabel;
    }
}
