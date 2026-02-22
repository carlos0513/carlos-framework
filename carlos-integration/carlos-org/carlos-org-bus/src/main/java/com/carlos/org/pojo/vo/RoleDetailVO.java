package com.carlos.org.pojo.vo;

import com.carlos.json.jackson.annotation.UserIdField;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统角色详情 显示层对象，向页面传输的对象
 * </p>
 *
 * @date 2023-4-1 10:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "用户id")
    private Set<String> userIds;

    @Schema(description = "用户列表")
    private Set<UserInfo> userList;

    @Schema(description = "表单权限列表")
    private Set<FormPermission> permissionList;

    @Schema(description = "组织机构列表")
    private List<DepartmentDTO> departmentList;

    @Schema(description = "资源组id")
    private String resourceGroupId;

    @Schema(description = "组织机构")
    private Set<String> departmentTypes;

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class UserInfo implements Serializable {

        private static final long serialVersionUID = 1L;
        @Schema(description = "主键")
        private String id;
        @Schema(description = "用户名")
        private String account;
        @Schema(description = "真实姓名")
        private String realname;
        @Schema(description = "电话号码")
        private String phone;
        @Schema(description = "部门名称")
        private String departmentName;
        @Schema(description = "部门id")
        private String departmentId;
        @Schema(description = "部门层级")
        private String departmentLevelCode;
        @Schema(description = "创建者")
        @UserIdField(type = UserIdField.SerializerType.REALNAME)
        private String createBy;
        @Schema(description = "创建时间")
        private LocalDateTime createTime;
    }

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class FormPermission implements Serializable {
        @Schema(description = "表单列表")
        FormTable table;
        @Schema(description = "操作集合")
        Set<String> operates;
    }

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class FormTable implements Serializable {
        @Schema(description = "主键ID")
        private String id;
        @Schema(description = "数据表名")
        private String tableName;
        @Schema(description = "数据表中文名")
        private String tableLabel;
    }


}