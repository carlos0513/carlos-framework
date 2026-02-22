package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 部门 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "上级id")
    private String parentId;
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "部门编号")
    private String deptCode;
    @Schema(description = "部门排序")
    private int sort;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "部门层级")
    private String departmentLevelCode;
    @Schema(description = "部门区域")
    private String regionCode;
    @Schema(description = "组织机构类型")
    private String departmentType;
    @Schema(description = "子部门信息")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<DepartmentTreeVO> children;

    @Schema(description = "用户信息")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<User> users;

    @Schema(description = "目标系统编码")
    private String targetCode;

    @Schema(description = "第三方部门id")
    private String thirdDeptId;

    @Schema(description = "部门管理员id")
    private String operatorId;

    @Schema(description = "子级数目")
    private Integer childNum;


    @Data
    public static class User {
        @Schema(description = "主键")
        private String id;
        @Schema(description = "用户id")
        private String userId;
        @Schema(description = "用户名")
        private String account;
        @Schema(description = "姓名")
        private String name;
        @Schema(description = "真实姓名")
        private String realname;
        @Schema(description = "用户排序")
        private int sort;
        @Schema(description = "创建时间")
        private LocalDateTime createTime;
        @Schema(description = "部门层级")
        private String departmentLevelCode;
    }
}
