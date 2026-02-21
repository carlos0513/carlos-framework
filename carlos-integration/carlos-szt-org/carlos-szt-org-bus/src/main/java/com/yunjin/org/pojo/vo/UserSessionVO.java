package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.json.jackson.annotation.EnumField;
import com.yunjin.org.pojo.enums.UserGenderEnum;
import com.yunjin.org.pojo.enums.UserStateEnum;
import com.yunjin.resource.pojo.vo.ResourceGroupVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统用户 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserSessionVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "用户名")
    private String account;
    @Schema(value = "真实姓名")
    private String realname;
    @Schema(value = "手机号码")
    private String phone;
    @Schema(value = "详细地址")
    private String address;
    @EnumField(type = EnumField.SerializerType.FULL)
    @Schema(value = "性别，0：保密, 1：男，2：女，默认0")
    private UserGenderEnum gender;
    @Schema(value = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
    @Schema(value = "是否是机构管理员")
    private Boolean admin;
    @Schema(value = "最后登录时间")
    private LocalDateTime lastLogin;
    @Schema(value = "登录次数")
    private Long loginCount;
    @Schema(value = "创建者")
    private String createBy;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "修改者")
    private String updateBy;
    @Schema(value = "修改时间")
    private LocalDateTime updateTime;
    @Schema("部门名称")
    private String department;
    @Schema(value = "部门id")
    private DepartmentBaseVO departmentInfo;
    @Schema(value = "角色名称")
    private String roleName;
    @Schema(value = "用户菜单")
    private List<MenuVO> menus;
    @Schema(value = "角色id列表")
    private Set<String> roleIds;
    @Schema(value = "所属部门信息")
    private List<DepartmentVO> departments;
    @Schema(value = "头像ID")
    private String head;
    @Schema(value = "介绍")
    private String description;
    @Schema(value = "邮箱")
    private String email;
    @Schema(value = "角色资源列表")
    private List<RoleResourceGroupItemVO> roleResources;
    @Schema(value = "角色权限组")
    private List<ResourceGroupVO> resourceGroup;

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class MenuVO implements Serializable {

        private static final long serialVersionUID = 1L;
        @Schema(value = "父级ID")
        private Long parentId;
        @Schema(value = "ID")
        private String id;
        @Schema(value = "controller名称")
        private String title;
        @Schema(value = "前端路由")
        private String path;
        @Schema(value = "前端名称")
        private String name;
        @Schema(value = "前端图标")
        private String icon;
        @Schema(value = "url")
        private String url;
        @Schema(value = "菜单配置")
        private Map<String, Object> meta;
        @Schema(value = "目标组件")
        private String component;
        @Schema(value = "菜单级数")
        private Integer level;
        @Schema(value = "显示和隐藏，0：显示，1：隐藏")
        private Boolean hidden;
        @Schema(value = "子集")
        private List<MenuVO> children;
        @Schema(value = "菜单状态，0：禁用，1：启用")
        private Boolean state;
        /**
         * 该字段为前端需要
         */
        private Set<String> roles;
    }

}
