package com.carlos.org.pojo.dto;

import com.carlos.org.pojo.enums.UserGenderEnum;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.carlos.org.pojo.param.UserDeptRoleDTO;
import com.carlos.system.pojo.ao.SysRegionAO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 系统用户 数据传输对象，service和manager向外传输对象
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
public class UserDTO {

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
     * 密码
     */
    private String pwd;
    /**
     * 确认密码
     */
    private String confirmPwd;
    /**
     * 盐值
     */
    private String salt;
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
     * 行政区域
     */
    private SysRegionAO region;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 性别，0：保密, 1：男，2：女，默认0
     */
    private UserGenderEnum gender;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String head;
    /**
     * 电子签名
     */
    private String signature;
    /**
     * 备注
     */
    private String description;
    /**
     * 状态，0：禁用，1：启用，2：锁定
     */
    private UserStateEnum state;
    /**
     * 是否是机构管理员
     */
    private Boolean admin;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLogin;
    /**
     * 登录次数
     */
    private Long loginCount;
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
     * 角色id列表
     */
    private Set<String> roleIds;
    private Set<String> departmentIds;
    private List<UserDeptRoleDTO> deptRoles;

    private String department;
    private Long departmentId;
    private String departmentName;
    private String departmentLevelCode;
    /**
     * 所选部门信息
     */
    private DepartmentDTO departmentInfo;
    /**
     * 用户菜单信息
     */
    private List<UserMenuDTO> menus;
    /**
     * 所属部门信息
     */
    private List<DepartmentDTO> departments;
    /**
     * 部门完整名称
     */
    private List<String> deptFullNames;

    /**
     * 所拥有角色信息
     */
    private RoleDTO role;

    /**
     * 用户可选角色
     */
    private List<UserRoleDTO> roles;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名称，多个角色以英文逗号分割
     */
    private String roleName;

    /**
     * 用户排序
     */
    private int sort;

    /**
     * 密码创建时间
     */
    private LocalDateTime passwordCreateTime;

}
