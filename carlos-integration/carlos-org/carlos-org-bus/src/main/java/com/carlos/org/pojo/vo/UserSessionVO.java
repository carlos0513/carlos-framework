package com.carlos.org.pojo.vo;

import com.carlos.json.jackson.annotation.EnumField;
import com.carlos.org.pojo.enums.UserGenderEnum;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    @Schema(description = "主键")
    private String id;
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "详细地址")
    private String address;
    @EnumField(type = EnumField.SerializerType.FULL)
    @Schema(description = "性别，0：保密, 1：男，2：女，默认0")
    private UserGenderEnum gender;
    @Schema(description = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
    @Schema(description = "是否是机构管理员")
    private Boolean admin;
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLogin;
    @Schema(description = "登录次数")
    private Long loginCount;
    @Schema(description = "创建者")
    private String createBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改者")
    private String updateBy;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
    @Schema(description = "部门名称")
    private String department;
    @Schema(description = "部门id")
    private DepartmentBaseVO departmentInfo;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "用户菜单")
    private List<MenuVO> menus;
    @Schema(description = "角色id列表")
    private Set<String> roleIds;
    @Schema(description = "所属部门信息")
    private List<DepartmentVO> departments;
    @Schema(description = "头像ID")
    private String head;
    @Schema(description = "介绍")
    private String description;
    @Schema(description = "邮箱")
    private String email;

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class MenuVO implements Serializable {

        private static final long serialVersionUID = 1L;
        @Schema(description = "父级ID")
        private Long parentId;
        @Schema(description = "ID")
        private String id;
        @Schema(description = "controller名称")
        private String title;
        @Schema(description = "前端路由")
        private String path;
        @Schema(description = "前端名称")
        private String name;
        @Schema(description = "前端图标")
        private String icon;
        @Schema(description = "url")
        private String url;
        @Schema(description = "菜单配置")
        private Map<String, Object> meta;
        @Schema(description = "目标组件")
        private String component;
        @Schema(description = "菜单级数")
        private Integer level;
        @Schema(description = "显示和隐藏，0：显示，1：隐藏")
        private Boolean hidden;
        @Schema(description = "子集")
        private List<MenuVO> children;
        @Schema(description = "菜单状态，0：禁用，1：启用")
        private Boolean state;
        /**
         * 该字段为前端需要
         */
        private Set<String> roles;
    }

}
